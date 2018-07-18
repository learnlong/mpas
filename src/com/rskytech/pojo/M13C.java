package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * M13C entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class M13C implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7484568563260361893L;
	private String m13cId;
	private M13F m13F;
	private String causeCode;
	private String causeDesc;
	private Integer isRef;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set m3s = new HashSet(0);
	private Set MReferMsis = new HashSet(0);
	private String msetfId;

	// Constructors

	/** default constructor */
	public M13C() {
	}

	/** full constructor */
	public M13C(M13F m13F, String causeCode, String causeDesc,
			Integer isRef, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set m3s, Set MReferMsis) {
		this.m13F = m13F;
		this.causeCode = causeCode;
		this.causeDesc = causeDesc;
		this.isRef = isRef;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.m3s = m3s;
		this.MReferMsis = MReferMsis;
	}

	// Property accessors

	public String getM13cId() {
		return this.m13cId;
	}

	public void setM13cId(String m13cId) {
		this.m13cId = m13cId;
	}

	public M13F getM13F() {
		return this.m13F;
	}

	public void setM13F(M13F m13F) {
		this.m13F = m13F;
	}

	public String getCauseCode() {
		return this.causeCode;
	}

	public void setCauseCode(String causeCode) {
		this.causeCode = causeCode;
	}

	public String getCauseDesc() {
		return this.causeDesc;
	}

	public void setCauseDesc(String causeDesc) {
		this.causeDesc = causeDesc;
	}

	public Integer getIsRef() {
		return this.isRef;
	}

	public void setIsRef(Integer isRef) {
		this.isRef = isRef;
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

	public Set getM3s() {
		return this.m3s;
	}

	public void setM3s(Set m3s) {
		this.m3s = m3s;
	}

	public Set getMReferMsis() {
		return this.MReferMsis;
	}

	public void setMReferMsis(Set MReferMsis) {
		this.MReferMsis = MReferMsis;
	}

	public String getMsetfId() {
		return msetfId;
	}

	public void setMsetfId(String msetfId) {
		this.msetfId = msetfId;
	}
	

}