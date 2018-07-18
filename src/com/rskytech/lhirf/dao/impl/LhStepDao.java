package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILhStepDao;
import com.rskytech.pojo.LhStep;

public class LhStepDao extends BaseDAO implements ILhStepDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LhStep> getLhStepListBylhHsId(String lhHsi)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(LhStep.class);
		dc.add(Restrictions.eq("lhMain.id", lhHsi));
		return this.findByCriteria(dc);
	}

	
	
	
	
}
