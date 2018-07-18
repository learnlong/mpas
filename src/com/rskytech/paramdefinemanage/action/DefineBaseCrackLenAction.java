package com.rskytech.paramdefinemanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.paramdefinemanage.bo.ILhirfParamBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusMatrix;

public class DefineBaseCrackLenAction extends BaseAction{

	private static final long serialVersionUID = -6707331646162908821L;
	
	private IDefineBaseCrackLenBo  defineBaseCrackLenBo;
	private List<String> values;		//前台传回的修改过的matrixValue
	private List<String> id;			//前台传回的修改过的matrixValue的matrixID
	private Integer isAnalyse = 0;		//返回矩阵是否可以分析的状态
	private ILhirfParamBo lhirfParamBo;
	private String displayContent;
	private String displayId;

	public IDefineBaseCrackLenBo getDefineBaseCrackLenBo() {
		return defineBaseCrackLenBo;
	}

	public void setDefineBaseCrackLenBo(IDefineBaseCrackLenBo defineBaseCrackLenBo) {
		this.defineBaseCrackLenBo = defineBaseCrackLenBo;
	}
	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}
	public Integer getIsAnalyse() {
		return isAnalyse;
	}

	public void setIsAnalyse(Integer isAnalyse) {
		this.isAnalyse = isAnalyse;
	}

	@SuppressWarnings("unchecked")
	public String init(){
		return SUCCESS;
	}
	
	/**
	 * 初始化"自定义基本裂纹长度"页面的"自定义评级内容列表 "的数据
	 * @return Cus_DISPLAY表数据
	 */	
	public String loadGradeList() {
		JSONObject json = new JSONObject();
		List<HashMap> gradeForJson = new ArrayList<HashMap>();
		List<CusDisplay> gradeList = new ArrayList<CusDisplay>();
		ComModelSeries comModelSeries = this.getComModelSeries();
		String modelSeriesId = comModelSeries.getModelSeriesId();			
		gradeList = defineBaseCrackLenBo.getCusBaseCrackLenGradeList(modelSeriesId);
		if(gradeList.size()<1){
			String level = "";
			for(int i=1; i<4; i++){
				level = Integer.valueOf(i).toString();
				CusDisplay cusDisplay = new CusDisplay();
				cusDisplay.setComModelSeries(comModelSeries);
				cusDisplay.setDisplayWhere(ComacConstants.CUSBASECRACKLEN_ANAFLG);//s3
				cusDisplay.setDiff(level);
				cusDisplay.setDiffname(ComacConstants.CONGESTION_RATING);//稠密度等级				
				this.defineBaseCrackLenBo.saveOrUpdate(cusDisplay, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			for(int i=0; i<4; i++){
				level = Integer.valueOf(i).toString();
				CusDisplay cusDisplay = new CusDisplay();
				cusDisplay.setComModelSeries(comModelSeries);
				cusDisplay.setDisplayWhere(ComacConstants.CUSBASECRACKLEN_ANAFLG);//s3
				cusDisplay.setDiff(level);
				cusDisplay.setDiffname(ComacConstants.VIEWING_RATING);//目视等级				
				this.defineBaseCrackLenBo.saveOrUpdate(cusDisplay, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			for(int i=1; i<5; i++){
				level = Integer.valueOf(i).toString();
				CusDisplay cusDisplay = new CusDisplay();
				cusDisplay.setComModelSeries(comModelSeries);
				cusDisplay.setDisplayWhere(ComacConstants.CUSBASECRACKLEN_ANAFLG);//s3
				cusDisplay.setDiff(level);
				cusDisplay.setDiffname(ComacConstants.SIZE_RATING);	//尺寸等级			
				this.defineBaseCrackLenBo.saveOrUpdate(cusDisplay, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			for(int i=1; i<4; i++){
				level = Integer.valueOf(i).toString();
				CusDisplay cusDisplay = new CusDisplay();
				cusDisplay.setComModelSeries(comModelSeries);
				cusDisplay.setDisplayWhere(ComacConstants.CUSBASECRACKLEN_ANAFLG);//s3
				cusDisplay.setDiff(level);
				cusDisplay.setDiffname(ComacConstants.LIGHTING_RATING);	//光照等级			
				this.defineBaseCrackLenBo.saveOrUpdate(cusDisplay, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			for(int i=1; i<3; i++){
				level = Integer.valueOf(i).toString();
				CusDisplay cusDisplay = new CusDisplay();
				cusDisplay.setComModelSeries(comModelSeries);
				cusDisplay.setDisplayWhere(ComacConstants.CUSBASECRACKLEN_ANAFLG);//s3
				cusDisplay.setDiff(level);
				cusDisplay.setDiffname(ComacConstants.SURFACE_RATING);//表面等级				
				this.defineBaseCrackLenBo.saveOrUpdate(cusDisplay, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			gradeList = defineBaseCrackLenBo.getCusBaseCrackLenGradeList(modelSeriesId);
		}
		for (CusDisplay grade : gradeList) {
			gradeForJson.add(this.getJsonValueForCusBaseCrackLen(grade));
		}
		
		json.element("gradeList", gradeForJson);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 取得"自定义基本裂纹长度"页面的"自定义评级内容列表 "数据
	 * @return 以HashMap类型返回Cus_DISPLAY表数据
	 */
 	@SuppressWarnings("unchecked")
	public HashMap getJsonValueForCusBaseCrackLen(CusDisplay cusDisplay){
 		HashMap jsonValueList = new HashMap();
 		jsonValueList.put("displayId", cusDisplay.getDisplayId());
 		jsonValueList.put("gradeName", cusDisplay.getDiffname());
 		jsonValueList.put("grade", cusDisplay.getDiff());
 		jsonValueList.put("gradeContent", cusDisplay.getDisplayContent());
 		
		return jsonValueList;
 	}
 	
 	/**
	 * 初始化"自定义基本裂纹长度"页面的"自定义矩阵图 "的数据
	 * @return Cus_Matrix表数据
	 */
	@SuppressWarnings("unchecked")
	public String loadMatrix() {
		JSONObject json = new JSONObject();
		List<CusMatrix> matrixList = new ArrayList<CusMatrix>();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		matrixList = defineBaseCrackLenBo.getCusBaseCrackLenMatrixData(modelSeriesId);
		if((matrixList == null) || (matrixList.size() <= 0)){
			json.element("matrixData", "");
		}else{
			List<HashMap> matrixForJson = new ArrayList<HashMap>();
			for (CusMatrix matrix : matrixList) {
				matrixForJson.add(this.getJsonValue(matrix));
			}
			json.element("matrixData", matrixForJson);
		}
		if(defineBaseCrackLenBo.s3AnalyseState(modelSeriesId)){
			this.isAnalyse = 1;
		}
		json.element("isAnalyseMatrix", this.isAnalyse);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 取得"自定义基本裂纹长度"页面的"自定义矩阵图"中的数据
	 * @return 以HashMap类型返回Cus_MATRIX表数据
   	 */
	@SuppressWarnings( "unchecked" )
	public HashMap getJsonValue(CusMatrix cusMatrix){
		HashMap jsonFeildList = new HashMap();
		jsonFeildList.put("anaFlg", cusMatrix.getAnaFlg());
		jsonFeildList.put("matrixId", cusMatrix.getMatrixId());
		jsonFeildList.put("matrixRow", cusMatrix.getMatrixRow());
		jsonFeildList.put("matrixRowName", cusMatrix.getMatrixRowName());
		jsonFeildList.put("matrixCol", cusMatrix.getMatrixCol());
		jsonFeildList.put("matrixColName", cusMatrix.getMatrixColName());	
		jsonFeildList.put("matrixValue", cusMatrix.getMatrixValue());	
		return jsonFeildList;
	}
	
	/**
	 * 保存"自定义评级内容列表"中对评级内容修改后的信息
	 * 保存到数据表Cus_DISPLAY
	 * @return null
	 */
	public String  saveGradeList(){
		JSONArray jsonArray = JSONArray.fromObject(this.getJsonData());
		JSONObject jsonObject=new JSONObject();
		String dbOperate = "";
		for(int i=0; i<jsonArray.size(); i++){
			jsonObject=jsonArray.getJSONObject(i);
			String displayId=jsonObject.getString("displayId");
			CusDisplay cusDisplay = new CusDisplay(); 
			if((!BasicTypeUtils.isNullorBlank(displayId))){
				dbOperate = ComacConstants.DB_UPDATE;
				cusDisplay = (CusDisplay) defineBaseCrackLenBo.loadById(CusDisplay.class, displayId);
			}
			cusDisplay.setDisplayContent(jsonObject.getString("gradeContent"));
			defineBaseCrackLenBo.saveOrUpdate(cusDisplay,dbOperate,getSysUser().getUserId());
		}		
		return null;
	}
	
	
	/**
	 * 保存"自定义评级内容列表"的"自定义矩阵图 "上修改后的数据
	 * @return null
	 */
	public String  saveMatrix(){
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		if(values!=null && values.size()>0){
		for(int i = 0; i < this.values.size(); i++){
			Integer number = Integer.valueOf(this.id.get(i));
			CusMatrix cusMatrix = null; 
			if(number != null){
				cusMatrix = defineBaseCrackLenBo.getCusMatrixData(number,modelSeriesId);
				if(cusMatrix == null){
					cusMatrix = new CusMatrix();
					cusMatrix.setComModelSeries(this.getComModelSeries());
					cusMatrix.setAnaFlg("s3");
					cusMatrix.setMatrixFlg(new Integer(4));
					cusMatrix.setMatrixColName("条件等级");			
					cusMatrix.setMatrixRowName("实用性等级");
					
					int row = number/4+1;
					int col = number%4;
					if(number%4 == 0){
						row -= 1;
						col = 4;
					}
					cusMatrix.setMatrixRow(row);
					cusMatrix.setMatrixCol(col);
					cusMatrix.setMatrixValue(Integer.valueOf(this.values.get(i)));
					defineBaseCrackLenBo.saveOrUpdate(cusMatrix,ComacConstants.DB_INSERT,getSysUser().getUserId());
				}else{
					cusMatrix.setMatrixValue(Integer.valueOf(this.values.get(i)));
					defineBaseCrackLenBo.saveOrUpdate(cusMatrix,ComacConstants.DB_UPDATE,getSysUser().getUserId());
				}
			}
		}
		}
		return null;
	}
	
	/**
	 * 保存自定义LH4显示信息
	 */
 	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String loadCusDisplay(){
 		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();
		CusDisplay cusDisplay = defineBaseCrackLenBo.getLH4CusDisplay(getComModelSeries().getModelSeriesId());
		if (cusDisplay != null) {
			HashMap jsonFeildList = new HashMap();
			jsonFeildList.put("id", cusDisplay.getDisplayId());
			jsonFeildList.put("displayContent", cusDisplay.getDisplayContent());
			listJson.add(jsonFeildList);
		}
		json.element("cusDisplay", listJson);
		writeToResponse(json.toString());
		return null;
 	}
 	
 	/**
	 * 保存画面上修改追加的LH4显示信息数据
	 * 
	 * @return null
	 */
	public String saveCusDisplay() {
		JSONObject msgJsonObj = new JSONObject();
		boolean flag = this.lhirfParamBo.checkCusS45Mtrix(ComacConstants.LH4, getComModelSeries().getModelSeriesId());
		if (!flag) {
			msgJsonObj = this.putJsonOKFlag(msgJsonObj, false);
			writeToResponse(msgJsonObj.toString());
			return null;
		}
		CusDisplay cusDisplay = null;
		String dbOperate = ComacConstants.DB_INSERT;
		if(displayId!=null&&!"".equals(displayId)){
			cusDisplay = (CusDisplay) this.lhirfParamBo.loadById(CusDisplay.class, displayId);
			dbOperate = ComacConstants.DB_UPDATE;
		}else{
			cusDisplay = new CusDisplay();
			cusDisplay.setDisplayWhere("LH4");
			cusDisplay.setComModelSeries(getComModelSeries());
		}
		cusDisplay.setDisplayContent(displayContent);
		defineBaseCrackLenBo.saveOrUpdate(cusDisplay,dbOperate,getSysUser().getUserId());
		msgJsonObj = this.putJsonOKFlag(msgJsonObj, true);
		writeToResponse(msgJsonObj.toString());
		return null;
	}

	public ILhirfParamBo getLhirfParamBo() {
		return lhirfParamBo;
	}

	public void setLhirfParamBo(ILhirfParamBo lhirfParamBo) {
		this.lhirfParamBo = lhirfParamBo;
	}

	public String getDisplayContent() {
		return displayContent;
	}

	public void setDisplayContent(String displayContent) {
		this.displayContent = displayContent;
	}

	public String getDisplayId() {
		return displayId;
	}

	public void setDisplayId(String displayId) {
		this.displayId = displayId;
	}

}
