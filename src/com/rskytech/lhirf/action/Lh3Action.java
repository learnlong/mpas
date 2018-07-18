package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.lhirf.bo.ILh3Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh3;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

public class Lh3Action extends BaseAction {

	/**
	 * L/hirf 中的 lh_3表的 Action
	 */
	private static final long serialVersionUID = 1L;
	//注入
	private ILhStepBo lhStepBo;
	private ILh3Bo lh3Bo;
	
	//参数
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private String lh3Id;
	private ComArea comArea;
	private String areaId;
	private String defectModel;
	private String defectDesc;
	private String isMaintain;
	private String hsiName;
	private String areaName;
	private LhMain lhHsi;
	
	/*
	 * 初始化加载LH3页面信息
	 */
	public String  init(){
		
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		this.pagename = "LH3";
		lhHsi = (LhMain) this.lh3Bo.loadById(LhMain.class, hsiId);
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		comArea = (ComArea) lh3Bo.loadById(ComArea.class, this.lhHsi
				.getComArea().getAreaId());
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
	
	
	
	/*
	 * 初始化加载LH3页面store 数据源 信息 
	 */
	public String loadLh3List(){
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<Lh3> lh3List = lh3Bo.getLh3ListByHsiId(hsiId, this.page);
		if (lh3List != null) {
			for (Lh3 lh3 : lh3List) {
				listJsonFV.add(jsonFieldValue(lh3));
			}
		}
		json.element("totleCount", this.getPage().getTotalCount());
		json.element("lh3", listJsonFV);
		writeToResponse(json.toString());
		
		return null;
	}
	@SuppressWarnings("unchecked")
	private HashMap jsonFieldValue(Lh3 lh3){
		 HashMap jsonFeildList = new HashMap();
		 if(lh3.getLh3Id()!=null){
			 jsonFeildList.put("lh3Id",lh3.getLh3Id());
		 }else{
			 jsonFeildList.put("lh3Id","");
		 }
		 if(lh3.getDefectModel()!=null){
			 jsonFeildList.put("defectModel",lh3.getDefectModel());
		 }else{
			 jsonFeildList.put("defectModel","");

		 }
		 if(lh3.getDefectDesc()!=null){
			 jsonFeildList.put("defectDesc",lh3.getDefectDesc());
		 }else{
			 jsonFeildList.put("defectDesc","");
		 }
		return jsonFeildList;
	}
	
	/**
	 * 保存LH3页面store 数据源 信息 
	 * @author wangyueli
	 * createdate 2012-08-25
	 */
	public String saveLh3List(){
		  this.lh3Bo.saveLh3andStep(this.getSysUser(), hsiId, this.getJsonData(),getComModelSeries());
		 return null;
	}
	
	/**
	 *  删除选择的 lh3一条记录
	 * @author wangyueli
	 * createdate 2012-08-30
	 */
	public String deletelh3(){
		 if(!BasicTypeUtils.isNullorBlank(lh3Id)){
			 lh3Bo.delete(Lh3.class, lh3Id);
			 JSONObject	json = putJsonOKFlag(null,true);
	  			writeToResponse(json.toString());
	  			return null ;
		 }
	   JSONObject	json = putJsonOKFlag(null,false);
		writeToResponse(json.toString());
		return null;
	}



	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh3Bo getLh3Bo() {
		return lh3Bo;
	}

	public void setLh3Bo(ILh3Bo lh3Bo) {
		this.lh3Bo = lh3Bo;
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

	public String getLh3Id() {
		return lh3Id;
	}

	public void setLh3Id(String lh3Id) {
		this.lh3Id = lh3Id;
	}

	public ComArea getComArea() {
		return comArea;
	}

	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDefectModel() {
		return defectModel;
	}

	public void setDefectModel(String defectModel) {
		this.defectModel = defectModel;
	}

	public String getDefectDesc() {
		return defectDesc;
	}

	public void setDefectDesc(String defectDesc) {
		this.defectDesc = defectDesc;
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
