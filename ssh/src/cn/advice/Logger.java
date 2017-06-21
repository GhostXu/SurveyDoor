package cn.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;

import com.opensymphony.xwork2.ActionContext;

import cn.service.LogService;
import cn.util.DataUtils;
import cn.entity.Log;
import cn.entity.User;
//日志记录仪
public class Logger {
	private LogService logService;

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	public Object record(ProceedingJoinPoint pjp){
		Log log = new Log();
		try {
			//操作者
			ActionContext ac = ActionContext.getContext();
			if(ac != null){
				HttpServletRequest req = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
				if(req != null){
					User user = (User) req.getSession().getAttribute("user");
					if(user != null){
						log.setOperator(user.getUid() + ":" + user.getNickName());
					}
				}
			}
			//操作名称（方法名）
			String operName = pjp.getSignature().getName() ;
			log.setOperName(operName);
			//操作参数
			Object[] params = pjp.getArgs() ;
			log.setOperParams(DataUtils.strList2str(params));
			
			//调用目标类的目标方法
			Object ret = pjp.proceed();
			//如果目标方法执行成功返回“success”
			log.setOperResult("success");
			
			if(ret != null){
				log.setResultMsg(ret.toString());
			}
			return ret;
		} catch (Throwable e) {
			//如果目标方法执行失败
			log.setOperResult("failure");
			log.setResultMsg(e.getMessage());
		}finally{
			logService.saveLogBySql(log);
		}
		return null;
	}
}
