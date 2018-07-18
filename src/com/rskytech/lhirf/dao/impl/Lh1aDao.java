package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.dao.ILh1aDao;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.LhStep;

public class Lh1aDao extends BaseDAO implements ILh1aDao {

	@Override
	public List<LhStep> getLh5stepByCode(String refCode,String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(LhStep.class);
		dc.createAlias("lhMain", "lhMain");
		dc.add(Restrictions.eq("lhMain.hsiCode", refCode));
		dc.add(Restrictions.eq("lhMain.validFlag", ComacConstants.YES));
		dc.add(Restrictions.eq("lhMain.comModelSeries.id", modelSeriesId));
	    return this.findByCriteria(dc);
	}

	@Override
	public List<Lh1a> getLh1aListByHsiId(String hsiId) {
		DetachedCriteria dc = DetachedCriteria.forClass(Lh1a.class);
		dc.add(Restrictions.eq("lhMain.id", hsiId));
	    return this.findByCriteria(dc);
	}
     

	
	
	
	
}
