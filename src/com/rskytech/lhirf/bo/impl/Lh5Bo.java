package com.rskytech.lhirf.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILh1Bo;
import com.rskytech.lhirf.bo.ILh1aBo;
import com.rskytech.lhirf.bo.ILh5Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh5Dao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.task.dao.ITaskMsgDao;

public class Lh5Bo extends BaseBO implements ILh5Bo {
	private ILhMainBo lhMainBo;
	private ITaskMsgBo taskMsgBo;
	private ITaskMsgDao taskMsgDao;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IComAreaBo comAreaBo;
	private ILhStepBo lhStepBo;
	private ILh1Bo lh1Bo;
	private ILh1aBo  lh1aBo;
	private IComCoordinationBo comCoordinationBo;
	private ILh5Dao lh5Dao;
	
	@Override
	public Lh5 getLh5ByHsiId(String hsiId) throws BusinessException {
		List<Lh5> list = this.lh5Dao.getLh5ListByHsiId(hsiId);
		if (list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 保存 LH5 以及Lh5 分析过程中产生的任务 msg-3
	 * 
	 * @param Lhhsi
	 *            表lh_HSI
	 * @param taskmsg
	 *            表taskMsg
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-12-06
	 */
	@Override
	public void saveLh5andMsg(Lh5 lh5, String operateFlgLh5, TaskMsg taskMsg, String operateFlgMsg, String userId) throws BusinessException {
		this.saveOrUpdate(lh5, operateFlgLh5, userId);
		this.saveOrUpdate(taskMsg, operateFlgMsg, userId);
	}
	
	@Override
	public ArrayList<String> doSaveLh5andRef(String hsiId, String needRedesign, String dbOperate, ComUser user, LhMain lhHsilh5,
			Lh5 lh5, String jsonData, String[] arryDeltTaskId, ComModelSeries comModelSeries)throws BusinessException {
		this.saveComLogOperate(user, "LH5", ComacConstants.LHIRF_CODE); // 保存Log日志
		LhMain myLhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		String modelSeriesId = comModelSeries.getModelSeriesId();
		ArrayList<String> arr = new ArrayList<String>();
		if ("1".equals(needRedesign)) { // /当 重新设计是 needRedesign == 1是
			arr.addAll(this.isRedesignDelTask(user, hsiId,modelSeriesId));// 本HSI所有任务删除 //2 子类参见此的
												// msg任务也一同删除
			this.saveOrUpdate(lh5, dbOperate, user.getUserId());
			if (null != myLhHsi.getRefHsiCode()&&!"".equals(myLhHsi.getRefHsiCode())) {// 当本LH_HSI有HSI参见(有子hsi)保存自己数据时,
				this.saveReftaskLh5(lh5, "saverefLH5", user, hsiId,modelSeriesId);
			}
			
		}
		//适用性
		
		else { // / 保存MSG- 3 和LH5
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
			if (jsonArray.size() == 0) { // 页面只有 Lh5 做修改时 只保存 LH5
				this.saveOrUpdate(lh5, dbOperate, user.getUserId());
				if (null != myLhHsi.getRefHsiCode()&&!"".equals(myLhHsi.getRefHsiCode())) {// 当本LH_HSI有HSI参见(有子hsi)保存自己数据时,
					this.saveReftaskLh5(lh5, "saverefLH5", user, hsiId,modelSeriesId);
				}
			}
			if (arryDeltTaskId != null) {
				arr.addAll(this.deleteTask(arryDeltTaskId, user, hsiId, modelSeriesId)); // 删除页面 取消任务
			}
			JSONObject jsonObject = null;
			String dbOperate2 = "";
			TaskMsg taskMsg;
			String comAreaId = "";
			for (int i = 0; i < jsonArray.size(); i++) {
				taskMsg = new TaskMsg();
				jsonObject = jsonArray.getJSONObject(i);// 是否转移任务, 当
														// needTransfer=1 需要转移时
														// 向TaskMsgDetail
														// 表中插入一条信息
				TaskMsg taskMsgcode = null;
				String taskId = jsonObject.getString("taskId");
				String taskCode = jsonObject.getString("taskCode");
				String ownArea = jsonObject.getString("ownArea");
				comAreaId = comAreaBo.getAreaIdByAreaCode(ownArea, modelSeriesId);
				if (!BasicTypeUtils.isNullorBlank(taskId) && !"0".equals(taskId)) {// 修改操作
					dbOperate2 = ComacConstants.DB_UPDATE;
					taskMsg = (TaskMsg) taskMsgBo.loadById(TaskMsg.class, taskId);
				}else { // 追加操作时
					taskMsgcode = this.taskMsgBo.getTaskByTaskCode(modelSeriesId, taskCode);
					if (taskMsgcode == null) {
						dbOperate2 = ComacConstants.DB_INSERT;
						taskMsg.setComModelSeries(comModelSeries);
						taskMsg.setSourceSystem(ComacConstants.LHIRF_CODE);
						taskMsg.setSourceAnaId(hsiId);
						taskMsg.setSourceStep("LH5");
					}
				}
				taskMsg.setEffectiveness(myLhHsi.getEffectiveness() != null ? myLhHsi.getEffectiveness() : "");// 保存任务有效性
				if(dbOperate2.equals(ComacConstants.DB_UPDATE)){
					if(taskMsg.getNeedTransfer()!=null){
						if(taskMsg.getNeedTransfer()!=jsonObject.getInt("needTransfer")){
							if(comAreaId!=null){
								arr.add(comAreaId);
							}
						}
					}else{
						if(jsonObject.getInt("needTransfer")==1){
							if(comAreaId!=null){
								arr.add(comAreaId);
							}
						}
					}
					if(jsonObject.getInt("needTransfer")==1){
						if(taskMsg.getOwnArea()!=null){
							if(!taskMsg.getOwnArea().equals(comAreaId)){
								arr.add(comAreaId);
								arr.add(taskMsg.getOwnArea());
							}
						}else{
							arr.add(comAreaId);
						}
					}
				}else{
					if(jsonObject.getInt("needTransfer")==1){
						arr.add(comAreaId);
					}
				}
				if ("1".equals(jsonObject.getString("needTransfer"))) { // 转移时//保存
																		// 区域ID
					taskMsg.setSysTransfer(ComacConstants.ZONAL_CODE);
					taskMsg.setWhyTransfer(jsonObject.getString("whyTransfer"));
					//如果任务区域和协调单区域不相同，将协调单的接收区域改为msg-3任务区域
					comCoordinationBo.modifyCoordination(taskId, user.getUserId(),ownArea,comModelSeries.getModelSeriesId());
					if (comAreaId != null) {
						taskMsg.setOwnArea(comAreaId);
					}else {
						comAreaId = "noId";
					}
				}else {
					taskMsg.setWhyTransfer(null); // /不是转移任务时 把转移区域和转移原因 置空!
					taskMsg.setSysTransfer(null);// 转移到的系统
					taskMsg.setHasAccept(null);
					if (taskMsg.getTaskValid()!=null&&taskMsg.getTaskValid() == 2){
						taskMsg.setTaskValid(null);
					}
					taskMsg.setRejectReason(null);
					taskMsg.setDestTask(null);
				}
				taskMsg.setAnyContent1(lhHsilh5.getIpvOpvpOpve()); // 和本LH_HSI中
																	// IpvOpvpOpve的值
																	// 一致
				taskMsg.setTaskCode(jsonObject.getString("taskCode"));
				taskMsg.setTaskInterval(jsonObject.getString("taskInterval"));
				taskMsg.setTaskType(jsonObject.getString("taskType"));
				taskMsg.setReachWay(jsonObject.getString("reachWay"));
				taskMsg.setTaskDesc(jsonObject.getString("taskDesc"));
				taskMsg.setValidFlag(ComacConstants.YES);
				taskMsg.setTaskMsgDetails(null);
				taskMsg.setNeedTransfer(BasicTypeUtils.parseInt(jsonObject.getString("needTransfer")));
				if (taskMsgcode == null && !"noId".equals(comAreaId)) {
					// //当所填写的任务编号已经存在 或者 区域编号填写有误 都不可以保存本页面的LH5 和任务信息了
					// **modfiyed[]注意
					this.saveLh5andMsg(lh5, dbOperate, taskMsg, dbOperate2, user.getUserId());
					if (ComacConstants.YES.equals(taskMsg.getNeedTransfer())) {
						taskMsgDetailBo.addTaskMsgDetail(taskMsg, user.getUserId());//保存taskDetail数据
					} else {
						if(dbOperate2.equals(ComacConstants.DB_UPDATE)){
							taskMsgDetailBo.delTaskMsgDetail(taskMsg,user.getUserId());
							//删除msg-3任务对应的协调单
							comCoordinationBo.deleteCoordination(taskMsg.getTaskId(), user.getUserId(),modelSeriesId);
						}
					}
				}
				if (null != myLhHsi.getRefHsiCode() || !"".equals(myLhHsi.getRefHsiCode()) ) {
					// 当本LH_HSI有HSI参见(有子hsi)保存自己数据时, 同时更新参见的hsi数据
					this.saveReftaskLh5(lh5, "saverefLH5", user, hsiId,modelSeriesId);
					this.saveReftaskLh5(lh5, "saverefTask", user, hsiId,modelSeriesId);
				}
				
			}
			
		}
		this.isChangeStatus(hsiId, user, modelSeriesId);// /变更 main状态
		this.updateStep(lh5.getLhMain().getHsiId(), user.getUserId());
		return arr;
	}
	
	/*
	 * //以下是关于相似HSI处理
	 */
	private void saveReftaskLh5(Lh5 lh5, String lh5Omsg, ComUser user, String hsiId, String modelSeriesId) {
		LhMain parentLhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		List<LhMain> listLhHsichild = lhMainBo.getLhMainByRefHsiCode(parentLhHsi.getHsiCode(),modelSeriesId); // 自己的
		List<TaskMsg> superMsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId, parentLhHsi.getHsiId(),
				ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
		Integer msgTypeChange = 0;
		try {
			if (listLhHsichild!=null&&listLhHsichild.size() > 0) {
				for (LhMain myslefChild : listLhHsichild) {
						if ("saverefLH5".equals(lh5Omsg)) {
							Lh5 reflh5 = this.getLh5ByHsiId(myslefChild.getHsiId());
							if (reflh5 == null) { // 保存 LH5
								if (lh5 != null) {
									Lh5 newLh5 = lh5.clone();
									newLh5.setLh5Id(null);
									newLh5.setLhMain(myslefChild);
									this.save(newLh5, user.getUserId()); // 复制LH5
								}
							}
							if (reflh5 != null) {
								reflh5.setDetAvl(lh5.getDetAvl());
								reflh5.setDetDesc(lh5.getDetDesc());
								reflh5.setDisAvl(lh5.getDisAvl());
								reflh5.setDisDesc(lh5.getDisDesc());
								reflh5.setGviAvl(lh5.getGviAvl());
								reflh5.setGviDesc(lh5.getGviDesc());
								reflh5.setFncAvl(lh5.getFncAvl());
								reflh5.setFncDesc(lh5.getFncDesc());
								reflh5.setNeedRedesign(lh5.getNeedRedesign());
								reflh5.setRedesignDesc(lh5.getRedesignDesc());
								this.update(reflh5, user.getUserId()); // /更新参见的 LH5
								// 信息
							}
						}
						if ("saverefTask".equals(lh5Omsg)) {
							if (superMsgList.size() > 0) {
								// 自己的 子类的任务是
								List<TaskMsg> mYmsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId, myslefChild.getHsiId(),
										ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
								List<String> myTaskList = new ArrayList<String>();
								for (TaskMsg taskMsgss : mYmsgList) {
									myTaskList.add(taskMsgss.getTaskType());
								}
								for (TaskMsg mytaskSuperMsg : superMsgList) {
									if (!myTaskList.contains(mytaskSuperMsg.getTaskType())) {
										msgTypeChange++;
									}
								}
								if (msgTypeChange > 0) { // 这个时候 主的 产生新的 msg .
									// 和子类已经保存的
									// msg_3任务类型或者数量有变化时
									// 1lh5 步骤 分析完成 -->正在分析
									// 2 如果子类此时的状态时分析完成 -->变成 正在分析,lh_main的状态 也会改变
									isMsg3ChangeStatus(parentLhHsi, user,modelSeriesId);
								}
								for (TaskMsg taskMsg : superMsgList) {
									Integer isSame = -1;
									for (TaskMsg mytaskMsg : mYmsgList) {
										if (taskMsg.getTaskType().equals(mytaskMsg.getTaskType())) {
											isSame = 1;
										}
										// 子类有 MSG-3参见任务 但还没有维护时 把任务描述, 与接近方式复制给子类任务
										if ((mytaskMsg.getTaskCode() == null) && taskMsg.getTaskType().equals(mytaskMsg.getTaskType())) {
											TaskMsg newMsg11 = mytaskMsg;
											TaskMsg newMsg22 = taskMsg.clone();
											newMsg11.setReachWay(newMsg22.getReachWay());
											newMsg11.setTaskDesc(newMsg22.getTaskDesc());
											newMsg11.setNeedTransfer(newMsg22.getNeedTransfer());
											newMsg11.setOwnArea(newMsg22.getOwnArea());
											this.save(newMsg11, user.getUserId());
										}
									}
									if (!isSame.equals(1)) {
										TaskMsg newMsg = taskMsg.clone();
										newMsg.setTaskMsgDetails(null);
										newMsg.setTaskId(null);
										newMsg.setTaskCode("");
										newMsg.setSourceAnaId(myslefChild.getHsiId());
										this.save(newMsg, user.getUserId()); // 复制MSG
										List<TaskMsgDetail> list = taskMsgDetailBo.getListDetailTaskBytaskId(taskMsg.getTaskId());
										for(TaskMsgDetail superTaskDetail :list) { // 复制
											// taskMsgDetail
											// 表的内容
											TaskMsgDetail newTaskMsgDetail = new TaskMsgDetail();
											newTaskMsgDetail.setWhereTransfer(superTaskDetail.getWhereTransfer());
											newTaskMsgDetail.setValidFlag(superTaskDetail.getValidFlag());
											newTaskMsgDetail.setComTaskDetId(null);
											newTaskMsgDetail.setTaskMsg(newMsg);
											newTaskMsgDetail.setHasAccept(null);
											newTaskMsgDetail.setRejectReason(null);
											this.save(newTaskMsgDetail, user.getUserId());
										}
									}
								}
							}
							
						}
					}
				}
		}
		catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	// /当选择 重新设计 = 1 时, 删除本HSI全部 任务 ,
	// 删除 子类参见此hsi产生的所有任务 .
	private ArrayList<String> isRedesignDelTask(ComUser user, String hsiId, String modelSeriesId) {
		ArrayList<String> arr = new ArrayList<String>();
		LhMain parentLhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		List<TaskMsg> listMsg = taskMsgBo.getTaskMsgListByMainId(modelSeriesId, hsiId,
				ComacConstants.LHIRF_CODE, "LH5");
		List<LhMain> listLhHsichild = lhMainBo.getLhMainByRefHsiCode(parentLhHsi.getHsiCode(),modelSeriesId); // 自己的
		for (TaskMsg taskMsg : listMsg) {
			arr.addAll(this.deleteTaskMsg(taskMsg.getTaskId()));//删除相关任务
			
			if (listLhHsichild.size() > 0) {
				for (LhMain myslefChild : listLhHsichild) {
					// 子类参见本HSI 以下的所有HSI产生的任务
					List<TaskMsg> mYmsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId,  myslefChild.getHsiId(),
							ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
					for (TaskMsg mytaskMsg : mYmsgList) {
						if (taskMsg.getTaskType().equals(mytaskMsg.getTaskType())) {// 如果参见的HSI
							arr.addAll(this.deleteTaskMsg(mytaskMsg.getTaskId()));
						}
						
					}
				}
			}
		}
		return arr;
	}
	
	// 删除MSg -3 任务了
	private ArrayList<String> deleteTask(String[] arryDeltTaskId, ComUser user, String hsiId,String modelSeriesId) {
		ArrayList<String> arr = new ArrayList<String>();
		LhMain parentLhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		List<LhMain> listLhHsichild = lhMainBo.getLhMainByRefHsiCode(parentLhHsi.getHsiCode(),modelSeriesId); // 自己的
		for (int i = 0; i < arryDeltTaskId.length; i++) {
			if (!BasicTypeUtils.isNullorBlank(arryDeltTaskId[i])) {
				TaskMsg delTaskMsg = (TaskMsg) taskMsgBo.loadById(TaskMsg.class, arryDeltTaskId[i]);
				arr.addAll(this.deleteTaskMsg(arryDeltTaskId[i]));
				if (listLhHsichild.size() > 0) {
					for (LhMain myslefChild : listLhHsichild) {
							// 子类参见本HSI 以下的所有HSI产生的任务
							List<TaskMsg> mYmsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId, myslefChild.getHsiId(),
									ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
							for (TaskMsg mytaskMsg : mYmsgList) {
								if (delTaskMsg.getTaskType().equals(mytaskMsg.getTaskType())) {// 如果参见的HSI
									arr.addAll(this.deleteTaskMsg(mytaskMsg.getTaskId()));
								}
								
							}
						}
					}
			}
			
		}
		return arr;
	}
	
	// 当删除一个 task_MSG会导致 删除 MRB MPD
	private ArrayList<String> deleteTaskMsg(String taskId) {
		ArrayList<String> arr = new ArrayList<String>();
		if(!BasicTypeUtils.isNullorBlank(taskId)){
			TaskMsg task = (TaskMsg) this.loadById(TaskMsg.class, taskId);
			if(task.getNeedTransfer()!=null&&task.getOwnArea()!=null&&task.getNeedTransfer()==1){
				arr.add(task.getOwnArea());
			}
			taskMsgDao.deleteTasksByTaskId(taskId);//删除msg-3的关联任务
		}
		return arr;
	}
	
	// /步骤跳转变化
	private void updateStep(String hsiId, String userId) {
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (lhStep.getLh5().equals(ComacConstants.STEP_NOW)) {
			lhStep.setLh5(ComacConstants.STEP_FINISH);
			this.saveOrUpdate(lhStep, ComacConstants.DB_UPDATE, userId);
		}
	}
	
	// 设置MAin 下的LH_hsi 所有的状态
	private void isChangeStatus(String hsiId, ComUser currUser,String modelSeriesId) {
		LhMain lhHsi = (LhMain) lhMainBo.loadById(LhMain.class, hsiId);
		List<TaskMsg> superMsgList = taskMsgBo.getTaskMsgListByMainId(modelSeriesId,
				lhHsi.getHsiId(), ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
		List<LhMain> listLhHsichild = lhMainBo.getLhMainByRefHsiCode(lhHsi.getHsiCode(),modelSeriesId); // 自己的
		int isChange = 0;
		if (listLhHsichild.size() > 0) {
			for (LhMain lhHsiChildstatus : listLhHsichild) {
				LhStep childlhStep = lhStepBo.getLhStepBylhHsId(lhHsiChildstatus.getHsiId());
				if (superMsgList.size() == 0) {// / 1 主 hsi任务 从多个变成 0 状态变化
					if (childlhStep != null) {
						if (ComacConstants.STEP_FINISH.equals(childlhStep.getLh1a())) {
							if (!ComacConstants.STEP_INVALID.equals(childlhStep.getLh5())) {
								childlhStep.setLh5(ComacConstants.STEP_FINISH);
							}
							lhHsiChildstatus.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
						}
					}
					
				}
			}
			
		}
		if (ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(lhHsi.getStatus()) || ComacConstants.ANALYZE_STATUS_NEW.equals(lhHsi.getStatus())) {
			isChange = 1;
		}
		if (!ComacConstants.YES.equals(isChange)) {// 如果Main 下 LH_hsi 分析完 main
													// 就分析完成
			lhHsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		}
		else {
			lhHsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		}
		this.update(lhHsi);
	}
	
	// 当 lh5选择 msg_3任务 多少发生变化时 对状态的影响
	private void isMsg3ChangeStatus(LhMain parentLhHsi, ComUser currentUser,String modelSeriesId) {
		List<LhMain> listLhHsichild = lhMainBo.getLhMainByRefHsiCode(parentLhHsi.getHsiCode(),modelSeriesId);
		if (listLhHsichild.size() > 0) {
			for (LhMain lhHsiChildstatus : listLhHsichild) {
					LhStep childlhStep = lhStepBo.getLhStepBylhHsId(lhHsiChildstatus.getHsiId());
					LhMain saveLhMain = (LhMain) this.loadById(LhMain.class, lhHsiChildstatus.getHsiId());//为了保存操作时找不到pojo
					// 任务不为零, 变多, 变类型 时 有变动
					// 子类如果 分析完成, 则 lh5变成 正在分析,
					// 2 子类的hsi分析 正在分析 lhmain 分析完成 --->正在分析
					if (lhHsiChildstatus.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)) {
						if (ComacConstants.STEP_FINISH.equals(childlhStep.getLh5())) {
							childlhStep.setLh5(ComacConstants.STEP_NOW);
							lhHsiChildstatus.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
							this.update(saveLhMain, currentUser.getUserId());
						}
					}
				}
			}
			
	}

	@Override
	public List<CusInterval> getCusIntervalbyFlg(String anaFlg, String internalFlg,
			String modelSeriesId) {
		return this.lh5Dao.getCusIntervalbyFlg(anaFlg, internalFlg, modelSeriesId);
	}

	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh1Bo getLh1Bo() {
		return lh1Bo;
	}

	public void setLh1Bo(ILh1Bo lh1Bo) {
		this.lh1Bo = lh1Bo;
	}

	public ILh1aBo getLh1aBo() {
		return lh1aBo;
	}

	public void setLh1aBo(ILh1aBo lh1aBo) {
		this.lh1aBo = lh1aBo;
	}

	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public ILh5Dao getLh5Dao() {
		return lh5Dao;
	}

	public void setLh5Dao(ILh5Dao lh5Dao) {
		this.lh5Dao = lh5Dao;
	}

	public ITaskMsgDao getTaskMsgDao() {
		return taskMsgDao;
	}

	public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
		this.taskMsgDao = taskMsgDao;
	}

}
