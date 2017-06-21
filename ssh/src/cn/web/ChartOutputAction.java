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
	
	/* 平面饼图 */
	private static final int CHARTTYPE_PIE_2D = 0;
	/* 立体饼图 */
	private static final int CHARTTYPE_PIE_3D = 1;
	/* 水平平面柱状图 */
	private static final int CHARTTYPE_BAR_2D_H = 2;
	/* 竖直平面柱状图 */
	private static final int CHARTTYPE_BAR_2D_V = 3;
	/* 水平立体柱状图 */
	private static final int CHARTTYPE_BAR_3D_H = 4;
	/* 竖直立体柱状图 */
	private static final int CHARTTYPE_BAR_3D_V = 5;
	/* 平面折线图 */
	private static final int CHARTTYPE_LINE_2D = 6;
	/* 立体折线图 */
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
		//TODO 图片显示失败，但是标题和legend能显示
		JFreeChart chart = null;
		try{
			Font font = new Font("宋体", 0, 20);
			DefaultPieDataset dataset = null;//饼图数据
			DefaultCategoryDataset cateds = null;//
			//数据处理
			QuestionStatisticsModel qsm = statictiesService.staticties(qid);
			if(chartType < 2){//饼图
				dataset = new DefaultPieDataset();
				for(OptionStatisticsModel osm : qsm.getOsms()){
					dataset.setValue(osm.getOptionLabel(), osm.getCount());
				}
			}else{//非饼图
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
			//图形样式处理
			chart.getTitle().setFont(font);//字体
			chart.getLegend().setItemFont(font);
			
			return chart;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
