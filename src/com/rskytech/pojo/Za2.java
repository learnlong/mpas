package com.rskytech.pojo;

import java.util.Date;

/**
 * Za2 entity. @author MyEclipse Persistence Tools
 */

public class Za2 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1475591055141622988L;
	private String za2Id;
	private ZaMain zaMain;
	private Integer position;
	private String picContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Za2() {
	}

	/** full constructor */
	public Za2(ZaMain zaMain, Integer position, String picContent,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.zaMain = zaMain;
		this.position = position;
		this.picContent = picContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getZa2Id() {
		return this.za2Id;
	}

	public void setZa2Id(String za2Id) {
		this.za2Id = za2Id;
	}

	public ZaMain getZaMain() {
		return this.zaMain;
	}

	public void setZaMain(ZaMain zaMain) {
		this.zaMain = zaMain;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getPicContent() {
		return this.picContent;
	}

	public void setPicContent(String picContent) {
		this.picContent = picContent;
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