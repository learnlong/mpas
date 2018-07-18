package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * MMain entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class MMain implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3131208746694730506L;
	private String msiId;
	private ComModelSeries comModelSeries;
	private ComAta comAta;
	private String effectiveness;
	private String status;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set m0s = new HashSet(0);
	private Set m11s = new HashSet(0);
	private Set m2s = new HashSet(0);
	private Set m13s = new HashSet(0);
	private Set m4s = new HashSet(0);
	private Set m3s = new HashSet(0);
	private Set m12s = new HashSet(0);
	private Set MSteps = new HashSet(0);
	private Set mSets = new HashSet(0);
	// Constructors

	/** default constructor */
	public MMain() {
	}

	/** full constructor */
	public MMain(ComModelSeries comModelSeries, ComAta comAta,
			String effectiveness, String status, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set m0s, Set m11s, Set m2s, Set m13s, Set m4s,
			Set m3s, Set m12s, Set MSteps) {
		this.comModelSeries = comModelSeries;
		this.comAta = comAta;
		this.effectiveness = effectiveness;
		this.status = status;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.m0s = m0s;
		this.m11s = m11s;
		this.m2s = m2s;
		this.m13s = m13s;
		this.m4s = m4s;
		this.m3s = m3s;
		this.m12s = m12s;
		this.MSteps = MSteps;
	}

	// Property accessors

	public String getMsiId() {
		return this.msiId;
	}

	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public ComAta getComAta() {
		return this.comAta;
	}

	public void setComAta(ComAta comAta) {
		this.comAta = comAta;
	}

	public String getEffectiveness() {
		return this.effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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

	public Set getM0s() {
		return this.m0s;
	}

	public void setM0s(Set m0s) {
		this.m0s = m0s;
	}

	public Set getM11s() {
		return this.m11s;
	}

	public void setM11s(Set m11s) {
		this.m11s = m11s;
	}

	public Set getM2s() {
		return this.m2s;
	}

	public void setM2s(Set m2s) {
		this.m2s = m2s;
	}

	public Set getM13s() {
		return this.m13s;
	}

	public void setM13s(Set m13s) {
		this.m13s = m13s;
	}

	public Set getM4s() {
		return this.m4s;
	}

	public void setM4s(Set m4s) {
		this.m4s = m4s;
	}

	public Set getM3s() {
		return this.m3s;
	}

	public void setM3s(Set m3s) {
		this.m3s = m3s;
	}

	public Set getM12s() {
		return this.m12s;
	}

	public void setM12s(Set m12s) {
		this.m12s = m12s;
	}

	public Set getMSteps() {
		return this.MSteps;
	}

	public void setMSteps(Set MSteps) {
		this.MSteps = MSteps;
	}

	public Set getmSets() {
		return mSets;
	}

	public void setmSets(Set mSets) {
		this.mSets = mSets;
	}

}