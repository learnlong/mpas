package com.rskytech.lhirf.bo.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh6Bo;
import com.rskytech.lhirf.dao.ILh6Dao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMsg;

public class Lh6Bo extends BaseBO implements ILh6Bo {
	
	 private ILh6Dao lh6Dao;
	
	
	/*
	 * 根据 机型ID 区域ID 分页查询 HSI MSG-3任务详细情况
	 */
	
	@Override
	public Page getLhirfListById(String modelSeriesId, String AreaId, Page page)
			throws BusinessException {
		
		return this.lh6Dao.getLhirfTaskMsgList(modelSeriesId, AreaId, page);
	}
	
	@Override
	public List<Object[]> getLhirfListByIdNoPage(String modelSeriesId, String AreaId)
			throws BusinessException {
		
		return this.lh6Dao.getLhirfListByIdNoPage(modelSeriesId, AreaId);
	}
	///根据 hsiCode  areaId , 查询lh1, lh1a
	public void saveLh6LhEff(ComUser user, String jsonData)
			throws BusinessException {
		this.saveComLogOperate(user, "LH6", ComacConstants.LHIRF_CODE);
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = "";
		TaskMsg msg;
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String taskId = jsonObject.getString("taskId");
			msg=(TaskMsg) this.loadById(TaskMsg.class, taskId);
			msg.setEffectiveness(jsonObject.getString("lheff")==null?"":jsonObject.getString("lheff"));
			dbOperate = ComacConstants.DB_UPDATE;
			this.saveOrUpdate(msg, dbOperate, user.getUserId());
		}

	}


	public ILh6Dao getLh6Dao() {
		return lh6Dao;
	}


	public void setLh6Dao(ILh6Dao lh6Dao) {
		this.lh6Dao = lh6Dao;
	}

}
