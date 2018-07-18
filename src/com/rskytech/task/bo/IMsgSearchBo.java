package com.rskytech.task.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;

public interface IMsgSearchBo extends IBo {

	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMsgList(String msId, String sourceSystem, String taskType, String taskCode, Page page);
}
