package cn.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.dao.BaseDao;
import cn.entity.Log;

@SuppressWarnings("unchecked")
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	private Class<T> classz;

	public BaseDaoImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		classz = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	@Override
	public void insert(T t) {
		sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void deleteEntityByIds(List<Integer> ids) {
		List<T> tList = getEntityByIds(ids);
		for(int i=0 ; i<tList.size() ; i++){
			sessionFactory.getCurrentSession().delete(tList.get(i));
		}
	}
	
	public void deleteEntity(T t){
		sessionFactory.getCurrentSession().delete(t);
	}

	@Override
	public void updateEntityByIds(List<Integer> ids) {
		List<T> tList = getEntityByIds(ids);
		for(int i=0 ; i<tList.size() ; i++){
			sessionFactory.getCurrentSession().update(tList.get(i));
		}
	}
	
	public void saveOrUpdateEntity(T t){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(t);
	}

	@Override
	public List<T> getEntityByIds(List<Integer> ids) {
		List<T> tList = new ArrayList<T>();
		for (int i = 0; i < ids.size(); i++) {
			tList.add((T) sessionFactory.getCurrentSession().get(classz, ids.get(i)));
		}
		return tList;
	}
	
	public List<T> findEntityByHql(String hql, Object... objects){
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i=0 ; i<objects.length ; i++){
			query.setParameter(i, objects[i]);
		}
		List<T> tList = query.list();
		return tList;
	}

	@Override
	public void batchEntityByHQL(String hql, Object... objects) {
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		query.executeUpdate();
	}
	
	public void excuteSql(String sql, Object... objects){
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		query.executeUpdate();
	}
	
	@Override
	public T getEntityById(Integer id) {
		return (T) sessionFactory.getCurrentSession().get(classz, id);
	}
	
	public Object uniqueResult(String hql, Object...objects){
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < objects.length; i++){
			query.setParameter(i, objects[i]);
		}
		return query.uniqueResult();
	}

	public List<T> findEntitiesBySql(String sql, Object...objects) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		for(int i = 0; i < objects.length; i++){
			query.setParameter(i, objects[i]);
		}
		//添加实体类，将sql查询的集合中，将对象数组组装成实体对象集合
		query.addEntity(classz);
		return query.list();
	}
	
}
