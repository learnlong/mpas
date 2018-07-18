package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa43Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za43;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.dao.ITaskMsgDao;
import com.rskytech.util.MatrixUtil;

public class Za43Bo extends BaseBO implements IZa43Bo {

	private IIncreaseRegionParamBo increaseRegionParamBo;
	private IZa43Dao za43Dao;
	private ITaskMsgDao taskMsgDao;
	private IZaStepBo zaStepBo;
	
	public String generateMatrixHtml(String msId){
		StringBuffer sb = new StringBuffer();
		sb.append("<table class='za43MatrixPanel'>");//三个矩阵的外框
		sb.append("<tr>");
		sb.append("<td style='padding-left:5px'>");
		String mainMatrix = generateHtmlZa43MainMatrix(msId);
		sb.append(mainMatrix);
		sb.append("</td>");
		List<CusMatrix> matrixList = increaseRegionParamBo.searchFirstMatrix(msId);
		sb.append("<td style='padding-left:5px'>");
		String matrixMatrix = MatrixUtil.generateHtmlSelectMatrix(matrixList, ComacConstants.FIRST_MATRIX);
		sb.append(matrixMatrix);
		sb.append("</td>");		
		sb.append("</tr>");
		sb.append("<tr>");
		List<CusInterval> za43IntervalList = increaseRegionParamBo.searchFinalMatrix(msId);
		sb.append("<td align='left' colspan='2' style='padding-left:5px;padding-top:5px;'>");
		String finalMatrix = MatrixUtil.generateZa43HtmlSelectFinalMatrixData(za43IntervalList);
		sb.append(finalMatrix);
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		return sb.toString();
	}
	
	/**
	 * 生成ZA43主矩阵
	 */
	public String generateHtmlZa43MainMatrix(String msId){
		String edNm = ComacConstants.ED_CN;
		String adNm = ComacConstants.AD_CN;
		
		List<CusItemS45> za43EdItemList = increaseRegionParamBo.searchZa43Item(msId, ComacConstants.ED);
		List<CusItemS45> za43AdItemList = increaseRegionParamBo.searchZa43Item(msId, ComacConstants.AD);
		
		String edAlgorithm = za43EdItemList.get(0).getItemAlgorithm();// ed算法
		String adAlgorithm = za43AdItemList.get(0).getItemAlgorithm();// ad算法
		
		Integer levelCount = increaseRegionParamBo.isLevelNumberSame(msId);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<table id='mainMatrix' class='firstMatrixTableStyle'>");//可操作矩阵的样式//更换样式 (原来的) width='500' height='100' cellspacing='0' cellpadding='1' border='1' style='text-align:center; background-color:#FFFFFF; padding:0; margin:0;' 
		sb.append("<tr>");
		sb.append("<td rowspan='2' class='firstMatrix12TdClass'></td>");//更换样式 class='firstMatrix12TdClass'
		sb.append("<td id='edAlgorithmTitle' name='" + edAlgorithm + "' colspan='" + za43EdItemList.size() + "' class='firstMatrix12TdClass'> " + edNm + " </td>");//更换样式 class='firstMatrix12TdClass'
		sb.append("<td id='adAlgorithmTitle' name='" + adAlgorithm + "' colspan='" + za43AdItemList.size() + "' class='firstMatrix12TdClass'>" + adNm + "</td>");//更换样式 class='firstMatrix12TdClass'
		sb.append("</tr>");
		sb.append("<tr>");
		for (CusItemS45 edItem : za43EdItemList){
			sb.append("<td id='" + edItem.getItemS45Id() + "' class='firstMatrix12TdClass'>" + edItem.getItemName() + "</td>");//更换样式 class='firstMatrix12TdClass'
		}
		for (CusItemS45 adItem : za43AdItemList){
			sb.append("<td id='" + adItem.getItemS45Id() + "' class='firstMatrix12TdClass'>" + adItem.getItemName() + "</td>");//更换样式 class='firstMatrix12TdClass'
		}
		sb.append("</tr>");
		
		for (int i = 1; i <= levelCount; i++) {
			sb.append("<tr>");
            sb.append("<td class='firstMatrix12TdClass'>" + i + "</td>");//更换样式 class='firstMatrix12TdClass'
            for(CusItemS45 edItem : za43EdItemList){
            	List<CusLevel> levelList =increaseRegionParamBo.getLevelList(edItem.getItemS45Id(), ComacConstants.ZA43, msId);
                sb.append("<td id='" + levelList.get(i - 1).getLevelId() + "' name='" + levelList.get(i - 1).getItemId() + "' flg='" + ComacConstants.ED + "'" );               
                sb.append(" onclick='selectLevel(this)' levelSort='" + i + "' levelValue='" + levelList.get(i - 1).getLevelValue() + "' class='firstMatrix34TdClass'>");//第二列可选区域//更换样式 class='firstMatrix34TdClass'
        		sb.append(levelList.get(i - 1).getLevelName());
                sb.append("</td>");
            }
            for(CusItemS45 adItem : za43AdItemList){
            	List<CusLevel> levelList =increaseRegionParamBo.getLevelList(adItem.getItemS45Id(), ComacConstants.ZA43, msId);
                sb.append("<td id='" + levelList.get(i - 1).getLevelId() + "' name='" + levelList.get(i - 1).getItemId() + "' flg='" + ComacConstants.AD + "'");
                // levelSort是当前级别值在当前项目下的排序号
                sb.append(" onclick='selectLevel(this)' levelSort='" + i + "' levelValue='" + levelList.get(i - 1).getLevelValue() + "' class='firstMatrix34TdClass'>");//第三列可选区域//更换样式 class='firstMatrix34TdClass'
        		sb.append(levelList.get(i - 1).getLevelName());
                sb.append("</td>");
            }
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap> loadTaskMsgList(String msId, String zaId){
		List<TaskMsg> list = taskMsgDao.findAreaTaskMsg(msId, zaId, "ZA_4_2");
		if (list != null){
			List<HashMap> listJsonFV = new ArrayList();
			
			for (TaskMsg tm : list) {
				HashMap hm = new HashMap();
				hm.put("taskId", tm.getTaskId());
				hm.put("taskCode", tm.getTaskCode());
				hm.put("taskType", tm.getTaskType());				
				listJsonFV.add(hm);
			}
			return listJsonFV;
		}
		return null;
	}
	
	public JSONObject loadZa43Analysis(String msId, String zaId, String taskId){
		JSONObject json = new JSONObject();
		
		TaskMsg thisTask = (TaskMsg) this.loadById(TaskMsg.class, taskId);
	    
	    // 表示当前任务是有za43中的问题6生成，此时应该把rst任务带出
		TaskMsg rstTask = null;
	    if ("6".equals(thisTask.getAnyContent1())){
	    	List<TaskMsg> list = taskMsgDao.findAreaTaskMsg(msId, zaId, "ZA_4_1", "4");
	    	if (list != null && list.size() > 0){
	    		rstTask = list.get(0);
	    	}
		}
	    
	    json.element("taskId", thisTask.getTaskId());
	    json.element("taskCode", thisTask.getTaskCode() == null ? "" : thisTask.getTaskCode());	    
	    json.element("taskDesc", thisTask.getTaskDesc() == null ? "" : thisTask.getTaskDesc());
	    json.element("reachWay", thisTask.getReachWay() == null ? "" : thisTask.getReachWay());
	    json.element("taskInterval", thisTask.getTaskInterval() == null ? "" : thisTask.getTaskInterval());

		if (rstTask != null){
		    json.element("rstTaskId", rstTask.getTaskId());
		    json.element("rstTaskCode", rstTask.getTaskCode() == null ? "" : rstTask.getTaskCode());
		    json.element("rstTaskDesc", rstTask.getTaskDesc() == null ? "" : rstTask.getTaskDesc());
		    json.element("rstReachWay", rstTask.getReachWay() == null ? "" : rstTask.getReachWay());		    
		    json.element("rstTaskInterval", rstTask.getTaskInterval() == null ? "" : rstTask.getTaskInterval());
		}
		
		Za43 za43 = za43Dao.getZa43ByZaIdAndTaskId(zaId, taskId);
		if (za43 != null) {
			List<Integer> list = new ArrayList<Integer>();
			List<CusItemS45> za43EdItemList = increaseRegionParamBo.searchZa43Item(msId, ComacConstants.ED);
			List<CusItemS45> za43AdItemList = increaseRegionParamBo.searchZa43Item(msId, ComacConstants.AD);
			for (int i = 0; i < (za43EdItemList.size() + za43AdItemList.size()); i++) {
				list.add(BasicTypeUtils.parseInt(BasicTypeUtils.getEntityObjValue(za43, "select" + (i + 1)).toString()));
			}
			json.element("levelList",list);
		}
		return json;
	}
	
	public JSONObject saveZa43(String userId, String msId, String areaId, String zaId, String taskId, String taskDesc, 
			String reachWay, String taskInterval, String rstTaskId, String rstTaskDesc, String rstReachWay, 
			String rstTaskInterval, String za43Select, String finalResult){
		JSONObject json = new JSONObject();
		try {
			if (taskId != null && !"".equals(taskId)){
				TaskMsg task = (TaskMsg) this.loadById(TaskMsg.class, taskId);
				task.setTaskCode(generateTaskCode(task.getTaskCode(), msId, areaId, zaId));
				task.setTaskDesc(taskDesc);
				task.setReachWay(reachWay);
				task.setTaskInterval(taskInterval);
				this.saveOrUpdate(task, ComacConstants.DB_UPDATE, userId);
				
				Za43 za43 = za43Dao.getZa43ByZaIdAndTaskId(zaId, taskId);
				if (za43 == null){
					za43 = new Za43();
					ZaMain zaMain = new ZaMain();
					zaMain.setZaId(zaId);
					za43.setZaMain(zaMain);
					za43.setTaskId(taskId);
				}
				String[] selectValueArr = za43Select.split(",");
				for (int i = 0; i < selectValueArr.length; i++) {
					String fieldNm = "select"+(i+1);
					BasicTypeUtils.setEntityObjValue(za43, fieldNm, BasicTypeUtils.parseInt(selectValueArr[i]));
				}
				za43.setResult(BasicTypeUtils.parseInt(finalResult));
				if (za43.getZa43Id() == null){
					this.saveOrUpdate(za43, ComacConstants.DB_INSERT, userId);
				} else {
					this.saveOrUpdate(za43, ComacConstants.DB_UPDATE, userId);
				}
			}
			
			if (rstTaskId != null && !"".equals(rstTaskId)){
				TaskMsg task = (TaskMsg) this.loadById(TaskMsg.class, rstTaskId);
				task.setTaskCode(generateTaskCode(task.getTaskCode(), msId, areaId, zaId));
				task.setTaskDesc(rstTaskDesc);
				task.setReachWay(rstReachWay);
				task.setTaskInterval(rstTaskInterval);
				this.saveOrUpdate(task, ComacConstants.DB_UPDATE, userId);
			}
			
			//处理分析步骤和状态
			Integer nextStep = zaStepBo.updateZa43StepAndStatus(userId, msId, zaId);
			json.element("nextStep", nextStep);
			json.element("success", true);
		} catch(Exception e){
			json.element("success", false);
			e.printStackTrace();
		}
		return json;
	}
	
	public String generateTaskCode(String taskCode, String msId, String areaId, String zaId){
		if (taskCode != null){
			return taskCode;
		}
		
		List<TaskMsg> list = za43Dao.getTaskList(msId, zaId);
		ComArea area = (ComArea) this.loadById(ComArea.class, areaId);
		String codeIndexStr = "01";//最终的编号字符串
		
		if (list != null && list.size() > 0){
			String maxTaskCode = list.get(0).getTaskCode();
		    String[] codeItemArr = maxTaskCode.split("-");
		    Integer codeIndex = BasicTypeUtils.parseInt(codeItemArr[codeItemArr.length - 1]) + 1;
		    codeIndexStr = codeIndex.toString();
		    if (codeIndexStr.length() == 1 ){
		    	codeIndexStr = "0" + codeIndexStr;
		    }
		}
	    return ComacConstants.AREA_ZENGQIANG + "-" + area.getAreaCode() + "-" + codeIndexStr;
	}

	public IIncreaseRegionParamBo getIncreaseRegionParamBo() {
		return increaseRegionParamBo;
	}

	public void setIncreaseRegionParamBo(
			IIncreaseRegionParamBo increaseRegionParamBo) {
		this.increaseRegionParamBo = increaseRegionParamBo;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa43Dao getZa43Dao() {
		return za43Dao;
	}

	public void setZa43Dao(IZa43Dao za43Dao) {
		this.za43Dao = za43Dao;
	}
	
}
