package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

public interface IAreaTaskTrackSearchDao extends IDAO {

	public List<Object[]> getTaskMsgList(String msId, String taskCode, Page page);
	
	public List<TaskMsg> getTaskList(String msId, String destTask);
}
