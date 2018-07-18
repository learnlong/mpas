package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa42Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.area.dao.IZa42Dao;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.Za42;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.dao.ITaskMsgDao;

public class Za42Bo extends BaseBO implements IZa42Bo {

	private IZa1Dao za1Dao;
	private IZa42Dao za42Dao;
	private IZa43Dao za43Dao;
	private ITaskMsgDao taskMsgDao;
	private IZaStepBo zaStepBo;
	
	public String createZa42(String userId, String zaId){
		Za42 za42 = za42Dao.getZa42ByZaId(zaId);
		if (za42 == null){
			za42 = new Za42();
			ZaMain zaMain = new ZaMain();
			zaMain.setZaId(zaId);
			za42.setZaMain(zaMain);
			this.save(za42, userId);
		}
		return za42.getZa42Id();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap loadZa42(String za42Id){
		Za42 za42 = (Za42) this.loadById(Za42.class, za42Id);
		if (za42 != null) {
			HashMap hm = new HashMap();
			hm.put("za42Id", za42.getZa42Id());
			hm.put("select1", za42.getSelect1() == null ? 0 : za42.getSelect1());
			hm.put("select2", za42.getSelect2() == null ? 0 : za42.getSelect2());
			hm.put("select3", za42.getSelect3() == null ? 0 : za42.getSelect3());
			hm.put("result", za42.getResult() == null ? 0 : za42.getResult());
			hm.put("step6Desc", za42.getStep6Desc());
			hm.put("step7Desc", za42.getStep7Desc());
			return hm;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap> loadTaskMsgList(String msId, String zaId){
		List<TaskMsg> list = taskMsgDao.findAreaTaskMsg(msId, zaId, "ZA_4_2", "7");
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMsg tm : list) {
				HashMap hm = new HashMap();
				hm.put("taskId", tm.getTaskId());
				hm.put("taskType", tm.getTaskType());
				hm.put("taskDesc", tm.getTaskDesc());				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public JSONObject saveZa42(String userId, ComModelSeries ms, String zaId, Za42 za42, String jsonData, String storeRemoveId){
		Integer taskStatus = 0;//任务状态，1新增，2删除，0没变（删除和新增都有时，当新增处理）
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		Za1 za1 = za1Dao.getZa1ByZaId(zaId);
		
		JSONObject json = new JSONObject();
		try {
			za42.setZaMain(zaMain);
			if (za42.getResult() == 3){
				za42.setStep7Desc(null);
			}
			this.saveOrUpdate(za42, ComacConstants.DB_UPDATE, userId);
			
			//以下处理问题6自动生成的GVI任务，本任务只要做ZA42，那么就一定存在
			List<TaskMsg> sixList = taskMsgDao.findAreaTaskMsg(ms.getModelSeriesId(), zaId, "ZA_4_2", "6");
			if (sixList == null || sixList.size() == 0){
				TaskMsg tm = new TaskMsg();
				tm.setComModelSeries(ms);
				tm.setSourceSystem(ComacConstants.ZONAL_CODE);
				tm.setSourceAnaId(zaId);
				tm.setSourceStep("ZA_4_2");
				tm.setTaskType("GVI");
				tm.setReachWay(za1.getReachWay());
				tm.setOwnArea(zaMain.getComArea().getAreaId());//所属区域
				tm.setEffectiveness(zaMain.getEffectiveness());
				tm.setAnyContent1("6");// 问题6产生
				tm.setAnyContent2("noAnalysis");// 任务还没有被ZA6分析
				tm.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.saveOrUpdate(tm, ComacConstants.DB_INSERT, userId);
				taskStatus = 1;
			}
			
			//以下处理问题7生成的任务
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
			
			//有已保存的任务被删除
			if (!"".equals(storeRemoveId)){
				storeRemoveId = storeRemoveId.substring(0, storeRemoveId.length() - 1);//去除字段最后的分号
				String[] removeId = storeRemoveId.split(";");
				if (removeId.length > 0){
					for (int i = 0; i < removeId.length; i++){
						za43Dao.deleteZa43(zaId, removeId[i]);
						taskMsgDao.deleteTasksByTaskId(removeId[i]);
						if (taskStatus == 0){//没有新增GVI任务，如果有新增，当新增处理
							taskStatus = 2;
						}
					}
				}
			}
			
			//评级是3级的话，删除所有问题7的任务	
			if (za42.getResult() == 3){
				List<TaskMsg> sevenList = taskMsgDao.findAreaTaskMsg(ms.getModelSeriesId(), zaId, "ZA_4_2", "7");
				if (sevenList != null){
					for (TaskMsg tm : sevenList) {											
						za43Dao.deleteZa43(zaId, tm.getTaskId());
						taskMsgDao.deleteTasksByTaskId(tm.getTaskId());
						if (taskStatus == 0){//没有新增GVI任务，如果有新增，当新增处理
							taskStatus = 2;
						}
					}
				}
			}
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("taskId");
				
				TaskMsg tm = new TaskMsg();
				//修改操作
				if (!BasicTypeUtils.isNullorBlank(id)) {
					tm = (TaskMsg) taskMsgDao.loadById(TaskMsg.class, id);
					tm.setTaskType(jsonObject.getString("taskType"));
					tm.setTaskDesc(jsonObject.getString("taskDesc"));
					this.saveOrUpdate(tm, ComacConstants.DB_UPDATE, userId);																								
				} else {// 追加操作
					tm.setComModelSeries(ms);
					tm.setSourceSystem(ComacConstants.ZONAL_CODE);
					tm.setSourceAnaId(zaId);
					tm.setSourceStep("ZA_4_2");
					tm.setTaskType(jsonObject.getString("taskType"));
					tm.setTaskDesc(jsonObject.getString("taskDesc"));
					tm.setReachWay(za1.getReachWay());
					tm.setOwnArea(zaMain.getComArea().getAreaId());//所属区域
					tm.setEffectiveness(zaMain.getEffectiveness());
					tm.setAnyContent1("7");// 问题7产生
					tm.setAnyContent2("noAnalysis");// 任务还没有被ZA6分析
					tm.setValidFlag(ComacConstants.VALIDFLAG_YES);
					this.saveOrUpdate(tm, ComacConstants.DB_INSERT, userId);
					taskStatus = 1;
				}
			}	
			
			//处理分析步骤和状态
			Integer nextStep = zaStepBo.updateZa42StepAndStatus(userId, zaId, taskStatus);
			json.put("nextStep", nextStep);
			json.put("success", true);
		} catch(Exception e){
			json.put("success", false);
			e.printStackTrace();
		}
		return json;
	}

	public IZa42Dao getZa42Dao() {
		return za42Dao;
	}

	public void setZa42Dao(IZa42Dao za42Dao) {
		this.za42Dao = za42Dao;
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

	public IZa43Dao getZa43Dao() {
		return za43Dao;
	}

	public void setZa43Dao(IZa43Dao za43Dao) {
		this.za43Dao = za43Dao;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}
	
}
