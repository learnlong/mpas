package com.rskytech.pojo;

import java.util.Date;

/**
 * M0 entity. @author MyEclipse Persistence Tools
 */

public class M0 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1125795987090640392L;
	private String m0Id;
	private MMain MMain;
	private String proCode;
	private String proName;
	private Integer safety;
	private String safetyAnswer;
	private Integer detectable;
	private String detectableAnswer;
	private Integer task;
	private String taskAnswer;
	private Integer economic;
	private String economicAnswer;
	private Integer isMsi;
	private String highestLevel;
	private String remark;
	private Integer isAddAta;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public M0() {
	}

	/** full constructor */
	public M0(MMain MMain, String proCode, String proName, Integer safety,
			String safetyAnswer, Integer detectable,
			String detectableAnswer, Integer task, String taskAnswer,
			Integer economic, String economicAnswer, Integer isMsi,
			String highestLevel, String remark, Integer isAddAta,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.MMain = MMain;
		this.proCode = proCode;
		this.proName = proName;
		this.safety = safety;
		this.safetyAnswer = safetyAnswer;
		this.detectable = detectable;
		this.detectableAnswer = detectableAnswer;
		this.task = task;
		this.taskAnswer = taskAnswer;
		this.economic = economic;
		this.economicAnswer = economicAnswer;
		this.isMsi = isMsi;
		this.highestLevel = highestLevel;
		this.remark = remark;
		this.isAddAta = isAddAta;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getM0Id() {
		return this.m0Id;
	}

	public void setM0Id(String m0Id) {
		this.m0Id = m0Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public String getProCode() {
		return this.proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getSafety() {
		return this.safety;
	}

	public void setSafety(Integer safety) {
		this.safety = safety;
	}

	public String getSafetyAnswer() {
		return this.safetyAnswer;
	}

	public void setSafetyAnswer(String safetyAnswer) {
		this.safetyAnswer = safetyAnswer;
	}

	public Integer getDetectable() {
		return this.detectable;
	}

	public void setDetectable(Integer detectable) {
		this.detectable = detectable;
	}

	public String getDetectableAnswer() {
		return this.detectableAnswer;
	}

	public void setDetectableAnswer(String detectableAnswer) {
		this.detectableAnswer = detectableAnswer;
	}

	public Integer getTask() {
		return this.task;
	}

	public void setTask(Integer task) {
		this.task = task;
	}

	public String getTaskAnswer() {
		return this.taskAnswer;
	}

	public void setTaskAnswer(String taskAnswer) {
		this.taskAnswer = taskAnswer;
	}

	public Integer getEconomic() {
		return this.economic;
	}

	public void setEconomic(Integer economic) {
		this.economic = economic;
	}

	public String getEconomicAnswer() {
		return this.economicAnswer;
	}

	public void setEconomicAnswer(String economicAnswer) {
		this.economicAnswer = economicAnswer;
	}

	public Integer getIsMsi() {
		return this.isMsi;
	}

	public void setIsMsi(Integer isMsi) {
		this.isMsi = isMsi;
	}

	public String getHighestLevel() {
		return this.highestLevel;
	}

	public void setHighestLevel(String highestLevel) {
		this.highestLevel = highestLevel;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsAddAta() {
		return this.isAddAta;
	}

	public void setIsAddAta(Integer isAddAta) {
		this.isAddAta = isAddAta;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}