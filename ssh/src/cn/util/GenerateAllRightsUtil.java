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
		 * 1.先获取rightService
		 * 2.获取action本地存放地址url
		 * 3.获取URL下所有action
		 * 4.获取所有action名称
		 */
		ApplicationContext ac = new ClassPathXmlApplicationContext("config/application-context.xml");//获取spring的配置文件
		RightService rs = (RightService) ac.getBean("rightService");
		
		URL url = GenerateAllRightsUtil.class.getClassLoader().getSystemResource("cn/web");
		File dir  = new File(url.toURI());
		File[] files = dir.listFiles();
		String fname = "";
		for(File f : files){
			fname = f.getName();//获取所有action名称
			if(fname.endsWith("class") && !fname.equals("UserAware.class") 
					&& !fname.equals("LoginInterceptor.class") && !fname.equals("CatchUrlInterceptor.class")){
				processClass(fname,rs);
			}
		}
	}
	
	/**
	 * 单独处理每个类
	 * @param fname action名称 SurveyAction.class
	 * @param rs rightService
	 * @throws Exception 
	 */
	private static void processClass(String fname, RightService rs) throws Exception {
		/**
		 * 1.先获取包名 pkgName “cn.web”
		 * 2.获取类名 simpleClassName
		 * 3.获取方法名 methods
		 * 4.生成url
		 *   获取限定名、参数、返回值类型，根据struts2规定，action方法限定名必须是 public 返回值类型必须是String 参数必须为空
		 */
		String pkgName = "cn.web";
		String simpleClassName = fname.substring(0, fname.indexOf("."));
		Class classz = Class.forName(pkgName + "." + simpleClassName);//cn.web.RightAction
 		Method[] methods = classz.getDeclaredMethods();
 		
 		String mname = "";//方法名
 		Class resultType = null;//返回值类型
 		Class[] paramType = null;//参数列表
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
