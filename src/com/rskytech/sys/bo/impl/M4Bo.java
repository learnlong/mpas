package com.rskytech.sys.bo.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M4;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM4Dao;

public class M4Bo extends BaseBO implements IM4Bo {
	private ITaskMsgBo taskMsgBo;
	private IMStepBo mstepBo;
	private IM4Dao m4Dao;
	
	public IM4Dao getM4Dao() {
		return m4Dao;
	}

	public void setM4Dao(IM4Dao m4Dao) {
		this.m4Dao = m4Dao;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	@Override
	public M4 getM4ByTaskId(String taskId) throws BusinessException {
		List<M4> list = this.m4Dao.getM4ByTaskId(taskId);
		if(list.size()>0){
			return list.get(0);
		}
			return null;
	}

	/**
	 * 根据任务Id查询与之有关系的M2
	 * 
	 * @param taskId
	 * @return
	 * @throws BusinessException
	 * @author chendexu createdate 2012-08-29
	 */
	@Override
	public List<M2> getListM2ByTaskId(String taskId) throws BusinessException {
		return m4Dao.getListM2ByTaskId(taskId);
	}

	@Override
	public List<M4> getListM4ByMsiId(String msiId) throws BusinessException {
		return this.m4Dao.getListM4ByMsiId(msiId);
	}

	@Override
	public void saveM4(ComUser user, String sourceSystem, String pageId,
			String jsonData, String taskId, String msiId,ComModelSeries comModelSeries) {
		this.saveComLogOperate(user, pageId, sourceSystem);// 保存com_log_operate
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		// db操作区分
		String dbOperate = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			M4 m4 = this.getM4ByTaskId(taskId);
			if (m4 != null) {
				// 修改操作
				dbOperate = ComacConstants.DB_UPDATE;
			} else {
				// 添加操作
				m4 = new M4();
				dbOperate = ComacConstants.DB_INSERT;
				m4.setMMain(new MMain());
				m4.getMMain().setMsiId(msiId);
				m4.setTaskId(taskId);
			}
			m4.setAna(jsonObject.getString("ana"));
			m4.setSimilar(jsonObject.getString("similar"));
			m4.setEngineerReview(jsonObject.getString("engineerReview"));
			m4.setEngineerSuggest(jsonObject.getString("engineerSuggest"));
			m4.setGroupReview(jsonObject.getString("groupReview"));
			m4.setOther(jsonObject.getString("other"));
			m4.setRemark(jsonObject.getString("remark"));
			this.saveOrUpdate(m4, dbOperate, user.getUserId());
		
			TaskMsg taskMsg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class,
					taskId);
			taskMsg.setTaskInterval(jsonObject.getString("taskInterval"));
//			taskMsg.setTaskIntervalRepeat(jsonObject.getString("taskInterval"));// 重复检查间隔
			this.taskMsgBo.saveOrUpdate(taskMsg, ComacConstants.DB_UPDATE, user
					.getUserId());
			String modelSeriesId = comModelSeries.getModelSeriesId();
			// 修改步骤
			MStep mstep = mstepBo.getMStepByMsiId(msiId);
			Integer tsakSize = this.taskMsgBo.getTaskMsgListByMainId(
					modelSeriesId, msiId,ComacConstants.SYSTEM_CODE, "M3").size();//当前MSI下的任务数量
			if (tsakSize == this.getListM4ByMsiId(msiId).size()) {
				// 当前MSI下的任务数量和当前MSI下的M4数量相等即任务都已分析完成
				//M4的状态设为分析完成
				mstep.setM4(ComacConstants.STEP_FINISH);
				if (mstep.getM5().equals(ComacConstants.STEP_NO)) {
					// M5处于未分析状态
					//M5状态设为正在分析
					mstep.setM5(ComacConstants.STEP_NOW);
				}
			}
			this.mstepBo.update(mstep, user.getUserId());
		}
	}
	
	@Override
	public void deleteM4ByTaskId(String taskId) {
		this.m4Dao.deleteM4ByTaskId(taskId);
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

}
