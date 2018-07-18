package com.rskytech.pojo;

/**
 * ComUserPositionId entity. @author MyEclipse Persistence Tools
 */

public class ComUserPositionId implements java.io.Serializable {

	// Fields

	private ComUser comUser;
	private ComPosition comPosition;
	private ComProfession comProfession;

	// Constructors

	/** default constructor */
	public ComUserPositionId() {
	}

	/** full constructor */
	public ComUserPositionId(ComUser comUser, ComPosition comPosition,
			ComProfession comProfession) {
		this.comUser = comUser;
		this.comPosition = comPosition;
		this.comProfession = comProfession;
	}

	// Property accessors

	public ComUser getComUser() {
		return this.comUser;
	}

	public void setComUser(ComUser comUser) {
		this.comUser = comUser;
	}

	public ComPosition getComPosition() {
		return this.comPosition;
	}

	public void setComPosition(ComPosition comPosition) {
		this.comPosition = comPosition;
	}

	public ComProfession getComProfession() {
		return this.comProfession;
	}

	public void setComProfession(ComProfession comProfession) {
		this.comProfession = comProfession;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ComUserPositionId))
			return false;
		ComUserPositionId castOther = (ComUserPositionId) other;

		return ((this.getComUser() == castOther.getComUser()) || (this
				.getComUser() != null
				&& castOther.getComUser() != null && this.getComUser().equals(
				castOther.getComUser())))
				&& ((this.getComPosition() == castOther.getComPosition()) || (this
						.getComPosition() != null
						&& castOther.getComPosition() != null && this
						.getComPosition().equals(castOther.getComPosition())))
				&& ((this.getComProfession() == castOther.getComProfession()) || (this
						.getComProfession() != null
						&& castOther.getComProfession() != null && this
						.getComProfession()
						.equals(castOther.getComProfession())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getComUser() == null ? 0 : this.getComUser().hashCode());
		result = 37
				* result
				+ (getComPosition() == null ? 0 : this.getComPosition()
						.hashCode());
		result = 37
				* result
				+ (getComProfession() == null ? 0 : this.getComProfession()
						.hashCode());
		return result;
	}

}