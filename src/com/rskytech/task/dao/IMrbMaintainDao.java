package com.rskytech.task.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMrb;

@SuppressWarnings("rawtypes")
public interface IMrbMaintainDao extends IDAO {

	public List<TaskMrb> getTaskMrbList(String msId, String sourceSystem, String taskType, String mrbCode, Page page);
	
	public List<TaskMrb> getTaskMrbList(String mrbId, String mrbCode,String modelId);
	
	public List<TaskMrb> getTaskMrbList(String mrbId);

	public List<TaskMrb> getTaskMrbListByMsIdAndMrbCode(String msId, String mrbCode);
	
	public List getTaskMrbListBysql(String msId);
}
