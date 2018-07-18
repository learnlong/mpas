package com.rskytech.lhirf.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMrb;

public interface ILhMrbBo extends IBo {
	

	/**
	 * 登陆用户ID号 查询其分析权限下的 msg任务
	 * 
	 * @param modelSeriesId
	 * @param userId 用戶id
	 * @param nowsysTem 任务生成所在子系统
	 * @return objectlist[]
	 * @author wangyueli
	 * @createdate 2012-09-03
	 */
	public List<Object[]> getLhMsgListBythreeUserId(String modelSeriesId, String userId,
			String taskType , String ipvOpvpOpve ,String taskInterval) throws BusinessException ;
	/**
	 *   根据 mrbCode 登陆用户ID号  机型Id查询
	 * 
	 * @param modelSeriesId
	 * @param userId 用戶id
	 * @param nowsysTem 任务生成所在子系统
	 * @return objectlist[]
	 * @author wangyueli
	 * @createdate 2012-09-03
	 */
	public TaskMrb getMrbByMrbCode(String modelSeriesId,String mrbCode) throws BusinessException ;
	

	/**
	 * MRB 维护中 保存 MSG-3中的MRB时 同时保存两张表
	 * @param String jsonData MRB页面传入的数据 
	 * @param String jsonDataMsg MSG-3 页面传入的数据 
	 * @param comuser  user
	 * @throws BusinessException
	 * @author wangyueli
	 * @param comModelSeries 
	 * @createdate 2012-8-25
	 */
	public void doSaveMrbMpdAndMsg(String jsonData,String jsonDataMsg,String deleteMrbIds,ComUser user, ComModelSeries comModelSeries) throws BusinessException  ;
}
