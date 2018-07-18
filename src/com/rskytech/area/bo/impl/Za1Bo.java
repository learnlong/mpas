package com.rskytech.area.bo.impl;

import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.web.util.JavaScriptUtils;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa1Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.bo.ITaskMsgBo;

public class Za1Bo extends BaseBO implements IZa1Bo {

	private IZa1Dao za1Dao;
	private IZaStepBo zaStepBo;
	private ITaskMsgBo taskMsgBo;
	
	public ZaMain selectZaMain(ComUser user, ComModelSeries ms, ComArea area){
		ZaMain zaMain = za1Dao.getZaMainByAreaId(ms.getModelSeriesId(), area.getAreaId());
		if (zaMain == null){
			zaMain = new ZaMain();
			zaMain.setComModelSeries(ms);
			zaMain.setComArea(area);
			zaMain.setEffectiveness(ms.getModelSeriesCode());
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_NEW);
			zaMain.setValidFlag(ComacConstants.VALIDFLAG_YES);
			this.saveOrUpdate(zaMain, ComacConstants.DB_INSERT, user.getUserId());
		}
		return zaMain;
	}
	
	public ZaMain selectZaMain(ComModelSeries ms, ComArea area){
		return za1Dao.getZaMainByAreaId(ms.getModelSeriesId(), area.getAreaId());
	}
	
	@SuppressWarnings("unchecked")
	public Za1 selectZa1(String zaId, ComArea area){
		Za1 za1 = za1Dao.getZa1ByZaId(zaId);
		if (za1 == null){
			za1 = new Za1();
			
			ZaMain zaMain = new ZaMain();
			zaMain.setZaId(zaId);
			za1.setZaMain(zaMain);
			za1.setReachWay(JavaScriptUtils.javaScriptEscape(area.getReachWay()));
			
			String equStruc = "";
			Set<ComAreaDetail> set = area.getComAreaDetails();
			if (set != null && set.size() > 0){
				for (ComAreaDetail cad : set){
					equStruc = equStruc + cad.getEquipmentName() + "(" + cad.getEquipmentPicNo()
						+ "/" + cad.getEquipmentTypeNo() + ");";
				}
				equStruc = equStruc.substring(0, equStruc.length() - 1);
			}
			equStruc = equStruc + "。" + area.getWirePiping();
			
			za1.setEquStruc(JavaScriptUtils.javaScriptEscape(equStruc));
			return za1;
		} else {
			Za1 za1New = new Za1();
			za1New.setZa1Id(za1.getZa1Id());
			za1New.setZaMain(za1.getZaMain());
			za1New.setBorder(JavaScriptUtils.javaScriptEscape(za1.getBorder()));
			za1New.setEnv(JavaScriptUtils.javaScriptEscape(za1.getEnv()));
			za1New.setReachWay(JavaScriptUtils.javaScriptEscape(za1.getReachWay()));
			za1New.setEquStruc(JavaScriptUtils.javaScriptEscape(za1.getEquStruc()));
			za1New.setHasStrucOnly(za1.getHasStrucOnly());
			za1New.setNeedAreaAnalyze(za1.getNeedAreaAnalyze());
			za1New.setHasPipe(za1.getHasPipe());
			za1New.setHasMaterial(za1.getHasMaterial());
			za1New.setCloseToSystem(za1.getCloseToSystem());
			za1New.setRemark(JavaScriptUtils.javaScriptEscape(za1.getRemark()));
			return za1New;
		}
	}
	
	public JSONObject saveZa1(String userId, Za1 za1, String oldType, String effectiveness){
		String zaId = za1.getZaMain().getZaId();
		
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		
		//把无效的按钮数据变更成数据2
		if (za1.getHasStrucOnly() == null) za1.setHasStrucOnly(2);
		if (za1.getNeedAreaAnalyze() == null) za1.setNeedAreaAnalyze(2);
		if (za1.getHasPipe() == null) za1.setHasPipe(2);
		if (za1.getHasMaterial() == null) za1.setHasMaterial(2);
		if (za1.getCloseToSystem() == null) za1.setCloseToSystem(2);
		
		String newType = getAreaAnalysisType(za1);		
		
		JSONObject json = new JSONObject();
		try {
			zaMain.setEffectiveness(effectiveness);
			this.saveOrUpdate(zaMain, ComacConstants.DB_UPDATE, userId);
			
			if (za1.getZa1Id() == null || "".equals(za1.getZa1Id())){
				za1.setZa1Id(null);
				this.saveOrUpdate(za1, ComacConstants.DB_INSERT, userId);
			} else {
				this.saveOrUpdate(za1, ComacConstants.DB_UPDATE, userId);
			}
			
			//如果修改前后变更了区域的分析类型，则需要考虑删除原有分析数据、任务，并更新ZA_STEP
			if (!newType.equals(oldType)){
				zaStepBo.updateZa1StepAndStatus(userId, zaId, newType);
				taskMsgBo.deleteAreaTask(zaMain.getComModelSeries().getModelSeriesId(), zaId, "ALL");
				za1Dao.deleteAreaAnalysis(zaId);
			}
			
			if ("no".equals(newType)){
				json.put("nextStep", 0);//跳转到ZA1页面
			} else {
				json.put("nextStep", 1);//跳转懂啊ZA2页面
			}
			json.put("success", true);
		} catch(Exception e){
			json.put("success", false);
			e.printStackTrace();
		}
		
		return json;
	}
	
	public String getAreaAnalysisType(Za1 za1){
		if (za1.getZa1Id() == null){//还没有做分析
			return "";
		} else if (za1.getNeedAreaAnalyze() == 0){//不需要区域分析
			return "no";
		} else if (za1.getNeedAreaAnalyze() == 1 || za1.getHasPipe() == 0 || za1.getCloseToSystem() == 0){//标准区域分析
			return "biaozhun";
		} else {//增强区域分析和标准区域分析
			return "zengqiang";
		}
	}
	
	public Za1 getZa1ByZaId(String zaId){
		return za1Dao.getZa1ByZaId(zaId);
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	
}
