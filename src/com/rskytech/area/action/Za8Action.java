package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa8Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za8Action extends BaseAction {

	private static final long serialVersionUID = 2642682869943420460L;
	
	public static final String ZA8 = "ZA8";
	
	private IZaStepBo zaStepBo;
	private IZa8Bo za8Bo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za8Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za8Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA8);
        return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getZa8List(){
		JSONObject json = new JSONObject();
		List<HashMap> listFV = za8Bo.getZa8List(getComModelSeries().getModelSeriesId(), zaId);
		json.element("tasks", listFV);
		writeToResponse(json.toString());
	}
	
	public void saveZa8(){
		za8Bo.saveZa8(getSysUser().getUserId(), getComModelSeries().getModelSeriesId(), jsonData);
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa8Bo getZa8Bo() {
		return za8Bo;
	}

	public void setZa8Bo(IZa8Bo za8Bo) {
		this.za8Bo = za8Bo;
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
