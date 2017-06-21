package cn.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.User;
import cn.entity.security.Role;
import cn.service.RoleService;
import cn.service.UserService;

@Controller
@Scope("prototype")
public class UserAuthorizeAction extends BaseAction<User> {
	
	private static final long serialVersionUID = 6671760885945583948L;
	
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	private List<User> allUsers;
	
	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	private Integer userId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	private Integer[] ownRoleIds;

	public Integer[] getOwnRoleIds() {
		return ownRoleIds;
	}

	public void setOwnRoleIds(Integer[] ownRoleIds) {
		this.ownRoleIds = ownRoleIds;
	}
	
	private List<Role> noOwnRoles;

	public List<Role> getNoOwnRoles() {
		return noOwnRoles;
	}

	public void setNoOwnRoles(List<Role> noOwnRoles) {
		this.noOwnRoles = noOwnRoles;
	}

	/**
	 * 所有用户
	 * @return
	 */
	public String findAllUsers(){
		this.allUsers = userService.findAllEntity();
		return "userAuthorizeListPage";
	}
	
	/**
	 * 用户授权页面
	 * @return
	 */
	public String editAuthorize(){
		this.model = userService.getEntityById(userId);
		noOwnRoles = roleService.findRoleNoInRange(model.getRoles());
		return "userAuthorizePage";
	}
	
	/**
	 * 授权
	 * @return
	 */
	public String updateAuthorize(){
		userService.updateAuthorize(model, ownRoleIds);
		return "findAllUsersAction";
	}
	
	/**
	 * 清除用户授权
	 * @return
	 */
	public String clearAuthorize(){
		userService.clearAuthorize(userId);
		return "findAllUsersAction";
	}
}
