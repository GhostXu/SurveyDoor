package cn.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.Question;
import cn.service.QuestionService;

@Service(value="questionService")
public class QuestionServiceImpl implements QuestionService{
	@Resource(name="questionDao")
	private BaseDao questionDao;

	@Override
	public void saveOrUpdateQuestion(Question model) {
		questionDao.saveOrUpdateEntity(model);
	}

	@Override
	public Question getQuestion(Integer qid) {
		return (Question) questionDao.getEntityById(qid);
	}

	@Override
	public List<Question> getQuestions() {
		String sql = "select q.* from question q where q.pid in (select p.id from page p where p.surveyid in "
				+ " (select s.id from survey s where s.title = ?))";
		return questionDao.findEntitiesBySql(sql, "每日一面");
	}

	@Override
	public List<Question> getQuestions(int questionId) {
		return null;
	}
}
