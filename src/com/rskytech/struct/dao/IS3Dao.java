package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.S3Crack;

public interface IS3Dao extends IDAO {
	/**
	 * 查询S3数据
	 * @param ssiId
	 * @return
	 * @throws BusinessException
	 */
	public List getS3Records(String ssiId) throws BusinessException;
	/**
	 * 根据s3Id查询S3Crack
	 * @param id
	 * @return
	 * @throws BusinessException
	 */
	public List<S3Crack> getS3Crack(String id) throws BusinessException;
	/**
	 * 根据机型Id查询s3任务
	 * @param modelId
	 * @return
	 * @throws BusinessException
	 */
	public List getMissionCount(String modelId) throws BusinessException;
	
	public List getMatrixByAnaFlg(String anaFlg,String modelId) throws BusinessException;
}
