package cn.service.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import cn.dao.BaseDao;
import cn.service.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
	
	private BaseDao<T> baseDao;
	@Resource
	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}
	
	private Class<T> classz;
	
	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		this.classz = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	@Override
	public void insert(T t) {
		baseDao.insert(t);
	}

	@Override
	public void deleteEntityByIds(List<Integer> ids) {
		baseDao.deleteEntityByIds(ids);
	}
	
	public void deleteEntity(T t){
		baseDao.deleteEntity(t);
	}

	@Override
	public void updateEntityByIds(List<Integer> ids) {
		baseDao.updateEntityByIds(ids);
	}
	
	public void saveOrUpdateEntity(T t){
		this.baseDao.saveOrUpdateEntity(t);
	}

	@Override
	public List<T> getEntityByIds(List<Integer> ids) {
		return baseDao.getEntityByIds(ids);
	}
	
	public List<T> getEntityByHql(String hql, Object... objects){
		return baseDao.findEntityByHql(hql,objects);
	}

	@Override
	public void batchEntityByHQL(String hql, Object... objects) {
		baseDao.batchEntityByHQL(hql, objects);
	}
	
	public void saveEntityBySql(String sql, Object... objects){
		baseDao.excuteSql(sql, objects);
	}

	public T getEntityById(Integer id){
		return baseDao.getEntityById(id);
	}
	
	//获取单一结果
	public Object uniqueResult(String hql, Object...objects){
		return baseDao.uniqueResult(hql, objects);
	}
	
	//查询所有实体
	public List<T> findAllEntity(){
		String hql = "from "+ classz.getSimpleName();
		return this.getEntityByHql(hql);
	}
	
	//按照sql查询实体
	public List<T> findEntitiesBySql(String sql, Object...objects){
		return baseDao.findEntitiesBySql(sql, objects);
	}
}
