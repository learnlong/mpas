package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * LhMain entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class LhMain implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5402493677910174897L;
	private String hsiId;
	private ComArea comArea;
	private ComModelSeries comModelSeries;
	private String hsiCode;
	private String hsiName;
	private String lhCompName;
	private String ataCode;
	private String ipvOpvpOpve;
	private String refHsiCode;
	private String anaUser;
	private String effectiveness;
	private String status;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set lh4s = new HashSet(0);
	private Set lh1as = new HashSet(0);
	private Set lhSteps = new HashSet(0);
	private Set lh5s = new HashSet(0);
	private Set lh2s = new HashSet(0);
	private Set lh3s = new HashSet(0);
	private Set lh1s = new HashSet(0);

	// Constructors

	/** default constructor */
	public LhMain() {
	}

	/** full constructor */
	public LhMain(ComArea comArea, ComModelSeries comModelSeries,
			String hsiCode, String hsiName, String lhCompName, String ataCode,
			String ipvOpvpOpve, String refHsiCode, String anaUser,
			String effectiveness, String status, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set lh4s, Set lh1as, Set lhSteps, Set lh5s,
			Set lh2s, Set lh3s, Set lh1s) {
		this.comArea = comArea;
		this.comModelSeries = comModelSeries;
		this.hsiCode = hsiCode;
		this.hsiName = hsiName;
		this.lhCompName = lhCompName;
		this.ataCode = ataCode;
		this.ipvOpvpOpve = ipvOpvpOpve;
		this.refHsiCode = refHsiCode;
		this.anaUser = anaUser;
		this.effectiveness = effectiveness;
		this.status = status;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.lh4s = lh4s;
		this.lh1as = lh1as;
		this.lhSteps = lhSteps;
		this.lh5s = lh5s;
		this.lh2s = lh2s;
		this.lh3s = lh3s;
		this.lh1s = lh1s;
	}

	// Property accessors

	public String getHsiId() {
		return this.hsiId;
	}

	public void setHsiId(String hsiId) {
		this.hsiId = hsiId;
	}

	public ComArea getComArea() {
		return this.comArea;
	}

	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getHsiCode() {
		return this.hsiCode;
	}

	public void setHsiCode(String hsiCode) {
		this.hsiCode = hsiCode;
	}

	public String getHsiName() {
		return this.hsiName;
	}

	public void setHsiName(String hsiName) {
		this.hsiName = hsiName;
	}

	public String getLhCompName() {
		return this.lhCompName;
	}

	public void setLhCompName(String lhCompName) {
		this.lhCompName = lhCompName;
	}

	public String getAtaCode() {
		return this.ataCode;
	}

	public void setAtaCode(String ataCode) {
		this.ataCode = ataCode;
	}

	public String getIpvOpvpOpve() {
		return this.ipvOpvpOpve;
	}

	public void setIpvOpvpOpve(String ipvOpvpOpve) {
		this.ipvOpvpOpve = ipvOpvpOpve;
	}

	public String getRefHsiCode() {
		return this.refHsiCode;
	}

	public void setRefHsiCode(String refHsiCode) {
		this.refHsiCode = refHsiCode;
	}

	public String getAnaUser() {
		return this.anaUser;
	}

	public void setAnaUser(String anaUser) {
		this.anaUser = anaUser;
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

	public Set getLh4s() {
		return this.lh4s;
	}

	public void setLh4s(Set lh4s) {
		this.lh4s = lh4s;
	}

	public Set getLh1as() {
		return this.lh1as;
	}

	public void setLh1as(Set lh1as) {
		this.lh1as = lh1as;
	}

	public Set getLhSteps() {
		return this.lhSteps;
	}

	public void setLhSteps(Set lhSteps) {
		this.lhSteps = lhSteps;
	}

	public Set getLh5s() {
		return this.lh5s;
	}

	public void setLh5s(Set lh5s) {
		this.lh5s = lh5s;
	}

	public Set getLh2s() {
		return this.lh2s;
	}

	public void setLh2s(Set lh2s) {
		this.lh2s = lh2s;
	}

	public Set getLh3s() {
		return this.lh3s;
	}

	public void setLh3s(Set lh3s) {
		this.lh3s = lh3s;
	}

	public Set getLh1s() {
		return this.lh1s;
	}

	public void setLh1s(Set lh1s) {
		this.lh1s = lh1s;
	}

}