package com.rskytech.pojo;

import java.util.Date;

/**
 * ComProcessDetail entity. @author MyEclipse Persistence Tools
 */

public class ComProcessDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7222031436980554923L;
	// Fields

	private String detailId;
	private ComProcess comProcess;
	private String analysisType;
	private String mainId;
	private Integer isOk;
	private String checkOpinion;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComProcessDetail() {
	}

	/** minimal constructor */
	public ComProcessDetail(String detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public ComProcessDetail(String detailId, ComProcess comProcess,
			String analysisType, String mainId, Integer isOk,
			String checkOpinion, Integer validFlag, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.detailId = detailId;
		this.comProcess = comProcess;
		this.analysisType = analysisType;
		this.mainId = mainId;
		this.isOk = isOk;
		this.checkOpinion = checkOpinion;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public ComProcess getComProcess() {
		return this.comProcess;
	}

	public void setComProcess(ComProcess comProcess) {
		this.comProcess = comProcess;
	}

	public String getAnalysisType() {
		return this.analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	public String getMainId() {
		return this.mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public Integer getIsOk() {
		return this.isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}

	public String getCheckOpinion() {
		return this.checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
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