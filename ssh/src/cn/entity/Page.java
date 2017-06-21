package cn.entity;

import java.util.HashSet;
import java.util.Set;

public class Page extends BaseEntity {
	
	private static final long serialVersionUID = 4438824766667581173L;
	private Integer id;
	private String title = "未命名";
	private String description;
	//排序序号
	private float orderno;
	
	/**
	 * 调查
	 * transient 表示在序列化page对象时，不串行化survey对象
	 */
	private transient Survey survey ;
	
	//问题集合
	private Set<Question> questions = new HashSet<Question>();

	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		if(id != null){
			this.orderno = id ;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getOrderno() {
		return orderno;
	}

	public void setOrderno(float orderno) {
		this.orderno = orderno;
	}

}
