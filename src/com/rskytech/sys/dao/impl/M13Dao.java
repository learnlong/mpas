package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.sys.dao.IM13Dao;

@SuppressWarnings("unchecked")
public class M13Dao extends BaseDAO implements IM13Dao {
	
	/**
	 * 根据m13cId查询MReferMsi
	 */
	public List<MReferMsi> getMReferMsiByM13cId(String m13cId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MReferMsi.class);
		dc.add(Restrictions.eq("m13C.id", m13cId));
		return this.findByCriteria(dc);
	}
	/**
	 * 根据msiId查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13> getM13ListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("functionCode"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 根据msiId与功能编号查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13>  getM13ByfunctionCode(String msiId, String functionCode)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.add(Restrictions.eq("functionCode", functionCode));
		return this.findByCriteria(dc);
	}
	
	public List<M13F> getM13FByEffectCode(String msiId, String effectCode)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13F.class);
		dc.createAlias("m13", "m13");
		dc.add(Restrictions.eq("m13.MMain.id", msiId));
		dc.add(Restrictions.eq("effectCode", effectCode));
		return this.findByCriteria(dc);
	}
	
	
	public List<M13F> getM13fListByM13Id(String m13Id) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13F.class);
		dc.add(Restrictions.eq("m13.id", m13Id));
		dc.addOrder(Order.asc("failureCode"));
		return this.findByCriteria(dc);

	}
	
	public List<M13C> getM13cListByM13FId(String m13fId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.add(Restrictions.eq("m13F.id", m13fId));
		dc.addOrder(Order.asc("causeCode"));
		return this.findByCriteria(dc);
	}
	
	@Override
	public List<M13F> getM13fListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13F.class);
		dc.createAlias("m13", "m13");
		dc.createAlias("m13.MMain", "MMain");
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("effectCode"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M13C> getM13cListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.createAlias("m13F", "m13F");
		dc.createAlias("m13F.m13", "m13");
		dc.createAlias("m13.MMain", "MMain");
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M13C> getM13cListByMsiIdNoidNoisRef(String msiId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.add(Restrictions.eq("isRef", ComacConstants.NO));
		dc.createAlias("m13F", "m13F");
		dc.createAlias("m13F.m13", "m13");
		dc.createAlias("m13.MMain", "MMain");
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}
	@Override
	public List<M13C> getM13cByMsetfIdAndmsId(String msetfId, String msiId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M13C.class);
		dc.add(Restrictions.eq("msetfId", msetfId));
		dc.createAlias("m13F", "m13F");
		dc.createAlias("m13F.m13", "m13");
		dc.createAlias("m13.MMain", "MMain");
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}

}

