package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa6Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa6Dao;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.dao.ITaskMsgDao;

public class Za6Bo extends BaseBO implements IZa6Bo {

	private ITaskMsgDao taskMsgDao;
	private IZaStepBo zaStepBo;
	private IZa6Dao za6Dao;
	private IZa7Dao za7Dao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa6List(String msId, String zaId, String areaId){
		List<HashMap> listFV = new ArrayList<HashMap>();
		List<TaskMsg> list = taskMsgDao.findOneAnaAllTask(msId, ComacConstants.ZONAL_CODE, zaId);
		if (list != null){
			HashMap hm;
			for (TaskMsg tm : list){
				hm = new HashMap();
				hm.put("taskId", tm.getTaskId());
				hm.put("taskValid", tm.getTaskValid());
				hm.put("taskCode", tm.getTaskCode());
				hm.put("taskType", tm.getTaskType());
				hm.put("reachWay", tm.getReachWay());
				hm.put("taskDesc", tm.getTaskDesc2());
				hm.put("taskInterval", tm.getTaskInterval());
				
				hm.put("destTask", tm.getDestTask() == null ? "" : tm.getDestTask());
				
				if ("ZA5A".equals(tm.getSourceStep()) || "ZA5B".equals(tm.getSourceStep())){//标准任务
					hm.put("taskIntervalMerge", tm.getTaskIntervalMerge());
					hm.put("transfer", "N/A");  //N/A
				} else {//增强任务
					hm.put("taskIntervalMerge", "N/A");
					if ("GVI".equals(tm.getTaskType())){//GVI任务
						if ("noAnalysis".equals(tm.getAnyContent2())){//还没有做za6分析
							hm.put("transfer", "pleaseSelect"); //请选择
						} else if (tm.getTaskValid() == 1){//被合并
							if (tm.getDestTask() != null){
								TaskMsg tempTm = (TaskMsg) taskMsgDao.loadById(TaskMsg.class, tm.getDestTask());
								if (tempTm == null){
									hm.put("transfer", "合并任务已经不存在");
								} else {
									hm.put("transfer", "合并到" + tempTm.getTaskCode());
								}
							} else {
								hm.put("transfer", "合并任务已经不存在");
							}
						}
					} else {//非GVI任务
						if ("noAnalysis".equals(tm.getAnyContent2())){//还没有做za6分析
							hm.put("transfer", "pleaseSelect"); //请选择
						} else if (tm.getTaskValid() == null){
							hm.put("transfer", "toMpd");//进入维修大纲
						} else if (tm.getTaskValid() == 0){
							hm.put("transfer", "transferToAta20");//转移ATA20
						}
					}
				}
				listFV.add(hm);
			}
		}
		return listFV;
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getStandardTaskList(String msId, String zaId){
		List<HashMap> listFV = new ArrayList<HashMap>();
		List<TaskMsg> list = taskMsgDao.getAreaStandardTask(msId, zaId);
		if (list != null){
			HashMap hm;
			for (TaskMsg tm : list){
				hm = new HashMap();
				hm.put("taskId", tm.getTaskId());
				hm.put("taskCode", tm.getTaskCode());
				hm.put("taskInterval", tm.getTaskInterval());
				hm.put("taskIntervalMerge", tm.getTaskIntervalMerge());
				hm.put("taskIntervalRepeat", tm.getTaskIntervalRepeat());
				listFV.add(hm);
			}
		}
		return listFV;
	}
	
	public JSONObject saveZa6(String userId, String msId, String zaId, String doSelect, String taskId, String destTask, String taskIntervalMerge){
		TaskMsg tm = (TaskMsg) this.loadById(TaskMsg.class, taskId);
		
		if ("1".equals(doSelect)){//转移到ATA20
			tm.setTaskValid(0);
			tm.setAnyContent2(null);
			tm.setDestTask(null);
		} else if ("3".equals(doSelect)){//进入维修大纲
			tm.setTaskValid(null);
			tm.setAnyContent2(null);
			tm.setDestTask(null);
		} else if ("2".equals(doSelect)){//合并到标准任务
			tm.setTaskValid(1);
			tm.setAnyContent2(null);
			tm.setDestTask(destTask);
			
			TaskMsg sTm = (TaskMsg) this.loadById(TaskMsg.class, destTask);
			sTm.setTaskIntervalMerge(taskIntervalMerge);
			this.saveOrUpdate(sTm, ComacConstants.DB_UPDATE, userId);
		}
		this.saveOrUpdate(tm, ComacConstants.DB_UPDATE, userId);
		
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		Integer za7 = zaStepBo.updateZa6StepAndStatus(userId, msId, zaMain);
		JSONObject json = new JSONObject();
		json.element("za7", za7);
		json.element("success", true);
		return json;
	}
	
	public void cleanTaskInterval(String msId){
		za7Dao.cleanTaskInterval(msId);
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa6Dao getZa6Dao() {
		return za6Dao;
	}

	public void setZa6Dao(IZa6Dao za6Dao) {
		this.za6Dao = za6Dao;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
