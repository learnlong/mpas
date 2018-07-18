package com.rskytech.area.action;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa2Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za2;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za2Action extends BaseAction {

	private static final long serialVersionUID = -8154537391971254932L;
	
	public static final String ZA2 = "ZA2";

	private IZa2Bo za2Bo;
	private IZaStepBo zaStepBo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private String za2Id;
	private Integer position;
	private String picContent;
	
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init(){
		ComUser user =  getSysUser();
		area = (ComArea) za2Bo.loadById(ComArea.class, areaId);
		
		Za2 za2 = za2Bo.getZa2ByZaId(zaId);
		if (za2 == null){
			za2Id = null;
			position = null;
		} else {
			za2Id = za2.getZa2Id();
			position = za2.getPosition();
		}
		
		ZaMain zaMain = (ZaMain) za2Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA2);
		return SUCCESS;
	}
	
	public String loadZa2PicContent(){
		Za2 za2 = (Za2) this.za2Bo.loadById(Za2.class, this.za2Id);
		JSONObject json = new JSONObject();
		json.put("picContent", za2 == null ? null : za2.getPicContent());
		this.writeToResponse(json.toString());		
		return null;
	}
	
	public String save(){
		ComUser user = getSysUser();
		JSONObject json = za2Bo.saveZa2(user.getUserId(), zaId, za2Id, position, picContent);
		this.writeToResponse(json.toString());
		return null;
	}

	public IZa2Bo getZa2Bo() {
		return za2Bo;
	}

	public void setZa2Bo(IZa2Bo za2Bo) {
		this.za2Bo = za2Bo;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public String getZaId() {
		return zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
	}

	public String getZa2Id() {
		return za2Id;
	}

	public void setZa2Id(String za2Id) {
		this.za2Id = za2Id;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
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

	public String getPicContent() {
		return picContent;
	}

	public void setPicContent(String picContent) {
		this.picContent = picContent;
	}
	
}
