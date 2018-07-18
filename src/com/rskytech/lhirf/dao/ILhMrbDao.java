package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMrb;

public interface ILhMrbDao extends IDAO {
	
	/**
	 * 登陆用户ID号 查询其分析权限下的 msg任务
	 * @param modelSeriesId 
	 * @param userId 用户ID
	 * @param  nowsysTem 查询任务生成系统
	 * @return objectlist[]
	 * @author wangyueli
	 * @createdate 2012-09-03
	 */
	 public List<Object[]> getLhMsgListBythree(String modelSeriesId, String userId,
				String taskType , String ipvOpvpOpve ,String taskInterval) throws BusinessException ;
	/**
	 *   根据 mrbCode 登陆用户ID号  机型Id查询
	 * 
	 * @param modelSeriesId
	 * @param userId 用戶id
	 * @param nowsysTem 任务生成所在子系统
	 */
	public List<TaskMrb> getMrbByMrbCode(String modelSeriesId,String mrbCode) throws BusinessException ;
}
