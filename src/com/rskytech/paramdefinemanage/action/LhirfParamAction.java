package com.rskytech.paramdefinemanage.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.ILhirfParamBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;

public class LhirfParamAction extends BaseAction {

	private static final long serialVersionUID = 2482102287037171452L;
	
	private ICusLevelBo  cusLevelBo;
	private ICusEdrAdrBo cusEdrAdrBo;
	private ILhirfParamBo lhirfParamBo;
	
	private String node; // 树节点
	private String itemId; // 项目Id
	private String levelValue; // 等级值
	private String levelNameCn; // 等级名称
	private String levelNameEn; // 等级名称
	private String levelId; // 级别Id
	private String itemFlg; // 项目标记（相当于父节点：VR PR SC EV）
	private String itemSort; // 用于自定义矩阵解析时，项目显示的先后顺序（可以不连续）
	private String itemNameCn; // 项目中文名
	private String ROOTMMA; // 根节点算法
	private String EDMMA; // ED的算法
	private String ADMMA; // AD的算法
	private String stemFlg; // 分析步骤区分
	private String jsonAlg; // Json字符串存放各个树的算法
	private Integer lh4Isfull; // lh4自定义矩阵是否完整
	
	
	
	
	/**
	 * 初始化Lh4自定义矩阵页面
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
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		List<CusEdrAdr> list = cusEdrAdrBo.getCusEdrAdrList(modelSeriesId,
				ComacConstants.LH4);
		if(list!=null && list.size()>0){
			Integer operateFlag = list.get(0).getOperateFlg();
			if(operateFlag!=null && operateFlag.equals(1)){
				lh4Isfull = ComacConstants.CUSEDRADR_Full_YES;
			}else{
				lh4Isfull = ComacConstants.CUSEDRADR_Full_NO;
			}
		}else{
			lh4Isfull = ComacConstants.CUSEDRADR_Full_NO;
		}
		
		return SUCCESS;
	}

	/**
	 * 初始化评级表维护页面Lh5
	 * 
	 * @return 初始化自定义评级表数据
	 */
	@SuppressWarnings("unchecked")
	public String loadLh5() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		Object[] obs = { "A", "B", "LH5",
				this.getComModelSeries().getModelSeriesId() };
		List<Object[]> cusIntervalList = lhirfParamBo
				.getCusIntervalList(obs);
		if (cusIntervalList != null) {
			for (Object[] ob : cusIntervalList) {
				HashMap jsonFeildList = new HashMap();
				jsonFeildList.put("level", ob[0]);
				jsonFeildList.put("valueA", ob[1]);
				jsonFeildList.put("valueB", ob[2]);
				jsonFeildList.put("intervalIdA", ob[3]);
				jsonFeildList.put("intervalIdB", ob[4]);
				listJsonFV.add(jsonFeildList);
			}
		}
		json.element("rootLh5", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 获取分析步骤中的算法
	 * 
	 * @return 获取分析步骤中的算法List
	 */
	@SuppressWarnings("unchecked")
	public String getAllALG() {
		String[] itemFlgs = { "ED", "AD" };
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		HashMap hm = new HashMap();
		List<CusEdrAdr> ceaList = cusEdrAdrBo.getCusEdrAdrList(modelSeriesId,
				ComacConstants.LH4);
		if (ceaList.size() != 0) {
			hm.put("ROOT", ceaList.get(0).getAlgorithmFlg());
		} else {
			hm.put("ROOT", "");
		}
		for (int j = 0; j < itemFlgs.length; j++) {
			List<CusItemS45> list = lhirfParamBo.getAlgList(
					ComacConstants.LH4, itemFlgs[j], modelSeriesId);
			if (list.size() != 0) {
				hm.put(itemFlgs[j], list.get(0).getItemAlgorithm());
			} else {
				hm.put(itemFlgs[j], "");
			}
			json.element(ComacConstants.LH4, hm);
		}
		System.out.println(json.toString());
		return json.toString();
	}

	// 加载自定义矩阵数据
	@SuppressWarnings("unchecked")
	public String loadCustom() {
		// modelSeriesId=getSysUser().getComModelSeries().getModelSeriesId();
		// //直接获取登入系统所选机型
		/*
		 * 加载自定义矩阵第二级第三级数据
		 */
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		String[] itmsFlgs = { "ED", "AD" };
		for (int i = 0; i < itmsFlgs.length; i++) {
			if (node.equals(itmsFlgs[i])) { // 判断是否是加载第二级数据
				List<CusItemS45> iteList = lhirfParamBo.getS45List(
						modelSeriesId, stemFlg, node);
				List<HashMap> listJsonCI = new ArrayList<HashMap>();
				for (CusItemS45 cusItem : iteList) {
					HashMap hm = new HashMap();
					String itemName =  cusItem.getItemName();
							
					hm.put("text", itemName);
					hm.put("id", cusItem.getItemS45Id());
					hm.put("itemNameCn", cusItem.getItemName());
				
					hm.put("itemSort", cusItem.getItemSort());
					hm.put("leaf", false);
					hm.put("expanded", true);
					listJsonCI.add(hm);
				}
				String jsonStr = JSONArray.fromObject(listJsonCI).toString();// 把jsonList转换成String
				this.writeToResponse(jsonStr);
				return null; // 第二级节点加载完成返回去
			}
		}
		// 加载第三级数据
		List<CusLevel> levelList = cusLevelBo.getLevelList(modelSeriesId,
				stemFlg, node);
		List<HashMap> listJsonCL = new ArrayList<HashMap>();
		for (CusLevel cusLevel : levelList) {
			HashMap hm = new HashMap();
			String levelName =  cusLevel.getLevelName() ;
			hm.put("text", "<b>" + levelName + "</b>");
			hm.put("id", cusLevel.getLevelId());
			hm.put("itemId", cusLevel.getItemId());
			hm.put("levelNameCn", cusLevel.getLevelName());
			
			hm.put("sort", cusLevel.getLevelValue());
			hm.put("leaf", true);
			hm.put("expanded", false);
			listJsonCL.add(hm);
		}
		String jsonStr = JSONArray.fromObject(listJsonCL).toString();// 把jsonList转换成String
		this.writeToResponse(jsonStr);
		return null;
	}

	// 增加/修改项目（第二级结点）
	public String saveS45Item() {

		String operateFlag;
		CusItemS45 cusItemS45 = new CusItemS45();
		if (BasicTypeUtils.isNullorBlank(itemId)) { // 为true则执行增加项目
			cusItemS45.setComModelSeries(this.getComModelSeries());
			cusItemS45.setItemFlg(itemFlg);
			cusItemS45.setItemSort(BasicTypeUtils.parseInt(itemSort));
			cusItemS45.setStepFlg(stemFlg);
			operateFlag = ComacConstants.DB_INSERT;
		} else {
			cusItemS45 = (CusItemS45) lhirfParamBo.loadById(CusItemS45.class,
					itemId);
			operateFlag = ComacConstants.DB_UPDATE;
		}
		cusItemS45.setItemName(itemNameCn);
	
		cusItemS45.setValidFlag(ComacConstants.YES);
		lhirfParamBo.saveS45Item(cusItemS45, operateFlag, getSysUser()
				.getUserId(), this.getComModelSeries()
				.getModelSeriesId(), ComacConstants.LH4);
		writeToResponse("{success:true}");
		return null;
	}

	/*
	 * 检测级别是否重复:即数据库中是否已经存在该级别值 return true:检测通过(数据库中没有该级别值) false
	 * :检测不通过(数据库中存在该级别值)
	 */
	public boolean checkDoubleLevel() {
		List<CusLevel> list = cusLevelBo.getLevelList(this
				.getComModelSeries().getModelSeriesId(), ComacConstants.LH4,
				itemId);
		for (CusLevel cusLevel : list) {
			if (cusLevel.getLevelValue().equals(
					BasicTypeUtils.parseInt(levelValue))) {
				if (!levelId.equals(cusLevel.getLevelId())) { // 编辑时,级别值没有修改
					return false;
				}
			}
		}
		return true;
	}

	// 为LH4下项目增加修改级别
	public String saveS45Level() {
		if (!checkDoubleLevel()) {
			writeToResponse("{success:true,checkLevel:false}");
			return null;
		}
		String operateFlag;
		CusLevel cusLevel = new CusLevel();
		if (BasicTypeUtils.isNullorBlank(levelId)) { // 为true则执行增加级别
			cusLevel.setItemId(itemId);
			cusLevel.setAnaFlg(stemFlg);
			cusLevel.setComModelSeries(this.getComModelSeries());
			operateFlag = ComacConstants.DB_INSERT;
		} else { // 修改级别
			cusLevel = (CusLevel) cusLevelBo.loadById(CusLevel.class, levelId);
			operateFlag = ComacConstants.DB_UPDATE;
		}
		cusLevel.setLevelValue(BasicTypeUtils.parseInt(levelValue));
		cusLevel.setLevelName(levelNameCn);

		cusLevel.setValidFlag(ComacConstants.YES);
		cusLevelBo.saveS45Level(cusLevel, operateFlag,
				getSysUser().getUserId(), this.getComModelSeries()
						.getModelSeriesId(), ComacConstants.LH4);
		writeToResponse("{success:true}");
		return null;
	}

	// 删除树结点
	public String deleteNode() {

		if (BasicTypeUtils.isNullorBlank(itemId)) { // 为true则删除等级节点，为false则删除项目节点及节点下等级数据
			CusLevel cusLevel = (CusLevel) cusLevelBo.loadById(CusLevel.class,
					levelId);
			// 有效flg置为否
			cusLevel.setValidFlag(ComacConstants.NO);
			// 执行逻辑删除
			cusLevelBo.deleteNode(cusLevel, getSysUser().getUserId(),
					this.getComModelSeries().getModelSeriesId(),
					ComacConstants.LH4);
		} else { // 删除级别节点
			CusItemS45 cusItemS45 = (CusItemS45) lhirfParamBo.loadById(
					CusItemS45.class, itemId);
			cusItemS45.setValidFlag(ComacConstants.NO);
			// 执行逻辑删除
			lhirfParamBo.deleteNode(cusItemS45, getSysUser().getUserId(),
					this.getComModelSeries().getModelSeriesId(),
					ComacConstants.LH4);
		}
		writeToResponse("{success:true}");
		return null;
	}

	// 检测是否可以修改自定义矩阵
	public boolean isOkUpdate(String stemFlg) {
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		// 判断分析步骤是否已经存在数据
		return lhirfParamBo.checkCusS45Mtrix(stemFlg, modelSeriesId);
	}

	// 检测数据是否完整
	public String checkFull() {

		List<Integer> levelValueList = new ArrayList<Integer>();
		String[] algorithms = new String[2];
		if (stemFlg.equals(ComacConstants.LH4)) {
			String[] algorithms2 = { "ED", "AD" };
			algorithms = algorithms2;
		}
		for (int i = 0; i < algorithms.length; i++) {
			List<CusItemS45> cis45list = lhirfParamBo.getS45List(this
					.getComModelSeries().getModelSeriesId(), stemFlg,
					algorithms[i]);
			if (cis45list.size() == 0) { // 判断属性（ED、AD）下面是否有项目
				writeToResponse("{success:false}");
				return null;
			}
			for (int j = 0; j < cis45list.size(); j++) { // 判断项目下面是否有级别
				List<CusLevel> levelList = cusLevelBo.getLevelList(this
						.getComModelSeries().getModelSeriesId(), stemFlg,
						cis45list.get(j).getItemS45Id());
				if (levelList.size() == 0) {
					writeToResponse("{success:false}");
					return null;
				}
				if (stemFlg.equals(ComacConstants.LH4)) { // LH4需要判断每个项目的级别数是否相同
					if (levelValueList.size() == 0) {
						for (int k = 0; k < levelList.size(); k++) {
							levelValueList
									.add(levelList.get(0).getLevelValue());
						}
					} else {
						if (levelList.size() != levelValueList.size()) {
							writeToResponse("{success:false}");
							return null;
						}
					}
				}
			}
		}
		writeToResponse("{success:" + levelValueList.size() + "}");
		return null;
	}

	// 保存LH4的数据
	public String saveAll() {

		lhirfParamBo.saveLH4(this.getSysUser(),this.getComModelSeries(), EDMMA, ADMMA, ROOTMMA);
		writeToResponse("{success:true}");
		return null;
	}

	/*
	 * LH5 回退
	 */
	public String finalBackUrl() {
		if (!isOkUpdate(ComacConstants.LH4)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		String modelSeriesId = this.getComModelSeries()
				.getModelSeriesId();
		String userId = getSysUser().getUserId();
		lhirfParamBo.finalBackLh5(modelSeriesId, userId);
		writeToResponse("{success:true}");
		return null;
	}

	/*
	 * LH5保存
	 */
	public String saveLh5() {
		if (!isOkUpdate(ComacConstants.LH4)) {
			writeToResponse("{success:true,checkS45:false}");
			return null;
		}
		String jsonData = this.getJsonData();
		lhirfParamBo.saveLh5(this.getSysUser(),this.getComModelSeries(), jsonData);
		writeToResponse("{success:true}");
		return null;
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
	
	

	public ILhirfParamBo getLhirfParamBo() {
		return lhirfParamBo;
	}

	public void setLhirfParamBo(ILhirfParamBo lhirfParamBo) {
		this.lhirfParamBo = lhirfParamBo;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(String levelValue) {
		this.levelValue = levelValue;
	}

	public String getLevelNameCn() {
		return levelNameCn;
	}

	public void setLevelNameCn(String levelNameCn) {
		this.levelNameCn = levelNameCn;
	}

	public String getLevelNameEn() {
		return levelNameEn;
	}

	public void setLevelNameEn(String levelNameEn) {
		this.levelNameEn = levelNameEn;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
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

	public String getItemNameCn() {
		return itemNameCn;
	}

	public void setItemNameCn(String itemNameCn) {
		this.itemNameCn = itemNameCn;
	}


	public String getStemFlg() {
		return stemFlg;
	}

	public void setStemFlg(String stemFlg) {
		this.stemFlg = stemFlg;
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

	public String getROOTMMA() {
		return ROOTMMA;
	}

	public void setROOTMMA(String rootmma) {
		ROOTMMA = rootmma;
	}

	public String getJsonAlg() {
		return jsonAlg;
	}

	public void setJsonAlg(String jsonAlg) {
		this.jsonAlg = jsonAlg;
	}



	public Integer getLh4Isfull() {
		return lh4Isfull;
	}

	public void setLh4Isfull(Integer lh4Isfull) {
		this.lh4Isfull = lh4Isfull;
	}

}
