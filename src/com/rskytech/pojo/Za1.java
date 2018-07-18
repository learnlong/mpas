package com.rskytech.pojo;

import java.util.Date;

/**
 * Za1 entity. @author MyEclipse Persistence Tools
 */

public class Za1 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3098410508237479587L;
	private String za1Id;
	private ZaMain zaMain;
	private String border;
	private String env;
	private String reachWay;
	private String equStruc;
	private Integer hasStrucOnly;
	private Integer needAreaAnalyze;
	private Integer hasPipe;
	private Integer hasMaterial;
	private Integer closeToSystem;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public Za1() {
	}

	/** full constructor */
	public Za1(ZaMain zaMain, String border, String env, String reachWay,
			String equStruc, Integer hasStrucOnly,
			Integer needAreaAnalyze, Integer hasPipe,
			Integer hasMaterial, Integer closeToSystem, String remark,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.zaMain = zaMain;
		this.border = border;
		this.env = env;
		this.reachWay = reachWay;
		this.equStruc = equStruc;
		this.hasStrucOnly = hasStrucOnly;
		this.needAreaAnalyze = needAreaAnalyze;
		this.hasPipe = hasPipe;
		this.hasMaterial = hasMaterial;
		this.closeToSystem = closeToSystem;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getZa1Id() {
		return this.za1Id;
	}

	public void setZa1Id(String za1Id) {
		this.za1Id = za1Id;
	}

	public ZaMain getZaMain() {
		return this.zaMain;
	}

	public void setZaMain(ZaMain zaMain) {
		this.zaMain = zaMain;
	}

	public String getBorder() {
		return this.border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getEnv() {
		return this.env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getReachWay() {
		return this.reachWay;
	}

	public void setReachWay(String reachWay) {
		this.reachWay = reachWay;
	}

	public String getEquStruc() {
		return this.equStruc;
	}

	public void setEquStruc(String equStruc) {
		this.equStruc = equStruc;
	}

	public Integer getHasStrucOnly() {
		return this.hasStrucOnly;
	}

	public void setHasStrucOnly(Integer hasStrucOnly) {
		this.hasStrucOnly = hasStrucOnly;
	}

	public Integer getNeedAreaAnalyze() {
		return this.needAreaAnalyze;
	}

	public void setNeedAreaAnalyze(Integer needAreaAnalyze) {
		this.needAreaAnalyze = needAreaAnalyze;
	}

	public Integer getHasPipe() {
		return this.hasPipe;
	}

	public void setHasPipe(Integer hasPipe) {
		this.hasPipe = hasPipe;
	}

	public Integer getHasMaterial() {
		return this.hasMaterial;
	}

	public void setHasMaterial(Integer hasMaterial) {
		this.hasMaterial = hasMaterial;
	}

	public Integer getCloseToSystem() {
		return this.closeToSystem;
	}

	public void setCloseToSystem(Integer closeToSystem) {
		this.closeToSystem = closeToSystem;
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