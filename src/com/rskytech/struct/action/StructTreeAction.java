package com.rskytech.struct.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.pojo.ComUser;
import com.rskytech.struct.bo.IStructTreeBo;

public class StructTreeAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IStructTreeBo structTreeBo;
	
	private String id;//区域树的节点ID
	private String level;
	private String searchType;
	private String type;
	private String isSsi;
	private String ssiId;

	public String getIsSsi() {
		return isSsi;
	}

	public void setIsSsi(String isSsi) {
		this.isSsi = isSsi;
	}

	/**
	 * 初始化页面
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String init(){
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		
		}
		
		return SUCCESS;
	}
	
	public void getAtaOrSsiTree(){
		JSONArray json = new JSONArray();
		json.addAll(structTreeBo.searchSubAtaOrSsiTreeList(this.getSysUser(), id, level, this.getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
	}
		
	public void getAtaOrSsiGrid(){
		JSONObject json = new JSONObject();
		json.element("ata", structTreeBo.searchMyMaintainList(this.getSysUser(), this.getComModelSeries().getModelSeriesId(), searchType));
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
		boolean flag = structTreeBo.openAnalysisStatus(ssiId);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	public IStructTreeBo getStructTreeBo() {
		return structTreeBo;
	}

	public void setStructTreeBo(IStructTreeBo structTreeBo) {
		this.structTreeBo = structTreeBo;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSsiId() {
		return ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}
	
}
