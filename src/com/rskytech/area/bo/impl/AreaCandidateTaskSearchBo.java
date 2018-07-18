package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.area.bo.IAreaCandidateTaskSearchBo;
import com.rskytech.area.dao.IAreaCandidateTaskSearchDao;
import com.rskytech.pojo.TaskMsg;

public class AreaCandidateTaskSearchBo extends BaseBO implements IAreaCandidateTaskSearchBo {

	private IAreaCandidateTaskSearchDao areaCandidateTaskSearchDao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page){
		List<TaskMsg> list = areaCandidateTaskSearchDao.getTaskMsgList(msId, sourceSystem, taskType, taskCode, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMsg tm : list) {
				HashMap hm = new HashMap();
				hm.put("taskId", tm.getTaskId());
				hm.put("sourceSystem", tm.getSourceSystem());
				hm.put("taskCode", tm.getTaskCode());
				hm.put("taskType", tm.getTaskType());
				hm.put("taskDesc", tm.getTaskDesc());
				hm.put("reachWay", tm.getReachWay());
				hm.put("taskInterval", tm.getTaskInterval());
				
				String showDestTask = "";
				String[] destTaskArray;
				if (tm.getDestTask() != null){
					destTaskArray = tm.getDestTask().split(",");
					
					if (destTaskArray.length > 0){
						for (String s : destTaskArray){
							TaskMsg destTask = (TaskMsg) this.loadById(TaskMsg.class, s);
							showDestTask = showDestTask + destTask.getTaskCode() + "\r\n";
						}
					}
					
					if (!"".equals(showDestTask)){
						showDestTask = showDestTask.substring(0, showDestTask.length() - 2);
					}
				}
				hm.put("showDestTask", showDestTask);
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}

	public IAreaCandidateTaskSearchDao getAreaCandidateTaskSearchDao() {
		return areaCandidateTaskSearchDao;
	}

	public void setAreaCandidateTaskSearchDao(
			IAreaCandidateTaskSearchDao areaCandidateTaskSearchDao) {
		this.areaCandidateTaskSearchDao = areaCandidateTaskSearchDao;
	}
	
}
