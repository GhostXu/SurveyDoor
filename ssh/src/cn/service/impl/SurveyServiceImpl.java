package cn.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.Answer;
import cn.entity.Page;
import cn.entity.Question;
import cn.entity.Survey;
import cn.entity.User;
import cn.service.SurveyService;
import cn.util.DataUtils;
import cn.util.ValidateUtil;

@Service(value="surveyService")
public class SurveyServiceImpl implements SurveyService{
	@Resource(name="surveyDao")
	private BaseDao<Survey> surveyDao;
	
	@Resource(name="pageDao")
	private BaseDao<Page> pageDao;
	
	@Resource(name="userDao")
	private BaseDao<User> userDao;
	
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao;
	
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao;
	
	@Override
	public Survey newSurvey(User user) {
		Survey survey = new Survey();
		Page page = new Page();
				
		survey.setUser(user);
		page.setSurvey(survey);
		survey.getPages().add(page);
		
		surveyDao.insert(survey);
		pageDao.insert(page);
		return survey;
	}
	
	//根据用户id获取该用户的所有survey
	@Override
	public List<Survey> findMySurveys(User user) {
		String hql = " from Survey s where s.user.email = ?";
		return this.surveyDao.findEntityByHql(hql, user.getEmail());
	}
	
	//根据id查询survey
	public Survey getSurvey(Integer id){
		return surveyDao.getEntityById(id);
	}
	
	//获取调查中包含的所有子节点
	public Survey getSurveyWithChildren(Integer sid) {
		Survey survey = this.getSurvey(sid);
		//强行初始化pages和questions集合
		for(Page page:survey.getPages()){
			page.getQuestions().size();
		}
		return survey;
	}
	
	//更新调查
	public void updateSurvey(Survey model){
		this.surveyDao.saveOrUpdateEntity(model);
	}
	
	//保存或更新页面
	public void saveOrUpdatePage(Page model) {
		pageDao.saveOrUpdateEntity(model);
	}
	
	//获取页面
	public Page getPage(Integer pid){
		return pageDao.getEntityById(pid);
	}
	
	//保存或更新问题
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	//获取问题
	public Question getQuestion(Integer qid){
		return questionDao.getEntityById(qid);
	}

	//删除问题
	public void deleteQuestion(Integer qid){
		//删除答案
		String hql = "delete from Answer a where a.questionId = ?";
		answerDao.batchEntityByHQL(hql, qid);
		//删除问题
		hql = "delete from Question q where q.id = ?";
		questionDao.batchEntityByHQL(hql, qid);
	}
	
	//删除页面
	public void deletePage(Integer pid){
		//删除答案
		String hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)";
		answerDao.batchEntityByHQL(hql, pid);
		//删除问题
		hql = "delete from Question q where q.page.id = ?";
		questionDao.batchEntityByHQL(hql, pid);
		//删除页面
		hql = "delete from Page p where p.id = ?";
		pageDao.batchEntityByHQL(hql, pid);
	}
	
	//删除调查
	public void deleteSurvey(Integer sid){
		//删除答案
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
		//删除问题
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)";
		questionDao.batchEntityByHQL(hql, sid);
		//删除页面
		hql = "delete from Page p where p.survey.id = ?";
		pageDao.batchEntityByHQL(hql, sid);
		//删除调查
		hql = "delete from Survey s where s.id = ?";
		surveyDao.batchEntityByHQL(hql, sid);
	}
	
	//清楚调查
	public void deleteAnswer(Integer sid){
		//删除答案
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
	}
	
	//修改状态
	public void updateStatus(Integer sid){
		Survey survey = surveyDao.getEntityById(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, !survey.getClosed(), sid);
	}
	
	//保存上传图片相对路径
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, path, sid);
	}
	
	//查询调查，携带page集合
	public List<Survey> findSurveyWithPage(User user){
		String hql = " from Survey s where s.user.uid=?";
		List<Survey> list = surveyDao.findEntityByHql(hql, user.getUid());
		//强行初始化页面集合
		for(Survey s:list){
			s.getPages().size();
		}
		return list;
	}
	
	/**
	 * 移动/复制页面
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		//TODO 复制和移动失败
		//源
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		//目标
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		//同一调查之间移动
		if((srcSurvey.getId()).equals(targSurvey.getId())){
			setOrdernum(srcPage,targPage,pos);
			pageDao.saveOrUpdateEntity(srcPage);
		}else{//不同调查间复制
			//targSurvey.getPages().size();
			Page copy = (Page) DataUtils.deeplyCopy(srcPage);
			copy.setSurvey(targSurvey);
			
			setOrdernum(copy,targPage,pos);
			pageDao.saveOrUpdateEntity(targPage);
		}
	}
	
	//设置orderno
	private void setOrdernum(Page srcPage, Page targPage, int pos) {
		//向前移动
		if(pos == 0){
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((prePage.getOrderno() + targPage.getOrderno())/2);
			}
		}
		//向后
		else{
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}else{
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((nextPage.getOrderno() + targPage.getOrderno())/2);
			}
		}
	}
	//获取前一个page
	private Page getPrePage(Page targPage) {
		String hql = " from Page p where p.id < ? and p.survey.id = ? order by p.orderno desc";
		return pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId()).get(0);
	}
	//获取后一个page
	private Page getNextPage(Page targPage) {
		String hql = " from Page p where p.id > ? and p.survey.id = ? order by p.orderno asc";
		return pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId()).get(0);
	}

	//是否是最后一个
	private boolean isLastPage(Page targPage) {
		String hql = " from Page p where p.id > ? and p.survey.id = ?";
		List<Page> pageList = pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId());
		return !ValidateUtil.regList(pageList);
	}

	//是否是第一个
	private boolean isFirstPage(Page targPage) {
		String hql = " from Page p where p.id < ? and p.survey.id = ?";
		List<Page> pageList = pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId());
		return !ValidateUtil.regList(pageList);
	}
	//获取所有可用调查
	public List<Survey> getAllSurveys(){
		String hql = " from Survey s where s.closed = ?";
		return surveyDao.findEntityByHql(hql, false);
	}
	//获取当前页面
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by orderno asc";
		Page page = pageDao.findEntityByHql(hql, sid).get(0);
		//懒加载
		page.getQuestions().size();
		page.getSurvey().getTitle();
		return page;
	}
	
	//获取上一页
	public Page getPrePage(Integer currPid){
		Page page = this.getPage(currPid);
		page = this.getPrePage(page);
		page.getQuestions().size();
		return page;
	}

	//获取下一页
	public Page getNextPage(Integer currPid){
		Page page = this.getPage(currPid);
		page = this.getNextPage(page);
		page.getQuestions().size();
		return page;
	}
	/**
	 * 保存答案list
	 */
	public void saveAnswers(List<Answer> answers){
		String uuid = UUID.randomUUID().toString();
		Date date = new Date();
		for(Answer answer : answers){
			if(answer != null){
				answer.setUuid(uuid);
				answer.setAnswerTime(date);
				answerDao.saveOrUpdateEntity(answer);
			}
		}
		
	}
	
	/**
	 * 获取所有answer
	 */
	public List<Answer> getAnswer(Integer sid){
		String hql = " from Answer a where a.surveyId = ?";
		return answerDao.findEntityByHql(hql, sid);
	}
	
	/**
	 * 根据sid获取所有question
	 */
	public List<Question> getQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ? order by q.id";
		return questionDao.findEntityByHql(hql, sid);
	}

}
