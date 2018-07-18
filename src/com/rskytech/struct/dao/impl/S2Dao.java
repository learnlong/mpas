package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.S2;
import com.rskytech.struct.dao.IS2Dao;

public class S2Dao extends BaseDAO implements IS2Dao {

	@Override
	public List<S2> getS2BySssId(String sssId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(S2.class);
		dc.add(Restrictions.eq("SMain.id", sssId));
		return this.findByCriteria(dc);
	}

}
