package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

public interface IAreaCandidateTaskSearchDao extends IDAO {

	public List<TaskMsg> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page);
}
