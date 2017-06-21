package cn.web;

import org.apache.struts2.ServletActionContext;

import cn.entity.User;
import cn.entity.security.Right;
import cn.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class RightFilterInterceptor extends AbstractInterceptor{

	private static final long serialVersionUID = 1287728761324709017L;

	public String intercept(ActionInvocation invocation) throws Exception {
		BaseAction action = (BaseAction) invocation.getAction();
		
		ActionProxy proxy = invocation.getProxy();
		String ns = proxy.getNamespace();
		String actionName = proxy.getActionName();
		if(ValidateUtil.hasRight(ns, actionName, ServletActionContext.getRequest(), action)){
			return invocation.invoke();
		}
		else{
			return "no_right_error" ;
		}
	}
}
