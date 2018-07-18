package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.web.util.JavaScriptUtils;


import com.richong.arch.action.BaseAction;
import com.rskytech.lhirf.bo.ILh2Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh2;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

public class Lh2Action extends BaseAction {
	/**
	 * L/hirf 中的 lh_2表的 Action
	 */

	private static final long serialVersionUID = 1L;
	//注入
	private ILhStepBo lhStepBo;
	private ILh2Bo lh2Bo ;
	//参数
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private String lh2Id;
	private Lh2 lh2;
	private ComArea comArea;
	private Integer areaId;
	private String env;
	private String picContent;
	private String isMaintain;
	private String hsiName;
	private String areaName;
	private LhMain lhHsi;
	
	/*
	 * 初始化加载LH2页面信息
	 */
	public String init(){
		
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		this.pagename = "LH2";
		lh2 = lh2Bo.getLh2ByHsiId(hsiId);
		if (lh2 != null) {
			this.env = JavaScriptUtils
					.javaScriptEscape(lh2.getEnv() == null ? "" : lh2
							.getEnv());
			this.picContent = JavaScriptUtils.javaScriptEscape(lh2
					.getPicContent() == null ? "" : lh2.getPicContent());
		}
		lhHsi = (LhMain) this.lh2Bo.loadById(LhMain.class, hsiId);
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		comArea = (ComArea) lh2Bo.loadById(ComArea.class, this.lhHsi.getComArea().getAreaId());
		if(lhHsi != null){
				this.hsiName = JavaScriptUtils.javaScriptEscape(lhHsi
						.getHsiName() == null ? "" : lhHsi.getHsiName());
		}
		if(comArea != null ){
				this.areaName = JavaScriptUtils.javaScriptEscape(comArea
						.getAreaName() == null ? "" : comArea.getAreaName());
		}
		return SUCCESS;
	}
	/**
	 * 获取同步加载 lh2的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLh2Records(){
		Lh2 lh2 = lh2Bo.getLh2ByHsiId(hsiId);
		List listJson = new ArrayList<HashMap>();
		HashMap jsonFeildList = null;
		JSONArray json = new JSONArray();
		jsonFeildList= new HashMap();
		if (lh2 != null) {
			if(lh2.getPicContent()!=null){
				jsonFeildList.put("cn",lh2.getPicContent());
			}else{
				jsonFeildList.put("cn","");
			}
			listJson.add(jsonFeildList);
			json.addAll(listJson);
		}else{
			jsonFeildList.put("cn","");
			listJson.add(jsonFeildList);
			json.addAll(listJson);
		}
		writeToResponse(json.toString());
		return null;
	}
	
	
	/*
	 * 初始化加载LH2页面信息
	 * @author wangyueli
	 * createdate 2012-08-25
	 */
	public String saveLh2(){
	   
	   lh2Bo.saveLh2andStep(this.getSysUser(), hsiId, env, picContent);
		return null;
	}
	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}
	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}
	public ILh2Bo getLh2Bo() {
		return lh2Bo;
	}
	public void setLh2Bo(ILh2Bo lh2Bo) {
		this.lh2Bo = lh2Bo;
	}
	public LhStep getLhStep() {
		return lhStep;
	}
	public void setLhStep(LhStep lhStep) {
		this.lhStep = lhStep;
	}
	public String getHsiId() {
		return hsiId;
	}
	public void setHsiId(String hsiId) {
		this.hsiId = hsiId;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public String getLh2Id() {
		return lh2Id;
	}
	public void setLh2Id(String lh2Id) {
		this.lh2Id = lh2Id;
	}
	public Lh2 getLh2() {
		return lh2;
	}
	public void setLh2(Lh2 lh2) {
		this.lh2 = lh2;
	}
	public ComArea getComArea() {
		return comArea;
	}
	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getPicContent() {
		return picContent;
	}
	public void setPicContent(String picContent) {
		this.picContent = picContent;
	}
	public String getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}
	public String getHsiName() {
		return hsiName;
	}
	public void setHsiName(String hsiName) {
		this.hsiName = hsiName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public LhMain getLhHsi() {
		return lhHsi;
	}
	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}
}
	
