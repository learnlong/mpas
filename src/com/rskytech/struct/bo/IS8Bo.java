package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;

public interface IS8Bo extends IBo {
	/**
	 * 得到mrb数据集
	 * @param ssiId 当前分析中的ssiId
	 * @return taskMrb 数据集
	 * @throws BusinessException
	 * @author 赵涛
	 * @createdate 2012年8月31日
	 */
	public List<TaskMrb> getMrbRecords(String ssiId,String modelId) throws BusinessException;
	
	/**
	 * 得到S8grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 *  @author 赵涛
	 *  @createdate 2012年8月30日
	 */
	public List<TaskMsg> getS8Records(String ssiId,String step) throws BusinessException;

	/**
	 * 根据MrbCode查询mrb
	 * @param mrbCode
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskMrb> getMrbCountByCode(String mrbCode,ComModelSeries model) throws BusinessException;
	/**
	 * 根据MpdCode查询mpd
	 * @param mpd
	 * @return
	 * @throws BusinessException
	 */
	public TaskMpd getMpdByCode(String mpdCode,ComModelSeries model) throws BusinessException;
}


