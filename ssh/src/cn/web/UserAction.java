package cn.web;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.User;
import cn.service.UserService;
import cn.util.DataUtils;
import cn.util.ValidateUtil;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{
	@Resource(name="userService")
	private UserService userService;
	//ȷ������
	private String confirmPassword;
	
	public void setConfirmPassword(String confirmPassword){
		this.confirmPassword = confirmPassword;
	}
	/**
	 * ���뵽ע��ҳ��
	 * @return
	 */
	@SkipValidation
	public String goRegist(){
		return "register";
	}
	
	/**
	 * ��ע����Ϣ����У��
	 */
	public void validate(){
		//�ǿ�У��
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email����Ϊ�գ�");
		}else if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "password����Ϊ�գ�");
		}else if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName����Ϊ�գ�");
		}
		//�����Ƿ�ռ��
		if(userService.regEmail(model.getEmail())){
			addFieldError("email", "���䱻ռ�ã�");
		}
		if(this.hasErrors()){
			return ;
		}
		//����һ����
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("confirmPassword", "���벻һ�£�");
			return ;
		}
	}
	/**
	 * �����ݿ����������
	 * @return
	 */
	public String doReg(){
		model.setReDate(new Date());
		model.setPassword(DataUtils.md5(model.getPassword()));
		userService.insert(model);
		return SUCCESS;
	}
}
