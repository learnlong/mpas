package com.rskytech.pojo;

import java.util.Date;

/**
 * Lh1a entity. @author MyEclipse Persistence Tools
 */

public class Lh1a implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1180440513320116327L;
	private String picId;
	private LhMain lhMain;
	private String content1;
	private String content2;
	private String content3;
	private String picContent;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Lh1a() {
	}

	/** full constructor */
	public Lh1a(LhMain lhMain, String content1, String content2,
			String content3, String picContent, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.lhMain = lhMain;
		this.content1 = content1;
		this.content2 = content2;
		this.content3 = content3;
		this.picContent = picContent;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getPicId() {
		return this.picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public LhMain getLhMain() {
		return this.lhMain;
	}

	public void setLhMain(LhMain lhMain) {
		this.lhMain = lhMain;
	}

	public String getContent1() {
		return this.content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getContent2() {
		return this.content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getContent3() {
		return this.content3;
	}

	public void setContent3(String content3) {
		this.content3 = content3;
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