package com.rskytech.area.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa43Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za43;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za43Action extends BaseAction {

	private static final long serialVersionUID = 9100873749135162011L;

	public static final String ZA43 = "ZA43";
	
	private IZa43Bo za43Bo;
	private IZaStepBo zaStepBo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private String za43Id;
	private Za43 za43;
	private String taskId;
	private String reachWay;
	private String taskDesc;
	private String taskInterval;
	private String rstTaskId;
	private String rstReachWay;
	private String rstTaskDesc;
	private String rstTaskInterval;
	private String za43Select;
	private String finalResult;
	
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	private String MATRIX_HTML;
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za43Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za43Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, ZA43);
		String msId = getComModelSeries().getModelSeriesId();
		
        MATRIX_HTML = za43Bo.generateMatrixHtml(msId);
        return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getTaskList(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = za43Bo.loadTaskMsgList(msId, zaId);
		
		JSONObject json = new JSONObject();
		json.element("taskMsgList", listJsonFV);
		writeToResponse(json.toString());
	}
	
	/**
	 * 查询za43矩阵的选中结果
	 */
	public void getZa43MatrixResults(){
		JSONObject json = za43Bo.loadZa43Analysis(getComModelSeries().getModelSeriesId(), zaId, taskId);
		writeToResponse(json.toString());
	}
	
	public void saveZa43(){
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		JSONObject json = za43Bo.saveZa43(user.getUserId(), ms.getModelSeriesId(), areaId, zaId, taskId, taskDesc, reachWay, taskInterval, 
				rstTaskId, rstTaskDesc, rstReachWay, rstTaskInterval, za43Select, finalResult);
		writeToResponse(json.toString());
	}

	public IZa43Bo getZa43Bo() {
		return za43Bo;
	}

	public void setZa43Bo(IZa43Bo za43Bo) {
		this.za43Bo = za43Bo;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public ComArea getArea() {
		return area;
	}

	public void setArea(ComArea area) {
		this.area = area;
	}

	public String getZaId() {
		return zaId;
	}

	public void setZaId(String zaId) {
		this.zaId = zaId;
	}

	public String getZa43Id() {
		return za43Id;
	}

	public void setZa43Id(String za43Id) {
		this.za43Id = za43Id;
	}

	public Za43 getZa43() {
		return za43;
	}

	public void setZa43(Za43 za43) {
		this.za43 = za43;
	}

	public ZaStep getZaStep() {
		return zaStep;
	}

	public void setZaStep(ZaStep zaStep) {
		this.zaStep = zaStep;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getMATRIX_HTML() {
		return MATRIX_HTML;
	}

	public void setMATRIX_HTML(String mATRIXHTML) {
		MATRIX_HTML = mATRIXHTML;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getReachWay() {
		return reachWay;
	}

	public void setReachWay(String reachWay) {
		this.reachWay = reachWay;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getTaskInterval() {
		return taskInterval;
	}

	public void setTaskInterval(String taskInterval) {
		this.taskInterval = taskInterval;
	}

	public String getRstTaskId() {
		return rstTaskId;
	}

	public void setRstTaskId(String rstTaskId) {
		this.rstTaskId = rstTaskId;
	}

	public String getRstReachWay() {
		return rstReachWay;
	}

	public void setRstReachWay(String rstReachWay) {
		this.rstReachWay = rstReachWay;
	}

	public String getRstTaskDesc() {
		return rstTaskDesc;
	}

	public void setRstTaskDesc(String rstTaskDesc) {
		this.rstTaskDesc = rstTaskDesc;
	}

	public String getRstTaskInterval() {
		return rstTaskInterval;
	}

	public void setRstTaskInterval(String rstTaskInterval) {
		this.rstTaskInterval = rstTaskInterval;
	}

	public String getZa43Select() {
		return za43Select;
	}

	public void setZa43Select(String za43Select) {
		this.za43Select = za43Select;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}
	
}
