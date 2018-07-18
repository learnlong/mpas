package com.rskytech.lhirf.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComUser;

public interface ILh6Bo extends IBo {
	
	
	/*
	 * 根据 机型ID 区域ID 分页查询 HSI MSG-3任务详细情况
	 */
	
	public Page getLhirfListById(String  modelSeriesId ,String AreaId,Page page) throws BusinessException;
	
	///保存 LH6 中 出现的 lheff  有效性
	public void saveLh6LhEff(ComUser user, String jsonData)throws BusinessException ;
	/**
	 * 根据 机型ID 区域ID 不分页查询 HSI MSG-3任务详细情况
	 * @param modelSeriesId
	 * @param AreaId
	 * @return
	 * @throws BusinessException
	 */
	public List<Object[]> getLhirfListByIdNoPage(String  modelSeriesId ,String AreaId) throws BusinessException;
}
