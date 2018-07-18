package com.rskytech.basedata.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComAtaAction extends BaseAction {

	private static final long serialVersionUID = 1511750655484394347L;
	
	private IComAtaBo comAtaBo;
	private IComAtaDao comAtaDao;
	private IZa7Dao za7Dao;
	
	private String treeId;//树节点的ID
	private String ataId;
	private String parentId;

	public String init(){
		return SUCCESS;
	}
	
	//查询ATA树的分支信息
	@SuppressWarnings("unchecked")
	public String loadAtaTree(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comAtaBo.loadAtaTree(msId, treeId);
		
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	
	//查询ATA列表信息
	@SuppressWarnings("unchecked")
	public String loadAta(){
		if (this.getPage() == null) 
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comAtaBo.loadAtaList(msId, ataId, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("ComAta", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	public String saveAta(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			HashMap<String, String> map = comAtaBo.newOrUpdateMs(user, ms, jsonData, parentId);
			
			if ("success".equals(map.get("return"))){
				HashMap<String, String> mapPro = comAtaDao.importAta(ms.getModelSeriesId(), "2");
				if ("导入失败".equals(mapPro.get("res"))){
					json.put("msg", mapPro.get("msg"));
					
					//还原修改的ATA数据
					if ("update".equals(map.get("biaoshi"))){
						ComAta comAta = (ComAta) comAtaDao.loadById(ComAta.class, map.get("ataId"));
						
						comAta.setAtaCode(map.get("ataCode"));
						comAta.setAtaName(map.get("ataName"));
						comAta.setEquipmentName(map.get("equipmentName"));
						comAta.setEquipmentPicNo(map.get("equipmentPicNo"));
						comAta.setEquipmentTypeNo(map.get("equipmentTypeNo"));
						comAta.setEquipmentPosition(map.get("equipmentPosition"));
						comAta.setRemark(map.get("remark"));
						comAta.setValidFlag(1);
						comAtaBo.saveOrUpdate(comAta, ComacConstants.DB_UPDATE, user.getUserId());				
					} else if ("insert".equals(map.get("biaoshi"))){
						comAtaBo.delete(ComAta.class, map.get("ataId"));
					}
				} else {
					json.put("msg", "保存成功");
				}
			}else if ("havemsi".equals(map.get("return"))){
				json.put("msg", "ATA编号已存在系统分析，不能修改");
			} else {
				json.put("msg", "ATA编号已存在");
			}
			
			writeToResponse(json.toString());
			return null;
		}
		
		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (ataId != null && !"".equals(ataId)) {
				ComAta comAta = (ComAta) comAtaBo.loadById(ComAta.class, ataId);	
				if (comAta != null){
					comAtaBo.deleteAta(ms.getModelSeriesId(), ataId);
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

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}

	public String getAtaId() {
		return ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}

	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}
	
}
