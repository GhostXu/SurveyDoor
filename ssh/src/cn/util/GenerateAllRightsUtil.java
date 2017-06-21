package cn.util;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.service.RightService;

public class GenerateAllRightsUtil {
	/**
	 * 
	 */
	public static void main(String[] args) throws Exception {
		/**
		 * 1.�Ȼ�ȡrightService
		 * 2.��ȡaction���ش�ŵ�ַurl
		 * 3.��ȡURL������action
		 * 4.��ȡ����action����
		 */
		ApplicationContext ac = new ClassPathXmlApplicationContext("config/application-context.xml");//��ȡspring�������ļ�
		RightService rs = (RightService) ac.getBean("rightService");
		
		URL url = GenerateAllRightsUtil.class.getClassLoader().getSystemResource("cn/web");
		File dir  = new File(url.toURI());
		File[] files = dir.listFiles();
		String fname = "";
		for(File f : files){
			fname = f.getName();//��ȡ����action����
			if(fname.endsWith("class") && !fname.equals("UserAware.class") 
					&& !fname.equals("LoginInterceptor.class") && !fname.equals("CatchUrlInterceptor.class")){
				processClass(fname,rs);
			}
		}
	}
	
	/**
	 * ��������ÿ����
	 * @param fname action���� SurveyAction.class
	 * @param rs rightService
	 * @throws Exception 
	 */
	private static void processClass(String fname, RightService rs) throws Exception {
		/**
		 * 1.�Ȼ�ȡ���� pkgName ��cn.web��
		 * 2.��ȡ���� simpleClassName
		 * 3.��ȡ������ methods
		 * 4.����url
		 *   ��ȡ�޶���������������ֵ���ͣ�����struts2�涨��action�����޶��������� public ����ֵ���ͱ�����String ��������Ϊ��
		 */
		String pkgName = "cn.web";
		String simpleClassName = fname.substring(0, fname.indexOf("."));
		Class classz = Class.forName(pkgName + "." + simpleClassName);//cn.web.RightAction
 		Method[] methods = classz.getDeclaredMethods();
 		
 		String mname = "";//������
 		Class resultType = null;//����ֵ����
 		Class[] paramType = null;//�����б�
 		String url = "" ;
 		for(Method m : methods){
 			mname = m.getName();
 			resultType = m.getReturnType();
 			paramType = m.getParameterTypes();
 			if(resultType == String.class && !ValidateUtil.isValid(paramType) && Modifier.isPublic(m.getModifiers())){
 				if(mname.equals("excute")){
 	 				url = "/" + simpleClassName ;
 	 			}else{
 	 				url = "/" + simpleClassName + "_"+ mname;
 	 			}
 				rs.appendRightByUrl(url);
 			}
 		}
	}

}
