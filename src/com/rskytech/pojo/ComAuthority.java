package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComAuthority entity. @author MyEclipse Persistence Tools
 */

public class ComAuthority implements java.io.Serializable {

	// Fields

	private String authorityId;
	private ComModelSeries comModelSeries;
	private ComProfession comProfession;
	private String authorityType;
	private String content;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set comUserAuthorities = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComAuthority() {
	}

	/** full constructor */
	public ComAuthority(ComModelSeries comModelSeries,
			ComProfession comProfession, String authorityType, String content,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set comUserAuthorities) {
		this.comModelSeries = comModelSeries;
		this.comProfession = comProfession;
		this.authorityType = authorityType;
		this.content = content;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comUserAuthorities = comUserAuthorities;
	}

	// Property accessors

	public String getAuthorityId() {
		return this.authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public ComProfession getComProfession() {
		return this.comProfession;
	}

	public void setComProfession(ComProfession comProfession) {
		this.comProfession = comProfession;
	}

	public String getAuthorityType() {
		return this.authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Set getComUserAuthorities() {
		return this.comUserAuthorities;
	}

	public void setComUserAuthorities(Set comUserAuthorities) {
		this.comUserAuthorities = comUserAuthorities;
	}

}