package com.rskytech.task.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.task.bo.IMpdMaintainBo;

public class MpdMaintainAction extends BaseAction {

	private static final long serialVersionUID = 3937983769967251448L;
	
	private IMpdMaintainBo mpdMaintainBo;

	private String sourceSystem;
	private String taskType;
	private String mpdId;
	private String mpdCode;

	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void searchTaskMpd(){
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		List<HashMap> listJsonFV = mpdMaintainBo.getTaskMpdList(getComModelSeries().getModelSeriesId(), sourceSystem, taskType, mpdCode, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("taskMpd", listJsonFV);
		writeToResponse(json.toString());
	}
	
	// 检测mpdcode是否已经存在
	public void checkTaskMpdCode() {
		if (mpdCode != null && !"".equals(mpdCode)) {
			boolean flag = this.mpdMaintainBo.checkTaskMpdCode(mpdId, mpdCode,this.getComModelSeries().getModelSeriesId());
			if (!flag) {
				writeToResponse("false");
			} else {
				writeToResponse("true");
			}
		}
	}
	
	public void saveMpd(){
		mpdMaintainBo.saveMpd(getSysUser().getUserId(), getComModelSeries(), jsonData);
	}
	
	public void delTaskMpd() {
		TaskMpd taskMpd = (TaskMpd) mpdMaintainBo.loadById(TaskMpd.class, mpdId);
		
		if(taskMpd != null && "ONESELFADD".equals(taskMpd.getSourceSystem())){
			mpdMaintainBo.delete(taskMpd);
		}
	}

	public IMpdMaintainBo getMpdMaintainBo() {
		return mpdMaintainBo;
	}

	public void setMpdMaintainBo(IMpdMaintainBo mpdMaintainBo) {
		this.mpdMaintainBo = mpdMaintainBo;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getMpdId() {
		return mpdId;
	}

	public void setMpdId(String mpdId) {
		this.mpdId = mpdId;
	}

	public String getMpdCode() {
		return mpdCode;
	}

	public void setMpdCode(String mpdCode) {
		this.mpdCode = mpdCode;
	}
	
}
