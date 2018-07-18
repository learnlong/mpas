package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComProfession entity. @author MyEclipse Persistence Tools
 */

public class ComProfession implements java.io.Serializable {

	// Fields

	private String professionId;
	private String professionCode;
	private String professionName;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set comUserPositions = new HashSet(0);
	private Set comProfessionUsers = new HashSet(0);
	private Set comAuthorities = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComProfession() {
	}

	/** full constructor */
	public ComProfession(String professionCode, String professionName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set comUserPositions,
			Set comProfessionUsers, Set comAuthorities) {
		this.professionCode = professionCode;
		this.professionName = professionName;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comUserPositions = comUserPositions;
		this.comProfessionUsers = comProfessionUsers;
		this.comAuthorities = comAuthorities;
	}

	// Property accessors

	public String getProfessionId() {
		return this.professionId;
	}

	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}

	public String getProfessionCode() {
		return this.professionCode;
	}

	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}

	public String getProfessionName() {
		return this.professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
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

	public Set getComUserPositions() {
		return this.comUserPositions;
	}

	public void setComUserPositions(Set comUserPositions) {
		this.comUserPositions = comUserPositions;
	}

	public Set getComProfessionUsers() {
		return this.comProfessionUsers;
	}

	public void setComProfessionUsers(Set comProfessionUsers) {
		this.comProfessionUsers = comProfessionUsers;
	}

	public Set getComAuthorities() {
		return this.comAuthorities;
	}

	public void setComAuthorities(Set comAuthorities) {
		this.comAuthorities = comAuthorities;
	}

}