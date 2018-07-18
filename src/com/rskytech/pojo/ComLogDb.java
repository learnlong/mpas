package com.rskytech.pojo;

import java.util.Date;

/**
 * ComLogDb entity. @author MyEclipse Persistence Tools
 */

public class ComLogDb implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1653563496203265427L;
	private String logDbId;
	private Date logDate;
	private String opContent;
	private String modTable;
	private String dbOperate;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComLogDb() {
	}

	/** full constructor */
	public ComLogDb(Date logDate, String opContent, String modTable,
			String dbOperate, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.logDate = logDate;
		this.opContent = opContent;
		this.modTable = modTable;
		this.dbOperate = dbOperate;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLogDbId() {
		return this.logDbId;
	}

	public void setLogDbId(String logDbId) {
		this.logDbId = logDbId;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getOpContent() {
		return this.opContent;
	}

	public void setOpContent(String opContent) {
		this.opContent = opContent;
	}

	public String getModTable() {
		return this.modTable;
	}

	public void setModTable(String modTable) {
		this.modTable = modTable;
	}

	public String getDbOperate() {
		return this.dbOperate;
	}

	public void setDbOperate(String dbOperate) {
		this.dbOperate = dbOperate;
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