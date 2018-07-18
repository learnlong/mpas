package com.rskytech.area.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.action.BaseAction;
import com.rskytech.area.bo.IZa1Bo;
import com.rskytech.area.bo.IZa5Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.Za5;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public class Za5Action extends BaseAction {

	private static final long serialVersionUID = 4386070393931166135L;

	private IZa1Bo za1Bo;
	private IZa5Bo za5Bo;
	private IZaStepBo zaStepBo;
	private IStandardRegionParamBo standardRegionParamBo;
	
	private String areaId;
	private ComArea area;
	private String zaId;
	private ZaStep zaStep;
	private int isMaintain;//1是有分析权限；0是没有分析权限
	
	private String step;//当前步骤：ZA5A和ZA5B
	private String ZA5_FIRST_CUSTOM_MATRIX; // za5第一个自定义矩阵
	private String ZA5_SECOND_CUSTOM_MATRIX;// za5第二个自定义矩阵
	private String ZA5_THIRD_CUSTOM_MATRIX; // za5第三个自定义矩阵
	private String ZA5_LAST_CUSTOM_MATRIX; // za5最终自定义矩阵
	private String checkLevelArr; // 选中的级别字符串
	private String reachWay; // 接近方式
	private String taskDesc; // 任务描述
	private String taskInterval; // 最后结果：任务间隔
	private Integer level_1; // 中间级别1
	private Integer level_2; // 中间级别2
	private Integer level_3; // 中间级别3
	
	public String init() {
		ComUser user = getSysUser();
		area = (ComArea) za5Bo.loadById(ComArea.class, areaId);
		
		ZaMain zaMain = (ZaMain) za5Bo.loadById(ZaMain.class, zaId);
		zaStep = zaStepBo.selectZaStep(user.getUserId(), zaMain, step);
		String msId = getComModelSeries().getModelSeriesId();
		
		ZA5_FIRST_CUSTOM_MATRIX = za5Bo.loadFirstMatrix(msId);
		ZA5_SECOND_CUSTOM_MATRIX = za5Bo.loadSecondMatrix(msId);
		List<CusItemZa5> firstNodeList = standardRegionParamBo.getMatrixList("00", msId);
		if (firstNodeList.size() == 3) {
			ZA5_THIRD_CUSTOM_MATRIX = za5Bo.loadThirdMatrix(msId);
		}
		ZA5_LAST_CUSTOM_MATRIX = za5Bo.loadLastMatrix(msId);
		
		Za5 za5 = za5Bo.getZa5ByZaId(zaId, step);
		if (za5 == null){
			taskDesc = "";
			taskInterval = "";
			
			Za1 za1 = za1Bo.getZa1ByZaId(zaId);
			reachWay = JavaScriptUtils.javaScriptEscape(za1.getReachWay());
		} else {
			taskDesc = JavaScriptUtils.javaScriptEscape(za5.getTaskDesc());
			taskInterval = za5.getTaskInterval();
			reachWay = za5.getReachWay();
		}
        return SUCCESS;
	}
	
	// 加载级别
	public void loadLevel() {
		String msId = getComModelSeries().getModelSeriesId();
		JSONObject json = za5Bo.loadLevel(msId, zaId, step);
		writeToResponse(json.toString());
	}
	
	public void saveZa5(){
		JSONObject json = za5Bo.saveZa5(getSysUser().getUserId(), getComModelSeries(), zaId, step, checkLevelArr, 
				reachWay, taskDesc, taskInterval, level_1, level_2, level_3);
		writeToResponse(json.toString());
	}

	public IZa1Bo getZa1Bo() {
		return za1Bo;
	}

	public void setZa1Bo(IZa1Bo za1Bo) {
		this.za1Bo = za1Bo;
	}

	public IZa5Bo getZa5Bo() {
		return za5Bo;
	}

	public void setZa5Bo(IZa5Bo za5Bo) {
		this.za5Bo = za5Bo;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IStandardRegionParamBo getStandardRegionParamBo() {
		return standardRegionParamBo;
	}

	public void setStandardRegionParamBo(
			IStandardRegionParamBo standardRegionParamBo) {
		this.standardRegionParamBo = standardRegionParamBo;
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

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getZA5_FIRST_CUSTOM_MATRIX() {
		return ZA5_FIRST_CUSTOM_MATRIX;
	}

	public void setZA5_FIRST_CUSTOM_MATRIX(String zA5FIRSTCUSTOMMATRIX) {
		ZA5_FIRST_CUSTOM_MATRIX = zA5FIRSTCUSTOMMATRIX;
	}

	public String getZA5_SECOND_CUSTOM_MATRIX() {
		return ZA5_SECOND_CUSTOM_MATRIX;
	}

	public void setZA5_SECOND_CUSTOM_MATRIX(String zA5SECONDCUSTOMMATRIX) {
		ZA5_SECOND_CUSTOM_MATRIX = zA5SECONDCUSTOMMATRIX;
	}

	public String getZA5_THIRD_CUSTOM_MATRIX() {
		return ZA5_THIRD_CUSTOM_MATRIX;
	}

	public void setZA5_THIRD_CUSTOM_MATRIX(String zA5THIRDCUSTOMMATRIX) {
		ZA5_THIRD_CUSTOM_MATRIX = zA5THIRDCUSTOMMATRIX;
	}

	public String getZA5_LAST_CUSTOM_MATRIX() {
		return ZA5_LAST_CUSTOM_MATRIX;
	}

	public void setZA5_LAST_CUSTOM_MATRIX(String zA5LASTCUSTOMMATRIX) {
		ZA5_LAST_CUSTOM_MATRIX = zA5LASTCUSTOMMATRIX;
	}

	public String getCheckLevelArr() {
		return checkLevelArr;
	}

	public void setCheckLevelArr(String checkLevelArr) {
		this.checkLevelArr = checkLevelArr;
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

	public Integer getLevel_1() {
		return level_1;
	}

	public void setLevel_1(Integer level_1) {
		this.level_1 = level_1;
	}

	public Integer getLevel_2() {
		return level_2;
	}

	public void setLevel_2(Integer level_2) {
		this.level_2 = level_2;
	}

	public Integer getLevel_3() {
		return level_3;
	}

	public void setLevel_3(Integer level_3) {
		this.level_3 = level_3;
	}
	
}
