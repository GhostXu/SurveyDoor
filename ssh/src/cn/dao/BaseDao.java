package cn.dao;

import java.util.Date;
import java.util.List;

import cn.entity.Log;

public interface BaseDao<T> {
	//insert
	public void insert(T t);
	//delete
	public void deleteEntityByIds(List<Integer> ids);
	public void deleteEntity(T t);
	//update
	public void updateEntityByIds(List<Integer> ids);
	public void saveOrUpdateEntity(T t);
	//select
	public T getEntityById(Integer id);
	public List<T> getEntityByIds(List<Integer> ids);
	public List<T> findEntityByHql(String hql,Object...objects );
	public void batchEntityByHQL(String hql, Object... objects);
	public void excuteSql(String sql, Object... objects);
	//获取单一结果
	public Object uniqueResult(String hql, Object...objects);
	public List<T> findEntitiesBySql(String sql, Object...objects) ;
	
}
