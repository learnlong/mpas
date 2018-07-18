package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Za2;

public interface IZa2Dao extends IDAO {

	/**
	 * 通过区域主表ID，查询ZA2
	 * @param zaId 区域主表ID
	 * @return Za2
	 * @author zhangjianmin
	 */
	public Za2 getZa2ByZaId(String zaId) throws BusinessException;
	
	/**
	 * 删除原始的区域分析数据
	 * @param zaId 区域主表ID
	 * @author zhangjianmin
	 */
	public void deleteAreaAnalysis(String zaId, Integer position) throws BusinessException;
}
