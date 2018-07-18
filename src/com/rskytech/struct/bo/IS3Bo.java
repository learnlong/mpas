package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S3Crack;

public interface IS3Bo extends IBo {
	public List getS3Records(String ssiId) throws BusinessException;
	
	public List getMatrixByAnaFlg(String anaFlg,String modelId) throws BusinessException;
	/**
	 * 得到S3Crack的数据
	 * @param s3Id
	 * @return
	 * @throws BusinessException
	 */
	public S3Crack getS3Crack(String s3Id)throws BusinessException;
	
	/**
	 * 得到任务数
	 * @param modelId
	 * @return
	 * @throws BusinessException
	 */
	public int getMissionCount(String modelId)  throws BusinessException;
	
	
	public List<String> saveS3Records(String jsonData,String ssiId,ComUser user,ComModelSeries comModelSeries)  throws BusinessException;
	
}
