package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;

public interface IS1Dao extends IDAO {
	/**
	 * 根据SSiid查询SStep
	 * @param sssiId
	 * @return
	 * @throws BusinessException
	 */
	public List<SStep> getSstepBySssiId(String sssiId) throws BusinessException;
	/**
	 * 根据SSiId查询S1数据
	 */
	public List<S1> getS1ListBySssiId(String sssiId) throws BusinessException;
	
	/**
	 * 根据ssiId查询SRemark
	 * @param sssiId
	 * @return
	 * @throws BusinessException
	 */
	public List<SRemark> getRemarkBySssi(String sssiId) throws BusinessException;
	/**
	 * 通过ataId查询ssi主表
	 * @param ataId
	 * @return
	 */
	public List<SMain> getSMainByAtaId(String ataId,String modelSeriesId);
}
