package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComProcess entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("rawtypes")
public class ComProcess implements java.io.Serializable {

	private static final long serialVersionUID = -2754934566074186518L;
	// Fields

	private String processId;
	private ComModelSeries comModelSeries;
	private String launchUser;
	private String launchOpinion;
	private String checkUser;
	private String processStatus;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String analysisType;
	private Set comProcessDetails = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComProcess() {
	}

	/** minimal constructor */
	public ComProcess(String processId) {
		this.processId = processId;
	}

	/** full constructor */
	public ComProcess(String processId, ComModelSeries comModelSeries,
			String launchUser, String launchOpinion, String checkUser,
			String processStatus, Integer validFlag, String createUser,
			Date createDate, String modifyUser, Date modifyDate, String analysisType,
			Set comProcessDetails) {
		this.processId = processId;
		this.comModelSeries = comModelSeries;
		this.launchUser = launchUser;
		this.launchOpinion = launchOpinion;
		this.checkUser = checkUser;
		this.processStatus = processStatus;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.analysisType = analysisType;
		this.comProcessDetails = comProcessDetails;
	}

	// Property accessors

	public String getProcessId() {
		return this.processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getLaunchUser() {
		return this.launchUser;
	}

	public void setLaunchUser(String launchUser) {
		this.launchUser = launchUser;
	}

	public String getLaunchOpinion() {
		return this.launchOpinion;
	}

	public void setLaunchOpinion(String launchOpinion) {
		this.launchOpinion = launchOpinion;
	}

	public String getCheckUser() {
		return this.checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getProcessStatus() {
		return this.processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
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

	public Set getComProcessDetails() {
		return this.comProcessDetails;
	}

	public void setComProcessDetails(Set comProcessDetails) {
		this.comProcessDetails = comProcessDetails;
	}

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

}