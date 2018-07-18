package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.basedata.bo.impl.ComAtaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMSetBo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.bo.impl.MSetBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.pojo.M13;

public class MSetAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String MSET = "MSET";
	private IMStepBo mstepBo;
	private String id;
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称;
	private IMsiMainBo msiMainBo;
	private String deljson;
	private String isMaintain;//用户权限(1:修改,0:查看)
	private IComMmelBo comMmelBo;
	private Boolean noM2; 
	private IMSetBo mSetBo;
	private IComAtaBo comAtaBo;
	private IM13Bo m13Bo;
	
	public String init() {
		this.pagename = MSET;
		List<ComMmel> list = this.comMmelBo.getMmelList(getComModelSeries().getModelSeriesId());
		if(list.size()>0){
			noM2 = true;
		}else{
			noM2 =false;
		}
		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		this.mSetBo.initMset(getComModelSeries().getModelSeriesId(), showAta.getAtaId(), msiId,getSysUser().getUserId());
		return SUCCESS;
	}

	/**
	 * 加载画面显示的数据
	 */
	public String loadMset() {
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJson = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> jsonFeildList;
		List<Object> li=this.comAtaBo.getSelfAndChildAta(getComModelSeries().getModelSeriesId(),ataId);
		for (int i = 0; i < li.size(); i++) {
			ComAta ataTemp=(ComAta) this.mSetBo.getDao().loadById(ComAta.class, li.get(i).toString());
			jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("ataCode", ataTemp.getAtaCode());
			jsonFeildList.put("ataName", ataTemp.getAtaName());
			List<MSet> listMset = this.mSetBo.getMsetListByMsiIdAndAtaId(msiId, ataTemp.getAtaId());
			for (MSet mSet : listMset) {
				//遍历功能
				jsonFeildList.put("ataId", ataTemp.getAtaId());
				jsonFeildList.put("msetId", mSet.getMsetId());
				jsonFeildList.put("functionCode", mSet.getFunctionCode());
				jsonFeildList.put("functionDesc", mSet.getFunctionDesc());
				List<MSetF> setf = mSetBo.getMsetfListByMsetId(mSet.getMsetId());
				if (setf.size() > 0) {
					//功能下的功能故障大于0
					for (MSetF mSetF : setf) {
						//遍历功能故障
						jsonFeildList.put("msetfId", mSetF.getMsetfId());
						jsonFeildList.put("failureCode", mSetF.getFailureCode());
						jsonFeildList.put("failureDesc", mSetF.getFailureDesc());
						listJson.add(jsonFeildList);
						jsonFeildList = new HashMap<String, Object>();
					}
				} else {//功能下的功能故障不大于0
					listJson.add(jsonFeildList);
					jsonFeildList = new HashMap<String, Object>();
				}
			}
		}
		
		json.element("mset", listJson);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 保存操作
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22 
	 */
	public String saveMset() {
		String pageId="MSET";
		this.mSetBo.saveOrUpdateMset( this.getSysUser(), ComacConstants.SYSTEM_CODE, 
									pageId, msiId, jsonData,true,this.getComModelSeries());
		this.mSetBo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
		return null;

	}
	
	/**
	 * 暂存操作
	 * @return
	 * @author zhangjianmin
	 * createdate 2015-6-15
	 */
	public String saveZan() {
		String pageId="MSET";
		this.mSetBo.saveOrUpdateMset( this.getSysUser(), ComacConstants.SYSTEM_CODE, 
									pageId, msiId, jsonData,true,this.getComModelSeries());
		return null;
	}
	
	/**
	 * 删除操作
	 */
	public String delMset() {
		String errMsg="";
		JSONObject json=new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(deljson);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String msetfId = jsonObject.getString("msetfId");
			List<M13C> lim13c=this.m13Bo.getM13cByMsetfIdAndmsId(msetfId, msiId);
			if(null!=lim13c&&lim13c.size()>0){//后面节点已存在该msetfId的分析数据
				errMsg="err";
				json.put("errProCodes",errMsg);
				writeToResponse(json.toString());
				return null;
			}
		}
		String pageId="MSET";
		this.mSetBo.deleteMsetAndSave(this.deljson, this.getSysUser(), ComacConstants.SYSTEM_CODE, pageId, msiId, jsonData,this.getComModelSeries());
		this.mSetBo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
		json.put("errProCodes",errMsg);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 根据msiId得到所有的功能故障
	 */
	public String searchCase(){
		List<HashMap<String, Object>> listJsonFV = new ArrayList<HashMap<String, Object>>();
		
		List<MSet> mSets=this.mSetBo.getMsetListByMsiId(msiId);
		if(null!=mSets&&mSets.size()>0){
			for (MSet mSet : mSets) {
				Object[] obj=mSet.getmSetFs().toArray();
				ComAta comAta=mSet.getComAta();
				for(Object o : obj){
					HashMap<String, Object> hm = new HashMap<String, Object>();
					MSetF mSetF= (MSetF) o;
					String str=comAta.getAtaCode()+"/"+comAta.getAtaName()+"/"+mSetF.getFailureDesc();
					hm.put("caseValue", mSetF.getFailureDesc());
					hm.put("caseDisplay",str);
					hm.put("id", mSetF.getMsetfId());
					listJsonFV.add(hm);
				}
			}
		}
		JSONObject json = new JSONObject();
		json.put("case", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	public MStep getMstep() {
		return mstep;
	}

	public void setMstep(MStep mstep) {
		this.mstep = mstep;
	}

	public String getMsiId() {
		return msiId;
	}

	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}

	public ComAta getShowAta() {
		return showAta;
	}

	public void setShowAta(ComAta showAta) {
		this.showAta = showAta;
	}

	public String getAtaId() {
		return ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getDeljson() {
		return deljson;
	}

	public void setDeljson(String deljson) {
		this.deljson = deljson;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}


	public IComMmelBo getComMmelBo() {
		return comMmelBo;
	}

	public void setComMmelBo(IComMmelBo comMmelBo) {
		this.comMmelBo = comMmelBo;
	}

	public Boolean getNoM2() {
		return noM2;
	}

	public void setNoM2(Boolean noM2) {
		this.noM2 = noM2;
	}

	public IMsiMainBo getMsiMainBo() {
		return msiMainBo;
	}

	public void setMsiMainBo(IMsiMainBo msiMainBo) {
		this.msiMainBo = msiMainBo;
	}

	public IMSetBo getmSetBo() {
		return mSetBo;
	}

	public void setmSetBo(IMSetBo mSetBo) {
		this.mSetBo = mSetBo;
	}

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo m13Bo) {
		this.m13Bo = m13Bo;
	}



}
