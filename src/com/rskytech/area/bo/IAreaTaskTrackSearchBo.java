package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;

public interface IAreaTaskTrackSearchBo extends IBo {

	@SuppressWarnings("unchecked")
	public List<HashMap> getTaskMsgList(String msId, String taskCode, Page page);
}
