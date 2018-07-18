package com.rskytech.task.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public interface ITaskMsgDetailDao extends IDAO {

	
	/**
	 * 根据任务Id和区域Id查询转移任务明细
	 */
	public List<TaskMsgDetail> getDetailByTaskIdAndAreaId(String taskId,String areaId);
	/**
	 * 根据 taskmsg 任务表中的 任务Id查找 
	 * @param taskId
	 * @return
	 */
	public List<TaskMsgDetail> getListDetailTaskBytaskId(String taskId)throws BusinessException;
	
	/**
	 * 根据taskid查询其字表数据，子表id不在IdList中
	 * 
	 */
	public List<TaskMsgDetail> searchTaskMsgDetailById(String taskId,List<String> idList);


}
