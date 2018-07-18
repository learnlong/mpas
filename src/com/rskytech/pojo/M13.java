package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * M13 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class M13 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8993420994628984521L;
	private String m13Id;
	private MMain MMain;
	private String functionCode;
	private String functionDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set m13Fs = new HashSet(0);

	// Constructors

	/** default constructor */
	public M13() {
	}

	/** full constructor */
	public M13(MMain MMain, String functionCode, String functionDesc,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set m13Fs) {
		this.MMain = MMain;
		this.functionCode = functionCode;
		this.functionDesc = functionDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.m13Fs = m13Fs;
	}

	// Property accessors

	public String getM13Id() {
		return this.m13Id;
	}

	public void setM13Id(String m13Id) {
		this.m13Id = m13Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public String getFunctionCode() {
		return this.functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getFunctionDesc() {
		return this.functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
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

	public Set getM13Fs() {
		return this.m13Fs;
	}

	public void setM13Fs(Set m13Fs) {
		this.m13Fs = m13Fs;
	}

}