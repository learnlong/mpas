package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * M2 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class M2 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4401206565122560054L;
	private String m2Id;
	private MMain MMain;
	private M13F m13F;
	private Integer q1;
	private String q1Desc;
	private Integer q2;
	private String q2Desc;
	private Integer q3;
	private String q3Desc;
	private Integer q4;
	private String q4Desc;
	private Integer q5;
	private String q5Desc;
	private Integer q7;
	private String q7Desc;
	private Integer failureCauseType;
	private String mmel;
	private String remark;
	private Integer isRefAfm;
	private Integer isRefMmel;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set MReferMmels = new HashSet(0);
	private Set MReferAfms = new HashSet(0);

	// Constructors

	/** default constructor */
	public M2() {
	}

	/** full constructor */
	public M2(MMain MMain, M13F m13F, Integer q1, String q1Desc,
			Integer q2, String q2Desc, Integer q3, String q3Desc,
			Integer q4, String q4Desc, Integer q5, String q5Desc,Integer q7, String q7Desc,
			Integer failureCauseType, String mmel, String remark,
			Integer isRefAfm, Integer isRefMmel, String createUser,
			Date createDate, String modifyUser, Date modifyDate,
			Set MReferMmels, Set MReferAfms) {
		this.MMain = MMain;
		this.m13F = m13F;
		this.q1 = q1;
		this.q1Desc = q1Desc;
		this.q2 = q2;
		this.q2Desc = q2Desc;
		this.q3 = q3;
		this.q3Desc = q3Desc;
		this.q4 = q4;
		this.q4Desc = q4Desc;
		this.q5 = q5;
		this.q5Desc = q5Desc;
		this.q7 = q7;
		this.q7Desc = q7Desc;
		this.failureCauseType = failureCauseType;
		this.mmel = mmel;
		this.remark = remark;
		this.isRefAfm = isRefAfm;
		this.isRefMmel = isRefMmel;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.MReferMmels = MReferMmels;
		this.MReferAfms = MReferAfms;
	}

	// Property accessors

	public String getM2Id() {
		return this.m2Id;
	}

	public void setM2Id(String m2Id) {
		this.m2Id = m2Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public M13F getM13F() {
		return this.m13F;
	}

	public void setM13F(M13F m13F) {
		this.m13F = m13F;
	}

	public Integer getQ1() {
		return this.q1;
	}

	public void setQ1(Integer q1) {
		this.q1 = q1;
	}

	public String getQ1Desc() {
		return this.q1Desc;
	}

	public void setQ1Desc(String q1Desc) {
		this.q1Desc = q1Desc;
	}

	public Integer getQ2() {
		return this.q2;
	}

	public void setQ2(Integer q2) {
		this.q2 = q2;
	}

	public String getQ2Desc() {
		return this.q2Desc;
	}

	public void setQ2Desc(String q2Desc) {
		this.q2Desc = q2Desc;
	}

	public Integer getQ3() {
		return this.q3;
	}

	public void setQ3(Integer q3) {
		this.q3 = q3;
	}

	public String getQ3Desc() {
		return this.q3Desc;
	}

	public void setQ3Desc(String q3Desc) {
		this.q3Desc = q3Desc;
	}

	public Integer getQ4() {
		return this.q4;
	}

	public void setQ4(Integer q4) {
		this.q4 = q4;
	}

	public String getQ4Desc() {
		return this.q4Desc;
	}

	public void setQ4Desc(String q4Desc) {
		this.q4Desc = q4Desc;
	}

	public Integer getQ5() {
		return this.q5;
	}

	public void setQ5(Integer q5) {
		this.q5 = q5;
	}

	public String getQ5Desc() {
		return this.q5Desc;
	}

	public void setQ5Desc(String q5Desc) {
		this.q5Desc = q5Desc;
	}

	public Integer getQ7() {
		return q7;
	}

	public void setQ7(Integer q7) {
		this.q7 = q7;
	}

	public String getQ7Desc() {
		return q7Desc;
	}

	public void setQ7Desc(String q7Desc) {
		this.q7Desc = q7Desc;
	}

	public Integer getFailureCauseType() {
		return this.failureCauseType;
	}

	public void setFailureCauseType(Integer failureCauseType) {
		this.failureCauseType = failureCauseType;
	}

	public String getMmel() {
		return this.mmel;
	}

	public void setMmel(String mmel) {
		this.mmel = mmel;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsRefAfm() {
		return this.isRefAfm;
	}

	public void setIsRefAfm(Integer isRefAfm) {
		this.isRefAfm = isRefAfm;
	}

	public Integer getIsRefMmel() {
		return this.isRefMmel;
	}

	public void setIsRefMmel(Integer isRefMmel) {
		this.isRefMmel = isRefMmel;
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

	public Set getMReferMmels() {
		return this.MReferMmels;
	}

	public void setMReferMmels(Set MReferMmels) {
		this.MReferMmels = MReferMmels;
	}

	public Set getMReferAfms() {
		return this.MReferAfms;
	}

	public void setMReferAfms(Set MReferAfms) {
		this.MReferAfms = MReferAfms;
	}

}