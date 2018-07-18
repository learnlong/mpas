package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IS8Dao;

@SuppressWarnings("unchecked")
public class S8Dao extends BaseDAO implements IS8Dao {


	@Override
	public List<TaskMrb> getMrbRecords(String ssiId,String modelId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("sourceAnaId",ssiId));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelId));
		dc.addOrder(Order.asc("mrbCode"));
		return this.findByCriteria(dc);
	}

	/**
	 * 得到S7grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 */
	@Override
	public List<TaskMsg> getS8Records(String ssiId,String step) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("sourceAnaId",ssiId));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		if("S8".equals(step)){
			dc.add(Restrictions.or(Restrictions.eq("sourceStep", step), Restrictions.eq("sourceStep","S6")));
		}
		dc.add(Restrictions.isNull("taskValid"));
		dc.add(Restrictions.or(Restrictions.eq("needTransfer", ComacConstants.NO), Restrictions.eq("hasAccept", ComacConstants.NO)));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<TaskMrb> getTaskMrbByTaskCode(String mrbCode,ComModelSeries model) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("mrbCode",mrbCode));
		dc.add(Restrictions.eq("comModelSeries",model));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}

	@Override
	public List<TaskMpd> getMpdByCode(String mpdCode, ComModelSeries model)
			throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMpd.class);
		dc.add(Restrictions.eq("mpdCode",mpdCode));
		dc.add(Restrictions.eq("comModelSeries",model));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}

}
