package cn.advice;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;

import com.opensymphony.xwork2.ActionContext;

import cn.service.LogService;
import cn.util.DataUtils;
import cn.entity.Log;
import cn.entity.User;
//��־��¼��
public class Logger {
	private LogService logService;

	public void setLogService(LogService logService) {
		this.logService = logService;
	}
	
	public Object record(ProceedingJoinPoint pjp){
		Log log = new Log();
		try {
			//������
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
			//�������ƣ���������
			String operName = pjp.getSignature().getName() ;
			log.setOperName(operName);
			//��������
			Object[] params = pjp.getArgs() ;
			log.setOperParams(DataUtils.strList2str(params));
			
			//����Ŀ�����Ŀ�귽��
			Object ret = pjp.proceed();
			//���Ŀ�귽��ִ�гɹ����ء�success��
			log.setOperResult("success");
			
			if(ret != null){
				log.setResultMsg(ret.toString());
			}
			return ret;
		} catch (Throwable e) {
			//���Ŀ�귽��ִ��ʧ��
			log.setOperResult("failure");
			log.setResultMsg(e.getMessage());
		}finally{
			logService.saveLogBySql(log);
		}
		return null;
	}
}
