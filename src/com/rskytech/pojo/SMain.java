package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SMain entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class SMain implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8197505994076589458L;
	private String ssiId;
	private ComModelSeries comModelSeries;
	private ComAta comAta;
	private String parentAtaId;
	private Integer isSsi;
	private Integer isAna;
	private Integer isAdd;
	private String addUser;
	private String remark;
	private String effectiveness;
	private String status;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String addCode;
	private String addName;
	private Set s3s = new HashSet(0);
	private Set SRemarks = new HashSet(0);
	private Set s2s = new HashSet(0);
	private Set s5s = new HashSet(0);
	private Set SSteps = new HashSet(0);
	private Set s6s = new HashSet(0);
	private Set s1s = new HashSet(0);
	private Set s4s = new HashSet(0);
	private Set sys = new HashSet(0);
	// Constructors

	/** default constructor */
	public SMain() {
	}

	/** full constructor */
	public SMain(ComModelSeries comModelSeries, ComAta comAta,
			String parentAtaId, Integer isSsi, Integer isAna, Integer isAdd, String addUser,
			String remark, String effectiveness, String status,String addCode,String addName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set s3s, Set SRemarks, Set s2s,
			Set s5s, Set SSteps, Set s6s, Set s1s, Set s4s) {
		this.comModelSeries = comModelSeries;
		this.comAta = comAta;
		this.parentAtaId = parentAtaId;
		this.isSsi = isSsi;
		this.isAna = isAna;
		this.isAdd = isAdd;
		this.addUser = addUser;
		this.remark = remark;
		this.addName=addName;
		this.addCode=addCode;
		this.effectiveness = effectiveness;
		this.status = status;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.s3s = s3s;
		this.SRemarks = SRemarks;
		this.s2s = s2s;
		this.s5s = s5s;
		this.SSteps = SSteps;
		this.s6s = s6s;
		this.s1s = s1s;
		this.s4s = s4s;
	}

	// Property accessors

	public Set getSys() {
		return sys;
	}

	public void setSys(Set sys) {
		this.sys = sys;
	}
	
	public String getSsiId() {
		return this.ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public ComAta getComAta() {
		return this.comAta;
	}

	public void setComAta(ComAta comAta) {
		this.comAta = comAta;
	}

	public String getParentAtaId() {
		return this.parentAtaId;
	}

	public void setParentAtaId(String parentAtaId) {
		this.parentAtaId = parentAtaId;
	}

	public Integer getIsSsi() {
		return this.isSsi;
	}

	public void setIsSsi(Integer isSsi) {
		this.isSsi = isSsi;
	}

	public Integer getIsAna() {
		return this.isAna;
	}

	public void setIsAna(Integer isAna) {
		this.isAna = isAna;
	}

	public Integer getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(Integer isAdd) {
		this.isAdd = isAdd;
	}

	public String getAddUser() {
		return addUser;
	}

	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEffectiveness() {
		return this.effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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

	public Set getS3s() {
		return this.s3s;
	}

	public void setS3s(Set s3s) {
		this.s3s = s3s;
	}

	public Set getSRemarks() {
		return this.SRemarks;
	}

	public void setSRemarks(Set SRemarks) {
		this.SRemarks = SRemarks;
	}

	public Set getS2s() {
		return this.s2s;
	}

	public void setS2s(Set s2s) {
		this.s2s = s2s;
	}

	public Set getS5s() {
		return this.s5s;
	}

	public void setS5s(Set s5s) {
		this.s5s = s5s;
	}

	public Set getSSteps() {
		return this.SSteps;
	}

	public void setSSteps(Set SSteps) {
		this.SSteps = SSteps;
	}

	public Set getS6s() {
		return this.s6s;
	}

	public void setS6s(Set s6s) {
		this.s6s = s6s;
	}

	public Set getS1s() {
		return this.s1s;
	}

	public void setS1s(Set s1s) {
		this.s1s = s1s;
	}

	public Set getS4s() {
		return this.s4s;
	}

	public void setS4s(Set s4s) {
		this.s4s = s4s;
	}

	public String getAddCode() {
		return addCode;
	}

	public void setAddCode(String addCode) {
		this.addCode = addCode;
	}

	public String getAddName() {
		return addName;
	}

	public void setAddName(String addName) {
		this.addName = addName;
	}

	

}