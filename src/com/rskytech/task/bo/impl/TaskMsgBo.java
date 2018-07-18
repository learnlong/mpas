package com.rskytech.task.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.dao.ITaskMsgDao;

/**
 * 执行对任务表的增删改查操作
 */
public  class TaskMsgBo extends BaseBO implements ITaskMsgBo {
	private ITaskMsgDao taskMsgDao;

	/**
     * 根据机型和任务编号查询任务
     * @param msiId
     * @param taskCode
     * @return
     * @throws BusinessException
     */
		public TaskMsg getTaskByTaskCode(String modelSeriesId, String taskCode)
				throws BusinessException {
			List<TaskMsg> list = taskMsgDao.getTaskByTaskCode(modelSeriesId, taskCode);
			if(list.size()>0){
				return list.get(0);
			}
			return null;
		}
		
		/**
		 * 根据lhirf 或系统的Mainid 机型查询任务表中记录
		   @param modelSeriesId 当前机型id
		 * @param mainId 主表id生成任务的分析 mainId ID（四大MAIN的ID）
		 *  @param sourceSystem 产生任务系统
		 *  @param sourceStep  产生任务步骤
		 * @return
		 * @throws BusinessException
		 */
	public List<TaskMsg> getTaskMsgListByMainId(String modelSeriesId,String mainId, String sourceSystem, String sourceStep)
			throws BusinessException {
		return taskMsgDao.getTaskMsgListByMainId(modelSeriesId, mainId,sourceSystem, sourceStep);
	}

	/**
	 * 查询系统转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskMsg> getTaskMsgIsSysTransfer(String modelSeriesId, 
			String sourceSystem,String firstTextField,Page page) throws BusinessException {
		return taskMsgDao.getTaskMsgIsSysTransfer(modelSeriesId,sourceSystem,firstTextField,page);
	}
	
	/**
	 * 查询结构转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @return
	 * @throws BusinessException
	 */
	public List<TaskMsg> getTaskMsgIsStructTransfer(String modelSeriesId, 
			String sourceSystem,String firstTextField,Page page) throws BusinessException {
		return taskMsgDao.getTaskMsgIsStructTransfer(modelSeriesId,sourceSystem,firstTextField,page);
	}
	
	@Override
	public List<TaskMsg> getTaskByMrbId(String modelSeriesId, String mrbId) {
		return this.taskMsgDao.getTaskByMrbId(modelSeriesId, mrbId);
	}

	@Override
	public List<TaskMsg> getTempTaskMsgByS1Id(String s1Id,String sourceStep,String inOrOut) {
		return this.taskMsgDao.getTempTaskMsgByS1Id(s1Id,sourceStep,inOrOut);
	}
	
	@Override
	public boolean deleteTaskMsgById(String taskId) {
		return this.taskMsgDao.deleteTasksByTaskId(taskId);
	}
	
	public void deleteAreaTask(String msId, String zaId, String type){
		List<TaskMsg> list = taskMsgDao.findOneAnaAllTask(msId, ComacConstants.ZONAL_CODE, zaId);
		
		if (list != null && list.size() > 0){
			if ("ALL".equals(type)){
				for (TaskMsg tm : list){
					if ("ZA5A".equals(tm.getSourceStep()) || "ZA5B".equals(tm.getSourceStep())){
						taskMsgDao.deleteBiaoZhunTasksByTaskId(msId, tm.getTaskId());
					} else {
						taskMsgDao.deleteTasksByTaskId(tm.getTaskId());
					}
				}
			}
			
			if ("ZA5A".equals(type) || "ZA5B".equals(type)){
				for (TaskMsg tm : list){
					if ("ZA5A".equals(tm.getSourceStep()) || "ZA5B".equals(tm.getSourceStep())){
						if (type.equals(tm.getSourceStep())){
							taskMsgDao.deleteBiaoZhunTasksByTaskId(msId, tm.getTaskId());
						}						
					}
				}
			}
		}
		
		
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

}

