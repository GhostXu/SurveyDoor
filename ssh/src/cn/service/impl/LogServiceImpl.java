package cn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.id.UUIDHexGenerator;
import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.Log;
import cn.service.LogService;
import cn.util.LogUtil;

@Service(value="logService")
public class LogServiceImpl extends BaseServiceImpl<Log> implements LogService{
	@Resource(name="logDao")
	public void setBaseDao(BaseDao<Log> logDao) {
		super.setBaseDao(logDao);
	}
	
	/**
	 * 分表保存日志
	 */
	public void saveLogBySql(Log log){
		String sql = "insert into " + LogUtil.generateLogTableName(0) + "(id,operator,operName,operParams,operResult,resultMsg,opertime) values (?,?,?,?,?,?,?)" ;
		UUIDHexGenerator id = new UUIDHexGenerator() ;
		String uuid = (String) id.generate(null, null) ;
		this.saveEntityBySql(sql, uuid,
				log.getOperator(),
				log.getOperName(),
				log.getOperParams(),
				log.getOperResult(),
				log.getResultMsg(),
				log.getOperTime());
	}

	public List<Log> findNearestLogs(){
		String sql = "select * from " + LogUtil.generateLogTableName(0) + 
				" union select * from " + LogUtil.generateLogTableName(1); 
		return this.findEntitiesBySql(sql);
	}
}
