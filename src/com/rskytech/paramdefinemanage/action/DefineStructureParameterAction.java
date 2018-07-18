package com.rskytech.paramdefinemanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;

public class DefineStructureParameterAction extends BaseAction{

	private static final long serialVersionUID = -4940913954265713277L;
	
	private IDefineStructureParameterBo defineStructureParameterBo;
	private String node; // 树节点
	private String itemId; // 项目Id
	private String levelValue; // 等级值
	private String levelName; // 等级名称
	
	private String levelId; // 级别Id
	private String itemFlg; // 项目标记（相当于父节点：VR PR SC EV）
	private String itemSort; // 用于自定义矩阵解析时，项目显示的先后顺序（可以不连续）
	private String itemName; // 项目中文名
	
	private String itemAlg; // 项目算法
	private String ROOTMMA; // 根节点算法
	private String VRMMA; // VR的算法
	private String SCMMA; // SC的算法
	private String PRMMA; // PR的算法
	private String EVMMA; // EV的算法
	private String RSMMA; // EV的算法
	private String LKMMA; // EV的算法
	private String EDMMA; // ED的算法
	private String ADMMA; // AD的算法
	private String stepFlg; // 分析步骤区分
	private String jsonAlg; // Json字符串存放各个树的算法
	
	
	
	/**
	 * 初始化S45自定义矩阵页面
	 * 
	 * @return 初始化自定义矩阵数据
	 */
	public String init() {
		ComUser thisUser = getSysUser();
		// session过期则直接返回
		if (thisUser == null) {
			return SUCCESS;
		}
		jsonAlg = getAllALG();
		return SUCCESS;
	}
	
	/**
	 * 获取分析步骤中的算法
	 * 
	 * @return 获取分析步骤中的算法List
	 */
	@SuppressWarnings("unchecked")
	public String getAllALG() {
		String[] stepFlgs = { "S4A", "S4B", "S5A", "S5B", "SYA", "SYB" };
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		for (int i = 0; i < stepFlgs.length; i++) {
			HashMap<String,String> hm = new HashMap<String,String>();
			List<CusEdrAdr> ceaList = defineStructureParameterBo.getCusEdrAdrList(
					modelSeriesId, stepFlgs[i]);
			if (ceaList.size() != 0) {
				hm.put("ROOT", ceaList.get(0).getAlgorithmFlg());
			} else {
				hm.put("ROOT", "");
			}
			json.element(stepFlgs[i], hm);
		}
		return json.toString();
	}
	
	
	// 加载自定义矩阵数据
	@SuppressWarnings("unchecked")
	public String loadCustom() {
		/*
		 * 加载自定义矩阵树
		 */
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<CusItemS45> itemList = defineStructureParameterBo.getS45List(modelSeriesId,
				stepFlg, node);
		List<HashMap> listJsonCI = new ArrayList<HashMap>();
		if (itemList.size() > 0) {
			for (CusItemS45 cusItem : itemList) {
				HashMap hm = new HashMap();
				String itemName = cusItem.getItemName();
				if (node.equals("0")){//加载第一级节点
					hm.put("text", itemName + "--<b>"
							+ cusItem.getItemAlgorithm() + "</b>");
					hm.put("itemAlgorithm", cusItem.getItemAlgorithm()); // 算法加粗
				} else {
					hm.put("text", itemName);
				}
				hm.put("id", cusItem.getItemS45Id());
				hm.put("itemName", cusItem.getItemName());			
				hm.put("itemSort", cusItem.getItemSort());
				hm.put("leaf", false);
				hm.put("expanded", true);
				listJsonCI.add(hm);
			}
		} else {
			// 加载级别数据
			List<CusLevel> levelList = defineStructureParameterBo.getLevelList(modelSeriesId,stepFlg, node);
			for (CusLevel cusLevel : levelList) {
				HashMap hm = new HashMap();
				String levelName = cusLevel.getLevelName();
						
				hm.put("text", "<b>" + levelName + "</b>"); // 级别加粗
				hm.put("id", cusLevel.getLevelId());
				hm.put("itemId", cusLevel.getItemId());
				hm.put("levelName", cusLevel.getLevelName());			
				hm.put("sort", cusLevel.getLevelValue());
				hm.put("leaf", true);
				hm.put("expanded", false);
				listJsonCI.add(hm);
			}
		}
		String jsonStr = JSONArray.fromObject(listJsonCI).toString();// 把jsonList转换成String
		this.writeToResponse(jsonStr);
		return null;
	}
	
	// 增加/修改项目（第二级结点）
	public String saveS45Item() {
		if (!isOkUpdate(stepFlg)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		String operateFlag;
		CusItemS45 cusItemS45 = new CusItemS45();
		if (BasicTypeUtils.isNullorBlank(itemId)) { // 为true则执行增加项目
			cusItemS45.setComModelSeries(this.getComModelSeries());
			cusItemS45.setItemFlg(itemFlg);
			cusItemS45.setItemSort(BasicTypeUtils.parseInt(itemSort));
			cusItemS45.setStepFlg(stepFlg);
			operateFlag = ComacConstants.DB_INSERT;
//			if (!itemFlg.equals(ComacConstants.MATRIX_ZEROLEVELID)) { // 增加的不是一级项目,是二级项目
				if (!checkItemCount()) { // 表示的二级数目已经超出了数据库中字段的数量
					writeToResponse("{success:\"count\"}");
					return null;
				}
//			}
		} else {
			cusItemS45 = (CusItemS45) defineStructureParameterBo.loadById(CusItemS45.class,
					itemId);
			if (cusItemS45.getItemAlgorithm() != null
					|| cusItemS45.getItemAlgorithm() == "") {
				cusItemS45.setItemAlgorithm(itemAlg);
			}
			operateFlag = ComacConstants.DB_UPDATE;
		}
		cusItemS45.setItemName(itemName);
		if (itemFlg.equals("0")) { // 第一级项目。注：只有第一级项目需要将算法存入数据库中
			cusItemS45.setItemAlgorithm(itemAlg);
		}
		cusItemS45.setValidFlag(ComacConstants.YES);
		defineStructureParameterBo.saveS45Item(cusItemS45, operateFlag, getSysUser()
				.getUserId(), this.getComModelSeries()
				.getModelSeriesId(), stepFlg);
		writeToResponse("{success:true}");
		return null;
	}
	
	/*
	 * 判断二级项目的数量是否超出数据库的存放字段的数量 
	 * 方法1: 一级项目的 itemFlg(相当于父节点的标记) 为"0",二级项目的父节点不为0,可以按照这个区别来进行查询这要就方便很多了 
	 * 方法2: 先查询出所有的一级项目,然后遍历每一个一级项目下的二级项目,
	 * 然后进行累加,此方法有点繁琐 本方法采用 方法1中的方案
	 */
	public boolean checkItemCount() {
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean temp = false;
		Integer firstItemCount = defineStructureParameterBo.checkItemCount(stepFlg,
				"0", modelSeriesId);
		if("0".equals(itemFlg)){//增加的项目是一级项目,检测其数量是否超过12个
			if(firstItemCount >= ComacConstants.S45_FIRSTITEMMAXCOUNT){	//一级项目数量已经达到最大数量12个
				return temp;
			}
		}
		Integer secondItemCount = defineStructureParameterBo.checkItemCount(stepFlg,
				null, modelSeriesId);
		if (firstItemCount + secondItemCount < ComacConstants.S45_ALLITEMMAXCOUNT) {//24
			temp = true;
		}
		return temp;
	}
	
	/*
	 * 检测级别是否重复:即数据库中是否已经存在该级别值
	 * return true:检测通过(数据库中没有该级别值)  false :检测不通过(数据库中存在该级别值)
	 */
	public boolean checkDoubleLevel(){
		List<CusLevel> list = defineStructureParameterBo.getLevelList(this.getComModelSeries()
				.getModelSeriesId(), stepFlg, itemId);
		for (CusLevel cusLevel : list) {
			if(cusLevel.getLevelValue().equals(BasicTypeUtils.parseInt(levelValue))){
				if(!levelId.equals(cusLevel.getLevelId())){		//编辑时,级别值没有修改
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * 检测级别个数，级别允许最多5个
	 * return true:检测通过(级别个数<=5)     false:检测不通过(级别个数>5)
	 * */
	public boolean checkLevelNum(){
		List<CusLevel> list = defineStructureParameterBo.getLevelList(this.getComModelSeries()
				.getModelSeriesId(), stepFlg, itemId);
		if(list!=null && list.size()>0){
			int num = list.size();
			if(num>=5){//级别个数最多5个
				return false;
			}
		}
		return true;
	}
	
	// 为S4、S5下项目增加修改级别
	public String saveS45Level() {
		if (!isOkUpdate(stepFlg)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		if(!checkDoubleLevel()){
			writeToResponse("{success:true,checkLevel:false}");
			return null;
		}
		
		
		
		String operateFlag;
		CusLevel cusLevel = new CusLevel();
		if (BasicTypeUtils.isNullorBlank(levelId)) { // 为true则执行增加级别
			//检测级别个数，级别允许最多5个
			if(!checkLevelNum()){
				writeToResponse("{success:true,checkLevelNum:false}");
				return null;			
			}
			
					
			cusLevel.setItemId(itemId);
			cusLevel.setAnaFlg(stepFlg);
			cusLevel.setComModelSeries(this.getComModelSeries());
			operateFlag = ComacConstants.DB_INSERT;
		} else { // 修改级别
			cusLevel = (CusLevel) defineStructureParameterBo.loadById(CusLevel.class, levelId);
			operateFlag = ComacConstants.DB_UPDATE;
		}
		cusLevel.setLevelValue(BasicTypeUtils.parseInt(levelValue));
		cusLevel.setLevelName(levelName);
		cusLevel.setValidFlag(ComacConstants.YES);
		defineStructureParameterBo.saveS45Level(cusLevel, operateFlag,
				getSysUser().getUserId(), this.getComModelSeries()
						.getModelSeriesId(), stepFlg);
		writeToResponse("{success:true}");
		return null;
	}
	
	// 删除树结点
	public String deleteNode() {
		if (!isOkUpdate(stepFlg)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		if (BasicTypeUtils.isNullorBlank(itemId)) { // 为true则删除等级节点，为false则删除项目节点及节点下等级数据
			CusLevel cusLevel = (CusLevel) defineStructureParameterBo.loadById(CusLevel.class,
					levelId);
			// 有效flg置为否
			cusLevel.setValidFlag(ComacConstants.NO);
			defineStructureParameterBo.deleteNodeLevel(cusLevel, getSysUser().getUserId(),
					this.getComModelSeries().getModelSeriesId(),
					stepFlg);
		} else {
			CusItemS45 cusItemS45 = (CusItemS45) defineStructureParameterBo.loadById(
					CusItemS45.class, itemId);
			// 有效flg置为否
			cusItemS45.setValidFlag(ComacConstants.NO);
			defineStructureParameterBo.deleteNode(cusItemS45, getSysUser().getUserId(),
					this.getComModelSeries().getModelSeriesId(),
					stepFlg);
		}
		writeToResponse("{success:true}");
		return null;
	}
	

	
	
	
	
	// 检测是否可以修改自定义矩阵
	public boolean isOkUpdate(String stepFlg) {
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// 判断分析步骤是否已经存在数据
		return defineStructureParameterBo.checkCusS45Mtrix(stepFlg, modelSeriesId);
	}
	
	// 检测数据是否完整
	public String checkFull() {
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		if (!BasicTypeUtils.isNullorBlank(stepFlg)) { // 验证是否为空，需要吗？总觉得没必要，这个参数在url中是写死的
			List<CusItemS45> firstItemList = defineStructureParameterBo.getS45List(
					modelSeriesId, stepFlg, ComacConstants.MATRIX_ZEROLEVELID); // 一级节点的项目
			if (firstItemList.size() == 0) { // 没有一级项目，那么数据不完整
				writeToResponse("{success:false}");
				return null;
			} else { // 存在一级项目
				  
				    int levelCount=0;//各个二级项目下的级别个数统计,要保持个数一致
				for (CusItemS45 cusItem1 : firstItemList) {
										
					List<CusItemS45> secondItemList = defineStructureParameterBo.getS45List(modelSeriesId, stepFlg, cusItem1
									.getItemS45Id().toString());
					if (secondItemList.size() == 0) { // 该一级节点没有二级项目，故数据不完整
						writeToResponse("{success:false}");
						return null;
					} else { // 该一级节点存在二级项目,则判断级别数量是否相等
						for (CusItemS45 cusItem2 : secondItemList) {
							List<CusLevel> levelList = defineStructureParameterBo.getLevelList(
									modelSeriesId, stepFlg, cusItem2
											.getItemS45Id());
							if (levelList.size() == 0) { // 该项目下没有级别，故数据不完整
								writeToResponse("{success:false}");
								return null;
							}else{
								if(levelCount==0){//记录第一个二级项目下的level级别个数
									levelCount=levelList.size();
								}else{//其余二级项目下的level级别个数  同  第一个二级项目下的level级别个数 比较
									if(levelCount!=levelList.size()){
										writeToResponse("{success:false}");
										return null;
									}
								}
							}
						}
					}
				}
			}
		}
		writeToResponse("{success:true}");
		return null;
	}
	
	// 保存S4a、S4b的数据
	public String saveAll() {
		// 检测是否可以进行修改
		if (!isOkUpdate(stepFlg)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		String operateFlag;
		// 操作EDRADR表
		List<CusEdrAdr> cusEdrAdrList = defineStructureParameterBo.getCusEdrAdrList(
				modelSeriesId, stepFlg);
		CusEdrAdr cusEdrAdr = new CusEdrAdr();
		if (cusEdrAdrList.size() == 0) { // edradr中不存在该记录，则进行增加操作
			operateFlag = ComacConstants.DB_INSERT;
			cusEdrAdr.setComModelSeries(this.getComModelSeries());
			cusEdrAdr.setStepFlg(stepFlg);
			cusEdrAdr.setAlgorithmFlg(ROOTMMA);
			cusEdrAdr.setOperateFlg(ComacConstants.VALIDFLAG_YES);
		} else { // 更新操作
			cusEdrAdr = cusEdrAdrList.get(0);
			cusEdrAdr.setAlgorithmFlg(ROOTMMA);
			cusEdrAdr.setOperateFlg(ComacConstants.VALIDFLAG_YES);
			operateFlag = ComacConstants.DB_UPDATE;
		}
		defineStructureParameterBo.saveOrUpdate(cusEdrAdr, operateFlag, getSysUser().getUserId());
		writeToResponse("{success:true}");
		return null;
	}
	
	
	
	
	
	
	
	public IDefineStructureParameterBo getDefineStructureParameterBo() {
		return defineStructureParameterBo;
	}
	public void setDefineStructureParameterBo(
			IDefineStructureParameterBo defineStructureParameterBo) {
		this.defineStructureParameterBo = defineStructureParameterBo;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getLevelValue() {
		return levelValue;
	}
	public void setLevelValue(String levelValue) {
		this.levelValue = levelValue;
	}
	
	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getLevelId() {
		return levelId;
	}
	public void setLevelId(String levelId) {
		this.levelId = levelId;
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
	
	public String getItemAlg() {
		return itemAlg;
	}
	public void setItemAlg(String itemAlg) {
		this.itemAlg = itemAlg;
	}
	public String getROOTMMA() {
		return ROOTMMA;
	}
	public void setROOTMMA(String rootmma) {
		ROOTMMA = rootmma;
	}
	public String getVRMMA() {
		return VRMMA;
	}
	public void setVRMMA(String vrmma) {
		VRMMA = vrmma;
	}
	public String getSCMMA() {
		return SCMMA;
	}
	public void setSCMMA(String scmma) {
		SCMMA = scmma;
	}
	public String getPRMMA() {
		return PRMMA;
	}
	public void setPRMMA(String prmma) {
		PRMMA = prmma;
	}
	public String getEVMMA() {
		return EVMMA;
	}
	public void setEVMMA(String evmma) {
		EVMMA = evmma;
	}
	public String getRSMMA() {
		return RSMMA;
	}
	public void setRSMMA(String rsmma) {
		RSMMA = rsmma;
	}
	public String getLKMMA() {
		return LKMMA;
	}
	public void setLKMMA(String lkmma) {
		LKMMA = lkmma;
	}
	public String getEDMMA() {
		return EDMMA;
	}
	public void setEDMMA(String edmma) {
		EDMMA = edmma;
	}
	public String getADMMA() {
		return ADMMA;
	}
	public void setADMMA(String admma) {
		ADMMA = admma;
	}
	public String getStepFlg() {
		return stepFlg;
	}
	public void setStepFlg(String stepFlg) {
		this.stepFlg = stepFlg;
	}
	public String getJsonAlg() {
		return jsonAlg;
	}
	public void setJsonAlg(String jsonAlg) {
		this.jsonAlg = jsonAlg;
	}
	

}
