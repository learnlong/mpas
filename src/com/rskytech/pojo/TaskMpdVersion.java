package com.rskytech.pojo;

import java.util.Date;

public class TaskMpdVersion implements java.io.Serializable {

	private String verId;
	private ComModelSeries comModelSeries;
	private String versionType;
	private String versionNo;
	private String versionDescCn;
	private String versionDescEn;
	private String reportUrl;
	private String wordReportUrl;
	private String status;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public TaskMpdVersion() {
	}

	/** full constructor */
	public TaskMpdVersion(ComModelSeries comModelSeries, String versionType,
			String versionNo, String versionDescCn, String versionDescEn, String reportUrl, String status, 
			Integer validFlag, String createUser, Date createDate, String modifyUser, Date modifyDate,String wordReportUrl) {
		this.comModelSeries = comModelSeries;
		this.versionType = versionType;
		this.versionNo = versionNo;
		this.versionDescCn = versionDescCn;
		this.versionDescEn = versionDescEn;
		this.reportUrl = reportUrl;
		this.wordReportUrl = wordReportUrl;
		this.status = status;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getVerId() {
		return this.verId;
	}

	public void setVerId(String verId) {
		this.verId = verId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getVersionType() {
		return this.versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getReportUrl() {
		return this.reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getWordReportUrl() {
		return wordReportUrl;
	}

	public void setWordReportUrl(String wordReportUrl) {
		this.wordReportUrl = wordReportUrl;
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

	public String getVersionDescCn() {
		return versionDescCn;
	}

	public void setVersionDescCn(String versionDescCn) {
		this.versionDescCn = versionDescCn;
	}

	public String getVersionDescEn() {
		return versionDescEn;
	}

	public void setVersionDescEn(String versionDescEn) {
		this.versionDescEn = versionDescEn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
