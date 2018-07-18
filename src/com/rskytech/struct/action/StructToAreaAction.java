package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IS7Bo;

@SuppressWarnings({"rawtypes","unchecked"})
public class StructToAreaAction extends BaseAction {
	private static final long serialVersionUID = -6112380473816441158L;
	private IS7Bo s7Bo;
	private Integer needTransfer;
	private Integer hasAccept;
	
	public String getRecords(){
		page.setStartIndex(start);
		page.setPageSize(limit);
		String modelId=getComModelSeries().getModelSeriesId();
		Page pageList=s7Bo.getStructToAreaRecords(needTransfer, hasAccept, page,modelId);
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		List<TaskMsg> list=pageList.getResult();
		if (list != null) {
			for (TaskMsg tm : list) {
				jsonFieldList=new HashMap();
				jsonFieldList.put("taskId", tm.getTaskId());
				jsonFieldList.put("ownArea",tm.getOwnArea());
				jsonFieldList.put("taskCode", tm.getTaskCode());
				if(tm.getSourceAnaId()!=null){
					SMain sMain=(SMain)s7Bo.loadById(SMain.class, tm.getSourceAnaId());
					if(sMain!=null){
						if(sMain.getComAta()!=null){
							ComAta comAta = sMain.getComAta();
							jsonFieldList.put("ssiName",comAta.getAtaName());
						}else{
							jsonFieldList.put("ssiName",sMain.getAddName());
						}
						
					}
				}
				
				jsonFieldList.put("msg3Code", tm.getTaskCode());
				jsonFieldList.put("taskType", tm.getTaskType());
				jsonFieldList.put("taskInterval",tm.getTaskInterval());
				jsonFieldList.put("reachWay",tm.getReachWay());
				jsonFieldList.put("taskDesc", tm.getTaskDesc());
				jsonFieldList.put("whyTransfer", tm.getWhyTransfer());
				jsonFieldList.put("hasAccept", tm.getHasAccept());
				jsonFieldList.put("rejectReason",tm.getRejectReason());
				jsonList.add(jsonFieldList);
			}
		}
		json.element("msg3", jsonList);
		json.element("totleCount",jsonList.size());
		writeToResponse(json.toString());
		return null;
	}
	
	public Integer getNeedTransfer() {
		return needTransfer;
	}
	public void setNeedTransfer(Integer needTransfer) {
		this.needTransfer = needTransfer;
	}
	public Integer getHasAccept() {
		return hasAccept;
	}
	public void setHasAccept(Integer hasAccept) {
		this.hasAccept = hasAccept;
	}
	public String init(){
		
		return SUCCESS;
	}
	public IS7Bo getS7Bo() {
		return s7Bo;
	}
	public void setS7Bo(IS7Bo bo) {
		s7Bo = bo;
	}
}
