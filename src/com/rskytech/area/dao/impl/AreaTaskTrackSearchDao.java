package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IAreaTaskTrackSearchDao;
import com.rskytech.pojo.TaskMsg;

public class AreaTaskTrackSearchDao extends BaseDAO implements IAreaTaskTrackSearchDao {

	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskMsgList(String msId, String taskCode, Page page) {
		String s = "";
		if (!"".equals(taskCode)){
			s = "  and a.task_code like '%" + taskCode + "%'";
		}
		String sql = "select a.task_id, b.area_code, a.task_code, a.task_type, a.task_interval, " +
					"        a.task_interval_merge, a.reach_way, a.task_desc, a.task_valid, a.dest_task, a.source_step " +
					"from task_msg a, com_area b " +
					"where a.own_area = b.area_id " +
					"  and a.source_system = '" + ComacConstants.ZONAL_CODE + "' " +
					"  and a.model_series_id = '" + msId + "' " +
					s +
					"  and a.valid_flag = 1 " +
					"  and b.valid_flag = 1 " +
					"order by b.area_code, a.task_code";
		return this.findBySql(page, sql, null).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskList(String msId, String destTask) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA_4_1"), Restrictions.eq("sourceStep", "ZA_4_2")));
		dc.add(Restrictions.eq("taskValid", 1));
		dc.add(Restrictions.eq("destTask", destTask));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
}
