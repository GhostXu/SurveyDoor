package cn.entity;

import cn.util.StringUtil;

public class Question extends BaseEntity{
	
	private static final long serialVersionUID = -8917811545961733693L;
	private static final String  RN = "\r\n";
	private Integer id;
	// ����0-8
	private int questionType;
	// ����
	private String title;
	// ѡ��
	private String options;
	private String[] optionArr;
	// ������
	private boolean other;

	// ��������ʽ:0-�� 1-�ı��� 2-�����б�
	private int otherStyle;
	// ����������ѡ��
	private String otherSelectOptions;
	private String[] otherSelectOptionArr;

	// ����ʽ�б��⼯
	private String matrixRowTitles;
	private String[] matrixRowTitleArr;
	// ����ʽ�б��⼯
	private String matrixColTitles;
	private String[] matrixColTitleArr;
	// ����������ѡ�
	private String matrixSelectOptions;
	private String[] matrixSelectOptionArr;
	
	//������Question��Page֮����һ������ϵ
	private Page page ;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
		this.optionArr = StringUtil.str2arr(options, RN);
	}

	public boolean isOther() {
		return other;
	}

	public void setOther(boolean other) {
		this.other = other;
	}

	public int getOtherStyle() {
		return otherStyle;
	}

	public void setOtherStyle(int otherStyle) {
		this.otherStyle = otherStyle;
	}

	public String getOtherSelectOptions() {
		return otherSelectOptions;
	}

	public void setOtherSelectOptions(String otherSelectOptions) {
		this.otherSelectOptions = otherSelectOptions;
		this.otherSelectOptionArr = StringUtil.str2arr(otherSelectOptions, RN);
	}

	public String getMatrixRowTitles() {
		return matrixRowTitles;
	}

	public void setMatrixRowTitles(String matrixRowTitles) {
		this.matrixRowTitles = matrixRowTitles;
		this.matrixRowTitleArr = StringUtil.str2arr(matrixRowTitles, RN);
	}

	public String getMatrixColTitles() {
		return matrixColTitles;
	}

	public void setMatrixColTitles(String matrixColTitles) {
		this.matrixColTitles = matrixColTitles;
		this.matrixColTitleArr = StringUtil.str2arr(matrixColTitles, RN);
	}

	public String getMatrixSelectOptions() {
		return matrixSelectOptions;
	}

	public void setMatrixSelectOptions(String matrixSelectOptions) {
		this.matrixSelectOptions = matrixSelectOptions;
		this.matrixSelectOptionArr = StringUtil.str2arr(matrixSelectOptions, RN);
	}

	public String[] getOptionArr() {
		return optionArr;
	}

	public void setOptionArr(String[] optionArr) {
		this.optionArr = optionArr;
	}

	public String[] getOtherSelectOptionArr() {
		return otherSelectOptionArr;
	}

	public void setOtherSelectOptionArr(String[] otherSelectOptionArr) {
		this.otherSelectOptionArr = otherSelectOptionArr;
	}

	public String[] getMatrixRowTitleArr() {
		return matrixRowTitleArr;
	}

	public void setMatrixRowTitleArr(String[] matrixRowTitleArr) {
		this.matrixRowTitleArr = matrixRowTitleArr;
	}

	public String[] getMatrixColTitleArr() {
		return matrixColTitleArr;
	}

	public void setMatrixColTitleArr(String[] matrixColTitleArr) {
		this.matrixColTitleArr = matrixColTitleArr;
	}

	public String[] getMatrixSelectOptionArr() {
		return matrixSelectOptionArr;
	}

	public void setMatrixSelectOptionArr(String[] matrixSelectOptionArr) {
		this.matrixSelectOptionArr = matrixSelectOptionArr;
	}

}