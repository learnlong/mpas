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
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.basedata.dao.IComMmelDao;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComMmelBo extends BaseBO implements IComMmelBo {

private IComMmelDao comMmelDao;
	
	public List<ComMmel> getMmelList(String msId){
		return comMmelDao.loadMmelList(msId);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap> loadMmelList(String msId){
		List<ComMmel> list = comMmelDao.loadMmelList(msId);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (ComMmel cm : list) {
				HashMap hm = new HashMap();
				hm.put("mmelId", cm.getMmelId());
				hm.put("mmelCode", cm.getMmelCode());
				hm.put("mmelName", cm.getMmelName());
				hm.put("remark", cm.getRemark());
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public String newOrUpdateMmel(ComUser user, ComModelSeries ms, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		String msId = ms.getModelSeriesId();
		
		//判断编号是否重复
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			String mmelCode = jsonArray.getJSONObject(i).getString("mmelCode").trim();					
			js.add(mmelCode);
		}
		if (js.size() != jsonArray.size()){
			return "exits";
		}
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("mmelId");
			
			//判断编号是否重复
			boolean bool = comMmelDao.checkMmel(msId, id, jsonObject.getString("mmelCode"));
			if (bool){
				return "exits";
			}
			
			ComMmel cm = new ComMmel();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				cm = (ComMmel) comMmelDao.loadById(ComMmel.class, id);
				
				cm.setMmelCode(jsonObject.getString("mmelCode"));
				cm.setMmelName(jsonObject.getString("mmelName"));
				cm.setRemark(jsonObject.getString("remark"));
				cm.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(cm, ComacConstants.DB_UPDATE, user.getUserId());																								
			} else {// 追加操作
				cm.setComModelSeries(ms);
				cm.setMmelCode(jsonObject.getString("mmelCode"));
				cm.setMmelName(jsonObject.getString("mmelName"));
				cm.setRemark(jsonObject.getString("remark"));
				cm.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(cm, ComacConstants.DB_INSERT, user.getUserId());
			}
		}	
		return "success";
	}

	public IComMmelDao getComMmelDao() {
		return comMmelDao;
	}

	public void setComMmelDao(IComMmelDao comMmelDao) {
		this.comMmelDao = comMmelDao;
	}
	
}
