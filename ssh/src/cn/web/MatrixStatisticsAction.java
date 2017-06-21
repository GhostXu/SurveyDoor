package cn.web;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Question;
import cn.entity.statistics.OptionStatisticsModel;
import cn.entity.statistics.QuestionStatisticsModel;
import cn.service.StatictiesService;

@Controller
@Scope("prototype")
public class MatrixStatisticsAction extends BaseAction<Question> {

	private static final long serialVersionUID = -5785437088820882819L;
	
	private String[] colors = {
			"#ff0000",
			"#00ff00",
			"#0000ff",
			"#ffff00",
			"#ff00ff",
			"#000fff",
		};
	
	public String[] getColors() {
		return colors;
	}

	@Resource(name="statictiesService")
	private StatictiesService ss;
	private Integer qid;
	
	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}
	
	private QuestionStatisticsModel qsm;
	
	public QuestionStatisticsModel getQsm() {
		return qsm;
	}

	public void setQsm(QuestionStatisticsModel qsm) {
		this.qsm = qsm;
	}

	@Override
	public String execute() throws Exception {
		this.qsm = this.ss.staticties(qid);
		Question question = qsm.getQuestion();
		return ""+ question.getQuestionType();
	}
	
	public String getScale(Integer rindex, Integer cindex){
		int qcount = qsm.getCount();//问题总数
		int ocount = 0;//选择人数
		List<OptionStatisticsModel> osm = qsm.getOsms();
		for(OptionStatisticsModel o : osm){
			if(rindex == o.getMatrixRowIndex() && cindex == o.getMatrixColIndex()){
				ocount = o.getCount();
				break;
			}
		}
		
		float scale = 0 ;//选择人数所占百分比
		if(qcount != 0){
			scale = (float)ocount / (float)qcount * 100;
		}
		DecimalFormat format = new DecimalFormat();
		format.applyPattern("#,###.00");//采用科学计数法，并保留两位小数
		return ocount + "(" + format.format(scale) + ")";
	}
	
	public String getScale(Integer rindex, Integer cindex, Integer optIndex){
		int qcount = qsm.getCount();//问题总数
		int ocount = 0;//选择人数
		List<OptionStatisticsModel> osm = qsm.getOsms();
		for(OptionStatisticsModel o : osm){
			if(rindex == o.getMatrixRowIndex() && cindex == o.getMatrixColIndex()
					&& optIndex == o.getMatrixSelectIndex()){
				ocount = o.getCount();
				break;
			}
		}
		
		float scale = 0 ;//选择人数所占百分比
		if(qcount != 0){
			scale = (float)ocount / (float)qcount * 100;
		}
		DecimalFormat format = new DecimalFormat();
		format.applyPattern("#,###.00");//采用科学计数法，并保留两位小数
		return ocount + "(" + format.format(scale) + ")";
	}
	
	public int getPercent(Integer rindex, Integer cindex, Integer optIndex){
		int qcount = qsm.getCount();//问题总数
		int ocount = 0;//选择人数
		List<OptionStatisticsModel> osm = qsm.getOsms();
		for(OptionStatisticsModel o : osm){
			if(rindex == o.getMatrixRowIndex() && cindex == o.getMatrixColIndex()
					&& optIndex == o.getMatrixSelectIndex()){
				ocount = o.getCount();
				break;
			}
		}
		
		float scale = 0 ;//选择人数所占百分比
		if(qcount != 0){
			scale = (float)ocount / (float)qcount * 100;
		}
		return (int) scale;
	}
}
