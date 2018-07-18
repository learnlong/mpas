package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;

public interface IZa6Dao extends IDAO {

	/**
	 * 查询已经合并到标准任务的增强任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param destTask 合并增强任务的标准任务ID
	 * @author zhangjianmin
	 */
	public List<TaskMsg> findHeBingHouDeTask(String msId, String zaId, String destTask) throws BusinessException;
}
