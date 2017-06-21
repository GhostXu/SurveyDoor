package cn.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.opensymphony.xwork2.ActionContext;

import cn.dataSource.SurveyToken;
import cn.entity.Answer;
import cn.entity.Page;
import cn.entity.Question;
import cn.entity.Survey;
import cn.service.PageService;
import cn.service.QuestionService;
import cn.service.SurveyService;
import cn.util.DataUtils;
import cn.util.StringUtil;
import cn.util.ValidateUtil;

@Controller
@Scope("prototype")
public class EngageSurveyAction extends BaseAction<Survey> implements SessionAware, ServletContextAware, ParameterAware{

	private static final long serialVersionUID = 1751667541986677906L;
	
	private static final String CURRENT_SURVEY = "current_survey" ;
	
	private static final String ALL_PARAMS_MAP = "all_params_map" ;
	
	@Resource(name="surveyService")
	private SurveyService surveyService;
	
	private List<Survey> surveys;
	
	private Map<String, Object> sessionMap;
	//接收ServletContext
	private ServletContext sc;
	
	public List<Survey> getSurveys() {
		return surveys;
	}
	
	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	
	private Integer sid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	//获取当前页面id
	private Integer currPid;
	public Integer getCurrPid() {
		return currPid;
	}
	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}
	//当前页面
	private Page currPage;
	
	private Map<String, String[]> paramsMap;
	
	public Page getCurrPage() {
		return currPage;
	}
	public void setCurrPage(Page currPage) {
		this.currPage = currPage;
	}

	//获取所有可用调查
	public String findAllAvailableSurveys(){
		this.surveys = surveyService.getAllSurveys();
		return "engageSurveyList";
	}
	//获取图片
	public String getImageUrl(String url){
		if(ValidateUtil.isValid(url)){
			String realPath = sc.getRealPath(url);
			if(new File(realPath).exists()){
				return "/ssh/"+url ;
			}
		}
		return "/ssh/jsp/question.bmp";
	}
	//进入到调查页面
	public String entry(){
		this.currPage = surveyService.getFirstPage(sid);
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer, Map<String, String[]>>());
		return "engageSurvey";
	}
	//提交调查
	public String doEngageSurvey(){
		//获取提交按钮：通过遍历所有页面提交的参数
		String submitName = getSubmitName();
		//上一步
		if(submitName.endsWith("pre")){
			mergeParamsIntoSession();
			currPage = surveyService.getPrePage(currPid);
			return "engageSurvey";
		}
		//下一步
		else if(submitName.endsWith("next")){
			mergeParamsIntoSession();
			currPage = surveyService.getNextPage(currPid);
			return "engageSurvey";
		}
		//退出
		else if(submitName.endsWith("exit")){
			clearSessionData();
			return "getSurveysAction";
		}
		//完成
		else if(submitName.endsWith("done")){
			mergeParamsIntoSession();
			List<Answer> answers = processAnswer();
			/**
			 * 1.当保存请求过来后，先绑定token到当前线程中，surveyService.saveAnswers方法，
			 * 2.tx 先判断是否有事务，如果没有就新建个事务(TransactionInterceptor)，执行目标方法；
			 * 3.logger 跳转到日志记录仪中，执行目标方法 pjp.proceed();
			 * 4.在执行surveyService中的saveAnswers方法前要先根据当前survey的id判断是奇偶（SurveyparkDataSourceRouter）。目的是确定答案应该保存到哪个库
			 * 5.保存答案
			 * 6.返回到logger中，执行finally中的方法 logService.saveLogBySql(log)
			 * 7.跳到logService（代理）中，判断事务，因为在2中，事务就已经开启 了，所以此处直接根据token中的奇偶对日志保存。
			 * 
			 * 因为需求为 奇数调查的答案保存到主库 ，偶数调查的答案保存到从库 中，而日志只保存到主库中。所以上述解决方案会报错，因为从库中没有日志表；
			 * 
			 * 解决：
			 * 更改事务tx和日志记录仪logger的执行顺序，先执行logger后执行tx。在判断完id奇偶后，解绑定token（此时答案已确定应该保存到哪个库，并且执行完保存答案后，事务关闭）。
			 * 解绑后在保存日志的时候会重新开启，保存日志到默认数据库--主库
			 * 
			 * 注：一定要在答案知道保存到那个库后才能解绑。日志保存之所以不判断奇偶，是因为在此之前答案已保存成功，并且事务关闭
			 */
			SurveyToken token = new SurveyToken();
			token.setCurrentSurvey(getCurrentSurvey());
			SurveyToken.bindToken(token);
			
			surveyService.saveAnswers(answers);
			clearSessionData();
			return "getSurveysAction";
		}
		return null;
	}
	//处理答案
	private List<Answer> processAnswer() {
		//矩阵式单选按钮
		Map<Integer, String> matrixRadioMap = new HashMap<Integer, String>();
		
		List<Answer> answers = new ArrayList<Answer>();
		Answer answer = null;
		/**
		 * getAllParamsMapInSession().values()
		 * 获取到session中的所有paramsMap的值（[{q4other=[Ljava.lang.String;@65fa0419, q5other=[Ljava.lang.String;@123399d, submit_next=[Ljava.lang.String;@2f9b7300, currPid=[Ljava.lang.String;@69b1d6b}, 
		 * {q6=[Ljava.lang.String;@37a5989f, submit_next=[Ljava.lang.String;@58c70b43, currPid=[Ljava.lang.String;@73b4893d}, 
		 * {q7=[Ljava.lang.String;@6ac3816, submit_next=[Ljava.lang.String;@14962fc6, currPid=[Ljava.lang.String;@101a7ab9}, 
		 * {submit_done=[Ljava.lang.String;@440eb073, currPid=[Ljava.lang.String;@206270ed}]）调查中共包含4个页面
		 */
		for(Map<String, String[]> map : getAllParamsMapInSession().values()){
			/**
			 * map.entrySet()
			 * 获取每个页面的集合（[q4other=[Ljava.lang.String;@65fa0419, q5other=[Ljava.lang.String;@123399d, submit_next=[Ljava.lang.String;@2f9b7300, currPid=[Ljava.lang.String;@69b1d6b]）
			 * entry:q4other=[Ljava.lang.String;@65fa0419
			 */
			for(Entry<String, String[]> entry : map.entrySet()){
				//key = q4other
				String key = entry.getKey();
				String[] value = entry.getValue();//答案集合
				if(key.contains("q")){
					//q1=0|1|2,q2,q3,q4,q5,q6,q8,q9
					if(!key.contains("_") && !key.contains("other")){
						answer = new Answer();
						int qid = getQuestionId(key,0);//问题序号
						String answerIds = DataUtils.strList2str(value);
						
						answer.setQuestionId(qid);
						answer.setAnswerIds(answerIds);
						answer.setSurveyId(getCurrentSurvey().getId());
						//处理其他项
						String[] otherValues = map.get(key+"other");
						answer.setOtherAnswer(DataUtils.strList2str(otherValues));
						
						answers.add(answer);
					}
					//处理矩阵式单选按钮q7_0=0_0,q7_1=0_1,q7_2=0_2..
					else if(key.contains("_")){
						int qid = getQuestionId(key,1);//问题序号
						String oldValue = matrixRadioMap.get(qid);
						if(!ValidateUtil.isValid(oldValue)){
							matrixRadioMap.put(qid, DataUtils.strList2str(value));
						}else{
							matrixRadioMap.put(qid, oldValue + "," + DataUtils.strList2str(value));
						}
					}
				}
			}
		}
		//单独处理矩阵式单选
		processMatrixRadioAnswers(answers,matrixRadioMap);
		return answers;
	}
	/**
	 * @param answers 答案集合
	 * @param matrixRadioMap 矩阵式单选答案
	 */
	private void processMatrixRadioAnswers(List<Answer> answers,
			Map<Integer, String> matrixRadioMap) {
		Answer answer = null;
		Integer key = null;
		String value = null ;
		for(Entry<Integer, String> entry : matrixRadioMap.entrySet()){
			answer = new Answer();
			key = entry.getKey();
			value = entry.getValue();
			
			answer.setAnswerIds(value);
			answer.setQuestionId(key);
			answer.setSurveyId(getCurrentSurvey().getId());
		}
		answers.add(answer);
	}
	//获取当前survey
	private Survey getCurrentSurvey() {
		return (Survey)sessionMap.get(CURRENT_SURVEY);
	}

	/**
	 * 获取问题序号
	 * @param key eg:q4,q4other,q7_0
	 * @param tag 0:q4,1:q4other q7_0
	 * @return
	 */
	private int getQuestionId(String key,int tag) {
		String pid = "";
		if(tag == 0){
			pid = key.substring(1);
		}else{
			pid = key.substring(1, 2);
		}
		if(ValidateUtil.isValid(pid)){
			return Integer.parseInt(pid);
		}
		return 0;
	}

	//清理session中的内容
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}

	//把参数合并到session中
	private void mergeParamsIntoSession() {
		Map<Integer, Map<String, String[]>> allParamsMap = getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	//获取session中所有的参数
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}

	//获得提交按钮名称
	private String getSubmitName() {
		for(String paramName : paramsMap.keySet()){
			if(paramName.startsWith("submit_")){
				return paramName;
			}
		}
		return null;
	}

	//注入session
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}
	//注入ServletContext
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0 ;
	}
	//注入参数
	@Override
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap = arg0 ;
	}
	/**
	 * 回显选中标记
	 * @param name 选题
	 * @param value 答案
	 * @param tag 被选中的标签设置（checked，selected。。。）
	 */
	public String setTag(String name, String value, String tag){
		Integer pid = this.currPage.getId();
		Map<String, String[]> map = getAllParamsMapInSession().get(pid);
		String[] oldValues = map.get(name);
		if(ValidateUtil.contains(oldValues,value)){
			return tag ;
		}
		return "";
	}
	/**
	 * 回显文本框的内容
	 * @param name 选题
	 * @return
	 */
	public String setText(String name){
		Integer pid = this.currPage.getId();
		Map<String, String[]> map = getAllParamsMapInSession().get(pid);
		String[] oldValues = map.get(name);
		if(ValidateUtil.isValid(oldValues)){
			return "value = '"+oldValues[0]+"'";
		}
		return "";
	}
	
	@Resource(name="questionService")
	private QuestionService questionService;
	@Resource(name="pageService")
	private PageService pageService;
	/**
	 * 每日一面
	 * @return
	 */
	public String review(){
		Survey survey = surveyService.getSurveyWithChildren(sid);
		//重新构建一个survey，只包含3道题的调查
		/**
		 * 1.根据随机数得到需要展示的3道题
		 * 2.根据每道题的pid 页面id 将每道题放回到page中
		 * 3.再将pageList发到survey中
		 */
		List<Question> questions = questionService.getQuestions();
		Random random = new Random();
		
		survey.setPages(null);
		
		List<Page> pages = new ArrayList<Page>();
		for(int questionId = random.nextInt(questions.size()); questionId<questions.size();questionId++){
			if(pages.size() != 0){
				Page page = pageService.getPageByQid(questions.get(questionId).getId());
				for(int i=0 ; i<pages.size() ; i++){
					//如果当前问题所在页面已经在调查中存在，就把这个问题追加到页面中；如果不存在，就添加页面并追加到调查中
					
					if(page.getId().equals(pages.get(i).getId())){
						Set<Question> ques = pages.get(i).getQuestions();
						ques.add(questions.get(questionId));
						pages.get(i).setQuestions(ques);
					}else{
						pages.add(getPageByQid(questions.get(questionId)));
					}
					
				}
			}else{
				pages.add(getPageByQid(questions.get(questionId)));
			}
		}
		
		Set<Page> pageList = new HashSet<Page>();
		pageList.addAll(pages);
		survey.setPages(pageList);
		
		this.currPage = pages.get(0);
		this.currPage.setSurvey(survey);
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer, Map<String, String[]>>());
		return "review";
	}
	
	public Page getPageByQid(Question question){
		/**
		 * 1.拿到当前问题所在页面，并把问题清空，再把当前问题放到页面中
		 * 2.把页面放到页面list中
		 */
		Page page = pageService.getPageByQid(question.getId());
		Set<Question> ques = new HashSet<Question>();
		page.setQuestions(null);
		
		ques.add(question);
		page.setQuestions(ques);
		return page;
	}
}
