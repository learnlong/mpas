package com.rskytech.pojo;

/**
 * ComProfessionUserId entity. @author MyEclipse Persistence Tools
 */

public class ComProfessionUserId implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2015307543085691170L;
	private ComProfession comProfession;
	private ComUser comUser;

	// Constructors

	/** default constructor */
	public ComProfessionUserId() {
	}

	/** full constructor */
	public ComProfessionUserId(ComProfession comProfession, ComUser comUser) {
		this.comProfession = comProfession;
		this.comUser = comUser;
	}

	// Property accessors

	public ComProfession getComProfession() {
		return this.comProfession;
	}

	public void setComProfession(ComProfession comProfession) {
		this.comProfession = comProfession;
	}

	public ComUser getComUser() {
		return this.comUser;
	}

	public void setComUser(ComUser comUser) {
		this.comUser = comUser;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComProfessionUserId))
			return false;
		ComProfessionUserId castOther = (ComProfessionUserId) other;

		return ((this.getComProfession() == castOther.getComProfession()) || (this
				.getComProfession() != null
				&& castOther.getComProfession() != null && this
				.getComProfession().equals(castOther.getComProfession())))
				&& ((this.getComUser() == castOther.getComUser()) || (this
						.getComUser() != null
						&& castOther.getComUser() != null && this.getComUser()
						.equals(castOther.getComUser())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getComProfession() == null ? 0 : this.getComProfession()
						.hashCode());
		result = 37 * result
				+ (getComUser() == null ? 0 : this.getComUser().hashCode());
		return result;
	}

}