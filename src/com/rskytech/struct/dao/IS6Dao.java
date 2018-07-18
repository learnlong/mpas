package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;

public interface IS6Dao extends IDAO {

	/**
	 * 得到S6表的所有数据
	 * @param ssiId ssiid
	 * @param region 内或者外
	 * @return 数据list
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List getS6EaRecords(String ssiId,String region) throws BusinessException;

	public List searchSsi(String ssiId,String str) throws BusinessException;
	
	public List getCusInList(String inOrOut,int miniValue,String modelSeriesId) throws BusinessException;	
	/**
	 * 通过s1Id得到S6Ea
	 * @param inorOut
	 * @param i
	 */
	public List<S6Ea> getS6EaByS1Id(String inorOut, String i)  throws BusinessException;
	/**
	 * 根据SSSIID得到对应的所有S1id
	 * @param ssiId
	 * @param inOrOut
	 * @return
	 * @throws BusinessException
	 */
	public List<S1> getS1IdBySssiId(String ssiId,String inOrOut) throws BusinessException;
	/**
	 * 得到S6表的所有数据
	 * @param ssiId ssiid
	 * @return S6对象
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List<S6> getS6Records(String ssiId,String inorOut) throws BusinessException;
	/**
	 * 得到S4表的数据
	 * @param s1Id s1表主键
	 * @param region 
	 * @return s4表的数据集
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List<S4> getS4Records(String s1Id, String region) throws BusinessException;
	/**
	 * 得到Sy表的数据
	 * @param s1Id s1表主键
	 * @param region 
	 * @return sy表的数据集
	 * @throws BusinessException
	 * @createdate 2015年4月18日
	 */
	public List<Sy> getSyRecords(String s1Id, String region) throws BusinessException;
	/**
	 * 得到S5表的数据
	 * @param s1Id s1表主键
	 * @param region 
	 * @return s5表的数据集
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List<S5> getS5Records(String s1Id, String region) throws BusinessException;
	
	/**
	 * 得到临时任务数据符合要求的
	 * @param modelId 飞机ID
	 * @param sourceSystem 所在系统区域
	 * @param taskValid 数据的有效性
	 * @param region 数据所属内或者外
	 * @return
	 * @throws BusinessException
	 */
	public List getOtherRecords(String ssiId,String modelId,String sourceSystem,Integer taskValid,String region) throws BusinessException;
	
	/**
	 * 根据SSIID得到S6的数据
	 * @param ssiId ssiId
	 * @param sourceSystem 所在系统区域
	 * @return
	 * @throws BusinessException
	 */
	public List getS6BySsiId(String ssiId,String inOrOut) throws BusinessException;
	
	
	
	/**
	 * 根据SSIID得到S6的数据
	 * @param ssiId ssiId
	 * @param sourceSystem 所在系统区域
	 * @return
	 * @throws BusinessException
	 */
	public List<CusInterval> getIntervalRecords(String inOrOut,String modelId) throws BusinessException;
	
	/**
	 * 查询临时任务表中任务编号是否存在
	 * @param region 
	 */
	public List<TaskMsg> searchTempTaskCode(String taskCode,String modelId, String region);
}
