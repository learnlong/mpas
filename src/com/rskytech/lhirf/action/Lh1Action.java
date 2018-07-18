package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.web.util.JavaScriptUtils;


import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh1Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh1;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
/**
 * L/hirf 中的 lh_1表的 Action
 * @author wangyueli
 * createdate 2012-08-22
 */
public class Lh1Action extends BaseAction {
	private static final long serialVersionUID = 1L;

	// 注入bo
	private ILhStepBo lhStepBo;
	private ILh1Bo lh1Bo;
	private ILhMainBo lhMainBo;
	// 参数
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private String hsiName;
	private String areaName;
	private ComArea comArea;
	private String lheff;//lhrif  中 适用性
	private String picContent;
	private String lh1Id;
	private String isMaintain;
	private LhMain lhHsi;
	/*
	 * 初始化加载LH1页面信息
	 */
	public String init() {
		this.pagename = "LH1";
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		this.lh1Bo.lh1Stepshow(hsiId, this.getSysUser(),getComModelSeries());
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		lhHsi = (LhMain) this.lhMainBo.loadById(LhMain.class, hsiId);
		
		if(ComacConstants.ANALYZE_COLOR_APPROVED.equals(lhHsi.getStatus())){
			//版本状态为 审批完成时只有查看权限
			isMaintain = ComacConstants.NO.toString();
		}
		
		if(lhHsi != null){
			this.hsiName = JavaScriptUtils.javaScriptEscape(lhHsi
					.getHsiName() == null ? "" : lhHsi.getHsiName());
			this.lheff = JavaScriptUtils.javaScriptEscape(lhHsi.
					getEffectiveness()== null ? this.getComModelSeries().getModelSeriesName():lhHsi.getEffectiveness());
		}else{
			this.lheff =this.getComModelSeries().getModelSeriesName();
		}
	
		comArea = (ComArea) lhStepBo.loadById(ComArea.class, lhHsi.getComArea().getAreaId());
		if(comArea != null ){
				this.areaName = JavaScriptUtils.javaScriptEscape(comArea
						.getAreaName() == null ? "" : comArea.getAreaName());
			
		}
		Lh1 lh1 = lh1Bo.getLh1ByHsiId(this.hsiId);
		if (lh1 != null) {
			this.lh1Id = lh1.getLh1Id();
			this.picContent = JavaScriptUtils.javaScriptEscape(lh1
					.getPicContent() == null ? "" : lh1.getPicContent());
		}
		return SUCCESS;
	}
	// 保存 lh1 画面数据
	public String saveLh1Records() {
		lh1Bo.saveLh1andStep(this.getSysUser(), hsiId, picContent,lheff );
		return null;
	}
	/**
	 * 获取同步加载 lh1的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLh1Records(){
		Lh1 lh1 = lh1Bo.getLh1ByHsiId(this.hsiId);
		List listJson = new ArrayList<HashMap>();
		HashMap jsonFeildList = null;
		JSONArray json = new JSONArray();
		jsonFeildList= new HashMap();
		if (lh1 != null) {
				if(lh1.getPicContent()!=null){
					jsonFeildList.put("cn",lh1.getPicContent());
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
	
	
	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}
	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}
	public ILh1Bo getLh1Bo() {
		return lh1Bo;
	}
	public void setLh1Bo(ILh1Bo lh1Bo) {
		this.lh1Bo = lh1Bo;
	}
	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}
	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
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
	public ComArea getComArea() {
		return comArea;
	}
	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}
	public String getLheff() {
		return lheff;
	}
	public void setLheff(String lheff) {
		this.lheff = lheff;
	}
	public String getPicContent() {
		return picContent;
	}
	public void setPicContent(String picContent) {
		this.picContent = picContent;
	}
	public String getLh1Id() {
		return lh1Id;
	}
	public void setLh1Id(String lh1Id) {
		this.lh1Id = lh1Id;
	}
	public String getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}
	public LhMain getLhHsi() {
		return lhHsi;
	}
	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}

}
