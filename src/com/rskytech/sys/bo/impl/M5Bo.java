package com.rskytech.sys.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.dao.IM5Dao;

public class M5Bo extends BaseBO implements IM5Bo {
	private IM5Dao m5Dao;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
	private IComCoordinationBo comCoordinationBo;
	
	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public void saveOrUpdateM5(TaskMsg taskMsg, String dbOperate, String userId,String modelSeriesId)
			throws BusinessException {
		this.saveOrUpdate(taskMsg, dbOperate, userId);
		if (ComacConstants.YES.equals(taskMsg.getNeedTransfer())) {
			// 任务转移到区域
			taskMsgDetailBo.addTaskMsgDetail(taskMsg, userId);
		} else {
			if(dbOperate.equals(ComacConstants.DB_UPDATE)){
				taskMsgDetailBo.delTaskMsgDetail(taskMsg,userId);
				//删除msg-3任务对应的协调单
				comCoordinationBo.deleteCoordination(taskMsg.getTaskId(), userId,modelSeriesId);
			}
		}
		return;
	}
	
	
	/**
	 * 根据任务Id查询所属的原因编号及故障影响类型
	 * 
	 * @param taskId
	 * @return
	 * @author chendexu createdate 2012-08-15
	 */
	@SuppressWarnings("rawtypes")
	public List getCauseCodeAndCauseTypeByTaskId(String taskId)
			throws BusinessException {
		return this.m5Dao.getCauseCodeAndCauseTypeByTaskId(taskId);
	}

	public ArrayList<String> saveM5(ComUser user, String sourceSystemCode,
			String jsonData, String msiId, String m5,ComModelSeries comModelSeries) {
		this.saveComLogOperate(user, m5, sourceSystemCode);
		String flag = "";
		JSONObject jsonObject = null;
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		TaskMsg taskMsg;
		// db操作区分
		String dbOperate = "";
		String areaId=null;
		String modelSeriesId = comModelSeries.getModelSeriesId();
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("taskId");
			String merge = jsonObject.getString("merge");// 是否是合并后的任务
			areaId = this.comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"), modelSeriesId);
			if (!BasicTypeUtils.isNullorBlank(id)) {
				taskMsg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class, id);
				// 修改操作
				dbOperate = ComacConstants.DB_UPDATE;
			} else {
				// 添加操作
				taskMsg = new TaskMsg();
				dbOperate = ComacConstants.DB_INSERT;
				taskMsg.setValidFlag(ComacConstants.YES);
				taskMsg.setSourceSystem(ComacConstants.SYSTEM_CODE);
				taskMsg.setSourceAnaId(msiId);
				taskMsg.setSourceStep(m5);
				taskMsg.setComModelSeries(comModelSeries);
			}

			if (BasicTypeUtils.isNumberString(merge)
					&& ComacConstants.YES.equals(Integer.valueOf(merge))) {
				// 是合并后的任务
				taskMsg.setTaskCode(jsonObject.getString("taskCode"));
				TaskMsg taskMsg1 = this.taskMsgBo.getTaskByTaskCode(
						modelSeriesId, taskMsg.getTaskCode());
				if (taskMsg1 != null
						&& !taskMsg1.getTaskId().equals(taskMsg.getTaskId())) {
					// 任务编号重复，修改失败
					flag = "yes";
				}
				taskMsg.setTaskType(jsonObject.getString("taskType"));
				taskMsg.setTaskDesc(jsonObject.getString("taskDesc"));
				taskMsg.setReachWay(jsonObject.getString("reachWay"));

				taskMsg.setTaskInterval(jsonObject.getString("taskInterval"));// 标准任务间隔
				taskMsg.setEffectiveness(jsonObject.getString("effectiveness"));
				taskMsg.setAnyContent1(ComacConstants.YES.toString());// 标记是合并任务
				taskMsg.setAnyContent2(jsonObject.getString("causeType"));
				taskMsg.setAnyContent3(jsonObject.getString("causeCode"));
			} else {
				// 不是合并后的任务
				if (BasicTypeUtils.isNumberString(jsonObject.getString("isHeBing"))
						&& ComacConstants.YES.equals(jsonObject.getInt("isHeBing"))) {
					// 是被合并的任务
					taskMsg.setTaskValid(ComacConstants.YES);// 任务有效性（0转ATA20、1被合并、2被区域接收、3临时）
					if (taskMsg.getMrbId() != null) {
						// 删除MRB任务
						this.delete(this.loadById(TaskMrb.class, taskMsg.getMrbId()));
						taskMsg.setMrbId(null);
					}
					if (taskMsg.getMpdId() != null) {
						// 删除MPD任务
						this.delete(this.loadById(TaskMpd.class, taskMsg.getMpdId()));
						taskMsg.setMpdId(null);
					}
				} else {
					if (ComacConstants.YES.equals(taskMsg.getTaskValid())) {
						taskMsg.setTaskValid(null);
						addMrbAndMpd(taskMsg, user.getUserId());
					}
				}
			}
			if(!flag.equals("yes")){
				if(dbOperate.equals(ComacConstants.DB_UPDATE)){
					if(taskMsg.getNeedTransfer()!=null){
						if(taskMsg.getNeedTransfer()!=jsonObject.getInt("needTransfer")){
							if(areaId!=null){
								arr.add(areaId);
							}
						}
					}else{
						if(jsonObject.getInt("needTransfer")==1){
							if(areaId!=null){
								arr.add(areaId);
							}
						}
					}
					if(jsonObject.getInt("needTransfer")==1){
						if(taskMsg.getOwnArea()!=null){
							if(!taskMsg.getOwnArea().equals(areaId)){
								arr.add(areaId);
								arr.add(taskMsg.getOwnArea());
							}
						}else{
							arr.add(areaId);
						}
					}
				}else{
					if(jsonObject.getInt("needTransfer")==1){
						arr.add(areaId);
					}
				}
				if ("1".equals(jsonObject.getString("needTransfer"))) {
					taskMsg.setNeedTransfer(ComacConstants.YES);// 是否要转移
					taskMsg.setSysTransfer(ComacConstants.ZONAL_CODE);// 转移到的系统
					//如果任务区域和协调单区域不相同，将协调单的接收区域改为msg-3任务区域
					comCoordinationBo.modifyCoordination(id, user.getUserId(),jsonObject.getString("ownArea"),comModelSeries.getModelSeriesId());
				} else {
					taskMsg.setNeedTransfer(ComacConstants.NO);// 是否要转移
					taskMsg.setSysTransfer(null);// 转移到的系统
					taskMsg.setHasAccept(null);
					if (taskMsg.getTaskValid()!=null&&taskMsg.getTaskValid() == 2){
						taskMsg.setTaskValid(null);
					}
					taskMsg.setRejectReason(null);
					taskMsg.setDestTask(null);
				}
				taskMsg.setWhyTransfer(jsonObject.getString("whyTransfer"));
				taskMsg.setOwnArea(areaId);
				this.saveOrUpdateM5(taskMsg, dbOperate, user.getUserId(),modelSeriesId);
			}
		}
		arr.add(flag);
		return arr;
	}

	public void deleteM5HeBing(String deleteId, ComUser user,ComModelSeries comModelSeries)
			throws BusinessException {
		TaskMsg taskMsg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class,
				deleteId);
		if (taskMsg != null
				&& ComacConstants.YES.toString()
						.equals(taskMsg.getAnyContent1())) {
			this.taskMsgBo.deleteTaskMsgById(taskMsg.getTaskId());
		}
		List<TaskMsg> list = this.searchM5HeBing(taskMsg.getSourceAnaId());
		for (TaskMsg taskMsg2 : list) {
			taskMsg2.setTaskValid(null);
			this.update(taskMsg2, user.getUserId());
			addMrbAndMpd(taskMsg2, user.getUserId());
		}
		//删除msg-3任务对应的协调单
		comCoordinationBo.deleteCoordination(deleteId,  user.getUserId(),comModelSeries.getModelSeriesId());
	}
	
	public void addMrbAndMpd(TaskMsg taskMsg, String userId) {
		TaskMpd taskMpd = new TaskMpd();
		TaskMrb taskMrb = new TaskMrb();
		if (taskMsg.getMrbId() == null) {
			taskMrb.setMrbCode(taskMsg.getTaskCode() + "-01");
			taskMrb.setAnyContent(taskMsg.getAnyContent1());
			taskMrb.setComModelSeries(taskMsg.getComModelSeries());
			taskMrb.setSourceSystem(taskMsg.getSourceSystem());
			taskMrb.setSourceAnaId(taskMsg.getSourceAnaId());
			taskMrb.setTaskType(taskMsg.getTaskType());
			taskMrb.setReachWay(taskMsg.getReachWay());
			taskMrb.setTaskIntervalOriginal(taskMsg.getTaskInterval());
			taskMrb.setOwnArea(taskMsg.getOwnArea());
			taskMrb.setEffectiveness(taskMsg.getEffectiveness());
			taskMrb.setValidFlag(taskMsg.getValidFlag());
			taskMrb.setFailureCauseType(taskMsg.getAnyContent2());// 故障影响类别
			this.save(taskMrb, userId);
			taskMsg.setMrbId(taskMrb.getMrbId());
		}
		if (taskMsg.getMpdId() == null) {
			taskMpd.setMpdCode(taskMsg.getTaskCode() + "-01-01");
			taskMpd.setComModelSeries(taskMsg.getComModelSeries());
			taskMpd.setSourceSystem(taskMsg.getSourceSystem());
			taskMpd.setTaskType(taskMsg.getTaskType());
			taskMpd.setReachWay(taskMsg.getReachWay());
			taskMpd.setTaskIntervalOriginal(taskMsg.getTaskInterval());
			taskMpd.setOwnArea(taskMsg.getOwnArea());
			taskMpd.setEffectiveness(taskMsg.getEffectiveness());
			taskMpd.setValidFlag(taskMsg.getValidFlag());
			taskMpd.setFailureCauseType(taskMsg.getAnyContent2());// 故障影响类别
			this.save(taskMpd, userId);
			taskMsg.setMpdId(taskMpd.getMpdId());
		}
	}
	
	@Override
	public List<TaskMsg> searchM5HeBing(String msiId){
		return this.m5Dao.searchM5HeBing(msiId);
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

	public IM5Dao getM5Dao() {
		return m5Dao;
	}

	public void setM5Dao(IM5Dao dao) {
		m5Dao = dao;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}
	
}
