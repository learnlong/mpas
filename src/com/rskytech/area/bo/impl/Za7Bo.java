package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa7Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;
import com.rskytech.pojo.ZaMain;

public class Za7Bo extends BaseBO implements IZa7Bo {

	private IZaStepBo zaStepBo;
	private IZa7Dao za7Dao;
	
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa7List(String msId, String areaId){
		List<HashMap> listFV = new ArrayList<HashMap>();
		List<Object[]> allList = new ArrayList<Object[]>();
		
		List<Object[]> lhList = za7Dao.searchLhirfTask(msId, areaId);
		List<Object[]> structureList = za7Dao.searchStructureTask(msId, areaId);
		List<Object[]> sysList = za7Dao.searchSysTask(msId, areaId);
		
		if (lhList != null && lhList.size() != 0){
			allList.addAll(lhList);
		}
		
		if (structureList != null && structureList.size() != 0){
			allList.addAll(structureList);
		}
		
		if (sysList != null && sysList.size() != 0){
			allList.addAll(sysList);
		}
		
		if (allList != null){
			HashMap hm;
			for (int i = 0; i < allList.size(); i++){
				Object[] obj = allList.get(i);
				hm = new HashMap();
				hm.put("taskId", obj[0]);
				hm.put("sourceSystem", obj[1]);
				hm.put("areaCode", obj[2]);
				hm.put("areaName", obj[3]);
				hm.put("taskCode", obj[4]);
				hm.put("taskType", obj[5]);
				hm.put("taskInterval", obj[6]);
				hm.put("reachWay", obj[7]);
				hm.put("taskDesc", obj[8]);
				hm.put("whyTransfer", obj[9]);
				hm.put("hasAccept", obj[10]);
				hm.put("rejectResion", obj[11]);
				hm.put("destTask", obj[12]);
				
				if (obj[10] == null){
					hm.put("show", 2);//请选择
				} else if ("0".equals(obj[10].toString())){
					hm.put("show", 0);//否
				} else if ("1".equals(obj[10].toString())){
					if (obj[12] != null){
						TaskMsg tm = (TaskMsg) za7Dao.loadById(TaskMsg.class, obj[12].toString());
						hm.put("show", "是，合并到" + tm.getTaskCode());
					} else {
						hm.put("show", 3);//合并到任务已经不存在
					}
				}else if("2".equals(obj[10].toString())){
					hm.put("show", 4);//待定
				}
				listFV.add(hm);
			}
		}
		return listFV;
	}
	
	public void saveZa7(String areaId, Integer hasAccept, String taskId, String destTask, String taskIntervalMerge, String rejectResion){
		List<TaskMsgDetail> detailList = za7Dao.searchDestTaskDetail(taskId, areaId);
		if (detailList != null && detailList.size() == 1){//有且仅有一条记录
			TaskMsgDetail tmd = detailList.get(0);
			if (hasAccept == 1){//同意接受
				tmd.setHasAccept(hasAccept);
				tmd.setRejectReason(null);
				tmd.setDestTask(destTask);
				za7Dao.update(tmd);
				
				TaskMsg dest = (TaskMsg) this.loadById(TaskMsg.class, destTask);
				dest.setTaskIntervalMerge(taskIntervalMerge);
				za7Dao.update(dest); 
			} else if (hasAccept == 0){//不同意接受
				tmd.setHasAccept(hasAccept);
				tmd.setRejectReason(rejectResion);
				tmd.setDestTask(null);
				za7Dao.update(tmd);
			} else if (hasAccept == 2){//待定
				tmd.setHasAccept(hasAccept);
				tmd.setRejectReason(null);
				tmd.setDestTask(null);
				za7Dao.update(tmd);
			}
		}
	}
	
	public void changeTask(String userId, String msId, String areaId, String zaId, String taskId){
		Object[] obj = za7Dao.getAcceptResult(taskId);
		TaskMsg tm = (TaskMsg) this.loadById(TaskMsg.class, taskId);
		if (Integer.valueOf(obj[0].toString()) > 0){//有至少一条记录拒绝
			TaskMsgDetail tmd = za7Dao.getNoAcceptTaskMsgDetail(taskId);
			if (tmd != null){
				tm.setTaskValid(null);
				tm.setHasAccept(0);
				tm.setRejectReason(tmd.getRejectReason());
				tm.setDestTask(null);
			}
		} else if (Integer.valueOf(obj[1].toString()) > 0){//还有记录没有做选择或选择了待定
			tm.setTaskValid(null);
			tm.setHasAccept(null);
			tm.setRejectReason(null);
			tm.setDestTask(null);
		} else {//所有记录都选择了接受
			List<TaskMsgDetail> tmdList = za7Dao.searchDestTaskDetail(taskId, areaId);
			if (tmdList != null && tmdList.size() > 0){
				String str = "";
				for (TaskMsgDetail tmd : tmdList){
					str = str + "," + tmd.getDestTask();
				}
				str = str.substring(1);
				tm.setTaskValid(2);
				tm.setHasAccept(1);
				tm.setRejectReason(null);
				tm.setDestTask(str);
			}
			
			if (ComacConstants.STRUCTURE_CODE.equals(tm.getSourceSystem()) || ComacConstants.LHIRF_CODE.equals(tm.getSourceSystem())){
				if (tm.getMrbId() != null){
					TaskMrb mrb = (TaskMrb) this.loadById(TaskMrb.class, tm.getMrbId());
					this.delete(mrb);
					tm.setMrbId(null);
				}
				if (tm.getMpdId() != null){
					TaskMpd mpd = (TaskMpd) this.loadById(TaskMpd.class, tm.getMpdId());
					this.delete(mpd);
					tm.setMpdId(null);
				}
			}
		}
		za7Dao.update(tm);
		
	}
	
	public void cleanTaskInterval(String msId){
		za7Dao.cleanTaskInterval(msId);
	}
	
	public JSONObject updateZa7StepAndStatus(String userId, String msId, String zaId){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		Integer za7 = zaStepBo.updateZa7StepAndStatus(userId, msId, zaMain);
		JSONObject json = new JSONObject();
		json.element("za7", za7);
		json.element("success", true);
		return json;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
