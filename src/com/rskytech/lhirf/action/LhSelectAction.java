package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILhSelectBo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;
import com.rskytech.task.bo.ITaskMsgDetailBo;


public class LhSelectAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private IComAreaBo comAreaBo;
	private ILhMainBo lhMainBo;
	private ILhSelectBo lhSelectBo;
	
	private String areaId;
	private String areaCode;
	private String hsiCode;
	private String hsiName;
	private String deleteHsiId;
	private String isMaintain;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;

	
	/**
	 * 初始化加载lhSelect页面信息
	 */
	public String init() {

		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		ComArea comArea = (ComArea) comAreaBo.loadById(ComArea.class, areaId);
		List<LhMain> listLh = lhMainBo.getLhMainList(areaId,getComModelSeries().getModelSeriesId(),null, null);
		boolean flag =true;
		if (listLh != null&&listLh.size()>0) {
			for(LhMain lhMain : listLh){
				if (!ComacConstants.ANALYZE_STATUS_APPROVED.equals(lhMain.getStatus())||ComacConstants.ANALYZE_STATUS_HOLD.equals(lhMain.getStatus())){
					flag = false;
					break;
				}
			}
			if(flag){
				isMaintain = ComacConstants.NO.toString();
			}
		}
		this.areaCode = comArea.getAreaCode();
		return SUCCESS;
	}
	
	/**
	 * 初始化HSI store 数据源页面Lh0 
	 * @return 初始化LH_HSI表数据
	 * 
	 */
	public String sesrchLhHsiList() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<LhMain> lhMainList = lhMainBo.getLhMainList(areaId,getComModelSeries().getModelSeriesId(),hsiCode, hsiName);
		if (lhMainList != null&&lhMainList.size()>0) {
			for(LhMain lhMain:lhMainList){
				listJsonFV.add(jsonFieldValue(lhMain));
			}
		}
		json.element("lhSelect", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	@SuppressWarnings("unchecked")
	private HashMap jsonFieldValue(LhMain lhMain){
		 	HashMap jsonFeildList=new HashMap();
		 	jsonFeildList.put("hsiId",lhMain.getHsiId());
	        jsonFeildList.put("ataCode",lhMain.getAtaCode());
	        jsonFeildList.put("hsiCode",lhMain.getHsiCode());
	        jsonFeildList.put("hsiName",lhMain.getHsiName());
	        jsonFeildList.put("lhCompName",lhMain.getLhCompName());
	        jsonFeildList.put("ipvOpvpOpve",lhMain.getIpvOpvpOpve());
	        jsonFeildList.put("refHsiCode",lhMain.getRefHsiCode());
	        jsonFeildList.put("status", lhMain.getStatus());
	       if(!BasicTypeUtils.isNullorBlank(lhMain.getAnaUser())){
	    	 ComUser user =(ComUser) this.lhMainBo.loadById(ComUser.class, lhMain.getAnaUser());
				if(user != null){
					jsonFeildList.put("anaUserName",user.getUserName());
				}
			}else{
				jsonFeildList.put("anaUserName",  "");
			}
	       if((getSysUser().getUserId()).equals(lhMain.getAnaUser())){
	    	   jsonFeildList.put("isAuthorHsi",  "1");
	        }else{
	        	jsonFeildList.put("isAuthorHsi",  "0");
	        }
		return jsonFeildList;
	}
	
	/**
	 * 保存 HSI维护 页面 
	 * @return 
	 */
	public String saveLhHsiList(){ 
		boolean falg = false;
		if(jsonData != null){
			 falg =lhSelectBo.saveOrUpdateLhHsi( this.getSysUser() , areaId,  jsonData,getComModelSeries());
		}
		JSONObject	json = putJsonOKFlag(null,falg);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 删除hsi页面选择数据 
	 * 在BO中 1. 判断 lh_hsi表中是否产生任务 MSG-3 在BO中
	 *  2.mrb 是否只有这一个任务 使用 mrb,mpd 
	 *  @return
	 */
	public String deleteLhSelectHsi(){
		if (!BasicTypeUtils.isNullorBlank(deleteHsiId)){
			LhMain  delHsi = (LhMain) lhMainBo.loadById(LhMain.class, deleteHsiId);
		  if(delHsi == null){
			  	JSONObject json = putJsonOKFlag(null,false);
				writeToResponse(json.toString());
				return null; 
		  }else if(delHsi != null ){
			  	ArrayList<String> array = lhSelectBo.deleteHsi(delHsi,this.getSysUser(),getComModelSeries().getModelSeriesId());
			  	if(array!=null&&array.size()>0){
					for(String areaId : array){
						String[] arr = areaId.split(",");
						for(String string : arr){
							taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
						}
					}
					za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
				}
				JSONObject	json = putJsonOKFlag(null,true);
				writeToResponse(json.toString());
				return null ; 
		  }
		}
		return null;
	}


	/**
	 * 页面认领HSI, 需替换的MSG-3的任务表 创建人 MRB 中的创建人
	 * @return 
	 */
		public String getReplaceHsi(){
		lhSelectBo.doReplaceHsi(deleteHsiId, this.getSysUser(),getComModelSeries());
		JSONObject	json = putJsonOKFlag(null,true);
		writeToResponse(json.toString());
		return null;
	}

		/**
		 * 检查hsi编号是否存在
		 * @return
		 */
		public String verifyHsiCodeExist(){
			if(lhSelectBo.verifyHsiCodeExist(hsiCode,getComModelSeries().getModelSeriesId())){
				writeToResponse(hsiCode);
			}
			return null;
		}
		
		
		
		public IComAreaBo getComAreaBo() {
			return comAreaBo;
		}

		public void setComAreaBo(IComAreaBo comAreaBo) {
			this.comAreaBo = comAreaBo;
		}

		public ILhMainBo getLhMainBo() {
			return lhMainBo;
		}

		public void setLhMainBo(ILhMainBo lhMainBo) {
			this.lhMainBo = lhMainBo;
		}

		public ILhSelectBo getLhSelectBo() {
			return lhSelectBo;
		}

		public void setLhSelectBo(ILhSelectBo lhSelectBo) {
			this.lhSelectBo = lhSelectBo;
		}

		public String getAreaId() {
			return areaId;
		}

		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}

		public String getAreaCode() {
			return areaCode;
		}

		public void setAreaCode(String areaCode) {
			this.areaCode = areaCode;
		}

		public String getHsiCode() {
			return hsiCode;
		}

		public void setHsiCode(String hsiCode) {
			this.hsiCode = hsiCode;
		}

		public String getHsiName() {
			return hsiName;
		}

		public void setHsiName(String hsiName) {
			this.hsiName = hsiName;
		}

		public String getDeleteHsiId() {
			return deleteHsiId;
		}

		public void setDeleteHsiId(String deleteHsiId) {
			this.deleteHsiId = deleteHsiId;
		}

		public String getIsMaintain() {
			return isMaintain;
		}

		public void setIsMaintain(String isMaintain) {
			this.isMaintain = isMaintain;
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