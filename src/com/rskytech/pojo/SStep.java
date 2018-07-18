package com.rskytech.pojo;

import java.util.Date;

/**
 * SStep entity. @author MyEclipse Persistence Tools
 */

public class SStep implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7632046278822899781L;
	private String SStepId;
	private SMain SMain;
	private Integer s1;
	private Integer s2;
	private Integer s3;
	private Integer s4aIn;
	private Integer s4bIn;
	private Integer s4aOut;
	private Integer s4bOut;
	private Integer s5aIn;
	private Integer s5bIn;
	private Integer s5aOut;
	private Integer s5bOut;
	private Integer syaIn;
	private Integer sybIn;
	private Integer syaOut;
	private Integer sybOut;
	private Integer s6In;
	private Integer s6Out;
	private Integer s7;
	private Integer s8;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public SStep() {
	}

	/** full constructor */
	public SStep(SMain SMain, Integer s1, Integer s2, Integer s3,
			Integer s4aIn, Integer s4bIn, Integer s4aOut,
			Integer s4bOut, Integer s5aIn, Integer s5bIn,
			Integer s5aOut, Integer s5bOut, Integer s6In,
			Integer s6Out, Integer s7, Integer s8, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.SMain = SMain;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.s4aIn = s4aIn;
		this.s4bIn = s4bIn;
		this.s4aOut = s4aOut;
		this.s4bOut = s4bOut;
		this.s5aIn = s5aIn;
		this.s5bIn = s5bIn;
		this.s5aOut = s5aOut;
		this.s5bOut = s5bOut;
		this.s6In = s6In;
		this.s6Out = s6Out;
		this.s7 = s7;
		this.s8 = s8;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getSStepId() {
		return this.SStepId;
	}

	public void setSStepId(String SStepId) {
		this.SStepId = SStepId;
	}

	public SMain getSMain() {
		return this.SMain;
	}

	public void setSMain(SMain SMain) {
		this.SMain = SMain;
	}

	public Integer getS1() {
		return this.s1;
	}

	public void setS1(Integer s1) {
		this.s1 = s1;
	}

	public Integer getS2() {
		return this.s2;
	}

	public void setS2(Integer s2) {
		this.s2 = s2;
	}

	public Integer getS3() {
		return this.s3;
	}

	public void setS3(Integer s3) {
		this.s3 = s3;
	}

	public Integer getS4aIn() {
		return this.s4aIn;
	}

	public void setS4aIn(Integer s4aIn) {
		this.s4aIn = s4aIn;
	}

	public Integer getS4bIn() {
		return this.s4bIn;
	}

	public void setS4bIn(Integer s4bIn) {
		this.s4bIn = s4bIn;
	}

	public Integer getS4aOut() {
		return this.s4aOut;
	}

	public void setS4aOut(Integer s4aOut) {
		this.s4aOut = s4aOut;
	}

	public Integer getS4bOut() {
		return this.s4bOut;
	}

	public void setS4bOut(Integer s4bOut) {
		this.s4bOut = s4bOut;
	}

	public Integer getS5aIn() {
		return this.s5aIn;
	}

	public void setS5aIn(Integer s5aIn) {
		this.s5aIn = s5aIn;
	}

	public Integer getS5bIn() {
		return this.s5bIn;
	}

	public void setS5bIn(Integer s5bIn) {
		this.s5bIn = s5bIn;
	}

	public Integer getS5aOut() {
		return this.s5aOut;
	}

	public void setS5aOut(Integer s5aOut) {
		this.s5aOut = s5aOut;
	}

	public Integer getS5bOut() {
		return this.s5bOut;
	}

	public void setS5bOut(Integer s5bOut) {
		this.s5bOut = s5bOut;
	}

	public Integer getSyaIn() {
		return syaIn;
	}

	public void setSyaIn(Integer syaIn) {
		this.syaIn = syaIn;
	}

	public Integer getSybIn() {
		return sybIn;
	}

	public void setSybIn(Integer sybIn) {
		this.sybIn = sybIn;
	}

	public Integer getSyaOut() {
		return syaOut;
	}

	public void setSyaOut(Integer syaOut) {
		this.syaOut = syaOut;
	}

	public Integer getSybOut() {
		return sybOut;
	}

	public void setSybOut(Integer sybOut) {
		this.sybOut = sybOut;
	}

	public Integer getS6In() {
		return this.s6In;
	}

	public void setS6In(Integer s6In) {
		this.s6In = s6In;
	}

	public Integer getS6Out() {
		return this.s6Out;
	}

	public void setS6Out(Integer s6Out) {
		this.s6Out = s6Out;
	}

	public Integer getS7() {
		return this.s7;
	}

	public void setS7(Integer s7) {
		this.s7 = s7;
	}

	public Integer getS8() {
		return this.s8;
	}

	public void setS8(Integer s8) {
		this.s8 = s8;
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