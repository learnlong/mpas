package com.rskytech.task.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.task.bo.IMrbMaintainBo;

public class MrbSearchAction extends BaseAction {

	private static final long serialVersionUID = -4972150062524643897L;

	private IMrbMaintainBo mrbMaintainBo;
	
	private String sourceSystem;
	private String taskType;
	private String mrbCode;
	
	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void searchTaskMrb(){
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		List<HashMap> listJsonFV = mrbMaintainBo.getTaskMrbList(getComModelSeries().getModelSeriesId(), sourceSystem, taskType, mrbCode, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("taskMrb", listJsonFV);
		writeToResponse(json.toString());
	}

	public IMrbMaintainBo getMrbMaintainBo() {
		return mrbMaintainBo;
	}

	public void setMrbMaintainBo(IMrbMaintainBo mrbMaintainBo) {
		this.mrbMaintainBo = mrbMaintainBo;
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

	public String getMrbCode() {
		return mrbCode;
	}

	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}
	
}
