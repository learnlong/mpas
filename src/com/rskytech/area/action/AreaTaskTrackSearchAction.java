package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IAreaTaskTrackSearchBo;

public class AreaTaskTrackSearchAction extends BaseAction {

	private static final long serialVersionUID = -22820295931733163L;
	
	private IAreaTaskTrackSearchBo areaTaskTrackSearchBo;
	
	private String taskCode;
	
	public String init(){
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String findTaskTrackList(){
		if (this.getPage() == null) this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) this.getPage().setPageSize(getLimit());
		
		String msId = getComModelSeries().getModelSeriesId();
		
		if (this.taskCode == null){
			this.taskCode = "";
		}
		
		List<HashMap> listJsonFV = areaTaskTrackSearchBo.getTaskMsgList(msId, taskCode, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("tasks", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public IAreaTaskTrackSearchBo getAreaTaskTrackSearchBo() {
		return areaTaskTrackSearchBo;
	}

	public void setAreaTaskTrackSearchBo(
			IAreaTaskTrackSearchBo areaTaskTrackSearchBo) {
		this.areaTaskTrackSearchBo = areaTaskTrackSearchBo;
	}
	
}
