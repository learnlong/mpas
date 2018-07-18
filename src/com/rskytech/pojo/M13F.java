package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * M13F entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class M13F implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6801464218915163642L;
	private String m13fId;
	private M13 m13;
	private String failureCode;
	private String failureDesc;
	private String effectCode;
	private String effectDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set m13Cs = new HashSet(0);
	private Set m2s = new HashSet(0);

	// Constructors

	/** default constructor */
	public M13F() {
	}

	/** full constructor */
	public M13F(M13 m13, String failureCode, String failureDesc,
			String effectCode, String effectDesc, String createUser,
			Date createDate, String modifyUser, Date modifyDate, Set m13Cs,
			Set m2s) {
		this.m13 = m13;
		this.failureCode = failureCode;
		this.failureDesc = failureDesc;
		this.effectCode = effectCode;
		this.effectDesc = effectDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.m13Cs = m13Cs;
		this.m2s = m2s;
	}

	// Property accessors

	public String getM13fId() {
		return this.m13fId;
	}

	public void setM13fId(String m13fId) {
		this.m13fId = m13fId;
	}

	public M13 getM13() {
		return this.m13;
	}

	public void setM13(M13 m13) {
		this.m13 = m13;
	}

	public String getFailureCode() {
		return this.failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getFailureDesc() {
		return this.failureDesc;
	}

	public void setFailureDesc(String failureDesc) {
		this.failureDesc = failureDesc;
	}

	public String getEffectCode() {
		return this.effectCode;
	}

	public void setEffectCode(String effectCode) {
		this.effectCode = effectCode;
	}

	public String getEffectDesc() {
		return this.effectDesc;
	}

	public void setEffectDesc(String effectDesc) {
		this.effectDesc = effectDesc;
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

	public Set getM13Cs() {
		return this.m13Cs;
	}

	public void setM13Cs(Set m13Cs) {
		this.m13Cs = m13Cs;
	}

	public Set getM2s() {
		return this.m2s;
	}

	public void setM2s(Set m2s) {
		this.m2s = m2s;
	}

}