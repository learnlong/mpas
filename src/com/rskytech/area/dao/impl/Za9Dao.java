package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa9Dao;
import com.rskytech.pojo.TaskMsg;

public class Za9Dao extends BaseDAO implements IZa9Dao {
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> searchTask(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.eq("taskValid", ComacConstants.TASK_VALID_TOATA20));//任务有效性   ATA20
		dc.add(Restrictions.isNotNull("taskCode"));//任务编号
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
}
