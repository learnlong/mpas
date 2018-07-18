package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.Lh3;

public interface ILh3Dao extends IDAO {
	/**
	 * 通过Lhsid 查询lh3 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh3数据List
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-23
	 */
	public  List<Lh3> getLh3ListByHsiId(String hsiId,Page page)throws BusinessException ;
	
	public List<Lh3> getLh3ListByHsiIdNoPage(String hsiId) throws BusinessException;
}
