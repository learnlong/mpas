package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.ZaMain;

public interface IZa1Dao extends IDAO {

	/**
	 * 通过区域ID，查询ZA_MAIN
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @return ZA_MAIN
	 * @author zhangjianmin
	 */
	public ZaMain getZaMainByAreaId(String msId, String areaId) throws BusinessException;
	
	/**
	 * 通过区域主表ID，查询ZA1
	 * @param zaId 区域主表ID
	 * @return Za1
	 * @author zhangjianmin
	 */
	public Za1 getZa1ByZaId(String zaId) throws BusinessException;
	
	/**
	 * 删除原始的区域分析数据
	 * @param zaId 区域主表ID
	 * @author zhangjianmin
	 */
	public void deleteAreaAnalysis(String zaId) throws BusinessException;
	
	/**
	 * 删除当前区域的所有分析数据
	 * @param zaId 区域主表ID
	 * @author zhangjianmin
	 */
	public void deleteAreaAnalysisAll(String zaId) throws BusinessException;
}
