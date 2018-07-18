package com.rskytech.task.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMpd;

public interface IMpdMaintainDao extends IDAO {

	public List<TaskMpd> getTaskMpdList(String msId, String sourceSystem, String taskType, String mpdCode, Page page);
	
	public List<TaskMpd> getTaskMpdList(String mpdId, String mpdCode,String modelId);
	
	public List<TaskMpd> getTaskMpdList(String msId);
	
	public List<TaskMpd> getTaskMpdListByMsIdAndMpdCode(String msId, String mpdCode);

	public List getTaskMpdListBysql(String modelSeriesId);
}
