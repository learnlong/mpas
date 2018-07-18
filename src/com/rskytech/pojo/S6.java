package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * S6 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class S6 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2613789497419848041L;
	private String s6Id;
	private SMain SMain;
	private String inOrOut;
	private Integer isCpcp;
	private String cpcp;
	private String finalRemark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String considerWear;
	private Set s6Eas = new HashSet(0);

	// Constructors

	/** default constructor */
	public S6() {
	}

	/** full constructor */
	public S6(SMain SMain, String inOrOut, Integer isCpcp, String cpcp,
			String finalRemark, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set s6Eas,String considerWear) {
		this.SMain = SMain;
		this.inOrOut = inOrOut;
		this.isCpcp = isCpcp;
		this.cpcp = cpcp;
		this.finalRemark = finalRemark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.considerWear = considerWear;
		this.s6Eas = s6Eas;
	}

	// Property accessors

	public String getS6Id() {
		return this.s6Id;
	}

	public void setS6Id(String s6Id) {
		this.s6Id = s6Id;
	}

	public SMain getSMain() {
		return this.SMain;
	}

	public void setSMain(SMain SMain) {
		this.SMain = SMain;
	}

	public String getInOrOut() {
		return this.inOrOut;
	}

	public void setInOrOut(String inOrOut) {
		this.inOrOut = inOrOut;
	}

	public Integer getIsCpcp() {
		return this.isCpcp;
	}

	public void setIsCpcp(Integer isCpcp) {
		this.isCpcp = isCpcp;
	}

	public String getCpcp() {
		return this.cpcp;
	}

	public void setCpcp(String cpcp) {
		this.cpcp = cpcp;
	}

	public String getFinalRemark() {
		return this.finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
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

	public Set getS6Eas() {
		return this.s6Eas;
	}

	public void setS6Eas(Set s6Eas) {
		this.s6Eas = s6Eas;
	}

	public String getConsiderWear() {
		return considerWear;
	}

	public void setConsiderWear(String considerWear) {
		this.considerWear = considerWear;
	}

}