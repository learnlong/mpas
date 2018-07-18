package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.IDefineBaseCrackLenDao;
import com.rskytech.pojo.CusDisplay;

public class DefineBaseCrackLenDao extends BaseDAO implements IDefineBaseCrackLenDao{
	@Override
	public List<CusDisplay> getLH4CusDisplayList(String modelSeriesId){
		DetachedCriteria dc = DetachedCriteria.forClass(CusDisplay.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("displayWhere", "LH4"));
		return this.findByCriteria(dc);
	}
}
