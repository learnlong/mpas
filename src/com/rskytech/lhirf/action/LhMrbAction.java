package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILhMrbBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.struct.bo.IS3Bo;

@SuppressWarnings({"unchecked","rawtypes"})
public class LhMrbAction extends BaseAction {

	/**  Lhrif mrb 维护 的action
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private ILhMrbBo lhMrbBo;
	private IS3Bo s3Bo;
	
	protected String jsonDataMsg; // 前台传递json字符串
	//参数
	private String ipvOpvpOpve;
	private String taskType;
	private String taskInterval;
	private String mrbCode;
	private Integer  isMaintain;
	private String deleteMrbId;  ///待删除的Mrb Id
	private String mrbCodeVerifyId ;
	public static final String LH5 = "LH5";
	private IComAreaBo comAreaBo;
	
	/**
	 * 初始化加载L/Hirf  MRB 页面信息
	 */
	public String init(){
		ComUser thisUser = this.getSysUser();
		if ( null == thisUser) {
			return SUCCESS;
		}
		isMaintain = ComacConstants.YES;
		return SUCCESS;
	}
	
	/**
	 * 初始化加载MSG-3 任务 store数据源
	 */
	public String loadlhMsgTask() throws Exception{
		String nowUserId =  this.getSysUser().getUserId() ;
		String modelSeriesId = getComModelSeries().getModelSeriesId() ;
		  
		  JSONObject json = new JSONObject();
			List<HashMap> listJsonFV = new ArrayList<HashMap>();
		  if(nowUserId != null && modelSeriesId != null){
			  List listmsg = lhMrbBo.getLhMsgListBythreeUserId(modelSeriesId, nowUserId, taskType, ipvOpvpOpve,taskInterval);
			 if(listmsg != null){
				  for (int i = 0; i < listmsg.size(); i++){
						Object[] ob = (Object[]) listmsg.get(i);
						HashMap jsonFeildList = new HashMap();
						
						if (ob[0] == null){
							jsonFeildList.put("areaCode", "");
						} else {
							jsonFeildList.put("areaCode", ob[0]);
						}
						
						if (ob[1] == null){
							jsonFeildList.put("hsiCode", "");
						} else {
							jsonFeildList.put("hsiCode", ob[1]);
						}
						
						if(ob[2] == null){
							jsonFeildList.put("mrbId", "");
						}else{
							   jsonFeildList.put("mrbId", ob[2]);
						}
						
						if(ob[3] == null){
							jsonFeildList.put("mrbTaskCode", "");
						}else{
							jsonFeildList.put("mrbTaskCode", ob[3]); 
						}
						if(ob[4] == null){
							jsonFeildList.put("taskId", "") ;
						}else{
							jsonFeildList.put("taskId", ob[4]) ;
						}
						if(ob[5] == null){
							jsonFeildList.put("taskCode","") ;
						}else{
							jsonFeildList.put("taskCode", ob[5]) ;
						}
						if(null == ob[6]){
					    	jsonFeildList.put("taskType", "") ;
					    }else {
					    	jsonFeildList.put("taskType", ob[6]);
					    }
						
					    if(null == ob[7]){
						     jsonFeildList.put("ipvOpvpOpve", "") ;
						 }else{
						     jsonFeildList.put("ipvOpvpOpve", ob[7]);
					    }
					    
					    if(null ==ob[8]){
					    	jsonFeildList.put("taskInterval", "");
					    }else{
					    	jsonFeildList.put("taskInterval", ob[8]);
					    }
					    
					    if(null ==ob[9]){
					    	jsonFeildList.put("reachWay", "");
					    }else{
					    	jsonFeildList.put("reachWay",ob[9]);
					    }
					  
					    if(null ==ob[10]){
					    	jsonFeildList.put("taskDesc", "");
					    }else{
					    	jsonFeildList.put("taskDesc", ob[10]);
					    }
					    
					   if(ob[11] == null){
						   jsonFeildList.put("lheff", "");
					   }else{
						   jsonFeildList.put("lheff",ob[11]);
					   }
						
				    listJsonFV.add(jsonFeildList);   
			 }
				    
		  }
		json.element("lhMsg", listJsonFV);
        writeToResponse(json.toString()); 
	  }
		return null ;
	}
   /*
    *  mrb 数据初始化 加载
    */
	/*
	 * 初始化加载MRB任务 store数据源
	 */
	public String  loadMrbTask(){
		String nowUserId =  this.getSysUser().getUserId() ;
		String modelSeriesId = getComModelSeries().getModelSeriesId() ;
		 JSONObject json = new JSONObject();
			List<HashMap> listJsonFV = new ArrayList<HashMap>();
		 List listmsg = lhMrbBo.getLhMsgListBythreeUserId(modelSeriesId, nowUserId, null, null,null);
		 if(listmsg != null){
			   String[] ids = new String[listmsg.size()];
			  for (int i = 0; i < listmsg.size(); i++){
					Object[] obb = (Object[]) listmsg.get(i);
					if(obb[12] != null){
						ids[i]= obb[12].toString();
					}
			  }
			  Set mrbIdhas = new HashSet();
			  Object[] ids22 = new String[listmsg.size()];
				if (ids.length > 0) {
					for (int i = 0; i < ids.length; i++) {
						mrbIdhas.add(ids[i]);
					}
				}
				Object[] in = mrbIdhas.toArray();
				for (int i = 0; i < in.length; i++) {
					ids22[i]= in[i];
				}
				String areaCode = "";
			  for (int i = 0; i < ids22.length; i++){
					if(ids22[i] != null){
						TaskMrb  mrb =(TaskMrb) lhMrbBo.loadById(TaskMrb.class, ids22[i].toString());
						if(mrb != null && ComacConstants.YES.equals(mrb.getValidFlag())){
							HashMap jsonFeildList = new HashMap();
					    	 jsonFeildList.put("mrbId",mrb.getMrbId());
					    	 jsonFeildList.put("mrbCode",mrb.getMrbCode());
					    	 jsonFeildList.put("taskType",mrb.getTaskType());
					    	 jsonFeildList.put("ipvOpvpOpve",mrb.getAnyContent());
					    	 jsonFeildList.put("taskInterval",mrb.getTaskIntervalOriginal());
					    	 jsonFeildList.put("reachWay",mrb.getReachWay());
					    	 jsonFeildList.put("taskDesc",mrb.getTaskDesc());
					    	 if(mrb.getOwnArea()!=null){
					    		areaCode = comAreaBo.getAreaCodeByAreaId(mrb.getOwnArea());
					    	 }
					    	 jsonFeildList.put("mrbownArea",areaCode);
					    	 jsonFeildList.put("effectiveness",mrb.getEffectiveness());
					    	 listJsonFV.add(jsonFeildList); 
						}
					}
			  }
		 }
		  json.element("mrb", listJsonFV);
	      writeToResponse(json.toString()); 
		return null ;
	}
	
	
	/**
	 * msg  填写 mrb编号后  验证
	 */
	public String  verifyMrbCode(){
		JSONObject json = new JSONObject();
		TaskMrb mrb = lhMrbBo.getMrbByMrbCode(getComModelSeries().getModelSeriesId(), mrbCode);
		if (null != mrb) { // 存在 条件下 再次判断是否 lhrif 本系统的
			if(!mrb.getMrbId().equals(mrbCodeVerifyId)){
				json.put("message", false);
				writeToResponse(json.toString());
				return null;
			}
		} 
		json.put("message", true);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 得到页面 lhrif Mrb维护页面取 mrbcode的combox
	 *  @author wangyueli 
	 * 	 
	 * */
	 public String getmrbCombox(){
			String nowUserId = this.getSysUser().getUserId();
			String modelSeriesId = getComModelSeries().getModelSeriesId();
			JSONObject json = new JSONObject();
			List<HashMap> listJsonFV = new ArrayList<HashMap>();
			List listmsg = lhMrbBo.getLhMsgListBythreeUserId(modelSeriesId,
					nowUserId, null, null, null);
			if (listmsg != null) {
				String[] ids = new String[listmsg.size()];
				for (int i = 0; i < listmsg.size(); i++) {
					Object[] obb = (Object[]) listmsg.get(i);
					if (obb[12] != null) {
						ids[i] = obb[12].toString();
					}
				}
				Set mrbIdhas = new HashSet();
				Object[] ids22 = new String[listmsg.size()];
				if (ids.length > 0) {
					for (int i = 0; i < ids.length; i++) {
						mrbIdhas.add(ids[i]);
					}
				}
				Object[] in = mrbIdhas.toArray();
				for (int i = 0; i < in.length; i++) {
					ids22[i] = in[i];
				}
				for (int i = 0; i < ids22.length; i++) {
					if (ids22[i] != null) {
						TaskMrb mrb = (TaskMrb) lhMrbBo.loadById(TaskMrb.class,
								ids22[i].toString());
						if (mrb != null
								&& ComacConstants.YES.equals(mrb.getValidFlag())) {
							HashMap jsonFeildList = new HashMap();
							jsonFeildList.put("mrbId", mrb.getMrbId());
							jsonFeildList.put("mrbCode", mrb.getMrbCode());
							jsonFeildList.put("taskType", mrb.getTaskType());
							jsonFeildList.put("ipvOpvpOpve", mrb.getAnyContent());
							jsonFeildList.put("taskInterval", mrb
									.getTaskIntervalOriginal());
							listJsonFV.add(jsonFeildList);
						}
					}
				}
			}
			json.element("mrbCombox", listJsonFV);
			writeToResponse(json.toString());
			return null;
	 }
	
	    /*
		 * 保存页面的MSG-3 和MRB的变动
		 *  @author wangyueli 
		 *  createdate 2012-09-21
		 */
	public String saveMrbMsg(){
		JSONObject json = null;
	   this.lhMrbBo.doSaveMrbMpdAndMsg(jsonData, jsonDataMsg, deleteMrbId,this.getSysUser(),getComModelSeries());
		json = this.putJsonOKFlag(json, true);
		json.put("msg", "success");
		writeToResponse(json.toString());
		return null;
	}

	public ILhMrbBo getLhMrbBo() {
		return lhMrbBo;
	}

	public void setLhMrbBo(ILhMrbBo lhMrbBo) {
		this.lhMrbBo = lhMrbBo;
	}

	public IS3Bo getS3Bo() {
		return s3Bo;
	}

	public void setS3Bo(IS3Bo s3Bo) {
		this.s3Bo = s3Bo;
	}

	public String getJsonDataMsg() {
		return jsonDataMsg;
	}

	public void setJsonDataMsg(String jsonDataMsg) {
		this.jsonDataMsg = jsonDataMsg;
	}

	public String getIpvOpvpOpve() {
		return ipvOpvpOpve;
	}

	public void setIpvOpvpOpve(String ipvOpvpOpve) {
		this.ipvOpvpOpve = ipvOpvpOpve;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskInterval() {
		return taskInterval;
	}

	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}

	public String getMrbCode() {
		return mrbCode;
	}

	public void setMrbCode(String mrbCode) {
		this.mrbCode = mrbCode;
	}

	public Integer getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(Integer isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getDeleteMrbId() {
		return deleteMrbId;
	}

	public void setDeleteMrbId(String deleteMrbId) {
		this.deleteMrbId = deleteMrbId;
	}

	public String getMrbCodeVerifyId() {
		return mrbCodeVerifyId;
	}

	public void setMrbCodeVerifyId(String mrbCodeVerifyId) {
		this.mrbCodeVerifyId = mrbCodeVerifyId;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	
}
