package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusMpdPs;

public interface ICusMpdPsDao extends IDAO{
	
	/**
	 * 根据机型号查询MPD附录列表
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("rawtypes")
	public List findAllPsByModelSeriesId(String id) throws DAOException;
	
	public CusMpdPs findById(String id) throws DAOException;
	
	public boolean isPsFlgUnique(String modelSeriesId, String cusMpdPsId) throws DAOException;

}
