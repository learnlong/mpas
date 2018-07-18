package com.rskytech.pojo;

import java.util.Date;

/**
 * CusDisplay entity. @author MyEclipse Persistence Tools
 */

public class CusDisplay implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1957706411837280392L;
	private String displayId;
	private ComModelSeries comModelSeries;
	private String displayWhere;
	private String diff;
	private String displayContent;
	private String diffname;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusDisplay() {
	}

	/** full constructor */
	public CusDisplay(ComModelSeries comModelSeries, String displayWhere,
			String diff, String displayContent, String diffname,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.displayWhere = displayWhere;
		this.diff = diff;
		this.displayContent = displayContent;
		this.diffname = diffname;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getDisplayId() {
		return this.displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getDisplayWhere() {
		return this.displayWhere;
	}

	public void setDisplayWhere(String displayWhere) {
		this.displayWhere = displayWhere;
	}

	public String getDiff() {
		return this.diff;
	}

	public void setDiff(String diff) {
		this.diff = diff;
	}

	public String getDisplayContent() {
		return this.displayContent;
	}

	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
	}

	public String getDiffname() {
		return this.diffname;
	}

	public void setDiffname(String diffname) {
		this.diffname = diffname;
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