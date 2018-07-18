package com.rskytech.area.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZa5Bo;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa5Dao;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za5;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.dao.ITaskMsgDao;
import com.rskytech.util.MatrixUtil;

public class Za5Bo extends BaseBO implements IZa5Bo {

	private IStandardRegionParamBo standardRegionParamBo;
	private ICusLevelBo cusLevelBo;
	private IZa5Dao za5Dao;
	private ITaskMsgDao taskMsgDao;
	private IZaStepBo zaStepBo;
	
	// 生成za5的第一矩阵表格
	public String loadFirstMatrix(String msId){
		StringBuffer sb = new StringBuffer();
		// 加载表头第一行
		loadFirstMatrixFirstRow(msId, sb);
		// 加载表头第二行
		loadFirstMatrixSecondRow(msId, sb);
		// 加载表头第三行
		loadFirstMatrixThirdRow(msId, sb);
		// 加载表头第四行
		loadFirstMatrixForthRow(msId, sb);
		// 加载级别部分
		loadFirstMatrixLevel(msId, sb);
		return sb.toString();
	}
	
	// 加载第一矩阵的第一行
	public void loadFirstMatrixFirstRow(String msId, StringBuffer sb) {
		sb.append("<div id='firstMatrixDiv' class='firstMatrixDiv'>");
		List<CusItemZa5> firstNodeList = standardRegionParamBo.getMatrixList("00", msId);
		for (int i = 0; i < firstNodeList.size(); i++) {
			sb.append("<input type='hidden' name='" + firstNodeList.get(i).getItemZa5Id() + "' id='alg" + i
					+ "' value='" + firstNodeList.get(i).getLevel1Algorithm() + "'>");
		}
		List<CusItemZa5> lastItemList = standardRegionParamBo.getAllLeafNode("00", msId);
		// 获取每个项目的级别数量
		int levelCount = cusLevelBo.getLevelList(msId, ComacConstants.ZA5, lastItemList.get(0).getItemZa5Id()).size();
		sb.append("<input type='hidden' id='levelCount' value='" + levelCount + "'>");
		sb.append("<input type='hidden' id='lastItemCount' value='" + lastItemList.size() + "'>");
		sb.append("<input type='hidden' id='algCount' value='" + firstNodeList.size() + "'>");
		sb.append("<a href='javascript:endIeStatus();' id='endIeStatus' style='display: none;'></a>");
		sb.append("<table id='firstMatrix' class='firstMatrixTableStyle'>");
		sb.append("<tr><td rowspan='4' class='firstMatrix1TdClass'>级号</td>");
		// 生成第一行自定义元素
		for (int i = 0; i < firstNodeList.size(); i++) {
			// 获取当前登录的语言国际化
			String tempItemName = firstNodeList.get(i).getItemName();
			// 判断一级结点是否为叶子结点
			if (firstNodeList.get(i).getIsLeafNode() == 1) { // 是叶子节点
				sb.append("<td rowspan='4' abbr='1' class='firstMatrix1TdClass'>" + tempItemName + "</td>");
			} else { // 不是叶子结点
				// 获取该节点下所有的叶子节点
				List<CusItemZa5> tempList = standardRegionParamBo.getAllLeafNode(firstNodeList.get(i).getItemZa5Id(), msId);
				sb.append("<td colspan='" + tempList.size() + "' abbr='"
						+ tempList.size() + "' class='firstMatrix1TdClass'>" + tempItemName + "</td>");
				// 需要获取其的子节点
			}
		}
		sb.append("</tr>");
	}
	
	// 加载第一矩阵的第二行
	public void loadFirstMatrixSecondRow(String msId, StringBuffer sb) {
		sb.append("<tr>");
		List<CusItemZa5> firstNotLeafList = standardRegionParamBo.getMatrix("00", 0, msId);
		if (firstNotLeafList != null) { // 一级非叶子节点不为空
			// 获取二级节点数据
			// 表头第二行
			for (int i = 0; i < firstNotLeafList.size(); i++) {
				List<CusItemZa5> secondNodeList = standardRegionParamBo.getMatrixList(firstNotLeafList.get(i).getItemZa5Id(), msId);
				for (int j = 0; j < secondNodeList.size(); j++) {
					if (secondNodeList.get(j).getIsLeafNode() == 1) { // 二级节点下的叶子结点
						String tempItemName = secondNodeList.get(j).getItemName();
						sb.append("<td rowspan='" + (4 - 1)
								+ "' name='lastTrItem' class='firstMatrix23TdClass'>" + tempItemName + "</td>");
					} else { // 二级下面的非叶子节点
						// 获取该节点下所有的叶子节点
						String tempItemName = secondNodeList.get(j).getItemName();
						List<CusItemZa5> tempList = standardRegionParamBo.getAllLeafNode(secondNodeList.get(j).getItemZa5Id(), msId);
						sb.append("<td colspan='" + tempList.size() + "' class='firstMatrix23TdClass'>" + tempItemName + "</td>");
					}
				}
			}
			sb.append("</tr>");
		}
	}
	
	// 加载第一矩阵的第三行
	public void loadFirstMatrixThirdRow(String msId, StringBuffer sb) {
		List<CusItemZa5> firstNotLeafList = standardRegionParamBo.getMatrix("00", 0, msId);
		sb.append("<tr>");
		for (int i = 0; i < firstNotLeafList.size(); i++) {
			List<CusItemZa5> secondNodeList = standardRegionParamBo.getMatrixList(
					firstNotLeafList.get(i).getItemZa5Id(), msId);
			for (int j = 0; j < secondNodeList.size(); j++) {
				if (secondNodeList.get(j).getIsLeafNode() == 0) { // 二级节点下的非叶子结点
					// 表头第三行节点
					List<CusItemZa5> thirdNodeList = standardRegionParamBo.getMatrixList(secondNodeList.get(j).getItemZa5Id(), msId);
					for (int k = 0; k < thirdNodeList.size(); k++) {
						if (thirdNodeList.get(k).getIsLeafNode() == 1) { // 三级下叶子节点
							String tempItemName = thirdNodeList.get(k).getItemName();
							sb.append("<td rowspan='" + (4 - 2)
									+ "' name='lastTrItem' class='firstMatrix23TdClass'> " + tempItemName + "</td>");
						} else { // 三级下的非叶子节点
							String tempItemName = thirdNodeList.get(k).getItemName();
							List<CusItemZa5> tempList = standardRegionParamBo.getAllLeafNode(thirdNodeList.get(k).getItemZa5Id(), msId);
							sb.append("<td colspan='" + tempList.size() + "' class='firstMatrix23TdClass'>"+ tempItemName + "</td>");
						}
					}
				}
			}
		}
		sb.append("</tr>");
	}
	
	// 加载第一矩阵的第四行
	public void loadFirstMatrixForthRow(String msId, StringBuffer sb) {
		List<CusItemZa5> firstNotLeafList = standardRegionParamBo.getMatrix("00", 0, msId);
		sb.append("<tr>");
		for (int i = 0; i < firstNotLeafList.size(); i++) {
			List<CusItemZa5> secondNodeList = standardRegionParamBo.getMatrixList(firstNotLeafList.get(i).getItemZa5Id(), msId);
			for (int j = 0; j < secondNodeList.size(); j++) {
				if (secondNodeList.get(j).getIsLeafNode() == 0) {
					List<CusItemZa5> thirdNodeList = standardRegionParamBo.getMatrixList(secondNodeList.get(j).getItemZa5Id(), msId);
					for (int k = 0; k < thirdNodeList.size(); k++) {
						if (thirdNodeList.get(k).getIsLeafNode() == 0) { // 三级下非叶子节点
							List<CusItemZa5> lastNodeList = standardRegionParamBo.getMatrixList(thirdNodeList.get(k).getItemZa5Id(), msId);
							for (int l = 0; l < lastNodeList.size(); l++) {
								String tempItemName = lastNodeList.get(l).getItemName();
								sb.append("<td name='lastTrItem' color='red' class='firstMatrix23TdClass'>" + tempItemName + "</td>");
							}
						}
					}
				}
			}
		}
		sb.append("</tr>");
	}
	
	// 加载第一矩阵的级别
	public void loadFirstMatrixLevel(String msId, StringBuffer sb) {
		// 获取每个项目的级别数量
		List<CusItemZa5> lastItemList = standardRegionParamBo.getAllLeafNode("00", msId);
		int levelCount = cusLevelBo.getLevelList(msId, ComacConstants.ZA5, lastItemList.get(0).getItemZa5Id()).size();
		for (int i = 0; i < levelCount; i++) {
			sb.append("<tr><td class='firstMatrix1TdClass'>" + (i + 1) + "</td>");//第一矩阵第一列,序号
			for (int j = 0; j < lastItemList.size(); j++) {
				List<CusLevel> levelList = cusLevelBo.getLevelList(msId, ComacConstants.ZA5, lastItemList.get(j).getItemZa5Id());
				if (lastItemList.get(j).getParentId().equals("00")) { // 此项目是一级节点
					/**
					 * 第一矩阵二三行二三列,选择位置
					 */
					sb.append("<td id='level_" + (i + 1) + "td_" + (j + 1)
							+ "' abbr='" + lastItemList.get(j).getItemZa5Id()
							+ "' title='" + levelList.get(i).getLevelValue()
							+ "' onclick='checkedLevel(" + (i + 1) + ","
							+ (j + 1) + ");' class='firstMatrixLevelTdClass'>"
							+ levelList.get(i).getLevelName() + "</td>");
				} else {
					CusItemZa5 za1 = (CusItemZa5) standardRegionParamBo.loadById(CusItemZa5.class, lastItemList.get(j).getParentId());
					if (za1.getParentId().equals("00")) { // 此项目是二级节点
						sb.append("<td id='level_" + (i + 1) + "td_" + (j + 1)
								+ "' abbr='" + za1.getItemZa5Id() + "' title='"
								+ levelList.get(i).getLevelValue()
								+ "' onclick='checkedLevel(" + (i + 1) + ","
								+ (j + 1) + ");' class='firstMatrixLevelTdClass'>"
								+ levelList.get(i).getLevelName() + "</td>");
					} else {
						CusItemZa5 za2 = (CusItemZa5) standardRegionParamBo.loadById(CusItemZa5.class, za1.getParentId());
						if (za2.getParentId().equals("00")) { // 此项目是三级节点
							sb.append("<td id='level_" + (i + 1) + "td_"
									+ (j + 1) + "' abbr='" + za2.getItemZa5Id()
									+ "' title='" + levelList.get(i).getLevelValue()
									+ "' onclick='checkedLevel(" + (i + 1)
									+ "," + (j + 1) + ");' class='firstMatrixLevelTdClass'>"
									+ levelList.get(i).getLevelName() + "</td>");
						} else { // 此项目是四级节点
							CusItemZa5 za3 = (CusItemZa5) standardRegionParamBo.loadById(CusItemZa5.class, za2.getParentId());
							sb.append("<td id='level_" + (i + 1) + "td_"
									+ (j + 1) + "' abbr='" + za3.getItemZa5Id()
									+ "' title='" + levelList.get(i).getLevelValue()
									+ "' onclick='checkedLevel(" + (i + 1)
									+ "," + (j + 1) + ");' class='firstMatrixLevelTdClass'>"
									+ levelList.get(i).getLevelName() + "</td>");
						}
					}
				}
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("</div>");
	}
	
	// 生成za5的第二矩阵表格
	public String loadSecondMatrix(String msId) {
		List<CusMatrix> matrixList = standardRegionParamBo.searchFirstMatrix(msId);
		String strHtml = MatrixUtil.generateHtmlSelectMatrix(matrixList, ComacConstants.FIRST_MATRIX);
		return strHtml;
	}
	
	// 生成za5的第三矩阵表格
	public String loadThirdMatrix(String msId) {
		List<CusMatrix> matrixList = standardRegionParamBo.searchSecondMatrix(msId);
		String strHtml = MatrixUtil.generateHtmlSelectMatrix(matrixList, ComacConstants.SECOND_MATRIX);
		return strHtml;
	}
	
	// 生成za5的最终矩阵表格
	public String loadLastMatrix(String msId) {
		List<CusInterval> finalInMatrixList = standardRegionParamBo.searchFinalMatrix(msId, ComacConstants.INNER);
		List<CusInterval> finalOutMatrixList = standardRegionParamBo.searchFinalMatrix(msId, ComacConstants.OUTTER);
		HashMap<String, List<CusInterval>> hMap = new HashMap<String, List<CusInterval>>();
		hMap.put(ComacConstants.INNER, finalInMatrixList);
		hMap.put(ComacConstants.OUTTER, finalOutMatrixList);
		// 加载检查间隔矩阵内容
		String strHtml = MatrixUtil.generateZa5HtmlSelectFinalMatrixData(hMap);
		return strHtml;
	}
	
	public Za5 getZa5ByZaId(String zaId, String step){
		return za5Dao.getZa5ByZaId(zaId, step);
	}
	
	public JSONObject loadLevel(String msId, String zaId, String step){
		JSONObject json = new JSONObject();
		List<CusItemZa5> lastItemList = standardRegionParamBo.getAllLeafNode("00", msId);
		
		Za5 za5 = getZa5ByZaId(zaId, step);
		if (za5 == null) { // 没有此分析Id的级别
			json.element("levelList", "fail");
		} else {
			List<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < lastItemList.size(); i++) {
				list.add(BasicTypeUtils.parseInt(BasicTypeUtils.getEntityObjValue(za5, "select" + (i + 1)).toString()));
			}
			json.element("levelList", list);
			json.element("level_1", za5.getLevel1());
			json.element("level_2", za5.getLevel2());
			json.element("level_3", za5.getLevel3());
		}
		return json;
	}
	
	public JSONObject saveZa5(String userId, ComModelSeries ms, String zaId, String step, String checkLevelArr, 
			String reachWay, String taskDesc, String taskInterval, Integer level_1, Integer level_2, Integer level_3){
		ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
		ComArea area = zaMain.getComArea();		
		
		JSONObject json = new JSONObject();
		String operateFlg = "";
		
		try{
			Za5 za5 = getZa5ByZaId(zaId, step);
			if (za5 == null) { // 存在此记录，执行插入操作
				operateFlg = ComacConstants.DB_INSERT;
				za5 = new Za5();
				za5.setZaMain(zaMain);
			} else {
				operateFlg = ComacConstants.DB_UPDATE;			
			}
			
			String[] tempArray = checkLevelArr.split(",");
			for (int i = 0; i < tempArray.length; i++) {
				BasicTypeUtils.setEntityObjValue(za5, "select" + (i + 1), BasicTypeUtils.parseInt(tempArray[i].trim()));
			}
			
			za5.setStep(step);
			za5.setReachWay(reachWay);
			za5.setTaskDesc(taskDesc);
			za5.setTaskInterval(taskInterval);
			za5.setResult(taskInterval);
			za5.setLevel1(level_1);
			za5.setLevel2(level_2);
			za5.setLevel3(level_3);
			this.saveOrUpdate(za5, operateFlg, userId);
		
			TaskMsg taskMsg = new TaskMsg();
			List<TaskMsg> listTempA = taskMsgDao.findAreaTaskMsg(ms.getModelSeriesId(), zaId, "ZA5A");
			List<TaskMsg> listTempB = taskMsgDao.findAreaTaskMsg(ms.getModelSeriesId(), zaId, "ZA5B");
			String tempStr = "";
			
			if ("ZA5A".equals(step)) {
				if (listTempA.size() == 0) { // 生成Msg-3任务
					operateFlg = ComacConstants.DB_INSERT;
					if (listTempB.size() == 0) {
						tempStr = ComacConstants.AREA_BIAOZHUN + "-" + area.getAreaCode() + "-01";
					} else {
						tempStr = ComacConstants.AREA_BIAOZHUN + "-" + area.getAreaCode() + "-02";
					}
					taskMsg.setTaskCode(tempStr);// 任务编号
				} else { // 更新Msg-3任务
					taskMsg = listTempA.get(0);
					operateFlg = ComacConstants.DB_UPDATE;
				}
			} else if ("ZA5B".equals(step)) {
				if (listTempB.size() == 0) { // 生成Msg-3任务
					operateFlg = ComacConstants.DB_INSERT;
					if (listTempA.size() == 0) {
						tempStr = ComacConstants.AREA_BIAOZHUN + "-" + area.getAreaCode() + "-01";
					} else {
						tempStr = ComacConstants.AREA_BIAOZHUN + "-" + area.getAreaCode() + "-02";
					}
					taskMsg.setTaskCode(tempStr);// 任务编号
				} else { // 更新Msg-3任务
					taskMsg = listTempB.get(0);
					operateFlg = ComacConstants.DB_UPDATE;
				}
			}		
			
			taskMsg.setComModelSeries(ms);// 机型系列ID
			taskMsg.setSourceSystem(ComacConstants.ZONAL_CODE);// 生成任务的系统
			taskMsg.setSourceAnaId(zaId);// 生成任务的分析ID
			taskMsg.setSourceStep(step);// 生成任务的步骤
			taskMsg.setTaskType("GVI");
			String taskDesc1=taskMsg.getTaskDesc();
			String taskDesc2=taskMsg.getTaskDesc2();
			taskMsg.setTaskDesc2(taskDesc);//任务描述	
			if(taskDesc1==null){
				taskMsg.setTaskDesc(taskDesc);//区域任务描述	
			}else{
				if (null!=taskDesc2&&taskDesc1.equals(taskDesc2)) {
					taskMsg.setTaskDesc(taskDesc);//区域任务描述	
				}
			}
			taskMsg.setReachWay(reachWay);// 接近方式
			taskMsg.setTaskInterval(taskInterval);// 标准任务间隔
			taskMsg.setOwnArea(area.getAreaId());// 所属区域
			taskMsg.setEffectiveness(zaMain.getEffectiveness());// 保存任务有效性
			taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);// 有效标志
			this.saveOrUpdate(taskMsg, operateFlg, userId);
			
			//处理分析步骤和状态
			Integer nextStep = zaStepBo.updateZa5StepAndStatus(userId, ms.getModelSeriesId(), zaId, step);
			json.element("nextStep", nextStep);
			json.element("success", true);
		} catch(Exception e){
			json.element("success", false);
			e.printStackTrace();
		}
		return json;
	}

	public IStandardRegionParamBo getStandardRegionParamBo() {
		return standardRegionParamBo;
	}

	public void setStandardRegionParamBo(
			IStandardRegionParamBo standardRegionParamBo) {
		this.standardRegionParamBo = standardRegionParamBo;
	}

	public ICusLevelBo getCusLevelBo() {
		return cusLevelBo;
	}

	public void setCusLevelBo(ICusLevelBo cusLevelBo) {
		this.cusLevelBo = cusLevelBo;
	}

	public IZa5Dao getZa5Dao() {
		return za5Dao;
	}

	public void setZa5Dao(IZa5Dao za5Dao) {
		this.za5Dao = za5Dao;
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
	
	
}
