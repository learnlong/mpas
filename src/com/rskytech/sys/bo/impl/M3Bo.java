package com.rskytech.sys.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.M4;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM3Dao;

@SuppressWarnings( { "unchecked" })
public class M3Bo extends BaseBO implements IM3Bo {
	private ITaskMsgBo taskMsgBo;
	private IM4Bo m4Bo;
	private IM3Dao m3Dao;
	private IComAreaBo comAreaBo;
	private IM13Bo m13Bo;
	private IMStepBo mstepBo;
	private IComCoordinationBo comCoordinationBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	
	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public M3 getM3ByM13cId(String id) throws BusinessException {
		List<M3> list = this.m3Dao.getM3ByM13cId(id);
		if(list.size()>0){
			return list.get(0);
		}
			return null;
	}

	/**
	 * 根据m3Id和任务ID查询唯一一条附加任务记录
	 * 
	 * @param m13cId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public M3Additional getM3AdditionalByM3Id(String id, String taskId)
			throws BusinessException {
		List<M3Additional> list = m3Dao.getM3AdditionalByM3Id(id, taskId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 删除任务
	 */
	public ArrayList<String> deleteMoreTaskByMsiId(String msiId, String userId,
			String modelSeriesId) throws BusinessException {
		List<TaskMsg> listTask = this.taskMsgBo.getTaskMsgListByMainId(
				modelSeriesId, msiId,ComacConstants.SYSTEM_CODE,"M3");
		List<M3> listM3 = this.getM3ListByMsiId(msiId);
		ArrayList<String> arr = new ArrayList<String>();
		TaskMsg task;
		for (TaskMsg taskMsg : listTask) {
			int has = 0;
			String taskId = taskMsg.getTaskId();
			for (M3 m3 : listM3) {
				if (taskId.equals(m3.getZongheTaskId())
						|| taskId.equals(m3.getBaoyangTaskId())
						|| taskId.equals(m3.getJiankongTaskId())
						|| taskId.equals(m3.getJianyanTaskId())
						|| taskId.equals(m3.getJianchaTaskId())
						|| taskId.equals(m3.getBaofeiTaskId())
						|| taskId.equals(m3.getChaixiuTaskId())) {
					// 当前任务是问题1-7所产生的任务
					has = 1;
					break;
				}
				Set<M3Additional> set = m3.getM3Additionals();
				for (M3Additional additional : set) {
					// 当前任务是附加任务
					if (taskId.equals(additional.getAddTaskId())) {
						has = 1;
						break;
					}
				}
				if (has == 1) {
					// 当前任务是有效任务
					break;
				}
			}
			if (has == 0) {
				// 当前任务是无效任务
				M4 m4 = this.m4Bo.getM4ByTaskId(taskId);
				if (m4 != null) {
					// 删除任务的M4分析
					this.delete(m4, userId);
				}
				task = (TaskMsg) this.loadById(TaskMsg.class,taskId);
				if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
					arr.add(task.getOwnArea());
				}
				this.taskMsgBo.deleteTaskMsgById(taskId);
			}

		}
		return arr;

	}

	/**
	 * 根据msiId查询M3
	 * 
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public List<M3> getM3ListByMsiId(String msiId) throws BusinessException {
		return m3Dao.getM3ListByMsiId(msiId);
	}

	public ITaskMsgBo getTaskMsgBO() {
		return taskMsgBo;
	}

	public void setTaskMsgBO(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IM4Bo getM4Bo() {
		return m4Bo;
	}

	public void setM4Bo(IM4Bo bo) {
		m4Bo = bo;
	}

	@SuppressWarnings("rawtypes")
	public List getVendorCountByMsi(String msiId) throws BusinessException {
		return m3Dao.getVendorCountByMsi(msiId);
	}

	public IM3Dao getM3Dao() {
		return m3Dao;
	}

	public void setM3Dao(IM3Dao dao) {
		m3Dao = dao;
	}

	/**
	 * 保存m3数据
	 * 
	 * @param user
	 *            用户
	 * @param pageId
	 *            操作页面
	 * @param systemLhirfCode
	 * @param jsonData
	 * @param msiId
	 * @param m13cId
	 */
	@Override
	public ArrayList<String> saveM3(ComUser user, String pageId, String sourceSystem,
			String jsonData, String msiId, String m13cId, String jsonTask,ComModelSeries comModelSeries) {
		this.saveComLogOperate(user, pageId, sourceSystem);
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		// db操作区分
		String dbOperate = "";
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			M3 m3 = this.getM3ByM13cId(m13cId);
			if (m3 != null) {
				// 修改操作
				dbOperate = ComacConstants.DB_UPDATE;
			} else {
				// 添加操作
				m3 = new M3();
				dbOperate = ComacConstants.DB_INSERT;
				m3.setMMain(new MMain());
				m3.getMMain().setMsiId(msiId);
				m3.setM13C(new M13C());
				m3.getM13C().setM13cId(m13cId);
			}
			if (BasicTypeUtils.isNumberString(jsonObject
					.getString("baoyang"))) {
				m3.setBaoyang(jsonObject.getInt("baoyang"));
			}
			m3.setBaoyangDesc(jsonObject.getString("baoyangDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject
					.getString("jianyan"))) {
				m3.setJianyan(jsonObject.getInt("jianyan"));
			}
			m3.setJianyanDesc(jsonObject
							.getString("jianyanDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject.getString("jiankong"))) {
				m3.setJiankong(jsonObject.getInt("jiankong"));
			}
			m3.setJiankongDesc(jsonObject.getString("jiankongDesc"));
 			if (BasicTypeUtils.isNumberString(jsonObject.getString("chaixiu"))) {
				m3.setChaixiu(jsonObject.getInt("chaixiu"));
			}
			m3.setChaixiuDesc(jsonObject.getString("chaixiuDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject.getString("Baofei"))) {
				m3.setBaofei(jsonObject.getInt("Baofei"));
			}
			m3.setBaofeiDesc(jsonObject.getString("BaofeiDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject
					.getString("zonghe"))) {
				m3.setZonghe(jsonObject.getInt("zonghe"));
			}
			m3.setZongheDesc(jsonObject.getString("zongheDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject
					.getString("jiancha"))) {
				m3.setJiancha(jsonObject.getInt("jiancha"));
			}
			m3.setJianchaDesc(jsonObject.getString("jianchaDesc"));
			if (BasicTypeUtils.isNumberString(jsonObject
					.getString("gaijin"))) {
				m3.setGaijin(jsonObject.getInt("gaijin"));
			}
			m3.setRemark(jsonObject.getString("remark"));
			m3.setBaoyangTaskId(null);
			m3.setJianyanTaskId(null);
			m3.setJiankongTaskId(null);
			m3.setJianchaTaskId(null);
			m3.setChaixiuTaskId(null);
			m3.setBaofeiTaskId(null);
			m3.setZongheTaskId(null);
			this.saveOrUpdate(m3, dbOperate, user.getUserId());
			
			Set<M3Additional> set = m3.getM3Additionals();
			if(set.size()>0){
				for (M3Additional additional : set) {
					this.delete(additional);
				}
			}
			
			arr.addAll(saveTask(m3, user, jsonTask, msiId, comModelSeries));
			// 修改步骤
		
			updataMStep(user,msiId, comModelSeries);
		}
		return arr;
	}

	/**
	 * 保存故障原因分析中的任务
	 * 
	 * @param m3
	 * @author chendexu createdate 2012-08-25
	 */
private void updataMStep(ComUser user,String msiId,ComModelSeries comModelSeries){
	String modelSeriesId = comModelSeries.getModelSeriesId();
	MStep mstep = mstepBo.getMStepByMsiId(msiId);
	Integer tsakSize = this.taskMsgBo.getTaskMsgListByMainId(
			modelSeriesId, msiId,
			ComacConstants.SYSTEM_CODE,"M3")
			.size();
	Integer m4Size = this.m4Bo.getListM4ByMsiId(msiId).size();
	if (this.m13Bo.getM13cListByMsiIdNoidNoisRef(msiId).size() == this
			.getM3ListByMsiId(msiId).size()) {
		// 故障影响都已分析
		mstep.setM3(ComacConstants.STEP_FINISH);
		if (ComacConstants.STEP_FINISH.equals(mstep.getM2())) {
			if (tsakSize > m4Size) {
				mstep.setM4(ComacConstants.STEP_NOW);
				mstep.setM5(ComacConstants.STEP_NO);
				mstep.getMMain().setStatus(
						ComacConstants.ANALYZE_STATUS_MAINTAIN);
			} else if(tsakSize==0){
				mstep.setM4(ComacConstants.STEP_INVALID);
				mstep.setM5(ComacConstants.STEP_INVALID);
				mstep.getMMain().setStatus(
						ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			}else {
				mstep.setM4(ComacConstants.STEP_FINISH);
				mstep.setM5(ComacConstants.STEP_FINISH);
				mstep.getMMain().setStatus(
						ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			}
		}
	} else {
		mstep.setM3(ComacConstants.STEP_NOW);
		mstep.getMMain().setStatus(
				ComacConstants.ANALYZE_STATUS_MAINTAIN);
	}
	this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
			.getUserId());
}
	public ArrayList<String> saveTask(M3 m3, ComUser user, String jsonTask, String msiId,ComModelSeries comModelSeries) {
		JSONArray jsonArrayTask = JSONArray.fromObject(jsonTask);
		JSONObject jsonObjectTask = null;
		// db操作区分
		String dbOperate = ComacConstants.DB_UPDATE;
		String modelSeriesId =comModelSeries.getModelSeriesId();
		ArrayList<String> arr = new ArrayList<String>();
		String areaId="";
		String nowAreaId="";
		String taskCode="";
		TaskMsg taskMsg;
		for (int i = 0; i < jsonArrayTask.size(); i++) {
			jsonObjectTask = jsonArrayTask.getJSONObject(i);
			taskCode = jsonObjectTask.getString("taskCode");
			if (taskCode.length() >= 2 && "#&".equals(taskCode.substring(0, 2))) {
				// 修改操作
				String id = taskCode.substring(2);
				taskMsg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class, id);
				if(BasicTypeUtils.isNullorBlank(jsonObjectTask.getString("zoneTransfer"))){
					comCoordinationBo.deleteCoordination(id,user.getUserId(), comModelSeries.getModelSeriesId());
					taskMsg.setNeedTransfer(null);
				}else{
					comCoordinationBo.modifyCoordination(id,user.getUserId(),
							jsonObjectTask.getString("zoneTransfer"),comModelSeries.getModelSeriesId());
				}
				
			} else {
				taskMsg = this.taskMsgBo.getTaskByTaskCode(modelSeriesId,taskCode);
				if (taskMsg == null) {
					// 添加的任务编号没有被使用
					taskMsg = new TaskMsg();
					dbOperate = ComacConstants.DB_INSERT;
					taskMsg.setSourceSystem(ComacConstants.SYSTEM_CODE);
					taskMsg.setSourceAnaId(msiId);
					taskMsg.setSourceStep("M3");
					taskMsg.setComModelSeries(comModelSeries);
					taskMsg.setTaskCode(jsonObjectTask.getString("taskCode"));
				} 
			}

			taskMsg.setTaskType(jsonObjectTask.getString("taskType"));
			taskMsg.setTaskDesc(jsonObjectTask.getString("taskDesc"));
			taskMsg.setReachWay(jsonObjectTask.getString("reachWay"));
			taskMsg.setEffectiveness(jsonObjectTask.getString("effectiveness"));
			taskMsg.setAnyContent4(jsonObjectTask.getString("whichPro"));
			nowAreaId = this.comAreaBo.getAreaIdByAreaCode(jsonObjectTask.getString("zoneTransfer"), modelSeriesId);
			if(dbOperate.equals(ComacConstants.DB_UPDATE)){
				areaId = taskMsg.getOwnArea();
				if(taskMsg.getNeedTransfer()!=null&&taskMsg.getNeedTransfer()==1){
					if(areaId!=null){
						if(!areaId.equals(nowAreaId)){
							arr.add(nowAreaId);
							arr.add(areaId);
						}
					}else{
						arr.add(nowAreaId);
					}
				}
				
			}
			taskMsg.setOwnArea(nowAreaId);
			taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);
			if (ComacConstants.YES.equals(taskMsg.getNeedTransfer())) {
				this.taskMsgDetailBo .addTaskMsgDetail(taskMsg, user.getUserId());
			}
			this.taskMsgBo.saveOrUpdate(taskMsg, dbOperate, user.getUserId());
			whichProConfirm(jsonObjectTask.getString("whichPro"), m3, taskMsg.getTaskId(), user);
		}
		this.update(m3, user.getUserId());
		arr.addAll(delMoreTask(user, msiId, comModelSeries));
		return arr;
	}

	/**
	 * 建立任务与问题间的关系
	 * 
	 * @param whichPro
	 * @param m3
	 * @param taskId
	 * @author chendexu createdate 2012-08-25
	 */
	private void whichProConfirm(String whichPro, M3 m3, String taskId,
			ComUser user) {
		if ("1".equals(whichPro)) {
			//1保养是适用和有效的吗？
			m3.setBaoyangTaskId(taskId);
		} else if ("2".equals(whichPro)) {
			//2状态检验是适用和有效的吗？
			m3.setJianyanTaskId(taskId);
		} else if ("3".equals(whichPro)) {
			//3用正常的操作人员监控来探测功能恶化是适用和有效的吗？
			m3.setJiankongTaskId(taskId);
		} else if ("4".equals(whichPro)) {
			//4用原位或离位检查来探测功能恶化是适用和有效的吗？
			m3.setJianchaTaskId(taskId);
		} else if ("5".equals(whichPro)) {
			//5定时拆修是适用和有效的吗？
			m3.setChaixiuTaskId(taskId);
		} else if ("6".equals(whichPro)) {
			//6定时报废是适用的吗？
			m3.setBaofeiTaskId(taskId);
		} else if ("7".equals(whichPro)) {
			//7有一种工作或综合工作是适用和有效的吗？
			m3.setZongheTaskId(taskId);
		}else{
			// 附加任务
			M3Additional additional = this.getM3AdditionalByM3Id(m3.getM3Id(),
					taskId);
			if (additional == null) {
				additional = new M3Additional();
				additional.setM3(m3);
				additional.setAddTaskId(taskId);
				this.saveOrUpdate(additional, ComacConstants.DB_INSERT, user
						.getUserId());
			}
		}
	}

	/**
	 * 删除多余的任务
	 * 
	 * @author chendexu createdate 2012-08-25
	 */
	private ArrayList<String> delMoreTask(ComUser user, String msiId,ComModelSeries comModelSeries) {
		String modelSeriesId = comModelSeries.getModelSeriesId();
		ArrayList<String> arr = new ArrayList<String>();
		arr.addAll(this.deleteMoreTaskByMsiId(msiId, user.getUserId(), modelSeriesId));
		updataMStep(user,msiId, comModelSeries);
		return arr;
	}

	/**
	 * 删除任务表
	 * 
	 */
	@Override
	public ArrayList<String> deleteTask(String m13cId, String select, String effectResult,
			String taskId, ComUser user, String msiId,ComModelSeries comModelSeries) {
		M3 m3 = this.getM3ByM13cId(m13cId);
		ArrayList<String> arr = new ArrayList<String>();
		if ("1".equals(effectResult)) {
			//1保养是适用和有效的吗？
			m3.setBaoyang(Integer.valueOf(select));
			m3.setBaoyangTaskId(null);
		} else if ("2".equals(effectResult)) {
			//2状态检验是适用和有效的吗？
			m3.setJianyan(Integer.valueOf(select));
			m3.setJianyanTaskId(null);
		} else if ("3".equals(effectResult)) {
			//3用正常的操作人员监控来探测功能恶化是适用和有效的吗？
			m3.setJiankong(Integer.valueOf(select));
			m3.setJiankongTaskId(null);
		} else if ("4".equals(effectResult)) {
			//4用原位或离位检查来探测功能恶化是适用和有效的吗？
			m3.setJiancha(Integer.valueOf(select));
			m3.setJianchaTaskId(null);
		} else if ("5".equals(effectResult)) {
			//5定时拆修是适用和有效的吗？
			m3.setChaixiu(Integer.valueOf(select));
			m3.setChaixiuTaskId(null);
		} else if ("6".equals(effectResult)) {
			//6定时报废是适用的吗？
			m3.setBaofei(Integer.valueOf(select));
			m3.setBaofeiTaskId(null);
		} else if ("7".equals(effectResult)) {
			//7有一种工作或综合工作是适用和有效的吗？
			m3.setZonghe(Integer.valueOf(select));
			m3.setZongheTaskId(null);
		} else{
			// 附加任务
			this.delete(this.getM3AdditionalByM3Id(m3.getM3Id(), taskId));
			//删除msg-3任务对应的协调单
			comCoordinationBo.deleteCoordination(taskId, user.getUserId(), comModelSeries.getModelSeriesId());
		}
		this.update(m3, user.getUserId());
		arr.addAll(delMoreTask(user, msiId, comModelSeries));
		return arr;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo m13Bo) {
		this.m13Bo = m13Bo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	
}
