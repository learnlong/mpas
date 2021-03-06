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
import com.rskytech.pojo.TaskMrb;
import com.rskytech.task.dao.IMrbMaintainDao;

public class MrbMaintainDao extends BaseDAO implements IMrbMaintainDao {

	@SuppressWarnings("unchecked")
	public List<TaskMrb> getTaskMrbList(String msId, String sourceSystem, String taskType, String mrbCode, Page page){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		
		if (!BasicTypeUtils.isNullorBlank(sourceSystem)) {
			dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		}
		if (!BasicTypeUtils.isNullorBlank(taskType)) {
			dc.add(Restrictions.eq("taskType", taskType));
		}
		if (!BasicTypeUtils.isNullorBlank(mrbCode)) {
			dc.add(Restrictions.like("mrbCode", mrbCode, MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("mrbCode"));
		return this.findByCriteria(dc, page).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMrb> getTaskMrbList(String mrbId, String mrbCode, String modelId){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("mrbCode", mrbCode));
		dc.add(Restrictions.eq("comModelSeries.id", modelId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		if (mrbId != null && !"".equals(mrbId)) {
			dc.add(Restrictions.ne("mrbId", mrbId));
		}
		return this.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskMrb> getTaskMrbList(String msId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("mrbCode"));
		return this.findByCriteria(dc);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getTaskMrbListBysql(String msId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.source_system,");
		sb.append("       a.mrb_code,");
		sb.append("       a.task_type,");
		sb.append("       a.task_interval_original,");
		sb.append("       a.task_desc,");
		sb.append("       a.reach_way,");
		sb.append("       a.own_area,");
		sb.append("       a.effectiveness,");
		sb.append("       FUN_GETTASKMSGBYMRBIDS(a.mrb_id, a.model_series_id) msgTaskCodes");
		sb.append("  from task_mrb a");
		sb.append(" where a.model_series_id = '"+ msId + "'");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append(" order by a.source_system, a.mrb_code");
		return this.executeQueryBySql(sb.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskMrb> getTaskMrbListByMsIdAndMrbCode(String msId,
			String mrbCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("mrbCode", mrbCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		return this.findByCriteria(dc);
	}
	
}
