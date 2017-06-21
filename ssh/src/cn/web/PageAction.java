package cn.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Page;
import cn.entity.Survey;
import cn.service.SurveyService;

@Controller
@Scope("prototype")
public class PageAction extends BaseAction<Page> {
	@Resource(name="surveyService")
	private SurveyService surveyService;
	//调查id
	private Integer sid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	//页面id
	private Integer id;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	private Integer pid;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	//跳转到增加页面
	public String toAddPage(){
		return "addPagePage";
	}
	
	//保存页面，跳转到设计调查页面
	public String saveOrUpdatePage(){
		Survey survey = (Survey) surveyService.getSurvey(sid);
		model.setSurvey(survey);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	
	//跳转到修改页面
	public String toEditPage(){
		this.model = (Page) surveyService.getPage(pid);
		return "addPagePage";
	}
	//修改页面标题
	public String editPageTitle(){
		System.out.println(model.getTitle());
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	//删除页面
	public String deletePage(){
		//TODO 新建调查--删除页面 报错
		surveyService.deletePage(pid);
		return "designSurveyAction";
	}
}
