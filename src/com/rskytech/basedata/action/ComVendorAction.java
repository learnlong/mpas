package com.rskytech.basedata.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ComVendor;

public class ComVendorAction extends BaseAction {

	private static final long serialVersionUID = -3212445408400735425L;

	private IComVendorBo comVendorBo;
	
	private String vendorId;
	
	public String init(){
		return SUCCESS;
	}
	
	//查询供应商列表信息
	@SuppressWarnings("unchecked")
	public String loadVendor(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comVendorBo.loadVendorList(msId);
		
		JSONObject json = new JSONObject();
		json.element("ComVendor", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	//新增、删除、保存供应商
	public String saveVendor(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			String msg = comVendorBo.newOrUpdateVendor(user, ms, jsonData);
			
			json.put("msg", msg);
			writeToResponse(json.toString());
			return null;
		}
		
		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (vendorId != null && !"".equals(vendorId)) {
				ComVendor cv = (ComVendor) comVendorBo.loadById(ComVendor.class, vendorId);	
				if (cv != null){
					cv.setValidFlag(ComacConstants.VALIDFLAG_NO);
					comVendorBo.saveOrUpdate(cv, ComacConstants.DB_DELETE, user.getUserId());

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

	public IComVendorBo getComVendorBo() {
		return comVendorBo;
	}

	public void setComVendorBo(IComVendorBo comVendorBo) {
		this.comVendorBo = comVendorBo;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
}
