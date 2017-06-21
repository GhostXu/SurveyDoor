package cn.dataSource;

import cn.entity.Survey;

/**
 * 调查令牌，绑定到当前线程，传播到数据源路由器，进行分库判断
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
	 * 绑定令牌到当前线程
	 */
	public static void bindToken(SurveyToken token){
		t.set(token);
	}

	/**
	 * 获得令牌
	 */
	public static SurveyToken getCurrentToken(){
		return t.get();
	}
	
	/**
	 * 解除绑定
	 */
	public static void unbindToken(){
		t.remove();
	}
}
