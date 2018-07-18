package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComUser entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5205881096105798839L;
	private String userId;
	private String userCode;
	private String userName;
	private String password;
	private String post;
	private String plone;
	private String EMail;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private boolean isAdmin = false;
	private boolean isProfessionAdmin = false;
	private boolean isProfessionEngineer = false;
	private boolean isProfessionAnalysis = false;
	private Date modifyDate;
	private Set comUserAuthorities = new HashSet(0);
	private Set comProfessionUsers = new HashSet(0);
	private Set comUserPositions = new HashSet(0);
	
	// Constructors

	/** default constructor */
	public ComUser() {
	}

	/** full constructor */
	public ComUser(String userCode, String userName, String password,
			String post, String plone, String EMail, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set comUserAuthorities, Set comProfessionUsers,
			Set comUserPositions) {
		this.userCode = userCode;
		this.userName = userName;
		this.password = password;
		this.post = post;
		this.plone = plone;
		this.EMail = EMail;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comUserAuthorities = comUserAuthorities;
		this.comProfessionUsers = comProfessionUsers;
		this.comUserPositions = comUserPositions;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPost() {
		return this.post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPlone() {
		return this.plone;
	}

	public void setPlone(String plone) {
		this.plone = plone;
	}

	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
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

	public Set getComProfessionUsers() {
		return this.comProfessionUsers;
	}

	public void setComProfessionUsers(Set comProfessionUsers) {
		this.comProfessionUsers = comProfessionUsers;
	}

	public Set getComUserPositions() {
		return this.comUserPositions;
	}

	public void setComUserPositions(Set comUserPositions) {
		this.comUserPositions = comUserPositions;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isProfessionAdmin() {
		return isProfessionAdmin;
	}

	public void setProfessionAdmin(boolean isProfessionAdmin) {
		this.isProfessionAdmin = isProfessionAdmin;
	}

	public boolean isProfessionEngineer() {
		return isProfessionEngineer;
	}

	public void setProfessionEngineer(boolean isProfessionEngineer) {
		this.isProfessionEngineer = isProfessionEngineer;
	}

	public boolean isProfessionAnalysis() {
		return isProfessionAnalysis;
	}

	public void setProfessionAnalysis(boolean isProfessionAnalysis) {
		this.isProfessionAnalysis = isProfessionAnalysis;
	}

}