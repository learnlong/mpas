package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh5;

public interface ILh5Dao extends IDAO {
	

	/**
	 * 通过Lhsid 查询lh5 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 */
	public List<Lh5> getLh5ListByHsiId(String hsiId)throws BusinessException ;
	/**
	 * 查询Cus_Interval表中属于某一机分析区域数据 A或者 B 套数据
	 * @param  anaFlg LH5/S6
	 * @param  internalFlg A/B
	 * @param modelSeriesId
	 * @return 自定义评级表List
	 * @throws BusinessException
	 */
	public List<CusInterval> getCusIntervalbyFlg(String anaFlg, String internalFlg,
			String modelSeriesId);
}
