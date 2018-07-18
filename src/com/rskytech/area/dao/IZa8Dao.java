package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public interface IZa8Dao extends IDAO {
	
	/**
	 * 查询进入维修大纲的任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> searchTask(String msId, String zaId) throws BusinessException;
	
	/**
	 * 查询任务明细
	 * @param taskId 任务ID
	 * @return 任务明细列表
	 * @author zhangjianmin
	 */
	public List<TaskMsgDetail> getTaskMsgDetailList(String taskId) throws BusinessException;
}
