package cn.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dao.BaseDao;
import cn.entity.Answer;
import cn.entity.Question;
import cn.entity.statistics.OptionStatisticsModel;
import cn.entity.statistics.QuestionStatisticsModel;
import cn.service.StatictiesService;

@Service(value="statictiesService")
public class StatictiesServiceImpl implements StatictiesService {
	@Resource(name="questionDao")
	private BaseDao<Question> questionDao;
	@Resource(name="answerDao")
	private BaseDao<Answer> answerDao;
	
	@Override
	public QuestionStatisticsModel staticties(Integer qid) {
		QuestionStatisticsModel qsm = new QuestionStatisticsModel();
		/**
		 * 1.查询问题
		 * 2.查询参与调查的人数
		 * 3.查询每个选项人数
		 */
		Question question = questionDao.getEntityById(qid);
		int count = getCount(qid) ;//参与调查人数
		List<OptionStatisticsModel> osm = getOptionStatisticsModel(question, qid);
		qsm.setCount(count);
		qsm.setOsms(osm);
		qsm.setQuestion(question);
		return qsm;
	}
	/**
	 * 查询参与调查人数
	 * @param qid 问题id
	 * @return
	 */
	private int getCount(Integer qid) {
		String hql = "select count(*) from Answer a where a.questionId = ?";
		int count = ((Long)answerDao.uniqueResult(hql, qid)).intValue();
		return count;
	}
	/**
	 * 获取某个选项的选择人数
	 * @param qid
	 * @param optArr 
	 * @return
	 */
	private List<OptionStatisticsModel> getOptionStatisticsModel(Question question, Integer qid){
		List<OptionStatisticsModel> osms = new ArrayList<OptionStatisticsModel>();
		OptionStatisticsModel osm = null;
		String hql = "select count(*) from Answer a where a.questionId = ? and concat(',',a.answerIds,',') like ?";
		StringBuffer like = new StringBuffer();//like子句
		String[] optArr = null ;//选项
		String[] colTitleArr = null;//行标题
		String[] rowTitleArr = null;//列标题
		int ocount = 0 ;
		
		int questionType = question.getQuestionType();
		switch (questionType) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			optArr = question.getOptionArr();//选项数组
			for(int i=0 ; i<optArr.length ; i++){
				osm = new OptionStatisticsModel();
				osm.setOptionLabel(optArr[i]);//选项标签
				osm.setOptionIndex(i);//选项索引
				like.append("%,"+i +",%");
				ocount = ((Long)answerDao.uniqueResult(hql, qid, like.toString())).intValue();
				osm.setCount(ocount);
				osms.add(osm);
				like.delete(0, like.length());
			}
			break;
		case 6:
		case 7://矩阵单/多选：q6_0 = 0_0;q7_0 = 0_0,0_1.....
			colTitleArr = question.getMatrixColTitleArr();
			rowTitleArr = question.getMatrixRowTitleArr();
			for(int i=0; i<colTitleArr.length; i++){
				for(int j=0; j<rowTitleArr.length; j++){
					osm = new OptionStatisticsModel();
					osm.setMatrixColLabel(colTitleArr[i]);
					osm.setMatrixColIndex(i);
					osm.setMatrixRowLabel(rowTitleArr[j]);
					osm.setMatrixRowIndex(j);
					like.append("%,"+ i+ "_"+ j+ ",%");
					ocount = ((Long)answerDao.uniqueResult(hql, qid, like.toString())).intValue();
					osm.setCount(ocount);
					osms.add(osm);
					like.delete(0, like.length());
				}
			}
			break;
		case 8://矩阵下拉：q8_0 = 0_0_3
			colTitleArr = question.getMatrixColTitleArr();
			rowTitleArr = question.getMatrixRowTitleArr();
			optArr = question.getMatrixSelectOptionArr();
			for(int i=0; i<colTitleArr.length; i++){
				for(int j=0; j<rowTitleArr.length; j++){
					for(int k=0; k<optArr.length; k++){
						osm = new OptionStatisticsModel();
						osm.setMatrixColLabel(colTitleArr[i]);
						osm.setMatrixColIndex(i);
						osm.setMatrixRowLabel(rowTitleArr[j]);
						osm.setMatrixRowIndex(j);
						osm.setMatrixSelectLabel(optArr[k]);
						osm.setMatrixSelectIndex(k);
						like.append("%,"+ i+ "_"+ j+ "_"+ k+ ",%");
						ocount = ((Long)answerDao.uniqueResult(hql, qid, like.toString())).intValue();
						osm.setCount(ocount);
						osms.add(osm);
						like.delete(0, like.length());
					}
				}
			}
			break;
		}
		return osms;
	}
}
