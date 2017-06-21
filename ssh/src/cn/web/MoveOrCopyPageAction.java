package cn.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Page;
import cn.entity.Survey;
import cn.entity.User;
import cn.service.SurveyService;

@Controller
@Scope("prototype")
public class MoveOrCopyPageAction extends BaseAction<Page> implements UserAware {
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
	//ԭҳ��id
	private Integer srcPid;
	public Integer getSrcPid() {
		return srcPid;
	}
	public void setSrcPid(Integer srcPid) {
		this.srcPid = srcPid;
	}
	
	//ԭҳ��id
	private Integer targPid;
	public Integer getTargPid() {
		return targPid;
	}
	public void setTargPid(Integer targPid) {
		this.targPid = targPid;
	}
	//Ŀ��ҳ��
	private List<Survey> surveys;
	public List<Survey> getSurveys() {
		return surveys;
	}
	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}
	//�ƶ�����
	private int pos;
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	private User user;
	@Override
	public void setUser(User user) {
		this.user = user;
	}

	//��ת��ѡ����ҳ��
	public String toSelectTargetPage(){
		this.surveys = surveyService.findSurveyWithPage(user);
		return "moveOrCopyPage";
	}
	
	//�ƶ�/���Ʋ���
	public String doMoveOrCopyPage(){
		surveyService.moveOrCopyPage(srcPid,targPid,pos);
		return "designSurveyAction";
	}
}
