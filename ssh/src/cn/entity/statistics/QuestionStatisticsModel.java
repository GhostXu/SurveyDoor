package cn.entity.statistics;

import java.util.ArrayList;
import java.util.List;

import cn.entity.Question;

/**
 * ����ͳ��ģ��
 */
public class QuestionStatisticsModel {
	//����ʵ��
	private Question question;
	//�ش���������
	private int count;
	//OptionStatisticsModelʵ��
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
