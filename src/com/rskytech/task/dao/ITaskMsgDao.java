package com.rskytech.task.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.TaskMsg;

public interface ITaskMsgDao extends IDAO {
	/**
     * 根据机型和任务编号查询任务
     * @param msiId
     * @param taskCode
     * @return
     * @throws BusinessException
     */
		public List<TaskMsg> getTaskByTaskCode(String modelSeriesId, String taskCode)
				throws BusinessException ;
		
		/**
		 * 查询系统转区域的任务
		 * @param modelSeriesId 
		 * @param sourceSystem
		 * @return
		 * @throws BusinessException
		 */
		public List<TaskMsg> getTaskMsgIsSysTransfer(String modelSeriesId, 
				String sourceSystem,String firstTextField,Page page) throws BusinessException;
		
		/**
		 * 查询结构转区域的任务
		 * @param modelSeriesId 
		 * @param sourceSystem
		 * @return
		 * @throws BusinessException
		 */
		public List<TaskMsg> getTaskMsgIsStructTransfer(String modelSeriesId,String sourceSystem,
				String firstTextField,Page page) throws BusinessException;
		
		/**
		 * 根据lhirf 或系统的Mainid 机型查询任务表中记录
		   @param modelSeriesId 当前机型id
		 * @param mainId 主表id生成任务的分析 mainId ID（四大MAIN的ID）
		 *  @param sourceSystem 产生任务系统
		 *  @param sourceStep  产生任务步骤
		 * @return
		 * @throws BusinessException
		 */
	public List<TaskMsg> getTaskMsgListByMainId(String modelSeriesId,
			String mainId,String sourceSystem, String sourceStep)
			throws BusinessException;
	
	/**
	 * 根据mrbId查询taskMsg
	 * @param modelSeriesId
	 * @param mrbId
	 * @return
	 */
	public List<TaskMsg> getTaskByMrbId(String modelSeriesId, String mrbId);
	
	/**
	 * 查询转区域任务，并且还没有被区域接收
	 * @param msId 机型系列ID
	 * @param areaId 区域ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<Object[]> getToAreaTaskNoAccept(String msId, String areaId) throws BusinessException;
	
	/**
	 * 查询还没有被合并和转移的增强区域任务
	 * @param msId 机型系列ID
	 * @param zaId 区域主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> getZengQiangAreaTaskNoAccept(String msId, String zaId) throws BusinessException;
	
	/**
	 * 查询增强区域任务
	 * @param msId 机型系列ID
	 * @param zaId 区域主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> getZengQiangAreaTask(String msId, String zaId) throws BusinessException;
	
	/**
	 * 查询标准区域任务
	 * @param msId 机型系列ID
	 * @param zaId 区域主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> getAreaStandardTask(String msId, String zaId) throws BusinessException;
	
	/**
	 * 查询区域任务
	 * @param msId 机型系列ID
	 * @param zaId 区域主表ID
	 * @param areaStep 任务生成的区域步骤
	 * @param anyContent 问题编号
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> findAreaTaskMsg(String msId, String zaId, String areaStep, String anyContent) throws BusinessException;
	
	/**
	 * 查询区域任务
	 * @param msId 机型系列ID
	 * @param zaId 区域主表ID
	 * @param areaStep 任务生成的区域步骤
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> findAreaTaskMsg(String msId, String zaId, String areaStep) throws BusinessException;
	
	/**
	 * 根据taskId删除msg-3、msgetail、mpd、mrb以及协调单数据
	 * @param taskId
	 */
	public boolean deleteTasksByTaskId(String taskId);
	
	/**
	 * 根据taskId删除标准区域任务，包括关于当前任务的自身、合并、接收、MRB、MPD等
	 * @param taskId
	 */
	public boolean deleteBiaoZhunTasksByTaskId(String msId, String taskId);
	
	/**
	 * 根据S1Id查询其产生的临时任务
	 * @param s1Id
	 * @param sourceStep 临时任务产生步骤
	 * @param inOrOut 
	 * @return
	 */
	public List<TaskMsg> getTempTaskMsgByS1Id(String s1Id, String sourceStep, String inOrOut);
	
	/**
	 * 查询某个分析中的所有任务
	 * @param msId 机型系列ID
	 * @param sourceSystem 分析类别
	 * @param sourceAnaId 分析主表ID
	 * @return 任务列表
	 * @author zhangjianmin
	 */
	public List<TaskMsg> findOneAnaAllTask(String msId, String sourceSystem, String sourceAnaId) throws BusinessException;
}
