package com.rskytech.pojo;

import java.util.Date;

/**
 * M3Additional entity. @author MyEclipse Persistence Tools
 */

public class M3Additional implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8874900932892010695L;
	private String additionalId;
	private M3 m3;
	private String addTaskId;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public M3Additional() {
	}

	/** full constructor */
	public M3Additional(M3 m3, String addTaskId, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.m3 = m3;
		this.addTaskId = addTaskId;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getAdditionalId() {
		return this.additionalId;
	}

	public void setAdditionalId(String additionalId) {
		this.additionalId = additionalId;
	}

	public M3 getM3() {
		return this.m3;
	}

	public void setM3(M3 m3) {
		this.m3 = m3;
	}

	public String getAddTaskId() {
		return this.addTaskId;
	}

	public void setAddTaskId(String addTaskId) {
		this.addTaskId = addTaskId;
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