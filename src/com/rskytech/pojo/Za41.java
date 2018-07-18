package com.rskytech.pojo;

import java.util.Date;

/**
 * Za41 entity. @author MyEclipse Persistence Tools
 */

public class Za41 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3154081124662327193L;
	private String za41Id;
	private ZaMain zaMain;
	private Integer step1;
	private String step1Desc;
	private Integer step2;
	private String step2Desc;
	private Integer step3;
	private String step3Desc;
	private Integer step4;
	private String step4Desc;
	private Integer step5;
	private String step5Desc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Za41() {
	}

	/** full constructor */
	public Za41(ZaMain zaMain, Integer step1, String step1Desc,
			Integer step2, String step2Desc, Integer step3,
			String step3Desc, Integer step4, String step4Desc,
			Integer step5, String step5Desc, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.zaMain = zaMain;
		this.step1 = step1;
		this.step1Desc = step1Desc;
		this.step2 = step2;
		this.step2Desc = step2Desc;
		this.step3 = step3;
		this.step3Desc = step3Desc;
		this.step4 = step4;
		this.step4Desc = step4Desc;
		this.step5 = step5;
		this.step5Desc = step5Desc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getZa41Id() {
		return this.za41Id;
	}

	public void setZa41Id(String za41Id) {
		this.za41Id = za41Id;
	}

	public ZaMain getZaMain() {
		return this.zaMain;
	}

	public void setZaMain(ZaMain zaMain) {
		this.zaMain = zaMain;
	}

	public Integer getStep1() {
		return this.step1;
	}

	public void setStep1(Integer step1) {
		this.step1 = step1;
	}

	public String getStep1Desc() {
		return this.step1Desc;
	}

	public void setStep1Desc(String step1Desc) {
		this.step1Desc = step1Desc;
	}

	public Integer getStep2() {
		return this.step2;
	}

	public void setStep2(Integer step2) {
		this.step2 = step2;
	}

	public String getStep2Desc() {
		return this.step2Desc;
	}

	public void setStep2Desc(String step2Desc) {
		this.step2Desc = step2Desc;
	}

	public Integer getStep3() {
		return this.step3;
	}

	public void setStep3(Integer step3) {
		this.step3 = step3;
	}

	public String getStep3Desc() {
		return this.step3Desc;
	}

	public void setStep3Desc(String step3Desc) {
		this.step3Desc = step3Desc;
	}

	public Integer getStep4() {
		return this.step4;
	}

	public void setStep4(Integer step4) {
		this.step4 = step4;
	}

	public String getStep4Desc() {
		return this.step4Desc;
	}

	public void setStep4Desc(String step4Desc) {
		this.step4Desc = step4Desc;
	}

	public Integer getStep5() {
		return this.step5;
	}

	public void setStep5(Integer step5) {
		this.step5 = step5;
	}

	public String getStep5Desc() {
		return this.step5Desc;
	}

	public void setStep5Desc(String step5Desc) {
		this.step5Desc = step5Desc;
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