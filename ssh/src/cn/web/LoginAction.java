package cn.web;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.SessionAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.User;
import cn.service.RightService;
import cn.service.UserService;
import cn.util.DataUtils;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction<User> implements SessionAware{
	@Resource(name="userService")
	private UserService userService;
	
	@Resource(name="rightService")
	private RightService rightService;
	
	private Map<String, Object> sessionmaMap;
	
	public String toLoginPage(){
		return "toLoginPage";
	}
	/**
	 * ��½
	 * @return
	 */
	public String doLogin(){
		String email = model.getEmail();
		String password = model.getPassword();
		
		User user = userService.getEntityByParams(email,DataUtils.md5(password));
		
		if(user == null){
			addActionError("�����������󣡣�");
		}else{
			//��ʼ��Ȩ���ܺ�����
			int maxRightPos = rightService.getMaxRightPos();
			user.setRightSum(new long[maxRightPos + 1]);
			//�����û�Ȩ���ܺ�
			user.calculateRight();
			sessionmaMap.put("user", user);
		}
		if(this.hasErrors()){
			return "toLoginPage";
		}
		return SUCCESS;
	}
	/**
	 * ʵ��SessionAware�ӿڣ���дsetSession������ע��sessionmaMap
	 * 
	 * sessionMap����ʵ����session�м�������
	 */
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionmaMap = arg0;
	}
}
