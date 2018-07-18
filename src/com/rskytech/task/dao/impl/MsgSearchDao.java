package com.rskytech.task.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.dao.IMsgSearchDao;

public class MsgSearchDao extends BaseDAO implements IMsgSearchDao {

	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.isNull("taskValid"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.isNotNull("taskCode"));
		if (!BasicTypeUtils.isNullorBlank(sourceSystem)) {
			dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		}
		if (!BasicTypeUtils.isNullorBlank(taskType)) {
			dc.add(Restrictions.eq("taskType", taskType));
		}
		if (!BasicTypeUtils.isNullorBlank(taskCode)) {
			dc.add(Restrictions.like("taskCode", taskCode, MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc, page).getResult();
	}
}
