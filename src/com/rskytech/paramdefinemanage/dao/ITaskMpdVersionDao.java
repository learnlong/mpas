package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMpdVersion;

public interface ITaskMpdVersionDao extends IDAO{
	
	/**
	 * 根据机型号查询所有版本
	 * 
	 * @param modelSeriesId
	 * @return
	 * @throws DAOException
	 */
	public List<TaskMpdVersion> findByModelSeriesId(String modelSeriesId, String versionType, Integer validFlag) throws DAOException;
	
	/**
	 * 根据机型号查询版本号的最大值
	 * 
	 * @param modelSeriesId
	 * @return
	 * @throws DAOException
	 */
	public int getMaxVersionCode(String modelSeriesId, String versionType, Integer validFlag) throws DAOException;

}
