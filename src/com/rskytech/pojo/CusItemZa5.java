package com.rskytech.pojo;

import java.util.Date;

/**
 * CusItemZa5 entity. @author MyEclipse Persistence Tools
 */

public class CusItemZa5 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3002388162898329575L;
	private String itemZa5Id;
	private ComModelSeries comModelSeries;
	private String parentId;
	private Integer itemLevel;
	private Integer isLeafNode;
	private String itemCode;
	private String itemName;
	private String level1Algorithm;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusItemZa5() {
	}

	/** full constructor */
	public CusItemZa5(ComModelSeries comModelSeries, String parentId,
			Integer itemLevel, Integer isLeafNode, String itemCode,
			String itemName, String level1Algorithm, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.parentId = parentId;
		this.itemLevel = itemLevel;
		this.isLeafNode = isLeafNode;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.level1Algorithm = level1Algorithm;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getItemZa5Id() {
		return this.itemZa5Id;
	}

	public void setItemZa5Id(String itemZa5Id) {
		this.itemZa5Id = itemZa5Id;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getItemLevel() {
		return this.itemLevel;
	}

	public void setItemLevel(Integer itemLevel) {
		this.itemLevel = itemLevel;
	}

	public Integer getIsLeafNode() {
		return this.isLeafNode;
	}

	public void setIsLeafNode(Integer isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getLevel1Algorithm() {
		return this.level1Algorithm;
	}

	public void setLevel1Algorithm(String level1Algorithm) {
		this.level1Algorithm = level1Algorithm;
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