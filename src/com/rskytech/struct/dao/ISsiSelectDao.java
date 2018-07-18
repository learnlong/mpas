package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.SMain;

public interface ISsiSelectDao extends IDAO{
	/**
	 * 得到SMain查询数据
	 * @param isSsi 是否是SSI
	 * @param isOwn 是否属于自增
	 * @param ssiName SSI名称
	 * @throws BusinessException
	 */
	public List<Object[]> getSsiRecords(Integer isSsi,Integer isOwn,String ssiName,String model,int start,int limit) throws BusinessException;
	/**
	 * 得到SMain总数据条数
	 * @param isSsi 是否是SSI
	 * @param isOwn 是否属于自增
	 * @param ssiName SSI名称
	 * @throws BusinessException
	 */
	public List<Object> getSsiTotalRecords(Integer isSsi,Integer isOwn,String ssiName,String model) throws BusinessException;
	/**
	 * 根据ataID查询Smain
	 * @param ataId
	 * @return
	 * @throws BusinessException
	 */
	public List<SMain> getSMainByAtaId(String ataId) throws BusinessException;
	/**
	 * 根据ataID查询其下的自加SSI和非SSI
	 * @param ataId
	 * @return
	 * @throws BusinessException
	 */
	public List<SMain> getSelfSMainByAtaId(String ataId) throws BusinessException;
	/**
	 * 根据ataid,机型Id，用户Id查询符合需要分析的Ssi数据
	 * @param ataId
	 * @param modelSeriesId
	 * @param userId
	 * @return
	 */
	public List<Object[]> getSsiList(String ataId,String modelSeriesId,String userId);
	/**
	 * 根据AtaId查询其有没有分析权限
	 */
	public List searchAnalysisProByAtaId(String ataId,String modelSeriesId,String userId);
	/**
	 * 根据SSIcode查询ata
	 * @param SssiCode 传入的SSIcode
	 * @return
	 * @throws BusinessException
	 */
	public List SearchSsiListAndAtaListByAtaCode(String SsiCode,String modelSerierId) throws BusinessException;
	
	/**
	 * 删除非ssi分析数据
	 */
	public void deleteUnSsiAnalysis(String ssiId,String modelSeriesId);
	
}
