package com.rskytech.area.action;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa41Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za41;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za41Action extends BaseAction {

	private static final long serialVersionUID = 8946320362068526991L;

	public static final String ZA41 = "ZA41";
	
	private IZa41Bo za41Bo;
	private IZaStepBo zaStepBo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private Za41 za41;
	
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init(){
		ComUser user =  getSysUser();
		area = (ComArea) za41Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za41Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA41);
		return SUCCESS;
	}
	
	public void loadZa41(){
		JSONObject json = new JSONObject();
		json.element("za41", za41Bo.loadZa41(zaId));
		writeToResponse(json.toString());
	}
	
	public void saveZa41(){
		ComUser user = getSysUser();
		JSONObject json = za41Bo.saveZa41(user.getUserId(), zaId, za41);
		writeToResponse(json.toString());
	}

	public IZa41Bo getZa41Bo() {
		return za41Bo;
	}

	public void setZa41Bo(IZa41Bo za41Bo) {
		this.za41Bo = za41Bo;
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

	public Za41 getZa41() {
		return za41;
	}

	public void setZa41(Za41 za41) {
		this.za41 = za41;
	}
	
}
