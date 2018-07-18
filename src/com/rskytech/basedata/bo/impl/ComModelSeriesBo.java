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
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComModelSeriesBo;
import com.rskytech.basedata.dao.IComModelSeriesDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComModelSeriesBo extends BaseBO implements IComModelSeriesBo {

	private IComModelSeriesDao comModelSeriesDao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> loadModelSeriesList(String msCode, String msName, Page page){
		List<ComModelSeries> list = comModelSeriesDao.getComModelSeriesList(msCode, msName, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList<HashMap>();
			
			for (ComModelSeries ms : list) {
				HashMap hm = new HashMap();
				hm.put("modelSeriesId", ms.getModelSeriesId());
				hm.put("modelSeriesCode", ms.getModelSeriesCode());
				hm.put("modelSeriesName", ms.getModelSeriesName());
				hm.put("defaultModelSeries", ms.getDefaultModelSeries() == 1 ? "是" : "否");
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public boolean checkModelSeries(String msCode, String msId){
		List<ComModelSeries> list = comModelSeriesDao.getModelSeriesByCodeNotNow(msCode, msId);
		if (list != null && list.size() > 0){		
			return false;
		} else {
			return true;
		}	
	}
	
	public String newOrUpdateMs(ComUser user, ComModelSeries ms, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		//判断编号是否重复
		Set<String> js = new HashSet<String>();
		for (int i = 0; i < jsonArray.size(); i++){
			String modelSeriesCode = jsonArray.getJSONObject(i).getString("modelSeriesCode").trim();					
			js.add(modelSeriesCode);
		}
		if (js.size() != jsonArray.size()){
			return "exits";
		}
		
		//开始保存操作
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("modelSeriesId");;
			
			ComModelSeries comModelSeries = new ComModelSeries();
			//修改操作
			if (!BasicTypeUtils.isNullorBlank(id)) {
				if (ms.getModelSeriesId().equals(id)){//当前使用机型不允许修改
					return "nowMs";
				} else {
					comModelSeries = (ComModelSeries) comModelSeriesDao.loadById(ComModelSeries.class, id);
					comModelSeries.setModelSeriesCode(jsonObject.getString("modelSeriesCode"));
					comModelSeries.setModelSeriesName(jsonObject.getString("modelSeriesName"));
					this.saveOrUpdate(comModelSeries, ComacConstants.DB_UPDATE, user.getUserId());																								
				}
			} else {// 追加操作
				Boolean cms = checkModelSeries(jsonObject.getString("modelSeriesCode"), null);//检查机型系列编号是否存在(新增数据)
				if (!cms){
					return "exits";
				} else {
					comModelSeries.setModelSeriesCode(jsonObject.getString("modelSeriesCode"));
					comModelSeries.setModelSeriesName(jsonObject.getString("modelSeriesName"));
					comModelSeries.setDefaultModelSeries(0);
					comModelSeries.setValidFlag(ComacConstants.VALIDFLAG_YES);
					this.saveOrUpdate(comModelSeries, ComacConstants.DB_INSERT, user.getUserId());
				}
			}
		}	
		return "success";
	}
	
	public String defaultModelSeries(ComUser user, String msId){
		if (msId != null && !"".equals(msId)) {
			ComModelSeries ms = (ComModelSeries) comModelSeriesDao.loadById(ComModelSeries.class, msId);
			if (ms != null){
				List<ComModelSeries> msList = comModelSeriesDao.getDefaultMs();
				if (msList != null){
					for (ComModelSeries c : msList){
						c.setDefaultModelSeries(0);
						this.saveOrUpdate(c, ComacConstants.DB_UPDATE, user.getUserId());
					}
				}
				
				ms.setDefaultModelSeries(1);
				this.saveOrUpdate(ms, ComacConstants.DB_UPDATE, user.getUserId());
				return "true";
			}
		} 
		return "false";
	}
	
	@Override
	public ComModelSeries getMsByMsCode(String msCode){
		List<ComModelSeries> list = comModelSeriesDao.getModelSeriesByCodeNotNow(msCode, null);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public boolean copyDefaultCustomData(String msId, String userId){
		 return comModelSeriesDao.copyDefaultCustomData(msId, userId);
	}
	
	public boolean deleteModelSeries(String modelSeriesId) {
		return this.comModelSeriesDao.deleteModelSeries(modelSeriesId);
	}

	public IComModelSeriesDao getComModelSeriesDao() {
		return comModelSeriesDao;
	}

	public void setComModelSeriesDao(IComModelSeriesDao comModelSeriesDao) {
		this.comModelSeriesDao = comModelSeriesDao;
	}
	
}
