package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;

public interface IDefineStructureParameterBo extends IBo{

	public List<CusEdrAdr> getCusEdrAdrList(String modelSeriesId,
			String stepFlg) throws BusinessException ;
	
	public List<CusItemS45> getS45List(String modelSeriesId, String stepFlg,
			String itemFlg) throws BusinessException ;
	
	public List<CusLevel> getLevelList(String modelSeriesId, String anaFlg,
			String itemId) throws BusinessException ;
	
	public void saveS45Item(CusItemS45 cusItemS45, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException;
	
	public Boolean checkCusS45Mtrix(String stemFlg, String modelSeriesId);


	public Integer checkItemCount(String stepFlg, String itemFlg,
			String modelSeriesId) throws BusinessException ;
	
	public void saveS45Level(CusLevel cusLevel, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException ;
	
	public void deleteNode(CusItemS45 cusItemS45, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException ;
	public void deleteNodeLevel(CusLevel cusLevel, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException;
	
	
	
	
			
}
