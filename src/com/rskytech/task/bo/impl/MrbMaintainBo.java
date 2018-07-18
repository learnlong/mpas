package com.rskytech.task.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.bo.IMrbMaintainBo;
import com.rskytech.task.dao.IMrbMaintainDao;
import com.rskytech.task.dao.ITaskMsgDao;

public class MrbMaintainBo extends BaseBO implements IMrbMaintainBo {
	
	private IMrbMaintainDao mrbMaintainDao;
	private IComAreaBo comAreaBo;
	private ITaskMsgDao taskMsgDao;

	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMrbList(String msId, String sourceSystem, String taskType, String mrbCode, Page page){
		List<TaskMrb> list = mrbMaintainDao.getTaskMrbList(msId, sourceSystem, taskType, mrbCode, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMrb tm : list) {
				HashMap hm = new HashMap();
				
				hm.put("mrbId", tm.getMrbId());
				hm.put("mrbCode", tm.getMrbCode());
				hm.put("sourceSystem", tm.getSourceSystem());
				hm.put("taskType", tm.getTaskType());
				hm.put("taskDesc", tm.getTaskDesc());
				hm.put("reachWay", tm.getReachWay());
				hm.put("taskIntervalOriginal", tm.getTaskIntervalOriginal());
				hm.put("ownArea", comAreaBo.getAreaCodeByAreaId(tm.getOwnArea()));
				hm.put("effectiveness", tm.getEffectiveness());
				
				List<TaskMsg> msgList = this.taskMsgDao.getTaskByMrbId(msId, tm.getMrbId());
				String msgTaskCodes = "";
				for (int i = 0; i < msgList.size(); i++) {
					if (i != msgList.size() - 1) {
						msgTaskCodes += msgList.get(i).getTaskCode() + " , ";
					} else {
						msgTaskCodes += msgList.get(i).getTaskCode();
					}
				}
				hm.put("msgTaskCode", msgTaskCodes);
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public boolean checkTaskMrbCode(String mrbId, String mrbCode, String modelId) {
		List<TaskMrb> list = mrbMaintainDao.getTaskMrbList(mrbId, mrbCode,modelId);
		return list.size() > 0 ? false : true;// size大于0表示已经存在相同的code
	}
	
	public void saveMrb(String userId, String jsonData){
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("mrbId");
			
			TaskMrb tm = (TaskMrb) this.loadById(TaskMrb.class, id);
			if (tm != null){
				tm.setMrbCode(jsonObject.getString("mrbCode"));
				this.saveOrUpdate(tm, ComacConstants.DB_UPDATE, userId);			
			}
		}	
	}

	public IMrbMaintainDao getMrbMaintainDao() {
		return mrbMaintainDao;
	}

	public void setMrbMaintainDao(IMrbMaintainDao mrbMaintainDao) {
		this.mrbMaintainDao = mrbMaintainDao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}
	
}
