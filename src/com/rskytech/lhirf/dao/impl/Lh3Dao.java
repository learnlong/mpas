package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.lhirf.dao.ILh3Dao;
import com.rskytech.pojo.Lh3;

public class Lh3Dao extends BaseDAO implements ILh3Dao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Lh3> getLh3ListByHsiId(String hsiId, Page page)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Lh3.class);	
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		dc.addOrder(Order.desc("createDate"));	
		return this.findByCriteria(dc, page).getResult();
	}


	@SuppressWarnings("unchecked")
	public List<Lh3> getLh3ListByHsiIdNoPage(String hsiId) throws BusinessException {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Lh3.class);	
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		dc.addOrder(Order.desc("createDate"));	
		return this.findByCriteria(dc);
	}
}
