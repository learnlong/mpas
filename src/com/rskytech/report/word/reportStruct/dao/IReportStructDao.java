package com.rskytech.report.word.reportStruct.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;

public interface IReportStructDao extends IDAO{
	
	/**
	 * 根据S1Id查询由S3,或者S6产生的最终任务
	 * @param s1Id
	 * @param msId 
	 * @return
	 */
	public List<TaskMsg> getTaskByS1Id(String s1Id,String Step, String msId,String inOrOut);
}
