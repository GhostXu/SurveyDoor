package cn.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.service.LogService;
import cn.util.LogUtil;

@SuppressWarnings("rawtypes")
@Component
public class InitLogTableListener implements ApplicationListener{

	@Resource(name="logService")
	private LogService logService;
	
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		if(arg0 instanceof ContextRefreshedEvent){
			String tableName = LogUtil.generateLogTableName(0);
			String sql = "create table if not exists " + tableName + " like log" ;
			logService.saveEntityBySql(sql);
			
			tableName = LogUtil.generateLogTableName(1);
			sql = "create table if not exists " + tableName + " like log" ;
			logService.saveEntityBySql(sql);
			
			tableName = LogUtil.generateLogTableName(2);
			sql = "create table if not exists " + tableName + " like log" ;
			logService.saveEntityBySql(sql);
			System.out.println("日志表初始化完成！");
		}
	}

}
