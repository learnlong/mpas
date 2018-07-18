package com.rskytech.paramdefinemanage.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.paramdefinemanage.dao.IIncreaseRegionParamDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.CusMatrix;
import com.rskytech.pojo.ZaStep;

public class IncreaseRegionParamBo extends BaseBO implements IIncreaseRegionParamBo{
	
	private IIncreaseRegionParamDao increaseRegionParamDao;
	
	
	public IIncreaseRegionParamDao getIncreaseRegionParamDao() {
		return increaseRegionParamDao;
	}

	public void setIncreaseRegionParamDao(
			IIncreaseRegionParamDao increaseRegionParamDao) {
		this.increaseRegionParamDao = increaseRegionParamDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CusItemS45> searchZa43Item(String modelSeriesId,String itemFlg) {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemS45.class);
		dc.add(Restrictions.eq("itemFlg", itemFlg));// 项目区分
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("stepFlg", ComacConstants.ZA43));
		// 只取有效的
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		// 用项目序号排一下序
		dc.addOrder(Order.asc("itemSort"));
		
		return this.findByCritera(dc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isFinalMatrixExist(String modelSeriesId) {
		List resultList = searchFinalMatrix(modelSeriesId);		
		if (resultList.size() > 0){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusInterval> searchFinalMatrix(String modelSeriesId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA43));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.addOrder(Order.asc("intervalLevel"));
		return this.findByCritera(dc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isFirstMatrixExist(String modelSeriesId) {
		List resultList = searchFirstMatrix(modelSeriesId);
		if (resultList.size() > 0){
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusMatrix> searchFirstMatrix(String modelSeriesId) {
		// TODO Auto-generated method stub
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA43));
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
	
	/**
	 * 判断ed节点下是否存在item节点
	 * @return
	 */
	private boolean isItemExistByEdAd(String itemFlg,String modelSeriesId){		
		List<CusItemS45> itemList = searchZa43Item(modelSeriesId,itemFlg);	
	    if (itemList.size() > 0){
	    	return true;
	    }
	    return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer isLevelNumberSame(String modelSeriesId) {
		// ed节点下不存在item节点时
		if(!isItemExistByEdAd(ComacConstants.ED,modelSeriesId)){
			return -1;
		}
		// ad节点下不存在item节点时
		if(!isItemExistByEdAd(ComacConstants.AD, modelSeriesId)){
			return -1;
		}
		// 取得当前机型下ZA43矩阵有效的级别节点个数中不重复的数据
		List levelCountList = increaseRegionParamDao.getLevelCount(modelSeriesId,ComacConstants.YES);
		
		// 如果数据个数为0，表示当前机型没有za43自定义主矩阵数据
		if (levelCountList==null || levelCountList.size() <= 0){
			return -1;
		}else if (levelCountList.size() == 1){
			BigDecimal count = (BigDecimal)levelCountList.get(0);
			// 如果个数为0证明该ZA43主矩阵没有级别节点
			if (count.intValue() == 0){
				return -1;
			}else{
				return count.intValue();
			}
		}else{ // 如果以上数据个数超过1条,证明存在有项目节点下的级别节点个数不一致
			return -1;
		}		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusMatrix> generateFirstMatrix(ComModelSeries modelSeries,
			String userId,String edAlgorithm,String adAlgorithm) {
        // 更新算法
		updateAlgorithm(userId, modelSeries.getModelSeriesId(), edAlgorithm, adAlgorithm);
		
		List<CusMatrix> matrixList = createFirstMatrixData(modelSeries);
		// 保存第一矩阵数据
		try {
			for (CusMatrix mat : matrixList) {
				this.saveOrUpdate(mat, ComacConstants.DB_INSERT, userId);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
        
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("anaFlg", ComacConstants.ZA43));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeries.getModelSeriesId()));
		// 取第一矩阵数据
		dc.add(Restrictions.eq("matrixFlg", ComacConstants.FIRST_MATRIX));
		// 按行号排序
		dc.addOrder(Order.asc("matrixRow"));
		// 按列号排序
		dc.addOrder(Order.asc("matrixCol"));
		return this.findByCritera(dc);
	}
	
	/**
	 * 更新算法
	 * @param userId 用户id
	 * @param modelSeriesId 机型编号
	 * @param edAlgorithm ed算法
	 * @param adAlgorithm ad算法
	 */
	@SuppressWarnings("unchecked")
	private void updateAlgorithm(String userId,String modelSeriesId,String edAlgorithm,String adAlgorithm){
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemS45.class);
		dc.add(Restrictions.eq("stepFlg", ComacConstants.ZA43));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		// 按ed/ad排序
		dc.addOrder(Order.asc("itemFlg"));
		// 按ed/ad内序号排序
		dc.addOrder(Order.asc("itemSort"));
		List<CusItemS45> za43List = (List<CusItemS45>)this.findByCritera(dc);
		for(CusItemS45 item : za43List){
			if (item.getItemFlg().equals(ComacConstants.ED)){
				item.setItemAlgorithm(edAlgorithm);
			}else if (item.getItemFlg().equals(ComacConstants.AD)){
				item.setItemAlgorithm(adAlgorithm);
			}
			update(item, userId);
		}
	}
	
	/**
	 * 生成主矩阵单元数据
	 * @param modelSeriesId 机型编号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CusMatrix> createFirstMatrixData(ComModelSeries modelSeries){
		// 矩阵单元数据
		List<CusMatrix> matrixList = new ArrayList<CusMatrix>();
		
		// 第一副矩阵的行名为itemcode最小的一级节点名
		String rowNmCn = ComacConstants.ED_CN;
		String rowNmEn = ComacConstants.ED;
		// 第二副矩阵的列名为itemcode第二的一级节点名
		String colNmCn = ComacConstants.AD_CN;
		String colNmEn = ComacConstants.AD;
		
		// 取级别统计个数,根据个数生成第一副矩阵，例：级别个数为4，则第一副矩阵为四行四列
		List rowColCountList = increaseRegionParamDao.getLevelCount(modelSeries.getModelSeriesId(),ComacConstants.YES);
	    Integer levelCount = ((BigDecimal)rowColCountList.get(0)).intValue();
	    
	    CusMatrix matrixItem;
	    
	    // 生成矩阵数据
	    for(int rowIndex = 1 ; rowIndex <= levelCount; rowIndex++){	    	
	    	for(int colIndex = 1 ; colIndex <= levelCount; colIndex++){
	    		matrixItem = new CusMatrix();
	    		matrixItem.setComModelSeries(modelSeries);
	    		matrixItem.setAnaFlg(ComacConstants.ZA43);
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
	
	@Override
	public List<CusInterval> generateFinalMatrix(ComModelSeries modelSeries,
			String userId, List<CusMatrix> queryList, List<Integer> valueList) {
	    // 保存第一矩阵数据  
		for(CusMatrix query : queryList){
			CusMatrix updateData = (CusMatrix)loadById(CusMatrix.class, query.getMatrixId());
			updateData.setMatrixValue(query.getMatrixValue());
		    update(updateData, userId);
		}	
	    
	    // 生成检查间隔矩阵数据(内部)
	    List<CusInterval> finalMatrixList = createFinalMatrixData(modelSeries,valueList);
		
	    // 保存检查间隔矩阵数据
	    for(CusInterval in : finalMatrixList){
	    	saveOrUpdate(in, ComacConstants.DB_INSERT, userId);
	    }

	    finalMatrixList = searchFinalMatrix(modelSeries.getModelSeriesId());
	    
	    return finalMatrixList;
	}
	
	/**
     * 创建检查间隔矩阵数据
     * @param modelSeries 机型对象
     * @param valueList 第一或第二副矩阵不重复值的list
     * @return
     */
    private List<CusInterval> createFinalMatrixData(ComModelSeries modelSeries,List<Integer> valueList){
    	ArrayList<CusInterval> finalMatrixList = new ArrayList<CusInterval>();
        
        for (Integer level : valueList){
        	CusInterval in = new CusInterval();
        	in.setAnaFlg(ComacConstants.ZA43);
        	in.setComModelSeries(modelSeries);
        	in.setIntervalLevel(level);
        	finalMatrixList.add(in);
        }
        
        return finalMatrixList; 
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
		if (step.equals(ComacConstants.ZA43)) {//待确认
			//dc.add(Restrictions.in("za43", new Integer[] { 1, 2 }));// 1分析完成，2分析中
		}
		List<ZaStep> stepList = (List<ZaStep>) this.findByCritera(dc);
		if (stepList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
    
    @SuppressWarnings("unchecked")
	@Override
	public void updateFinalMatrix(List<CusInterval> queryList, String userId,
			ComModelSeries modelSeries) {
		for (CusInterval query : queryList) {
			CusInterval updateData = (CusInterval) loadById(CusInterval.class,
					query.getIntervalId());
			updateData.setIntervalValue(query.getIntervalValue());
			update(updateData, userId);
		}
		// 插入CUS_EDR_ADR一条za43数据，并且operaterFlg等于1表示自定义完成
		DetachedCriteria dc = DetachedCriteria.forClass(CusEdrAdr.class);
		dc.add(Restrictions.eq("stepFlg", ComacConstants.ZA43));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeries
				.getModelSeriesId()));
		List<CusEdrAdr> cusEdrAdrList = (List<CusEdrAdr>) this.findByCritera(dc);
		if (cusEdrAdrList.size() > 0) {
			CusEdrAdr cusEdrAdr = cusEdrAdrList.get(0);
			cusEdrAdr.setOperateFlg(ComacConstants.YES);
			this.update(cusEdrAdr, userId);
		} else {
			CusEdrAdr cusEdrAdr = new CusEdrAdr();
			cusEdrAdr.setComModelSeries(modelSeries);
			cusEdrAdr.setStepFlg(ComacConstants.ZA43);
			cusEdrAdr.setOperateFlg(ComacConstants.YES);// 自定义完成
			this.saveOrUpdate(cusEdrAdr, ComacConstants.DB_INSERT, userId);
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
	
	
	
	
	
	
}
