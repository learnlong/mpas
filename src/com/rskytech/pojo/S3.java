package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * S3 entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class S3 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8179721480142989468L;
	private String s3Id;
	private SMain SMain;
	private S1 s1;
	private String taskType;
	private Integer isAdEffect;
	private Double basicCrack;
	private Double materialSize;
	private Double edgeEffect;
	private Double detectCrack;
	private Double lc;
	private Double lo;
	private Double detailCrack;
	private String taskInterval;
	private String taskIntervalRepeat;
	private Integer isOk;
	private String remark;
	private Integer intOut;
	private String picUrl;
	private String detailSdi;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set s3Cracks = new HashSet(0);

	// Constructors

	/** default constructor */
	public S3() {
	}

	/** full constructor */
	public S3(SMain SMain, S1 s1, String taskType, Integer isAdEffect,
			Double basicCrack, Double materialSize, Double edgeEffect,
			Double detectCrack, Double lc, Double lo, Double detailCrack,
			String taskInterval, String taskIntervalRepeat, Integer isOk,
			String remark, Integer intOut, String picUrl, String detailSdi,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set s3Cracks) {
		this.SMain = SMain;
		this.s1 = s1;
		this.taskType = taskType;
		this.isAdEffect = isAdEffect;
		this.basicCrack = basicCrack;
		this.materialSize = materialSize;
		this.edgeEffect = edgeEffect;
		this.detectCrack = detectCrack;
		this.lc = lc;
		this.lo = lo;
		this.detailCrack = detailCrack;
		this.taskInterval = taskInterval;
		this.taskIntervalRepeat = taskIntervalRepeat;
		this.isOk = isOk;
		this.remark = remark;
		this.intOut = intOut;
		this.picUrl = picUrl;
		this.detailSdi = detailSdi;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.s3Cracks = s3Cracks;
	}

	// Property accessors

	public String getS3Id() {
		return this.s3Id;
	}

	public void setS3Id(String s3Id) {
		this.s3Id = s3Id;
	}

	public SMain getSMain() {
		return this.SMain;
	}

	public void setSMain(SMain SMain) {
		this.SMain = SMain;
	}

	public S1 getS1() {
		return this.s1;
	}

	public void setS1(S1 s1) {
		this.s1 = s1;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getIsAdEffect() {
		return this.isAdEffect;
	}

	public void setIsAdEffect(Integer isAdEffect) {
		this.isAdEffect = isAdEffect;
	}

	public Double getBasicCrack() {
		return this.basicCrack;
	}

	public void setBasicCrack(Double basicCrack) {
		this.basicCrack = basicCrack;
	}

	public Double getMaterialSize() {
		return this.materialSize;
	}

	public void setMaterialSize(Double materialSize) {
		this.materialSize = materialSize;
	}

	public Double getEdgeEffect() {
		return this.edgeEffect;
	}

	public void setEdgeEffect(Double edgeEffect) {
		this.edgeEffect = edgeEffect;
	}

	public Double getDetectCrack() {
		return this.detectCrack;
	}

	public void setDetectCrack(Double detectCrack) {
		this.detectCrack = detectCrack;
	}

	public Double getLc() {
		return this.lc;
	}

	public void setLc(Double lc) {
		this.lc = lc;
	}

	public Double getLo() {
		return this.lo;
	}

	public void setLo(Double lo) {
		this.lo = lo;
	}

	public Double getDetailCrack() {
		return this.detailCrack;
	}

	public void setDetailCrack(Double detailCrack) {
		this.detailCrack = detailCrack;
	}

	public String getTaskInterval() {
		return this.taskInterval;
	}

	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}

	public String getTaskIntervalRepeat() {
		return this.taskIntervalRepeat;
	}

	public void setTaskIntervalRepeat(String taskIntervalRepeat) {
		this.taskIntervalRepeat = taskIntervalRepeat;
	}

	public Integer getIsOk() {
		return this.isOk;
	}

	public void setIsOk(Integer isOk) {
		this.isOk = isOk;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIntOut() {
		return this.intOut;
	}

	public void setIntOut(Integer intOut) {
		this.intOut = intOut;
	}

	public String getPicUrl() {
		return this.picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDetailSdi() {
		return this.detailSdi;
	}

	public void setDetailSdi(String detailSdi) {
		this.detailSdi = detailSdi;
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

	public Set getS3Cracks() {
		return this.s3Cracks;
	}

	public void setS3Cracks(Set s3Cracks) {
		this.s3Cracks = s3Cracks;
	}

}