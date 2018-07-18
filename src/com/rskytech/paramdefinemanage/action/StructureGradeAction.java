package com.rskytech.paramdefinemanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.paramdefinemanage.bo.IStructureGradeBo;

public class StructureGradeAction extends BaseAction{

	private static final long serialVersionUID = -5846224745905625776L;
	
	private String deleteIdAint;
	private String deleteIdBout;
	private IStructureGradeBo structureGradeBo;
	private IDefineStructureParameterBo  defineStructureParameterBo;
	
	
	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 初始化评级表维护页面Lh5
	 * @return 初始化自定义评级表数据
	 */
	@SuppressWarnings("unchecked")
	public String loadLh5(){
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		Object[] obs = {"A", "B", "LH5", this.getComModelSeries().getModelSeriesId()};
		List <Object[]> cusIntervalList = structureGradeBo.getCusIntervalList(obs);
		if(cusIntervalList!=null){
		for(Object[] ob : cusIntervalList){
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("level", ob[0]);
			jsonFeildList.put("valueA", ob[1]);
			jsonFeildList.put("valueB", ob[2]);
			jsonFeildList.put("intervalIdA", ob[3]);
			jsonFeildList.put("intervalIdB", ob[4]);
			listJsonFV.add(jsonFeildList);
		}
		}
		json.element("rootLh5", listJsonFV);
        writeToResponse(json.toString());
		return null;
	}
	
	
	/**
	 * 初始化评级表维护页面S6
	 * @return 初始化自定义评级表数据
	 */
	@SuppressWarnings("unchecked")
	public String loadS6(){
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		Object[] obs={"int","out","S6",this.getComModelSeries().getModelSeriesId()};
		List <Object[]> cusIntervalList=structureGradeBo.getCusIntervalList(obs);
		if(cusIntervalList!=null){
		for(Object[] object:cusIntervalList){
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("levelS6", object[0]);
			jsonFeildList.put("valueInt", object[1]);
			jsonFeildList.put("valueOut", object[2]);
			jsonFeildList.put("intervalIntId", object[3]);
			jsonFeildList.put("intervalOutId", object[4]);
			listJsonFV.add(jsonFeildList);
		}
		}
		json.element("rootS6", listJsonFV);
        writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 保存S6画面上选中的自定义评级数据
	 * @return null
	 */
	public String saveS6(){
		String jsonData = this.getJsonData();
	   this.structureGradeBo.saveS6All(this.getSysUser(),this.getComModelSeries(), jsonData);		
		return null;
	}
	
	/**
	 * 删除自定义评级一等级数据
	 */
	@SuppressWarnings("unchecked")
	public String delete(){
		String jsonData = this.getJsonData();
		structureGradeBo.deleteAll(deleteIdAint, deleteIdBout,jsonData,this.getSysUser(),this.getComModelSeries());
		return null;
		
	}
	
	// 检测是否可以修改自定义矩阵
	public String isOkUpdate() {
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// 判断分析步骤是否已经存在数据
		if(defineStructureParameterBo.checkCusS45Mtrix(ComacConstants.LH4, modelSeriesId)){	//可以进行修改
			writeToResponse("{success:true}");
		}else{
			writeToResponse("{success:false}");
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	public String getDeleteIdAint() {
		return deleteIdAint;
	}
	public void setDeleteIdAint(String deleteIdAint) {
		this.deleteIdAint = deleteIdAint;
	}
	public String getDeleteIdBout() {
		return deleteIdBout;
	}
	public void setDeleteIdBout(String deleteIdBout) {
		this.deleteIdBout = deleteIdBout;
	}
	public IStructureGradeBo getStructureGradeBo() {
		return structureGradeBo;
	}
	public void setStructureGradeBo(IStructureGradeBo structureGradeBo) {
		this.structureGradeBo = structureGradeBo;
	}
	public IDefineStructureParameterBo getDefineStructureParameterBo() {
		return defineStructureParameterBo;
	}
	public void setDefineStructureParameterBo(
			IDefineStructureParameterBo defineStructureParameterBo) {
		this.defineStructureParameterBo = defineStructureParameterBo;
	}
	
	
	
}
