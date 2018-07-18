package com.rskytech.pojo;

import java.util.Date;

/**
 * CusLevel entity. @author MyEclipse Persistence Tools
 */

public class CusLevel implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3528777298103260225L;
	private String levelId;
	private ComModelSeries comModelSeries;
	private String anaFlg;
	private String itemId;
	private Integer levelValue;
	private String levelName;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusLevel() {
	}

	/** full constructor */
	public CusLevel(ComModelSeries comModelSeries, String anaFlg,
			String itemId, Integer levelValue, String levelName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.anaFlg = anaFlg;
		this.itemId = itemId;
		this.levelValue = levelValue;
		this.levelName = levelName;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLevelId() {
		return this.levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
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

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Integer getLevelValue() {
		return this.levelValue;
	}

	public void setLevelValue(Integer levelValue) {
		this.levelValue = levelValue;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
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