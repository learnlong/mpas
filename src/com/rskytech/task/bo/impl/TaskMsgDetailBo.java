package com.rskytech.task.bo.impl;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaStepBo;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;
import com.rskytech.pojo.ZaMain;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.task.dao.ITaskMsgDetailDao;

public class TaskMsgDetailBo extends BaseBO implements ITaskMsgDetailBo {
	private ITaskMsgDetailDao taskMsgDetailDao;
	private IZaStepBo zaStepBo;
	private IZa1Dao za1Dao;

	/**
	 * 根据任务Id和区域Id查询转移任务明细
	 * @return
	 */
	public TaskMsgDetail getDetailByTaskIdAndAreaId(String taskId,String areaId) {
		 List<TaskMsgDetail> list = taskMsgDetailDao.getDetailByTaskIdAndAreaId(taskId,areaId);
	       if(list.size()>0){
	    	   return list.get(0);
	       }
		return null;
	}
	
	public List<TaskMsgDetail> getListDetailTaskBytaskId(String taskId)
			throws BusinessException {
		 return  taskMsgDetailDao.getListDetailTaskBytaskId(taskId);

	}
	
	/**
	 * 当任务转移区域时在转移任务明细表插入数据
	 */
	public void addTaskMsgDetail(TaskMsg taskMsg, String userId) {
		String ownArea = taskMsg.getOwnArea();
		if (ownArea == null || ownArea == "") {
			delTaskMsgDetail( taskMsg,  userId);
			return;
		}
		String[] areaIds = ownArea.split(",");
		String taskId = taskMsg.getTaskId();
		List<String> idList = new ArrayList<String>();
		Integer hasAccept = ComacConstants.YES ;
		int num = 0;
		for (String string : areaIds) {
			TaskMsgDetail detail = getDetailByTaskIdAndAreaId(taskId, string);
			if (detail == null) {
				detail = new TaskMsgDetail();
				detail.setTaskMsg(taskMsg);
				detail.setValidFlag(ComacConstants.YES);
				detail.setWhereTransfer(string);
				this.save(detail, userId);
			}
			if(ComacConstants.NO.equals(detail.getHasAccept())){
					num++;
			}else if(!ComacConstants.NO.equals(hasAccept)&&(detail.getHasAccept()==null ||
					"".equals(detail.getHasAccept()))){
				hasAccept = null;
				
			}
			idList.add(detail.getComTaskDetId());
		}
		if(num>0){
			hasAccept = ComacConstants.NO;
		}
		if(hasAccept!=ComacConstants.YES){
			taskMsg.setHasAccept(hasAccept);
			this.update(taskMsg);
		}
		if(idList.size()>0){
			 List<TaskMsgDetail> list = taskMsgDetailDao.searchTaskMsgDetailById(taskId,idList);
			 for (TaskMsgDetail taskMsgDetail : list) {
				 this.delete(taskMsgDetail, userId);
			}
		}
	}
	
	/**
	 * 当转移区域设为否时，从任务转移区域表中删除数据
	 * 
	 * @param taskMsg
	 */
	public void delTaskMsgDetail(TaskMsg taskMsg, String userId) {
		String taskId = taskMsg.getTaskId();
		List<TaskMsgDetail> list = this.getListDetailTaskBytaskId(taskId);
		for (TaskMsgDetail taskMsgDetail : list) {
			this.delete(taskMsgDetail, userId);
		}

	}
	
	public void updateZa7Status(String modelSeriesId,String userId,String areaId){
		ZaMain zaMain =za1Dao.getZaMainByAreaId(modelSeriesId,areaId);
		if(zaMain!=null){
			zaStepBo.updateZa7StepAndStatus(userId, modelSeriesId, zaMain);
		}
	}
	
	public ITaskMsgDetailDao getTaskMsgDetailDao() {
		return taskMsgDetailDao;
	}

	public void setTaskMsgDetailDao(ITaskMsgDetailDao taskMsgDetailDao) {
		this.taskMsgDetailDao = taskMsgDetailDao;
	}

	public IZaStepBo getZaStepBo() {
		return zaStepBo;
	}

	public void setZaStepBo(IZaStepBo zaStepBo) {
		this.zaStepBo = zaStepBo;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}
	
	
}
