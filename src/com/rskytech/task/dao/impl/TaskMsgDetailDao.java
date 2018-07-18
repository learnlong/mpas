package com.rskytech.task.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsgDetail;
import com.rskytech.task.dao.ITaskMsgDetailDao;

public class TaskMsgDetailDao extends BaseDAO  implements ITaskMsgDetailDao{


	/**
	 * 根据任务Id和区域Id查询转移任务明细
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TaskMsgDetail> getDetailByTaskIdAndAreaId(String taskId,String areaId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		dc.add(Restrictions.eq("taskMsg.taskId", taskId));
		dc.add(Restrictions.eq("whereTransfer", areaId));
		return  this.findByCriteria(dc);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TaskMsgDetail> getListDetailTaskBytaskId(String taskId)
			throws BusinessException {
		 DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		 dc.add(Restrictions.eq("taskMsg.id", taskId));
		 dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		 return  this.findByCriteria(dc);

	}
	
	/**
	 * 根据taskid查询其字表数据，子表id不在IdList中
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<TaskMsgDetail> searchTaskMsgDetailById(String taskId,List<String> idList) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		dc.add(Restrictions.eq("taskMsg.taskId", taskId));
		dc.add(Restrictions.not(Restrictions.in("comTaskDetId", idList)));
		return this.findByCriteria(dc);
	}
}
