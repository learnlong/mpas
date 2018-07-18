package com.rskytech.pojo;

import java.util.Date;

/**
 * ZaStep entity. @author MyEclipse Persistence Tools
 */

public class ZaStep implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 505318397991758378L;
	private String areaStepId;
	private ZaMain zaMain;
	private Integer za1;
	private Integer za2;
	private Integer za41;
	private Integer za42;
	private Integer za43;
	private Integer za5a;
	private Integer za5b;
	private Integer za6;
	private Integer za7;
	private Integer za8;
	private Integer za9;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	
	private String pageName;//放置需要显示的区域页面的编号
	private String areaStatus;//放置当前区域的状态

	// Constructors

	/** default constructor */
	public ZaStep() {
	}

	/** full constructor */
	public ZaStep(ZaMain zaMain, Integer za1, Integer za2,
			Integer za41, Integer za42, Integer za43, Integer za5a,
			Integer za5b, Integer za6, Integer za7, Integer za8,
			Integer za9, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.zaMain = zaMain;
		this.za1 = za1;
		this.za2 = za2;
		this.za41 = za41;
		this.za42 = za42;
		this.za43 = za43;
		this.za5a = za5a;
		this.za5b = za5b;
		this.za6 = za6;
		this.za7 = za7;
		this.za8 = za8;
		this.za9 = za9;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getAreaStepId() {
		return this.areaStepId;
	}

	public void setAreaStepId(String areaStepId) {
		this.areaStepId = areaStepId;
	}

	public ZaMain getZaMain() {
		return this.zaMain;
	}

	public void setZaMain(ZaMain zaMain) {
		this.zaMain = zaMain;
	}

	public Integer getZa1() {
		return this.za1;
	}

	public void setZa1(Integer za1) {
		this.za1 = za1;
	}

	public Integer getZa2() {
		return this.za2;
	}

	public void setZa2(Integer za2) {
		this.za2 = za2;
	}

	public Integer getZa41() {
		return this.za41;
	}

	public void setZa41(Integer za41) {
		this.za41 = za41;
	}

	public Integer getZa42() {
		return this.za42;
	}

	public void setZa42(Integer za42) {
		this.za42 = za42;
	}

	public Integer getZa43() {
		return this.za43;
	}

	public void setZa43(Integer za43) {
		this.za43 = za43;
	}

	public Integer getZa5a() {
		return this.za5a;
	}

	public void setZa5a(Integer za5a) {
		this.za5a = za5a;
	}

	public Integer getZa5b() {
		return this.za5b;
	}

	public void setZa5b(Integer za5b) {
		this.za5b = za5b;
	}

	public Integer getZa6() {
		return this.za6;
	}

	public void setZa6(Integer za6) {
		this.za6 = za6;
	}

	public Integer getZa7() {
		return this.za7;
	}

	public void setZa7(Integer za7) {
		this.za7 = za7;
	}

	public Integer getZa8() {
		return this.za8;
	}

	public void setZa8(Integer za8) {
		this.za8 = za8;
	}

	public Integer getZa9() {
		return this.za9;
	}

	public void setZa9(Integer za9) {
		this.za9 = za9;
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

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getAreaStatus() {
		return areaStatus;
	}

	public void setAreaStatus(String areaStatus) {
		this.areaStatus = areaStatus;
	}

}