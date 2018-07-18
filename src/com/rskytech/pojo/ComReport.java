package com.rskytech.pojo;

import java.util.Date;

/**
 * ComReport entity. @author MyEclipse Persistence Tools
 */

public class ComReport implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3155126827105632267L;
	private String reportId;
	private ComModelSeries comModelSeries;
	private String reportType;
	private String generateId;
	private Integer showLevel;
	private String versionNo;
	private Date versionDate;
	private String versionUser;
	private String versionDesc;
	private String reportWordUrl;
	private String reportExcelUrl;
	private String reportPdfUrl;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String reportName;
	private String reportStatus;

	// Constructors

	/** default constructor */
	public ComReport() {
	}

	/** full constructor */
	public ComReport(ComModelSeries comModelSeries, String reportType,
			String generateId, Integer showLevel, String versionNo,
			Date versionDate, String versionUser, String versionDesc,
			String reportWordUrl, String reportExcelUrl, String reportPdfUrl, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, String reportName, String reportStatus) {
		this.comModelSeries = comModelSeries;
		this.reportType = reportType;
		this.generateId = generateId;
		this.showLevel = showLevel;
		this.versionNo = versionNo;
		this.versionDate = versionDate;
		this.versionUser = versionUser;
		this.versionDesc = versionDesc;
		this.reportWordUrl = reportWordUrl;
		this.reportExcelUrl = reportExcelUrl;
		this.reportPdfUrl = reportPdfUrl;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.reportName = reportName;
		this.reportStatus = reportStatus;
	}

	// Property accessors

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getGenerateId() {
		return this.generateId;
	}

	public void setGenerateId(String generateId) {
		this.generateId = generateId;
	}

	public Integer getShowLevel() {
		return this.showLevel;
	}

	public void setShowLevel(Integer showLevel) {
		this.showLevel = showLevel;
	}

	public String getVersionNo() {
		return this.versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public Date getVersionDate() {
		return this.versionDate;
	}

	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	public String getVersionUser() {
		return this.versionUser;
	}

	public void setVersionUser(String versionUser) {
		this.versionUser = versionUser;
	}

	public String getVersionDesc() {
		return this.versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getReportWordUrl() {
		return this.reportWordUrl;
	}

	public void setReportWordUrl(String reportWordUrl) {
		this.reportWordUrl = reportWordUrl;
	}

	public String getReportPdfUrl() {
		return this.reportPdfUrl;
	}

	public void setReportPdfUrl(String reportPdfUrl) {
		this.reportPdfUrl = reportPdfUrl;
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReportExcelUrl() {
		return reportExcelUrl;
	}

	public void setReportExcelUrl(String reportExcelUrl) {
		this.reportExcelUrl = reportExcelUrl;
	}

}