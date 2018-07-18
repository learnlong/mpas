package com.rskytech.task.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

/**
 * 执行对任务表的增删改查操作
 * @author ZhangYaLin
 * @createdate 2012-08-20
 */
public interface ITaskMsgBo extends IBo {
	
	 /**
     * 根据机型和任务编号查询任务
     * @param modelSeriesId
     * @param taskCode
     * @return
     * @throws BusinessException
     */
	public TaskMsg getTaskByTaskCode(String modelSeriesId, String taskCode) throws BusinessException;
	/**
	 * 根据lhirf 或系统的Mainid 机型查询任务表中记录
	 *  
	 *  @param modelSeriesId 当前机型id
	 * @param mainId 主表id生成任务的分析 mainId ID（四大MAIN的ID）
	 *  @param sourceSystem 产生任务系统
	 *  @param sourceStep  产生任务步骤
	 * @throws BusinessException
	 */
	public List<TaskMsg> getTaskMsgListByMainId(String modelSeriesId,String mainId,String sourceSystem ,String sourceStep) throws BusinessException ;
	
	/**
	 * 查询系统转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskMsg>  getTaskMsgIsSysTransfer(String modelSeriesId,String sourceSystem,
			String firstTextField,Page page)throws BusinessException;
	
	/**
	 * 查询系统转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @param firstTextField
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskMsg>  getTaskMsgIsStructTransfer(String modelSeriesId,String sourceSystem,
			String firstTextField,Page page)throws BusinessException;
	
	/**
	 * 删除区域任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param type ALL、ZA4、ZA5A、ZA5B
	 * @author zhangjianmin
	 */
	public void deleteAreaTask(String msId, String zaId, String type);
	/**
	 * 根据mrbId查询taskMsg
	 * @param modelSeriesId
	 * @param mrbId
	 * @return
	 */
	public List<TaskMsg> getTaskByMrbId(String modelSeriesId, String mrbId);
	
	/**
	 * 根据S1Id查询其产生的临时任务
	 * @param s1Id
	 * @param sourceStep 临时任务产生步骤
	 * @param inOrOut 
	 * @return
	 */
	public List<TaskMsg> getTempTaskMsgByS1Id(String s1Id,String sourceStep, String inOrOut);
	
	/**
	 * 根据taskId删除msg-3、msgetail、mpd、mrb以及协调单数据
	 * @param taskId
	 */
	public boolean deleteTaskMsgById(String taskId);
}
