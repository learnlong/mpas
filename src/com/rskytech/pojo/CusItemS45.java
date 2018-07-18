package com.rskytech.pojo;

import java.util.Date;

/**
 * CusItemS45 entity. @author MyEclipse Persistence Tools
 */

public class CusItemS45 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4801450002626559444L;
	private String itemS45Id;
	private ComModelSeries comModelSeries;
	private String itemName;
	private String itemFlg;
	private Integer itemSort;
	private String stepFlg;
	private Integer validFlag;
	private String itemAlgorithm;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusItemS45() {
	}

	/** full constructor */
	public CusItemS45(ComModelSeries comModelSeries, String itemName,
			String itemFlg, Integer itemSort, String stepFlg,
			Integer validFlag, String itemAlgorithm, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.itemName = itemName;
		this.itemFlg = itemFlg;
		this.itemSort = itemSort;
		this.stepFlg = stepFlg;
		this.validFlag = validFlag;
		this.itemAlgorithm = itemAlgorithm;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getItemS45Id() {
		return this.itemS45Id;
	}

	public void setItemS45Id(String itemS45Id) {
		this.itemS45Id = itemS45Id;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemFlg() {
		return this.itemFlg;
	}

	public void setItemFlg(String itemFlg) {
		this.itemFlg = itemFlg;
	}

	public Integer getItemSort() {
		return this.itemSort;
	}

	public void setItemSort(Integer itemSort) {
		this.itemSort = itemSort;
	}

	public String getStepFlg() {
		return this.stepFlg;
	}

	public void setStepFlg(String stepFlg) {
		this.stepFlg = stepFlg;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public String getItemAlgorithm() {
		return this.itemAlgorithm;
	}

	public void setItemAlgorithm(String itemAlgorithm) {
		this.itemAlgorithm = itemAlgorithm;
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