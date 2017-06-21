package cn.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Log;
import cn.service.LogService;

@Controller
@Scope("prototype")
public class LogAction extends BaseAction<Log> {
	private static final long serialVersionUID = 37111457546770863L;

	@Resource(name="logService")
	private LogService logService ;

	private List<Log> allLogs ;
	
	public List<Log> getAllLogs() {
		return allLogs;
	}

	public void setAllLogs(List<Log> allLogs) {
		this.allLogs = allLogs;
	}

	public String findAllLogs(){
		this.allLogs = logService.findNearestLogs() ;
		return "logListPage";
	}
}
