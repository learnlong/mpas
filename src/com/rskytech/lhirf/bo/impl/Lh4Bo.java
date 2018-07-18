package com.rskytech.lhirf.bo.impl;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILh5Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh4Dao;
import com.rskytech.lhirf.dao.ILhMainDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.dao.ITaskMsgDao;

public class Lh4Bo extends BaseBO implements ILh4Bo {
	
	private ILhMainDao lhMainDao;
	private ILhStepBo lhStepBo;
	private ITaskMsgBo taskMsgBo;
	private ITaskMsgDao taskMsgDao;
	private ILh5Bo lh5Bo;
	private ILh4Dao lh4Dao;
	@Override
	public Lh4 getLh4BylhHsId(String hsiId) throws BusinessException {
		List<Lh4> list = this.lh4Dao.getLh4ListBylhHsId(hsiId);
		if (list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public ArrayList<String> doSaveLh4andRef(Lh4 tlh4, String hsiId, String dbOperate,
			ComUser user, Integer needLhTask, Integer oldneedLhTask,
			boolean isSafeChange, String[] eDArr, String[] aDArr,ComModelSeries comModelSeries)
			throws BusinessException {
		Integer deleteLH5MsgFalg = ComacConstants.NO;
		this.saveComLogOperate(user, "LH4", ComacConstants.LHIRF_CODE);
		LhMain lhMainlh = (LhMain) this.loadById(LhMain.class, hsiId);
		// 如果没有lh5 设置 分析完成
		LhStep lhstepLh4 = lhStepBo.getLhStepBylhHsId(hsiId);
		ArrayList<String> arr = new ArrayList<String>();
		if (ComacConstants.NO.equals(needLhTask)) {
			lhMainlh.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		} else { // /需要第五步骤
			if (!ComacConstants.STEP_FINISH.equals(lhstepLh4.getLh5())) {
				lhMainlh.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				if (ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(lhMainlh
						.getStatus())) {// /变更状态
					lhMainlh.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
			}
		}
		tlh4.setLhMain(lhMainlh);
		this.saveOrUpdate(tlh4, dbOperate, user.getUserId());
	
		Lh5 lh5 = lh5Bo.getLh5ByHsiId(hsiId);
		LhMain parentLhHsi = (LhMain)loadById(LhMain.class, hsiId);
		List<TaskMsg> parentLhTask = taskMsgBo.getTaskMsgListByMainId(comModelSeries.getModelSeriesId(), hsiId,
				ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
		if (ComacConstants.YES.equals(oldneedLhTask)&& ComacConstants.NO.equals(needLhTask)) {// 当从保存 1 变更为 0 时
			if (lh5 != null) {
				this.delete(lh5, user.getUserId());// 删除自己的LH5 表
			}
			// :删除自己 lh5; 删除taskMSG
			if (parentLhTask.size() > 0) {
				for (TaskMsg doDeltaskMsg : parentLhTask) { // 先删除
					arr.addAll(this.deleteTaskMsg(doDeltaskMsg.getTaskId()));
				}
			}
			deleteLH5MsgFalg = ComacConstants.YES ;//此时 子类参见此HSI    h5和MSG任务一并删除
		}
		if(ComacConstants.YES.equals(needLhTask) && isSafeChange){ //当 lh4-->是需要维修任务  同时  是否  安全有变更时  删除任务
			// :删除自己 lh5; 删除taskMSG
			if (parentLhTask.size() > 0) {
				for (TaskMsg doDeltaskMsg : parentLhTask) { // 先删除
					arr.addAll(this.deleteTaskMsg(doDeltaskMsg.getTaskId()));
				}
			}
			if (lh5 != null) {
				if (ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(parentLhHsi.getStatus())) {
					parentLhHsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
				this.delete(lh5, user.getUserId());// 删除自己的LH5 表
				LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
				lhStep.setLh5(ComacConstants.STEP_NOW);
				this.saveOrUpdate(lhStep, ComacConstants.DB_UPDATE, user.getUserId());
			}
			deleteLH5MsgFalg =  ComacConstants.NO;//此时 子类参见此HSI    h5和MSG任务一并删除
		}
		arr.addAll(this.saveRef(tlh4, hsiId, deleteLH5MsgFalg, eDArr, aDArr, user,comModelSeries));///保存参见的  相关数据
		this.isChangeStatus(lhMainlh, needLhTask,isSafeChange, user.getUserId(), comModelSeries.getModelSeriesId());
		this.updateStep(tlh4.getLhMain().getHsiId(), needLhTask, user
				.getUserId());
		return arr;
	}

	 
		// 设置MAin 下的LH_hsi 所有的状态
	private void isChangeStatus(LhMain lhHsilh4, Integer needLhTask,boolean isSafeChange, String userId,String modelSeriesId) {
		List<LhMain> listLhHsichild = lhMainDao.getLhMainByRefHsiCode(lhHsilh4.getHsiCode(),modelSeriesId);
		if (listLhHsichild.size() > 0) {
			for (LhMain myslefChild : listLhHsichild) {
				LhStep childlhStep = lhStepBo.getLhStepBylhHsId(myslefChild.getHsiId());
				if (ComacConstants.NO.equals(needLhTask)) { // 不需要分析LH5
					if (childlhStep != null) {// /子类已经分析时
						if (ComacConstants.STEP_FINISH.equals(childlhStep.getLh1a())) {
							myslefChild.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
						}
					}

				} 
				if(ComacConstants.YES.equals(needLhTask)){ // 需要分析 LH5 有可能是从 0 ---> 1 更改到
					if(ComacConstants.YES.equals(needLhTask) && isSafeChange){ //当 lh4-->是需要维修任务  同时  是否  安全有变更时  LH5如果分析完成, 则变正在分析
						if (childlhStep != null&&ComacConstants.STEP_FINISH.equals(childlhStep.getLh5())) {
							childlhStep.setLh5(ComacConstants.STEP_NOW);
							myslefChild.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
						}
					}
					if(childlhStep != null&&ComacConstants.STEP_FINISH.equals(childlhStep.getLh1a())){
						myslefChild.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					}
				}
				this.update(myslefChild, userId);
				///
			}
		}
		if(!ComacConstants.YES.equals(needLhTask)){
			//当没有LH5步骤时  在此LH4循环判断本LH_MAIN表下的所有HSI  变更其状态
			if (!(ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(lhHsilh4.getStatus())
				 || ComacConstants.ANALYZE_STATUS_NEW.equals(lhHsilh4.getStatus()))) {
				lhHsilh4.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			}
			if(ComacConstants.YES.equals(needLhTask) && isSafeChange){//当 lh4-->是需要维修任务  同时  是否  安全有变更时  LH5如果分析完成, 则变正在分析
				if(ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(lhHsilh4.getStatus())){
					lhHsilh4.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				}
			}
		}
		this.update(lhHsilh4);
	}
		
		
		//保存 相似性
private ArrayList<String> saveRef(Lh4 tlh4, String hsiId, Integer deleteLH5MsgFalg,
			String[] eDArr, String[] aDArr, ComUser currentUser, ComModelSeries comModelSeries) {
		LhMain parentLhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		ArrayList<String> arr = new ArrayList<String>();
		List<LhMain> listLhHsichild = lhMainDao.getLhMainByRefHsiCode(parentLhHsi.getHsiCode(),comModelSeries.getModelSeriesId());
		if (listLhHsichild.size() > 0) {
				for (LhMain lhHsicopyto : listLhHsichild) {
					Lh4 reflh4 = this.getLh4BylhHsId(lhHsicopyto.getHsiId());
					if (reflh4 != null) {
						reflh4.setLhMain(lhHsicopyto);
						for (int i = 0; i < eDArr.length; i++) {
							BasicTypeUtils.setEntityObjValue(reflh4,
									"select" + (i + 1), BasicTypeUtils
											.parseInt(eDArr[i]));
						}
						for (int i = 0; i < aDArr.length; i++) {
							BasicTypeUtils.setEntityObjValue(reflh4,
									"select" + (eDArr.length + i + 1),
									BasicTypeUtils.parseInt(aDArr[i]));
						}
						reflh4.setNeedLhTask(tlh4.getNeedLhTask());
						reflh4.setRemark(tlh4.getRemark());
						reflh4.setResult(tlh4.getResult());
						reflh4.setIsSafe(tlh4.getIsSafe());
						reflh4.setSafeReason(tlh4.getSafeReason());
						this.update(reflh4, currentUser.getUserId());
					}
				}
				if(!ComacConstants.NO.equals(deleteLH5MsgFalg)){
					//删除 lh5 表, 删除 taskMSG-3  子类
					for (LhMain myslefChild : listLhHsichild) {
						Lh5 reflh5 = lh5Bo.getLh5ByHsiId(myslefChild.getHsiId());
						List<TaskMsg> childTaskList = taskMsgBo.getTaskMsgListByMainId(comModelSeries.getModelSeriesId(),
										myslefChild.getHsiId(),ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
						if (reflh5 != null) {
							this.delete(reflh5);
						}
						if (childTaskList.size() > 0) {
							for (TaskMsg reftaskMsg : childTaskList) {
								arr.addAll(this.deleteTaskMsg(reftaskMsg.getTaskId()));
							}

						}

					}
				}
			}
		return arr;
	}

	//当删除一个 task_MSG会导致 删除 MRB MPD
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
		///步骤跳转变化
 private void updateStep(String hsiId, Integer needLhTask, String userId) {
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (ComacConstants.YES.equals(needLhTask)) {
			if (!ComacConstants.STEP_FINISH.equals(lhStep.getLh5())) {// /第五步还没有分析时
				lhStep.setLh4(ComacConstants.STEP_FINISH);
				lhStep.setLh5(ComacConstants.STEP_NOW);
			}
		}
		if (ComacConstants.NO.equals(needLhTask)) {
			lhStep.setLh4(ComacConstants.STEP_FINISH);
			lhStep.setLh5(ComacConstants.STEP_INVALID);
			lhStep.setLh6(ComacConstants.STEP_FINISH);
		}
		this.saveOrUpdate(lhStep, ComacConstants.DB_UPDATE, userId);
	}

public ILhMainDao getLhMainDao() {
	return lhMainDao;
}

public void setLhMainDao(ILhMainDao lhMainDao) {
	this.lhMainDao = lhMainDao;
}

public ILhStepBo getLhStepBo() {
	return lhStepBo;
}

public void setLhStepBo(ILhStepBo lhStepBo) {
	this.lhStepBo = lhStepBo;
}

public ITaskMsgBo getTaskMsgBo() {
	return taskMsgBo;
}

public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
	this.taskMsgBo = taskMsgBo;
}

public ILh5Bo getLh5Bo() {
	return lh5Bo;
}

public void setLh5Bo(ILh5Bo lh5Bo) {
	this.lh5Bo = lh5Bo;
}

public ILh4Dao getLh4Dao() {
	return lh4Dao;
}

public void setLh4Dao(ILh4Dao lh4Dao) {
	this.lh4Dao = lh4Dao;
}

public ITaskMsgDao getTaskMsgDao() {
	return taskMsgDao;
}

public void setTaskMsgDao(ITaskMsgDao taskMsgDao) {
	this.taskMsgDao = taskMsgDao;
}
 
}
