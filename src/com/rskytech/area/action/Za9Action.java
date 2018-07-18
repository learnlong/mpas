package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa9Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za9Action extends BaseAction {

	private static final long serialVersionUID = 8199593385829269160L;

	public static final String ZA9 = "ZA9";
	
	private IZaStepBo zaStepBo;
	private IZa9Bo za9Bo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za9Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za9Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA9);
        return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getZa9List(){
		JSONObject json = new JSONObject();
		List<HashMap> listFV = za9Bo.getZa9List(getComModelSeries().getModelSeriesId(), zaId);
		json.element("tasks", listFV);
		writeToResponse(json.toString());
	}
	
	public void saveZa9(){
		za9Bo.saveZa9(getSysUser().getUserId(), getComModelSeries().getModelSeriesId(), jsonData);
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa9Bo getZa9Bo() {
		return za9Bo;
	}

	public void setZa9Bo(IZa9Bo za9Bo) {
		this.za9Bo = za9Bo;
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
	
}
