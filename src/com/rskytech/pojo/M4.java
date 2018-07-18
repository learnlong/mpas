package com.rskytech.pojo;

import java.util.Date;

/**
 * M4 entity. @author MyEclipse Persistence Tools
 */

public class M4 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1068321231917513952L;
	private String m4Id;
	private MMain MMain;
	private String taskId;
	private String ana;
	private String similar;
	private String engineerReview;
	private String engineerSuggest;
	private String groupReview;
	private String other;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public M4() {
	}

	/** full constructor */
	public M4(MMain MMain, String taskId, String ana, String similar,
			String engineerReview, String engineerSuggest, String groupReview,
			String other, String remark, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.MMain = MMain;
		this.taskId = taskId;
		this.ana = ana;
		this.similar = similar;
		this.engineerReview = engineerReview;
		this.engineerSuggest = engineerSuggest;
		this.groupReview = groupReview;
		this.other = other;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getM4Id() {
		return this.m4Id;
	}

	public void setM4Id(String m4Id) {
		this.m4Id = m4Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAna() {
		return this.ana;
	}

	public void setAna(String ana) {
		this.ana = ana;
	}

	public String getSimilar() {
		return this.similar;
	}

	public void setSimilar(String similar) {
		this.similar = similar;
	}

	public String getEngineerReview() {
		return this.engineerReview;
	}

	public void setEngineerReview(String engineerReview) {
		this.engineerReview = engineerReview;
	}

	public String getEngineerSuggest() {
		return this.engineerSuggest;
	}

	public void setEngineerSuggest(String engineerSuggest) {
		this.engineerSuggest = engineerSuggest;
	}

	public String getGroupReview() {
		return this.groupReview;
	}

	public void setGroupReview(String groupReview) {
		this.groupReview = groupReview;
	}

	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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