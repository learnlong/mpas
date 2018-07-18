package com.rskytech.struct.bo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.SMain;

public interface ISsiSelectBo extends IBo {
	
	/**
	 * 得到SSI查询数据
	 * @param isSsi 是否是SSI
	 * @param isOwn 是否属于自增
	 * @param ssiName SSI名称
	 * @param Page 分页数据
	 */
	public JSONObject getSsiRecords(Integer isSsi,Integer isOwn,String ssiName,String model,int start,int limit) throws BusinessException;
	/**
	 * 根据ataID查询Smain
	 * @param ataId
	 * @return
	 * @throws BusinessException
	 */
	public List<SMain> getSMainByAtaId(String ataId) throws BusinessException;
	/**
	 * 根据ataid,机型Id，用户Id查询符合需要分析的Ssi数据
	 * @param ataId
	 * @param modelSeriesId
	 * @param userId
	 * @return
	 */
	public List<Object[]> getSsiListByAtaId(String ataId,String modelSeriesId,String userId);
	/**
	 * 根据AtaId查询其有没有分析权限
	 */
	public boolean searchAnalysisProByAtaId(String ataId,String modelSeriesId,String userId);
	/**
	 * 验证SSIcode是否存在
	 * @param SssiCode 传入的SSIcode
	 * @return true 编号不存在，FALSE 编号存在
	 * @throws BusinessException
	 */
	public boolean verifySsiCode(String SsiCode,String modelSeriesId) throws BusinessException;
	/**
	 * 保存ssi数据
	 * @param jsonData
	 * @param ataId
	 * @param userId
	 * @param modelSeriesName
	 * @return 
	 */
	public ArrayList<String> saveSsi(String jsonData, String ataId, String userId,ComModelSeries comModelSeries);
	/**
	 * 删除ssi数据
	 * @param ssiId
	 * @param userId
	 * @param modelSeriesId 
	 * @return 
	 */
	public ArrayList<String> delRecord(String ssiId, String userId, String modelSeriesId);
}
