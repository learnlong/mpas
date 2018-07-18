package com.rskytech.pojo;

/**
 * ComRoleMenuId entity. @author MyEclipse Persistence Tools
 */

public class ComRoleMenuId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2544954792347132389L;
	private ComRole comRole;
	private ComMenu comMenu;

	// Constructors

	/** default constructor */
	public ComRoleMenuId() {
	}

	/** full constructor */
	public ComRoleMenuId(ComRole comRole, ComMenu comMenu) {
		this.comRole = comRole;
		this.comMenu = comMenu;
	}

	// Property accessors

	public ComRole getComRole() {
		return this.comRole;
	}

	public void setComRole(ComRole comRole) {
		this.comRole = comRole;
	}

	public ComMenu getComMenu() {
		return this.comMenu;
	}

	public void setComMenu(ComMenu comMenu) {
		this.comMenu = comMenu;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComRoleMenuId))
			return false;
		ComRoleMenuId castOther = (ComRoleMenuId) other;

		return ((this.getComRole() == castOther.getComRole()) || (this
				.getComRole() != null
				&& castOther.getComRole() != null && this.getComRole().equals(
				castOther.getComRole())))
				&& ((this.getComMenu() == castOther.getComMenu()) || (this
						.getComMenu() != null
						&& castOther.getComMenu() != null && this.getComMenu()
						.equals(castOther.getComMenu())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getComRole() == null ? 0 : this.getComRole().hashCode());
		result = 37 * result
				+ (getComMenu() == null ? 0 : this.getComMenu().hashCode());
		return result;
	}

}