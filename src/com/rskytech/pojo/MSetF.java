package com.rskytech.pojo;

import java.util.Date;



public class MSetF implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -6801464218915163642L;
	private String msetfId;
	private MSet mset;
	private String failureCode;
	private String failureDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public MSetF() {
	}


	/** full constructor */
	public MSetF(String msetfId, MSet mset, String failureCode,
			String failureDesc, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		super();
		this.msetfId = msetfId;
		this.mset = mset;
		this.failureCode = failureCode;
		this.failureDesc = failureDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getMsetfId() {
		return msetfId;
	}


	public void setMsetfId(String msetfId) {
		this.msetfId = msetfId;
	}


	public MSet getMset() {
		return mset;
	}


	public void setMset(MSet mset) {
		this.mset = mset;
	}


	public String getFailureCode() {
		return failureCode;
	}


	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}


	public String getFailureDesc() {
		return failureDesc;
	}


	public void setFailureDesc(String failureDesc) {
		this.failureDesc = failureDesc;
	}


	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public String getModifyUser() {
		return modifyUser;
	}


	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}


	public Date getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}