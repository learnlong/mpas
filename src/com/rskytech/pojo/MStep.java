package com.rskytech.pojo;

import java.util.Date;

/**
 * MStep entity. @author MyEclipse Persistence Tools
 */

public class MStep implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2948721124935163270L;
	private String MStepId;
	private MMain MMain;
	private Integer m11;
	private Integer m12;
	private Integer m13;
	private Integer m2;
	private Integer m3;
	private Integer m4;
	private Integer m5;
	private Integer mset;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public MStep() {
	}

	/** full constructor */
	public MStep(String mStepId, com.rskytech.pojo.MMain mMain, Integer m11,
			Integer m12, Integer m13, Integer m2, Integer m3, Integer m4,
			Integer m5, Integer mset, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		super();
		MStepId = mStepId;
		MMain = mMain;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m2 = m2;
		this.m3 = m3;
		this.m4 = m4;
		this.m5 = m5;
		this.mset = mset;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getMStepId() {
		return this.MStepId;
	}

	public void setMStepId(String MStepId) {
		this.MStepId = MStepId;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public Integer getM11() {
		return this.m11;
	}

	public void setM11(Integer m11) {
		this.m11 = m11;
	}

	public Integer getM12() {
		return this.m12;
	}

	public void setM12(Integer m12) {
		this.m12 = m12;
	}

	public Integer getM13() {
		return this.m13;
	}

	public void setM13(Integer m13) {
		this.m13 = m13;
	}

	public Integer getM2() {
		return this.m2;
	}

	public void setM2(Integer m2) {
		this.m2 = m2;
	}

	public Integer getM3() {
		return this.m3;
	}

	public void setM3(Integer m3) {
		this.m3 = m3;
	}

	public Integer getM4() {
		return this.m4;
	}

	public void setM4(Integer m4) {
		this.m4 = m4;
	}

	public Integer getM5() {
		return this.m5;
	}

	public void setM5(Integer m5) {
		this.m5 = m5;
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

	public Integer getMset() {
		return mset;
	}

	public void setMset(Integer mset) {
		this.mset = mset;
	}

}