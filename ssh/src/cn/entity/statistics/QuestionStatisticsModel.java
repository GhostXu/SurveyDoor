package cn.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import cn.entity.Question;

/**
 * 问题统计模型
 */
public class QuestionStatisticsModel {
	//问题实体
	private Question question;
	//回答问题人数
	private int count;
	//OptionStatisticsModel实体
	private List<OptionStatisticsModel> osms = new ArrayList<OptionStatisticsModel>();

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OptionStatisticsModel> getOsms() {
		return osms;
	}

	public void setOsms(List<OptionStatisticsModel> osms) {
		this.osms = osms;
	}
}
