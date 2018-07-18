package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IS7Dao;

public class S7Dao extends BaseDAO implements IS7Dao {

	@Override
	public List<TaskMsg> getS7Records(String ssiId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("sourceAnaId",ssiId));
		dc.add(Restrictions.isNull("anyContent4"));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.or(Restrictions.isNull("taskValid"), Restrictions.eq("taskValid", 2)));
		return this.findByCriteria(dc);
	}

	@Override
	public Page getStructToAreaRecords(Integer needTransfer, Integer hasAccept,
			Page page, String modelId) throws BusinessException {

		DetachedCriteria dc=DetachedCriteria.forClass(TaskMsg.class);
		if(needTransfer!=null&&needTransfer!=2){
			dc.add(Restrictions.eq("needTransfer",needTransfer));
		}
		if(hasAccept!=null&&hasAccept!=2&&hasAccept!=3){
			dc.add(Restrictions.eq("hasAccept",hasAccept));
		}
		if(hasAccept!=null&&hasAccept==3){
			dc.add(Restrictions.isNull("hasAccept"));
		}
		dc.add(Restrictions.eq("validFlag",ComacConstants.YES));
		dc.add(Restrictions.eq("sourceSystem",ComacConstants.STRUCTURE_CODE));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelId));
		dc.addOrder(Order.asc("taskCode"));
		return 	this.findByCriteria(dc, page);
	
	}

}
