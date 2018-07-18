package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.dao.IS1Dao;

public class S1Dao extends BaseDAO implements IS1Dao {

	@Override
	public List<SStep> getSstepBySssiId(String sssiId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(SStep.class);
		dc.add(Restrictions.eq("SMain",this.loadById(SMain.class, sssiId)));
		return this.findByCriteria(dc);
	}

	@Override
	public List<S1> getS1ListBySssiId(String sssiId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(S1.class);
		dc.add(Restrictions.eq("SMain",this.loadById(SMain.class, sssiId)));
		dc.addOrder(Order.asc("s1Id"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<SRemark> getRemarkBySssi(String sssiId)
			throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(SRemark.class);
		dc.add(Restrictions.eq("SMain",this.loadById(SMain.class, sssiId)));
		return this.findByCriteria(dc);
	}

	@Override
	public List<SMain> getSMainByAtaId(String ataId, String modelSeriesId) {
		DetachedCriteria dc=DetachedCriteria.forClass(SMain.class);
		dc.add(Restrictions.eq("comAta.id",ataId));
		dc.add(Restrictions.eq("comModelSeries.id",modelSeriesId));
		return this.findByCriteria(dc);
	}
	
	
}
