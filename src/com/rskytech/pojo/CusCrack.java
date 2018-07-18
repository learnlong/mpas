package com.rskytech.pojo;

import java.util.Date;

/**
 * CusCrack entity. @author MyEclipse Persistence Tools
 */

public class CusCrack implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1986022419000427332L;
	private String crackId;
	private ComModelSeries comModelSeries;
	private Integer crackSort;
	private Double crackValue;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusCrack() {
	}

	/** full constructor */
	public CusCrack(ComModelSeries comModelSeries, Integer crackSort,
			Double crackValue, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.crackSort = crackSort;
		this.crackValue = crackValue;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getCrackId() {
		return this.crackId;
	}

	public void setCrackId(String crackId) {
		this.crackId = crackId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public Integer getCrackSort() {
		return this.crackSort;
	}

	public void setCrackSort(Integer crackSort) {
		this.crackSort = crackSort;
	}

	public Double getCrackValue() {
		return this.crackValue;
	}

	public void setCrackValue(Double crackValue) {
		this.crackValue = crackValue;
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