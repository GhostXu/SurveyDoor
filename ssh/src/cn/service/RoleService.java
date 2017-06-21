package cn.service;

import java.util.List;
import java.util.Set;

import cn.entity.security.Role;

public interface RoleService extends BaseService<Role>{

	public void saveOrUpdateRole(Role model, Integer[] ownRightIds);

	public Role getRole(Integer roleId);

	public void deleteLink(Integer roleId);

	public List<Role> findRoleNoInRange(Set<Role> roles);

	public List<Role> findRoleInRange(Integer[] ownRoleIds);


}
