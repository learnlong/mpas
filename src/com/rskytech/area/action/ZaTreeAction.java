package com.rskytech.area.action;



import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZaTreeBO;

/**
 * 包括所有区域树的查询功能
 * @author 张建民
 * @createdate 2012-8-27
 */
public class ZaTreeAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	
	private IZaTreeBO zaTreeBO;
	private String id;//区域树的节点ID
	private String searchType;
	private String type;
	private String areaId;
	private Integer anode;//没有用到
	private Double versionNo;
	private String zaAnaId;
	private String versionDescCn;
	private String versionDescEn;
	private String zaId;
	/**
	 * 初始化页面
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 查询并返回指定节点下的所有区域
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String getAreaTree() {
		JSONArray json = new JSONArray();
		json.addAll(zaTreeBO.searchSubAreaTreeList(this.getSysUser(), id, getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
		return null;
	}
	/**
	 * 查询并返回该用户所有的拥有维护权限的区域
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String getAreaGrid() {
		JSONObject json = new JSONObject();
		json.element("areas", zaTreeBO.searchMyMaintainList(this.getSysUser(), getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
		return null;
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
		boolean flag = zaTreeBO.openAnalysisStatus(zaId);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	public IZaTreeBO getZaTreeBO() {
		return zaTreeBO;
	}

	public void setZaTreeBO(IZaTreeBO zaTreeBO) {
		this.zaTreeBO = zaTreeBO;
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

	public Integer getAnode() {
		return anode;
	}

	public void setAnode(Integer anode) {
		this.anode = anode;
	}

	public Double getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Double versionNo) {
		this.versionNo = versionNo;
	}

	public String getVersionDescCn() {
		return versionDescCn;
	}

	public void setVersionDescCn(String versionDescCn) {
		this.versionDescCn = versionDescCn;
	}

	public String getVersionDescEn() {
		return versionDescEn;
	}

	public void setVersionDescEn(String versionDescEn) {
		this.versionDescEn = versionDescEn;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getZaAnaId() {
		return zaAnaId;
	}

	public void setZaAnaId(String zaAnaId) {
		this.zaAnaId = zaAnaId;
	}

	public String getZaId() {
		return zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
	}
	
}
