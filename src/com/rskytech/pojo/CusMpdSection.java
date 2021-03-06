package com.rskytech.pojo;

import java.util.Date;

/**
 * CusMpdSection entity. @author MyEclipse Persistence Tools
 */

public class CusMpdSection implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3470555518344862043L;
	private String sectionId;
	private CusMpdChapter cusMpdChapter;
	private String sectionCode;
	private String sectionName;
	private String sectionContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusMpdSection() {
	}

	/** full constructor */
	public CusMpdSection(CusMpdChapter cusMpdChapter, String sectionCode,
			String sectionName, String sectionContent, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.cusMpdChapter = cusMpdChapter;
		this.sectionCode = sectionCode;
		this.sectionName = sectionName;
		this.sectionContent = sectionContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getSectionId() {
		return this.sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public CusMpdChapter getCusMpdChapter() {
		return this.cusMpdChapter;
	}

	public void setCusMpdChapter(CusMpdChapter cusMpdChapter) {
		this.cusMpdChapter = cusMpdChapter;
	}

	public String getSectionCode() {
		return this.sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSectionName() {
		return this.sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionContent() {
		return this.sectionContent;
	}

	public void setSectionContent(String sectionContent) {
		this.sectionContent = sectionContent;
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