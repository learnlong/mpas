package com.rskytech.basedata.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComMmelDao;
import com.rskytech.pojo.ComMmel;

public class ComMmelDao extends BaseDAO implements IComMmelDao {

	@SuppressWarnings("unchecked")
	public List<ComMmel> loadMmelList(String msId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComMmel.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("mmelCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkMmel(String msId, String mmelId, String mmelCode) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComMmel.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("mmelCode", mmelCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComMmel> list = this.findByCriteria(dc);
		
		if (list == null || list.size() == 0){
			return false;
		} else if (mmelId == null || "".equals(mmelId)){
			return true;
		} else {
			ComMmel cm = list.get(0);
			if (cm.getMmelId().equals(mmelId)){
				return false;
			}
		}
		return true;
	}
}
