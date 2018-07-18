package com.rskytech.pojo;

import java.util.Date;

/**
 * ComLogOperate entity. @author MyEclipse Persistence Tools
 */

public class ComLogOperate implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2900872405514572767L;
	private String logOpId;
	private ComModelSeries comModelSeries;
	private String sourceSystem;
	private Date logDate;
	private String opUser;
	private String opPage;
	private String opContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComLogOperate() {
	}

	/** full constructor */
	public ComLogOperate(ComModelSeries comModelSeries, String sourceSystem,
			Date logDate, String opUser, String opPage, String opContent,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.sourceSystem = sourceSystem;
		this.logDate = logDate;
		this.opUser = opUser;
		this.opPage = opPage;
		this.opContent = opContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLogOpId() {
		return this.logOpId;
	}

	public void setLogOpId(String logOpId) {
		this.logOpId = logOpId;
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

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getOpUser() {
		return this.opUser;
	}

	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}

	public String getOpPage() {
		return this.opPage;
	}

	public void setOpPage(String opPage) {
		this.opPage = opPage;
	}

	public String getOpContent() {
		return this.opContent;
	}

	public void setOpContent(String opContent) {
		this.opContent = opContent;
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