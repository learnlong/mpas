package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * M3 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class M3 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5705836590494002691L;
	private String m3Id;
	private M13C m13C;
	private MMain MMain;
	private Integer baoyang;
	private String baoyangDesc;
	private String baoyangTaskId;
	private Integer jianyan;
	private String jianyanDesc;
	private String jianyanTaskId;
	private Integer jiankong;
	private String jiankongDesc;
	private String jiankongTaskId;
	private Integer jiancha;
	private String jianchaDesc;
	private String jianchaTaskId;
	private Integer chaixiu;
	private String chaixiuDesc;
	private String chaixiuTaskId;
	private Integer baofei;
	private String baofeiDesc;
	private String baofeiTaskId;
	private Integer zonghe;
	private String zongheDesc;
	private String zongheTaskId;
	private Integer gaijin;
	private String remark;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set m3Additionals = new HashSet(0);

	// Constructors

	/** default constructor */
	public M3() {
	}

	/** full constructor */
	public M3(M13C m13C, MMain MMain, Integer baoyang, String baoyangDesc,
			String baoyangTaskId, Integer jianyan, String jianyanDesc,
			String jianyanTaskId, Integer jiankong, String jiankongDesc,
			String jiankongTaskId, Integer jiancha, String jianchaDesc,
			String jianchaTaskId, Integer chaixiu, String chaixiuDesc,
			String chaixiuTaskId, Integer baofei, String baofeiDesc,
			String baofeiTaskId, Integer zonghe, String zongheDesc,
			String zongheTaskId, Integer gaijin, String remark,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set m3Additionals) {
		this.m13C = m13C;
		this.MMain = MMain;
		this.baoyang = baoyang;
		this.baoyangDesc = baoyangDesc;
		this.baoyangTaskId = baoyangTaskId;
		this.jianyan = jianyan;
		this.jianyanDesc = jianyanDesc;
		this.jianyanTaskId = jianyanTaskId;
		this.jiankong = jiankong;
		this.jiankongDesc = jiankongDesc;
		this.jiankongTaskId = jiankongTaskId;
		this.jiancha = jiancha;
		this.jianchaDesc = jianchaDesc;
		this.jianchaTaskId = jianchaTaskId;
		this.chaixiu = chaixiu;
		this.chaixiuDesc = chaixiuDesc;
		this.chaixiuTaskId = chaixiuTaskId;
		this.baofei = baofei;
		this.baofeiDesc = baofeiDesc;
		this.baofeiTaskId = baofeiTaskId;
		this.zonghe = zonghe;
		this.zongheDesc = zongheDesc;
		this.zongheTaskId = zongheTaskId;
		this.gaijin = gaijin;
		this.remark = remark;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.m3Additionals = m3Additionals;
	}

	// Property accessors

	public String getM3Id() {
		return this.m3Id;
	}

	public void setM3Id(String m3Id) {
		this.m3Id = m3Id;
	}

	public M13C getM13C() {
		return this.m13C;
	}

	public void setM13C(M13C m13C) {
		this.m13C = m13C;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public Integer getBaoyang() {
		return this.baoyang;
	}

	public void setBaoyang(Integer baoyang) {
		this.baoyang = baoyang;
	}

	public String getBaoyangDesc() {
		return this.baoyangDesc;
	}

	public void setBaoyangDesc(String baoyangDesc) {
		this.baoyangDesc = baoyangDesc;
	}

	public String getBaoyangTaskId() {
		return this.baoyangTaskId;
	}

	public void setBaoyangTaskId(String baoyangTaskId) {
		this.baoyangTaskId = baoyangTaskId;
	}

	public Integer getJianyan() {
		return this.jianyan;
	}

	public void setJianyan(Integer jianyan) {
		this.jianyan = jianyan;
	}

	public String getJianyanDesc() {
		return this.jianyanDesc;
	}

	public void setJianyanDesc(String jianyanDesc) {
		this.jianyanDesc = jianyanDesc;
	}

	public String getJianyanTaskId() {
		return this.jianyanTaskId;
	}

	public void setJianyanTaskId(String jianyanTaskId) {
		this.jianyanTaskId = jianyanTaskId;
	}

	public Integer getJiankong() {
		return this.jiankong;
	}

	public void setJiankong(Integer jiankong) {
		this.jiankong = jiankong;
	}

	public String getJiankongDesc() {
		return this.jiankongDesc;
	}

	public void setJiankongDesc(String jiankongDesc) {
		this.jiankongDesc = jiankongDesc;
	}

	public String getJiankongTaskId() {
		return this.jiankongTaskId;
	}

	public void setJiankongTaskId(String jiankongTaskId) {
		this.jiankongTaskId = jiankongTaskId;
	}

	public Integer getJiancha() {
		return this.jiancha;
	}

	public void setJiancha(Integer jiancha) {
		this.jiancha = jiancha;
	}

	public String getJianchaDesc() {
		return this.jianchaDesc;
	}

	public void setJianchaDesc(String jianchaDesc) {
		this.jianchaDesc = jianchaDesc;
	}

	public String getJianchaTaskId() {
		return this.jianchaTaskId;
	}

	public void setJianchaTaskId(String jianchaTaskId) {
		this.jianchaTaskId = jianchaTaskId;
	}

	public Integer getChaixiu() {
		return this.chaixiu;
	}

	public void setChaixiu(Integer chaixiu) {
		this.chaixiu = chaixiu;
	}

	public String getChaixiuDesc() {
		return this.chaixiuDesc;
	}

	public void setChaixiuDesc(String chaixiuDesc) {
		this.chaixiuDesc = chaixiuDesc;
	}

	public String getChaixiuTaskId() {
		return this.chaixiuTaskId;
	}

	public void setChaixiuTaskId(String chaixiuTaskId) {
		this.chaixiuTaskId = chaixiuTaskId;
	}

	public Integer getBaofei() {
		return this.baofei;
	}

	public void setBaofei(Integer baofei) {
		this.baofei = baofei;
	}

	public String getBaofeiDesc() {
		return this.baofeiDesc;
	}

	public void setBaofeiDesc(String baofeiDesc) {
		this.baofeiDesc = baofeiDesc;
	}

	public String getBaofeiTaskId() {
		return this.baofeiTaskId;
	}

	public void setBaofeiTaskId(String baofeiTaskId) {
		this.baofeiTaskId = baofeiTaskId;
	}

	public Integer getZonghe() {
		return this.zonghe;
	}

	public void setZonghe(Integer zonghe) {
		this.zonghe = zonghe;
	}

	public String getZongheDesc() {
		return this.zongheDesc;
	}

	public void setZongheDesc(String zongheDesc) {
		this.zongheDesc = zongheDesc;
	}

	public String getZongheTaskId() {
		return this.zongheTaskId;
	}

	public void setZongheTaskId(String zongheTaskId) {
		this.zongheTaskId = zongheTaskId;
	}

	public Integer getGaijin() {
		return this.gaijin;
	}

	public void setGaijin(Integer gaijin) {
		this.gaijin = gaijin;
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

	public Set getM3Additionals() {
		return this.m3Additionals;
	}

	public void setM3Additionals(Set m3Additionals) {
		this.m3Additionals = m3Additionals;
	}

}