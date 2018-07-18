package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ITaskMrbMaintainBo;
import com.rskytech.pojo.TaskMrb;

public class TaskMrbMaintainBo extends BaseBO implements ITaskMrbMaintainBo{

	/**
	 * 根据任务来源查找任务
	 * 
	 * @param modelSeriesId
	 *            机型
	 * @param SourceSystem
	 *            任务来源
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskMrb> getMrbTaskBySourceSystem(String modelSeriesId, String sourceSystem) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMrb.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		return this.findByCritera(dc);
	}
	
	
}
