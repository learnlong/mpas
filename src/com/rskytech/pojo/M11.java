package com.rskytech.pojo;

import java.util.Date;

/**
 * M11 entity. @author MyEclipse Persistence Tools
 */

public class M11 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -757171213750124991L;
	private String m11Id;
	private MMain MMain;
	private String picContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public M11() {
	}

	/** full constructor */
	public M11(MMain MMain, String picContent, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.MMain = MMain;
		this.picContent = picContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getM11Id() {
		return this.m11Id;
	}

	public void setM11Id(String m11Id) {
		this.m11Id = m11Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
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