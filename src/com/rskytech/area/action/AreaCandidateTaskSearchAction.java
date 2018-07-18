package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IAreaCandidateTaskSearchBo;

public class AreaCandidateTaskSearchAction extends BaseAction {

	private static final long serialVersionUID = 6097543204781172173L;

	private IAreaCandidateTaskSearchBo areaCandidateTaskSearchBo;
	
	private String sourceSystem;
	private String taskType;
	private String taskCode;
	
	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String showAreaCandidateTaskList(){
		if (this.getPage() == null) this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) this.getPage().setPageSize(getLimit());
		
		String msId = getComModelSeries().getModelSeriesId();
			
		if (ComacConstants.ALL.equals(this.sourceSystem) || (this.sourceSystem == null)){
			this.sourceSystem = "";
		}
		
		if (ComacConstants.ALL.equals(this.taskType) || (this.taskType == null)){
			this.taskType = "";
		}
		
		if (this.taskCode == null){
			this.taskCode = "";
		}
		
		List<HashMap> listJsonFV = areaCandidateTaskSearchBo.getTaskMsgList(msId, sourceSystem, taskType, taskCode, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("tasks", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public IAreaCandidateTaskSearchBo getAreaCandidateTaskSearchBo() {
		return areaCandidateTaskSearchBo;
	}

	public void setAreaCandidateTaskSearchBo(
			IAreaCandidateTaskSearchBo areaCandidateTaskSearchBo) {
		this.areaCandidateTaskSearchBo = areaCandidateTaskSearchBo;
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
