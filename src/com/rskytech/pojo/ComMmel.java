package com.rskytech.pojo;

import java.util.Date;

/**
 * ComMmel entity. @author MyEclipse Persistence Tools
 */

public class ComMmel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1191867036148150603L;
	private String mmelId;
	private ComModelSeries comModelSeries;
	private String mmelCode;
	private String mmelName;
	private String remark;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComMmel() {
	}

	/** full constructor */
	public ComMmel(ComModelSeries comModelSeries, String mmelCode,
			String mmelName, String remark, Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.mmelCode = mmelCode;
		this.mmelName = mmelName;
		this.remark = remark;
		this.validFlag = validFlag;
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

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getMmelCode() {
		return this.mmelCode;
	}

	public void setMmelCode(String mmelCode) {
		this.mmelCode = mmelCode;
	}

	public String getMmelName() {
		return this.mmelName;
	}

	public void setMmelName(String mmelName) {
		this.mmelName = mmelName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getValidFlag() {
		return validFlag;
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