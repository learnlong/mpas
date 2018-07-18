package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusItemS45;

public interface ILhirfParamBo extends IBo {
	
	public List<CusItemS45> getAlgList(String stepFlg, String itemFlg,
			String modelSeriesId) throws BusinessException;
	
	public List<CusItemS45> getS45List(String modelSeriesId, String stepFlg,
			String itemFlg) throws BusinessException ;
	
	public void saveS45Item(CusItemS45 cusItemS45, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException ;
	
	public Boolean checkCusS45Mtrix(String stemFlg, String modelSeriesId)
	throws BusinessException ;
	
	public void deleteNode(CusItemS45 cusItemS45, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException ;
	
	public void saveLH4(ComUser comUser,ComModelSeries comModelSeries, String EDMMA, String ADMMA,
			String ROOTMMA) throws BusinessException ;
	
	public List<Object[]> getCusIntervalList(Object[] obs)
	throws BusinessException ;
	
	public void finalBackLh5(String modelSeriesId, String userId)
	throws BusinessException ;
	
	public void saveLh5(ComUser comUser,ComModelSeries comModelSeries ,String jsonData)
	throws BusinessException ;
	
	
	
	

}
