package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;

public interface ITaskMpdDao extends IDAO{

	/**
	 * 使用sql方式查询mpd任务(暂时未使用)
	 * @param modelSeriesId
	 * @param SOURCE_SYSTEM
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("rawtypes")
	@Deprecated
	public List findBySql(String modelSeriesId, String SOURCE_SYSTEM) throws DAOException;
	
	/**
	 * 使用hql方式查询mpd任务
	 * @param modelSeriesId
	 * @param SOURCE_SYSTEM
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("rawtypes")
	public List findByHql(String modelSeriesId, String SOURCE_SYSTEM) throws DAOException;
}
