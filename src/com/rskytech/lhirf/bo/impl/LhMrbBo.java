package com.rskytech.lhirf.bo.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILhMrbBo;
import com.rskytech.lhirf.dao.ILhMrbDao;

import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.dao.IMpdMaintainDao;

public class LhMrbBo extends BaseBO implements ILhMrbBo {

	private ILhMrbDao lhMrbDao ;
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
	private IMpdMaintainDao mpdMaintainDao;
	

	@Override
	public TaskMrb getMrbByMrbCode(String modelSeriesId, String mrbCode) throws BusinessException {
		   List<TaskMrb> list = this.lhMrbDao.getMrbByMrbCode(modelSeriesId, mrbCode);
			if (list.size() > 0){
				return list.get(0);
			}
			return null;
	}

	@Override
	public List<Object[]> getLhMsgListBythreeUserId(String modelSeriesId,
			String userId, String taskType, String ipvOpvpOpve,
			String taskInterval) throws BusinessException {
		return this.lhMrbDao.getLhMsgListBythree(modelSeriesId, userId, taskType, ipvOpvpOpve, taskInterval);
	}
	
	@Override
	public void doSaveMrbMpdAndMsg(String jsonData, String jsonDataMsg, String deleteMrbIds,ComUser user, ComModelSeries comModelSeries)
			throws BusinessException {
		if(deleteMrbIds!=null){
			String[] deleteArray=deleteMrbIds.split(",");
			for(String mrbId:deleteArray){
				if("".equals(mrbId.trim())){
					continue;
				}
				TaskMrb deleteMrb = (TaskMrb) this.loadById(TaskMrb.class, mrbId.trim());
				if(deleteMrb !=null){
					List<TaskMsg> taskList = taskMsgBo.getTaskByMrbId(comModelSeries.getModelSeriesId(),mrbId.trim());
					if(taskList!=null&&taskList.size()>0){
						TaskMsg task = taskList.get(0);
						String mpdId = task.getMpdId();
						if(!BasicTypeUtils.isNullorBlank(mpdId)){
							this.delete((TaskMpd)this.loadById(TaskMpd.class, mpdId));
						}
						task.setMpdId(null);
						task.setMrbId(null);
						this.update(task,user.getUserId());
					}
					this.delete(deleteMrb, user.getUserId());
				}
			}
		}
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = ComacConstants.DB_UPDATE;
		String ownerArea =null;
		TaskMrb mrb;
		TaskMpd mpd;
		JSONArray jsonArrayMsg = JSONArray.fromObject(jsonDataMsg);
		JSONObject jsonOb = null;
		TaskMsg newMsg = null;
		for (int j = 0; j < jsonArrayMsg.size(); j++) {
			jsonOb = jsonArrayMsg.getJSONObject(j);
			String newMrbCode = jsonOb.getString("mrbTaskCode");
			String msgid = jsonOb.getString("taskId");
			if (!"".equals(newMrbCode)) {
				if (!BasicTypeUtils.isNullorBlank(msgid) && !"0".equals(msgid)) {
					newMsg = (TaskMsg) this.loadById(TaskMsg.class, msgid);
					if (newMsg != null) {
						for (int i = 0; i < jsonArray.size(); i++) {
							jsonObject = jsonArray.getJSONObject(i);
							if(newMrbCode.equals(jsonObject.getString("mrbCode"))){
								String id = jsonObject.getString("mrbId");
								if (!BasicTypeUtils.isNullorBlank(id)) { // 修改操作时
									mrb = (TaskMrb) this.loadById(TaskMrb.class, id);
									if(!BasicTypeUtils.isNullorBlank(newMsg.getMpdId())){
										mpd = (TaskMpd) this.loadById(TaskMpd.class, newMsg.getMpdId());
									}else{
										mpd = new TaskMpd();
										dbOperate = ComacConstants.DB_INSERT;
									}
								} else { // 添加操作
									mpd = new TaskMpd();
									mrb = new TaskMrb();
									dbOperate = ComacConstants.DB_INSERT;
									mrb.setComModelSeries(comModelSeries);
									mrb.setSourceSystem(ComacConstants.LHIRF_CODE);
									mrb.setValidFlag(ComacConstants.YES);
								}
								mrb.setAnyContent(jsonObject.getString("ipvOpvpOpve"));
								mrb.setEffectiveness(jsonObject.getString("effectiveness"));
								if(!BasicTypeUtils.isNullorBlank(jsonObject.getString("mrbownArea"))){
									ownerArea = comAreaBo.getAreaIdByAreaCode(jsonObject.getString("mrbownArea"), 
											comModelSeries.getModelSeriesId());
								}
								mrb.setOwnArea(ownerArea);
								mrb.setReachWay(jsonObject.getString("reachWay"));
								mrb.setTaskDesc(jsonObject.getString("taskDesc"));
								mrb.setTaskIntervalOriginal(jsonObject.getString("taskInterval"));
								mrb.setTaskType(jsonObject.getString("taskType"));
								mrb.setMrbCode(jsonObject.getString("mrbCode"));
								this.saveOrUpdate(mrb, dbOperate, user.getUserId());
								this.saveMPD(mpd, mrb, user, comModelSeries,dbOperate);
								if (BasicTypeUtils.isNullorBlank(newMsg.getMpdId())
										||BasicTypeUtils.isNullorBlank(newMsg.getMrbId())) {
									newMsg.setMrbId(mrb.getMrbId());
									newMsg.setMpdId(mpd.getMpdId());
									this.update(newMsg, user.getUserId());
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void saveMPD(TaskMpd mpd,TaskMrb mrb, ComUser user, ComModelSeries comModelSeries, String dbOperate){
		if(dbOperate.equals(ComacConstants.DB_INSERT)){
			mpd.setComModelSeries(comModelSeries);
			mpd.setSourceSystem(ComacConstants.LHIRF_CODE);
			mpd.setValidFlag(ComacConstants.YES);
			mpd.setMpdCode(mrb.getMrbCode()+"-01");
		}
		mpd.setEffectiveness(mrb.getEffectiveness());
		mpd.setOwnArea(mrb.getOwnArea());
		mpd.setReachWay(mrb.getReachWay());
		mpd.setTaskDesc(mrb.getTaskDesc());
		mpd.setTaskIntervalOriginal(mrb.getTaskIntervalOriginal());
		mpd.setTaskType(mrb.getTaskType());
		this.saveOrUpdate(mpd, dbOperate, user.getUserId());
	}

	public ILhMrbDao getLhMrbDao() {
		return lhMrbDao;
	}

	public void setLhMrbDao(ILhMrbDao lhMrbDao) {
		this.lhMrbDao = lhMrbDao;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IMpdMaintainDao getMpdMaintainDao() {
		return mpdMaintainDao;
	}

	public void setMpdMaintainDao(IMpdMaintainDao mpdMaintainDao) {
		this.mpdMaintainDao = mpdMaintainDao;
	}
	
}
