package com.rskytech.paramdefinemanage.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.util.MatrixUtil;

public class IncreaseRegionParamAction extends BaseAction {

	
	private static final long serialVersionUID = 1336394415162344629L;
	
	private String matrixJson;// 矩阵值集合	
	private IIncreaseRegionParamBo increaseRegionParamBo;
	private String node;// 树节点id
	private Integer matrixFlg;// 矩阵区分flg
	
	
	/*************************************ZA43矩阵项目部分变量*********************/
	private String za43_item_nm_cn;
	private String za43_item_nm_en;
    private String itemFlg;// ED/AD区分
	private String itemSort;//原表里的sort
	private String itemId;//保存删除时项目ID 主键
	private String edAlgorithm;// ed算法
	private String adAlgorithm;// ad算法
	/*****************************************************************************/
	
	/*************************************ZA43矩阵级别部分变量**********************/
	private String anaflg;// 自定义分析区分flg
	private String level_nm_cn;// 自定义级别显示值(中文)

	private String level_value;// 自定义级别值
	private String item_id;// 自定义级别所属项目id
	private String level_id;// 自定义级别id
	/*****************************************************************************/	
	
   /************************************矩阵显示状态及矩阵内容********************/
	
	private String TREE_DISABLED = "true";// 主矩阵树是否可用	
	private String FIRST_MATRIX_DISABLED  = "true";// 第一副矩阵是否可用	
	private String FINAL_MATRIX_DISABLED = "true";// 自定义评级矩阵是否可用	
	private String FIRST_MATRIX_HTML = BasicTypeUtils.EMPTY_STR;// 第一矩阵html内容 		
	private String FINAL_MATRIX_HTML = BasicTypeUtils.EMPTY_STR;// 检查间隔矩阵html内容 
	private String ED_ALGORITHM = BasicTypeUtils.EMPTY_STR;// ED算法
	private String AD_ALGORITHM = BasicTypeUtils.EMPTY_STR;// AD算法
	
	/****************************************************************************/
	
	/**
	 * 初始化画面
	 */
	public String init() {
	   
		ComUser thisUser = getSysUser();
		// session过期则直接返回
		if (thisUser == null){
			return SUCCESS;
		}
		
	   // 获取机型编号 	 
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
	
	   
	   List<CusItemS45> edList = increaseRegionParamBo.searchZa43Item(modelSeriesId, ComacConstants.ED);
	   List<CusItemS45> adList = increaseRegionParamBo.searchZa43Item(modelSeriesId, ComacConstants.AD);
	   // 获取算法
	   if (edList.size() > 0){
		   ED_ALGORITHM = edList.get(0).getItemAlgorithm();
	   }	   
	   // 获取算法
	   if (adList.size() > 0){
		   AD_ALGORITHM = adList.get(0).getItemAlgorithm();
	   }
	   
	   boolean isFinalMatrixExist = increaseRegionParamBo.isFinalMatrixExist(modelSeriesId);
	   boolean isFirstMatrixExist = increaseRegionParamBo.isFirstMatrixExist(modelSeriesId);	   
	   
		// 检查间隔矩阵数据存在时
		if (isFinalMatrixExist) {
			FINAL_MATRIX_DISABLED = "false";

			// 加载第一矩阵内容
			List<CusMatrix> firstMatrixList = increaseRegionParamBo
					.searchFirstMatrix(modelSeriesId);
			FIRST_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(
					firstMatrixList, ComacConstants.FIRST_MATRIX);
			List<CusInterval> finalMatrixList = increaseRegionParamBo
					.searchFinalMatrix(modelSeriesId);
			// 加载检查间隔矩阵内容
			FINAL_MATRIX_HTML = MatrixUtil.generateZa43HtmlFinalMatrixData(
					finalMatrixList);
		} else if (isFirstMatrixExist) {// 第一副矩阵数据存在时
			FIRST_MATRIX_DISABLED = "false";
			// 加载第一矩阵内容
			List<CusMatrix> firstMatrixList = increaseRegionParamBo
					.searchFirstMatrix(modelSeriesId);
			FIRST_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(
					firstMatrixList, ComacConstants.FIRST_MATRIX);
		} else {
			TREE_DISABLED = "false";
		}			   		   
	   
       return SUCCESS;
	}	
	
	/**
	 * 初始化ZA43主矩阵树
	 * @return null
	 */
	public String treeLoad() {
        // 当前机型
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		
		String strJson;
		// 根节点时
		if (node.equals("00")){
			strJson = createEdAdJson();
		}else if (node.equals(ComacConstants.ED) || node.equals(ComacConstants.AD)){
		   // 当前节点为ED、AD节点时，加载所有项目节点
			List<CusItemS45> za43ItemList = increaseRegionParamBo.searchZa43Item(modelSeriesId, node);
			strJson = createItemJson(za43ItemList);
		}else{
			List<CusLevel> levelList = increaseRegionParamBo.getLevelList(node,ComacConstants.ZA43,modelSeriesId);
		    strJson = createLevelJson(levelList);    
		}
		
		writeToResponse(strJson);
		return null;
	}
	
	/**
	 * 保存主矩阵树节点
	 * @return null
	 * @author changyf
	 * createdate 2012-08-17
	 */
	public String saveMatrixTree(){
		CusItemS45 za4;
		String operateFlg;
		// 判断第一副矩阵是否已经生成
		boolean existFlg = increaseRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
		if (existFlg){
			writeToResponse("{success:false}");
			return null;
		}
		
		 // 当前机型		
		if (BasicTypeUtils.isNullorBlank(itemId)){
			za4 = new CusItemS45();
			za4.setItemName(za43_item_nm_cn);
			
			za4.setItemFlg(itemFlg);
			za4.setItemSort(BasicTypeUtils.parseInt(itemSort));
			za4.setStepFlg(ComacConstants.ZA43);
			za4.setValidFlag(ComacConstants.YES);
			za4.setComModelSeries(this.getComModelSeries());			
			operateFlg = ComacConstants.DB_INSERT;
		}else{		
			Object nodeObj = increaseRegionParamBo.loadById(CusItemS45.class, itemId);
			if (nodeObj.equals(null)){// 当前数据已经被删除了
				writeToResponse("{success:false}");
				return null;
			}else{
				 za4 = (CusItemS45) nodeObj;
				 za4.setItemName(za43_item_nm_cn);
			     operateFlg = ComacConstants.DB_UPDATE;
			}
		}	
		try {
			increaseRegionParamBo.saveOrUpdate(za4, operateFlg, getSysUser().getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeToResponse("{success:true}");
		return null;
	}
	
	/**
	 * 确认主矩阵自定义完成
	 * @return 执行结果信息
	 */
	public String confirmMainMatrix() {
		ComUser thisUser = getSysUser();		
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// za43判断第一副矩阵是否已经生成
		boolean existFlg = increaseRegionParamBo.isFirstMatrixExist(modelSeriesId);
		if (existFlg){
			writeToResponse("{success:false}");
			return null;
		}
		Integer levelNumberCount = increaseRegionParamBo.isLevelNumberSame(modelSeriesId);
		if (levelNumberCount >= 1){
			// 生成第一副矩阵数据
			List<CusMatrix> matrixList = increaseRegionParamBo.generateFirstMatrix(this.getComModelSeries(), thisUser.getUserId(),edAlgorithm,adAlgorithm);			
			String strHtml = MatrixUtil.generateHtmlMatrixData(matrixList,ComacConstants.FIRST_MATRIX);			
			// 生成副矩阵
			writeToResponse("{mainMatrix:\""  + strHtml + "\"}");
		}else{
			writeToResponse("{mainMatrix:false}");
		}
		return null;
	}
	
	/**
	 * 确认第一副矩阵自定义完成
	 * @return
	 */
	public String confirmFristMatrix(){
		ComUser thisUser = getSysUser();
		String html = BasicTypeUtils.EMPTY_STR;
		
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// za43判断第二副矩阵是否已经生成
		boolean existFlg = increaseRegionParamBo.isFinalMatrixExist(modelSeriesId);
		if (existFlg){
			writeToResponse("{success:false}");
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(matrixJson);
        JSONArray valueArray = jsonObj.getJSONArray("root");
        List<CusMatrix> queryList = new ArrayList<CusMatrix>();
        for(Integer i = 0; i < valueArray.size(); i++){
        	JSONObject thisJsonObj = JSONObject.fromObject(valueArray.get(i));
        	CusMatrix query = new CusMatrix();
        	query.setMatrixId(thisJsonObj.get("id").toString());
        	query.setMatrixValue(BasicTypeUtils.parseInt(thisJsonObj.get("value").toString()));
        	queryList.add(query);
        }
       
        ArrayList<Integer> valueList = new ArrayList<Integer>();
        for(CusMatrix query : queryList){
			valueList.add(query.getMatrixValue());			
		}
		// 去除第一矩阵中重复的矩阵值
		BasicTypeUtils.removeDuplicate(valueList);
		// 再将矩阵值按升序排序
		valueList = BasicTypeUtils.sortIntegerListAsc(valueList);
               
    	// 直接生成检查间隔矩阵
    	List<CusInterval> za43IntervalList = increaseRegionParamBo.generateFinalMatrix(this.getComModelSeries(), thisUser.getUserId(), queryList, valueList);         	
    	// 生成检查间隔矩阵的html
    	html = MatrixUtil.generateZa43HtmlFinalMatrixData(za43IntervalList);   
        writeToResponse("{finalMatrix:\""  + html + "\"}");
 
		return null;
	}
	
	public String confirmFinalMatrix(){
		ComUser thisUser = getSysUser();	
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = increaseRegionParamBo.isExistMatrixAnalyze(modelSeriesId, ComacConstants.ZA43);
		if (flag){
			// 存在分析数据时，提示当前矩阵不可修改
			writeToResponse("{finalMatrix:false}");
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(matrixJson);
        JSONArray valueArray = jsonObj.getJSONArray("root");
        List<CusInterval> queryList = new ArrayList<CusInterval>();
        for(Integer i = 0; i < valueArray.size(); i++){
        	JSONObject thisJsonObj = JSONObject.fromObject(valueArray.get(i));
        	CusInterval query = new CusInterval();
        	query.setIntervalId(thisJsonObj.get("id").toString());
        	query.setIntervalValue(thisJsonObj.get("value").toString());
        	queryList.add(query);
        }
        
        increaseRegionParamBo.updateFinalMatrix(queryList,thisUser.getUserId(),this.getComModelSeries());
        return null;
	}
	
	/**
	 * 删除项目节点
	 * @return
	 */
	public String delItemNode(){
		Object nodeObj = increaseRegionParamBo.loadById(CusItemS45.class,itemId);
		if (nodeObj.equals(null)){
			return null;
		}else{
			// za5判断第一副矩阵是否已经生成
			boolean existFlg = increaseRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
			CusItemS45 thisNode = (CusItemS45) nodeObj;
			// 有效flg置为否
			thisNode.setValidFlag(ComacConstants.NO);
			try {
				// 执行逻辑删除
				increaseRegionParamBo.update(thisNode,getSysUser().getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * 返回级别节点的json字符串
	 * @param za5LevelList 项目节点List
	 * @param language 语言 
	 * @return json字符串
	 */
	private String createLevelJson(List<CusLevel> za43LevelList) {
		// 节点显示值
		String displayText = BasicTypeUtils.EMPTY_STR;
		// 是否是叶节点
		boolean leaf;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("[");
		for (CusLevel cusLevel : za43LevelList) {
			// 节点显示值
			String levelNm = cusLevel.getLevelName();
			displayText = "<b>" + levelNm + "</b>";
			sb1.append("{text:'" + displayText + "'," + "id:'"
					+ cusLevel.getLevelId() + "',leaf:true,sort:'"//此时的节点排序用id即可
					+ cusLevel.getLevelId() + "',parentId:'"// 级别节点的父节点id即为itemId
					+ cusLevel.getItemId() + "',levelNmCn:'"
					+ cusLevel.getLevelName()  + "',levelValue:'"
					+ cusLevel.getLevelValue() + "',expanded:true},");
		}
		// 去掉最后一个逗号
		if (!BasicTypeUtils.isNullorBlank(sb1.toString())) {
			sb.append(sb1.substring(0, sb1.length() - 1));
		}
		sb.append("]");		
		return sb.toString();
	}
	
	
	/**
	 * 将项目节点list组合为json字符串
	 * @param za43ItemList 项目节点list
	 * @param language 语言 
	 * @return
	 */
	public String createItemJson(List<CusItemS45> za43ItemList){
		// 节点显示值
		String displayText = BasicTypeUtils.EMPTY_STR;
		// 是否是叶节点
		boolean leaf;
		// 是否存在级别节点
		boolean hasLevel = false;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("[");
		for (CusItemS45 matrixdor : za43ItemList) {			
			
			displayText = matrixdor.getItemName();
			

			List<CusLevel> za43Level = increaseRegionParamBo.getLevelList(matrixdor.getItemS45Id(),ComacConstants.ZA43,matrixdor.getComModelSeries().getModelSeriesId());
		    // 判断是否存在级别
			if (za43Level.isEmpty()){
		    	hasLevel = false;
				leaf = true;
		    }else{
		    	hasLevel = true;
		    	leaf = false;
		    }
			// 获取算法
			String algorithm = (matrixdor.getItemAlgorithm() == null?BasicTypeUtils.EMPTY_STR:matrixdor.getItemAlgorithm());
			sb1.append("{text:'" + displayText 
					+ "',hasLevel:"+ hasLevel 
					+",id:'"+ matrixdor.getItemS45Id() 
					+ "',leaf:" + leaf 
					+ ",sort:'" + matrixdor.getItemSort() 
					+ "',parentId:'" + matrixdor.getItemFlg()
					+ "',itemAlgorithm:'" + algorithm
					+ "',itemNmCn:'" + matrixdor.getItemName() 
					+ "',expanded:true},");
		}
		// 去掉最后一个逗号
		if (!BasicTypeUtils.isNullorBlank(sb1.toString())) {
			sb.append(sb1.substring(0, sb1.length() - 1));
		}
		sb.append("]");		
		return sb.toString();
	}
	
	/**
	 * 创建ED、AD节点
	 * @return
	 */
	private String createEdAdJson(){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append("{text:'" + ComacConstants.ED + "'," + "id:'"
				+ ComacConstants.ED + "',leaf:false,sort:'1'"
			    + ",expanded:true},");
		sb.append("{text:'" + ComacConstants.AD + "'," + "id:'"
				+ ComacConstants.AD + "',leaf:false,sort:'2'"
			    + ",expanded:true}");
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 撤销矩阵
	 * @return
	 */
	public String revokeMatrix(){
		ComUser thisUser = getSysUser();	
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = increaseRegionParamBo.isExistMatrixAnalyze(modelSeriesId, ComacConstants.ZA43);		
		if (flag){
			// 存在分析数据时，提示检查间隔矩阵不可撤销
			writeToResponse("{revokeFlg:false}");
			return null;
		}
		
		try {
			if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)){
				increaseRegionParamBo.deleteMatrix(modelSeriesId, ComacConstants.ZA43, ComacConstants.FIRST_MATRIX);				
			}else if (matrixFlg.equals(ComacConstants.FINAL_MATRIX)){
				increaseRegionParamBo.deleteFinalMatrix(modelSeriesId, ComacConstants.ZA43);
			}			
			// 撤销成功时，提示撤销成功
			writeToResponse("{revokeFlg:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getMatrixJson() {
		return matrixJson;
	}
	public void setMatrixJson(String matrixJson) {
		this.matrixJson = matrixJson;
	}
	public IIncreaseRegionParamBo getIncreaseRegionParamBo() {
		return increaseRegionParamBo;
	}
	public void setIncreaseRegionParamBo(
			IIncreaseRegionParamBo increaseRegionParamBo) {
		this.increaseRegionParamBo = increaseRegionParamBo;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public Integer getMatrixFlg() {
		return matrixFlg;
	}
	public void setMatrixFlg(Integer matrixFlg) {
		this.matrixFlg = matrixFlg;
	}
	public String getItemFlg() {
		return itemFlg;
	}
	public void setItemFlg(String itemFlg) {
		this.itemFlg = itemFlg;
	}
	public String getItemSort() {
		return itemSort;
	}
	public void setItemSort(String itemSort) {
		this.itemSort = itemSort;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getEdAlgorithm() {
		return edAlgorithm;
	}
	public void setEdAlgorithm(String edAlgorithm) {
		this.edAlgorithm = edAlgorithm;
	}
	public String getAdAlgorithm() {
		return adAlgorithm;
	}
	public void setAdAlgorithm(String adAlgorithm) {
		this.adAlgorithm = adAlgorithm;
	}
	public String getAnaflg() {
		return anaflg;
	}
	public void setAnaflg(String anaflg) {
		this.anaflg = anaflg;
	}
	public String getLevel_nm_cn() {
		return level_nm_cn;
	}
	public void setLevel_nm_cn(String level_nm_cn) {
		this.level_nm_cn = level_nm_cn;
	}

	public String getLevel_value() {
		return level_value;
	}
	public void setLevel_value(String level_value) {
		this.level_value = level_value;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getLevel_id() {
		return level_id;
	}
	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}
	public String getTREE_DISABLED() {
		return TREE_DISABLED;
	}
	public void setTREE_DISABLED(String tree_disabled) {
		TREE_DISABLED = tree_disabled;
	}
	public String getFIRST_MATRIX_DISABLED() {
		return FIRST_MATRIX_DISABLED;
	}
	public void setFIRST_MATRIX_DISABLED(String first_matrix_disabled) {
		FIRST_MATRIX_DISABLED = first_matrix_disabled;
	}
	public String getFINAL_MATRIX_DISABLED() {
		return FINAL_MATRIX_DISABLED;
	}
	public void setFINAL_MATRIX_DISABLED(String final_matrix_disabled) {
		FINAL_MATRIX_DISABLED = final_matrix_disabled;
	}
	public String getFIRST_MATRIX_HTML() {
		return FIRST_MATRIX_HTML;
	}
	public void setFIRST_MATRIX_HTML(String first_matrix_html) {
		FIRST_MATRIX_HTML = first_matrix_html;
	}
	public String getFINAL_MATRIX_HTML() {
		return FINAL_MATRIX_HTML;
	}
	public void setFINAL_MATRIX_HTML(String final_matrix_html) {
		FINAL_MATRIX_HTML = final_matrix_html;
	}
	public String getED_ALGORITHM() {
		return ED_ALGORITHM;
	}
	public void setED_ALGORITHM(String ed_algorithm) {
		ED_ALGORITHM = ed_algorithm;
	}
	public String getAD_ALGORITHM() {
		return AD_ALGORITHM;
	}
	public void setAD_ALGORITHM(String ad_algorithm) {
		AD_ALGORITHM = ad_algorithm;
	}

	public String getZa43_item_nm_cn() {
		return za43_item_nm_cn;
	}

	public void setZa43_item_nm_cn(String za43ItemNmCn) {
		za43_item_nm_cn = za43ItemNmCn;
	}

	public String getZa43_item_nm_en() {
		return za43_item_nm_en;
	}

	public void setZa43_item_nm_en(String za43ItemNmEn) {
		za43_item_nm_en = za43ItemNmEn;
	}

}
