package com.rskytech.pojo;

import java.util.Date;

/**
 * MReferMmel entity. @author MyEclipse Persistence Tools
 */

public class MReferMmel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7470702391262107188L;
	private String mmelId;
	private M2 m2;
	private Integer isRefPmmel;
	private String pmmelId;
	private String reviewResult;
	private Date reviewDate;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public MReferMmel() {
	}

	/** full constructor */
	public MReferMmel(M2 m2, Integer isRefPmmel, String pmmelId,
			String reviewResult, Date reviewDate, String remark,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.m2 = m2;
		this.isRefPmmel = isRefPmmel;
		this.pmmelId = pmmelId;
		this.reviewResult = reviewResult;
		this.reviewDate = reviewDate;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getMmelId() {
		return this.mmelId;
	}

	public void setMmelId(String mmelId) {
		this.mmelId = mmelId;
	}

	public M2 getM2() {
		return this.m2;
	}

	public void setM2(M2 m2) {
		this.m2 = m2;
	}

	public Integer getIsRefPmmel() {
		return this.isRefPmmel;
	}

	public void setIsRefPmmel(Integer isRefPmmel) {
		this.isRefPmmel = isRefPmmel;
	}

	public String getPmmelId() {
		return this.pmmelId;
	}

	public void setPmmelId(String pmmelId) {
		this.pmmelId = pmmelId;
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