package cn.service;

import java.util.List;
import java.util.Set;

import cn.entity.security.Right;

public interface RightService extends BaseService<Right>{

	public void saveOrUpdateRight(Right model);

	public void deleteRight(Right right);

	public void appendRightByUrl(String url);

	public void updateRights(List<Right> allRights);

	public List<Right> findRightsNoInRange(Set<Right> rights);

	public List<Right> findRightsInRange(Integer[] ownRightIds);

	/**
	 * ��ȡ���Ȩ��λ
	 * @return
	 */
	public int getMaxRightPos();

}
