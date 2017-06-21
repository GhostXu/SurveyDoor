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
		//�Ƿ���������ˢ���¼�
		if(arg0 instanceof ContextRefreshedEvent){
			List<Right> rights = rightService.findAllEntity();
			Map<String, Right> map = new HashMap<String, Right>();
			for(Right r : rights){
				map.put(r.getRightUrl(), r);
			}
			if(sc != null){
				sc.setAttribute("all_rights_map", map);
				System.out.println("Ȩ�޳�ʼ����ɣ�");
			}
		}
	}

	private ServletContext sc;
	@Override
	public void setServletContext(ServletContext arg0) {
		System.out.println("ע��servletContext");
		this.sc = arg0;
	}

}
