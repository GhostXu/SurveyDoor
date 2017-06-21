package cn.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dao.BaseDao;
import cn.entity.security.Right;
import cn.service.RightService;
import cn.util.DataUtils;
import cn.util.ValidateUtil;

@Service(value="rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements RightService{
	
	@Resource(name="rightDao")
	public void setBaseDao(BaseDao<Right> rightDao) {
		super.setBaseDao(rightDao);
	}
	
	/**
	 * 保存或更新权限
	 */
	public void saveOrUpdateRight(Right right){
		//需要手动设置rightPos、rightCode的值
		if(right.getId() == null){//保存权限
			String hql = "select max(r.rightPos),max(r.rightCode) from Right r where r.rightPos = "
					+ "(select max(rr.rightPos) from Right rr) order by r.rightPos desc, r.rightCode desc";
			Object[] topObjects = (Object[]) this.uniqueResult(hql);
			Integer topRightPos = (Integer) topObjects[0];
			Long topRightcode =  (Long) topObjects[1];
			
			if(topRightPos == null){
				topRightPos = 0 ;
				topRightcode = 1L ;
			}else{
				if(topRightcode >= (1L << 60)){
					topRightPos = topRightPos +1;
					topRightcode = 1L ;
				}else{
					topRightcode = topRightcode << 1;
				}
			}
			
			right.setRightPos(topRightPos);
			right.setRightCode(topRightcode);
		}
		saveOrUpdateEntity(right);
	}
	
	public void deleteRight(Right right){
		this.deleteEntity(right);
	}
	
	public void appendRightByUrl(String url){
		String hql = " from Right r where r.rightUrl = ?";
		List<Right> rights = this.getEntityByHql(hql, url);
		if(rights.size() == 0){
			Right right = new Right();
			right.setRightUrl(url);
			saveOrUpdateRight(right);
		}
	}
	
	/**
	 * 批量更新权限
	 */
	public void updateRights(List<Right> allRights){
		String hql = "update Right r set r.rightName = ?,r.common = ? where r.id = ?";
		for(Right r : allRights){
			this.batchEntityByHQL(hql, r.getRightName(), r.isCommon(), r.getId());
		}
	}
	
	/**
	 * 获取范围内的权限
	 */
	public List<Right> findRightsInRange(Integer[] ownRightIds){
		if(ValidateUtil.isValid(ownRightIds)){
			String hql = " from Right r where r.id in (" + DataUtils.strList2str(ownRightIds) + ")";
			return this.getEntityByHql(hql);
		}else{
			return null;
		}
	}
	/**
	 * 获取不在范围内的权限
	 */
	public List<Right> findRightsNoInRange(Set<Right> rights){
		if(!ValidateUtil.regList(rights)){
			return this.findAllEntity();
		}else{
			String hql = "from Right r where r.id not in (" + DataUtils.extractEntityId(rights) + ")";
			return getEntityByHql(hql);
		}
		
	}
	
	/**
	 * 获取最大权限位
	 * @return
	 */
	public int getMaxRightPos(){
		String hql = "select max(r.rightPos) from Right r";
		return (int) uniqueResult(hql);
	}
}
