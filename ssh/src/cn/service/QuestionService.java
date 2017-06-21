package cn.service;

import java.util.List;

import cn.entity.Question;

public interface QuestionService {

	public void saveOrUpdateQuestion(Question model);

	public Question getQuestion(Integer qid);

	public List<Question> getQuestions();

	public List<Question> getQuestions(int questionId);

}
