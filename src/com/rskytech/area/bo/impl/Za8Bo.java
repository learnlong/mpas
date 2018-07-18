package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa8Bo;
import com.rskytech.area.dao.IZa8Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public class Za8Bo extends BaseBO implements IZa8Bo {

	private IZa8Dao za8Dao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa8List(String msId, String zaId){
		List<HashMap> listFV = new ArrayList<HashMap>();
		List<TaskMsg> list = za8Dao.searchTask(msId, zaId);
		
		if (list != null){
			HashMap hm;
			for (TaskMsg task : list){
				hm = new HashMap();
				hm.put("taskId", task.getTaskId());
				hm.put("taskCode", task.getTaskCode());
				hm.put("taskType", task.getTaskType());
				hm.put("reachWay", task.getReachWay());
				hm.put("taskDesc", task.getTaskDesc());
				hm.put("taskInterval", task.getTaskIntervalMerge() == null ? task.getTaskInterval() : task.getTaskIntervalMerge());// 合并后的标准任务间隔				
				hm.put("effectiveness", task.getEffectiveness());// 试用性
				
				List<TaskMsgDetail> taskList = this.za8Dao.getTaskMsgDetailList(task.getTaskId());// 查询被该任务合并的所有任务
				String code = "";
				String sumTaskDesc = "";
				for (TaskMsgDetail taskMsgDetail : taskList) {
					code += taskMsgDetail.getTaskMsg().getTaskCode() + "</br>";
					if(taskMsgDetail.getTaskMsg().getTaskDesc()!=null){
						sumTaskDesc += taskMsgDetail.getTaskMsg().getTaskDesc() + "</br>";
					}
				}
				hm.put("mrbId", code);
				hm.put("sumTaskDesc", sumTaskDesc);
				listFV.add(hm);
			}
		}
		return listFV;
	}
	
	public void saveZa8(String userId, String msId, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("taskId");
			
			TaskMsg tm = (TaskMsg) this.loadById(TaskMsg.class, id);
			if (tm != null){
				tm.setEffectiveness(jsonObject.getString("effectiveness"));
				tm.setTaskDesc(jsonObject.getString("taskDesc"));
				this.saveOrUpdate(tm, ComacConstants.DB_UPDATE, userId);			
			}
		}	
	}

	public IZa8Dao getZa8Dao() {
		return za8Dao;
	}

	public void setZa8Dao(IZa8Dao za8Dao) {
		this.za8Dao = za8Dao;
	}
	
}
