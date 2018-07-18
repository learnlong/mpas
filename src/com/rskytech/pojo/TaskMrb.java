package com.rskytech.pojo;

import java.util.Date;

/**
 * TaskMrb entity. @author MyEclipse Persistence Tools
 */

public class TaskMrb implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4760281545501901157L;
	private String mrbId;
	private ComModelSeries comModelSeries;
	private String sourceSystem;
	private String sourceAnaId;
	private String sourceWhere;
	private String mrbCode;
	private String failureCauseType;
	private String taskType;
	private String taskDesc;
	private String reachWay;
	private String taskIntervalOriginal;
	private String ownArea;
	private String effectiveness;
	private String anyContent;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public TaskMrb() {
	}

	/** full constructor */
	public TaskMrb(ComModelSeries comModelSeries, String sourceSystem,
			String sourceAnaId, String sourceWhere, String mrbCode,
			String failureCauseType, String taskType, String taskDesc,
			String reachWay, String taskIntervalOriginal, String ownArea,
			String effectiveness, String anyContent, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.sourceSystem = sourceSystem;
		this.sourceAnaId = sourceAnaId;
		this.sourceWhere = sourceWhere;
		this.mrbCode = mrbCode;
		this.failureCauseType = failureCauseType;
		this.taskType = taskType;
		this.taskDesc = taskDesc;
		this.reachWay = reachWay;
		this.taskIntervalOriginal = taskIntervalOriginal;
		this.ownArea = ownArea;
		this.effectiveness = effectiveness;
		this.anyContent = anyContent;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getMrbId() {
		return this.mrbId;
	}

	public void setMrbId(String mrbId) {
		this.mrbId = mrbId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getSourceSystem() {
		return this.sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceAnaId() {
		return this.sourceAnaId;
	}

	public void setSourceAnaId(String sourceAnaId) {
		this.sourceAnaId = sourceAnaId;
	}

	public String getSourceWhere() {
		return this.sourceWhere;
	}

	public void setSourceWhere(String sourceWhere) {
		this.sourceWhere = sourceWhere;
	}

	public String getMrbCode() {
		return this.mrbCode;
	}

	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}

	public String getFailureCauseType() {
		return this.failureCauseType;
	}

	public void setFailureCauseType(String failureCauseType) {
		this.failureCauseType = failureCauseType;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskDesc() {
		return this.taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getReachWay() {
		return this.reachWay;
	}

	public void setReachWay(String reachWay) {
		this.reachWay = reachWay;
	}

	public String getTaskIntervalOriginal() {
		return this.taskIntervalOriginal;
	}

	public void setTaskIntervalOriginal(String taskIntervalOriginal) {
		this.taskIntervalOriginal = taskIntervalOriginal;
	}

	public String getOwnArea() {
		return this.ownArea;
	}

	public void setOwnArea(String ownArea) {
		this.ownArea = ownArea;
	}

	public String getEffectiveness() {
		return this.effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}

	public String getAnyContent() {
		return this.anyContent;
	}

	public void setAnyContent(String anyContent) {
		this.anyContent = anyContent;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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