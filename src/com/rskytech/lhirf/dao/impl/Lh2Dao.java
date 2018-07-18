package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILh2Dao;
import com.rskytech.pojo.Lh2;

public class Lh2Dao extends BaseDAO implements ILh2Dao {

	@Override
	public List<Lh2> getLh2ListByHsiId(String hsiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Lh2.class);
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		return this.findByCriteria(dc);
	}

}
