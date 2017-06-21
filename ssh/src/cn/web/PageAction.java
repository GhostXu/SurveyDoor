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
	//����id
	private Integer sid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	//ҳ��id
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
	
	//��ת������ҳ��
	public String toAddPage(){
		return "addPagePage";
	}
	
	//����ҳ�棬��ת����Ƶ���ҳ��
	public String saveOrUpdatePage(){
		Survey survey = (Survey) surveyService.getSurvey(sid);
		model.setSurvey(survey);
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	
	//��ת���޸�ҳ��
	public String toEditPage(){
		this.model = (Page) surveyService.getPage(pid);
		return "addPagePage";
	}
	//�޸�ҳ�����
	public String editPageTitle(){
		System.out.println(model.getTitle());
		surveyService.saveOrUpdatePage(model);
		return "designSurveyAction";
	}
	//ɾ��ҳ��
	public String deletePage(){
		//TODO �½�����--ɾ��ҳ�� ����
		surveyService.deletePage(pid);
		return "designSurveyAction";
	}
}
