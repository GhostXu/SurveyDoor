package cn.util;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.entity.User;
import cn.entity.security.Right;
import cn.service.UserService;
import cn.web.BaseAction;
import cn.web.LoginAction;
import cn.web.UserAction;
import cn.web.UserAware;

public abstract class ValidateUtil {
	
	public static boolean regList(Collection col){
		if(col.size() == 0 || col.isEmpty()){
			return false;
		}
		return true;
	}
	
	public static boolean isValid(String str){
		if("".equals(str) || str == null){
			return false;
		}
		return true;
	}

	public static boolean contains(String[] oldValues, String value) {
		for(String v : oldValues){
			if(v.equals(value)){
				return true;
			}
		}
		return false;
	}

	public static boolean isValid(Object[] arr) {
		if(arr == null || arr.length == 0){
			return false;
		}
		return true;
	}
	 
	/**
	 * 判断是否有权限
	 * @param ns 名字空间
	 * @param actionName action名称 LoginAction_doLogin
	 * @param request
	 * @param action 
	 * @return
	 */
	public static boolean hasRight(String ns, String actionName,
			HttpServletRequest request, BaseAction action) {
		if(!ValidateUtil.isValid(ns) || "/".equals(ns)){
			ns = "" ;
		}
		//处理连接中包含的参数
		if(actionName.contains("?")){
			actionName = actionName.substring(0, actionName.indexOf("?"));
		}
		/**
		 * all_rights_map中获取的权限名称为首字母大写，actionName为首字母小写
		 */
		if(!"".equals(actionName) && actionName != null){
			actionName = actionName.substring(0, 1).toUpperCase() + actionName.substring(1);
		}
		String url = ns + "/" + actionName ;//获取url
		/**
		 * 这里获取权限的方法：是通过监听器，把权限集合放到application中，在服务器启动之前容器初始化之后加载集合并把集合放入到application中。
		 */
		ServletContext sc = request.getSession().getServletContext();
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		Right right = map.get(url);//获取到权限
		
		if(right == null || right.isCommon() ){
			return true;
		}else{
			if(action instanceof LoginAction || action instanceof UserAction){
				return true;
			}else{
				User user = (User) request.getSession().getAttribute("user");
				//是否登录
				if(user == null){
					return false;
				}else{
					if(action != null && action instanceof UserAware){
						((UserAware) action).setUser(user);//注入user
					}
					//是否是超级管理员
					if(user.isSuperAdmin()){
						return true;
					}else{
						//是否有权限
						if(user.getRight(right)){
							return true;
						}else{
							return false;
						}
					}
				}
			}
		}
	}
}
