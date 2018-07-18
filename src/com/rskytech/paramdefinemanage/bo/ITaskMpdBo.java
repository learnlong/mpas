package com.rskytech.paramdefinemanage.bo;

import java.util.Map;

import com.richong.arch.bo.BusinessException;
import com.rskytech.pojo.TaskMpd;

public interface ITaskMpdBo {
	
	/**
	 * 查询mpd任务信息
	 * @param modelSeiesId
	 * @param SOURCE_SYSTEM
	 * @return
	 * @throws BusinessException
	 */
	public Map<TaskMpd, String> findTaskMpdList(String modelSeiesId, String SOURCE_SYSTEM) throws BusinessException;

}
