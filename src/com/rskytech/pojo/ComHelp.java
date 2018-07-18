package com.rskytech.pojo;

import java.util.Date;

/**
 * ComHelp entity. @author MyEclipse Persistence Tools
 */

public class ComHelp implements java.io.Serializable {

	private static final long serialVersionUID = -2543001671751972428L;
	
	private String helpId;
	private ComModelSeries comModelSeries;
	private String helpWhere;
	private String content;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComHelp() {
	}

	/** full constructor */
	public ComHelp(ComModelSeries comModelSeries, String helpWhere,
			String content, Integer validFlag, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.helpWhere = helpWhere;
		this.content = content;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getHelpId() {
		return this.helpId;
	}

	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getHelpWhere() {
		return this.helpWhere;
	}

	public void setHelpWhere(String helpWhere) {
		this.helpWhere = helpWhere;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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