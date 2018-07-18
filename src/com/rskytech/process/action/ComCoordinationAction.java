package com.rskytech.process.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.user.bo.IComProfessionBo;

public class ComCoordinationAction extends BaseAction {
	private static final long serialVersionUID = -1329452673431902676L;
	private String comcontent;
	private String comCoordinationId;
	private String comcoordinationCode;
	private String comcoordinationName;
	private String comtheme;
	private String comsendWg;
	private String comreceiveWg;
	private String comreceiveArea;
	private String comsendUser;
	private String comcreateDate;
	private String comreContent;
	private String comreceiveUser;
	private String comreceiveDate;
	private String comTaskId;
	private String type;
	private IComCoordinationBo comCoordinationBo;
	private String isReceive;
	private String comS6OutOrIn;
	private IComAreaBo comAreaBo;
	private IComProfessionBo comProfessionBo;
	private String professionId;
	
	
	public String getComS6OutOrIn() {
		return comS6OutOrIn;
	}
	public void setComS6OutOrIn(String comS6OutOrIn) {
		this.comS6OutOrIn = comS6OutOrIn;
	}
	public String getComCoordinationId() {
		return comCoordinationId;
	}
	public void setComCoordinationId(String comCoordinationId) {
		this.comCoordinationId = comCoordinationId;
	}
	public String getIsReceive() {
		return isReceive;
	}
	public void setIsReceive(String isReceive) {
		this.isReceive = isReceive;
	}
	public String getComTaskId() {
		return comTaskId;
	}
	public void setComTaskId(String comTaskId) {
		this.comTaskId = comTaskId;
	}
	public String getComcontent() {
		return comcontent;
	}
	public void setComcontent(String comcontent) {
		this.comcontent = comcontent;
	}
	public String getComcoordinationCode() {
		return comcoordinationCode;
	}
	public void setComcoordinationCode(String comcoordinationCode) {
		this.comcoordinationCode = comcoordinationCode;
	}
	public String getComcoordinationName() {
		return comcoordinationName;
	}
	public void setComcoordinationName(String comcoordinationName) {
		this.comcoordinationName = comcoordinationName;
	}
	public String getComtheme() {
		return comtheme;
	}
	public void setComtheme(String comtheme) {
		this.comtheme = comtheme;
	}
	public String getComsendWg() {
		return comsendWg;
	}
	public void setComsendWg(String comsendWg) {
		this.comsendWg = comsendWg;
	}
	public String getComreceiveWg() {
		return comreceiveWg;
	}
	public void setComreceiveWg(String comreceiveWg) {
		this.comreceiveWg = comreceiveWg;
	}
	public String getComreceiveArea() {
		return comreceiveArea;
	}
	public void setComreceiveArea(String comreceiveArea) {
		this.comreceiveArea = comreceiveArea;
	}
	public String getComsendUser() {
		return comsendUser;
	}
	public void setComsendUser(String comsendUser) {
		this.comsendUser = comsendUser;
	}
	public String getComcreateDate() {
		return comcreateDate;
	}
	public void setComcreateDate(String comcreateDate) {
		this.comcreateDate = comcreateDate;
	}
	public String getComreContent() {
		return comreContent;
	}
	public void setComreContent(String comreContent) {
		this.comreContent = comreContent;
	}
	public String getComreceiveUser() {
		return comreceiveUser;
	}
	public void setComreceiveUser(String comreceiveUser) {
		this.comreceiveUser = comreceiveUser;
	}
	public String getComreceiveDate() {
		return comreceiveDate;
	}
	public void setComreceiveDate(String comreceiveDate) {
		this.comreceiveDate = comreceiveDate;
	}
	
	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}
	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String init() {
		return SUCCESS;
	}
	
	/**
	 * 查询所有的协调单数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void loadCoordinationData(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		String nowUserId= this.getSysUser().getUserId();
		int flag =ComacConstants.VALIDFLAG_YES;
		List<ComCoordination> coorList =  this.comCoordinationBo.findCoordinationNeedList(nowUserId,modelSeriesId,flag,page,type,isReceive);	
		JSONObject json = new JSONObject();		
		List<HashMap> listJson = new ArrayList<HashMap>();	
		String type ="";	
		int permission =0;
		String isreceive = "";
		int i=0;
		for(ComCoordination cc : coorList){
				HashMap jsonFeildList = new HashMap();
				String areaCode = this.comAreaBo.getAreaCodeByAreaId(cc.getReceiveArea());
				if (comreceiveArea != null && !"".equals(comreceiveArea)) {
					if(cc.getReceiveArea()!=null){
						if(areaCode!=null){
							if(areaCode.contains(comreceiveArea)){
								jsonFeildList.put("receiveArea", areaCode);
							}else{
								i++;
								continue;
							}
						}else{
							if(cc.getReceiveArea().contains(comreceiveArea)){
								jsonFeildList.put("receiveArea", cc.getReceiveArea());
							}else{
								i++;
								continue;
							}	
						}
					}else{
						i++;
						continue;
					}
				}else{
					if(areaCode!=null){
						jsonFeildList.put("receiveArea", areaCode);
					}else{
						jsonFeildList.put("receiveArea", cc.getReceiveArea());
					}
				}
				if("3".equals(cc.getIsReceived())||"4".equals(cc.getIsReceived())){
					permission = 3;
					jsonFeildList.put("permission", permission);
				}else{
					if(!"2".equals(cc.getIsReceived())){						
						if(nowUserId.equals(cc.getSendUser())&&!nowUserId.equals(cc.getReceiveUser())){
							permission = 3;
							jsonFeildList.put("permission", permission);
						}else{							
							permission = 1;
							jsonFeildList.put("permission", permission);
						}
					}else{
						permission = 2;
						jsonFeildList.put("permission", permission);
					}
				}
					if("SYS_TO_Z".equals(cc.getType())){
							type = "系统分析";
					}
					if("STR_TO_Z".equals(cc.getType())||"STR_TO_SYS".equals(cc.getType())){
							type = "结构分析";
					}
					if("LH_TO_Z".equals(cc.getType())){
							type = "L/H分析";
					}				
					if("1".equals(cc.getIsReceived())){
						isreceive = "未审批";
					}
					if("3".equals(cc.getIsReceived())){
						isreceive = "审批未通过";
					}
					if("2".equals(cc.getIsReceived())){
						isreceive = "未提交";
					}
					if("4".equals(cc.getIsReceived())){
						isreceive = "审批通过";
					}
					jsonFeildList.put("isReceive", isreceive);
					jsonFeildList.put("permission", permission);
					jsonFeildList.put("type", type);
					jsonFeildList.put("id", cc.getCoordinationId());
					jsonFeildList.put("sendUser",this.comCoordinationBo.findUserNameByUserId(cc.getSendUser()));
					jsonFeildList.put("receiveUser",this.comCoordinationBo.findUserNameByUserId(cc.getReceiveUser()));					
					jsonFeildList.put("sendDate", cc.getSendDate());
					jsonFeildList.put("taskId", cc.getTaskId());				
					jsonFeildList.put("s6OutOrIn", cc.getS6OutOrIn());				
					listJson.add(jsonFeildList);
		     }
			json.element("coorData", listJson);
			json.element("total", this.page.getTotalCount()-i);
			writeToResponse(json.toString());
	}
	
	/**
	 * 保存协调单数据
	 */
	public void saveCoordination(){
		int flag = ComacConstants.VALIDFLAG_YES;
		ComCoordination comCoordination = new ComCoordination();
		String dbOperate;
		if(!"".equals(comCoordinationId)&&comCoordinationId!=null){
		dbOperate= ComacConstants.DB_UPDATE;
			comCoordination = (ComCoordination) this.comCoordinationBo.loadById(ComCoordination.class, comCoordinationId);
		}else{
			dbOperate= ComacConstants.DB_INSERT;	
		}
		this.comCoordinationBo.saveOrUpdateCoo(comCoordination,type, comTaskId,comcoordinationCode, comcontent, comtheme,
				   comsendWg, dbOperate, comreceiveWg, comreceiveArea, comsendUser, comcreateDate, comreContent, 
				   comreceiveUser, comreceiveDate,isReceive,this.getComModelSeries(),flag,comS6OutOrIn,this.getSysUser());
	   writeToResponse("{flag:true}");
	}
	/**
	 * 通过Taskid加载协调单数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String loadCoordination(){
		int flag =ComacConstants.VALIDFLAG_YES;
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<ComCoordination> coordinationList =  this.comCoordinationBo.findCoordinationByTaskId(comTaskId,modelSeriesId,flag);
		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();	
		String areaCode ="";
		if(coordinationList.size()>0){
			for(ComCoordination cc : coordinationList){
				HashMap jsonFeildList = new HashMap();
				if(!"2".equals(cc.getIsReceived())
					&&!this.getSysUser().getUserId().equals(cc.getReceiveUser())&&
					this.getSysUser().getUserId().equals(cc.getSendUser())){
						jsonFeildList.put("isreceive", "noPermission");	
					}else{
						jsonFeildList.put("isreceive", cc.getIsReceived());	
				}					
				jsonFeildList.put("comCoordinationId", cc.getCoordinationId());
				jsonFeildList.put("comTaskId", cc.getTaskId());
				jsonFeildList.put("comcoordinationCode", cc.getCoordinationCode());
				jsonFeildList.put("comcontent", cc.getSendContent());
				jsonFeildList.put("comtheme", cc.getTheme());
				jsonFeildList.put("comsendWgId", cc.getSendWorkgroup());
				jsonFeildList.put("comreceiveWgId", cc.getReceivedWorkgroup());	
				jsonFeildList.put("comsendWg",  this.comCoordinationBo. findProfessionNameById(cc.getSendWorkgroup()));
				jsonFeildList.put("comreceiveWg", this.comCoordinationBo. findProfessionNameById(cc.getReceivedWorkgroup()));	
				areaCode=comAreaBo.getAreaCodeByAreaId(cc.getReceiveArea());
				jsonFeildList.put("comreceiveArea", areaCode);
				jsonFeildList.put("comsendUserId", cc.getSendUser());
				jsonFeildList.put("comsendUser", this.comCoordinationBo.findUserNameByUserId(cc.getSendUser()));
				jsonFeildList.put("comcreateDate", cc.getSendDate());
				jsonFeildList.put("comreContent", cc.getReceiveContent());
				jsonFeildList.put("comreceiveUserId", cc.getReceiveUser());
				jsonFeildList.put("comreceiveUser", this.comCoordinationBo.findUserNameByUserId(cc.getReceiveUser()));
				jsonFeildList.put("comreceiveDate", cc.getReceiveDate());					
				jsonFeildList.put("type", cc.getType());	
				listJson.add(jsonFeildList);
			}
			json.element("coordination", listJson);
			writeToResponse(json.toString());
			return null;
		}else{
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("comCoordinationId", "nul");
			listJson.add(jsonFeildList);
			json.element("coordination", listJson);
			writeToResponse(json.toString());
			return null;
		}
	}
	/**
	 * 通过SsiId和S6内外加载协调单数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String loadStrToSysCoordination(){
		int flag =ComacConstants.VALIDFLAG_YES;
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<ComCoordination> coordinationList =  this.comCoordinationBo.findCoordinationById(comTaskId,comS6OutOrIn,modelSeriesId,flag);
		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();	
		if(coordinationList.size()>0){
			for(ComCoordination cc : coordinationList){
				HashMap jsonFeildList = new HashMap();
				if(!"2".equals(cc.getIsReceived())
					&&!this.getSysUser().getUserId().equals(cc.getReceiveUser())&&
					this.getSysUser().getUserId().equals(cc.getSendUser())){
						jsonFeildList.put("isreceive", "noPermission");	
					}else{
						jsonFeildList.put("isreceive", cc.getIsReceived());	
				}					
				jsonFeildList.put("comCoordinationId", cc.getCoordinationId());
				jsonFeildList.put("comTaskId", cc.getTaskId());
				jsonFeildList.put("comS6OutOrIn", cc.getS6OutOrIn());
				jsonFeildList.put("comcoordinationCode", cc.getCoordinationCode());
				jsonFeildList.put("comcontent", cc.getSendContent());
				jsonFeildList.put("comtheme", cc.getTheme());
				jsonFeildList.put("comsendWgId", cc.getSendWorkgroup());
				jsonFeildList.put("comreceiveWgId", cc.getReceivedWorkgroup());	
				jsonFeildList.put("comsendWg",  this.comCoordinationBo. findProfessionNameById(cc.getSendWorkgroup()));
				jsonFeildList.put("comreceiveWg", this.comCoordinationBo. findProfessionNameById(cc.getReceivedWorkgroup()));	
				jsonFeildList.put("comreceiveArea", cc.getReceiveArea());
				jsonFeildList.put("comsendUserId", cc.getSendUser());
				jsonFeildList.put("comsendUser", this.comCoordinationBo.findUserNameByUserId(cc.getSendUser()));
				jsonFeildList.put("comcreateDate", cc.getSendDate());
				jsonFeildList.put("comreContent", cc.getReceiveContent());
				jsonFeildList.put("comreceiveUserId", cc.getReceiveUser());
				jsonFeildList.put("comreceiveUser", this.comCoordinationBo.findUserNameByUserId(cc.getReceiveUser()));
				jsonFeildList.put("comreceiveDate", cc.getReceiveDate());					
				jsonFeildList.put("type", cc.getType());	
				listJson.add(jsonFeildList);
			}
			json.element("coordination", listJson);
			writeToResponse(json.toString());
			return null;
		}else{
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("comCoordinationId", "nul");
			listJson.add(jsonFeildList);
			json.element("coordination", listJson);
			writeToResponse(json.toString());
			return null;
		}
	}
	
	/**
	 * 根据用户ID得到全部专业组
	 */
	@SuppressWarnings("unchecked")
	public String getProfessionByUserId(){
		List<Object[]> lst = comProfessionBo.getProsByUserId(getSysUser().getUserId());
		List<HashMap<String, Object>> listJsonFV = new ArrayList<HashMap<String, Object>>();
		if(lst!=null){
			for(Object[] obj : lst){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("professionId", obj[0]);
				hm.put("professionName",  obj[2]);
				listJsonFV.add(hm);
			}
		}
		JSONObject json = new JSONObject();
		json.put("ownProfession", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到专业室列表
	 */
	public String jsonLoadAllProfession(){
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJsonFV = new ArrayList<HashMap<String, Object>>();
		List<Object[]> lst = this.comCoordinationBo.getProfessionIdByAreaCode(this.getComreceiveArea(),this.getComModelSeries().getModelSeriesId());
		if (lst != null) {
			for (Object[] obj : lst) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("comProfessionId", obj[0]);
				hm.put("comProfessionName",  obj[1]);
				listJsonFV.add(hm);
			}
		}
		json.element("comProfession", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 根据专业室Id查询该专业室下的所有用户
	 */
	public String loadUserByProfessonId(){
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJsonFV = new ArrayList<HashMap<String, Object>>();
		List<Object[]> lst = this.comCoordinationBo.getUserByProfessonId(professionId);
		if (lst != null) {
			for (Object[] obj : lst) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", obj[0]);
				hm.put("userName",  obj[1]);
				listJsonFV.add(hm);
			}
		}
		json.element("comUsers", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	public IComProfessionBo getComProfessionBo() {
		return comProfessionBo;
	}
	public void setComProfessionBo(IComProfessionBo comProfessionBo) {
		this.comProfessionBo = comProfessionBo;
	}
	public String getProfessionId() {
		return professionId;
	}
	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}
	
}
