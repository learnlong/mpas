package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa6Dao;
import com.rskytech.pojo.TaskMsg;

public class Za6Dao extends BaseDAO implements IZa6Dao {

	@SuppressWarnings("unchecked")
	public List<TaskMsg> findHeBingHouDeTask(String msId, String zaId, String destTask) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.eq("destTask", destTask));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
}
