package cn.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.service.LogService;
import cn.util.LogUtil;

/**
 * ʹ��spring���ɵ�ʯӢ���ȣ���̬������־��
 */
public class GenerateLogsTableTask extends QuartzJobBean{

	private LogService logService;
	
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		String tableName = LogUtil.generateLogTableName(1);
		String sql = "create table if not exists " + tableName + " like log";
		logService.saveEntityBySql(sql);
		System.out.println(tableName +"��������");
		
		tableName = LogUtil.generateLogTableName(2);
		sql = "create table if not exists " + tableName + " like log";
		logService.saveEntityBySql(sql);
		System.out.println(tableName +"��������");
	}
}
