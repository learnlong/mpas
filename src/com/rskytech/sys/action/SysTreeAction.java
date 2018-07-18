package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M4;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.ISysTreeBo;
import com.rskytech.sys.dao.IM3Dao;
import com.rskytech.task.bo.ITaskMsgBo;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class SysTreeAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	private ISysTreeBo sysTreeBo;
	private IM2Bo m2Bo;
	private IM3Bo m3Bo;
	private IM13Bo m13Bo;	
	private IM4Bo m4Bo;
	private ITaskMsgBo taskMsgBo;
	private String cid;
	private String msiId;		
	private String id;//区域树的节点ID
	private String type;
	private String searchType;
	private IM3Dao m3Dao;
	
	/**
	 * 初始化页面
	 * @author 张建民
	 * @createdate 2012-8-27
	 */
	public String init(){
		return SUCCESS;
	}
	
	public void getAtaOrMsiTree(){
		JSONArray json = new JSONArray();
		json.addAll(sysTreeBo.searchSubAtaOrMsiTreeList(this.getSysUser(), id, getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
	}
	
	public void getAtaOrMsiGrid(){
		JSONObject json = new JSONObject();
		json.element("ata", sysTreeBo.searchMyMaintainList(this.getSysUser(), getComModelSeries().getModelSeriesId(), searchType));
		writeToResponse(json.toString());
	}
	
	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月15日 上午11:18:09
	* @throws
	 */
	public String openAnalysisStatus(){
		JSONObject json = new JSONObject();
		boolean flag = sysTreeBo.openAnalysisStatus(msiId);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * M2中的树
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	public String m2Tree(){
		List<M13> list = m13Bo.getM13ListByMsiId(this.msiId);
		List<M13F> listM13f;
		List<HashMap> listJsonFV = new ArrayList();
		Set<M13C> setM13c;
		for (M13 m13 : list) {
			listM13f = m13Bo.getM13fListByM13Id(m13.getM13Id());
			for (M13F m13f : listM13f) {
				setM13c = m13f.getM13Cs();
				if(setM13c!=null&&setM13c.size()==1){
					M13C m13c = setM13c.iterator().next();
					if(m13c.getIsRef()!=null&&m13c.getIsRef()==1){
						continue;
					}
				}
				if(!BasicTypeUtils.isNullorBlank(m13f.getEffectCode())){
					HashMap hm = new HashMap();
					M2 m2 =  m2Bo.getM2ByM13fId(m13f.getM13fId());
					if(m2 == null){
						hm.put("text", m13f.getEffectCode());
					}else{
						hm.put("text", "<font color='red'><b>" + m13f.getEffectCode() + "</b></font>");
					}
					hm.put("id", m13f.getM13fId());
					hm.put("showtext", m13f.getEffectCode());
					hm.put("leaf", "true");     
					listJsonFV.add(hm);
				}
			}
		}
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	/**
	 * M3的树
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22 
	 */
   public String m3Tree(){
	   List<HashMap> listJsonFV = new ArrayList();
	   if (cid == null || "0".equals(cid)){
	      List<M13F> m13fList = this.m13Bo.getM13fListByMsiId(msiId);
	      for (M13F m13f : m13fList) {
		    	int count =0;
	    	  	Set<M13C> m13cSet = m13f.getM13Cs();
	    	  	for(M13C m13c : m13cSet){
	    	  		if(!ComacConstants.YES.equals(m13c.getIsRef())){
	    	  			count++;
	 			   }
	    	  	}
	    	  	if(count>0){
	    	  		HashMap hm = new HashMap();
	    	  		hm.put("id","-"+ m13f.getM13fId());
	    	  		hm.put("text", m13f.getEffectCode());
	    	  		listJsonFV.add(hm);
	    	  	}
	          }
	   }else{
		   cid = cid.substring(1);
		   List<M13C> m13cList  =this.m13Bo.getM13cListByM13FId(cid);
		   M2 m2 =this.m2Bo.getM2ByM13fId(cid);
		   String result;
			String m2Id;
		   for (M13C m13c : m13cList) {
			   if(ComacConstants.YES.equals(m13c.getIsRef())){
				   //如果已参考其他MSI，不需分析M3
				   continue;
			   }
			   if(m2 == null){
				   result = "";
					m2Id = "";
			   }else{
				   result = m2.getFailureCauseType().toString();
				   m2Id = m2.getM2Id().toString(); 
			   }
				HashMap hm = new HashMap(); 
				M3 m3 = this.m3Bo.getM3ByM13cId(m13c.getM13cId());
				if (m3 == null) {
					hm.put("text", m13c.getCauseCode() + getTreeAtaCode(m13c.getMsetfId()));
				} else {
					hm.put("text", "<font color='red'><b>"+ m13c.getCauseCode() + getTreeAtaCode(m13c.getMsetfId()) + "</b></font>");
				}
			   
				hm.put("code",m13c.getCauseCode());
				hm.put("id", m13c.getM13cId());
				hm.put("m2Id", m2Id);
				hm.put("result", result);
				hm.put("leaf", "true");
				listJsonFV.add(hm);
		}
		   
	   }
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
	   return null;
   }
   
   public String getTreeAtaCode(String mSetFId){
	   String s = "select d.ata_code from m_set_f b, m_set c, com_ata d " +
				   " where b.mset_id = c.mset_id " +
				   "   and c.ata_id = d.ata_id " +
				   " and b.msetf_id = '" + mSetFId + "'";
	   List<Object> list = m3Dao.executeQueryBySql(s);
	   if (list != null && list.size() > 0){
		   return "(" + list.get(0).toString() + ")";
	   }
	   return "";
   }
	/**
	 * M4的树
	 * @return
	 * @author chendexu
	 * createdate 2012-08-30
	 */
public String m4Tree(){
	  String modelSeriesId = getComModelSeries().getModelSeriesId();
	List<TaskMsg> list = this.taskMsgBo.getTaskMsgListByMainId(modelSeriesId,msiId,ComacConstants.SYSTEM_CODE,"M3");
	List<HashMap> listJsonFV = new ArrayList();		
	for (TaskMsg taskMsg : list) {
		HashMap hm = new HashMap(); 
		M4 m4 = this.m4Bo.getM4ByTaskId(taskMsg.getTaskId());
		if (m4 == null) {
			hm.put("text", taskMsg.getTaskCode());
		} else {
			hm.put("text", "<font color='red'><b>" + taskMsg.getTaskCode()
					+ "</b></font>");
		}
		hm.put("id",taskMsg.getTaskId());
		hm.put("leaf", "true");
		listJsonFV.add(hm);
	}
	String jsonStr = JSONArray.fromObject(listJsonFV).toString();
	this.writeToResponse(jsonStr);
	  return null;
  }
	public ISysTreeBo getSysTreeBo() {
		return sysTreeBo;
	}
	
	public void setSysTreeBo(ISysTreeBo sysTreeBo) {
		this.sysTreeBo = sysTreeBo;
	}
	
	public IM13Bo getM13Bo() {
		return m13Bo;
	}
	
	public void setM13Bo(IM13Bo bo) {
		m13Bo = bo;
	}
	
	public String getCid() {
		return cid;
	}
	
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	public String getMsiId() {
		return msiId;
	}
	
	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}
	
	public IM2Bo getM2Bo() {
		return m2Bo;
	}
	
	public void setM2Bo(IM2Bo bo) {
		m2Bo = bo;
	}
	
	public IM3Bo getM3Bo() {
		return m3Bo;
	}
	
	public void setM3Bo(IM3Bo bo) {
		m3Bo = bo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public IM4Bo getM4Bo() {
		return m4Bo;
	}

	public void setM4Bo(IM4Bo bo) {
		m4Bo = bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public IM3Dao getM3Dao() {
		return m3Dao;
	}

	public void setM3Dao(IM3Dao m3Dao) {
		this.m3Dao = m3Dao;
	}

}
