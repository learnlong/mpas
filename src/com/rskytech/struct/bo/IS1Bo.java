package com.rskytech.struct.bo;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;


public interface IS1Bo extends IBo {
	
	public SRemark getRemarkBySssi(String sssiId) throws BusinessException;
	
	public List<S1> getS1Records(String sssiId) throws BusinessException;
	
	public List<SStep> getSstepBySssiId(String sssiId) throws BusinessException;
	
	
	/**
	 * 保存s1的数据
	 * @param ssiId
	 * @param s1Remark
	 * @param jsonData
	 * @param defaultEff
	 * @param user
	 * @param modelSeriesId
	 * @param modifiedJson 
	 * @return 转区域的任务的区域id list 
	 */
	public ArrayList<String> saveS1(String ssiId,String s1Remark,String jsonData,String defaultEff,ComUser user,String modelSeriesId, String modifiedJson);


	/**
	 * 通过ataId查询ssi主表
	 * @param ataId
	 * @return
	 */
	public SMain getSMainByAtaId(String ataId,String modelSeriesId);
	
	/**
	 * 删除S1同时删除后面的相关分析数据
	 * @param s1Id
	 * @param modelSeriesId 
	 * @param ssiId 
	 * @param string 
	 * @return 转区域的任务的区域id list 
	 */
	public ArrayList<String> deleteS1Record(String s1Id, String modelSeriesId, String ssiId);
	
	/**
	 * 当s1数据为空时删除后续分析数据
	 * @param ssiId
	 * @param userId 
	 * @param modelSeriesId 
	 */
	public void deleteAnalysisData(String ssiId, String modelSeriesId, String userId);
	
}
