package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za43;

public class Za43Dao extends BaseDAO implements IZa43Dao {

	@SuppressWarnings("unchecked")
	public Za43 getZa43ByZaIdAndTaskId(String zaId, String taskId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za43.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		dc.add(Restrictions.eq("taskId", taskId));
		List<Za43> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public void deleteZa43(String zaId, String taskId) throws BusinessException{
		String s = "delete za_43 t where t.za_id = '" + zaId + "' and t.task_id = '" + taskId + "'";
		this.executeBySql(s);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskList(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA_4_1"), Restrictions.eq("sourceStep", "ZA_4_2")));
		dc.add(Restrictions.isNotNull("taskCode"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.desc("taskCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskListNoAna(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA_4_1"), Restrictions.eq("sourceStep", "ZA_4_2")));
		dc.add(Restrictions.isNull("taskCode"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.desc("taskCode"));
		return this.findByCriteria(dc);
	}
}
