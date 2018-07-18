package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComArea entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComArea implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 5342728080536932890L;
	private String areaId;
	private ComArea comArea;
	private ComModelSeries comModelSeries;
	private String areaCode;
	private String areaName;
	private Integer areaLevel;
	private String reachWay;
	private String wirePiping;
	private String remark;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set zaMains = new HashSet(0);
	private Set comAreas = new HashSet(0);
	private Set comAreaDetails = new HashSet(0);
	private Set lhMains = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComArea() {
	}

	/** full constructor */
	public ComArea(ComArea comArea, ComModelSeries comModelSeries,
			String areaCode, String areaName, Integer areaLevel,
			String reachWay, String wirePiping, String remark,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Set zaMains, Set comAreas,
			Set comAreaDetails, Set lhMains) {
		this.comArea = comArea;
		this.comModelSeries = comModelSeries;
		this.areaCode = areaCode;
		this.areaName = areaName;
		this.areaLevel = areaLevel;
		this.reachWay = reachWay;
		this.wirePiping = wirePiping;
		this.remark = remark;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.zaMains = zaMains;
		this.comAreas = comAreas;
		this.comAreaDetails = comAreaDetails;
		this.lhMains = lhMains;
	}

	// Property accessors

	public String getAreaId() {
		return this.areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public ComArea getComArea() {
		return this.comArea;
	}

	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(Integer areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getReachWay() {
		return this.reachWay;
	}

	public void setReachWay(String reachWay) {
		this.reachWay = reachWay;
	}

	public String getWirePiping() {
		return this.wirePiping;
	}

	public void setWirePiping(String wirePiping) {
		this.wirePiping = wirePiping;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Set getZaMains() {
		return this.zaMains;
	}

	public void setZaMains(Set zaMains) {
		this.zaMains = zaMains;
	}

	public Set getComAreas() {
		return this.comAreas;
	}

	public void setComAreas(Set comAreas) {
		this.comAreas = comAreas;
	}

	public Set getComAreaDetails() {
		return this.comAreaDetails;
	}

	public void setComAreaDetails(Set comAreaDetails) {
		this.comAreaDetails = comAreaDetails;
	}

	public Set getLhMains() {
		return this.lhMains;
	}

	public void setLhMains(Set lhMains) {
		this.lhMains = lhMains;
	}

}