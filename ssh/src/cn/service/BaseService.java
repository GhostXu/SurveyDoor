package cn.service;

import java.util.List;

public interface BaseService<T> {
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
	public List<T> getEntityByHql(String hql, Object... objects);
	public void batchEntityByHQL(String hql, Object... objects);
	public void saveEntityBySql(String sql, Object... objects);
	//获取单一结果
	public Object uniqueResult(String hql, Object...objects);
	
	public List<T> findAllEntity();
	
	public List<T> findEntitiesBySql(String sql, Object...objects);
}
