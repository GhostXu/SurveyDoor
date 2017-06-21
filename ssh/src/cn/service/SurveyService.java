package cn.service;

import java.util.List;

import cn.entity.Answer;
import cn.entity.Page;
import cn.entity.Question;
import cn.entity.Survey;
import cn.entity.User;

public interface SurveyService {
	//调查
	public Survey newSurvey(User user);

	public List<Survey> findMySurveys(User user);
	
	public Survey getSurvey(Integer id);

	public Survey getSurveyWithChildren(Integer sid);

	public void updateSurvey(Survey model);
	
	public void deleteSurvey(Integer sid);
	
	public void deleteAnswer(Integer sid);
	
	public void updateStatus(Integer sid);
	//页面
	public void saveOrUpdatePage(Page model);

	public Page getPage(Integer pid);
	
	public void deletePage(Integer pid);
	//问题
	public void saveOrUpdateQuestion(Question model);

	public Question getQuestion(Integer qid);

	public void deleteQuestion(Integer qid);

	public void updateLogoPhotoPath(Integer sid, String path);

	public List<Survey> findSurveyWithPage(User user);

	public void moveOrCopyPage(Integer srcPid, Integer targPid, int pos);

	public List<Survey> getAllSurveys();

	public Page getFirstPage(Integer sid);

	public Page getPrePage(Integer currPid);

	public Page getNextPage(Integer currPid);

	public void saveAnswers(List<Answer> answers);

	public List<Answer> getAnswer(Integer sid);

	public List<Question> getQuestions(Integer sid);

}
