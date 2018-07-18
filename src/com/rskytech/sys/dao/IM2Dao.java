package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MReferAfm;
import com.rskytech.pojo.MReferMmel;

public interface IM2Dao extends IDAO {
	/**
	 * 根据MsiId查询M2
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 */
	public List<M2> getM2ListByMsiId(String msiId)throws BusinessException;
	/**
	 * 通过故障影响的ID，查询唯一的一条M2记录
	 * @param msiId
	 * @return
	 */
	public List<M2> getM2ByM13fId(String m13fId)throws BusinessException ;
	/**
	 * 根据M2Id查询Afm
	 * @param m2Id
	 * @param userId
	 * @throws BusinessException
	 */
	public List<MReferAfm> searchAfm(String m2Id) throws BusinessException;
	/**
	 * 根据M2Id查询mmel
	 * @param m2Id
	 * @param userId
	 * @throws BusinessException
	 */
	public List<MReferMmel> searchMmel(String m2Id) throws BusinessException;

}
