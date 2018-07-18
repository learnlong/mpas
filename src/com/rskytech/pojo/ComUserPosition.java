package com.rskytech.pojo;

import java.util.Date;

/**
 * ComUserPosition entity. @author MyEclipse Persistence Tools
 */

public class ComUserPosition implements java.io.Serializable {

	// Fields

	private ComUserPositionId id;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComUserPosition() {
	}

	/** minimal constructor */
	public ComUserPosition(ComUserPositionId id) {
		this.id = id;
	}

	/** full constructor */
	public ComUserPosition(ComUserPositionId id, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.id = id;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public ComUserPositionId getId() {
		return this.id;
	}

	public void setId(ComUserPositionId id) {
		this.id = id;
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