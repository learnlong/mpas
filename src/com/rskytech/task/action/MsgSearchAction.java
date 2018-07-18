package com.rskytech.task.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.task.bo.IMsgSearchBo;

public class MsgSearchAction extends BaseAction {

	private static final long serialVersionUID = -4805310567074091542L;

	private IMsgSearchBo msgSearchBo;

	private String sourceSystem;
	private String taskType;
	private String taskCode;

	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void searchTaskMsg(){
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		List<HashMap> listJsonFV = msgSearchBo.getTaskMsgList(getComModelSeries().getModelSeriesId(), sourceSystem, taskType, taskCode, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("taskMsg", listJsonFV);
		writeToResponse(json.toString());
	}

	public IMsgSearchBo getMsgSearchBo() {
		return msgSearchBo;
	}

	public void setMsgSearchBo(IMsgSearchBo msgSearchBo) {
		this.msgSearchBo = msgSearchBo;
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

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	
}
