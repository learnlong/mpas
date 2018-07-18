package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.web.Page;

import com.rskytech.lhirf.bo.ILh6Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

public class Lh6Action extends BaseAction {
	/**
	 * L/hirf 中的 任务信息表的 Action
	 */
	private static final long serialVersionUID = 1L;
	//注入
	private ILhStepBo lhStepBo;
	private ILh6Bo  lh6Bo;

	//参数
	private String pagename;
	private String hsiId;
	private LhMain lhHsi;
	private ComArea comArea;
	private LhStep lhStep;
	private String areaId ;
	private String  lhAnaId;
	private String isMaintain;
	 private String hsiName;
	private String areaName;
	/*
	 * 初始化加载LH6页面信息
	 */
	public String init(){
		ComUser thisUser = this.getSysUser();
		if ( null == thisUser) {
			return SUCCESS;
		}
		this.pagename ="LH6";
		lhHsi=(LhMain) this.lh6Bo.loadById(LhMain.class, hsiId);
		lhStep=lhStepBo.getLhStepBylhHsId(hsiId);
		comArea = (ComArea) lh6Bo.loadById(ComArea.class, this.lhHsi.getComArea().getAreaId());
		areaId = comArea.getAreaId();
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
	 *加载页面 任务详细信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String loadLh6Msg() throws Exception{
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		LhMain lhMain = (LhMain) lh6Bo.loadById(LhMain.class,hsiId);
		if( lhMain != null){
			Page page = lh6Bo.getLhirfListById(getComModelSeries().getModelSeriesId(), areaId, this.page);
			List list = page.getResult();
			List listJsonFV = new ArrayList();
			for (int i = 0; i < list.size(); i++){
				Object[] ob = (Object[]) list.get(i);
				HashMap jsonFeildList = new HashMap();
				if(null == ob[0]){
					jsonFeildList.put("hsiId", "") ;
				}else{
					jsonFeildList.put("hsiId", ob[0]) ;
				}
				if (ob[1] == null){
					jsonFeildList.put("hsiCode", "");
				} else {
					jsonFeildList.put("hsiCode", ob[1]);
				}
				if(ob[2] == null){
			    	jsonFeildList.put("taskId", "");
			    }else{
			    	jsonFeildList.put("taskId", ob[2]);
				    	
			    }
				if(ob[3]!=null){
					jsonFeildList.put("taskCode", ob[3]);
				}else{
					jsonFeildList.put("taskCode", "");
				}
			    if(null == ob[4]){
			    	jsonFeildList.put("taskType", "") ;
			    }else {
			    	jsonFeildList.put("taskType", ob[4]);
			    }
			    if(null == ob[5]){
			    	jsonFeildList.put("ipvOpvpOpve", "") ;
			    }else{
			    	jsonFeildList.put("ipvOpvpOpve", ob[5]);
			    }
			    if(null ==ob[6]){
			    	jsonFeildList.put("reachWay", "");
			    }else{
			    	jsonFeildList.put("reachWay", ob[6]);
			    }
			    if(null ==ob[7]){
			    	jsonFeildList.put("taskDesc", "");
			    }else{
			    	jsonFeildList.put("taskDesc", ob[7]);
			    }
			    if(null ==ob[8]){
			    	jsonFeildList.put("taskInterval", "");
			    }else{
			    	jsonFeildList.put("taskInterval", ob[8]);
			    }
			    if( ob[9] != null && "1".equals(ob[9].toString())){
			    	jsonFeildList.put("needTransfer", "1");
			    } else  {
			    	jsonFeildList.put("needTransfer", "0");
			    } 
			    
			    if(null ==ob[10]){
			    	jsonFeildList.put("hasAccept", "");
			    }else{
			    	if("2".equals(ob[10].toString())){
			    		jsonFeildList.put("hasAccept", "");
			    	}
			    	jsonFeildList.put("hasAccept", ob[10].toString());
			    }
			    
			    if(null ==ob[11]){
			    	jsonFeildList.put("rejectReason", "");
			    }else{
			    	jsonFeildList.put("rejectReason",ob[11]);
			    }
			    if(ob[12] == null){
			    	 jsonFeildList.put("lheff", "");///lhrif 适用性
			    }else{
			    	 jsonFeildList.put("lheff", ob[12]);///lhrif 适用性
			    }
			    listJsonFV.add(jsonFeildList);
			}
			json.element("totalCount", page.getTotalCount());
			json.element("lh6", listJsonFV);
	        writeToResponse(json.toString());
			return null;
		}
		return null;
	}
	
	/**
	 * 保存LH6页面store 数据源 信息 
	 * @author wangyueli
	 */
	public String saveLh6List(){
		  this.lh6Bo.saveLh6LhEff(this.getSysUser(), jsonData);
		  JSONObject	json = putJsonOKFlag(null,true);
			writeToResponse(json.toString());
		 return null;
	}

	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh6Bo getLh6Bo() {
		return lh6Bo;
	}

	public void setLh6Bo(ILh6Bo lh6Bo) {
		this.lh6Bo = lh6Bo;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getHsiId() {
		return hsiId;
	}

	public void setHsiId(String hsiId) {
		this.hsiId = hsiId;
	}

	public LhMain getLhHsi() {
		return lhHsi;
	}

	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}

	public ComArea getComArea() {
		return comArea;
	}

	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}

	public LhStep getLhStep() {
		return lhStep;
	}

	public void setLhStep(LhStep lhStep) {
		this.lhStep = lhStep;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getLhAnaId() {
		return lhAnaId;
	}

	public void setLhAnaId(String lhAnaId) {
		this.lhAnaId = lhAnaId;
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

}
