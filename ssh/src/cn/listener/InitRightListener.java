package cn.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import cn.entity.security.Right;
import cn.service.RightService;

@SuppressWarnings("rawtypes")
@Component
public class InitRightListener implements ApplicationListener, ServletContextAware {

	@Resource(name="rightService")
	private RightService rightService;
	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		//是否是上下文刷新事件
		if(arg0 instanceof ContextRefreshedEvent){
			List<Right> rights = rightService.findAllEntity();
			Map<String, Right> map = new HashMap<String, Right>();
			for(Right r : rights){
				map.put(r.getRightUrl(), r);
			}
			if(sc != null){
				sc.setAttribute("all_rights_map", map);
				System.out.println("权限初始化完成！");
			}
		}
	}

	private ServletContext sc;
	@Override
	public void setServletContext(ServletContext arg0) {
		System.out.println("注入servletContext");
		this.sc = arg0;
	}

}
