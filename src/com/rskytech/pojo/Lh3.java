package com.rskytech.pojo;

import java.util.Date;

/**
 * Lh3 entity. @author MyEclipse Persistence Tools
 */

public class Lh3 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6286235869679482296L;
	private String lh3Id;
	private LhMain lhMain;
	private String defectModel;
	private String defectDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Lh3() {
	}

	/** full constructor */
	public Lh3(LhMain lhMain, String defectModel, String defectDesc,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.lhMain = lhMain;
		this.defectModel = defectModel;
		this.defectDesc = defectDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLh3Id() {
		return this.lh3Id;
	}

	public void setLh3Id(String lh3Id) {
		this.lh3Id = lh3Id;
	}

	public LhMain getLhMain() {
		return this.lhMain;
	}

	public void setLhMain(LhMain lhMain) {
		this.lhMain = lhMain;
	}

	public String getDefectModel() {
		return this.defectModel;
	}

	public void setDefectModel(String defectModel) {
		this.defectModel = defectModel;
	}

	public String getDefectDesc() {
		return this.defectDesc;
	}

	public void setDefectDesc(String defectDesc) {
		this.defectDesc = defectDesc;
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