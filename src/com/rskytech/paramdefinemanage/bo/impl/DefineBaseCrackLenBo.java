package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.paramdefinemanage.dao.IDefineBaseCrackLenDao;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusMatrix;


public class DefineBaseCrackLenBo extends BaseBO implements IDefineBaseCrackLenBo{
	private IDefineBaseCrackLenDao defineBaseCrackLenDao;
 
	/**
	 * 查询CUS_Display表数据
	 * @param modelSeriesId
	 * @return List<CusDisplay>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusDisplay> getCusBaseCrackLenGradeList(String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusDisplay.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId ));	
		dc.add(Restrictions.eq("displayWhere",ComacConstants.CUSBASECRACKLEN_ANAFLG ));
		//dc.addOrder(Order.asc("displayId"));
		dc.addOrder(Order.asc("diffname"));
		dc.addOrder(Order.asc("diff"));
		return this.findByCritera(dc);
	}
	
	
	/**
	 * 查询cus_matrix表数据
	 * @param modelSeriesId
	 * @return List<CusCrack>
	 */
    @SuppressWarnings("unchecked")
	@Override
	public List<CusMatrix> getCusBaseCrackLenMatrixData(String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId ));
		dc.add(Restrictions.eq("anaFlg",ComacConstants.CUSBASECRACKLEN_ANAFLG ));//s3
		dc.add(Restrictions.eq("matrixFlg",ComacConstants.CUSBASECRACKLEN_MATRIXFLG ));//4
		return this.findByCritera(dc);
	}
    
    
	/**
	 * 判断S3分析状态
	 * @param modelSeriesId
	 * @return Boolean 是表示可以进行矩阵分析
	 */
	@Override
	public Boolean s3AnalyseState(String modelSeriesId){
    	Integer isAnalyse = -1;
			
		if((isAnalyse != null) && ((isAnalyse == 1) || (isAnalyse == 2))){
			return false;
		}else{
			return true;
		}
	}
    
    
    /**
	 * 查询CUS_MATRIX表数据
	 */
    @SuppressWarnings("unchecked")
	@Override
	public CusMatrix getCusMatrixData(Integer matrixNumber, String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMatrix.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId ));
		dc.add(Restrictions.eq("anaFlg",ComacConstants.CUSBASECRACKLEN_ANAFLG ));//s3
		dc.add(Restrictions.eq("matrixFlg",ComacConstants.CUSBASECRACKLEN_MATRIXFLG ));//4
		int row = matrixNumber/4+1;
		int col = matrixNumber%4;
		if(matrixNumber%4 == 0){
			row -= 1;
			col = 4;
		}
		dc.add(Restrictions.eq("matrixRow",row ));
		dc.add(Restrictions.eq("matrixCol",col ));
		CusMatrix cusMatrix = null;
		List<CusMatrix> cusMatrixList = this.findByCritera(dc);
		if((cusMatrixList != null) && (!cusMatrixList.isEmpty())){
			cusMatrix = (CusMatrix) cusMatrixList.get(0);
		}
		return cusMatrix;
	}


	@Override
	public CusDisplay getLH4CusDisplay(String modelSeriesId) {
		List<CusDisplay> list = this.defineBaseCrackLenDao.getLH4CusDisplayList(modelSeriesId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}


	public IDefineBaseCrackLenDao getDefineBaseCrackLenDao() {
		return defineBaseCrackLenDao;
	}


	public void setDefineBaseCrackLenDao(
			IDefineBaseCrackLenDao defineBaseCrackLenDao) {
		this.defineBaseCrackLenDao = defineBaseCrackLenDao;
	}
	
}
