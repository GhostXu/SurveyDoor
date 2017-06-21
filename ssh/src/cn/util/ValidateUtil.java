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
	 * �ж��Ƿ���Ȩ��
	 * @param ns ���ֿռ�
	 * @param actionName action���� LoginAction_doLogin
	 * @param request
	 * @param action 
	 * @return
	 */
	public static boolean hasRight(String ns, String actionName,
			HttpServletRequest request, BaseAction action) {
		if(!ValidateUtil.isValid(ns) || "/".equals(ns)){
			ns = "" ;
		}
		//���������а����Ĳ���
		if(actionName.contains("?")){
			actionName = actionName.substring(0, actionName.indexOf("?"));
		}
		/**
		 * all_rights_map�л�ȡ��Ȩ������Ϊ����ĸ��д��actionNameΪ����ĸСд
		 */
		if(!"".equals(actionName) && actionName != null){
			actionName = actionName.substring(0, 1).toUpperCase() + actionName.substring(1);
		}
		String url = ns + "/" + actionName ;//��ȡurl
		/**
		 * �����ȡȨ�޵ķ�������ͨ������������Ȩ�޼��Ϸŵ�application�У��ڷ���������֮ǰ������ʼ��֮����ؼ��ϲ��Ѽ��Ϸ��뵽application�С�
		 */
		ServletContext sc = request.getSession().getServletContext();
		Map<String, Right> map = (Map<String, Right>) sc.getAttribute("all_rights_map");
		Right right = map.get(url);//��ȡ��Ȩ��
		
		if(right == null || right.isCommon() ){
			return true;
		}else{
			if(action instanceof LoginAction || action instanceof UserAction){
				return true;
			}else{
				User user = (User) request.getSession().getAttribute("user");
				//�Ƿ��¼
				if(user == null){
					return false;
				}else{
					if(action != null && action instanceof UserAware){
						((UserAware) action).setUser(user);//ע��user
					}
					//�Ƿ��ǳ�������Ա
					if(user.isSuperAdmin()){
						return true;
					}else{
						//�Ƿ���Ȩ��
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
