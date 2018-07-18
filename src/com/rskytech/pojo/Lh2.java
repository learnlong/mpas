package com.rskytech.pojo;

import java.util.Date;

/**
 * Lh2 entity. @author MyEclipse Persistence Tools
 */

public class Lh2 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6044659101931953631L;
	private String lh2Id;
	private LhMain lhMain;
	private String env;
	private String picContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Lh2() {
	}

	/** full constructor */
	public Lh2(LhMain lhMain, String env, String picContent, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.lhMain = lhMain;
		this.env = env;
		this.picContent = picContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLh2Id() {
		return this.lh2Id;
	}

	public void setLh2Id(String lh2Id) {
		this.lh2Id = lh2Id;
	}

	public LhMain getLhMain() {
		return this.lhMain;
	}

	public void setLhMain(LhMain lhMain) {
		this.lhMain = lhMain;
	}

	public String getEnv() {
		return this.env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getPicContent() {
		return this.picContent;
	}

	public void setPicContent(String picContent) {
		this.picContent = picContent;
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