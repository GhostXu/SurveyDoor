package cn.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 自定义数据源路由器
 * 奇数调查答案保存到主库，偶数调查答案保存到从库
 */
public class SurveyparkDataSourceRouter extends AbstractRoutingDataSource{

	protected Object determineCurrentLookupKey() {
		SurveyToken token = SurveyToken.getCurrentToken();
		if(token != null){
			Integer id = token.getCurrentSurvey().getId();
			/**
			 * 解除绑定令牌
			 */
			SurveyToken.unbindToken();
			return (id % 2) == 0 ? "even": "odd" ;
		}
		return null;
	}

}
