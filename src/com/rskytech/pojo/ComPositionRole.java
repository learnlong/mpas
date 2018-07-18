package com.rskytech.pojo;

import java.util.Date;

/**
 * ComPositionRole entity. @author MyEclipse Persistence Tools
 */

public class ComPositionRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8684376379404701122L;
	private ComPositionRoleId id;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComPositionRole() {
	}

	/** minimal constructor */
	public ComPositionRole(ComPositionRoleId id) {
		this.id = id;
	}

	/** full constructor */
	public ComPositionRole(ComPositionRoleId id, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.id = id;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public ComPositionRoleId getId() {
		return this.id;
	}

	public void setId(ComPositionRoleId id) {
		this.id = id;
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