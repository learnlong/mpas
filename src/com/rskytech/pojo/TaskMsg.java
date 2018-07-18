package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TaskMsg entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class TaskMsg implements java.io.Serializable, Cloneable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -9186640488784209022L;
	private String taskId;
	private ComModelSeries comModelSeries;
	private String sourceSystem;
	private String sourceAnaId;
	private String sourceStep;
	private Integer taskValid;
	private String taskCode;
	private String taskType;
	private String taskDesc;
	private String reachWay;
	private String taskInterval;
	private String taskIntervalMerge;
	private String taskIntervalRepeat;
	private String destTask;
	private String ownArea;
	private Integer needTransfer;
	private String sysTransfer;
	private String whyTransfer;
	private String effectiveness;
	private String mrbId;
	private String mpdId;
	private String anyContent1;
	private String anyContent2;
	private String anyContent3;
	private String anyContent4;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Integer hasAccept;
	private String rejectReason;
	private String remark;
	private Set taskMsgDetails = new HashSet(0);
	private String taskDesc2;

	// Constructors

	///copy 复制一条记录
	@Override
	public  TaskMsg clone() throws CloneNotSupportedException {
		Object obj =  super.clone();
		return (TaskMsg)obj;
	}
	/** default constructor */
	public TaskMsg() {
	}

	/** full constructor */
	public TaskMsg(ComModelSeries comModelSeries, String sourceSystem,
			String sourceAnaId,String sourceStep,
			Integer taskValid, String taskCode, String taskType,
			String taskDesc, String reachWay, String taskInterval,
			String taskIntervalMerge, String taskIntervalRepeat, String destTask, String rejectReason,
			String ownArea, Integer needTransfer, String sysTransfer,Integer hasAccept,
			String whyTransfer, String effectiveness, String mrbId,
			String mpdId, String anyContent1, String anyContent2,
			String anyContent3, String anyContent4, Integer validFlag,
			String createUser, Date createDate, String modifyUser,String remark,
			Date modifyDate, Set taskMsgDetails) {
		this.comModelSeries = comModelSeries;
		this.sourceSystem = sourceSystem;
		this.sourceAnaId = sourceAnaId;
		this.sourceStep = sourceStep;
		this.taskValid = taskValid;
		this.taskCode = taskCode;
		this.taskType = taskType;
		this.taskDesc = taskDesc;
		this.reachWay = reachWay;
		this.taskInterval = taskInterval;
		this.taskIntervalMerge = taskIntervalMerge;
		this.taskIntervalRepeat = taskIntervalRepeat;
		this.destTask = destTask;
		this.ownArea = ownArea;
		this.needTransfer = needTransfer;
		this.hasAccept=hasAccept;
		this.sysTransfer = sysTransfer;
		this.whyTransfer = whyTransfer;
		this.effectiveness = effectiveness;
		this.mrbId = mrbId;
		this.mpdId = mpdId;
		this.remark=remark;
		this.anyContent1 = anyContent1;
		this.anyContent2 = anyContent2;
		this.anyContent3 = anyContent3;
		this.anyContent4 = anyContent4;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.taskMsgDetails = taskMsgDetails;
		this.rejectReason=rejectReason;
	}

	// Property accessors

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getSourceSystem() {
		return this.sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getSourceAnaId() {
		return sourceAnaId;
	}

	public void setSourceAnaId(String sourceAnaId) {
		this.sourceAnaId = sourceAnaId;
	}

	public String getSourceStep() {
		return this.sourceStep;
	}

	public void setSourceStep(String sourceStep) {
		this.sourceStep = sourceStep;
	}

	public Integer getTaskValid() {
		return this.taskValid;
	}

	public void setTaskValid(Integer taskValid) {
		this.taskValid = taskValid;
	}

	public String getTaskCode() {
		return this.taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskDesc() {
		return this.taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getReachWay() {
		return this.reachWay;
	}

	public void setReachWay(String reachWay) {
		this.reachWay = reachWay;
	}

	public String getTaskInterval() {
		return this.taskInterval;
	}

	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}

	public String getTaskIntervalMerge() {
		return this.taskIntervalMerge;
	}

	public void setTaskIntervalMerge(String taskIntervalMerge) {
		this.taskIntervalMerge = taskIntervalMerge;
	}

	public String getTaskIntervalRepeat() {
		return this.taskIntervalRepeat;
	}

	public void setTaskIntervalRepeat(String taskIntervalRepeat) {
		this.taskIntervalRepeat = taskIntervalRepeat;
	}

	public String getOwnArea() {
		return this.ownArea;
	}

	public void setOwnArea(String ownArea) {
		this.ownArea = ownArea;
	}

	public Integer getNeedTransfer() {
		return this.needTransfer;
	}

	public void setNeedTransfer(Integer needTransfer) {
		this.needTransfer = needTransfer;
	}

	public Integer getHasAccept() {
		return hasAccept;
	}

	public void setHasAccept(Integer hasAccept) {
		this.hasAccept = hasAccept;
	}

	public String getSysTransfer() {
		return this.sysTransfer;
	}

	public void setSysTransfer(String sysTransfer) {
		this.sysTransfer = sysTransfer;
	}

	public String getWhyTransfer() {
		return this.whyTransfer;
	}

	public void setWhyTransfer(String whyTransfer) {
		this.whyTransfer = whyTransfer;
	}

	public String getEffectiveness() {
		return this.effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}

	public String getMrbId() {
		return this.mrbId;
	}

	public void setMrbId(String mrbId) {
		this.mrbId = mrbId;
	}

	public String getMpdId() {
		return this.mpdId;
	}

	public void setMpdId(String mpdId) {
		this.mpdId = mpdId;
	}

	public String getAnyContent1() {
		return this.anyContent1;
	}

	public void setAnyContent1(String anyContent1) {
		this.anyContent1 = anyContent1;
	}

	public String getAnyContent2() {
		return this.anyContent2;
	}

	public void setAnyContent2(String anyContent2) {
		this.anyContent2 = anyContent2;
	}

	public String getAnyContent3() {
		return this.anyContent3;
	}

	public void setAnyContent3(String anyContent3) {
		this.anyContent3 = anyContent3;
	}

	public String getAnyContent4() {
		return this.anyContent4;
	}

	public void setAnyContent4(String anyContent4) {
		this.anyContent4 = anyContent4;
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

	public Set getTaskMsgDetails() {
		return this.taskMsgDetails;
	}

	public void setTaskMsgDetails(Set taskMsgDetails) {
		this.taskMsgDetails = taskMsgDetails;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDestTask() {
		return destTask;
	}
	public void setDestTask(String destTask) {
		this.destTask = destTask;
	}
	public String getTaskDesc2() {
		return taskDesc2;
	}
	public void setTaskDesc2(String taskDesc2) {
		this.taskDesc2 = taskDesc2;
	}

}