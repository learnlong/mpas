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
import com.rskytech.pojo.TaskMpd;
import com.rskytech.task.dao.IMpdMaintainDao;

public class MpdMaintainDao extends BaseDAO implements IMpdMaintainDao {

	@SuppressWarnings("unchecked")
	public List<TaskMpd> getTaskMpdList(String msId, String sourceSystem, String taskType, String mpdCode, Page page){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpd.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		
		if (!BasicTypeUtils.isNullorBlank(sourceSystem)) {
			dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		}
		if (!BasicTypeUtils.isNullorBlank(taskType)) {
			dc.add(Restrictions.eq("taskType", taskType));
		}
		if (!BasicTypeUtils.isNullorBlank(mpdCode)) {
			dc.add(Restrictions.like("mpdCode", mpdCode, MatchMode.ANYWHERE));
		}
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("mpdCode"));
		return this.findByCriteria(dc, page).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMpd> getTaskMpdList(String mpdId, String mpdCode,String modelId){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpd.class);
		dc.add(Restrictions.eq("mpdCode", mpdCode));
		dc.add(Restrictions.eq("comModelSeries.id", modelId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		if (mpdId != null && !"".equals(mpdId)) {
			dc.add(Restrictions.ne("mpdId", mpdId));
		}
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMpd> getTaskMpdList(String msId){
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpd.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("sourceSystem"));
		dc.addOrder(Order.asc("mpdCode"));
		return this.findByCriteria(dc);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getTaskMpdListBysql(String modelSeriesId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.source_system,");
		sb.append("       a.mpd_code,");
		sb.append("       a.task_type,");
		sb.append("       a.task_interval_original,");
		sb.append("       a.task_desc,");
		sb.append("       a.reach_way,");
		sb.append("       a.own_area,");
		sb.append("       a.effectiveness,");
		sb.append("       a.amm,");
		sb.append("       a.work_time");
		sb.append("  from task_mpd a");
		sb.append(" where a.model_series_id = '"+ modelSeriesId + "'");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append(" order by a.source_system, a.mpd_code");
		return this.executeQueryBySql(sb.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskMpd> getTaskMpdListByMsIdAndMpdCode(String msId,
			String mpdCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpd.class);
		dc.add(Restrictions.eq("mpdCode", mpdCode));
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
}
