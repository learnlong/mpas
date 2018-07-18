package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Lh2;

public interface ILh2Dao extends IDAO {
	/**
	 * 通过Lhsid 查询lh2 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-22
	 */
	public List<Lh2> getLh2ListByHsiId(String hsiId) throws BusinessException;
}
