package com.rskytech.pojo;

import java.util.Date;

/**
 * CusInterval entity. @author MyEclipse Persistence Tools
 */

public class CusInterval implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1332167159964085516L;
	private String intervalId;
	private ComModelSeries comModelSeries;
	private String anaFlg;
	private String internalFlg;
	private Integer intervalLevel;
	private String intervalValue;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusInterval() {
	}

	/** full constructor */
	public CusInterval(ComModelSeries comModelSeries, String anaFlg,
			String internalFlg, Integer intervalLevel, String intervalValue,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.anaFlg = anaFlg;
		this.internalFlg = internalFlg;
		this.intervalLevel = intervalLevel;
		this.intervalValue = intervalValue;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getIntervalId() {
		return this.intervalId;
	}

	public void setIntervalId(String intervalId) {
		this.intervalId = intervalId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getAnaFlg() {
		return this.anaFlg;
	}

	public void setAnaFlg(String anaFlg) {
		this.anaFlg = anaFlg;
	}

	public String getInternalFlg() {
		return this.internalFlg;
	}

	public void setInternalFlg(String internalFlg) {
		this.internalFlg = internalFlg;
	}

	public Integer getIntervalLevel() {
		return this.intervalLevel;
	}

	public void setIntervalLevel(Integer intervalLevel) {
		this.intervalLevel = intervalLevel;
	}

	public String getIntervalValue() {
		return this.intervalValue;
	}

	public void setIntervalValue(String intervalValue) {
		this.intervalValue = intervalValue;
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