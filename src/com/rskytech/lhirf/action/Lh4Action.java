package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
import com.rskytech.task.bo.ITaskMsgDetailBo;

public class Lh4Action extends BaseAction {

	/**
	 * L/hirf 中的 lh_4表的 Action
	 */
	private static final long serialVersionUID = 1L;
	//注入
	private ILhStepBo lhStepBo;
	private ILh4Bo  lh4Bo;
	private IDefineBaseCrackLenBo  defineBaseCrackLenBo;
	private IDefineStructureParameterBo defineStructureParameterBo;
	private ICusLevelBo cusLevelBo;
	private ICusEdrAdrBo cusEdrAdrBo;
	private ILhMainBo lhMainBo;
	//参数
	private LhStep lhStep;
	private String hsiId;
	private String pagename;
	private Lh4 lh4;
	private LhMain lhHsi;
	private ComArea comArea;
	private String areaId;
	private String LH4_CUSTOM_MATRIX;
	private String[] edArr ;
	private String[] adArr;
	private Integer resultLh4;
	private String remark;
	private Integer needLhTask;
	private Integer  isSafe;
	private String safeReason;
    private String lh4Id;
    private String isMaintain;
    private  String lh4Display ;
    private String hsiName;
	private String areaName;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
    

    /*
	 * 初始化加载LH4页面信息
	 */
	public String init(){
		
		ComUser thisUser = this.getSysUser();
		if (null == thisUser) {
			return SUCCESS;
		}
		this.pagename = "LH4";
		lh4 = lh4Bo.getLh4BylhHsId(hsiId);
		lhHsi = (LhMain) this.lh4Bo.loadById(LhMain.class, hsiId);
		lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		comArea = (ComArea) lh4Bo.loadById(ComArea.class, this.lhHsi.getComArea().getAreaId());
		this.LH4_CUSTOM_MATRIX = loadEDADTable();
		if (lh4 != null) {
			this.needLhTask = lh4.getNeedLhTask();
			this.isSafe = lh4.getIsSafe();
			this.resultLh4 = lh4.getResult();
			this.remark = JavaScriptUtils
					.javaScriptEscape(lh4.getRemark() == null ? "" : lh4
							.getRemark());
			this.safeReason = JavaScriptUtils.javaScriptEscape(lh4
					.getSafeReason() == null ? "" : lh4.getSafeReason());
		}
		// ////得到备注信息
		lh4Display = "";
		CusDisplay lh4show = defineBaseCrackLenBo.getLH4CusDisplay(getComModelSeries().getModelSeriesId());
		if (lh4show != null) {
				lh4Display = JavaScriptUtils.javaScriptEscape(lh4show
						.getDisplayContent() == null ? "" : lh4show.getDisplayContent());
		}
		if(lhHsi != null){
				this.hsiName = JavaScriptUtils.javaScriptEscape(lhHsi
						.getHsiName() == null ? "" : lhHsi.getHsiName());
		}
		if(comArea != null ){
				this.areaName = JavaScriptUtils.javaScriptEscape(comArea
						.getAreaName() == null ? "" : comArea.getAreaName());
		}
		return SUCCESS;
	}
	
	
	/*
	 * 判断自定义数据是否完整
	 */
	public String checkCustomIsFull(){
		JSONObject json = new JSONObject();
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(modelSeriesId, ComacConstants.LH4);
		CusDisplay   lh4showremark = defineBaseCrackLenBo.getLH4CusDisplay(modelSeriesId);
		if (cusEdrAdrList.size() == 0 || cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.VALIDFLAG_NO) { 
			json.element("isOk", "no");
		}else{
			json.element("isOk", "yes");
		}
		if(lh4showremark == null){
			json.element("isRemark", "no");
		}else{
			json.element("isRemark", "yes");
		}
		writeToResponse(json.toString());
		return null;
	}
	
	/*
	 * 加载EDADTable
	 */
	public String loadEDADTable(){
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		  ///得到备注信息
		// String lh4Display ="";
		CusDisplay   lh4show = defineBaseCrackLenBo.getLH4CusDisplay(modelSeriesId);
		if(lh4show !=null){
				lh4Display = JavaScriptUtils.javaScriptEscape(lh4show
						.getDisplayContent() == null ? "" : lh4show.getDisplayContent());
		}
		StringBuffer sb = new StringBuffer();	
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(modelSeriesId, ComacConstants.LH4);
		List<CusItemS45> ED_itemList = defineStructureParameterBo.getS45List(modelSeriesId,ComacConstants.LH4,"ED");
		List<CusItemS45> AD_itemList = defineStructureParameterBo.getS45List(modelSeriesId,ComacConstants.LH4,"AD");
		//解析等级矩阵,首先要获得每个项目有多少几个级别
		List<CusLevel> levelList = cusLevelBo.getLevelList(modelSeriesId, ComacConstants.LH4, "-1");

		int levelCount = levelList.size()/(ED_itemList.size()+AD_itemList.size());	//获取每个项目有多少个级别（注：一定要保证数据的完整性）
		sb.append("<input type='hidden' id='LH4ALG' value='"+cusEdrAdrList.get(0).getAlgorithmFlg()+"'>");
		sb.append("<input type='hidden' id='EDCount' value='"+ED_itemList.size()+"'>");
		sb.append("<input type='hidden' id='ADCount' value='"+AD_itemList.size()+"'>");
		sb.append("<input type='hidden' id='levelCount' value='"+levelCount+"'>");
		sb.append("<input type='hidden' id='EDALG' value='"+ED_itemList.get(0).getItemAlgorithm()+"'>");
		sb.append("<input type='hidden' id='ADALG' value='"+AD_itemList.get(0).getItemAlgorithm()+"'>");
		sb.append("<table border='1' id='EDADtable'><tr id='tr_1'><td></td><td colspan='"+2*ED_itemList.size()+"'  id = 'ltitle'>" +
				"环境损伤（ED)ER:环境等级&nbsp;SR:敏感度等级</td><td colspan='"+2*AD_itemList.size()+"' id = 'laccidental'>偶然损伤（AD）</td></tr>");
		sb.append("<tr id='tr_2'><td rowspan='2' id='lsetting'>环境/敏感度等级</td>");
		for (int i = 0; i < ED_itemList.size(); i++) {
			sb.append("<td colspan='2'>"+JavaScriptUtils.javaScriptEscape(ED_itemList.get(i).getItemName())+"</td>");
		}
		for (int i = 0; i < AD_itemList.size(); i++) {
			sb.append("<td colspan='2'>"+JavaScriptUtils.javaScriptEscape(AD_itemList.get(i).getItemName())+"</td>");
		}
		sb.append("</tr>");
		sb.append("<tr id='tr_3'>");
		for (int i = 0; i < ED_itemList.size(); i++) {
			sb.append("<td>ER</td><td>SR</td>");
		}
		for (int i = 0; i < AD_itemList.size(); i++) {
			sb.append("<td>ER</td><td>SR</td>");
		}
		sb.append("</tr>");
		for(int i = 0; i < levelCount; i++) {
			sb.append("<tr id='tr_"+(i+4)+"'><td>"+(i+1)+"</td>");
			for (int j = 0; j < ED_itemList.size(); j++) {
				//渲染ED的级别
				List<CusLevel> EDlevelList = cusLevelBo.getLevelList(modelSeriesId, ComacConstants.LH4, ED_itemList.get(j).getItemS45Id());
					sb.append("<td id='1tr_"+(i+1)+"td_"+(2*(j+1)-1)+"' abbr='ED' title='"+EDlevelList.get(i).getLevelValue()+"'" +
							"onclick='checkedLevel(1,"+(i+1)+","+(2*(j+1)-1)+");'>"+JavaScriptUtils.javaScriptEscape(EDlevelList.get(i).getLevelName())+"</td>");
					sb.append("<td id='1tr_"+(i+1)+"td_"+2*(j+1)+"' abbr='ED' title='"+EDlevelList.get(i).getLevelValue()+"'"+
							"onclick='checkedLevel(1,"+(i+1)+","+2*(j+1)+");'>"+JavaScriptUtils.javaScriptEscape(EDlevelList.get(i).getLevelName())+"</td>");
			}
			for (int j = 0; j < AD_itemList.size(); j++) {
				//渲染AD的级别
				List<CusLevel> ADlevelList = cusLevelBo.getLevelList(modelSeriesId, ComacConstants.LH4, AD_itemList.get(j).getItemS45Id());
					sb.append("<td id='2tr_"+(i+1)+"td_"+(2*(j+1)-1)+"' abbr='AD' title='"+ADlevelList.get(i).getLevelValue()+"'" +
							"onclick='checkedLevel(2,"+(i+1)+","+(2*(j+1)-1)+");'>"+JavaScriptUtils.javaScriptEscape(ADlevelList.get(i).getLevelName())+"</td>");
					sb.append("<td id='2tr_"+(i+1)+"td_"+2*(j+1)+"' abbr='AD' title='"+ADlevelList.get(i).getLevelValue()+"'"+
							"onclick='checkedLevel(2,"+(i+1)+","+2*(j+1)+");'>"+JavaScriptUtils.javaScriptEscape(ADlevelList.get(i).getLevelName())+"</td>");
			}
			sb.append("</tr>");
		}
		sb.append("<tr><td>ED/AD:&nbsp;<label id='EDADResult' style='color:red'></label></td><td colspan='"+2*ED_itemList.size()+"' >ED<font id = 'lLevel1'>敏感度等级：</font><label id='EDResult' style='color:red'></label></td>" +
				"<td colspan='"+2*AD_itemList.size()+"' >AD<font id = 'lLevel2'>敏感度等级：</font><label id='ADResult' style='color:red'></label></td></tr>");
		sb.append("<tr rowspan='2'><td id='lRemark'>备注信息 : </td><td colspan='"+2*(ED_itemList.size()+AD_itemList.size())+"'><textarea id='myRemark' style='width:98%' rows = '4' ></textarea></td></tr>");
		sb.append("</table>");
		return sb.toString();
	}

	
	
	/*
	 * *获取Lh4的级别 
	 * @author wubo
	 * createdate 2012-09-11
	 */
	public String loadLh4Level(){
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		List<CusItemS45> ED_itemList = defineStructureParameterBo.getS45List(modelSeriesId,ComacConstants.LH4,"ED");
		List<CusItemS45> AD_itemList = defineStructureParameterBo.getS45List(modelSeriesId,ComacConstants.LH4,"AD");
		Lh4 tlh4 = new Lh4();
		tlh4 = lh4Bo.getLh4BylhHsId(hsiId);	
		
		JSONObject json = new JSONObject();
		if(tlh4!=null){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 2*(ED_itemList.size()+AD_itemList.size()); i++) {
			list.add(BasicTypeUtils.parseInt(BasicTypeUtils.getEntityObjValue(tlh4,"select"+(i+1)).toString()));
		}
		json.element("levelList", list);
		}else{
			json.element("levelList", "fail");
		}
		writeToResponse(json.toString());
		return null;
	}
	
	
	/*
	 * *获取Lh4的级别 
	 * @author wubo
	 * createdate 2012-09-11
	 */
	public String saveLh4(){
		Lh4 tlh4 =  lh4Bo.getLh4BylhHsId(hsiId);
		Integer oldneedLhTask = ComacConstants.NO;
		Integer oldIsSafe = ComacConstants.VALIDFLAG_TWO;
		boolean isSafeChange = false;
		if (tlh4 != null) {
			oldneedLhTask = tlh4.getNeedLhTask();
			oldIsSafe = tlh4.getIsSafe();
		}
		//当 是否安全着陆  IsSafe   在是/否见有切换 变更时 
		if(isSafe != oldIsSafe){
			isSafeChange = true;
		}
		String dbOperate = ""; // 数据操作
		if (tlh4 != null) { // 更新操作
			dbOperate = ComacConstants.DB_UPDATE;
		} else { // 添加操作
			dbOperate = ComacConstants.DB_INSERT;
			tlh4 = new Lh4();
			LhMain lhHsilh4 = (LhMain) lh4Bo.loadById(LhMain.class, hsiId);
			tlh4.setLhMain(lhHsilh4);
		}
		for (int i = 0; i < edArr.length; i++) {
			BasicTypeUtils.setEntityObjValue(tlh4, "select" + (i + 1),
					BasicTypeUtils.parseInt(edArr[i]));
		}
		for (int i = 0; i < adArr.length; i++) {
			BasicTypeUtils
					.setEntityObjValue(tlh4, "select" + (edArr.length + i + 1),
							BasicTypeUtils.parseInt(adArr[i]));
		}
		tlh4.setRemark(remark);
		if(!ComacConstants.YES.equals(needLhTask)){
			tlh4.setIsSafe(null);
			tlh4.setSafeReason(null);
		}else{
			tlh4.setIsSafe(isSafe);
			tlh4.setSafeReason(safeReason);
		}
		tlh4.setNeedLhTask(needLhTask);
		tlh4.setResult(resultLh4);

		ArrayList<String> array = this.lh4Bo.doSaveLh4andRef(tlh4, hsiId, dbOperate, this.getSysUser(),
				needLhTask, oldneedLhTask,isSafeChange, edArr, adArr, getComModelSeries());
		if(array!=null&&array.size()>0){
			for(String areaId : array){
				String[] arr = areaId.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		JSONObject json = putJsonOKFlag(null, true);
		writeToResponse(json.toString());
		return null;
	}


	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}


	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}


	public ILh4Bo getLh4Bo() {
		return lh4Bo;
	}


	public void setLh4Bo(ILh4Bo lh4Bo) {
		this.lh4Bo = lh4Bo;
	}

	public IDefineStructureParameterBo getDefineStructureParameterBo() {
		return defineStructureParameterBo;
	}


	public void setDefineStructureParameterBo(
			IDefineStructureParameterBo defineStructureParameterBo) {
		this.defineStructureParameterBo = defineStructureParameterBo;
	}


	public ICusLevelBo getCusLevelBo() {
		return cusLevelBo;
	}

	public void setCusLevelBo(ICusLevelBo cusLevelBo) {
		this.cusLevelBo = cusLevelBo;
	}


	public ICusEdrAdrBo getCusEdrAdrBo() {
		return cusEdrAdrBo;
	}

	public void setCusEdrAdrBo(ICusEdrAdrBo cusEdrAdrBo) {
		this.cusEdrAdrBo = cusEdrAdrBo;
	}


	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}


	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}


	public LhStep getLhStep() {
		return lhStep;
	}


	public void setLhStep(LhStep lhStep) {
		this.lhStep = lhStep;
	}


	public String getHsiId() {
		return hsiId;
	}


	public void setHsiId(String hsiId) {
		this.hsiId = hsiId;
	}


	public String getPagename() {
		return pagename;
	}


	public void setPagename(String pagename) {
		this.pagename = pagename;
	}


	public Lh4 getLh4() {
		return lh4;
	}


	public void setLh4(Lh4 lh4) {
		this.lh4 = lh4;
	}


	public LhMain getLhHsi() {
		return lhHsi;
	}


	public void setLhHsi(LhMain lhHsi) {
		this.lhHsi = lhHsi;
	}


	public ComArea getComArea() {
		return comArea;
	}


	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
	}


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public String getLH4_CUSTOM_MATRIX() {
		return LH4_CUSTOM_MATRIX;
	}


	public void setLH4_CUSTOM_MATRIX(String lH4_CUSTOM_MATRIX) {
		LH4_CUSTOM_MATRIX = lH4_CUSTOM_MATRIX;
	}

	
	public String[] getEdArr() {
		return edArr;
	}


	public void setEdArr(String[] edArr) {
		this.edArr = edArr;
	}


	public String[] getAdArr() {
		return adArr;
	}


	public void setAdArr(String[] adArr) {
		this.adArr = adArr;
	}


	public Integer getResultLh4() {
		return resultLh4;
	}


	public void setResultLh4(Integer resultLh4) {
		this.resultLh4 = resultLh4;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Integer getNeedLhTask() {
		return needLhTask;
	}


	public void setNeedLhTask(Integer needLhTask) {
		this.needLhTask = needLhTask;
	}


	public Integer getIsSafe() {
		return isSafe;
	}


	public void setIsSafe(Integer isSafe) {
		this.isSafe = isSafe;
	}


	public String getSafeReason() {
		return safeReason;
	}


	public void setSafeReason(String safeReason) {
		this.safeReason = safeReason;
	}


	public String getLh4Id() {
		return lh4Id;
	}


	public void setLh4Id(String lh4Id) {
		this.lh4Id = lh4Id;
	}


	public String getIsMaintain() {
		return isMaintain;
	}


	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}


	public String getLh4Display() {
		return lh4Display;
	}


	public void setLh4Display(String lh4Display) {
		this.lh4Display = lh4Display;
	}


	public String getHsiName() {
		return hsiName;
	}


	public void setHsiName(String hsiName) {
		this.hsiName = hsiName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public IDefineBaseCrackLenBo getDefineBaseCrackLenBo() {
		return defineBaseCrackLenBo;
	}

	public void setDefineBaseCrackLenBo(IDefineBaseCrackLenBo defineBaseCrackLenBo) {
		this.defineBaseCrackLenBo = defineBaseCrackLenBo;
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
