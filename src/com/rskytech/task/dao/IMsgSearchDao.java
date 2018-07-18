package com.rskytech.task.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

public interface IMsgSearchDao extends IDAO {

	public List<TaskMsg> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page);
}
