package com.rskytech.pojo;

import java.util.Date;

/**
 * Za42 entity. @author MyEclipse Persistence Tools
 */

public class Za42 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3430109824225182511L;
	private String za42Id;
	private ZaMain zaMain;
	private Integer select1;
	private Integer select2;
	private Integer select3;
	private Integer result;
	private String step6Desc;
	private String step7Desc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Za42() {
	}

	/** full constructor */
	public Za42(ZaMain zaMain, Integer select1, Integer select2,
			Integer select3, Integer result, String step6Desc,
			String step7Desc, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.zaMain = zaMain;
		this.select1 = select1;
		this.select2 = select2;
		this.select3 = select3;
		this.result = result;
		this.step6Desc = step6Desc;
		this.step7Desc = step7Desc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getZa42Id() {
		return this.za42Id;
	}

	public void setZa42Id(String za42Id) {
		this.za42Id = za42Id;
	}

	public ZaMain getZaMain() {
		return this.zaMain;
	}

	public void setZaMain(ZaMain zaMain) {
		this.zaMain = zaMain;
	}

	public Integer getSelect1() {
		return this.select1;
	}

	public void setSelect1(Integer select1) {
		this.select1 = select1;
	}

	public Integer getSelect2() {
		return this.select2;
	}

	public void setSelect2(Integer select2) {
		this.select2 = select2;
	}

	public Integer getSelect3() {
		return this.select3;
	}

	public void setSelect3(Integer select3) {
		this.select3 = select3;
	}

	public Integer getResult() {
		return this.result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getStep6Desc() {
		return this.step6Desc;
	}

	public void setStep6Desc(String step6Desc) {
		this.step6Desc = step6Desc;
	}

	public String getStep7Desc() {
		return this.step7Desc;
	}

	public void setStep7Desc(String step7Desc) {
		this.step7Desc = step7Desc;
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