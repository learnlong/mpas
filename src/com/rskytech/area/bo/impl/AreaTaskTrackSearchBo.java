package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.area.bo.IAreaTaskTrackSearchBo;
import com.rskytech.area.dao.IAreaTaskTrackSearchDao;
import com.rskytech.area.dao.IZa8Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public class AreaTaskTrackSearchBo extends BaseBO implements IAreaTaskTrackSearchBo {

	private IAreaTaskTrackSearchDao areaTaskTrackSearchDao;
	private IZa8Dao za8Dao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMsgList(String msId, String taskCode, Page page){
		List<Object[]> list = areaTaskTrackSearchDao.getTaskMsgList(msId, taskCode, page);
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (Object[] obj : list) {
				HashMap hm = new HashMap();
				hm.put("taskId", obj[0]);
				hm.put("areaCode", obj[1]);
				hm.put("taskCode", obj[2]);
				hm.put("taskType", obj[3]);
				hm.put("taskInterval", obj[5] == null ? obj[4] : obj[5]);
				hm.put("reachWay", obj[6]);
				hm.put("taskDesc", obj[7]);
				
				if (obj[8] == null){
					hm.put("showGo", "进入维修大纲");
				} else if ("0".equals(obj[8].toString())){
					hm.put("showGo", "转移到ATA20章");
				} else if ("1".equals(obj[8].toString())){
					if (obj[9] != null){
						TaskMsg tm = (TaskMsg) this.loadById(TaskMsg.class, obj[9].toString());
						hm.put("showGo", "合并到" + tm.getTaskCode());
					} else {
						hm.put("showGo", "合并到标准任务");
					}
				}
				
				if (obj[10] != null && ("ZA5A".equals(obj[10].toString()) || "ZA5B".equals(obj[10].toString()))){
					List<TaskMsgDetail> taskList = this.za8Dao.getTaskMsgDetailList(obj[0].toString());// 查询被该任务合并的系统、结构、区域任务
					List<TaskMsg> zengqiangList = areaTaskTrackSearchDao.getTaskList(msId, obj[0].toString());// 查询被该任务合并的增强区域任务
					String code = "";
					for (TaskMsgDetail taskMsgDetail : taskList) {
						code += taskMsgDetail.getTaskMsg().getTaskCode() + "\r\n";
					}
					for (TaskMsg taskMsg : zengqiangList) {
						code += taskMsg.getTaskCode() + "\r\n";
					}
					if (!"".equals(code)){
						code = code.substring(0, code.length() - 2);
						hm.put("showDestTask", code);
					} else {
						hm.put("showDestTask", "无");
					}
				} else {
					hm.put("showDestTask", "N/A");
				}
				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}

	public IAreaTaskTrackSearchDao getAreaTaskTrackSearchDao() {
		return areaTaskTrackSearchDao;
	}

	public void setAreaTaskTrackSearchDao(
			IAreaTaskTrackSearchDao areaTaskTrackSearchDao) {
		this.areaTaskTrackSearchDao = areaTaskTrackSearchDao;
	}

	public IZa8Dao getZa8Dao() {
		return za8Dao;
	}

	public void setZa8Dao(IZa8Dao za8Dao) {
		this.za8Dao = za8Dao;
	}
	
}
