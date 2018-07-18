package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa8Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public class Za8Dao extends BaseDAO implements IZa8Dao {

	@SuppressWarnings("unchecked")
	public List<TaskMsg> searchTask(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.isNull("taskValid"));
		dc.add(Restrictions.isNull("anyContent2"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<TaskMsgDetail> getTaskMsgDetailList(String taskId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		dc.add(Restrictions.eq("hasAccept", ComacConstants.VALIDFLAG_YES));
	    dc.add(Restrictions.eq("destTask", taskId));
	    dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}	
}
