package com.rskytech.sys.bo;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMsg;

public interface IM5Bo extends IBo {
	/**
	 * 根据任务Id查询所属的原因编号及故障影响类型
	 * @param taskId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-15
	 */
	public List getCauseCodeAndCauseTypeByTaskId(String taskId)throws BusinessException;
	/**
	 * 
	 * @param sysUser   当前用户
	 * @param systemSystemCode 所属系统
	 * @param jsonData   json数据集	
	 * @param msiId      Main表Id
	 * @param m5         当前页面
	 * @return
	 */
	public ArrayList<String> saveM5(ComUser sysUser, String systemSystemCode,
			String jsonData, String msiId, String m5,ComModelSeries comModelSeries);
	
	public void deleteM5HeBing(String deleteId,ComUser user,ComModelSeries comModelSeries)throws BusinessException;
	
	/**
	 * 添加mrb和mpd任务
	 * @param taskMsg
	 * @param userId
	 */
	public void addMrbAndMpd(TaskMsg taskMsg, String userId);
	
	/**
	 * 查询M5需要合并的任务
	 * @param msiId
	 * @return
	 */
	public List<TaskMsg> searchM5HeBing(String msiId);
}
