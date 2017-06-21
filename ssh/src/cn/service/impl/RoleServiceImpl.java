package cn.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.security.Right;
import cn.entity.security.Role;
import cn.service.RightService;
import cn.service.RoleService;
import cn.util.DataUtils;
import cn.util.ValidateUtil;

@Service(value="roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
	@Resource(name="rightService")
	private RightService rightService;
	
	@Resource(name="roleDao")
	public void setBaseDao(BaseDao<Role> roleDao){
		super.setBaseDao(roleDao);
	}	
	
	public void saveOrUpdateRole(Role role, Integer[] ownRightIds){
		if(ValidateUtil.isValid(ownRightIds)){
			List<Right> rights = rightService.findRightsInRange(ownRightIds);
			role.setRights(new HashSet<Right>(rights));
		}
		this.saveOrUpdateEntity(role);
	}
	
	public Role getRole(Integer roleId){
		Role role = this.getEntityById(roleId);
		return role;
	}
	
	public void deleteLink(Integer roleId){
		String hql = "delete role_right_link where roleid = ?";
		this.batchEntityByHQL(hql, roleId);
	}
	
	/**
	 * 获取没有授权给用户id为userId用户的角色
	 */
	public List<Role> findRoleNoInRange(Set<Role> roles){
		if(!ValidateUtil.regList(roles)){
			return this.findAllEntity();
		}else{
			String hql = " from Role r where r.id not in (" + DataUtils.extractEntityId(roles) + ")";
			return this.getEntityByHql(hql);
		}
	}

	public List<Role> findRoleInRange(Integer[] ownRoleIds){
		String hql = " from Role r where r.id in (" + DataUtils.strList2str(ownRoleIds) + ")";
		return this.getEntityByHql(hql);
	}
}
