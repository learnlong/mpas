package com.rskytech.area.bo.impl;

import net.sf.json.JSONObject;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa2Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa2Dao;
import com.rskytech.pojo.Za2;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.bo.ITaskMsgBo;

public class Za2Bo extends BaseBO implements IZa2Bo {

	private IZa2Dao za2Dao;
	private IZaStepBo zaStepBo;
	private ITaskMsgBo taskMsgBo;
	
	public Za2 getZa2ByZaId(String za2Id){
		return za2Dao.getZa2ByZaId(za2Id);
	}
	
	public JSONObject saveZa2(String userId, String zaId, String za2Id, Integer position, String picContent){		
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		
		JSONObject json = new JSONObject();
		try {
			if (za2Id == null || "".equals(za2Id)){//新建
				Za2 za2 = new Za2();
				za2.setZaMain(zaMain);
				za2.setPosition(position);
				za2.setPicContent(picContent);
				this.saveOrUpdate(za2, ComacConstants.DB_INSERT, userId);
			} else {
				Za2 za2 = (Za2) this.loadById(Za2.class, za2Id);
				za2.setPosition(position);
				za2.setPicContent(picContent);
				this.saveOrUpdate(za2, ComacConstants.DB_UPDATE, userId);
			}
			
			za2Dao.deleteAreaAnalysis(zaId, position);
			if (position == 1){//内部，则删除外部任务
				taskMsgBo.deleteAreaTask(zaMain.getComModelSeries().getModelSeriesId(), zaId, "ZA5B");
			} else if (position == 2){//外部，则删除内部任务
				taskMsgBo.deleteAreaTask(zaMain.getComModelSeries().getModelSeriesId(), zaId, "ZA5A");
			}
			Integer nextStep = zaStepBo.updateZa2StepAndStatus(userId, zaId, position);
			json.put("nextStep", nextStep);
			json.put("success", true);
		} catch(Exception e){
			json.put("success", false);
			e.printStackTrace();
		}
		return json;
	}

	public IZa2Dao getZa2Dao() {
		return za2Dao;
	}

	public void setZa2Dao(IZa2Dao za2Dao) {
		this.za2Dao = za2Dao;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}
	
}
