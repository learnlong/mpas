package com.rskytech.pojo;

import java.util.Date;

/**
 * MReferMsi entity. @author MyEclipse Persistence Tools
 */

public class MReferMsi implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -6339405232608675794L;
	private String refId;
	private M13C m13C;
	private String refMsiId;
	private String refFunctionId;
	private String refFailureId;
	private String refEffectId;
	private String refCauseId;
	private String isAna;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public MReferMsi() {
	}

	/** full constructor */
	public MReferMsi(M13C m13C, String refMsiId, String refFunctionId,
			String refFailureId, String refEffectId, String refCauseId,
			String isAna, String remark, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.m13C = m13C;
		this.refMsiId = refMsiId;
		this.refFunctionId = refFunctionId;
		this.refFailureId = refFailureId;
		this.refEffectId = refEffectId;
		this.refCauseId = refCauseId;
		this.isAna = isAna;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getRefId() {
		return this.refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public M13C getM13C() {
		return this.m13C;
	}

	public void setM13C(M13C m13C) {
		this.m13C = m13C;
	}

	public String getRefMsiId() {
		return this.refMsiId;
	}

	public void setRefMsiId(String refMsiId) {
		this.refMsiId = refMsiId;
	}

	public String getRefFunctionId() {
		return this.refFunctionId;
	}

	public void setRefFunctionId(String refFunctionId) {
		this.refFunctionId = refFunctionId;
	}

	public String getRefFailureId() {
		return this.refFailureId;
	}

	public void setRefFailureId(String refFailureId) {
		this.refFailureId = refFailureId;
	}

	public String getRefEffectId() {
		return this.refEffectId;
	}

	public void setRefEffectId(String refEffectId) {
		this.refEffectId = refEffectId;
	}

	public String getRefCauseId() {
		return this.refCauseId;
	}

	public void setRefCauseId(String refCauseId) {
		this.refCauseId = refCauseId;
	}

	public String getIsAna() {
		return this.isAna;
	}

	public void setIsAna(String isAna) {
		this.isAna = isAna;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

}