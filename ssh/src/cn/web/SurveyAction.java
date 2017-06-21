package cn.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Survey;
import cn.entity.User;
import cn.service.StatictiesService;
import cn.service.SurveyService;
import cn.util.ValidateUtil;

@Controller
@Scope("prototype")
public class SurveyAction extends BaseAction<Survey> implements UserAware, ServletContextAware{

	private static final long serialVersionUID = 1326986897695325102L;

	@Resource(name="surveyService")
	private SurveyService surveyService;
	
	private User user;
	public User getUser(){
		return this.user;
	}
	/**
	 * ʵ����UserAware�ӿڣ���дsetUser������ע��user
	 */
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	private List<Survey> mySurveys;
	public List<Survey> getMySurveys(){
		return this.mySurveys;
	}
	public void setMySurveys(List<Survey> mySurveys) {
		this.mySurveys = mySurveys;
	}
	
	private Integer sid;
	public Integer getSid(){
		return this.sid;
	}
	public void setSid(Integer sid){
		this.sid = sid;
	}
	//�ϴ��ļ�
	private File logoPhoto;
	public File getLogoPhoto() {
		return logoPhoto;
	}
	public void setLogoPhoto(File logoPhoto) {
		this.logoPhoto = logoPhoto;
	}
	private String logoPhotoFileName;
	public String getLogoPhotoFileName() {
		return logoPhotoFileName;
	}
	public void setLogoPhotoFileName(String logoPhotoFileName) {
		this.logoPhotoFileName = logoPhotoFileName;
	}
	
	private ServletContext sc;
	
	/**
	 * ʵ��ServletContextAware�ӿڣ���д�÷�����Ŀ����ע��ServletContext
	 *
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.sc = context;
	}
	//����ҳ��
	private String inputPage;
	public String getInputPage() {
		return inputPage;
	}
	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}
	//�½�����
	public String newSurvey(){
		//�½�һ������ҳ��
		this.model = surveyService.newSurvey(user);
		return "designSurvey";
	}
	
	//�ҵĵ���ҳ��
	public String mySurveys(){
		System.out.println(user.getNickName());
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyList";
	}
	
	//��Ƶ���
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurvey";
	}
	
	public String editSurvey(){
		this.model = surveyService.getSurvey(sid);
		return "editSurvey";
	}
	
	//�༭����
	public String updateSurvey() {
		//�����ض���
		this.sid = model.getId();
		//���ù�����ϵ
		model.setUser(user);
		this.surveyService.updateSurvey(this.model);
		return "designSurveyAction";
	}
	//ɾ������
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "mySurveysAction";
	}
	//�ı�״̬
	public String toggleStatus(){
		surveyService.updateStatus(sid);
		return "mySurveysAction";
	}
	//�������
	public String clearSurvey(){
		surveyService.deleteAnswer(sid);
		return "mySurveysAction";
	}
	//ת������logoҳ��
	public String toAddLogoPage(){
		return "addLogo";
	}
	//����logo
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//��ȡ�ļ��ϴ��󱣴�·��
			String dir = sc.getRealPath("/upload");
			//�ļ�����
			long l = System.nanoTime();
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File targetFile = new File(dir, l+ext);
			//����ͼƬ
			try {
				FileUtils.copyFile(logoPhoto, targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//����·��
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction";
	}
	
	/**
	 *  ��̬չʾ����ҳ��
	 *  
	 *  ʵ��Preparable�ӿں󣬴�ǰ׺prepare�ķ����������뷽��֮ǰִ�С�
	 *  
	 * ҳ�淢�������ִ�й��̣��Ⱦ�����������FileUploadInterceptor��,�˴������������������δִ�гɹ���ֱ�ӱ����ˣ��������¼���ִ�з���
	 *   ������Ҫ��������ִ��֮ǰ��inputPage��ֵ 
	 */
	public void prepareDoAddLogo(){
		inputPage = "/jsp/addLogo.jsp";
	}
	//�ж��ļ��Ƿ����
	public boolean logoPhotoExists(){
		String dir = model.getLogoPhotoPath();
		if(ValidateUtil.isValid(dir)){
			String realPath = sc.getRealPath(dir);
			return new File(realPath).exists();
		}
		return false;
	}
	
	public String analyzeSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "analyzeSurvey";
	}
	@Resource(name="statictiesService")
	private StatictiesService statictiesService;
	private Integer qid;
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}
	public String ChartOutputAction(){
		statictiesService.staticties(qid);
		return null;
	}
}
