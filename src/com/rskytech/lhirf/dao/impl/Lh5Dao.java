package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILh5Dao;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh5;

@SuppressWarnings("unchecked")
public class Lh5Dao extends BaseDAO implements ILh5Dao {

	@Override
	public List<Lh5> getLh5ListByHsiId(String hsiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Lh5.class);
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		return this.findByCriteria(dc);
	}
	
	@Override
	public List<CusInterval> getCusIntervalbyFlg(String anaFlg,
			String internalFlg, String modelSeriesId) throws BusinessException {

		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("anaFlg", anaFlg));
		dc.add(Restrictions.eq("internalFlg", internalFlg));
		dc.addOrder(Order.asc("intervalLevel"));
		return this.findByCriteria(dc);
	}

}
