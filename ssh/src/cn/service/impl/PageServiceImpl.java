package cn.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.Page;
import cn.service.PageService;

@Service(value="pageService")
public class PageServiceImpl implements PageService {
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao;
	@Override
	public Page getPageByQid(Integer qid) {
		String sql = "select p.* from page p where p.id=(select q.pid from question q where q.id=?)";
		return pageDao.findEntitiesBySql(sql, qid).size()==0?null:pageDao.findEntitiesBySql(sql, qid).get(0);
	}

}
