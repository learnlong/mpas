package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;

public interface IZa9Dao extends IDAO {

	/**
	 * 查询进入ATA20章的任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> searchTask(String msId, String zaId) throws BusinessException;
}
