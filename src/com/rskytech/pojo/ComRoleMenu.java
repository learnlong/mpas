package com.rskytech.pojo;

import java.util.Date;

/**
 * ComRoleMenu entity. @author MyEclipse Persistence Tools
 */

public class ComRoleMenu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7660396401026171664L;
	private ComRoleMenuId id;
	private Integer rtype;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComRoleMenu() {
	}

	/** minimal constructor */
	public ComRoleMenu(ComRoleMenuId id) {
		this.id = id;
	}

	/** full constructor */
	public ComRoleMenu(ComRoleMenuId id, Integer rtype, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.id = id;
		this.rtype = rtype;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public ComRoleMenuId getId() {
		return this.id;
	}

	public void setId(ComRoleMenuId id) {
		this.id = id;
	}

	public Integer getRtype() {
		return this.rtype;
	}

	public void setRtype(Integer rtype) {
		this.rtype = rtype;
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