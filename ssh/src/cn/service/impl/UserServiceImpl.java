package cn.service.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.User;
import cn.entity.security.Role;
import cn.service.RoleService;
import cn.service.UserService;
import cn.util.ValidateUtil;

@Service(value="userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	@Resource(name="userDao")
	public void setBaseDao(BaseDao<User> userDao) {
		super.setBaseDao(userDao);
	}

	@Resource(name="roleService")
	private RoleService roleService;
	@Override
	public boolean regEmail(String email) {
		String hql = " from User where email = ?";
		List<User> list = this.getEntityByHql(hql,email);
		return ValidateUtil.regList(list);
	}

	@Override
	public User getEntityByParams(String email, String password) {
		String hql = " from User where email = ? and password = ?";
		List<User> list = this.getEntityByHql(hql,email,password);
		boolean b = ValidateUtil.regList(list);
		if(b){
			return list.get(0); 
		}
		return null;
	}
	
	/**
	 * 给用户授权
	 */
	public void updateAuthorize(User model, Integer[] ownRoleIds){
		User user = this.getEntityById(model.getUid());
		if(ValidateUtil.isValid(ownRoleIds)){
			List<Role> roles = roleService.findRoleInRange(ownRoleIds);
			user.setRoles(new HashSet<Role>(roles));
		}else{
			user.getRoles().clear();
		}
		
	}

	@Override
	public void clearAuthorize(Integer userId) {
		User user = this.getEntityById(userId);
		user.getRoles().clear();
	}
}
