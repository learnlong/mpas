package com.rskytech.task.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public interface ITaskMsgDetailBo extends IBo {
	
	/**
	 * 根据任务Id和区域Id查询转移任务明细
	 * @return
	 */
	public TaskMsgDetail getDetailByTaskIdAndAreaId(String taskId,String areaId);
	/**
	 * 根据 taskmsg 任务表中的 任务Id查找 
	 * @param taskId
	 * @return
	 */
	public List<TaskMsgDetail> getListDetailTaskBytaskId(String taskId)throws BusinessException;
	
	/**
	 * 当任务转移区域时在转移任务明细表插入数据
	 * 
	 */
	public void addTaskMsgDetail(TaskMsg taskMsg, String userId);
	/**
	 * 当转移区域设为否时，从任务转移区域表中删除数据
	 * 
	 * @param taskMsg
	 */
	public void delTaskMsgDetail(TaskMsg taskMsg, String userId);
	/**
	 * 当转区域任务被删除或者添加时，更新Za7的状态
	 * @param modelSeriesId
	 * @param userId
	 * @param areaId
	 */
	public void updateZa7Status(String modelSeriesId,String userId,String areaId);
}
