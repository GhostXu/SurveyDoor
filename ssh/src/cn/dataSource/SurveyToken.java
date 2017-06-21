package cn.dataSource;

import cn.entity.Survey;

/**
 * �������ƣ��󶨵���ǰ�̣߳�����������Դ·���������зֿ��ж�
 */
public class SurveyToken {

	private Survey currentSurvey;
	
	private static ThreadLocal<SurveyToken> t = new ThreadLocal<SurveyToken>();

	public Survey getCurrentSurvey() {
		return currentSurvey;
	}

	public void setCurrentSurvey(Survey currentSurvey) {
		this.currentSurvey = currentSurvey;
	}
	
	/**
	 * �����Ƶ���ǰ�߳�
	 */
	public static void bindToken(SurveyToken token){
		t.set(token);
	}

	/**
	 * �������
	 */
	public static SurveyToken getCurrentToken(){
		return t.get();
	}
	
	/**
	 * �����
	 */
	public static void unbindToken(){
		t.remove();
	}
}
