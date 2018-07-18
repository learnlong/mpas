package com.rskytech.task.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.bo.IMsgSearchBo;
import com.rskytech.task.dao.IMsgSearchDao;

public class MsgSearchBo extends BaseBO implements IMsgSearchBo {

	private IMsgSearchDao msgSearchDao;
	private IComAreaBo comAreaBo;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page){
		List<TaskMsg> list = msgSearchDao.getTaskMsgList(msId, sourceSystem, taskType, taskCode, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMsg tm : list) {
				HashMap hm = new HashMap();
				
				hm.put("taskId", tm.getTaskId());
				hm.put("taskCode", tm.getTaskCode());
				hm.put("sourceSystem", tm.getSourceSystem());
				hm.put("taskType", tm.getTaskType());
				hm.put("taskDesc", tm.getTaskDesc());
				hm.put("reachWay", tm.getReachWay());
				hm.put("taskInterval", tm.getTaskIntervalMerge() == null ? tm.getTaskInterval() : tm.getTaskIntervalMerge());
				hm.put("ownArea", comAreaBo.getAreaCodeByAreaId(tm.getOwnArea()));
				hm.put("effectiveness", tm.getEffectiveness());
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}

	public IMsgSearchDao getMsgSearchDao() {
		return msgSearchDao;
	}

	public void setMsgSearchDao(IMsgSearchDao msgSearchDao) {
		this.msgSearchDao = msgSearchDao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	
}
