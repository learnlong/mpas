package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ZaMain entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ZaMain implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8100417793640362342L;
	private String zaId;
	private ComArea comArea;
	private ComModelSeries comModelSeries;
	private String effectiveness;
	private String status;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set za5s = new HashSet(0);
	private Set za43s = new HashSet(0);
	private Set za42s = new HashSet(0);
	private Set za2s = new HashSet(0);
	private Set za1s = new HashSet(0);
	private Set za41s = new HashSet(0);
	private Set zaSteps = new HashSet(0);

	// Constructors

	/** default constructor */
	public ZaMain() {
	}

	/** full constructor */
	public ZaMain(ComArea comArea, ComModelSeries comModelSeries,
			String effectiveness, String status, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set za5s, Set za43s, Set za42s, Set za2s,
			Set za1s, Set za41s, Set zaSteps) {
		this.comArea = comArea;
		this.comModelSeries = comModelSeries;
		this.effectiveness = effectiveness;
		this.status = status;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.za5s = za5s;
		this.za43s = za43s;
		this.za42s = za42s;
		this.za2s = za2s;
		this.za1s = za1s;
		this.za41s = za41s;
		this.zaSteps = zaSteps;
	}

	// Property accessors

	public String getZaId() {
		return this.zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
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

	public Set getZa5s() {
		return this.za5s;
	}

	public void setZa5s(Set za5s) {
		this.za5s = za5s;
	}

	public Set getZa43s() {
		return this.za43s;
	}

	public void setZa43s(Set za43s) {
		this.za43s = za43s;
	}

	public Set getZa42s() {
		return this.za42s;
	}

	public void setZa42s(Set za42s) {
		this.za42s = za42s;
	}

	public Set getZa2s() {
		return this.za2s;
	}

	public void setZa2s(Set za2s) {
		this.za2s = za2s;
	}

	public Set getZa1s() {
		return this.za1s;
	}

	public void setZa1s(Set za1s) {
		this.za1s = za1s;
	}

	public Set getZa41s() {
		return this.za41s;
	}

	public void setZa41s(Set za41s) {
		this.za41s = za41s;
	}

	public Set getZaSteps() {
		return this.zaSteps;
	}

	public void setZaSteps(Set zaSteps) {
		this.zaSteps = zaSteps;
	}

}