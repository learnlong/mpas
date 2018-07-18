package com.rskytech.task.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.task.bo.IMrbMaintainBo;

public class MrbMaintainAction extends BaseAction {

	private static final long serialVersionUID = 5948134412753027816L;
	
	private IMrbMaintainBo mrbMaintainBo;
	
	private String sourceSystem;
	private String taskType;
	private String mrbId;
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
	
	// 检测mrbcode是否已经存在
	public void checkTaskMrbCode() {
		if (mrbCode != null && !"".equals(mrbCode)) {
			boolean flag = this.mrbMaintainBo.checkTaskMrbCode(mrbId, mrbCode,this.getComModelSeries().getModelSeriesId());
			if (!flag) {
				writeToResponse("false");
			} else {
				writeToResponse("true");
			}
		}
	}
	
	public void saveMrb(){
		mrbMaintainBo.saveMrb(getSysUser().getUserId(), jsonData);
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

	public String getMrbId() {
		return mrbId;
	}

	public void setMrbId(String mrbId) {
		this.mrbId = mrbId;
	}

	public String getMrbCode() {
		return mrbCode;
	}

	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}
	
}
