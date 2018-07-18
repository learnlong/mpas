package com.rskytech.task.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;

public interface IMpdMaintainBo extends IBo {

	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMpdList(String msId, String sourceSystem, String taskType, String mpdCode, Page page);
	
	public boolean checkTaskMpdCode(String mpdId, String mpdCode,String modelId);
	
	public void saveMpd(String userId, ComModelSeries ms, String jsonData);
}
