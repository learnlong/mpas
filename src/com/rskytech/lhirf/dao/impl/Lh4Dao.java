package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILh4Dao;
import com.rskytech.pojo.Lh4;

@SuppressWarnings("unchecked")
public class Lh4Dao extends BaseDAO implements ILh4Dao {

	@Override
	public List<Lh4> getLh4ListBylhHsId(String hsiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Lh4.class);
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		return this.findByCriteria(dc);
	}

}
