package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IUnSsiBo;
import com.rskytech.struct.dao.IUnSsiDao;
import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;

public class UnSsiBo extends BaseBO implements IUnSsiBo{
	private IUnSsiDao unSsiDao;
	private IComAreaBo comAreaBo;
	
	/**
	 *  获取不需要分析的ssi任务数据
	 * @param ssiId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject getUnssiRecords(String ssiId,String modelSeriesId){
		List<TaskMsg> listTask = this.unSsiDao.searchUnSsiList(ssiId, modelSeriesId);
		String ssiCode="";
		String ssiName="";
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		SMain sMain= (SMain) this.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			ssiCode=comAta.getAtaCode();
			ssiName=comAta.getAtaName();
		}else{
			ssiCode=sMain.getAddCode();
			ssiName=sMain.getAddName();
		}
 	    String areaCode = "";
		for(TaskMsg tm : listTask){
			jsonFieldList=new HashMap();
			jsonFieldList.put("taskId", tm.getTaskId());
			jsonFieldList.put("ssiCode", ssiCode);
			jsonFieldList.put("ssiName", ssiName);
			jsonFieldList.put("taskCode", wipeNull(tm.getTaskCode()));
			jsonFieldList.put("componentName", wipeNull(tm.getAnyContent1()));
			jsonFieldList.put("picCode", wipeNull(tm.getAnyContent2()));
			jsonFieldList.put("markingCode", wipeNull(tm.getAnyContent3()));
			if(tm.getOwnArea()!=null){
				areaCode = comAreaBo.getAreaCodeByAreaId(tm.getOwnArea());
				jsonFieldList.put("ownArea", areaCode);
			}else{
				jsonFieldList.put("ownArea", "");
			}
			jsonFieldList.put("taskType", wipeNull(tm.getTaskType()));
			jsonFieldList.put("taskInterval", wipeNull(tm.getTaskInterval()));
			jsonFieldList.put("taskDesc", wipeNull(tm.getTaskDesc()));
			jsonFieldList.put("remark", wipeNull(tm.getRemark()));
			jsonList.add(jsonFieldList);
		}
		json.element("unSsi", jsonList);
		return json;
	}
	 /**
	 * 将空字符串替换为“”
	 * @param str
	 * @return
	 */
	private String wipeNull(String str){
		if(str!=null&&!"null".equals(str)){
			return str;
		}
		return "";
	}
	@Override
	public void saveUnSsiData(String jsonData, String ssiId,
			String modelSeriesId, String userId) {
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = ComacConstants.DB_INSERT;
		TaskMsg tm ;
		for(int i=0;i<jsonArray.size();i++){
			jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.get("taskId") != null&&!"".equals(jsonObject.get("taskId").toString())
					&& !"null".equals(jsonObject.get("taskId").toString())){
				tm = (TaskMsg) this.loadById(TaskMsg.class, jsonObject.get("taskId").toString());
				dbOperate = ComacConstants.DB_UPDATE;
			}else{
				tm = new TaskMsg();
				tm.setSourceStep("UNSSI");
				tm.setSourceAnaId(ssiId);
				tm.setSourceSystem(ComacConstants.STRUCTURE_CODE);
				tm.setValidFlag(ComacConstants.YES);
				tm.setComModelSeries((ComModelSeries) this.loadById(ComModelSeries.class, modelSeriesId));
			}
			tm.setTaskCode(jsonObject.getString("taskCode"));
			tm.setAnyContent1(jsonObject.getString("componentName"));
			tm.setAnyContent2(jsonObject.getString("picCode"));
			tm.setAnyContent3(jsonObject.getString("markingCode"));
			if(!BasicTypeUtils.isNullorBlank(jsonObject.getString("ownArea"))){
				tm.setOwnArea(comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"),modelSeriesId));
			}else{
				tm.setOwnArea(null);
			}
			tm.setTaskType(jsonObject.getString("taskType"));
			tm.setTaskInterval(jsonObject.getString("taskInterval"));
			tm.setTaskDesc(jsonObject.getString("taskDesc"));
			tm.setRemark(jsonObject.getString("remark"));
			SMain sm = (SMain) this.loadById(SMain.class, ssiId);
			sm.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			this.saveOrUpdate(sm,ComacConstants.DB_UPDATE,userId);
			this.saveOrUpdate(tm, dbOperate, userId);
			addMrbAndMpd(tm,userId);
		}
	}

	public void addMrbAndMpd(TaskMsg taskMsg, String userId) {
		TaskMpd taskMpd = new TaskMpd();
		TaskMrb taskMrb = new TaskMrb();
		if (taskMsg.getMrbId() == null) {
			taskMrb.setMrbCode(taskMsg.getTaskCode() + "-01");
			taskMrb.setAnyContent(taskMsg.getAnyContent1());
			taskMrb.setComModelSeries(taskMsg.getComModelSeries());
			taskMrb.setSourceSystem(taskMsg.getSourceSystem());
			taskMrb.setSourceAnaId(taskMsg.getSourceAnaId());
			taskMrb.setTaskType(taskMsg.getTaskType());
			taskMrb.setReachWay(taskMsg.getReachWay());
			taskMrb.setTaskIntervalOriginal(taskMsg.getTaskInterval());
			taskMrb.setOwnArea(taskMsg.getOwnArea());
			taskMrb.setEffectiveness(taskMsg.getEffectiveness());
			taskMrb.setValidFlag(taskMsg.getValidFlag());
			taskMrb.setFailureCauseType(taskMsg.getAnyContent2());// 故障影响类别
			this.save(taskMrb, userId);
			taskMsg.setMrbId(taskMrb.getMrbId());
		}
		if (taskMsg.getMpdId() == null) {
			taskMpd.setMpdCode(taskMsg.getTaskCode() + "-01-01");
			taskMpd.setComModelSeries(taskMsg.getComModelSeries());
			taskMpd.setSourceSystem(taskMsg.getSourceSystem());
			taskMpd.setTaskType(taskMsg.getTaskType());
			taskMpd.setReachWay(taskMsg.getReachWay());
			taskMpd.setTaskIntervalOriginal(taskMsg.getTaskInterval());
			taskMpd.setOwnArea(taskMsg.getOwnArea());
			taskMpd.setEffectiveness(taskMsg.getEffectiveness());
			taskMpd.setValidFlag(taskMsg.getValidFlag());
			taskMpd.setFailureCauseType(taskMsg.getAnyContent2());// 故障影响类别
			this.save(taskMpd, userId);
			taskMsg.setMpdId(taskMpd.getMpdId());
		}
	}
	
	public IUnSsiDao getUnSsiDao() {
		return unSsiDao;
	}

	public void setUnSsiDao(IUnSsiDao unSsiDao) {
		this.unSsiDao = unSsiDao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	
}
