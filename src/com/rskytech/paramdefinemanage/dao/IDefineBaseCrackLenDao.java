package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusDisplay;

public interface IDefineBaseCrackLenDao extends IDAO{
	/**
	 * 根据机型Id查询定义的LH4显示信息
	 * @param modelSeriesId
	 * @return
	 */
	public List<CusDisplay> getLH4CusDisplayList(String modelSeriesId);
}
