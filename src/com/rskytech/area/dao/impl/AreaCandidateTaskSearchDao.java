package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IAreaCandidateTaskSearchDao;
import com.rskytech.pojo.TaskMsg;

public class AreaCandidateTaskSearchDao extends BaseDAO implements IAreaCandidateTaskSearchDao {

	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (!"".equals(sourceSystem)){
			dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		} else {
			dc.add(Restrictions.in("sourceSystem", new Object[] {ComacConstants.LHIRF_CODE, ComacConstants.STRUCTURE_CODE, ComacConstants.SYSTEM_CODE}));
		}
		
		if (!"".equals(taskType)){
			dc.add(Restrictions.eq("taskType", taskType));
		}
		
		if (!"".equals(taskCode)){
			dc.add(Restrictions.like("taskCode", taskCode, MatchMode.ANYWHERE));
		}
		
		dc.add(Restrictions.eq("taskValid", ComacConstants.TASK_VALID_AREAACCPET));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc, page).getResult();
	}
}
