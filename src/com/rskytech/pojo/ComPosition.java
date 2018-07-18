package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComPosition entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComPosition implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5556232188071915211L;
	private String positionId;
	private String positionCode;
	private String positionName;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set comPositionRoles = new HashSet(0);
	private Set comUserPositions = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComPosition() {
	}

	/** full constructor */
	public ComPosition(String positionCode, String positionName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set comPositionRoles,
			Set comUserPositions) {
		this.positionCode = positionCode;
		this.positionName = positionName;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comPositionRoles = comPositionRoles;
		this.comUserPositions = comUserPositions;
	}

	// Property accessors

	public String getPositionId() {
		return this.positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getPositionCode() {
		return this.positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	public String getPositionName() {
		return this.positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
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

	public Set getComPositionRoles() {
		return this.comPositionRoles;
	}

	public void setComPositionRoles(Set comPositionRoles) {
		this.comPositionRoles = comPositionRoles;
	}

	public Set getComUserPositions() {
		return this.comUserPositions;
	}

	public void setComUserPositions(Set comUserPositions) {
		this.comUserPositions = comUserPositions;
	}

}