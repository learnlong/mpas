package com.rskytech.pojo;

import java.util.Date;

/**
 * CusMrbPs entity. @author MyEclipse Persistence Tools
 */

public class CusMrbPs implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3322794409246287785L;
	private String psId;
	private ComModelSeries comModelSeries;
	private Integer psSort;
	private String psName;
	private String psUrl;
	private Integer psFlg;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusMrbPs() {
	}

	/** full constructor */
	public CusMrbPs(ComModelSeries comModelSeries, Integer psSort,
			String psName, String psUrl, Integer psFlg, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.psSort = psSort;
		this.psName = psName;
		this.psUrl = psUrl;
		this.psFlg = psFlg;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getPsId() {
		return this.psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public Integer getPsSort() {
		return this.psSort;
	}

	public void setPsSort(Integer psSort) {
		this.psSort = psSort;
	}

	public String getPsName() {
		return this.psName;
	}

	public void setPsName(String psName) {
		this.psName = psName;
	}

	public String getPsUrl() {
		return this.psUrl;
	}

	public void setPsUrl(String psUrl) {
		this.psUrl = psUrl;
	}

	public Integer getPsFlg() {
		return this.psFlg;
	}

	public void setPsFlg(Integer psFlg) {
		this.psFlg = psFlg;
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