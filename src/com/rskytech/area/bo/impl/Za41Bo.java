package com.rskytech.area.bo.impl;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa41Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.area.dao.IZa41Dao;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.Za41;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.dao.ITaskMsgDao;

public class Za41Bo extends BaseBO implements IZa41Bo {
	
	private IZa1Dao za1Dao;
	private IZa41Dao za41Dao;
	private IZa43Dao za43Dao;
	private ITaskMsgDao taskMsgDao;
	private IZaStepBo zaStepBo;
	
	@SuppressWarnings("unchecked")
	public HashMap loadZa41(String zaId){
		Za41 za41 = za41Dao.getZa41ByZaId(zaId);
		if (za41 != null) {
			HashMap hm = new HashMap();
			hm.put("za41Id", za41.getZa41Id());
			hm.put("step1", za41.getStep1());
			hm.put("step1Desc", za41.getStep1Desc());
			hm.put("step2", za41.getStep2());
			hm.put("step2Desc", za41.getStep2Desc());
			hm.put("step3", za41.getStep3());
			hm.put("step3Desc", za41.getStep3Desc());
			hm.put("step4", za41.getStep4());
			hm.put("step4Desc", za41.getStep4Desc());
			hm.put("step5", za41.getStep5());
			hm.put("step5Desc", za41.getStep5Desc());
			return hm;
		}
		return null;
	}
	
	public JSONObject saveZa41(String userId, String zaId, Za41 za41){
		Integer rstTask = 0;//RST任务状态，1新增，2删除，0没变
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		
		JSONObject json = new JSONObject();
		try {
			if (za41.getZa41Id() == null || "".equals(za41.getZa41Id())){//新建
				za41.setZa41Id(null);
				za41.setZaMain(zaMain);				
				this.saveOrUpdate(za41, ComacConstants.DB_INSERT, userId);
			} else {
				za41.setZaMain(zaMain);
				this.saveOrUpdate(za41, ComacConstants.DB_UPDATE, userId);
			}
			
			if (za41.getStep4() == 1){//第四个问题回答“是”，则生成RST任务
				List<TaskMsg> list = taskMsgDao.findAreaTaskMsg(zaMain.getComModelSeries().getModelSeriesId(), zaId, "ZA_4_1", "4");
				if (list == null || list.size() == 0){
					Za1 za1 = za1Dao.getZa1ByZaId(zaId);
					
					TaskMsg taskMsg = new TaskMsg();
					taskMsg.setComModelSeries(zaMain.getComModelSeries());
					taskMsg.setSourceSystem(ComacConstants.ZONAL_CODE);
					taskMsg.setSourceAnaId(zaId);
					taskMsg.setSourceStep("ZA_4_1");
					taskMsg.setTaskType("RST");
					taskMsg.setReachWay(za1.getReachWay());
					taskMsg.setOwnArea(zaMain.getComArea().getAreaId());//所属区域
					taskMsg.setEffectiveness(zaMain.getEffectiveness());
					taskMsg.setAnyContent1("4");// 问题4产生
					taskMsg.setAnyContent2("noAnalysis");// 任务还没有被ZA6分析
					taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);
					this.saveOrUpdate(taskMsg, ComacConstants.DB_INSERT, userId);
					rstTask = 1;
				}
			} else {//第四个问题回答“否”或“无效”，则考虑删除已经生成的RST任务
				List<TaskMsg> list = taskMsgDao.findAreaTaskMsg(zaMain.getComModelSeries().getModelSeriesId(), zaId, "ZA_4_1", "4");
				if (list != null){
					for (TaskMsg tm : list){
						za43Dao.deleteZa43(zaId, tm.getTaskId());
						taskMsgDao.deleteTasksByTaskId(tm.getTaskId());
						rstTask = 2;
					}
				}
			}
			
			//处理分析步骤和状态
			Integer nextStep = zaStepBo.updateZa41StepAndStatus(userId, zaId, rstTask);
			json.put("nextStep", nextStep);
			json.put("success", true);
		} catch(Exception e){
			json.put("success", false);
			e.printStackTrace();
		}
		return json;
	}

	public IZa41Dao getZa41Dao() {
		return za41Dao;
	}

	public void setZa41Dao(IZa41Dao za41Dao) {
		this.za41Dao = za41Dao;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}

	public IZa43Dao getZa43Dao() {
		return za43Dao;
	}

	public void setZa43Dao(IZa43Dao za43Dao) {
		this.za43Dao = za43Dao;
	}
	
}
