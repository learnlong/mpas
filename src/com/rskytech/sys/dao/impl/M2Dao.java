package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MReferAfm;
import com.rskytech.pojo.MReferMmel;
import com.rskytech.sys.dao.IM2Dao;

public class M2Dao extends BaseDAO implements IM2Dao {

	public List<M2> getM2ListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M2.class);
		dc.createAlias("m13F", "m13F");
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("m13F.failureCode"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M2> getM2ByM13fId(String m13fId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M2.class);
		dc.add(Restrictions.eq("m13F.id", m13fId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<MReferAfm> searchAfm(String m2Id) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MReferAfm.class);
		dc.add(Restrictions.eq("m2.id", m2Id));
		return this.findByCriteria(dc);
	}

	@Override
	public List<MReferMmel> searchMmel(String m2Id) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MReferMmel.class);
		dc.add(Restrictions.eq("m2.id", m2Id));
		return this.findByCriteria(dc);
	}


}
