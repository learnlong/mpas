package com.rskytech.struct.action;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.SMain;
import com.rskytech.struct.bo.IUnSsiBo;
import com.rskytech.task.bo.ITaskMsgBo;

public class UnSsiAction  extends BaseAction {
	private static final long serialVersionUID = -664342800640904125L;
	private String ssiId;
	private String verifyStr;
	private String siCode;
	private String siTitle;
	private int isMaintain;
	private String isSsiChoosed;
	private IUnSsiBo unSsiBo;
	private String delId;
	private String picCode;
	private ITaskMsgBo taskMsgBo;
	
	public String init(){
		if(getSysUser()==null){
			return SUCCESS;
		}
		SMain sMain =null;
		if(ssiId!=null||"".equals(ssiId)||"null".equals(ssiId)||"undefined".equals(ssiId)){
			sMain = (SMain) unSsiBo.loadById(SMain.class, ssiId);
		}else{
			isSsiChoosed = "1";
			return "welcome";
		}
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.picCode = comAta.getEquipmentPicNo();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.picCode = "";
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		return SUCCESS;
	}
	
	/**
	 * 加载非重要结构项目检查数据
	 * @return
	 */
	public String getUnSsiList(){
		JSONObject json=this.unSsiBo.getUnssiRecords(ssiId, getComModelSeries().getModelSeriesId());
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 保存非重要结构项目检查数据
	 * @return
	 */
	public String saveUnSsi(){
		this.unSsiBo.saveUnSsiData(jsonData, ssiId, getComModelSeries().getModelSeriesId(), getSysUser().getUserId());
		return null;
	}
	
	/**
	 * 删除非重要结构项目检查数据
	 * @return
	 */
	public String delUnSsiReocrd(){
		if(delId!=null&&!"".equals(delId)){
			this.taskMsgBo.deleteTaskMsgById(delId);
		}
		if(ssiId!=null){
			if(this.unSsiBo.getUnssiRecords(ssiId,getComModelSeries().getModelSeriesId()).getString("unSsi").length()==2){
				SMain sMain = (SMain) this.unSsiBo.loadById(SMain.class, ssiId);
				sMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
				this.unSsiBo.update(sMain, getSysUser().getUserId());
			}
		}
		return null;
	}

	public String getSsiId() {
		return ssiId;
	}


	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}


	public String getVerifyStr() {
		return verifyStr;
	}


	public void setVerifyStr(String verifyStr) {
		this.verifyStr = verifyStr;
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


	public int getIsMaintain() {
		return isMaintain;
	}


	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}


	public String getIsSsiChoosed() {
		return isSsiChoosed;
	}


	public void setIsSsiChoosed(String isSsiChoosed) {
		this.isSsiChoosed = isSsiChoosed;
	}


	public IUnSsiBo getUnSsiBo() {
		return unSsiBo;
	}


	public void setUnSsiBo(IUnSsiBo unSsiBo) {
		this.unSsiBo = unSsiBo;
	}

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}

	public String getPicCode() {
		return picCode;
	}

	public void setPicCode(String picCode) {
		this.picCode = picCode;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	
}
