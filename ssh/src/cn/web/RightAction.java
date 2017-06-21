package cn.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.security.Right;
import cn.service.RightService;

@Controller
@Scope("prototype")
public class RightAction extends BaseAction<Right> {

	private static final long serialVersionUID = 708041979454147429L;
	
	@Resource(name="rightService")
	private RightService rightService;
	
	private List<Right> allRights;
	
	public List<Right> getAllRights() {
		return allRights;
	}

	public void setAllRights(List<Right> allRights) {
		this.allRights = allRights;
	}
	
	private Integer rightId;
	
	public Integer getRightId() {
		return rightId;
	}

	public void setRightId(Integer rightId) {
		this.rightId = rightId;
	}

	/**
	 * 获取所有的权限
	 */
	public String findAllRights(){
		this.allRights = rightService.findAllEntity();
		return "rightListPage";
	}
	
	//转到添加权限页面
	public String toAddRightPage(){
		return "addRightPage";
	}
	
	//保存/更新权限
	public String saveOrUpdateRight(){
		rightService.saveOrUpdateRight(this.model);
		return "findAllRightsAction";
	}
	
	//转到修改权限页面
	public String editRight(){
		this.model = rightService.getEntityById(rightId);
		return "addRightPage";
	}
	
	public String deleteRight(){
		Right right = rightService.getEntityById(rightId);
		rightService.deleteRight(right);
		return "findAllRightsAction";
	}
	
	/**
	 * 批量保存
	 * @return
	 */
	public String batchUpdateRights(){
		rightService.updateRights(this.allRights);
		return "findAllRightsAction";
	}
}
