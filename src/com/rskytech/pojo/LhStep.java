package com.rskytech.pojo;

import java.util.Date;

/**
 * LhStep entity. @author MyEclipse Persistence Tools
 */

public class LhStep implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1581709151078852152L;
	private String lhStepId;
	private LhMain lhMain;
	private Integer lh1;
	private Integer lh1a;
	private Integer lh2;
	private Integer lh3;
	private Integer lh4;
	private Integer lh5;
	private Integer lh6;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public LhStep() {
	}

	/** full constructor */
	public LhStep(LhMain lhMain, Integer lh1, Integer lh1a,
			Integer lh2, Integer lh3, Integer lh4, Integer lh5,
			Integer lh6, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.lhMain = lhMain;
		this.lh1 = lh1;
		this.lh1a = lh1a;
		this.lh2 = lh2;
		this.lh3 = lh3;
		this.lh4 = lh4;
		this.lh5 = lh5;
		this.lh6 = lh6;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLhStepId() {
		return this.lhStepId;
	}

	public void setLhStepId(String lhStepId) {
		this.lhStepId = lhStepId;
	}

	public LhMain getLhMain() {
		return this.lhMain;
	}

	public void setLhMain(LhMain lhMain) {
		this.lhMain = lhMain;
	}

	public Integer getLh1() {
		return this.lh1;
	}

	public void setLh1(Integer lh1) {
		this.lh1 = lh1;
	}

	public Integer getLh1a() {
		return this.lh1a;
	}

	public void setLh1a(Integer lh1a) {
		this.lh1a = lh1a;
	}

	public Integer getLh2() {
		return this.lh2;
	}

	public void setLh2(Integer lh2) {
		this.lh2 = lh2;
	}

	public Integer getLh3() {
		return this.lh3;
	}

	public void setLh3(Integer lh3) {
		this.lh3 = lh3;
	}

	public Integer getLh4() {
		return this.lh4;
	}

	public void setLh4(Integer lh4) {
		this.lh4 = lh4;
	}

	public Integer getLh5() {
		return this.lh5;
	}

	public void setLh5(Integer lh5) {
		this.lh5 = lh5;
	}

	public Integer getLh6() {
		return this.lh6;
	}

	public void setLh6(Integer lh6) {
		this.lh6 = lh6;
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