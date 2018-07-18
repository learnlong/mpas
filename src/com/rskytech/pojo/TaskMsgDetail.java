package com.rskytech.pojo;

import java.util.Date;

/**
 * TaskMsgDetail entity. @author MyEclipse Persistence Tools
 */

public class TaskMsgDetail implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8189399113319818648L;
	private String comTaskDetId;
	private TaskMsg taskMsg;
	private String whereTransfer;
	private Integer hasAccept;
	private String rejectReason;
	private String destTask;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public TaskMsgDetail() {
	}

	/** full constructor */
	public TaskMsgDetail(TaskMsg taskMsg, String whereTransfer,
			Integer hasAccept, String rejectReason, String destTask,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate) {
		this.taskMsg = taskMsg;
		this.whereTransfer = whereTransfer;
		this.hasAccept = hasAccept;
		this.rejectReason = rejectReason;
		this.destTask = destTask;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getComTaskDetId() {
		return this.comTaskDetId;
	}

	public void setComTaskDetId(String comTaskDetId) {
		this.comTaskDetId = comTaskDetId;
	}

	public TaskMsg getTaskMsg() {
		return this.taskMsg;
	}

	public void setTaskMsg(TaskMsg taskMsg) {
		this.taskMsg = taskMsg;
	}

	public String getWhereTransfer() {
		return this.whereTransfer;
	}

	public void setWhereTransfer(String whereTransfer) {
		this.whereTransfer = whereTransfer;
	}

	public Integer getHasAccept() {
		return this.hasAccept;
	}

	public void setHasAccept(Integer hasAccept) {
		this.hasAccept = hasAccept;
	}

	public String getRejectReason() {
		return this.rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getDestTask() {
		return this.destTask;
	}

	public void setDestTask(String destTask) {
		this.destTask = destTask;
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