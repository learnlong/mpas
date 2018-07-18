package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsg;
public interface IM5Dao  extends IDAO{
	/**
	 * 根据任务Id查询所属的原因编号及故障影响类型
	 * @param taskId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-15
	 */
	public List getCauseCodeAndCauseTypeByTaskId(String taskId)throws BusinessException;
	/**
	 * 根据任务Id查询m5合并任务
	 * @param sourceAnaId
	 * @throws BusinessException
	 */
	public List<TaskMsg> searchM5HeBing(String sourceAnaId)throws BusinessException;
}
