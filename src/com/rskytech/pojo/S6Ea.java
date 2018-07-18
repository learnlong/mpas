package com.rskytech.pojo;

import java.util.Date;

/**
 * S6Ea entity. @author MyEclipse Persistence Tools
 */

public class S6Ea implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369575339515239769L;
	private String eaId;
	private S6 s6;
	private S1 s1;
	private Double edr;
	private Double adr;
	private String interval;
	private String eaType;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public S6Ea() {
	}

	/** full constructor */
	public S6Ea(S6 s6, S1 s1, Double edr, Double adr, String interval,
			String eaType, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.s6 = s6;
		this.s1 = s1;
		this.edr = edr;
		this.adr = adr;
		this.interval = interval;
		this.eaType = eaType;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getEaId() {
		return this.eaId;
	}

	public void setEaId(String eaId) {
		this.eaId = eaId;
	}

	public S6 getS6() {
		return this.s6;
	}

	public void setS6(S6 s6) {
		this.s6 = s6;
	}

	public S1 getS1() {
		return this.s1;
	}

	public void setS1(S1 s1) {
		this.s1 = s1;
	}

	public Double getEdr() {
		return this.edr;
	}

	public void setEdr(Double edr) {
		this.edr = edr;
	}

	public Double getAdr() {
		return this.adr;
	}

	public void setAdr(Double adr) {
		this.adr = adr;
	}

	public String getInterval() {
		return this.interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getEaType() {
		return this.eaType;
	}

	public void setEaType(String eaType) {
		this.eaType = eaType;
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