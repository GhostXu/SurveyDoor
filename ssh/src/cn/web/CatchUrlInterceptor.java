package cn.web;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.service.RightService;
import cn.util.ValidateUtil;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class CatchUrlInterceptor implements Interceptor {
	public void destroy() {
	}

	public void init() {
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		String ns = proxy.getNamespace();//ÃüÃû¿Õ¼ä
		String actionName = proxy.getActionName();//actionÃû³Æ
		
		if(!ValidateUtil.isValid(ns) || "/".equals(ns)){
			ns = "";
		}
		String url = ns + "/" + actionName ;
		
		ServletContext sc = ServletActionContext.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		RightService rs = (RightService) ac.getBean("rightService");
		rs.appendRightByUrl(url);
		return invocation.invoke();
	}

}
