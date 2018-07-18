package com.rskytech.basedata.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.basedata.dao.IComVendorDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ComVendor;

public class ComVendorBo extends BaseBO implements IComVendorBo {

	private IComVendorDao comVendorDao;
	
	public List<ComVendor> getVendorList(String msId){
		return comVendorDao.loadVendorList(msId);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap> loadVendorList(String msId){
		List<ComVendor> list = comVendorDao.loadVendorList(msId);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComVendor cv : list) {
				HashMap hm = new HashMap();
				hm.put("vendorId", cv.getVendorId());
				hm.put("vendorCode", cv.getVendorCode());
				hm.put("vendorName", cv.getVendorName());
				hm.put("remark", cv.getRemark());
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public String newOrUpdateVendor(ComUser user, ComModelSeries ms, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		String msId = ms.getModelSeriesId();
		
		//判断编号是否重复
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			String vendorCode = jsonArray.getJSONObject(i).getString("vendorCode").trim();					
			js.add(vendorCode);
		}
		if (js.size() != jsonArray.size()){
			return "exits";
		}
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("vendorId");
			
			//判断编号是否重复
			boolean bool = comVendorDao.checkVendor(msId, id, jsonObject.getString("vendorCode"));
			if (bool){
				return "exits";
			}
			
			ComVendor cv = new ComVendor();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				cv = (ComVendor) comVendorDao.loadById(ComVendor.class, id);
				
				cv.setVendorCode(jsonObject.getString("vendorCode"));
				cv.setVendorName(jsonObject.getString("vendorName"));
				cv.setRemark(jsonObject.getString("remark"));
				cv.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(cv, ComacConstants.DB_UPDATE, user.getUserId());																								
			} else {// 追加操作
				cv.setComModelSeries(ms);
				cv.setVendorCode(jsonObject.getString("vendorCode"));
				cv.setVendorName(jsonObject.getString("vendorName"));
				cv.setRemark(jsonObject.getString("remark"));
				cv.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(cv, ComacConstants.DB_INSERT, user.getUserId());
			}
		}	
		return "success";
	}
	
	/**
	 * 根据供应商Id查询供应商名称
	 * @param vendorId
	 */
	public String getVendorNameById(String vendorId){
		ComVendor comVendor = (ComVendor) this.loadById(ComVendor.class, vendorId);
		if(comVendor!=null){
			return comVendor.getVendorName();
		}else{
			return null;
		}
	}
	
	/*
	 * 根据供应商Id查询供应商编号
	 * @param vendorId
	 */
	public String getVendorCodeById(String vendorId){
		ComVendor comVendor = (ComVendor) this.loadById(ComVendor.class, vendorId);
		if(comVendor!=null){
			return comVendor.getVendorCode();
		}else{
			return null;
		}
	}
	
	public IComVendorDao getComVendorDao() {
		return comVendorDao;
	}

	public void setComVendorDao(IComVendorDao comVendorDao) {
		this.comVendorDao = comVendorDao;
	}
	
}
