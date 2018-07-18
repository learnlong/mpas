package com.rskytech.task.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.task.bo.IMpdMaintainBo;

public class MpdSearchAction extends BaseAction {

	private static final long serialVersionUID = -4846214714301622927L;

	private IMpdMaintainBo mpdMaintainBo;

	private String sourceSystem;
	private String taskType;
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

	public String getMpdCode() {
		return mpdCode;
	}

	public void setMpdCode(String mpdCode) {
		this.mpdCode = mpdCode;
	}
	
}
