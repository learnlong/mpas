package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa6Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za6Action extends BaseAction {

	private static final long serialVersionUID = 3615470615727034828L;

	public static final String ZA6 = "ZA6";
	
	private IZaStepBo zaStepBo;
	private IZa6Bo za6Bo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	private String doSelect;
	private String taskIntervalMerge;
	private String taskId;
	private String destTask;
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za6Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za6Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA6);
        return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getZa6List(){
		JSONObject json = new JSONObject();
		List<HashMap> listFV = za6Bo.getZa6List(getComModelSeries().getModelSeriesId(), zaId, areaId);
		json.element("tasks", listFV);
		writeToResponse(json.toString());
	}
	
	@SuppressWarnings("unchecked")
	public void getStandardTaskList(){
		JSONObject json = new JSONObject();
		List<HashMap> listFV = za6Bo.getStandardTaskList(getComModelSeries().getModelSeriesId(), zaId);
		json.element("taskStore", listFV);
		writeToResponse(json.toString());
	}
	
	public void saveZa6(){
		JSONObject json = za6Bo.saveZa6(getSysUser().getUserId(), getComModelSeries().getModelSeriesId(), zaId, doSelect, taskId, destTask, taskIntervalMerge);
		za6Bo.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		writeToResponse(json.toString());
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa6Bo getZa6Bo() {
		return za6Bo;
	}

	public void setZa6Bo(IZa6Bo za6Bo) {
		this.za6Bo = za6Bo;
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

	public String getDoSelect() {
		return doSelect;
	}

	public void setDoSelect(String doSelect) {
		this.doSelect = doSelect;
	}

	public String getTaskIntervalMerge() {
		return taskIntervalMerge;
	}

	public void setTaskIntervalMerge(String taskIntervalMerge) {
		this.taskIntervalMerge = taskIntervalMerge;
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
	
}
