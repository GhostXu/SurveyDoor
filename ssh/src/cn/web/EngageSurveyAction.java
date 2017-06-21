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
	//����ServletContext
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
	//��ȡ��ǰҳ��id
	private Integer currPid;
	public Integer getCurrPid() {
		return currPid;
	}
	public void setCurrPid(Integer currPid) {
		this.currPid = currPid;
	}
	//��ǰҳ��
	private Page currPage;
	
	private Map<String, String[]> paramsMap;
	
	public Page getCurrPage() {
		return currPage;
	}
	public void setCurrPage(Page currPage) {
		this.currPage = currPage;
	}

	//��ȡ���п��õ���
	public String findAllAvailableSurveys(){
		this.surveys = surveyService.getAllSurveys();
		return "engageSurveyList";
	}
	//��ȡͼƬ
	public String getImageUrl(String url){
		if(ValidateUtil.isValid(url)){
			String realPath = sc.getRealPath(url);
			if(new File(realPath).exists()){
				return "/ssh/"+url ;
			}
		}
		return "/ssh/jsp/question.bmp";
	}
	//���뵽����ҳ��
	public String entry(){
		this.currPage = surveyService.getFirstPage(sid);
		sessionMap.put(CURRENT_SURVEY, currPage.getSurvey());
		sessionMap.put(ALL_PARAMS_MAP, new HashMap<Integer, Map<String, String[]>>());
		return "engageSurvey";
	}
	//�ύ����
	public String doEngageSurvey(){
		//��ȡ�ύ��ť��ͨ����������ҳ���ύ�Ĳ���
		String submitName = getSubmitName();
		//��һ��
		if(submitName.endsWith("pre")){
			mergeParamsIntoSession();
			currPage = surveyService.getPrePage(currPid);
			return "engageSurvey";
		}
		//��һ��
		else if(submitName.endsWith("next")){
			mergeParamsIntoSession();
			currPage = surveyService.getNextPage(currPid);
			return "engageSurvey";
		}
		//�˳�
		else if(submitName.endsWith("exit")){
			clearSessionData();
			return "getSurveysAction";
		}
		//���
		else if(submitName.endsWith("done")){
			mergeParamsIntoSession();
			List<Answer> answers = processAnswer();
			/**
			 * 1.����������������Ȱ�token����ǰ�߳��У�surveyService.saveAnswers������
			 * 2.tx ���ж��Ƿ����������û�о��½�������(TransactionInterceptor)��ִ��Ŀ�귽����
			 * 3.logger ��ת����־��¼���У�ִ��Ŀ�귽�� pjp.proceed();
			 * 4.��ִ��surveyService�е�saveAnswers����ǰҪ�ȸ��ݵ�ǰsurvey��id�ж�����ż��SurveyparkDataSourceRouter����Ŀ����ȷ����Ӧ�ñ��浽�ĸ���
			 * 5.�����
			 * 6.���ص�logger�У�ִ��finally�еķ��� logService.saveLogBySql(log)
			 * 7.����logService�������У��ж�������Ϊ��2�У�������Ѿ����� �ˣ����Դ˴�ֱ�Ӹ���token�е���ż����־���档
			 * 
			 * ��Ϊ����Ϊ ��������Ĵ𰸱��浽���� ��ż������Ĵ𰸱��浽�ӿ� �У�����־ֻ���浽�����С�����������������ᱨ����Ϊ�ӿ���û����־��
			 * 
			 * �����
			 * ��������tx����־��¼��logger��ִ��˳����ִ��logger��ִ��tx�����ж���id��ż�󣬽��token����ʱ����ȷ��Ӧ�ñ��浽�ĸ��⣬����ִ���걣��𰸺�����رգ���
			 * �����ڱ�����־��ʱ������¿�����������־��Ĭ�����ݿ�--����
			 * 
			 * ע��һ��Ҫ�ڴ�֪�����浽�Ǹ������ܽ����־����֮���Բ��ж���ż������Ϊ�ڴ�֮ǰ���ѱ���ɹ�����������ر�
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
	//�����
	private List<Answer> processAnswer() {
		//����ʽ��ѡ��ť
		Map<Integer, String> matrixRadioMap = new HashMap<Integer, String>();
		
		List<Answer> answers = new ArrayList<Answer>();
		Answer answer = null;
		/**
		 * getAllParamsMapInSession().values()
		 * ��ȡ��session�е�����paramsMap��ֵ��[{q4other=[Ljava.lang.String;@65fa0419, q5other=[Ljava.lang.String;@123399d, submit_next=[Ljava.lang.String;@2f9b7300, currPid=[Ljava.lang.String;@69b1d6b}, 
		 * {q6=[Ljava.lang.String;@37a5989f, submit_next=[Ljava.lang.String;@58c70b43, currPid=[Ljava.lang.String;@73b4893d}, 
		 * {q7=[Ljava.lang.String;@6ac3816, submit_next=[Ljava.lang.String;@14962fc6, currPid=[Ljava.lang.String;@101a7ab9}, 
		 * {submit_done=[Ljava.lang.String;@440eb073, currPid=[Ljava.lang.String;@206270ed}]�������й�����4��ҳ��
		 */
		for(Map<String, String[]> map : getAllParamsMapInSession().values()){
			/**
			 * map.entrySet()
			 * ��ȡÿ��ҳ��ļ��ϣ�[q4other=[Ljava.lang.String;@65fa0419, q5other=[Ljava.lang.String;@123399d, submit_next=[Ljava.lang.String;@2f9b7300, currPid=[Ljava.lang.String;@69b1d6b]��
			 * entry:q4other=[Ljava.lang.String;@65fa0419
			 */
			for(Entry<String, String[]> entry : map.entrySet()){
				//key = q4other
				String key = entry.getKey();
				String[] value = entry.getValue();//�𰸼���
				if(key.contains("q")){
					//q1=0|1|2,q2,q3,q4,q5,q6,q8,q9
					if(!key.contains("_") && !key.contains("other")){
						answer = new Answer();
						int qid = getQuestionId(key,0);//�������
						String answerIds = DataUtils.strList2str(value);
						
						answer.setQuestionId(qid);
						answer.setAnswerIds(answerIds);
						answer.setSurveyId(getCurrentSurvey().getId());
						//����������
						String[] otherValues = map.get(key+"other");
						answer.setOtherAnswer(DataUtils.strList2str(otherValues));
						
						answers.add(answer);
					}
					//�������ʽ��ѡ��ťq7_0=0_0,q7_1=0_1,q7_2=0_2..
					else if(key.contains("_")){
						int qid = getQuestionId(key,1);//�������
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
		//�����������ʽ��ѡ
		processMatrixRadioAnswers(answers,matrixRadioMap);
		return answers;
	}
	/**
	 * @param answers �𰸼���
	 * @param matrixRadioMap ����ʽ��ѡ��
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
	//��ȡ��ǰsurvey
	private Survey getCurrentSurvey() {
		return (Survey)sessionMap.get(CURRENT_SURVEY);
	}

	/**
	 * ��ȡ�������
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

	//����session�е�����
	private void clearSessionData() {
		sessionMap.remove(CURRENT_SURVEY);
		sessionMap.remove(ALL_PARAMS_MAP);
	}

	//�Ѳ����ϲ���session��
	private void mergeParamsIntoSession() {
		Map<Integer, Map<String, String[]>> allParamsMap = getAllParamsMapInSession();
		allParamsMap.put(currPid, paramsMap);
	}
	//��ȡsession�����еĲ���
	@SuppressWarnings("unchecked")
	private Map<Integer, Map<String, String[]>> getAllParamsMapInSession() {
		return (Map<Integer, Map<String, String[]>>) sessionMap.get(ALL_PARAMS_MAP);
	}

	//����ύ��ť����
	private String getSubmitName() {
		for(String paramName : paramsMap.keySet()){
			if(paramName.startsWith("submit_")){
				return paramName;
			}
		}
		return null;
	}

	//ע��session
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}
	//ע��ServletContext
	@Override
	public void setServletContext(ServletContext arg0) {
		this.sc = arg0 ;
	}
	//ע�����
	@Override
	public void setParameters(Map<String, String[]> arg0) {
		this.paramsMap = arg0 ;
	}
	/**
	 * ����ѡ�б��
	 * @param name ѡ��
	 * @param value ��
	 * @param tag ��ѡ�еı�ǩ���ã�checked��selected��������
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
	 * �����ı��������
	 * @param name ѡ��
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
	 * ÿ��һ��
	 * @return
	 */
	public String review(){
		Survey survey = surveyService.getSurveyWithChildren(sid);
		//���¹���һ��survey��ֻ����3����ĵ���
		/**
		 * 1.����������õ���Ҫչʾ��3����
		 * 2.����ÿ�����pid ҳ��id ��ÿ����Żص�page��
		 * 3.�ٽ�pageList����survey��
		 */
		List<Question> questions = questionService.getQuestions();
		Random random = new Random();
		
		survey.setPages(null);
		
		List<Page> pages = new ArrayList<Page>();
		for(int questionId = random.nextInt(questions.size()); questionId<questions.size();questionId++){
			if(pages.size() != 0){
				Page page = pageService.getPageByQid(questions.get(questionId).getId());
				for(int i=0 ; i<pages.size() ; i++){
					//�����ǰ��������ҳ���Ѿ��ڵ����д��ڣ��Ͱ��������׷�ӵ�ҳ���У���������ڣ������ҳ�沢׷�ӵ�������
					
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
		 * 1.�õ���ǰ��������ҳ�棬����������գ��ٰѵ�ǰ����ŵ�ҳ����
		 * 2.��ҳ��ŵ�ҳ��list��
		 */
		Page page = pageService.getPageByQid(question.getId());
		Set<Question> ques = new HashSet<Question>();
		page.setQuestions(null);
		
		ques.add(question);
		page.setQuestions(ques);
		return page;
	}
}
