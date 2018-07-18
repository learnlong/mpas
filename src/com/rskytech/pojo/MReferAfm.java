package com.rskytech.pojo;

import java.util.Date;

/**
 * MReferAfm entity. @author MyEclipse Persistence Tools
 */

public class MReferAfm implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5615079484265676015L;
	private String afmId;
	private M2 m2;
	private String refAfm;
	private String reviewResult;
	private Date reviewDate;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public MReferAfm() {
	}

	/** minimal constructor */
	public MReferAfm(M2 m2) {
		this.m2 = m2;
	}

	/** full constructor */
	public MReferAfm(M2 m2, String refAfm, String reviewResult,
			Date reviewDate, String remark, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.m2 = m2;
		this.refAfm = refAfm;
		this.reviewResult = reviewResult;
		this.reviewDate = reviewDate;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getAfmId() {
		return this.afmId;
	}

	public void setAfmId(String afmId) {
		this.afmId = afmId;
	}

	public M2 getM2() {
		return this.m2;
	}

	public void setM2(M2 m2) {
		this.m2 = m2;
	}

	public String getRefAfm() {
		return this.refAfm;
	}

	public void setRefAfm(String refAfm) {
		this.refAfm = refAfm;
	}

	public String getReviewResult() {
		return this.reviewResult;
	}

	public void setReviewResult(String reviewResult) {
		this.reviewResult = reviewResult;
	}

	public Date getReviewDate() {
		return this.reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
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