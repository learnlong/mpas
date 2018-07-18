package com.rskytech.lhirf.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh1aBo;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

public class Lh1aAction extends BaseAction {

	/**
	 * L/hirf 中的 lh_1A表的 Action
	 */

	private static final long serialVersionUID = 1L;
	//注入bo
	private ILhStepBo lhStepBo;
	private ILh1aBo  lh1aBo;
	private ILh4Bo  lh4Bo;
	
	//参数
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private ComArea comArea;
	private String picId;
	private Lh1a lh1a;
	private String content1;
	private String content2;
	private String content3;
	private String picContent;
	private String areaId;
	private String isneedRep;
	private String lheff;//lhrif  中 适用性
	private String isMaintain;
	private String hsiName;
	private String areaName;
	private ILhMainBo lhMainBo;
	private LhMain lhHsi;
	
	/*
	 * 初始化加载LH1a页面信息
	 */
	public String init(){
		this.pagename = "LH1A";
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		lhHsi = (LhMain) lhStepBo.loadById(LhMain.class, hsiId);
		if (ComacConstants.ANALYZE_STATUS_APPROVED.equals(lhHsi.getStatus())||ComacConstants.ANALYZE_STATUS_HOLD.equals(lhHsi.getStatus())) {
			// 版本状态为 审批完成只有查看权限
			isMaintain = ComacConstants.NO.toString();
		}
		this.lh1aBo.lh1aStepshow(hsiId, this.getSysUser(),getComModelSeries().getModelSeriesId());
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);

		if(lhHsi != null){
			this.hsiName = JavaScriptUtils.javaScriptEscape(lhHsi
					.getHsiName() == null ? "" : lhHsi.getHsiName());
			this.lheff = JavaScriptUtils.javaScriptEscape(lhHsi.getEffectiveness() == null ? 
								getComModelSeries().getModelSeriesName() :JavaScriptUtils.javaScriptEscape(lhHsi.getEffectiveness()));
		}else{
			this.lheff = getComModelSeries().getModelSeriesName();
		}
		
		comArea = (ComArea) lhStepBo.loadById(ComArea.class, lhHsi.getComArea().getAreaId());
		if(comArea != null ){
			this.areaName = JavaScriptUtils.javaScriptEscape(comArea
					.getAreaName() == null ? "" : comArea.getAreaName());
		}
		lh1a = lh1aBo.getLh1aByHsiId(hsiId);
		if (lh1a != null) {
			this.picId = lh1a.getPicId();
			this.content1 = JavaScriptUtils.javaScriptEscape(lh1a
					.getContent1() == null ? "" : lh1a.getContent1());
			this.content2 = JavaScriptUtils.javaScriptEscape(lh1a
					.getContent2() == null ? "" : lh1a.getContent2());
			this.content3 = JavaScriptUtils.javaScriptEscape(lh1a
					.getContent3() == null ? "" : lh1a.getContent3());
			this.picContent = JavaScriptUtils.javaScriptEscape(lh1a
					.getPicContent() == null ? "" : lh1a.getPicContent());
		}
		
		LhMain superLhHsi = lhMainBo.getLhMainByHsiCode(lhHsi.getRefHsiCode(),this.getComModelSeries().getModelSeriesId());
		if (superLhHsi != null) {
			Lh4 refLh4 = lh4Bo.getLh4BylhHsId(superLhHsi.getHsiId());
			if (refLh4 != null) {
				isneedRep = refLh4.getNeedLhTask().toString();
			}
		}
		return SUCCESS;
		
	}
	//
	/**
	 * 获取同步加载 lh2的数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLh1aRecords(){
		Lh1a lh1a = lh1aBo.getLh1aByHsiId(hsiId);
		List listJson = new ArrayList<HashMap>();
		HashMap jsonFeildList = null;
		JSONArray json = new JSONArray();
		jsonFeildList= new HashMap();
		if (lh1a != null) {
			if(lh1a.getPicContent()!=null){
				jsonFeildList.put("cn",lh1a.getPicContent());
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
	
	/**
	 * 保存 LH1a页面 数据
	 * @return 
	 */
	public String saveLh1a() {
		JSONObject json = null;
		LhMain lhMain = null;
		if (!BasicTypeUtils.isNullorBlank(hsiId)) {
			lhMain = (LhMain) this.lh1aBo.loadById(LhMain.class, hsiId);
		}
		LhMain superLhHsi = lhMainBo.getLhMainByHsiCode(lhMain.getRefHsiCode(),getComModelSeries().getModelSeriesId());
		if(superLhHsi != null) {// 如果参加HSI分析状态不等于 分析完成, LH1a的数据不可以保存.
			if(!ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(superLhHsi.getStatus())) {
				json = this.putJsonOKFlag(json, true);
				json.put("msg", "fail");
				this.writeToResponse(json.toString());
				return null;
			}
		}
		String dbOperate = "";
		Lh1a lh1a = lh1aBo.getLh1aByHsiId(hsiId);
		if (lh1a != null) { // 修改操作
			dbOperate = ComacConstants.DB_UPDATE;
		} else { // 新数据 添加操作
			dbOperate = ComacConstants.DB_INSERT;
			lh1a = new Lh1a();
			lh1a.setLhMain(lhMain);
		}
		lh1a.setPicContent(picContent);
		lh1a.setContent1(content1);
		lh1a.setContent2(content2);
		lh1a.setContent3(content3);
		lhMain.setEffectiveness(lheff);
		lh1aBo.save(lhMain);
		lh1aBo.doSaveLh1AandRef(hsiId, lh1a, dbOperate, this.getSysUser(),getComModelSeries().getModelSeriesId());
		json = this.putJsonOKFlag(json, true);
		json.put("msg", "success");
		this.writeToResponse(json.toString());
		return null;
	}
	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}
	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}
	public ILh1aBo getLh1aBo() {
		return lh1aBo;
	}
	public void setLh1aBo(ILh1aBo lh1aBo) {
		this.lh1aBo = lh1aBo;
	}
	public ILh4Bo getLh4Bo() {
		return lh4Bo;
	}
	public void setLh4Bo(ILh4Bo lh4Bo) {
		this.lh4Bo = lh4Bo;
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
	public ComArea getComArea() {
		return comArea;
	}
	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public Lh1a getLh1a() {
		return lh1a;
	}
	public void setLh1a(Lh1a lh1a) {
		this.lh1a = lh1a;
	}
	public String getContent1() {
		return content1;
	}
	public void setContent1(String content1) {
		this.content1 = content1;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}
	public String getContent3() {
		return content3;
	}
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	public String getPicContent() {
		return picContent;
	}
	public void setPicContent(String picContent) {
		this.picContent = picContent;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getIsneedRep() {
		return isneedRep;
	}
	public void setIsneedRep(String isneedRep) {
		this.isneedRep = isneedRep;
	}
	public String getLheff() {
		return lheff;
	}
	public void setLheff(String lheff) {
		this.lheff = lheff;
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
	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}
	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}
	public LhMain getLhHsi() {
		return lhHsi;
	}
	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}

	
}
