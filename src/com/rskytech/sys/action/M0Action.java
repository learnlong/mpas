package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.bo.IMsiSelectBo;
public class M0Action extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	public static final String M0 = "M0";
	private IM0Bo m0Bo;
	private IMsiSelectBo msiSelectBo;
	private IMStepBo mstepBo;
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称
	private String delId;   //删除的数据的ID
	private String isMaintain;//用户权限(1:修改,0:查看)
	private String modelId;   //机型Id
	private String areaCode;
	private String msiCode;
	private String defaultEff;
	private String msiSelectIsHave;
	
	public String getDefaultEff() {
		return defaultEff;
	}

	public void setDefaultEff(String defaultEff) {
		this.defaultEff = defaultEff;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String init() {

		this.pagename = M0;
		ComUser thisUser = getSysUser();
		// session过期则直接返回
		if (thisUser == null){
			return SUCCESS;
		}
		if(msiId==null||"null".equals(msiId)||"".equals(msiId)){
			msiSelectIsHave ="1";
			return "welcome";
		}
		MMain mMain =(MMain)this.mstepBo.loadById(MMain.class, msiId);
		if(ComacConstants.ANALYZE_STATUS_NEW.equals(mMain.getStatus())){
			//如果状态为“新建"设置状态为"正在分析"
			mMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			this.mstepBo.update(mMain, getSysUser().getUserId());
		}
		if(!(ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(mMain.getStatus())||
				ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(mMain.getStatus()))){
			//权限设为只读
			isMaintain = ComacConstants.NO.toString();
		}
		//初始化步骤状态
		mstep = mstepBo.getMStepByMsiId(msiId);
		if (mstep == null) {
			this.mstep = new MStep();
			this.mstep.setM11(ComacConstants.STEP_NOW);
			this.mstep.setM12(ComacConstants.STEP_NO);
			this.mstep.setM13(ComacConstants.STEP_NO);
			this.mstep.setM2(ComacConstants.STEP_NO);
			this.mstep.setM3(ComacConstants.STEP_NO);
			this.mstep.setM4(ComacConstants.STEP_NO);
			this.mstep.setM5(ComacConstants.STEP_NO);
			this.mstep.setMset(ComacConstants.STEP_NO);
			this.mstep.setMMain(mMain);
			mstepBo.save(mstep, this.getSysUser().getUserId());
		}
		if(mMain.getEffectiveness()!=null){
			defaultEff=JavaScriptUtils.javaScriptEscape(mMain.getEffectiveness());
		}else{
			defaultEff=JavaScriptUtils.javaScriptEscape(getComModelSeries().getModelSeriesName());//结构有效性
		}
		showAta = (ComAta) this.m0Bo.loadById(ComAta.class, ataId);	
		return SUCCESS;
	}

	/**
	 * 初始化M0画面
	 * @return 初始化M0数据
	 * @author chendexu 
	 * createdate 2012-08-19
	 */
	public String loadM0() {
		List<M0> listM0 = m0Bo.getMsiATAListByMsiId(msiId);
		if (listM0.size() == 0) {
			this.m0Bo.cogradientM0(msiId, getSysUser().getUserId(),getComModelSeries().getModelSeriesId());
			listM0 = m0Bo.getMsiATAListByMsiId(msiId);
		}
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJson = new ArrayList<HashMap<String, Object>>();
		for (M0 m0 : listM0) {
			listJson.add(getJsonFieldValueMap(m0));
		}
		json.element("m0", listJson);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 判断项目编号是否存在
	 * @return
	 */
	public String checkProCodeExist(){
		List<M0> listM0 = m0Bo.getMsiATAListByMsiId(msiId);
		boolean isExist = false;
		for(M0 m0 : listM0){
			if(msiCode.equals(m0.getProCode())){
				isExist =true;
			}
		}
		writeToResponse("{'exist':"+isExist+"}");
		return null;
	}
	
	private HashMap<String, Object> getJsonFieldValueMap(M0 m0) {
		HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
		if(m0.getM0Id()!=null){
			jsonFeildList.put("m0Id", m0.getM0Id());
		}else{
			jsonFeildList.put("m0Id", "");
		}
		if(m0.getMMain().getMsiId()!=null){
			jsonFeildList.put("msiId", m0.getMMain().getMsiId());
		 }else{
			 jsonFeildList.put("msiId", ""); 
		 }
		
		if(m0.getProCode()!=null){
			jsonFeildList.put("proCode", m0.getProCode());
		 }else{
			jsonFeildList.put("proCode", ""); 
		 }
		if(m0.getProName()!=null){
			jsonFeildList.put("proName", m0.getProName());
		 }else{
			jsonFeildList.put("proName", ""); 
		 }
		if(m0.getSafety()!=null){
			jsonFeildList.put("safety", m0.getSafety());
		 }else{
			jsonFeildList.put("safety", "");
		 }
		
		if( m0.getSafetyAnswer()!=null){
			jsonFeildList.put("safetyAnswer",  m0.getSafetyAnswer()); 
		 }else{
			jsonFeildList.put("safetyAnswer","");
		 }
		
		if(m0.getDetectable()!=null){
			jsonFeildList.put("detectable", m0.getDetectable()); 
		 }else{
			 jsonFeildList.put("detectable",""); 
		 }
		
		if(m0.getDetectableAnswer()!=null){
			jsonFeildList.put("detectableAnswer", m0.getDetectableAnswer());
		 }else{
			 jsonFeildList.put("detectableAnswer", ""); 
		 }
		if(m0.getTask()!=null){
			jsonFeildList.put("task", m0.getTask());
		 }else{
			jsonFeildList.put("task", ""); 
		 }
		if(m0.getTaskAnswer()!=null){
			jsonFeildList.put("taskAnswer", m0.getTaskAnswer());
		 }else{
			jsonFeildList.put("taskAnswer", "");
		 }
		if(m0.getEconomic()!=null){
			jsonFeildList.put("economic", m0.getEconomic());
		 }else{
			jsonFeildList.put("economic","");
		 }
		if(m0.getEconomicAnswer()!=null){
			jsonFeildList.put("economicAnswer", m0.getEconomicAnswer());
		 }else{
			 jsonFeildList.put("economicAnswer", ""); 
		 }
		if(m0.getIsMsi()!=null){
			jsonFeildList.put("isMsi", m0.getIsMsi()); 
		 }else{
			jsonFeildList.put("isMsi",""); 
		 }
		
		if( m0.getHighestLevel()!=null){
			jsonFeildList.put("highestLevel", m0.getHighestLevel());
		 }else{
			jsonFeildList.put("highestLevel",""); 
		 }
		if(m0.getRemark()!=null){
			jsonFeildList.put("remark", m0.getRemark());
		 }else{
			jsonFeildList.put("remark", "");
		 }
		if(m0.getIsAddAta()!=null){
			jsonFeildList.put("isAddAta", m0.getIsAddAta());
		 }else{
			 jsonFeildList.put("isAddAta","");
		 }
		return jsonFeildList;
	}
	
	/**
	 * 删除手动添加的ATA
	 * 
	 * @return
	 * @author chendexu
	 *  createdate 2012-08-20
	 */
	public String delM0() {
		m0Bo.delete(delId, getSysUser().getUserId());
		return null;
	}

	/**
	 * 添加或修改M0
	 * 
	 * @return
	 */
	public String saveM0() {
		m0Bo.saveOrUpdateM0(jsonData,this.getSysUser(),defaultEff);
		return null;
	}

	public MStep getMstep() {
		return mstep;
	}

	public void setMstep(MStep mstep) {
		this.mstep = mstep;
	}

	public IM0Bo getM0Bo() {
		return m0Bo;
	}

	public void setM0Bo(IM0Bo m0Bo) {
		this.m0Bo = m0Bo;
	}

	public IMsiSelectBo getMsiSelectBo() {
		return msiSelectBo;
	}

	public void setMsiSelectBo(IMsiSelectBo msiSelectBo) {
		this.msiSelectBo = msiSelectBo;
	}



	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
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



	public String getMsiId() {
		return msiId;
	}

	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getMsiCode() {
		return msiCode;
	}

	public void setMsiCode(String msiCode) {
		this.msiCode = msiCode;
	}


	public String getMsiSelectIsHave() {
		return msiSelectIsHave;
	}

	public void setMsiSelectIsHave(String msiSelectIsHave) {
		this.msiSelectIsHave = msiSelectIsHave;
	}

}
