package com.rskytech.area.action;

import net.sf.json.JSONObject;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa1Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za1Action extends BaseAction {

	private static final long serialVersionUID = 5683712514707032453L;

	public static final String ZA1 = "ZA1";
	
	private IZa1Bo za1Bo;
	private IZaStepBo zaStepBo;
	
	private String areaId;
	private String zaId;
	private ComArea area;
	private Za1 za1;
	private ZaStep zaStep;
	private String effectiveness;
	private String analysisType;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	public String init(){
		ComUser user =  getSysUser();
		ComModelSeries ms = getComModelSeries();
		area = (ComArea) za1Bo.loadById(ComArea.class, areaId);
		
		//查询区域主表，或插入新的主表记录
		ZaMain zaMain = null;
		if (isMaintain == 1){
			zaMain = za1Bo.selectZaMain(user, ms, area);
		} else {
			zaMain = za1Bo.selectZaMain(ms, area);
			if (zaMain == null){
				return "noAnalyse";
			}
		}
		
		zaId = zaMain.getZaId();
		effectiveness = JavaScriptUtils.javaScriptEscape(zaMain.getEffectiveness());
		
		za1 = za1Bo.selectZa1(zaMain.getZaId(), area);
		analysisType = za1Bo.getAreaAnalysisType(za1);
		
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA1);
		return SUCCESS;
	}
	
	public String save(){
		ComUser user = getSysUser();
		JSONObject json = za1Bo.saveZa1(user.getUserId(), za1, analysisType, effectiveness);
		this.writeToResponse(json.toString());
		return null;
	}

	public IZa1Bo getZa1Bo() {
		return za1Bo;
	}

	public void setZa1Bo(IZa1Bo za1Bo) {
		this.za1Bo = za1Bo;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public Za1 getZa1() {
		return za1;
	}

	public void setZa1(Za1 za1) {
		this.za1 = za1;
	}

	public String getEffectiveness() {
		return effectiveness;
	}

	public void setEffectiveness(String effectiveness) {
		this.effectiveness = effectiveness;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public ComArea getArea() {
		return area;
	}

	public void setArea(ComArea area) {
		this.area = area;
	}

	public ZaStep getZaStep() {
		return zaStep;
	}

	public void setZaStep(ZaStep zaStep) {
		this.zaStep = zaStep;
	}

	public String getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	public String getZaId() {
		return zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
	}
	
}
