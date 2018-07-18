package com.rskytech.pojo;

import java.util.Date;

/**
 * ComCoordination entity. @author MyEclipse Persistence Tools
 */

public class ComCoordination implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4996121103945911260L;
	private String coordinationId;
	private ComModelSeries comModelSeries;
	private String type;
	private String taskId;
	private String s6OutOrIn;
	private String coordinationCode;
	private String theme;
	private String sendWorkgroup;
	private String receivedWorkgroup;
	private String sendUser;
	private String receiveUser;
	private String sendDate;
	private String receiveDate;
	private String receiveArea;
	private String sendContent;
	private String isReceived;
	private String receiveContent;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public ComCoordination() {
	}

	/** full constructor */
	public ComCoordination(ComModelSeries comModelSeries, String type,
			String taskId, String s6OutOrIn, String coordinationCode,
			String theme, String sendWorkgroup, String receivedWorkgroup,
			String sendUser, String receiveUser, String sendDate,
			String receiveDate, String receiveArea, String sendContent,
			String isReceived, String receiveContent, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.type = type;
		this.taskId = taskId;
		this.s6OutOrIn = s6OutOrIn;
		this.coordinationCode = coordinationCode;
		this.theme = theme;
		this.sendWorkgroup = sendWorkgroup;
		this.receivedWorkgroup = receivedWorkgroup;
		this.sendUser = sendUser;
		this.receiveUser = receiveUser;
		this.sendDate = sendDate;
		this.receiveDate = receiveDate;
		this.receiveArea = receiveArea;
		this.sendContent = sendContent;
		this.isReceived = isReceived;
		this.receiveContent = receiveContent;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getCoordinationId() {
		return this.coordinationId;
	}

	public void setCoordinationId(String coordinationId) {
		this.coordinationId = coordinationId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getS6OutOrIn() {
		return this.s6OutOrIn;
	}

	public void setS6OutOrIn(String s6OutOrIn) {
		this.s6OutOrIn = s6OutOrIn;
	}

	public String getCoordinationCode() {
		return this.coordinationCode;
	}

	public void setCoordinationCode(String coordinationCode) {
		this.coordinationCode = coordinationCode;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getSendWorkgroup() {
		return this.sendWorkgroup;
	}

	public void setSendWorkgroup(String sendWorkgroup) {
		this.sendWorkgroup = sendWorkgroup;
	}

	public String getReceivedWorkgroup() {
		return this.receivedWorkgroup;
	}

	public void setReceivedWorkgroup(String receivedWorkgroup) {
		this.receivedWorkgroup = receivedWorkgroup;
	}

	public String getSendUser() {
		return this.sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getReceiveUser() {
		return this.receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	public String getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getReceiveArea() {
		return this.receiveArea;
	}

	public void setReceiveArea(String receiveArea) {
		this.receiveArea = receiveArea;
	}

	public String getSendContent() {
		return this.sendContent;
	}

	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}

	public String getIsReceived() {
		return this.isReceived;
	}

	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}

	public String getReceiveContent() {
		return this.receiveContent;
	}

	public void setReceiveContent(String receiveContent) {
		this.receiveContent = receiveContent;
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

}