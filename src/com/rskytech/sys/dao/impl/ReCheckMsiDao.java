package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.sys.dao.IReCheckMsiDao;

public class ReCheckMsiDao extends BaseDAO implements IReCheckMsiDao {

	@Override
	public List<MReferMsi> getReferListMsiByModelSeriesId(String modelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MReferMsi.class);
		dc.createAlias("m13C", "m13C");
		dc.createAlias("m13C.m13F", "m13F");
		dc.createAlias("m13F.m13", "m13");
		dc.createAlias("m13.MMain", "MMain");
		dc.add(Restrictions.eq("MMain.comModelSeries.modelSeriesId", modelSeriesId));
		return this.findByCriteria(dc);
	}

}
