package cn.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.service.LogService;
import cn.util.LogUtil;

/**
 * 使用spring集成的石英调度，动态生成日志表
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
		System.out.println(tableName +"：已生成");
		
		tableName = LogUtil.generateLogTableName(2);
		sql = "create table if not exists " + tableName + " like log";
		logService.saveEntityBySql(sql);
		System.out.println(tableName +"：已生成");
	}
}
