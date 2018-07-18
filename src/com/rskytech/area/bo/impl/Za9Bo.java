package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa9Bo;
import com.rskytech.area.dao.IZa9Dao;
import com.rskytech.pojo.TaskMsg;

public class Za9Bo extends BaseBO implements IZa9Bo {

	private IZa9Dao za9Dao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa9List(String msId, String zaId){
		List<HashMap> listFV = new ArrayList<HashMap>();
		List<TaskMsg> list = za9Dao.searchTask(msId, zaId);
		
		if (list != null){
			HashMap hm;
			for (TaskMsg task : list){
				hm = new HashMap();
				hm.put("taskId", task.getTaskId());
				hm.put("taskCode", task.getTaskCode());
				hm.put("reachWay", task.getReachWay());
				hm.put("taskDesc", task.getTaskDesc());
				hm.put("taskInterval", task.getTaskInterval());		
				hm.put("effectiveness", task.getEffectiveness());// 试用性
				
				listFV.add(hm);
			}
		}
		return listFV;
	}
	
	public void saveZa9(String userId, String msId, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("taskId");
			
			TaskMsg tm = (TaskMsg) this.loadById(TaskMsg.class, id);
			if (tm != null){
				tm.setEffectiveness(jsonObject.getString("effectiveness"));
				this.saveOrUpdate(tm, ComacConstants.DB_UPDATE, userId);			
			}
		}	
	}

	public IZa9Dao getZa9Dao() {
		return za9Dao;
	}

	public void setZa9Dao(IZa9Dao za9Dao) {
		this.za9Dao = za9Dao;
	}
	
}
