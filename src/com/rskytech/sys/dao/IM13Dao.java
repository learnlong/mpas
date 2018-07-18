package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MReferMsi;

public interface IM13Dao extends IDAO {
	/**
	 * 根据msiId查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13> getM13ListByMsiId(String msiId) throws BusinessException ;
	/**
	 * 根据msiId与功能编号查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13>  getM13ByfunctionCode(String msiId, String functionCode) throws BusinessException;
	
	/**
	 *根据msiId与故障影响编号查询唯一一条M13F
	 * @param msiId   系统MainId
	 * @param effectCode //故障影响编号
	 * @return
	 */
	public List<M13F> getM13FByEffectCode(String msiId, String effectCode) throws BusinessException;
	/**
	 * 
	 * @param m13Id 功能Id
	 * @return
	 * @throws BusinessException
	 */
	public List<M13F> getM13fListByM13Id(String m13Id)throws BusinessException;
	
	/**
	 * 
	 * @param m13fId 功能故障Id
	 * @return
	 * @throws BusinessException
	 */
	public List<M13C> getM13cListByM13FId(String m13fId)throws BusinessException;
	/**
	 * 根据MsiId查询所有的故障影响
	 * @param msiId 
	 * @throws BusinessException
	 */
	public List<M13F> getM13fListByMsiId(String msiId)throws BusinessException;
	/**
	 * 根据MsiId查询所有的故障影响原因
	 * @param msiId
	 * @throws BusinessException
	 */
	public List<M13C> getM13cListByMsiId(String msiId)throws BusinessException;
	/**
	 * 查询没有参考其他MSI的故障原因
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 */
	public List<M13C>   getM13cListByMsiIdNoidNoisRef(String msiId)throws BusinessException;
	/**
	 * 根据m13cId查询MReferMsi
	 */
	public List<MReferMsi> getMReferMsiByM13cId(String m13cId)throws BusinessException;
	
	/**
	 * 根据msetfId和msiId查询m13c
	 */
	public List<M13C> getM13cByMsetfIdAndmsId(String msetfId,String msiId)throws BusinessException;
}

