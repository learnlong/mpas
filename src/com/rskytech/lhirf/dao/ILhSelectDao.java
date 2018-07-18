package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.LhMain;

public interface ILhSelectDao extends IDAO {
	/**
	 *根据hsi编号查询LhMain
	 * @param modelSeriesId 
	 */
	public List<LhMain> searchLhMainByHsiCode(String hsiCode, String modelSeriesId);

	/**
	 * 根据lhMain删除其产生的数据，包括Lh1到lh6的	
	 * @param lhMain
	 * @param userId
	 */
	public void deleteLhMain(LhMain lhMain);
}
