package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.Sy;

/**
 * 
 * @author 赵涛
 *
 */
public interface IS6Bo extends IBo {
	/**
	 * 得到S6表的所有数据
	 * @param ssiId ssiid
	 * @return S6对象
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public S6 getS6Records(String ssiId,String inorOut) throws BusinessException;
	
	/**
	 * 得到S6ea表的数据
	 * @param ssiId ssi表主键
	 * @return s6ea表的数据集
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List getS6EaRecords(String ssiId,String region) throws BusinessException;
	/**
	 * 得到S4表的数据
	 * @param s1Id s1表主键
	 * @return s4表的数据集
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List<S4> getS4Records(String s1Id,String region) throws BusinessException;
	/**
	 * 得到Sy表的数据
	 * @param s1Id s1表主键
	 * @return sy表的数据集
	 * @throws BusinessException
	 * @createdate 2015年4月18日
	 */
	public List<Sy> getSyRecords(String s1Id,String region) throws BusinessException;
	/**
	 * 得到S5表的数据
	 * @param s1Id s1表主键
	 * @return s5表的数据集
	 * @throws BusinessException
	 * @createdate 2012年8月28日
	 */
	public List<S5> getS5Records(String s1Id,String region) throws BusinessException;
	
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
	 * 根据SSSIID得到对应的所有S1id
	 * @param ssiId
	 * @param inOrOut
	 * @return
	 * @throws BusinessException
	 */
	public List<S1> getS1IdBySssiId(String ssiId,String inOrOut) throws BusinessException;
	
	/**
	 * 通过s1Id得到S6Ea
	 * @param inorOut
	 * @param i
	 */
	public List<S6Ea> getS6EaByS1Id(String inorOut, String i)  throws BusinessException;
	
	/**
	 * 保存S6
	 * @return
	 * @throws BusinessException
	 */
	public List<String> saveS6Records(String delId,String comModelSeriesId,String otherJsonData,String resultJsonData,String listJsonData,String finalRemark,String cpcp,String ssiId,String s6Id,String inorOut,ComUser user,String coverCpcp,String considerWear) throws BusinessException;
	
	/**
	 * 得到所有的SSI下的任务
	 * @param ssiId
	 * @return
	 * @throws BusinessException
	 */
	public List searchSsi(String ssiId,String str)  throws BusinessException;
	
	
	/**
	 * 根据较小级别得到知识库s6评级表中的ed/ad间隔数据
	 * @param obs
	 * @return
	 */
	public List getS6CusInList(String inOrOut,int miniValue,String modelSeriesId);
	
	/**
	 * 校验任务临时任务表中任务编号是否存在
	 * @param region 
	 */
	public boolean checkTempTaskCodeExist(String tempTaskCode,String modelId, String region);
	
	
}
