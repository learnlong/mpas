package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M4;

public interface IM4Dao extends IDAO {
	/**
	 * 根据任务Id查询与之有关系的M2
	 * 
	 * @param taskId
	 * @return
	 * @throws BusinessException
	 */
	public List<M2> getListM2ByTaskId(String taskId) throws BusinessException;
	/**
	 * 根据MsiId查询M4
	 * @param taskId
	 * @return
	 * @throws BusinessException
	 */
	public List<M4> getListM4ByMsiId(String msiId)throws BusinessException;
	/**
	 * 根据任务Id查询M4
	 * @param taskId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-29
	 */
	public List<M4> getM4ByTaskId(String taskId)throws BusinessException;
	/**
	 * 通过taskId删除M4
	 * @param taskId
	 */
	public void deleteM4ByTaskId(String taskId);
}

