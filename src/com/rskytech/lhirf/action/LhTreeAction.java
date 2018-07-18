package com.rskytech.lhirf.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.lhirf.bo.ILhTreeBo;

public class LhTreeAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private ILhTreeBo lhTreeBo;

	private String id;//区域树的节点ID
	private String searchType;
	private String type;
	private String lhId;
	
	/**
	 * 初始化页面
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String init(){
		return SUCCESS;
	}
	
	public void getAreaOrHsiTree(){
		JSONArray json = new JSONArray();
		json.addAll(lhTreeBo.searchSubAreaOrHsiTreeList(this.getSysUser(), id, this.getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
	}
	
	public void getAreaOrHsiGrid(){
		JSONObject json = new JSONObject();
		json.element("areas", lhTreeBo.searchMyMaintainList(this.getSysUser(), this.getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
	}

	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月15日 上午11:21:22
	* @throws
	 */
	public String openAnalysisStatus(){
		JSONObject json = new JSONObject();
		boolean flag = lhTreeBo.openAnalysisStatus(lhId);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	public ILhTreeBo getLhTreeBo() {
		return lhTreeBo;
	}

	public void setLhTreeBo(ILhTreeBo lhTreeBo) {
		this.lhTreeBo = lhTreeBo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLhId() {
		return lhId;
	}

	public void setLhId(String lhId) {
		this.lhId = lhId;
	}
	
}
