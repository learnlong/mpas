package com.rskytech.basedata.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComAreaAction extends BaseAction {

	private static final long serialVersionUID = -4869605883494742545L;

	private IComAreaBo comAreaBo;
	private IZa7Dao za7Dao;
	
	private String treeId;//树节点的ID
	private String areaId;
	private String parentId;
	private String detailId;

	public String init(){
		return SUCCESS;
	}
	
	//查询区域树的分支信息
	@SuppressWarnings("unchecked")
	public String loadAreaTree(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comAreaBo.loadAreaTree(msId, treeId);
		
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	
	//查询区域列表信息
	@SuppressWarnings("unchecked")
	public String loadArea(){
		if (this.getPage() == null) 
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comAreaBo.loadAreaList(msId, areaId, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("ComArea", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	//新增、删除、保存区域
	public String saveArea(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			String msg = comAreaBo.newOrUpdateArea(user, ms, jsonData, parentId);
			
			json.put("msg", msg);
			writeToResponse(json.toString());
			return null;
		}
		
		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (areaId != null && !"".equals(areaId)) {
				ComArea comArea = (ComArea) comAreaBo.loadById(ComArea.class, areaId);	
				if (comArea != null){
					comAreaBo.deleteArea(ms.getModelSeriesId(), areaId);
					za7Dao.cleanTaskInterval(ms.getModelSeriesId());

					json.put("msg", "true");
					writeToResponse(json.toString());
					return null;
				} else {
					json.put("msg", "false");
					writeToResponse(json.toString());
					return null;
				}
			}
		}
		return null;
	}
	
	//查询设备列表信息
	@SuppressWarnings("unchecked")
	public String loadEquip(){
		if (this.getPage() == null) 
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		List<HashMap> listJsonFV = comAreaBo.loadEquipList(areaId);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("equip", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	

	//新增、删除、保存设备
	public String saveEquip(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			String msg = comAreaBo.newOrUpdateEquip(jsonData, areaId);
			
			json.put("msg", msg);
			writeToResponse(json.toString());
			return null;
		}
		
		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (detailId != null && !"".equals(detailId)) {
				ComAreaDetail cad = (ComAreaDetail) comAreaBo.loadById(ComAreaDetail.class, detailId);	
				if (cad != null){
					comAreaBo.delete(cad);

					json.put("msg", "true");
					writeToResponse(json.toString());
					return null;
				} else {
					json.put("msg", "false");
					writeToResponse(json.toString());
					return null;
				}
			}
		}
		return null;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
