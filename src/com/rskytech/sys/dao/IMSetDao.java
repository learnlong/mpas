package com.rskytech.sys.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;

public interface IMSetDao extends IDAO {
	
	/**
	 * 根据msiId和ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiIdAndAtaId(String msiId,String ataId) throws BusinessException ;
	
	
	/**
	 * 根据msiId查询M13
	 * 
	 * @param MSet
	 * @return
	 */
	public List<MSet> getMsetListByMsiId(String msiId) throws BusinessException ;
	
	/**
	 * 根据功能ID查找故障
	 * @param msetId
	 * @return
	 * @throws BusinessException
	 */
	public List<MSetF> getMsetfListByMsetId(String msetId)throws BusinessException;
	
	/**
	 * 根据msiId与功能编号查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet>  getMsetByfunctionCode(String msiId, String functionCode) throws BusinessException;
	
	
	/**
	 * 
	 * @param m13fId 功能故障Id
	 * @return
	 * @throws BusinessException
	 */
	public List<M13C> getM13cListByM13FId(String m13fId)throws BusinessException;
	
	/**
	 * 根据ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByAtaId(String ataId)throws BusinessException;
	
	/**
	 * 根据mSetFId查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13C> getM13ListByMSetFId(String mSetFId) throws BusinessException;
}

