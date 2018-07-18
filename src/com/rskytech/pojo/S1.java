package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * S1 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class S1 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7989560838575713344L;
	private String s1Id;
	private SMain SMain;
	private String ssiForm;
	private String material;
	private String surface;
	private String ownArea;
	private Integer isMetal;
	private Integer internal;
	private Integer outernal;
	private Integer designPri;
	private Integer isFD;//FD分析
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private String repairPassageway;
	private Set s5s = new HashSet(0);
	private Set s4s = new HashSet(0);
	private Set s6Eas = new HashSet(0);
	private Set s3s = new HashSet(0);
	private Set sys = new HashSet(0);

	// Constructors

	/** default constructor */
	public S1() {
	}

	/** full constructor */
	public S1(SMain SMain, String ssiForm, String material, String surface,
			String ownArea, Integer isMetal, Integer internal,
			Integer outernal, Integer designPri,Integer isFD, String createUser,
			Date createDate, String modifyUser, Date modifyDate, String repairPassageway, Set s5s,
			Set s4s, Set s6Eas, Set s3s) {
		this.SMain = SMain;
		this.ssiForm = ssiForm;
		this.material = material;
		this.surface = surface;
		this.ownArea = ownArea;
		this.isMetal = isMetal;
		this.internal = internal;
		this.outernal = outernal;
		this.designPri = designPri;
		this.isFD = isFD;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.repairPassageway = repairPassageway;
		this.s5s = s5s;
		this.s4s = s4s;
		this.s6Eas = s6Eas;
		this.s3s = s3s;
	}


	
	
	public Set getSys() {
		return sys;
	}

	public void setSys(Set sys) {
		this.sys = sys;
	}

	public String getS1Id() {
		return this.s1Id;
	}

	public void setS1Id(String s1Id) {
		this.s1Id = s1Id;
	}

	public SMain getSMain() {
		return this.SMain;
	}

	public void setSMain(SMain SMain) {
		this.SMain = SMain;
	}

	public String getSsiForm() {
		return this.ssiForm;
	}

	public void setSsiForm(String ssiForm) {
		this.ssiForm = ssiForm;
	}

	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getSurface() {
		return this.surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public String getOwnArea() {
		return this.ownArea;
	}

	public void setOwnArea(String ownArea) {
		this.ownArea = ownArea;
	}

	public Integer getIsMetal() {
		return this.isMetal;
	}

	public void setIsMetal(Integer isMetal) {
		this.isMetal = isMetal;
	}

	public Integer getInternal() {
		return this.internal;
	}

	public void setInternal(Integer internal) {
		this.internal = internal;
	}

	public Integer getOuternal() {
		return this.outernal;
	}

	public void setOuternal(Integer outernal) {
		this.outernal = outernal;
	}

	public Integer getDesignPri() {
		return this.designPri;
	}

	public void setDesignPri(Integer designPri) {
		this.designPri = designPri;
	}

	public Integer getIsFD() {
		return isFD;
	}

	public void setIsFD(Integer isFD) {
		this.isFD = isFD;
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

	public String getRepairPassageway(){
		return this.repairPassageway;
	}
	
	public void setRepairPassageway(String repairPassageway){
		this.repairPassageway = repairPassageway;
	}
	
	public Set getS5s() {
		return this.s5s;
	}

	public void setS5s(Set s5s) {
		this.s5s = s5s;
	}

	public Set getS4s() {
		return this.s4s;
	}

	public void setS4s(Set s4s) {
		this.s4s = s4s;
	}

	public Set getS6Eas() {
		return this.s6Eas;
	}

	public void setS6Eas(Set s6Eas) {
		this.s6Eas = s6Eas;
	}

	public Set getS3s() {
		return this.s3s;
	}

	public void setS3s(Set s3s) {
		this.s3s = s3s;
	}

}