package cn.web;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Page;
import cn.entity.Question;
import cn.service.SurveyService;

@Controller
@Scope("prototype")
public class QuestionAction extends BaseAction<Question> {
	@Resource
	private SurveyService surveyService;
	
	private Integer sid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	private Integer pid;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	
	private Integer qid;
	public Integer getQid() {
		return qid;
	}
	public void setQid(Integer qid) {
		this.qid = qid;
	}

	//��ת��ѡ����������ҳ��
	public String toSelectQuestionType(){
		return "selectQuestionType";
	}
	//ѡ�����ͺ�չʾ��Ӧ����ҳ��
	public String toDesignQuestionPage(){
		return "" + model.getQuestionType();
	}
	
	//��������
	public String saveOrUpdateQuestion(){
		Page page =(Page) surveyService.getPage(pid);
		model.setPage(page);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction";
	}
	
	//�༭����
	public String editQuestion(){
		this.model = surveyService.getQuestion(qid);
		return "" + model.getQuestionType();
	}
	
	//ɾ������
	public String deleteQuestion(){
		surveyService.deleteQuestion(qid);
		return "designSurveyAction";
	}
}
