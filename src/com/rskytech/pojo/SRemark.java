package com.rskytech.pojo;

import java.util.Date;

/**
 * SRemark entity. @author MyEclipse Persistence Tools
 */

public class SRemark implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 449092964185614777L;
	private String remarkId;
	private SMain SMain;
	private String s1Remark;
	private String s4aInRemark;
	private String s4bInRemark;
	private String s4aOutRemark;
	private String s4bOutRemark;
	private String s5aInRemark;
	private String s5bInRemark;
	private String s5aOutRemark;
	private String s5bOutRemark;
	private String s7Remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String syaInRemark;
	private String sybInRemark;
	private String syaOutRemark;
	private String sybOutRemark;

	// Constructors

	/** default constructor */
	public SRemark() {
	}

	/** full constructor */
	public SRemark(SMain SMain, String s1Remark, String s4aInRemark,
			String s4bInRemark, String s4aOutRemark, String s4bOutRemark,
			String s5aInRemark, String s5bInRemark, String s5aOutRemark,
			String s5bOutRemark, String s7Remark, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.SMain = SMain;
		this.s1Remark = s1Remark;
		this.s4aInRemark = s4aInRemark;
		this.s4bInRemark = s4bInRemark;
		this.s4aOutRemark = s4aOutRemark;
		this.s4bOutRemark = s4bOutRemark;
		this.s5aInRemark = s5aInRemark;
		this.s5bInRemark = s5bInRemark;
		this.s5aOutRemark = s5aOutRemark;
		this.s5bOutRemark = s5bOutRemark;
		this.s7Remark = s7Remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getRemarkId() {
		return this.remarkId;
	}

	public String getSyaInRemark() {
		return syaInRemark;
	}

	public void setSyaInRemark(String syaInRemark) {
		this.syaInRemark = syaInRemark;
	}

	public String getSybInRemark() {
		return sybInRemark;
	}

	public void setSybInRemark(String sybInRemark) {
		this.sybInRemark = sybInRemark;
	}

	public String getSyaOutRemark() {
		return syaOutRemark;
	}

	public void setSyaOutRemark(String syaOutRemark) {
		this.syaOutRemark = syaOutRemark;
	}

	public String getSybOutRemark() {
		return sybOutRemark;
	}

	public void setSybOutRemark(String sybOutRemark) {
		this.sybOutRemark = sybOutRemark;
	}

	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}

	public SMain getSMain() {
		return this.SMain;
	}

	public void setSMain(SMain SMain) {
		this.SMain = SMain;
	}

	public String getS1Remark() {
		return this.s1Remark;
	}

	public void setS1Remark(String s1Remark) {
		this.s1Remark = s1Remark;
	}

	public String getS4aInRemark() {
		return this.s4aInRemark;
	}

	public void setS4aInRemark(String s4aInRemark) {
		this.s4aInRemark = s4aInRemark;
	}

	public String getS4bInRemark() {
		return this.s4bInRemark;
	}

	public void setS4bInRemark(String s4bInRemark) {
		this.s4bInRemark = s4bInRemark;
	}

	public String getS4aOutRemark() {
		return this.s4aOutRemark;
	}

	public void setS4aOutRemark(String s4aOutRemark) {
		this.s4aOutRemark = s4aOutRemark;
	}

	public String getS4bOutRemark() {
		return this.s4bOutRemark;
	}

	public void setS4bOutRemark(String s4bOutRemark) {
		this.s4bOutRemark = s4bOutRemark;
	}

	public String getS5aInRemark() {
		return this.s5aInRemark;
	}

	public void setS5aInRemark(String s5aInRemark) {
		this.s5aInRemark = s5aInRemark;
	}

	public String getS5bInRemark() {
		return this.s5bInRemark;
	}

	public void setS5bInRemark(String s5bInRemark) {
		this.s5bInRemark = s5bInRemark;
	}

	public String getS5aOutRemark() {
		return this.s5aOutRemark;
	}

	public void setS5aOutRemark(String s5aOutRemark) {
		this.s5aOutRemark = s5aOutRemark;
	}

	public String getS5bOutRemark() {
		return this.s5bOutRemark;
	}

	public void setS5bOutRemark(String s5bOutRemark) {
		this.s5bOutRemark = s5bOutRemark;
	}

	public String getS7Remark() {
		return this.s7Remark;
	}

	public void setS7Remark(String s7Remark) {
		this.s7Remark = s7Remark;
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