package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComRole entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4590856714995633949L;
	private String roleId;
	private String roleType;
	private String roleCode;
	private String roleName;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set comPositionRoles = new HashSet(0);
	private Set comRoleMenus = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComRole() {
	}

	/** full constructor */
	public ComRole(String roleType, String roleCode, String roleName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set comPositionRoles,
			Set comRoleMenus) {
		this.roleType = roleType;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comPositionRoles = comPositionRoles;
		this.comRoleMenus = comRoleMenus;
	}

	// Property accessors

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public Set getComRoleMenus() {
		return this.comRoleMenus;
	}

	public void setComRoleMenus(Set comRoleMenus) {
		this.comRoleMenus = comRoleMenus;
	}

}