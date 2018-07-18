package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS7Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

public class S7Action extends BaseAction {
	private static final long serialVersionUID = 2829439936185269000L;
	private IS7Bo s7Bo;
	private String ataCode;
	private String siCode;
	private String siTitle;
	private String remark;
	private IS1Bo s1Bo;
	private String ssiId;
	private int isMaintain;
	private int[] step;
	private String  remarkId;
	private ISsiStepBo ssiStepBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	
	/**
	 * 初始化S7
	 * @return
	 */
	public String initS7(){
		SRemark sRemark=s1Bo.getRemarkBySssi(ssiId);
		if(sRemark!=null){
			remark=JavaScriptUtils.javaScriptEscape(sRemark.getS7Remark());
			remarkId=sRemark.getRemarkId();
		}
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;//现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
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
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		step=this.ssiStepBo.initStep(ssiId, step1.get(0), "S7");
		return SUCCESS;
	}
	/**
	 * 得到s7画面grid要用到的数据
	 * @return
	 */
	public String getRecords(){
		JSONObject json = new JSONObject();
		json.element("taskList", s7Bo.getS7Records(ssiId));
		writeToResponse(json.toString());
		return null;
	}
	
	
	/**
	 * 保存S7的数据
	 * @return
	 */
	public String saveS7(){
		String jsonData = this.getJsonData();
		ArrayList<String> arr=s7Bo.saveS7Records(jsonData, ssiId, getSysUser(), remarkId, remark, getComModelSeries().getModelSeriesId());
		if(arr.size()>1){
			String[] areaId=null;
			for(int i =0;i<arr.size()-1;i++){
				areaId = arr.get(i).split(",");
				for(String str : areaId){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(), str);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		this.ssiStepBo.changeStatus(ssiId);
		String index = arr.get(arr.size()-1);
		writeToResponse(index);
		return null;
	}
	public IS7Bo getS7Bo() {
		return s7Bo;
	}
	public void setS7Bo(IS7Bo s7Bo) {
		this.s7Bo = s7Bo;
	}
	public String getAtaCode() {
		return ataCode;
	}
	public void setAtaCode(String ataCode) {
		this.ataCode = ataCode;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public int[] getStep() {
		return step;
	}
	public void setStep(int[] step) {
		this.step = step;
	}
	public String getRemarkId() {
		return remarkId;
	}
	public void setRemarkId(String remarkId) {
		this.remarkId = remarkId;
	}
	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}
	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
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
