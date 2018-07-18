package com.rskytech.pojo;

import java.util.Date;

/**
 * ComFile entity. @author MyEclipse Persistence Tools
 */

public class ComFile implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3574786148686465132L;
	// Fields

	private String fileId;
	private ComDirectory comDirectory;
	private String fileName;
	private String fileUrl;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String appName;

	// Constructors

	/** default constructor */
	public ComFile() {
	}

	/** full constructor */
	public ComFile(ComDirectory comDirectory, String fileName, String fileUrl,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, String appName) {
		this.comDirectory = comDirectory;
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.appName = appName;
	}

	// Property accessors

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public ComDirectory getComDirectory() {
		return this.comDirectory;
	}

	public void setComDirectory(ComDirectory comDirectory) {
		this.comDirectory = comDirectory;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}