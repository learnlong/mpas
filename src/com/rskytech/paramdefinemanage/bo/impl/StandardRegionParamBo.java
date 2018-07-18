package com.rskytech.paramdefinemanage.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.paramdefinemanage.dao.IStandardRegionParamDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemZa5;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.ZaStep;

public class StandardRegionParamBo extends BaseBO implements IStandardRegionParamBo{

	private IStandardRegionParamDao standardRegionParamDao;
	
	
	public IStandardRegionParamDao getStandardRegionParamDao() {
		return standardRegionParamDao;
	}

	public void setStandardRegionParamDao(
			IStandardRegionParamDao standardRegionParamDao) {
		this.standardRegionParamDao = standardRegionParamDao;
	}
	
	
	
	@Override
	public List<CusItemZa5> getMatrixList(String parentId,String modelSeriesId) {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemZa5.class);
		dc.add(Restrictions.eq("parentId", parentId));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId));
		// 只取有效的
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		// 用项目序号排一下序
		dc.addOrder(Order.asc("itemCode"));
		return this.findByCritera(dc);
	}

	@Override
	public List<CusLevel> getLevelList(String itemId,String stepFlg,String modelSeriesId) {
		DetachedCriteria dc = DetachedCriteria.forClass(CusLevel.class);
		dc.add(Restrictions.eq("itemId", itemId));
		dc.add(Restrictions.eq("anaFlg", stepFlg));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		
		// 只取有效的
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		// 用级别值排一下序
		dc.addOrder(Order.asc("levelValue"));
		return this.findByCritera(dc);
	}

	@Override
	public Integer isLevelNumberSame(String modelSeriesId) {
		Integer tempCount = 0;
		// 取得当前机型下za5矩阵有效的级别节点个数中不重复的数据
		List levelCountList = standardRegionParamDao.getLevelCount(modelSeriesId,ComacConstants.YES);

		if (levelCountList == null || levelCountList.size() <= 0){
			// 如果数据为空或个数为0，表示当前机型没有za5自定义主矩阵数据
			return -1;
		}else if (levelCountList.size() == 1){
			BigDecimal count = (BigDecimal)levelCountList.get(0);
			// 如果个数为0证明该za5主矩阵没有级别节点
			if (count.intValue() == 0){
				return -1;
			}else{
				return count.intValue();
			}
		}else{ // 如果以上数据个数超过1条,证明存在有项目节点下的级别节点个数不一致
			return -1;
		}		
	}

	@Override
	public List<CusMatrix> generateFirstMatrix(String modelSeriesId,
			String userId) {

		List<CusMatrix> matrixList = createFirstMatrixData(modelSeriesId);
		// 保存第一矩阵数据
		try {
			for (CusMatrix mat : matrixList) {
				this.saveOrUpdate(mat, ComacConstants.DB_INSERT, userId);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
        
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA5));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		// 取第一矩阵数据
		dc.add(Restrictions.eq("matrixFlg", ComacConstants.FIRST_MATRIX));
		// 按行号排序
		dc.addOrder(Order.asc("matrixRow"));
		// 按列号排序
		dc.addOrder(Order.asc("matrixCol"));
		return this.findByCritera(dc);
	}
	
	@Override
	public boolean isFinalMatrixExist(String modelSeriesId) {	
		List resultList = searchFinalMatrix(modelSeriesId,BasicTypeUtils.EMPTY_STR);		
		if (resultList.size() > 0){
			return true;
		}
		return false;
	}
	

	@Override
	public boolean isFirstMatrixExist(String modelSeriesId) {
		List resultList = searchFirstMatrix(modelSeriesId);
		if (resultList.size() > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<CusMatrix> searchFirstMatrix(String modelSeriesId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA5));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		// 取第一矩阵数据
		dc.add(Restrictions.eq("matrixFlg", ComacConstants.FIRST_MATRIX));
		// 按行号排序
		dc.addOrder(Order.asc("matrixRow"));
		// 按列号排序
		dc.addOrder(Order.asc("matrixCol"));
		return this.findByCritera(dc);
	}
	
	@Override
	public boolean isSecondMatrixExist(String modelSeriesId) {
		// TODO Auto-generated method stub
		List resultList = searchSecondMatrix(modelSeriesId);
		if (resultList.size() > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<CusInterval> searchFinalMatrix(String modelSeriesId,String intevalFlg) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA5));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));

		if (!BasicTypeUtils.isNullorBlank(intevalFlg)){
			dc.add(Restrictions.eq("internalFlg", intevalFlg));  			
		}
		
		// 按检查间隔level排序
		dc.addOrder(Order.asc("internalFlg"));
		dc.addOrder(Order.asc("intervalLevel"));
		return this.findByCritera(dc);
	}

	@Override
	public List<CusMatrix> searchSecondMatrix(String modelSeriesId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA5));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		// 取第二矩阵数据
		dc.add(Restrictions.eq("matrixFlg", ComacConstants.SECOND_MATRIX));
		// 按行号排序
		dc.addOrder(Order.asc("matrixRow"));
		// 按列号排序
		dc.addOrder(Order.asc("matrixCol"));
		return this.findByCritera(dc);
	}
	
	@Override
	public List<CusMatrix> generateSecondMatrix(ComModelSeries modelSeries,
			String userId, List<CusMatrix> queryList,List<Integer> valueList,List<CusItemZa5> level1List,Integer levelCount) {
		 // 保存第一副矩阵数据
        saveMatrix(queryList, userId);
		
		// 生成第二矩阵数据
		List <CusMatrix> secondMatrixList = createSecondMatrixData(modelSeries,valueList,level1List,levelCount);
        // 保存第二矩阵数据
		for (CusMatrix mat : secondMatrixList){
			saveOrUpdate(mat, ComacConstants.DB_INSERT, userId);
		}
		// 返回第二矩阵数据
		return searchSecondMatrix(modelSeries.getModelSeriesId());
	}	
	
	 @Override
	public HashMap<String, List<CusInterval>>  generateFinalMatrix(ComModelSeries modelSeries,
			String userId, List<CusMatrix> queryList, List<Integer> valueList) {
		// TODO Auto-generated method stub
	    // 保存第一或第二矩阵数据  
	    saveMatrix(queryList, userId);
	    
	    HashMap<String, List<CusInterval>> hMap = createFinalMatrixData(modelSeries,valueList);
	    // 生成检查间隔矩阵数据(内部)
	    List<CusInterval> finalMatrixInList = hMap.get(ComacConstants.INNER);
		// 生成检查间隔矩阵数据(内部)
	    List<CusInterval> finalMatrixOutList = hMap.get(ComacConstants.OUTTER);
	    // 保存检查间隔矩阵数据
	    for(CusInterval in : finalMatrixInList){
	    	saveOrUpdate(in, ComacConstants.DB_INSERT, userId);
	    }
	    for(CusInterval out : finalMatrixOutList){
	    	saveOrUpdate(out, ComacConstants.DB_INSERT, userId);
	    }		  
	    
	    finalMatrixInList = searchFinalMatrix(modelSeries.getModelSeriesId(),ComacConstants.INNER);
	    finalMatrixOutList = searchFinalMatrix(modelSeries.getModelSeriesId(),ComacConstants.OUTTER);
	    
	    hMap = new HashMap<String, List<CusInterval>>();
	    hMap.put(ComacConstants.INNER, finalMatrixInList);
	    hMap.put(ComacConstants.OUTTER, finalMatrixOutList);
	    return hMap;
	}
	
	@Override
	public void saveMatrix(List<CusMatrix> matrixList,String userId) {
		// TODO Auto-generated method stub
		for(CusMatrix query : matrixList){
			CusMatrix updateData = (CusMatrix)loadById(CusMatrix.class, query.getMatrixId());
			updateData.setMatrixValue(query.getMatrixValue());
		    update(updateData, userId);
		}	
	}
	 
	@Override
	public void updateFinalMatrix(List<CusInterval> queryList,String userId,ComModelSeries modelSeries) {
		for(CusInterval query : queryList){
		   CusInterval updateData = (CusInterval)loadById(CusInterval.class, query.getIntervalId());
		   updateData.setIntervalValue(query.getIntervalValue());
		   update(updateData,userId);
		}
		// 插入CUS_EDR_ADR一条ZA5数据，并且operaterFlg等于1表示自定义完成
		insertCusMatrixFinishFlg(userId,modelSeries);	
	}	
		
	@Override
	public void insertCusMatrixFinishFlg(String userId,ComModelSeries modelSeries) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusEdrAdr.class);
		dc.add(Restrictions.eq("stepFlg", ComacConstants.ZA5));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeries.getModelSeriesId()));
		List<CusEdrAdr> cusEdrAdrList = (List<CusEdrAdr>)this.findByCritera(dc);
		if (cusEdrAdrList.size() > 0){
			CusEdrAdr cusEdrAdr = cusEdrAdrList.get(0);
			cusEdrAdr.setOperateFlg(ComacConstants.YES);
			this.update(cusEdrAdr, userId);			
		}else{
			CusEdrAdr cusEdrAdr = new CusEdrAdr();
		    cusEdrAdr.setComModelSeries(modelSeries);
		    cusEdrAdr.setStepFlg(ComacConstants.ZA5);
		    cusEdrAdr.setOperateFlg(ComacConstants.YES);// 自定义完成
		    this.saveOrUpdate(cusEdrAdr, ComacConstants.DB_INSERT, userId);	
		}
	}
	
	/**
	 * 生成主矩阵单元数据
	 * @param modelSeriesId 机型编号
	 * @return
	 * @author changyf
	 * createdate 2012-08-22
	 */
	private List<CusMatrix> createFirstMatrixData(String modelSeriesId){
		// 矩阵单元数据
		List<CusMatrix> matrixList = new ArrayList<CusMatrix>();
		// 取所有一级节点
		List<CusItemZa5> level1List = getLevel1ItemData(modelSeriesId);		
		// 机型对象
		ComModelSeries modelSeries = level1List.get(0).getComModelSeries();
		
		// 第一副矩阵的行名为itemcode最小的一级节点名
		String rowNmCn = level1List.get(0).getItemName();

		// 第二副矩阵的列名为itemcode第二的一级节点名
		String colNmCn = level1List.get(1).getItemName();
		
		// 取级别统计个数,根据个数生成第一副矩阵，例：级别个数为4，则第一副矩阵为四行四列
		List levelCountList = standardRegionParamDao.getLevelCount(modelSeriesId,ComacConstants.YES);
	    Integer levelCount = ((BigDecimal)levelCountList.get(0)).intValue();
	    
	    CusMatrix matrixItem;
	    
	    // 生成矩阵数据
	    for(int rowIndex = 1 ; rowIndex <= levelCount; rowIndex++){	    	
	    	for(int colIndex = 1 ; colIndex <= levelCount; colIndex++){
	    		matrixItem = new CusMatrix();
	    		matrixItem.setComModelSeries(modelSeries);
	    		matrixItem.setAnaFlg(ComacConstants.ZA5);
	    		matrixItem.setMatrixFlg(ComacConstants.FIRST_MATRIX);
	    		matrixItem.setMatrixRow(rowIndex);
	    		matrixItem.setMatrixRowName(rowNmCn);
	   
	    		matrixItem.setMatrixCol(colIndex);
	    		matrixItem.setMatrixColName(colNmCn);
	
	    		matrixList.add(matrixItem);
	    	}
	    }
	    return matrixList;	
	}
	
	
	/**
	 * 查询指定机型下所有一级项目节点的项目节点list
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 * @author changyf
	 * createdate 2012-08-22
	 */
	public List<CusItemZa5> getLevel1ItemData(String modelSeriesId){
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemZa5.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));		
		dc.add(Restrictions.eq("itemLevel", 0));// 所有一级节点的level为0
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		// 用项目序号排一下序
		dc.addOrder(Order.asc("itemCode"));
		return this.findByCritera(dc);
	}	
	
	
	
	/**
	 * 创建第二矩阵数据
	 * @param modelSeries 机型对象
	 * @param valueList 第一矩阵不重复value的list
	 * @param level1List 主矩阵树一级节点list
	 * @param levelCount 主矩阵级别值count
	 * @return
	 * @author changyf
	 * createdate 2012-08-24
	 */
    private List<CusMatrix> createSecondMatrixData(ComModelSeries modelSeries,List<Integer> valueList,List<CusItemZa5> level1List,Integer levelCount){
    	List<CusMatrix> secondMatrixList = new ArrayList<CusMatrix>();
    	Integer rowCount = valueList.size();
    	Integer colCount = levelCount;
    	String rowNmCn = level1List.get(0).getItemName() + "/" + level1List.get(1).getItemName();

    	String colNmCn = level1List.get(2).getItemName();

    	for (int i = 1; i <= rowCount; i++){
    		for (int j = 1; j <= colCount; j++){
    			CusMatrix matrixItem = new CusMatrix();
    			matrixItem.setComModelSeries(modelSeries);
	    		matrixItem.setAnaFlg(ComacConstants.ZA5);
	    		matrixItem.setMatrixFlg(ComacConstants.SECOND_MATRIX);
	    		matrixItem.setMatrixRow(i);
	    		// 把从第一矩阵获得的值拼到行名上
	    		matrixItem.setMatrixRowName(rowNmCn + ComacConstants.UNDER_LINE + valueList.get(i - 1));
	  
	    		matrixItem.setMatrixCol(j);
	    		matrixItem.setMatrixColName(colNmCn);

	    		secondMatrixList.add(matrixItem);
    		}
    	}
    	return secondMatrixList;
    }
     
    /**
     * 创建检查间隔矩阵数据
     * @param modelSeries 机型对象
     * @param valueList 第一或第二副矩阵不重复值的list
     * @return
     * @author changyf
     * createdate 2012-08-24
     */
    private HashMap<String, List<CusInterval>> createFinalMatrixData(ComModelSeries modelSeries,List<Integer> valueList){
    	ArrayList<CusInterval> finalMatrixInList = new ArrayList<CusInterval>();
    	ArrayList<CusInterval> finalMatrixOutList = new ArrayList<CusInterval>();
        
        for (Integer level : valueList){
        	CusInterval in = new CusInterval();
        	in.setAnaFlg(ComacConstants.ZA5);
        	in.setComModelSeries(modelSeries);
        	in.setInternalFlg(ComacConstants.INNER);
        	in.setIntervalLevel(level);
        	finalMatrixInList.add(in);
        	CusInterval out = new CusInterval();
        	out.setAnaFlg(ComacConstants.ZA5);
        	out.setComModelSeries(modelSeries);
        	out.setInternalFlg(ComacConstants.OUTTER);
        	out.setIntervalLevel(level);
        	finalMatrixOutList.add(out);
        }
        HashMap<String, List<CusInterval>> hMap = new HashMap<String, List<CusInterval>>();
        hMap.put(ComacConstants.INNER, finalMatrixInList);
        hMap.put(ComacConstants.OUTTER, finalMatrixOutList);
        return hMap; 
    }
    

	@SuppressWarnings("unchecked")
	@Override
	public List<CusItemZa5> getMatrix(String parentId, Integer isLeafNode,
			String modelSeriesId) {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemZa5.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));		
		dc.add(Restrictions.eq("isLeafNode", 0));		// 所有非叶子节点
		dc.add(Restrictions.eq("parentId", parentId));	
		// 用项目序号排一下序
		dc.addOrder(Order.asc("itemCode"));
		return this.findByCritera(dc);
	}

	/***************************************待删除
	 * 查询指定机型下每集项目下的的项目节点
	 * @param rowNum 节点的级数
	 * @param itemZa5Id 项目ID
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 * @author wubo
	 * createdate 2012-08-22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List getAllLeaf(Integer rowNum, String itemZa5Id,
			String modelSeriesId) {
		return standardRegionParamDao.getAllLeaf(rowNum, itemZa5Id, modelSeriesId);
	}
	
	/**
	 * 查询指定机型下所有项目节点的项目节点的数量
	 * @param parentId 父节点
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 * @author wubo
	 * createdate 2012-08-22
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusItemZa5> getAllLeafNode(String parentId,String modelSeriesId){
		List<CusItemZa5> list = new ArrayList<CusItemZa5>();
		this.getLeafNode(parentId, modelSeriesId, list);
		return list;
	}
	/**
     * 递归得到所有的叶子节点
     * @return parentId 父节点
     * @param modelSeries 机型对象
     * @param tempList 存放叶子节点List
     * @author wubo
     * createdate 2012-09-02
     */
	public void getLeafNode(String parentId,String modelSeriesId,List<CusItemZa5> tempList){
		List<CusItemZa5> list = this.getMatrixList(parentId, modelSeriesId);
		if(list == null || list.size()==0){
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getIsLeafNode()==1){
				tempList.add(list.get(i));
			}else{
				this.getLeafNode(list.get(i).getItemZa5Id(), modelSeriesId,tempList);
			}
		}
	}

	@Override
	public void saveOrUpdateZa5Item(CusItemZa5 itemNode, String operateFlg,
			String userId) {		
		// 父节点id为0时表示父节点是树上写死的根节点
		if (operateFlg.equals(ComacConstants.DB_INSERT) && !itemNode.getParentId().equals("00")){
			CusItemZa5 parentNode = (CusItemZa5)loadById(CusItemZa5.class, itemNode.getParentId());
			// 追加时判断父节点是否已经有子节点了，没有时修改父节点的isleafnode字段为否 
			if (parentNode.getIsLeafNode() != ComacConstants.YES){
		    	parentNode.setIsLeafNode(ComacConstants.NO);
				saveOrUpdate(parentNode, ComacConstants.DB_UPDATE, userId);
			}
		}
		// 保存当前节点
		saveOrUpdate(itemNode,operateFlg,userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteZa5Item(CusItemZa5 itemNode, String userId,String modelSeriesId) {
		// 父节点id为0时表示该父节点是主矩阵树上写死的根节点不需要改状态
		if (!"0".equals(itemNode.getParentId())) {
			DetachedCriteria dc = DetachedCriteria.forClass(CusItemZa5.class);
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId",
					modelSeriesId));
			dc.add(Restrictions.eq("parentId", itemNode.getParentId()));
			dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
			dc.add(Restrictions.not(Restrictions.eq("itemZa5Id", itemNode.getItemZa5Id())));
			List<CusItemZa5> otherSubNode = (List<CusItemZa5>) this
					.findByCritera(dc);
			// 当前被删除节点的父节点没有其他子节点时,将该父节点的isleafnode置为是
			if (otherSubNode.isEmpty()) {
				CusItemZa5 parentNode = (CusItemZa5) loadById(CusItemZa5.class,
						itemNode.getParentId());
				parentNode.setIsLeafNode(ComacConstants.YES);
				saveOrUpdate(parentNode, ComacConstants.DB_UPDATE, userId);
			}
		}
		saveOrUpdate(itemNode,ComacConstants.DB_UPDATE,userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isExistMatrixAnalyze(String modelSeriesId, String step) {
		DetachedCriteria dc = DetachedCriteria.forClass(ZaStep.class);
		dc.createCriteria("zaMain").add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId));
		if (step.equals(ComacConstants.ZA5)) {
			// 1分析完成，2分析中
			Criterion lhs = Restrictions.in("za5a", new Integer[] { 1, 2 });
			Criterion rhs = Restrictions.in("za5b", new Integer[] { 1, 2 });
			dc.add(Restrictions.or(lhs, rhs));
		}
		if (step.equals(ComacConstants.ZA43)) {
			dc.add(Restrictions.in("za43", new Integer[] { 1, 2 }));// 1分析完成，2分析中
		}
		List<ZaStep> stepList = (List<ZaStep>) this.findByCritera(dc);
		if (stepList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void deleteFinalMatrix(String modelSeriesId,String anaFlg) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));		
		dc.add(Restrictions.eq("anaFlg", anaFlg));
		List<CusInterval> dataList = (List<CusInterval>)this.findByCritera(dc);
	    // 删除检查间隔矩阵数据
		for (CusInterval cusInterval : dataList) {
			delete(cusInterval);
		}

		DetachedCriteria dc1 = DetachedCriteria.forClass(CusEdrAdr.class);
		dc1.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));		
		dc1.add(Restrictions.eq("stepFlg", anaFlg));
		List<CusEdrAdr> matrixCofirmData = (List<CusEdrAdr>)this.findByCritera(dc1);
		// 如果是在检查间隔矩阵已经确认过的情况下，删除CUS_EDR_ADR中指定anaflg的数据
		if (!matrixCofirmData.isEmpty()){
			delete(matrixCofirmData.get(0));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteMatrix(String modelSeriesId,String anaFlg,Integer matrixFlg) {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));		
		dc.add(Restrictions.eq("anaFlg", anaFlg));
		dc.add(Restrictions.eq("matrixFlg", matrixFlg));
		List<CusMatrix> dataList = (List<CusMatrix>)this.findByCritera(dc);
	    for (CusMatrix matrix : dataList) {
			delete(matrix);
		}
	}
	
	
	
	
	
}
