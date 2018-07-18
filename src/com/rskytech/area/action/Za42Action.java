package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa42Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za42;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za42Action extends BaseAction {

	private static final long serialVersionUID = -1289564177977056288L;
	
	public static final String ZA42 = "ZA42";
	
	private IZa42Bo za42Bo;
	private IZaStepBo zaStepBo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private String za42Id;
	private Za42 za42;
	private String storeRemoveId;
	
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init(){
		ComUser user =  getSysUser();
		area = (ComArea) za42Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za42Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA42);
		
		za42Id = za42Bo.createZa42(user.getUserId(), zaId);
		return SUCCESS;
	}
	
	public void loadZa42(){
		JSONObject json = new JSONObject();
		json.element("za42", za42Bo.loadZa42(za42Id));
		writeToResponse(json.toString());
	}
	
	@SuppressWarnings("unchecked")
	public void loadTaskMsg(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = za42Bo.loadTaskMsgList(msId, zaId);
		
		JSONObject json = new JSONObject();
		json.element("taskMsgList", listJsonFV);
		writeToResponse(json.toString());
	}
	
	public void saveZa42(){
		ComUser user = getSysUser();
		JSONObject json = za42Bo.saveZa42(user.getUserId(), getComModelSeries(), zaId, za42, jsonData, storeRemoveId);
		writeToResponse(json.toString());
	}

	public IZa42Bo getZa42Bo() {
		return za42Bo;
	}

	public void setZa42Bo(IZa42Bo za42Bo) {
		this.za42Bo = za42Bo;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
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

	public String getZa42Id() {
		return za42Id;
	}

	public void setZa42Id(String za42Id) {
		this.za42Id = za42Id;
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

	public Za42 getZa42() {
		return za42;
	}

	public void setZa42(Za42 za42) {
		this.za42 = za42;
	}

	public String getStoreRemoveId() {
		return storeRemoveId;
	}

	public void setStoreRemoveId(String storeRemoveId) {
		this.storeRemoveId = storeRemoveId;
	}
	
}
