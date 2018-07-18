package com.rskytech.pojo;

import java.util.Date;

/**
 * Lh5 entity. @author MyEclipse Persistence Tools
 */

public class Lh5 implements java.io.Serializable, Cloneable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4282204827943291833L;
	private String lh5Id;
	private LhMain lhMain;
	private Integer gviAvl;
	private String gviDesc;
	private Integer detAvl;
	private String detDesc;
	private Integer fncAvl;
	private String fncDesc;
	private Integer disAvl;
	private String disDesc;
	private Integer needRedesign;
	private String redesignDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors
	///copy 复制一条记录
		@Override
		public  Lh5 clone() throws CloneNotSupportedException {
			Object obj =  super.clone();
			return (Lh5)obj;
		}

	/** default constructor */
	public Lh5() {
	}

	/** full constructor */
	public Lh5(LhMain lhMain, Integer gviAvl, String gviDesc,
			Integer detAvl, String detDesc, Integer fncAvl,
			String fncDesc, Integer disAvl, String disDesc,
			Integer needRedesign, String redesignDesc, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.lhMain = lhMain;
		this.gviAvl = gviAvl;
		this.gviDesc = gviDesc;
		this.detAvl = detAvl;
		this.detDesc = detDesc;
		this.fncAvl = fncAvl;
		this.fncDesc = fncDesc;
		this.disAvl = disAvl;
		this.disDesc = disDesc;
		this.needRedesign = needRedesign;
		this.redesignDesc = redesignDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getLh5Id() {
		return this.lh5Id;
	}

	public void setLh5Id(String lh5Id) {
		this.lh5Id = lh5Id;
	}

	public LhMain getLhMain() {
		return this.lhMain;
	}

	public void setLhMain(LhMain lhMain) {
		this.lhMain = lhMain;
	}

	public Integer getGviAvl() {
		return this.gviAvl;
	}

	public void setGviAvl(Integer gviAvl) {
		this.gviAvl = gviAvl;
	}

	public String getGviDesc() {
		return this.gviDesc;
	}

	public void setGviDesc(String gviDesc) {
		this.gviDesc = gviDesc;
	}

	public Integer getDetAvl() {
		return this.detAvl;
	}

	public void setDetAvl(Integer detAvl) {
		this.detAvl = detAvl;
	}

	public String getDetDesc() {
		return this.detDesc;
	}

	public void setDetDesc(String detDesc) {
		this.detDesc = detDesc;
	}

	public Integer getFncAvl() {
		return this.fncAvl;
	}

	public void setFncAvl(Integer fncAvl) {
		this.fncAvl = fncAvl;
	}

	public String getFncDesc() {
		return this.fncDesc;
	}

	public void setFncDesc(String fncDesc) {
		this.fncDesc = fncDesc;
	}

	public Integer getDisAvl() {
		return this.disAvl;
	}

	public void setDisAvl(Integer disAvl) {
		this.disAvl = disAvl;
	}

	public String getDisDesc() {
		return this.disDesc;
	}

	public void setDisDesc(String disDesc) {
		this.disDesc = disDesc;
	}

	public Integer getNeedRedesign() {
		return this.needRedesign;
	}

	public void setNeedRedesign(Integer needRedesign) {
		this.needRedesign = needRedesign;
	}

	public String getRedesignDesc() {
		return this.redesignDesc;
	}

	public void setRedesignDesc(String redesignDesc) {
		this.redesignDesc = redesignDesc;
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