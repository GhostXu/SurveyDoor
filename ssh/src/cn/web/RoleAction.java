package cn.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.security.Right;
import cn.entity.security.Role;
import cn.service.RightService;
import cn.service.RoleService;

@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	private static final long serialVersionUID = -804057588674128494L;
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	@Resource(name="rightService")
	private RightService rightService;
	
	private List<Role> allRoles;
	
	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	private List<Right> noOwnRights;
	
	public List<Right> getNoOwnRights() {
		return noOwnRights;
	}

	public void setNoOwnRights(List<Right> noOwnRights) {
		this.noOwnRights = noOwnRights;
	}

	private Integer[] ownRightIds;

	public Integer[] getOwnRightIds() {
		return ownRightIds;
	}

	public void setOwnRightIds(Integer[] ownRightIds) {
		this.ownRightIds = ownRightIds;
	}
	
	private Integer roleId;
	

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * ��ȡ���н�ɫ
	 * @return
	 */
	public String findAllRoles(){
		this.allRoles = roleService.findAllEntity();
		return "roleListPage";
	}
	
	/**
	 * ת����ӽ�ɫҳ��
	 * @return
	 */
	public String toAddRolePage(){
		this.noOwnRights = rightService.findAllEntity();
		return "addRolePage";
	}
	
	/**
	 * �������½�ɫ
	 * @return
	 */
	public String saveOrUpdateRole(){
		roleService.saveOrUpdateRole(this.model,ownRightIds);
		return "findAllRolesAction";
	}
	
	/**
	 * �ﵽ�༭��ɫҳ��
	 * @return
	 */
	public String editRole(){
		this.model = roleService.getRole(roleId);
		noOwnRights = rightService.findRightsNoInRange(model.getRights());
		return "editRolePage";
	}
	
	/**
	 * ɾ����ɫ
	 * @return
	 */
	public String deleteRole(){
		Role role = new Role();
		role.setId(roleId);
		roleService.deleteEntity(role);
		return "findAllRolesAction";
	}
}
