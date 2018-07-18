package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za43;

public interface IZa43Dao extends IDAO {

	/**
	 * 通过区域主表ID，查询ZA43
	 * @param zaId 区域主表ID
	 * @param taskId 任务ID
	 * @return Za43
	 * @author zhangjianmin
	 */
	public Za43 getZa43ByZaIdAndTaskId(String zaId, String taskId) throws BusinessException;
	
	/**
	 * 删除ZA43
	 * @param zaId 区域主表ID
	 * @param taskId 任务ID
	 * @author zhangjianmin
	 */
	public void deleteZa43(String zaId, String taskId) throws BusinessException;
	
	/**
	 * 查询已经分析的增强区域任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return List<TaskMsg>
	 * @author zhangjianmin
	 */
	public List<TaskMsg> getTaskList(String msId, String zaId) throws BusinessException;
	
	/**
	 * 查询没有分析的增强区域任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return List<TaskMsg>
	 * @author zhangjianmin
	 */
	public List<TaskMsg> getTaskListNoAna(String msId, String zaId) throws BusinessException;
}
