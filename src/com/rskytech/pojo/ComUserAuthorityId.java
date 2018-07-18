package com.rskytech.pojo;

/**
 * ComUserAuthorityId entity. @author MyEclipse Persistence Tools
 */

public class ComUserAuthorityId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7768449227839182551L;
	private ComUser comUser;
	private ComAuthority comAuthority;

	// Constructors

	/** default constructor */
	public ComUserAuthorityId() {
	}

	/** full constructor */
	public ComUserAuthorityId(ComUser comUser, ComAuthority comAuthority) {
		this.comUser = comUser;
		this.comAuthority = comAuthority;
	}

	// Property accessors

	public ComUser getComUser() {
		return this.comUser;
	}

	public void setComUser(ComUser comUser) {
		this.comUser = comUser;
	}

	public ComAuthority getComAuthority() {
		return this.comAuthority;
	}

	public void setComAuthority(ComAuthority comAuthority) {
		this.comAuthority = comAuthority;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComUserAuthorityId))
			return false;
		ComUserAuthorityId castOther = (ComUserAuthorityId) other;

		return ((this.getComUser() == castOther.getComUser()) || (this
				.getComUser() != null
				&& castOther.getComUser() != null && this.getComUser().equals(
				castOther.getComUser())))
				&& ((this.getComAuthority() == castOther.getComAuthority()) || (this
						.getComAuthority() != null
						&& castOther.getComAuthority() != null && this
						.getComAuthority().equals(castOther.getComAuthority())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getComUser() == null ? 0 : this.getComUser().hashCode());
		result = 37
				* result
				+ (getComAuthority() == null ? 0 : this.getComAuthority()
						.hashCode());
		return result;
	}

}