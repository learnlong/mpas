package com.rskytech.pojo;

/**
 * ComPositionRoleId entity. @author MyEclipse Persistence Tools
 */

public class ComPositionRoleId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4770059201928476332L;
	private ComPosition comPosition;
	private ComRole comRole;

	// Constructors

	/** default constructor */
	public ComPositionRoleId() {
	}

	/** full constructor */
	public ComPositionRoleId(ComPosition comPosition, ComRole comRole) {
		this.comPosition = comPosition;
		this.comRole = comRole;
	}

	// Property accessors

	public ComPosition getComPosition() {
		return this.comPosition;
	}

	public void setComPosition(ComPosition comPosition) {
		this.comPosition = comPosition;
	}

	public ComRole getComRole() {
		return this.comRole;
	}

	public void setComRole(ComRole comRole) {
		this.comRole = comRole;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComPositionRoleId))
			return false;
		ComPositionRoleId castOther = (ComPositionRoleId) other;

		return ((this.getComPosition() == castOther.getComPosition()) || (this
				.getComPosition() != null
				&& castOther.getComPosition() != null && this.getComPosition()
				.equals(castOther.getComPosition())))
				&& ((this.getComRole() == castOther.getComRole()) || (this
						.getComRole() != null
						&& castOther.getComRole() != null && this.getComRole()
						.equals(castOther.getComRole())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getComPosition() == null ? 0 : this.getComPosition()
						.hashCode());
		result = 37 * result
				+ (getComRole() == null ? 0 : this.getComRole().hashCode());
		return result;
	}

}