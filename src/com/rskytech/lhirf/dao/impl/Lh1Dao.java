package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILh1Dao;
import com.rskytech.pojo.Lh1;

public class Lh1Dao extends BaseDAO implements ILh1Dao {
     
	@SuppressWarnings("unchecked")
	@Override
	public List<Lh1> getLh1ByHsiId(String hsiId) throws BusinessException {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Lh1.class);
		dc.add(Restrictions.eq("lhMain.id", hsiId));
		List<Lh1> list = this.findByCriteria(dc);
		return list;
	}
	
	
}
