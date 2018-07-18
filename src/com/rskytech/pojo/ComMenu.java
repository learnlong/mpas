package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComMenu entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComMenu implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4502212080357686966L;
	private String menuId;
	private String parentId;
	private String menuCode;
	private String menuName;
	private String actionUrl;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set comRoleMenus = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComMenu() {
	}

	/** full constructor */
	public ComMenu(String parentId, String menuCode, String menuName,
			String actionUrl, Integer validFlag, String createUser,
			Date createDate, String modifyUser, Date modifyDate,
			Set comRoleMenus) {
		this.parentId = parentId;
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.actionUrl = actionUrl;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.comRoleMenus = comRoleMenus;
	}

	// Property accessors

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuCode() {
		return this.menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getActionUrl() {
		return this.actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
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

	public Set getComRoleMenus() {
		return this.comRoleMenus;
	}

	public void setComRoleMenus(Set comRoleMenus) {
		this.comRoleMenus = comRoleMenus;
	}

}