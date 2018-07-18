package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa7Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za7Action extends BaseAction {

	private static final long serialVersionUID = 4313514923993806168L;

	public static final String ZA7 = "ZA7";
	
	private IZaStepBo zaStepBo;
	private IZa7Bo za7Bo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	private String hasAccept;
	private String taskId;
	private String destTask;
	private String taskIntervalMerge;
	private String rejectResion;
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za7Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za7Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA7);
        return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getZa7List(){
		JSONObject json = new JSONObject();
		List<HashMap> listFV = za7Bo.getZa7List(getComModelSeries().getModelSeriesId(), areaId);
		json.element("tasks", listFV);
		writeToResponse(json.toString());
	}
	
	public void saveZa7(){
		za7Bo.saveZa7(areaId, Integer.valueOf(hasAccept), taskId, destTask, taskIntervalMerge, rejectResion);
		za7Bo.changeTask(getSysUser().getUserId(), getComModelSeries().getModelSeriesId(), areaId, zaId, taskId);
		za7Bo.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		JSONObject json = za7Bo.updateZa7StepAndStatus(getSysUser().getUserId(), getComModelSeries().getModelSeriesId(), zaId);
		writeToResponse(json.toString());
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa7Bo getZa7Bo() {
		return za7Bo;
	}

	public void setZa7Bo(IZa7Bo za7Bo) {
		this.za7Bo = za7Bo;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public ComArea getArea() {
		return area;
	}

	public void setArea(ComArea area) {
		this.area = area;
	}

	public String getZaId() {
		return zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
	}

	public ZaStep getZaStep() {
		return zaStep;
	}

	public void setZaStep(ZaStep zaStep) {
		this.zaStep = zaStep;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getHasAccept() {
		return hasAccept;
	}

	public void setHasAccept(String hasAccept) {
		this.hasAccept = hasAccept;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getDestTask() {
		return destTask;
	}

	public void setDestTask(String destTask) {
		this.destTask = destTask;
	}

	public String getRejectResion() {
		return rejectResion;
	}

	public void setRejectResion(String rejectResion) {
		this.rejectResion = rejectResion;
	}

	public String getTaskIntervalMerge() {
		return taskIntervalMerge;
	}

	public void setTaskIntervalMerge(String taskIntervalMerge) {
		this.taskIntervalMerge = taskIntervalMerge;
	}
	
}
