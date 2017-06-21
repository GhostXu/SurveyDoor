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

	//跳转到选择问题类型页面
	public String toSelectQuestionType(){
		return "selectQuestionType";
	}
	//选择类型后展示对应类型页面
	public String toDesignQuestionPage(){
		return "" + model.getQuestionType();
	}
	
	//保存问题
	public String saveOrUpdateQuestion(){
		Page page =(Page) surveyService.getPage(pid);
		model.setPage(page);
		surveyService.saveOrUpdateQuestion(model);
		return "designSurveyAction";
	}
	
	//编辑问题
	public String editQuestion(){
		this.model = surveyService.getQuestion(qid);
		return "" + model.getQuestionType();
	}
	
	//删除问题
	public String deleteQuestion(){
		surveyService.deleteQuestion(qid);
		return "designSurveyAction";
	}
}
