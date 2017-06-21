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
	
	//�����û�id��ȡ���û�������survey
	@Override
	public List<Survey> findMySurveys(User user) {
		String hql = " from Survey s where s.user.email = ?";
		return this.surveyDao.findEntityByHql(hql, user.getEmail());
	}
	
	//����id��ѯsurvey
	public Survey getSurvey(Integer id){
		return surveyDao.getEntityById(id);
	}
	
	//��ȡ�����а����������ӽڵ�
	public Survey getSurveyWithChildren(Integer sid) {
		Survey survey = this.getSurvey(sid);
		//ǿ�г�ʼ��pages��questions����
		for(Page page:survey.getPages()){
			page.getQuestions().size();
		}
		return survey;
	}
	
	//���µ���
	public void updateSurvey(Survey model){
		this.surveyDao.saveOrUpdateEntity(model);
	}
	
	//��������ҳ��
	public void saveOrUpdatePage(Page model) {
		pageDao.saveOrUpdateEntity(model);
	}
	
	//��ȡҳ��
	public Page getPage(Integer pid){
		return pageDao.getEntityById(pid);
	}
	
	//������������
	public void saveOrUpdateQuestion(Question model){
		questionDao.saveOrUpdateEntity(model);
	}
	
	//��ȡ����
	public Question getQuestion(Integer qid){
		return questionDao.getEntityById(qid);
	}

	//ɾ������
	public void deleteQuestion(Integer qid){
		//ɾ����
		String hql = "delete from Answer a where a.questionId = ?";
		answerDao.batchEntityByHQL(hql, qid);
		//ɾ������
		hql = "delete from Question q where q.id = ?";
		questionDao.batchEntityByHQL(hql, qid);
	}
	
	//ɾ��ҳ��
	public void deletePage(Integer pid){
		//ɾ����
		String hql = "delete from Answer a where a.questionId in (select q.id from Question q where q.page.id = ?)";
		answerDao.batchEntityByHQL(hql, pid);
		//ɾ������
		hql = "delete from Question q where q.page.id = ?";
		questionDao.batchEntityByHQL(hql, pid);
		//ɾ��ҳ��
		hql = "delete from Page p where p.id = ?";
		pageDao.batchEntityByHQL(hql, pid);
	}
	
	//ɾ������
	public void deleteSurvey(Integer sid){
		//ɾ����
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
		//ɾ������
		hql = "delete from Question q where q.page.id in (select p.id from Page p where p.survey.id = ?)";
		questionDao.batchEntityByHQL(hql, sid);
		//ɾ��ҳ��
		hql = "delete from Page p where p.survey.id = ?";
		pageDao.batchEntityByHQL(hql, sid);
		//ɾ������
		hql = "delete from Survey s where s.id = ?";
		surveyDao.batchEntityByHQL(hql, sid);
	}
	
	//�������
	public void deleteAnswer(Integer sid){
		//ɾ����
		String hql = "delete from Answer a where a.surveyId = ?";
		answerDao.batchEntityByHQL(hql, sid);
	}
	
	//�޸�״̬
	public void updateStatus(Integer sid){
		Survey survey = surveyDao.getEntityById(sid);
		String hql = "update Survey s set s.closed = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, !survey.getClosed(), sid);
	}
	
	//�����ϴ�ͼƬ���·��
	public void updateLogoPhotoPath(Integer sid, String path){
		String hql = "update Survey s set s.logoPhotoPath = ? where s.id = ?";
		surveyDao.batchEntityByHQL(hql, path, sid);
	}
	
	//��ѯ���飬Я��page����
	public List<Survey> findSurveyWithPage(User user){
		String hql = " from Survey s where s.user.uid=?";
		List<Survey> list = surveyDao.findEntityByHql(hql, user.getUid());
		//ǿ�г�ʼ��ҳ�漯��
		for(Survey s:list){
			s.getPages().size();
		}
		return list;
	}
	
	/**
	 * �ƶ�/����ҳ��
	 */
	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos){
		//TODO ���ƺ��ƶ�ʧ��
		//Դ
		Page srcPage = this.getPage(srcPid);
		Survey srcSurvey = srcPage.getSurvey();
		//Ŀ��
		Page targPage = this.getPage(targPid);
		Survey targSurvey = targPage.getSurvey();
		//ͬһ����֮���ƶ�
		if((srcSurvey.getId()).equals(targSurvey.getId())){
			setOrdernum(srcPage,targPage,pos);
			pageDao.saveOrUpdateEntity(srcPage);
		}else{//��ͬ����临��
			//targSurvey.getPages().size();
			Page copy = (Page) DataUtils.deeplyCopy(srcPage);
			copy.setSurvey(targSurvey);
			
			setOrdernum(copy,targPage,pos);
			pageDao.saveOrUpdateEntity(targPage);
		}
	}
	
	//����orderno
	private void setOrdernum(Page srcPage, Page targPage, int pos) {
		//��ǰ�ƶ�
		if(pos == 0){
			if(isFirstPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() - 0.01f);
			}else{
				Page prePage = getPrePage(targPage);
				srcPage.setOrderno((prePage.getOrderno() + targPage.getOrderno())/2);
			}
		}
		//���
		else{
			if(isLastPage(targPage)){
				srcPage.setOrderno(targPage.getOrderno() + 0.01f);
			}else{
				Page nextPage = getNextPage(targPage);
				srcPage.setOrderno((nextPage.getOrderno() + targPage.getOrderno())/2);
			}
		}
	}
	//��ȡǰһ��page
	private Page getPrePage(Page targPage) {
		String hql = " from Page p where p.id < ? and p.survey.id = ? order by p.orderno desc";
		return pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId()).get(0);
	}
	//��ȡ��һ��page
	private Page getNextPage(Page targPage) {
		String hql = " from Page p where p.id > ? and p.survey.id = ? order by p.orderno asc";
		return pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId()).get(0);
	}

	//�Ƿ������һ��
	private boolean isLastPage(Page targPage) {
		String hql = " from Page p where p.id > ? and p.survey.id = ?";
		List<Page> pageList = pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId());
		return !ValidateUtil.regList(pageList);
	}

	//�Ƿ��ǵ�һ��
	private boolean isFirstPage(Page targPage) {
		String hql = " from Page p where p.id < ? and p.survey.id = ?";
		List<Page> pageList = pageDao.findEntityByHql(hql, targPage.getId(),targPage.getSurvey().getId());
		return !ValidateUtil.regList(pageList);
	}
	//��ȡ���п��õ���
	public List<Survey> getAllSurveys(){
		String hql = " from Survey s where s.closed = ?";
		return surveyDao.findEntityByHql(hql, false);
	}
	//��ȡ��ǰҳ��
	public Page getFirstPage(Integer sid){
		String hql = "from Page p where p.survey.id = ? order by orderno asc";
		Page page = pageDao.findEntityByHql(hql, sid).get(0);
		//������
		page.getQuestions().size();
		page.getSurvey().getTitle();
		return page;
	}
	
	//��ȡ��һҳ
	public Page getPrePage(Integer currPid){
		Page page = this.getPage(currPid);
		page = this.getPrePage(page);
		page.getQuestions().size();
		return page;
	}

	//��ȡ��һҳ
	public Page getNextPage(Integer currPid){
		Page page = this.getPage(currPid);
		page = this.getNextPage(page);
		page.getQuestions().size();
		return page;
	}
	/**
	 * �����list
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
	 * ��ȡ����answer
	 */
	public List<Answer> getAnswer(Integer sid){
		String hql = " from Answer a where a.surveyId = ?";
		return answerDao.findEntityByHql(hql, sid);
	}
	
	/**
	 * ����sid��ȡ����question
	 */
	public List<Question> getQuestions(Integer sid){
		String hql = "from Question q where q.page.survey.id = ? order by q.id";
		return questionDao.findEntityByHql(hql, sid);
	}

}
