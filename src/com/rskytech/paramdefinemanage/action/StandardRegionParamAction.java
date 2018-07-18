package com.rskytech.paramdefinemanage.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.util.MatrixUtil;

public class StandardRegionParamAction extends BaseAction {

	private static final long serialVersionUID = -9089622928378911790L;
	
	
	private IIncreaseRegionParamBo increaseRegionParamBo;
	private IStandardRegionParamBo standardRegionParamBo;
	private ICusLevelBo cusLevelBo;
	
	private String node;// 树节点id
	private String validFlag;//有效标志
	private Integer matrixFlg;// 矩阵区分flg
	
	/*************************************ZA5矩阵项目部分变量*********************/
	private String za5_item_nm_cn;
	private String za5_item_nm_en;
	private String algorithm;//项目算法
	private String za5_parent_id;
	private String za5_level;
	private String za5_is_leafnode;
	private String item_code;//原表里的sort
	private String itemId;//保存删除时项目ID 主键
	/*****************************************************************************/
    
	/*************************************ZA5矩阵级别部分变量**********************/
	private String anaflg;// 自定义分析区分flg
	private String level_nm_cn;// 自定义级别显示值(中文)
	private String level_nm_en;// 自定义级别显示值(英文)
	private String level_value;// 自定义级别值
	private String item_id;// 自定义级别所属项目id
	private String level_id;// 自定义级别id
	/*****************************************************************************/	
	
	/************************************矩阵显示状态及矩阵内容********************/
	
	private String TREE_DISABLED = "true";// 主矩阵树是否可用	
	private String FIRST_MATRIX_DISABLED  = "true";// 第一副矩阵是否可用	
	private String SECOND_MATRIX_DISABLED  = "true";// 第二副矩阵是否可用	
	private String FINAL_MATRIX_DISABLED = "true";// 自定义评级矩阵是否可用	
	private String FIRST_MATRIX_HTML = BasicTypeUtils.EMPTY_STR;// 第一矩阵html内容 	
	private String SECOND_MATRIX_HTML = BasicTypeUtils.EMPTY_STR;;// 第二矩阵html内容	
	private String FINAL_MATRIX_HTML = BasicTypeUtils.EMPTY_STR;;// 检查间隔矩阵html内容 
	/****************************************************************************/
	
	
	private String matrixJson;// 矩阵值集合	
	
	
	/**
	 * 初始化画面
	 * @author changyf
	 * createdate 2012-08-23
	 * @throws IOException 
	 */
	public String init() {
		ComUser thisUser = getSysUser();
		// session过期则直接返回
		if (thisUser == null){
			return SUCCESS;
		}
		
	   // 获取机型编号 	 
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();

	   
	   boolean isFinalMatrixExist = standardRegionParamBo.isFinalMatrixExist(modelSeriesId);
	   boolean isSecondMatrixExist = standardRegionParamBo.isSecondMatrixExist(modelSeriesId);
	   boolean isFirstMatrixExist = standardRegionParamBo.isFirstMatrixExist(modelSeriesId);	   
	   
	   // 检查间隔矩阵数据存在时
	   if (isFinalMatrixExist){
		   FINAL_MATRIX_DISABLED = "false";
		  		   
		   // 加载第一矩阵内容
		   List<CusMatrix> firstMatrixList = standardRegionParamBo.searchFirstMatrix(modelSeriesId); 
	       FIRST_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(firstMatrixList, ComacConstants.FIRST_MATRIX);
	       
	       List<CusMatrix> secondMatrixList = standardRegionParamBo.searchSecondMatrix(modelSeriesId);
	       // 第二矩阵内容存在时加载第二矩阵内容
	       if (secondMatrixList.size() > 0){
	    	   SECOND_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(secondMatrixList, ComacConstants.SECOND_MATRIX);   
	       }
	       
	       List<CusInterval> finalInMatrixList = standardRegionParamBo.searchFinalMatrix(modelSeriesId, ComacConstants.INNER);
	       List<CusInterval> finalOutMatrixList = standardRegionParamBo.searchFinalMatrix(modelSeriesId, ComacConstants.OUTTER);
	       HashMap<String, List<CusInterval>> hMap = new HashMap<String, List<CusInterval>>();
	       hMap.put(ComacConstants.INNER, finalInMatrixList);
	       hMap.put(ComacConstants.OUTTER, finalOutMatrixList);
	       // 加载检查间隔矩阵内容
	       FINAL_MATRIX_HTML = MatrixUtil.generateHtmlFinalMatrixData(hMap);
	   }else{		   		   
		   // 第二副矩阵数据存在时
		   if (isSecondMatrixExist){
			   SECOND_MATRIX_DISABLED = "false";
			   // 加载第一矩阵内容
			   List<CusMatrix> firstMatrixList = standardRegionParamBo.searchFirstMatrix(modelSeriesId); 
		       FIRST_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(firstMatrixList, ComacConstants.FIRST_MATRIX);
		       // 加载第二矩阵内容
		       List<CusMatrix> secondMatrixList = standardRegionParamBo.searchSecondMatrix(modelSeriesId);
		       SECOND_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(secondMatrixList, ComacConstants.SECOND_MATRIX);
		   }else{			   
			   // 第一副矩阵数据存在时
			   if (isFirstMatrixExist){
				   FIRST_MATRIX_DISABLED = "false";
				   // 加载第一矩阵内容
				   List<CusMatrix> firstMatrixList = standardRegionParamBo.searchFirstMatrix(modelSeriesId); 
			       FIRST_MATRIX_HTML = MatrixUtil.generateHtmlMatrixData(firstMatrixList, ComacConstants.FIRST_MATRIX);
			       
			   }else{
				   TREE_DISABLED = "false";
			   }			   
		   }
	   }
		
       return SUCCESS;
	}	

	/**
	 * 初始化ZA5自定义矩阵画面
	 * @return null
	 * @author changyf
	 * createdate 2012-08-17
	 */
	public String treeLoad() {
        // 当前机型
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// 当前节点编号
		String intItemId = node;		

		String strJson;
		
		List<CusItemZa5> za5ItemList = standardRegionParamBo.getMatrixList(intItemId, modelSeriesId);
		// 当前节点存在项目节点时
		if (za5ItemList.size() > 0) {
			strJson = createItemJson(za5ItemList);
		}else{
			// 当前节点不存在项目节点时取其级别节点
	        List<CusLevel> za5Level = standardRegionParamBo.getLevelList(intItemId,ComacConstants.ZA5,modelSeriesId);
	        strJson = createLevelJson(za5Level);	
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
		CusItemZa5 za5;
		String operateFlg;
		// 判断第一副矩阵是否已经生成
		boolean existFlg = standardRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
		if (existFlg){
			writeToResponse("{success:false}");
			return null;
		}
		
		 // 当前机型		
		if (BasicTypeUtils.isNullorBlank(itemId)){
			za5 = new CusItemZa5();
			za5.setItemName(za5_item_nm_cn);

			za5.setParentId(za5_parent_id);
			za5.setItemLevel(BasicTypeUtils.parseInt(za5_level));
			za5.setIsLeafNode(ComacConstants.YES);
			za5.setItemCode(item_code);	
			za5.setValidFlag(ComacConstants.YES);
			za5.setComModelSeries(this.getComModelSeries());
			
			operateFlg = ComacConstants.DB_INSERT;
		}else{		
			Object nodeObj = standardRegionParamBo.loadById(CusItemZa5.class, itemId);
			if (nodeObj.equals(null)){// 当前数据已经被删除了
				writeToResponse("{success:false}");
				return null;
			}else{
				 za5 = (CusItemZa5) nodeObj;
				 za5.setItemName(za5_item_nm_cn);
		
			     operateFlg = ComacConstants.DB_UPDATE;
			}
		}	
		// 算法不为空的情况下
		if (!BasicTypeUtils.isNullorBlank(algorithm)){
			za5.setLevel1Algorithm(algorithm);
		}
		try {
			standardRegionParamBo.saveOrUpdateZa5Item(za5, operateFlg, getSysUser()
					.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeToResponse("{success:true}");
		return null;
	}

	
	
	/**
	 * 保存级别节点
	 * @return null
	 */
	
	public String saveLevel(){		
		CusLevel cusLevel;
		String operateFlg;
		Integer intLevelValue = BasicTypeUtils.parseInt(level_value);
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		String stepFlg = request.getParameter("type");// 获取步骤区分是ZA5还是ZA43
		

		if (stepFlg.equals(ComacConstants.ZA5)) {
			// za5判断第一副矩阵是否已经生成
			boolean existFlg = standardRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
		}
		
		if (stepFlg.equals(ComacConstants.ZA43)){
			// za43判断第一副矩阵是否已经生成
			boolean existFlg = increaseRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
		}
		
		
        
		if (BasicTypeUtils.isNullorBlank(level_id)){
			cusLevel = new CusLevel();
			cusLevel.setLevelName(level_nm_cn);

			
			cusLevel.setLevelValue(intLevelValue);
			cusLevel.setItemId(item_id);
			
			
			// 检查级别值是否重复 
			if (checkAddLevelValue(item_id,stepFlg,this.getComModelSeries().getModelSeriesId(),intLevelValue)){
				writeToResponse("{success:false,doubleLevelErr:true}");
				return null;
			}	
			
			//检查级别个数 最多5级
			if(!checkLevelNum(item_id,stepFlg,this.getComModelSeries().getModelSeriesId())){
				writeToResponse("{success:false,levelCountErr:true}");
				return null;
			}
			
			
			
			cusLevel.setAnaFlg(stepFlg);
			cusLevel.setValidFlag(ComacConstants.YES);
			cusLevel.setComModelSeries(this.getComModelSeries());
			operateFlg = ComacConstants.DB_INSERT;
		}else{		
			Object nodeObj = cusLevelBo.loadById(CusLevel.class, level_id);
			if (nodeObj.equals(null)){// 数据已被删除时
				writeToResponse("{success:false}");
				return null;
			}else{
				cusLevel = (CusLevel) nodeObj;
				// 检查级别值是否重复
				if (checkUpdateLevelValue(cusLevel.getItemId(),stepFlg,this.getComModelSeries().getModelSeriesId(),intLevelValue,level_id)){
					writeToResponse("{success:false,doubleLevelErr:true}");
					return null;
				}
				
				 
				 cusLevel.setLevelName(level_nm_cn);
		
				 cusLevel.setLevelValue(intLevelValue);
			     operateFlg = ComacConstants.DB_UPDATE;
			}
		}	
		try {
			cusLevelBo.saveOrUpdate(cusLevel, operateFlg, getSysUser()
					.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeToResponse("{success:true}");
		return null;
	}
	
	/**
	 * 删除项目节点
	 * @return
	 * @author changyf 
	 * createdate 2012-08-18
	 */
	public String delItemNode(){
		Object nodeObj = standardRegionParamBo.loadById(CusItemZa5.class, itemId);
		if (nodeObj.equals(null)){
			writeToResponse("{success:false}");
			return null;
		}else{
			// za5判断第一副矩阵是否已经生成
			boolean existFlg = standardRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
			CusItemZa5 thisNode = (CusItemZa5) nodeObj;
			// 有效flg置为否
			thisNode.setValidFlag(ComacConstants.NO);
			try {
				// 执行逻辑删除
				standardRegionParamBo.deleteZa5Item(thisNode,getSysUser().getUserId(),this.getComModelSeries().getModelSeriesId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * 删除级别节点
	 * @return
	 */
	public String delLevelNode(){
		Object nodeObj = cusLevelBo.loadById(CusLevel.class, level_id);
		if (nodeObj.equals(null)){
			writeToResponse("{success:false}");
			return null;
		}else{
			
			ActionContext ctx = ActionContext.getContext();
			HttpServletRequest request = (HttpServletRequest) ctx
					.get(ServletActionContext.HTTP_REQUEST);
			String stepFlg = request.getParameter("type");// 获取步骤区分是ZA5还是ZA43

			if (stepFlg.equals(ComacConstants.ZA5)) {
				// za5判断第一副矩阵是否已经生成
				boolean existFlg = standardRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
				if (existFlg){
					writeToResponse("{success:false}");
					return null;
				}
			}
			
			if (stepFlg.equals(ComacConstants.ZA43)){
				// za43判断第一副矩阵是否已经生成
				boolean existFlg = increaseRegionParamBo.isFirstMatrixExist(this.getComModelSeries().getModelSeriesId());
				if (existFlg){
					writeToResponse("{success:false}");
					return null;
				}
			}
			
			CusLevel thisNode = (CusLevel) nodeObj;
			// 有效flg置为否
			thisNode.setValidFlag(ComacConstants.NO);
			try {
				// 执行逻辑删除
				standardRegionParamBo.update(thisNode,getSysUser().getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 检查级别的个数，最多允许5级
	 * return true:检测通过(级别个数<=5)     false:检测不通过(级别个数>=5)
	 * */
	
	private boolean checkLevelNum(String itemId,String stepFlg,String modelSeriesId){
		List<CusLevel> levelList = cusLevelBo.getLevelList(modelSeriesId, stepFlg, itemId);
	    if(levelList!=null && levelList.size()>0){
	    	int num = levelList.size();
	    	if(num >= 5){//级别个数最多5个
	    		return false;
	    	}
	    }
	    return true;
	}
	
	/**
	 * 追加时检查级别值是否存在重复
	 * @param itemId 项目id
	 * @param stepFlg ZA5/ZA43区分
	 * @param modelSeriesId 机型id
	 * @param levelValue 级别值
	 * @return
	 */
	private boolean checkAddLevelValue(String itemId,String stepFlg,String modelSeriesId,Integer levelValue){
		List<CusLevel> levelList = cusLevelBo.getLevelList(modelSeriesId, stepFlg, itemId);
	    for (CusLevel cusLevel : levelList) {
			if (cusLevel.getLevelValue().equals(levelValue)){
				return true;
			}
		}
	    return false;
	}
	
	/**
	 * 修改时检查级别值是否冲
	 * @param itemId
	 * @param stepFlg
	 * @param modelSeriesId
	 * @param levelValue
	 * @param levelId
	 * @return
	 */
	private boolean checkUpdateLevelValue(String itemId,String stepFlg,String modelSeriesId,Integer levelValue,String levelId){
		List<CusLevel> levelList = cusLevelBo.getLevelList(modelSeriesId, stepFlg, itemId);
	    for (CusLevel cusLevel : levelList) {
			if (!cusLevel.getLevelId().equals(levelId) && cusLevel.getLevelValue().equals(levelValue)){
				return true;
			}
		}
	    return false;
	}
	
	
	/**
	 * 确认主矩阵自定义完成
	 * @return 执行结果信息
	 */
	public String confirmMainMatrix() {
		ComUser thisUser = getSysUser();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();		
		// za5判断第一副矩阵是否已经生成
		boolean existFlg = standardRegionParamBo.isFirstMatrixExist(modelSeriesId);
		if (existFlg){
			writeToResponse("{success:false}");
			return null;
		}
		Integer levelNumberCount = standardRegionParamBo.isLevelNumberSame(modelSeriesId);
		// 级别节点存在并且相等而且个数超过1个时
		if (levelNumberCount >= 1){
			// 生成第一副矩阵数据
			List<CusMatrix> matrixList = standardRegionParamBo.generateFirstMatrix(modelSeriesId, thisUser.getUserId());			
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
        
        // 取主矩阵树上第一级节点list
        List<CusItemZa5> level1List = standardRegionParamBo.getLevel1ItemData(modelSeriesId);
                
        if (level1List.size() == 3){// 存在第二级副矩阵时生成
        	// za5判断第二副矩阵是否已经生成
			boolean existFlg = standardRegionParamBo.isSecondMatrixExist(modelSeriesId);
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
        	Integer levelCount = standardRegionParamBo.isLevelNumberSame(modelSeriesId);
        	List<CusMatrix> secondMatrixsList = standardRegionParamBo.generateSecondMatrix(this.getComModelSeries(), thisUser.getUserId(), queryList, valueList,level1List, levelCount);
        	// 生成第二副矩阵的html
        	html = MatrixUtil.generateHtmlMatrixData(secondMatrixsList,ComacConstants.SECOND_MATRIX);   
            writeToResponse("{secondMatrix:\""  + html + "\"}");
        }else{
        	// za5判断检查间隔矩阵是否已经生成
			boolean existFlg = standardRegionParamBo.isFinalMatrixExist(modelSeriesId);
			if (existFlg){
				writeToResponse("{success:false}");
				return null;
			}
        	// 直接生成检查间隔矩阵
        	HashMap<String, List<CusInterval>> hMap = standardRegionParamBo.generateFinalMatrix(this.getComModelSeries(), thisUser.getUserId(), queryList, valueList);         	
        	// 生成检查间隔矩阵的html
        	html = MatrixUtil.generateHtmlFinalMatrixData(hMap);   
            writeToResponse("{finalMatrix:\""  + html + "\"}");
        }
        
		return null;
	}
	
	/**
	 * 确认第二副矩阵自定义完成
	 * @return
	 */
	public String confirmSecondMatrix(){
		ComUser thisUser = getSysUser();
		String html = BasicTypeUtils.EMPTY_STR;
		
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// za5判断第一副矩阵是否已经生成
		boolean existFlg = standardRegionParamBo.isFinalMatrixExist(modelSeriesId);
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
		// 去除第二矩阵中重复的矩阵值
		BasicTypeUtils.removeDuplicate(valueList);
		// 再将矩阵值按升序排序
		valueList = BasicTypeUtils.sortIntegerListAsc(valueList);
                
		HashMap<String, List<CusInterval>> hMap = standardRegionParamBo.generateFinalMatrix(this.getComModelSeries(), thisUser.getUserId(), queryList, valueList); 
        	
    	// 生成检查间隔矩阵的html
    	html = MatrixUtil.generateHtmlFinalMatrixData(hMap);   
        writeToResponse("{finalMatrix:\""  + html + "\"}");        
		return null;
	}
	
	/**
	 * 确认检查间隔矩阵自定义完成
	 * @return
	 */
	public String confirmFinalMatrix(){
		ComUser thisUser = getSysUser();	
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		
		boolean flag = standardRegionParamBo.isExistMatrixAnalyze(modelSeriesId, ComacConstants.ZA5);
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
        
        standardRegionParamBo.updateFinalMatrix(queryList,thisUser.getUserId(),this.getComModelSeries());
        return null;
	}
	/**
	 * 撤销矩阵
	 * @return
	 */
	public String revokeMatrix(){
		ComUser thisUser = getSysUser();	
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = standardRegionParamBo.isExistMatrixAnalyze(modelSeriesId, ComacConstants.ZA5);		
		if (flag){
			// 存在分析数据时，提示检查间隔矩阵不可撤销
			writeToResponse("{revokeFlg:false}");
			return null;
		}
		
		try {
			if (matrixFlg.equals(ComacConstants.FIRST_MATRIX)){
				standardRegionParamBo.deleteMatrix(modelSeriesId, ComacConstants.ZA5, ComacConstants.FIRST_MATRIX);				
			}else if (matrixFlg.equals(ComacConstants.SECOND_MATRIX)){
				standardRegionParamBo.deleteMatrix(modelSeriesId, ComacConstants.ZA5, ComacConstants.SECOND_MATRIX);
			}else if (matrixFlg.equals(ComacConstants.FINAL_MATRIX)){
				standardRegionParamBo.deleteFinalMatrix(modelSeriesId, ComacConstants.ZA5);
			}			
			// 撤销成功时，提示撤销成功
			writeToResponse("{revokeFlg:true}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 返回项目节点的json字符串
	 * @param za5MatrixList 项目节点List
	 * @param language 语言
	 * @return json字符串
	 */
	private String createItemJson(List<CusItemZa5> za5MatrixList) {
		// 节点显示值
		String displayText = BasicTypeUtils.EMPTY_STR;
		// 是否是叶节点
		boolean leaf;
		// 是否存在级别节点
		boolean hasLevel = false;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("[");
		for (CusItemZa5 matrixdor : za5MatrixList) {
			// 节点显示值
			//displayText = matrixdor.getItemZa5Id() + "-" + matrixdor.getParentId() + "-" + matrixdor.getItemCode();
			
				displayText = matrixdor.getItemName();
			
			// 是否是叶节点
			if (matrixdor.getIsLeafNode().equals(ComacConstants.YES)){
				List<CusLevel> za5Level = standardRegionParamBo.getLevelList(matrixdor.getItemZa5Id(),ComacConstants.ZA5,matrixdor.getComModelSeries().getModelSeriesId());
			    // 判断是否存在级别
				if (za5Level.isEmpty()){
			    	hasLevel = false;
					leaf = true;
			    }else{
			    	hasLevel = true;
			    	leaf = false;
			    }
			}else{
				hasLevel = false;
				leaf = false;
			}
			// 获取算法
			String level1Algorithm = (matrixdor.getLevel1Algorithm() == null?BasicTypeUtils.EMPTY_STR:matrixdor.getLevel1Algorithm());
			sb1.append("{text:'" + displayText 
					+ "',hasLevel:"+ hasLevel 
					+",id:'"+ matrixdor.getItemZa5Id() 
					+ "',leaf:" + leaf 
					+ ",sort:'" + matrixdor.getItemCode() 
					+ "',parentId:'" + matrixdor.getParentId()
					+ "',itemAlgorithm:'" + level1Algorithm
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
	 * 返回级别节点的json字符串
	 * @param za5LevelList 项目节点List
	 * @param language 语言 
	 * @return json字符串
	 */
	private String createLevelJson(List<CusLevel> za5LevelList) {
		// 节点显示值
		String displayText = BasicTypeUtils.EMPTY_STR;
		// 是否是叶节点
		boolean leaf;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb1 = new StringBuffer();
		sb.append("[");
		for (CusLevel cusLevel : za5LevelList) {
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
	
	
	
	
	
	
	
	
	
	public IIncreaseRegionParamBo getIncreaseRegionParamBo() {
		return increaseRegionParamBo;
	}
	public void setIncreaseRegionParamBo(
			IIncreaseRegionParamBo increaseRegionParamBo) {
		this.increaseRegionParamBo = increaseRegionParamBo;
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


	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getValidFlag() {
		return validFlag;
	}
	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}
	public Integer getMatrixFlg() {
		return matrixFlg;
	}
	public void setMatrixFlg(Integer matrixFlg) {
		this.matrixFlg = matrixFlg;
	}
	public String getZa5_item_nm_cn() {
		return za5_item_nm_cn;
	}
	public void setZa5_item_nm_cn(String za5_item_nm_cn) {
		this.za5_item_nm_cn = za5_item_nm_cn;
	}
	public String getZa5_item_nm_en() {
		return za5_item_nm_en;
	}
	public void setZa5_item_nm_en(String za5_item_nm_en) {
		this.za5_item_nm_en = za5_item_nm_en;
	}
	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	public String getZa5_parent_id() {
		return za5_parent_id;
	}
	public void setZa5_parent_id(String za5_parent_id) {
		this.za5_parent_id = za5_parent_id;
	}
	public String getZa5_level() {
		return za5_level;
	}
	public void setZa5_level(String za5_level) {
		this.za5_level = za5_level;
	}
	public String getZa5_is_leafnode() {
		return za5_is_leafnode;
	}
	public void setZa5_is_leafnode(String za5_is_leafnode) {
		this.za5_is_leafnode = za5_is_leafnode;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
	public String getLevel_nm_en() {
		return level_nm_en;
	}
	public void setLevel_nm_en(String level_nm_en) {
		this.level_nm_en = level_nm_en;
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
	public String getSECOND_MATRIX_DISABLED() {
		return SECOND_MATRIX_DISABLED;
	}
	public void setSECOND_MATRIX_DISABLED(String second_matrix_disabled) {
		SECOND_MATRIX_DISABLED = second_matrix_disabled;
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
	public String getSECOND_MATRIX_HTML() {
		return SECOND_MATRIX_HTML;
	}
	public void setSECOND_MATRIX_HTML(String second_matrix_html) {
		SECOND_MATRIX_HTML = second_matrix_html;
	}
	public String getFINAL_MATRIX_HTML() {
		return FINAL_MATRIX_HTML;
	}
	public void setFINAL_MATRIX_HTML(String final_matrix_html) {
		FINAL_MATRIX_HTML = final_matrix_html;
	}
	public String getMatrixJson() {
		return matrixJson;
	}
	public void setMatrixJson(String matrixJson) {
		this.matrixJson = matrixJson;
	}
	
	
	

}
