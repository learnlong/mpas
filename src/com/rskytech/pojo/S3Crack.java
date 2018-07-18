package com.rskytech.pojo;

import java.util.Date;

/**
 * S3Crack entity. @author MyEclipse Persistence Tools
 */

public class S3Crack implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3867108202184919686L;
	private String crackId;
	private S3 s3;
	private Integer densityLevel;
	private Integer lookLevel;
	private Integer reachLevel;
	private Integer sizeLevel;
	private Integer lightLevel;
	private Integer surfaceLevel;
	private Integer conditionLevel;
	private Integer doLevel;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public S3Crack() {
	}

	/** full constructor */
	public S3Crack(S3 s3, Integer densityLevel, Integer lookLevel,
			Integer reachLevel, Integer sizeLevel, Integer lightLevel,
			Integer surfaceLevel, Integer conditionLevel,
			Integer doLevel, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.s3 = s3;
		this.densityLevel = densityLevel;
		this.lookLevel = lookLevel;
		this.reachLevel = reachLevel;
		this.sizeLevel = sizeLevel;
		this.lightLevel = lightLevel;
		this.surfaceLevel = surfaceLevel;
		this.conditionLevel = conditionLevel;
		this.doLevel = doLevel;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getCrackId() {
		return this.crackId;
	}

	public void setCrackId(String crackId) {
		this.crackId = crackId;
	}

	public S3 getS3() {
		return this.s3;
	}

	public void setS3(S3 s3) {
		this.s3 = s3;
	}

	public Integer getDensityLevel() {
		return this.densityLevel;
	}

	public void setDensityLevel(Integer densityLevel) {
		this.densityLevel = densityLevel;
	}

	public Integer getLookLevel() {
		return this.lookLevel;
	}

	public void setLookLevel(Integer lookLevel) {
		this.lookLevel = lookLevel;
	}

	public Integer getReachLevel() {
		return this.reachLevel;
	}

	public void setReachLevel(Integer reachLevel) {
		this.reachLevel = reachLevel;
	}

	public Integer getSizeLevel() {
		return this.sizeLevel;
	}

	public void setSizeLevel(Integer sizeLevel) {
		this.sizeLevel = sizeLevel;
	}

	public Integer getLightLevel() {
		return this.lightLevel;
	}

	public void setLightLevel(Integer lightLevel) {
		this.lightLevel = lightLevel;
	}

	public Integer getSurfaceLevel() {
		return this.surfaceLevel;
	}

	public void setSurfaceLevel(Integer surfaceLevel) {
		this.surfaceLevel = surfaceLevel;
	}

	public Integer getConditionLevel() {
		return this.conditionLevel;
	}

	public void setConditionLevel(Integer conditionLevel) {
		this.conditionLevel = conditionLevel;
	}

	public Integer getDoLevel() {
		return this.doLevel;
	}

	public void setDoLevel(Integer doLevel) {
		this.doLevel = doLevel;
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