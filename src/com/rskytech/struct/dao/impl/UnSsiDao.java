package com.rskytech.struct.dao.impl;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IUnSsiDao;

public class UnSsiDao extends BaseDAO implements IUnSsiDao {

	@Override
	public List<TaskMsg> searchUnSsiList(String ssiId, String modelSeriesId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("sourceAnaId", ssiId));
		dc.add(Restrictions.eq("sourceSystem",ComacConstants.STRUCTURE_CODE));
		dc.add(Restrictions.eq("sourceStep","UNSSI"));
		dc.add(Restrictions.eq("comModelSeries.id",modelSeriesId));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
	
	
	
	
}
