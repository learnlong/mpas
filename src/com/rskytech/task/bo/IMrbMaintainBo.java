package com.rskytech.task.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;

public interface IMrbMaintainBo extends IBo {

	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMrbList(String msId, String sourceSystem, String taskType, String mrbCode, Page page);
	
	public boolean checkTaskMrbCode(String mrbId, String mrbCode, String modelId);
	
	public void saveMrb(String userId, String jsonData);
}
