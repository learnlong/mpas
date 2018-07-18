package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILh5Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

public class Lh5Action extends BaseAction {

	/**
	 * L/hirf 中的 lh_5表的 Action
	 */
	private static final long serialVersionUID = 1L;
	// 注入
	private ILhStepBo lhStepBo;
	private ILh5Bo lh5Bo;
	private ILh4Bo lh4Bo;
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
	// 参数
	private Lh5 lh5;
	private String gviAvl;
	private String gviDesc;
	private Integer detAvl;
	private String detDesc;
	private Integer fncAvl;
	private String fncDesc;
	private Integer disAvl;
	private String disDesc;
	private String needRedesign;
	private String redesignDesc;
	private Lh4 lh4;
	private String lh5Id;
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private ComArea comArea;
	private LhMain lhHsi;
	private List<CusInterval> listCusList;
	private String resultLh4Value;
	private String[] arryDeltTaskId;
	private String isMaintain;
	private String msgTaskCode;
	private String msg3TaskId ;///用来校对msg_3任务  是否存在 /是否一致相同
	private String hsiName;
	private String areaName;
	private IComCoordinationBo comCoordinationBo;
	private String jsonGVI;
	private String jsonDET;
	private String jsonFNC;
	private String jsonDIS;
	private String[] arryDelCooId;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	
	/*
	 * 初始化加载LH5页面信息 @author wangyueli createdate 2012-09-10
	 */
	public String init() {
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		this.pagename = "LH5";
		Integer lh4NeedTask = ComacConstants.NO;
		lh5 = lh5Bo.getLh5ByHsiId(hsiId);
		if (lh5 != null) {
			this.gviDesc = JavaScriptUtils.javaScriptEscape(lh5
					.getGviDesc() == null ? "" : lh5.getGviDesc());
			this.detDesc = JavaScriptUtils.javaScriptEscape(lh5
					.getDetDesc() == null ? "" : lh5.getDetDesc());
			this.fncDesc = JavaScriptUtils.javaScriptEscape(lh5
					.getFncDesc() == null ? "" : lh5.getFncDesc());
			this.disDesc = JavaScriptUtils.javaScriptEscape(lh5
					.getDisDesc() == null ? "" : lh5.getDisDesc());
			this.redesignDesc = JavaScriptUtils.javaScriptEscape(lh5.
					getRedesignDesc() == null ? "": lh5.getRedesignDesc());
		}

		lhHsi = (LhMain) this.lh5Bo.loadById(LhMain.class, hsiId);
		// /自己有参见的refCode
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (!BasicTypeUtils.isNullorBlank(this.lhHsi.getComArea().getAreaId())) {
			comArea = (ComArea) lh5Bo.loadById(ComArea.class, this.lhHsi
					.getComArea().getAreaId());
		}
		lh4 = lh4Bo.getLh4BylhHsId(hsiId);

		if (lh4 != null) { // /is_safe=0 否 不妨碍安全飞行 ///is_safe=1 是 妨碍安全飞行
			lh4NeedTask = lh4.getNeedLhTask();
			if (ComacConstants.YES.equals(lh4NeedTask)) {
				if (lh4.getIsSafe() != 1) {
					listCusList = lh5Bo.getCusIntervalbyFlg("LH5",
							"B", getComModelSeries().getModelSeriesId());
				} else {
					listCusList = lh5Bo.getCusIntervalbyFlg("LH5",
							"A", getComModelSeries().getModelSeriesId());
				}
				if (listCusList != null) {// list
					for (CusInterval cusInt : listCusList) {
						if (lh4.getResult().equals(cusInt.getIntervalLevel())) {
							resultLh4Value = cusInt.getIntervalValue();
						}
					}
				}
			} else {
				return "lh6show"; // 如果不需要维修 则没有lh5 页面及数据 , 以及参见此LH5的数据都更新 无效
			}
		}
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
	 * 初始化加载LH5页面任务 MSG-3 信息 @author wangyueli createdate 2012-09-10
	 */
	@SuppressWarnings("rawtypes")
	public String searchMsglh5() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		lhHsi = (LhMain) lh5Bo.loadById(LhMain.class, hsiId); // 根据hsiId 得到分析
		// anyId
		List<TaskMsg> listMsg = taskMsgBo.getTaskMsgListByMainId(
				getComModelSeries().getModelSeriesId(), hsiId,
				ComacConstants.LHIRF_CODE, "LH5");
		for (TaskMsg taskMsg : listMsg) {
			listJsonFV.add(jsonFieldValue(taskMsg));
		}
		json.element("taskMsg", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	@SuppressWarnings("unchecked")
	private HashMap jsonFieldValue(TaskMsg taskMsg) {
		HashMap jsonFeildList = new HashMap();
		String areaCode="";
		jsonFeildList.put("taskId", taskMsg.getTaskId());
		if (null == taskMsg.getTaskCode()) {
			jsonFeildList.put("taskCode", "");// N/A
		} else {
			jsonFeildList.put("taskCode", taskMsg.getTaskCode());
		}
		if(taskMsg.getTaskType()!=null){
			jsonFeildList.put("taskType", taskMsg.getTaskType());
		 }else{
			jsonFeildList.put("taskType","");
		 }
		if(taskMsg.getReachWay()!=null){
			jsonFeildList.put("reachWay", taskMsg.getReachWay());
		}else{
			jsonFeildList.put("reachWay", "");
		}
		if( taskMsg.getTaskDesc()!=null){
			jsonFeildList.put("taskDesc", taskMsg.getTaskDesc());
		 }else{
			jsonFeildList.put("taskDesc", "");

		 }
		if(taskMsg.getTaskInterval()!=null){
			jsonFeildList.put("taskInterval", taskMsg.getTaskInterval());
		 }else{
			jsonFeildList.put("taskInterval", "");
		 }
		if(taskMsg.getNeedTransfer()!=null){
			jsonFeildList.put("needTransfer", taskMsg.getNeedTransfer());
		 }else{
		 	jsonFeildList.put("needTransfer", "");
		 }
		if (!BasicTypeUtils.isNullorBlank(taskMsg.getOwnArea())) {
			areaCode =comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea());
			jsonFeildList.put("ownArea", areaCode);
		} else {
			jsonFeildList.put("ownArea", areaCode);
		}
		jsonFeildList.put("whyTransfer", taskMsg.getWhyTransfer());
		return jsonFeildList;
	}

	/*
	 * 保存 LH5页面信息 @author wangyueli createdate 2012-09-10
	 */
	public String saveLh5() {
		String dbOperate = "";
		Lh5 lh5 = lh5Bo.getLh5ByHsiId(hsiId);
		LhMain lhHsilh5 = (LhMain) lh5Bo.loadById(LhMain.class, hsiId);
		if (lh5 != null) {
			dbOperate = ComacConstants.DB_UPDATE;// 修改操作
			// /参见的情况下
			if (!ComacConstants.EMPTY.endsWith(lhHsilh5.getRefHsiCode())
					|| lhHsilh5.getRefHsiCode() != null) {
				lhHsilh5.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			}
		} else { // 新数据 添加操作
			dbOperate = ComacConstants.DB_INSERT;
			lh5 = new Lh5();
			lhHsilh5.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			lh5.setLhMain(lhHsilh5);
		}

		lh5.setDetAvl(detAvl);
		lh5.setDetDesc(detDesc);
		lh5.setDisAvl(disAvl);
		lh5.setDisDesc(disDesc);
		lh5.setFncAvl(fncAvl);
		lh5.setFncDesc(fncDesc);
		lh5.setGviAvl(BasicTypeUtils.parseInt(gviAvl));
		lh5.setGviDesc(gviDesc);
		lh5.setNeedRedesign(BasicTypeUtils.parseInt(needRedesign));
		lh5.setRedesignDesc(redesignDesc);
		ArrayList<String> arr= lh5Bo.doSaveLh5andRef(hsiId, needRedesign, dbOperate,
				this.getSysUser(), lhHsilh5, lh5, jsonData, arryDeltTaskId,getComModelSeries());
		int cou=0;
	    if(arr!=null&&arr.size()>0){//如果是否转移区域发生变化 更新Za7表的状态
	    	String[] areaId=null;
			for(int i =0;i<arr.size();i++){
				areaId = arr.get(i).split(",");
				for(String str : areaId){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(), str);
				}
			}
			cou++;
	    }
	    if(cou>0){
	    	za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
	    }
	    if(arryDelCooId!=null){
	    	for(int i=0;i<arryDelCooId.length;i++){
	    		if (!BasicTypeUtils.isNullorBlank(arryDelCooId[i])){
	    			this.comCoordinationBo.delete((ComCoordination) this.comCoordinationBo.loadById(ComCoordination.class, arryDelCooId[i]));
	    		}
	    	}
	    }
	    //保存四个不同任务的协调单
   		saveCoordination(jsonGVI);
   		saveCoordination(jsonDET);
   		saveCoordination(jsonDIS);
   		saveCoordination(jsonFNC);
		writeToResponse("{'msg':'success'}");
		return null;
	}

	/**
	 * 保存msg-3关联的协调单数据
	 * @param json
	 */
	public void saveCoordination(String str ){
				str=str.replace("},{", ",");
		    	JSONArray jsonArray = JSONArray.fromObject(str);
		    	if(jsonArray.size()>0){
		    		ComCoordination comCoordination;
		    		JSONObject jsonObj = null;
		    		for(int m=0;m<jsonArray.size();m++){
		    			jsonObj = jsonArray.getJSONObject(m);
		    			String taskCode=jsonObj.getString("taskCode");
		    			TaskMsg taskMsg_3 = this.taskMsgBo.getTaskByTaskCode(this.getComModelSeries().getModelSeriesId(), taskCode);
		    			if(taskMsg_3.getNeedTransfer()==1){
		    				if(jsonObj.getString("comCoordinationId")!=null&&!"nul".equals(jsonObj.getString("comCoordinationId"))){
		    					comCoordination = (ComCoordination) this.comCoordinationBo.loadById(ComCoordination.class, jsonObj.getString("comCoordinationId"));
		    					comCoordinationBo.saveOrUpdateCoo(comCoordination,jsonObj.getString("type"), taskMsg_3.getTaskId(),
    							jsonObj.getString("comcoordinationCode"),jsonObj.getString("comcontent") , jsonObj.getString("comtheme"),
    							jsonObj.getString("comsendWg1"), ComacConstants.DB_UPDATE, jsonObj.getString("comreceiveWg1"),
    							jsonObj.getString("comreceiveArea"), jsonObj.getString("comsendUser1"),jsonObj.getString("comcreateDate") , jsonObj.getString("comreContent"), 
    							jsonObj.getString("comreceiveUser1"), jsonObj.getString("comreceiveDate"),jsonObj.getString("isReceive"),this.getComModelSeries(),1,"",this.getSysUser());
		    				}else{
		    					comCoordination=new ComCoordination();
		    					comCoordinationBo.saveOrUpdateCoo(comCoordination,jsonObj.getString("type"), taskMsg_3.getTaskId(),
    							jsonObj.getString("comcoordinationCode"),jsonObj.getString("comcontent") , jsonObj.getString("comtheme"),
    							jsonObj.getString("comsendWg1"), ComacConstants.DB_INSERT, jsonObj.getString("comreceiveWg1"),
    							jsonObj.getString("comreceiveArea"), jsonObj.getString("comsendUser1"),jsonObj.getString("comcreateDate") , "", 
    							jsonObj.getString("comreceiveUser1"), "",jsonObj.getString("isReceive"),this.getComModelSeries(),1,"",this.getSysUser());
		    				}
		    			}
		    		};
		    	};
	}
	

	/*
	 * 查找 msg_3Taskcode 是否在本机型下已近存在 @author chenyong createdate 2012-11-27
	 */
	public String verifyMsgTaskCode() {
		TaskMsg taskMsg_3 = this.taskMsgBo.getTaskByTaskCode(
				getComModelSeries().getModelSeriesId(), msgTaskCode);
		if (taskMsg_3 != null) {
			if(!taskMsg_3.getTaskId().equals(this.msg3TaskId)){
				writeToResponse(msgTaskCode);
			}
			return null;
		}
		return null;
	}

	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh5Bo getLh5Bo() {
		return lh5Bo;
	}

	public void setLh5Bo(ILh5Bo lh5Bo) {
		this.lh5Bo = lh5Bo;
	}

	public ILh4Bo getLh4Bo() {
		return lh4Bo;
	}

	public void setLh4Bo(ILh4Bo lh4Bo) {
		this.lh4Bo = lh4Bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public Lh5 getLh5() {
		return lh5;
	}

	public void setLh5(Lh5 lh5) {
		this.lh5 = lh5;
	}

	public String getGviAvl() {
		return gviAvl;
	}

	public void setGviAvl(String gviAvl) {
		this.gviAvl = gviAvl;
	}

	public String getGviDesc() {
		return gviDesc;
	}

	public void setGviDesc(String gviDesc) {
		this.gviDesc = gviDesc;
	}

	public Integer getDetAvl() {
		return detAvl;
	}

	public void setDetAvl(Integer detAvl) {
		this.detAvl = detAvl;
	}

	public String getDetDesc() {
		return detDesc;
	}

	public void setDetDesc(String detDesc) {
		this.detDesc = detDesc;
	}

	public Integer getFncAvl() {
		return fncAvl;
	}

	public void setFncAvl(Integer fncAvl) {
		this.fncAvl = fncAvl;
	}

	public String getFncDesc() {
		return fncDesc;
	}

	public void setFncDesc(String fncDesc) {
		this.fncDesc = fncDesc;
	}

	public Integer getDisAvl() {
		return disAvl;
	}

	public void setDisAvl(Integer disAvl) {
		this.disAvl = disAvl;
	}

	public String getDisDesc() {
		return disDesc;
	}

	public void setDisDesc(String disDesc) {
		this.disDesc = disDesc;
	}

	public String getNeedRedesign() {
		return needRedesign;
	}

	public void setNeedRedesign(String needRedesign) {
		this.needRedesign = needRedesign;
	}

	public String getRedesignDesc() {
		return redesignDesc;
	}

	public void setRedesignDesc(String redesignDesc) {
		this.redesignDesc = redesignDesc;
	}

	public Lh4 getLh4() {
		return lh4;
	}

	public void setLh4(Lh4 lh4) {
		this.lh4 = lh4;
	}

	public String getLh5Id() {
		return lh5Id;
	}

	public void setLh5Id(String lh5Id) {
		this.lh5Id = lh5Id;
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

	public LhMain getLhHsi() {
		return lhHsi;
	}

	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}

	public List<CusInterval> getListCusList() {
		return listCusList;
	}

	public void setListCusList(List<CusInterval> listCusList) {
		this.listCusList = listCusList;
	}

	public String getResultLh4Value() {
		return resultLh4Value;
	}

	public void setResultLh4Value(String resultLh4Value) {
		this.resultLh4Value = resultLh4Value;
	}

	public String[] getArryDeltTaskId() {
		return arryDeltTaskId;
	}

	public void setArryDeltTaskId(String[] arryDeltTaskId) {
		this.arryDeltTaskId = arryDeltTaskId;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getMsgTaskCode() {
		return msgTaskCode;
	}

	public void setMsgTaskCode(String msgTaskCode) {
		this.msgTaskCode = msgTaskCode;
	}

	public String getMsg3TaskId() {
		return msg3TaskId;
	}

	public void setMsg3TaskId(String msg3TaskId) {
		this.msg3TaskId = msg3TaskId;
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

	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public String getJsonGVI() {
		return jsonGVI;
	}

	public void setJsonGVI(String jsonGVI) {
		this.jsonGVI = jsonGVI;
	}

	public String getJsonDET() {
		return jsonDET;
	}

	public void setJsonDET(String jsonDET) {
		this.jsonDET = jsonDET;
	}

	public String getJsonFNC() {
		return jsonFNC;
	}

	public void setJsonFNC(String jsonFNC) {
		this.jsonFNC = jsonFNC;
	}

	public String getJsonDIS() {
		return jsonDIS;
	}

	public void setJsonDIS(String jsonDIS) {
		this.jsonDIS = jsonDIS;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String[] getArryDelCooId() {
		return arryDelCooId;
	}

	public void setArryDelCooId(String[] arryDelCooId) {
		this.arryDelCooId = arryDelCooId;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
