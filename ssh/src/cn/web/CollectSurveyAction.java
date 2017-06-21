package cn.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.entity.Answer;
import cn.entity.Page;
import cn.entity.Question;
import cn.entity.Survey;
import cn.entity.statistics.OptionStatisticsModel;
import cn.entity.statistics.QuestionStatisticsModel;
import cn.service.StatictiesService;
import cn.service.SurveyService;

@Controller
@Scope("prototype")
public class CollectSurveyAction extends BaseAction<Survey> {

	private static final long serialVersionUID = -8541123483219207048L;
	
	@Resource(name="statictiesService")
	private StatictiesService statictiesService;
	@Resource(name="surveyService")
	private SurveyService surveyService;
	
	private Integer sid;
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public InputStream getIs(){
		List<Answer> answers = surveyService.getAnswer(sid);//获取到所有答案
		List<Question> questions = surveyService.getQuestions(sid);//获取所有问题
		
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = null;
			
			HSSFCellStyle style = wb.createCellStyle();
			style.setWrapText(true);
			//设置表头
			Map<Integer, Integer> qidIndexMap = new HashMap<Integer, Integer>();
			for(int i=0; i<questions.size(); i++){
				cell = row.createCell(i);
				cell.setCellValue(questions.get(i).getTitle());//问题索引
				cell.setCellStyle(style);
				sheet.setColumnWidth(i, 8000);//列宽
				qidIndexMap.put(questions.get(i).getId(), i);
			}
			//添加答案
			String oldUuid = "";
			String newUuid = "";
			int rowIndex = 0 ;
			for(Answer answer : answers){
				newUuid = answer.getUuid();
				if(!oldUuid.equals(newUuid)){
					oldUuid = newUuid;
					rowIndex ++;
					row = sheet.createRow(rowIndex);
				}
				cell = row.createCell(qidIndexMap.get(answer.getQuestionId()));
				cell.setCellValue(answer.getAnswerIds());
			}
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			wb.write(boas);
			return new ByteArrayInputStream(boas.toByteArray());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
