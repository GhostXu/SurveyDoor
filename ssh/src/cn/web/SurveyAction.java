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
	 * 实现了UserAware接口，重写setUser方法，注入user
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
	//上传文件
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
	 * 实现ServletContextAware接口，重写该方法，目的是注入ServletContext
	 *
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.sc = context;
	}
	//错误页面
	private String inputPage;
	public String getInputPage() {
		return inputPage;
	}
	public void setInputPage(String inputPage) {
		this.inputPage = inputPage;
	}
	//新建调查
	public String newSurvey(){
		//新建一个调查页面
		this.model = surveyService.newSurvey(user);
		return "designSurvey";
	}
	
	//我的调查页面
	public String mySurveys(){
		System.out.println(user.getNickName());
		this.mySurveys = surveyService.findMySurveys(user);
		return "mySurveyList";
	}
	
	//设计调查
	public String designSurvey(){
		this.model = surveyService.getSurveyWithChildren(sid);
		return "designSurvey";
	}
	
	public String editSurvey(){
		this.model = surveyService.getSurvey(sid);
		return "editSurvey";
	}
	
	//编辑调查
	public String updateSurvey() {
		//用于重定向
		this.sid = model.getId();
		//设置关联关系
		model.setUser(user);
		this.surveyService.updateSurvey(this.model);
		return "designSurveyAction";
	}
	//删除调查
	public String deleteSurvey(){
		surveyService.deleteSurvey(sid);
		return "mySurveysAction";
	}
	//改变状态
	public String toggleStatus(){
		surveyService.updateStatus(sid);
		return "mySurveysAction";
	}
	//清楚调查
	public String clearSurvey(){
		surveyService.deleteAnswer(sid);
		return "mySurveysAction";
	}
	//转向增加logo页面
	public String toAddLogoPage(){
		return "addLogo";
	}
	//增加logo
	public String doAddLogo(){
		if(ValidateUtil.isValid(logoPhotoFileName)){
			//获取文件上传后保存路径
			String dir = sc.getRealPath("/upload");
			//文件名称
			long l = System.nanoTime();
			String ext = logoPhotoFileName.substring(logoPhotoFileName.lastIndexOf("."));
			File targetFile = new File(dir, l+ext);
			//复制图片
			try {
				FileUtils.copyFile(logoPhoto, targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//保存路径
			surveyService.updateLogoPhotoPath(sid,"/upload/" + l + ext);
		}
		return "designSurveyAction";
	}
	
	/**
	 *  动态展示错误页面
	 *  
	 *  实现Preparable接口后，带前缀prepare的方法都会先与方法之前执行。
	 *  
	 * 页面发起请求的执行过程：先经过拦截器（FileUploadInterceptor）,此处如果发生错误，拦截器未执行成功就直接报错了，不会往下继续执行方法
	 *   所以需要在拦截器执行之前给inputPage赋值 
	 */
	public void prepareDoAddLogo(){
		inputPage = "/jsp/addLogo.jsp";
	}
	//判断文件是否存在
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
