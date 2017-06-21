package cn.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * �Զ�������Դ·����
 * ��������𰸱��浽���⣬ż������𰸱��浽�ӿ�
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource{

	protected Object determineCurrentLookupKey() {
		SurveyToken token = SurveyToken.getCurrentToken();
		if(token != null){
			Integer id = token.getCurrentSurvey().getId();
			/**
			 * ���������
			 */
			SurveyToken.unbindToken();
			return (id % 2) == 0 ? "even": "odd" ;
		}
		return null;
	}

}
