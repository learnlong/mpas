package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;
import com.rskytech.sys.dao.IMSetDao;

@SuppressWarnings("unchecked")
public class MSetDao extends BaseDAO implements IMSetDao {
	
	/**
	 * 根据msiId和ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	@Override
	public List<MSet> getMsetListByMsiIdAndAtaId(String msiId,String ataId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSet.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.add(Restrictions.eq("comAta.id", ataId));
		dc.addOrder(Order.asc("functionCode"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 根据msiId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSet.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("functionCode"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 根据功能ID查找故障
	 * @param msetId
	 * @return
	 * @throws BusinessException
	 */
	public List<MSetF> getMsetfListByMsetId(String msetId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSetF.class);
		dc.add(Restrictions.eq("mset.id", msetId));
		dc.addOrder(Order.asc("failureCode"));
		return this.findByCriteria(dc);

	}
	
	/**
	 * 根据msiId与功能编号查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet>  getMsetByfunctionCode(String msiId, String functionCode)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSet.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.add(Restrictions.eq("functionCode", functionCode));
		return this.findByCriteria(dc);
	}
	

	
	public List<M13C> getM13cListByM13FId(String m13fId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.add(Restrictions.eq("m13F.id", m13fId));
		dc.addOrder(Order.asc("causeCode"));
		return this.findByCriteria(dc);
	}

	/**
	 * 根据ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	@Override
	public List<MSet> getMsetListByAtaId(String ataId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSet.class);
		dc.add(Restrictions.eq("comAta.id", ataId));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 根据mSetFId查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	@Override
	public List<M13C> getM13ListByMSetFId(String mSetFId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.add(Restrictions.eq("msetfId", mSetFId));
		return this.findByCriteria(dc);
	}
}

