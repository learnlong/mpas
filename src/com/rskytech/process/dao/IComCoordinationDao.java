package com.rskytech.process.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComCoordination;

public interface IComCoordinationDao extends IDAO {
	public List<ComCoordination> findCoordinationList(String userId,String modelSeriesId,int flag,Page page)
			throws BusinessException;
	public List<ComCoordination> findCoordinationSearchList(String userId,String modelSeriesId,int flag,Page page,String type,String isReceive)
			throws BusinessException;
	/**
	 * 通过taskID查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationByTaskId(String comTaskId,String modelSeriesId,int flag)
			throws BusinessException;
	/**
	 * 通过SSIID和S6内外查询协调单
	 * @param comTaskId
	 * @return
	 * @throws BusinessException
	 */
	public List<ComCoordination> findCoordinationById(String comTaskId,String comS6OutOrIn,String modelSeriesId,int flag)
			throws BusinessException;
}
