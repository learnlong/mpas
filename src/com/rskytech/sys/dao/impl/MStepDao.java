package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.dao.IMStepDao;

public class MStepDao extends BaseDAO implements IMStepDao {

	@Override
	public List<MStep> getMStepByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MStep.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}

}
