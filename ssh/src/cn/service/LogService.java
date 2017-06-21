package cn.service;

import java.util.List;

import cn.entity.Log;

public interface LogService extends BaseService<Log>{

	public void saveLogBySql(Log log);

	public List<Log> findNearestLogs();
	
}
