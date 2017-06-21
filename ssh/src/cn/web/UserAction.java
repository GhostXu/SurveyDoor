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
	//确认密码
	private String confirmPassword;
	
	public void setConfirmPassword(String confirmPassword){
		this.confirmPassword = confirmPassword;
	}
	/**
	 * 进入到注册页面
	 * @return
	 */
	@SkipValidation
	public String goRegist(){
		return "register";
	}
	
	/**
	 * 对注册信息进行校验
	 */
	public void validate(){
		//非空校验
		if(!ValidateUtil.isValid(model.getEmail())){
			addFieldError("email", "email不能为空！");
		}else if(!ValidateUtil.isValid(model.getPassword())){
			addFieldError("password", "password不能为空！");
		}else if(!ValidateUtil.isValid(model.getNickName())){
			addFieldError("nickName", "nickName不能为空！");
		}
		//邮箱是否被占用
		if(userService.regEmail(model.getEmail())){
			addFieldError("email", "邮箱被占用！");
		}
		if(this.hasErrors()){
			return ;
		}
		//数据一致性
		if(!model.getPassword().equals(confirmPassword)){
			addFieldError("confirmPassword", "密码不一致！");
			return ;
		}
	}
	/**
	 * 向数据库中添加数据
	 * @return
	 */
	public String doReg(){
		model.setReDate(new Date());
		model.setPassword(DataUtils.md5(model.getPassword()));
		userService.insert(model);
		return SUCCESS;
	}
}
