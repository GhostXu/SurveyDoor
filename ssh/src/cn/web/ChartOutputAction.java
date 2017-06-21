package cn.web;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.util.EnhancedOption;

import cn.entity.Question;
import cn.entity.statistics.OptionStatisticsModel;
import cn.entity.statistics.QuestionStatisticsModel;
import cn.service.StatictiesService;

@Controller
@Scope("prototype")
public class ChartOutputAction extends BaseAction<Question> {

	private static final long serialVersionUID = 5172931769533732448L;
	
	/* ƽ���ͼ */
	private static final int CHARTTYPE_PIE_2D = 0;
	/* �����ͼ */
	private static final int CHARTTYPE_PIE_3D = 1;
	/* ˮƽƽ����״ͼ */
	private static final int CHARTTYPE_BAR_2D_H = 2;
	/* ��ֱƽ����״ͼ */
	private static final int CHARTTYPE_BAR_2D_V = 3;
	/* ˮƽ������״ͼ */
	private static final int CHARTTYPE_BAR_3D_H = 4;
	/* ��ֱ������״ͼ */
	private static final int CHARTTYPE_BAR_3D_V = 5;
	/* ƽ������ͼ */
	private static final int CHARTTYPE_LINE_2D = 6;
	/* ��������ͼ */
	private static final int CHARTTYPE_LINE_3D = 7;
	
	@Resource(name="statictiesService")
	private StatictiesService statictiesService;
	private Integer qid;
	private int chartType;
	
	public Integer getQid() {
		return qid;
	}

	public void setQid(Integer qid) {
		this.qid = qid;
	}

	public int getChartType() {
		return chartType;
	}

	public void setChartType(int chartType) {
		this.chartType = chartType;
	}
	
	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public JFreeChart getChart(){
		//TODO ͼƬ��ʾʧ�ܣ����Ǳ����legend����ʾ
		JFreeChart chart = null;
		try{
			Font font = new Font("����", 0, 20);
			DefaultPieDataset dataset = null;//��ͼ����
			DefaultCategoryDataset cateds = null;//
			//���ݴ���
			QuestionStatisticsModel qsm = statictiesService.staticties(qid);
			if(chartType < 2){//��ͼ
				dataset = new DefaultPieDataset();
				for(OptionStatisticsModel osm : qsm.getOsms()){
					dataset.setValue(osm.getOptionLabel(), osm.getCount());
				}
			}else{//�Ǳ�ͼ
				cateds = new DefaultCategoryDataset();
				for(OptionStatisticsModel osm : qsm.getOsms()){
					cateds.setValue(osm.getCount(), osm.getOptionLabel(), "");
				}
			}/*
			Pie pie = new Pie();
			EnhancedOption option = new EnhancedOption();
			QuestionStatisticsModel qsm = statictiesService.staticties(qid);
			for(OptionStatisticsModel osm: qsm.getOsms()){
				option.legend(osm.getOptionLabel());
				pie.data(osm.getCount());
			}
			option.series(pie);
			String opt = JSON.toJSONString(option);
			res.setAttribute("option", opt);
			return SUCCESS;*/
			

			switch (chartType) {
			case CHARTTYPE_PIE_2D:
				chart = ChartFactory.createPieChart(qsm.getQuestion().getTitle(),dataset,true,false,false);
				break;
			case CHARTTYPE_PIE_3D:
				chart = ChartFactory.createPieChart3D(qsm.getQuestion().getTitle(),dataset,true,true,true);
				break;
			case CHARTTYPE_LINE_2D:
				chart = ChartFactory.createLineChart(qsm.getQuestion().getTitle(), qsm.getQuestion().getOptions(),
						ValueAxis.DEFAULT_RANGE.toString(), cateds, PlotOrientation.HORIZONTAL, true, false, false);
				break;
			case CHARTTYPE_LINE_3D:
				chart = ChartFactory.createLineChart3D(qsm.getQuestion().getTitle(), qsm.getQuestion().getOptions(),
						ValueAxis.DEFAULT_RANGE.toString(), cateds, PlotOrientation.HORIZONTAL, true, true, true);
				break;
			case CHARTTYPE_BAR_2D_H:
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,PlotOrientation.HORIZONTAL,true,false,false);
				break;
			case CHARTTYPE_BAR_2D_V:
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,PlotOrientation.VERTICAL,true,false,false);
				break;
			case CHARTTYPE_BAR_3D_H:
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,PlotOrientation.HORIZONTAL,true,true,true);
				break;
			case CHARTTYPE_BAR_3D_V:
				chart = ChartFactory.createBarChart(qsm.getQuestion().getTitle(), "", "", cateds,PlotOrientation.VERTICAL,true,true,true);
				break;
			}
			//ͼ����ʽ����
			chart.getTitle().setFont(font);//����
			chart.getLegend().setItemFont(font);
			
			return chart;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
