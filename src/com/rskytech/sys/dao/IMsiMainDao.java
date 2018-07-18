package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.MMain;

public interface IMsiMainDao extends IDAO {
	/**
	 * 查询所有MSI
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public List<Object[]> getMSIAll(String modelSeriesId)throws BusinessException;
	
	/**
	 * 根据机型查询MSI
	 * @param ataId
	 * @return MSelect
	 * @throws BusinessException
	 */
	public List<MMain> getMMainByStatus(String modelSeriesId)throws BusinessException;
	
	public List<MMain> getMMainByAtaIdAndModelSeries(String ataId, String modelSeriesId);

}
