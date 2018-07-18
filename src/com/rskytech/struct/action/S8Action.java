package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.IS8Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class S8Action extends BaseAction {
	private static final long serialVersionUID = -2732477923686564568L;
	private int[] step;
	private String siCode;
	private String siTitle;
	private IS1Bo s1Bo;
	private String ssiId;
	private int isMaintain;
	private String mrbCode;
	private String mrbId;
	private String delId;
	private String mrbJsonData;
	private IS8Bo s8Bo;
	private String msg3Id;
	private IComAreaBo comAreaBo;
	private ISsiStepBo ssiStepBo;
	private ITaskMsgBo taskMsgBo;
	private String ssiEff;
	/**
	 * 初始化S8
	 * @return
	 */
	public String initS8() {
		ComUser thisUser = getSysUser();
		if (thisUser == null) {
			return SUCCESS;// 现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		SMain sMain=(SMain)s1Bo.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		ssiEff=JavaScriptUtils.javaScriptEscape(sMain.getEffectiveness()==null?"":sMain.getEffectiveness());
		List<SStep> step1 = s1Bo.getSstepBySssiId(ssiId);
		step = this.ssiStepBo.initStep(ssiId, step1.get(0), "S8");
		return SUCCESS;
	}
	/**
	 * 得到S8的数据
	 * @return
	 */
	public String getS8Records() {
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		List<TaskMsg> tmlist = s8Bo.getS8Records(ssiId,"S8");
		String areaCode=null;
		if (tmlist != null) {
			for (TaskMsg tm : tmlist) {
				if(tm.getOwnArea()!=null){
				areaCode=comAreaBo.getAreaCodeByAreaId(tm.getOwnArea());}
				jsonFieldList=new HashMap();
				jsonFieldList.put("taskId", tm.getTaskId());// 任务ID
				jsonFieldList.put("ownArea",areaCode);
				jsonFieldList.put("taskCode", wipeNull(tm.getTaskCode()));// MSG-3任务号
				jsonFieldList.put("tempType", wipeNull(tm.getTaskType()));// 任务类型
				jsonFieldList.put("eff", wipeNull(tm.getEffectiveness()));//适用性
				jsonFieldList.put("reachWay", wipeNull(tm.getReachWay()));// 接近方式
				jsonFieldList.put("taskDesc", wipeNull(tm.getTaskDesc()));// 任务描述
				jsonFieldList.put("occures", wipeNull(tm.getTaskInterval()));// 任务间隔
				if(tm.getMrbId()!=null){
				TaskMrb tmb=(TaskMrb) s8Bo.loadById(TaskMrb.class,tm.getMrbId());
					if(tmb!=null){
						jsonFieldList.put("mrbTaskCode",tmb.getMrbCode() );// MRB任务号
					}
				}
			
				jsonFieldList.put("temp", wipeNull(tm.getAnyContent4()));// 自加任务
				jsonList.add(jsonFieldList);
			}
		}
		json.element("msg3Task", jsonList);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到MRB的数据
	 * @return
	 */
	public String getMrbRecords(){
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		List<TaskMrb> tmlist = s8Bo.getMrbRecords(ssiId, getComModelSeries().getModelSeriesId());
		String area=null;
		if (tmlist != null) {
			for (TaskMrb tm : tmlist) {
				jsonFieldList=new HashMap();
				jsonFieldList.put("id", tm.getMrbId());//MRBid
				jsonFieldList.put("taskMrbCode",wipeNull(tm.getMrbCode()));//任务编号
				jsonFieldList.put("taskType", wipeNull(tm.getTaskType()));
				jsonFieldList.put("occures",wipeNull(tm.getTaskIntervalOriginal()));//任务间隔
				jsonFieldList.put("reachWay", wipeNull(tm.getReachWay()));
				jsonFieldList.put("eff", wipeNull(tm.getEffectiveness()));//适用性
				jsonFieldList.put("taskDesc", wipeNull(tm.getTaskDesc()));
				if(tm.getOwnArea()!=null){
					area=comAreaBo.getAreaCodeByAreaId(tm.getOwnArea());
				}
				jsonFieldList.put("zone",wipeNull(area));//所属区域
				jsonList.add(jsonFieldList);
				
			}
		}
		json.element("mrbTask", jsonList);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到下拉框的数据
	 * @return
	 */
	public String getComboMrb(){
		HashMap jsonFieldList = null;
		List<HashMap> jsonList = new ArrayList();
		JSONObject json = new JSONObject();
		List<TaskMrb> tmlist = s8Bo.getMrbRecords(ssiId, getComModelSeries().getModelSeriesId());
		if (tmlist != null) {
			for (TaskMrb tm : tmlist) {
				jsonFieldList=new HashMap();
				jsonFieldList.put("mrbId", tm.getMrbId());//MRBid
				jsonFieldList.put("mrbCode",tm.getMrbCode());//任务编号
				jsonFieldList.put("taskType",tm.getTaskType());
				jsonList.add(jsonFieldList);
				
			}
		}
		json.element("mrbs", jsonList);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 验证MRBCODE是否存在
	 * @return
	 */
	public String verifyMrbCode(){
		List<TaskMrb> list=s8Bo.getMrbCountByCode(mrbCode,getComModelSeries());
		if(list==null||list.size()==0||ssiId.equals(list.get(0).getSourceAnaId())){
			writeToResponse("{success:true}");
		}else{
			writeToResponse("{success:false}");
		}
		return null;
	}
	Object[] str=null;
	private IS4Bo s4Bo;
	public IS4Bo getS4Bo() {
		return s4Bo;
	}

	public void setS4Bo(IS4Bo bo) {
		s4Bo = bo;
	}
	/**
	 * 保存S8
	 * @return
	 */
	public String saveS8Records(){
		if(delId!=null){
			String[] delArray=delId.split(",");
			for(String str:delArray){
				if("".equals(str.trim())){
					continue;
				}
				if(s8Bo.loadById(TaskMrb.class, str.trim())!=null){
					List<TaskMsg> taskList = taskMsgBo.getTaskByMrbId(getComModelSeries().getModelSeriesId(),str.trim());
					if(taskList!=null&&taskList.size()>0){
						TaskMsg task = taskList.get(0);
						String mpdId = task.getMpdId();
						if(!BasicTypeUtils.isNullorBlank(mpdId)){
							this.taskMsgBo.delete((TaskMpd)this.taskMsgBo.loadById(TaskMpd.class, mpdId));
						}
						task.setMpdId(null);
						task.setMrbId(null);
						this.taskMsgBo.update(task,getSysUser().getUserId());
					}
					s1Bo.delete(TaskMrb.class, str.trim());
				}
			}
		}
		List saveList = new ArrayList();
		String state=ComacConstants.DB_INSERT;
		JSONArray mrbJsonArray = JSONArray.fromObject(mrbJsonData);
		JSONArray msgJsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		TaskMrb tm = null;
		JSONArray mrbjsonArray = JSONArray.fromObject(mrbJsonArray);
		String areaId=null;
		ArrayList<HashMap> arr = new ArrayList<HashMap>();
		HashMap mrbMap;
		for(int i = 0;i<mrbjsonArray.size();i++){
			jsonObject = mrbjsonArray.getJSONObject(i);
			if(!"".equals(jsonObject.get("id"))){
				tm=(TaskMrb)s8Bo.loadById(TaskMrb.class, jsonObject.getString("id"));
				state=ComacConstants.DB_UPDATE;
				tm.setModifyUser(getSysUser().getUserId());
				tm.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
			}else{
				tm = new TaskMrb();
			}
			tm.setMrbCode(jsonObject.getString("taskMrbCode"));
			tm.setTaskType(jsonObject.getString("taskType"));
			tm.setReachWay(jsonObject.getString("reachWay"));
			tm.setEffectiveness(jsonObject.getString("eff"));
			tm.setComModelSeries(getComModelSeries());
			tm.setTaskDesc(jsonObject.getString("taskDesc"));
			tm.setTaskIntervalOriginal(jsonObject.getString("occures"));
			tm.setSourceSystem(ComacConstants.STRUCTURE_CODE);
			tm.setSourceWhere("S8");
			if(jsonObject.get("zone")!=null){
				areaId=comAreaBo.getAreaIdByAreaCode(jsonObject.getString("zone"), getComModelSeries().getModelSeriesId());
				tm.setOwnArea(areaId);
			}
			tm.setValidFlag(ComacConstants.VALIDFLAG_YES);
			tm.setSourceAnaId(ssiId);
			str=new Object[3];
			str[0]=tm;
			str[1]=state;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
			mrbMap= new HashMap();
			mrbMap.put("taskMpd", tm);
			mrbMap.put("dbOperate", state);
			arr.add(mrbMap);
		}	
		TaskMsg tmg= null;
		for(int i = 0;i<msgJsonArray.size();i++){
			jsonObject = msgJsonArray.getJSONObject(i);
			
			if(!"".equals(jsonObject.getString("taskId"))){
				tmg=(TaskMsg)s8Bo.loadById(TaskMsg.class, jsonObject.getString("taskId"));
				tmg.setModifyUser(getSysUser().getUserId());
				tmg.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
				state=ComacConstants.DB_UPDATE;
			}else{
					tmg=new TaskMsg();
					state=ComacConstants.DB_INSERT;
			}
			if(!"".equals(jsonObject.getString("mrbTaskCode"))){
					String isVerify=null;
					List<TaskMrb> listMrb=s8Bo.getMrbCountByCode(jsonObject.getString("mrbTaskCode"),getComModelSeries());
					if(listMrb!=null&&listMrb.size()>0){
						isVerify = listMrb.get(0).getMrbId();
					}
					if(isVerify!=null){
						tmg.setMrbId(isVerify);
					}else {
						List<TaskMrb> tmList=s8Bo.getMrbCountByCode(jsonObject.getString("mrbTaskCode").trim(),getComModelSeries());
						if(tmList==null||tmList.size()==0){
							s4Bo.saveList(saveList,getSysUser(),"S8");
							tmList=s8Bo.getMrbCountByCode(jsonObject.getString("mrbTaskCode").trim(),getComModelSeries());
							tmg.setMrbId(tmList.get(0).getMrbId());
						}else{
							tmg.setMrbId(tmList.get(0).getMrbId());
						}
					}
				}else if("".equals(jsonObject.getString("mrbTaskCode"))){
					tmg.setMrbId("");
				}
			if(jsonObject.get("temp")!=null&&!"null".equals(jsonObject.getString("temp"))){
				tmg.setAnyContent4(jsonObject.getString("temp"));
			}

			tmg.setTaskCode(jsonObject.getString("taskCode"));
			tmg.setNeedTransfer(ComacConstants.NO);
			tmg.setTaskDesc(jsonObject.getString("taskDesc"));
			areaId=comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"), getComModelSeries().getModelSeriesId());
			tmg.setOwnArea(areaId);
			tmg.setTaskInterval(jsonObject.getString("occures"));
			tmg.setReachWay(jsonObject.getString("reachWay"));
			tmg.setEffectiveness(jsonObject.getString("eff"));
			tmg.setValidFlag(ComacConstants.VALIDFLAG_YES);
			tmg.setComModelSeries(getComModelSeries());
			tmg.setSourceSystem(ComacConstants.STRUCTURE_CODE);
			tmg.setSourceAnaId(ssiId);
			tmg.setSourceStep("S8");
			tmg.setTaskType(jsonObject.getString("tempType"));
			str=new Object[3];
			str[0]=tmg;
			str[1]=state;
			str[2]=getSysUser().getUserId();
			saveList.add(str);
			
		}
		List<SStep> step1=s1Bo.getSstepBySssiId((ssiId));
		step1.get(0).setS8(1);
		str=new Object[3];
		str[0]=step1.get(0);
		str[1]=ComacConstants.DB_UPDATE;
		str[2]=getSysUser().getUserId();
		saveList.add(str);
		s4Bo.saveList(saveList,getSysUser(),"S8");
		saveMpd(arr);
		return null;
	}
	
	/**
	 * 保存Mpd数据
	 * @param set
	 */
	public void saveMpd(ArrayList<HashMap> arr){
		TaskMrb taskMrb;
		TaskMsg task = null;
		TaskMpd taskMpd;
		HashMap hash;
		String dbOperate;
		if(arr.size()>0){
			for(int i = 0;i<arr.size();i++){
				hash = arr.get(i);
				taskMrb = (TaskMrb) hash.get("taskMpd");
				dbOperate =  (String) hash.get("dbOperate");
				List<TaskMrb> taskMrbList = s8Bo.getMrbCountByCode(taskMrb.getMrbCode(),this.getComModelSeries());
				if(taskMrbList!=null&&taskMrbList.size()>0){
					List<TaskMsg> taskList = taskMsgBo.getTaskByMrbId(getComModelSeries().getModelSeriesId(),taskMrb.getMrbId());
					task = taskList.get(0);
				}
				taskMpd = new TaskMpd();
				if(dbOperate.endsWith(ComacConstants.DB_UPDATE)){
					if(task!=null&&task.getMpdId()!=null&&!"".equals(task.getMpdId())){
						taskMpd = (TaskMpd) this.s8Bo.loadById(TaskMpd.class, task.getMpdId());
					}
				}
				taskMpd.setMpdCode(taskMrb.getMrbCode()+"-01");
				taskMpd.setTaskType(taskMrb.getTaskType());
				taskMpd.setReachWay(taskMrb.getReachWay());
				taskMpd.setEffectiveness(taskMrb.getEffectiveness());
				taskMpd.setComModelSeries(getComModelSeries());
				taskMpd.setTaskDesc(taskMrb.getTaskDesc());
				taskMpd.setTaskIntervalOriginal(taskMrb.getTaskIntervalOriginal());
				taskMpd.setSourceSystem(ComacConstants.STRUCTURE_CODE);
				taskMpd.setOwnArea(taskMrb.getOwnArea());
				taskMpd.setValidFlag(ComacConstants.VALIDFLAG_YES);
				this.s8Bo.saveOrUpdate(taskMpd, dbOperate, getSysUser().getUserId());
				taskMpd = s8Bo.getMpdByCode(taskMpd.getMpdCode(), getComModelSeries());
				task.setMpdId(taskMpd.getMpdId());
				this.s8Bo.update(task, getSysUser().getUserId());
			}
		}
	}
	
	public String delS8Reocrd(){
		this.taskMsgBo.deleteTaskMsgById(msg3Id);
		return null;
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
	public int[] getStep() {
		return step;
	}
	public void setStep(int[] step) {
		this.step = step;
	}
	public String getSiCode() {
		return siCode;
	}
	public void setSiCode(String siCode) {
		this.siCode = siCode;
	}
	public String getSiTitle() {
		return siTitle;
	}
	public void setSiTitle(String siTitle) {
		this.siTitle = siTitle;
	}
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}
	public String getSsiId() {
		return ssiId;
	}
	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}
	public int getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}
	public String getMrbCode() {
		return mrbCode;
	}
	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}
	public String getMrbId() {
		return mrbId;
	}
	public void setMrbId(String mrbId) {
		this.mrbId = mrbId;
	}
	public String getDelId() {
		return delId;
	}
	public void setDelId(String delId) {
		this.delId = delId;
	}
	public String getMrbJsonData() {
		return mrbJsonData;
	}
	public void setMrbJsonData(String mrbJsonData) {
		this.mrbJsonData = mrbJsonData;
	}
	public IS8Bo getS8Bo() {
		return s8Bo;
	}
	public void setS8Bo(IS8Bo s8Bo) {
		this.s8Bo = s8Bo;
	}
	public String getMsg3Id() {
		return msg3Id;
	}
	public void setMsg3Id(String msg3Id) {
		this.msg3Id = msg3Id;
	}
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}
	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}
	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}
	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	public String getSsiEff() {
		return ssiEff;
	}
	public void setSsiEff(String ssiEff) {
		this.ssiEff = ssiEff;
	}
	
}
