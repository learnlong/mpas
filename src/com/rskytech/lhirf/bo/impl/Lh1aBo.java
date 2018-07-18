package com.rskytech.lhirf.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh1aBo;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILh5Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh1aDao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

public class Lh1aBo extends BaseBO implements ILh1aBo {
	private ILhStepBo lhStepBo;
	private ILh4Bo  lh4Bo;
	private ILh5Bo lh5Bo;
	private ITaskMsgBo taskMsgBo;
	private ILhMainBo lhMainBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private ILh1aDao lh1aDao;

	@Override
	public Lh1a getLh1aByHsiId(String hsiId) throws BusinessException {
		List<Lh1a> list = this.lh1aDao.getLh1aListByHsiId(hsiId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void doSaveLh1AandRef(String hsiId,Lh1a lh1a,String dbOperate,ComUser user,String modelSeriesId) throws BusinessException {
		this.saveComLogOperate(user, "LH1A", ComacConstants.LHIRF_CODE);
		LhMain myLHhsi = (LhMain) this.loadById(LhMain.class, hsiId);
		LhMain superLhHsi = lhMainBo.getLhMainByHsiCode(myLHhsi.getRefHsiCode(),modelSeriesId);
		////
		Integer isNeedLhTask = -1;
		///
		if (superLhHsi != null) {
			List<TaskMsg> superMsgLists = taskMsgBo.getTaskMsgListByMainId(
					modelSeriesId, superLhHsi.getHsiId(), ComacConstants.LHIRF_CODE,
					ComacConstants.LHIRF_LH5);
			Lh4 refLh4 = lh4Bo.getLh4BylhHsId(superLhHsi.getHsiId());
			if (refLh4 != null) {
				isNeedLhTask = refLh4.getNeedLhTask();
			}
			if (isNeedLhTask == 1) { // 如果所参见的 已经需要LH5 此时LH1A  hsi 正在分析
				if(superMsgLists.size() == 0){ //所参见的hsi没有产生 msg_3任务, 则 lh1a分析完成, lh5分析完成  hsi分析完成
					myLHhsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
				}else{
					if(!ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(myLHhsi.getStatus())){
						myLHhsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					}
				}
			} else {
				myLHhsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
				
			}
		
			if (refLh4 != null) {//当参见的HSI不需要LH5步骤  本Lh_hsi 只有 LH1A  Lh6步骤是   lh1a保存判断Lh_main状态是否做变更
				if(!ComacConstants.YES.equals(refLh4.getNeedLhTask()) || (ComacConstants.YES.equals(refLh4.getNeedLhTask()) && superMsgLists.size() == 0)){
					this.isChangeStatus(hsiId,modelSeriesId);
				}
			}
		}
		this.save(myLHhsi);
		this.saveOrUpdate(lh1a, dbOperate, user.getUserId());
		this.updateStep(hsiId, user,superLhHsi,modelSeriesId);
		this.saveRefHSI(hsiId, user,modelSeriesId);
	}
	//变更  LH_MAIN 状态  
	private void isChangeStatus(String hsiId, String modelSeriesId){
		LhMain lhHsilh4 = (LhMain) this.loadById(LhMain.class, hsiId);
		if(!(lhHsilh4.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAIN) || 
				lhHsilh4.getStatus().equals(ComacConstants.ANALYZE_STATUS_NEW) )){
			  lhHsilh4.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			  this.update(lhHsilh4);
		}
	}
	//步骤的变化
	private void updateStep(String lhHsiId,ComUser currUser,LhMain superLhHsi, String modelSeriesId){
    	LhStep lhStep = lhStepBo.getLhStepBylhHsId(lhHsiId);
		if (!ComacConstants.STEP_INVALID.equals(lhStep.getLh5())
				|| lhStep.getLh5().equals(ComacConstants.STEP_NO)) {  ///有参见的LH5
			lhStep.setLh1a(ComacConstants.STEP_FINISH);
			//有参见的lh5步骤
			// 2看参见的主 hsi 有没有产生  msg_3任务  
			//3  如果没有产生任务  则 本步骤 lh1a分析完成, lh5  分析完成   hsi分析完成
			///LhMain superLhHsi = lhHsiBo.getLhHsiByHsiCode2(lhHsi.getRefHsiCode(),lhHsi.getLhMain().getLhAnaId());
			List<TaskMsg> superMsgList = taskMsgBo.getTaskMsgListByMainId(
					modelSeriesId,superLhHsi.getHsiId(), ComacConstants.LHIRF_CODE,
					ComacConstants.LHIRF_LH5);
			if(superMsgList.size() == 0){
				lhStep.setLh5(ComacConstants.STEP_FINISH);
			}else{
				if(!ComacConstants.STEP_FINISH.equals(lhStep.getLh5())){
					lhStep.setLh5(ComacConstants.STEP_NOW);
				}
			}
			///
			
		} else if (lhStep.getLh5().equals(ComacConstants.STEP_INVALID)) {// 如果参照的没有lh5直接分析完成
			lhStep.setLh1a(ComacConstants.STEP_FINISH);
			lhStep.setLh6(ComacConstants.STEP_FINISH);
		}
    }
	/*
	 * 保存 Lh1a参见的 hsi内容
	 */
	  private void saveRefHSI(String hsiId,  ComUser user,String modelSeriesId) {
		LhMain lhMain = (LhMain)loadById(LhMain.class, hsiId);
		Lh4 myselfLh4 = lh4Bo.getLh4BylhHsId(lhMain.getHsiId());
		Lh5 myselfLh5 = lh5Bo.getLh5ByHsiId(lhMain.getHsiId()); // 自己的LH5
		LhMain superLhHsi =lhMainBo.getLhMainByHsiCode(lhMain.getRefHsiCode(),modelSeriesId);
		try {
			if (myselfLh4 == null) { // 保存LH4
				if (superLhHsi != null) {
					Lh4 superLh4 = lh4Bo.getLh4BylhHsId(superLhHsi.getHsiId());
					if (superLh4 != null) {
						Lh4 newLh4 = superLh4.clone();
						newLh4.setLh4Id(null);
						newLh4.setLhMain(lhMain);
						this.save(newLh4, user.getUserId()); // /复制 LH4
					}
				}
			}
			if (myselfLh5 == null) { // 保存 LH5
				Lh5 superLh5 = lh5Bo.getLh5ByHsiId(superLhHsi.getHsiId());
				if (superLh5 != null) {
					Lh5 newLh5 = superLh5.clone();
					newLh5.setLh5Id(null);
					newLh5.setLhMain(lhMain);
					this.save(newLh5, user.getUserId()); // 复制LH5
				}
			}
			List<TaskMsg> superMsgList = taskMsgBo.getTaskMsgListByMainId(
					modelSeriesId,  superLhHsi.getHsiId(), ComacConstants.LHIRF_CODE,
					ComacConstants.LHIRF_LH5);
			if (superMsgList.size() > 0) {
				// 自己的 任务是
				List<TaskMsg> mYmsgList = taskMsgBo.getTaskMsgListByMainId(
						modelSeriesId,hsiId, ComacConstants.LHIRF_CODE,
						ComacConstants.LHIRF_LH5);

				for (TaskMsg taskMsg : superMsgList) {
					Integer isSame = -1;
					for (TaskMsg mytaskMsg : mYmsgList) {
						if (taskMsg.getTaskType().equals(
								mytaskMsg.getTaskType())) {
							isSame = 1;
						}
					}
					if (!isSame.equals(1)) {
						TaskMsg newMsg = taskMsg.clone();
						newMsg.setTaskMsgDetails(null);
						newMsg.setTaskId(null);
						newMsg.setTaskCode("");//参见同时 copyhsi所产生的任务
						newMsg.setSourceAnaId(lhMain.getHsiId());
						this.save(newMsg, user.getUserId()); // 复制MSG
						List<TaskMsgDetail> superTaskDetailList = taskMsgDetailBo
								.getListDetailTaskBytaskId(taskMsg.getTaskId());
						TaskMsgDetail superTaskDetail=null;
						if (superTaskDetailList != null&&superTaskDetailList.size()>0) { // 复制 taskMsgDetail 表的内容
							superTaskDetail=superTaskDetailList.get(0);
						};
						if (superTaskDetail != null) { // 复制 taskMsgDetail 表的内容
							superTaskDetail=superTaskDetailList.get(0);
							TaskMsgDetail newTaskMsgDetail = new TaskMsgDetail();
							newTaskMsgDetail.setWhereTransfer(superTaskDetail
									.getWhereTransfer());
							newTaskMsgDetail.setValidFlag(superTaskDetail
									.getValidFlag());
							newTaskMsgDetail.setComTaskDetId(null);
							newTaskMsgDetail.setTaskMsg(newMsg);
							newTaskMsgDetail.setHasAccept(null);
							newTaskMsgDetail.setRejectReason(null);
							this.save(newTaskMsgDetail, user.getUserId());
						}
					}
				}
			}

		} catch (CloneNotSupportedException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void lh1aStepshow(String hsiId,ComUser user,String modelSeriesId)throws BusinessException {
		LhMain lhMain =(LhMain) this.loadById(LhMain.class, hsiId);
		List<LhStep> list = lh1aDao.getLh5stepByCode(lhMain.getRefHsiCode(),modelSeriesId);
		LhStep lhstept = null;
		if(list.size()>0){
			lhstept = list.get(0);
		}
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (lhStep != null) { //本操作时为了 如果hsi在第一次没参见其他HSi情况 下  第一进入Lh1步骤中  这时步骤表中已经有数据了,  
			//后期 依然还在 新建状态下 hsi 修改成  有参见的 HSi 在进入LH1a页面之前要把之前 lh1步骤表给删除
			if (ComacConstants.STEP_NOW.equals(lhStep.getLh1())) {
				lhStepBo.delete(lhStep);
			}
		}
		LhStep lhStep2 = lhStepBo.getLhStepBylhHsId(hsiId);
		if (null == lhStep2) {//本HSI初次分析,lhstep表还未创建时
			lhStep2 = new LhStep();
			lhStep2.setLh1a(ComacConstants.STEP_NOW);
			lhStep2.setLh1(ComacConstants.STEP_INVALID);
			lhStep2.setLh2(ComacConstants.STEP_INVALID);
			lhStep2.setLh3(ComacConstants.STEP_INVALID);
			lhStep2.setLh4(ComacConstants.STEP_INVALID);
			lhStep2.setLh6(ComacConstants.STEP_FINISH);
			lhStep2.setLhMain(lhMain);
			if (lhstept != null) {//参见的HSI已经分析了,(参见的HSI的步骤表LhStep表的数据已经存在了时)
				if (lhstept.getLh5().equals(ComacConstants.STEP_INVALID)) {
					lhStep2.setLh5(ComacConstants.STEP_INVALID); //lh1a 的第五步骤 跟随者所参见的 是有效 才存在
				} else {
					lhStep2.setLh5(ComacConstants.STEP_NO);
				}
			} else { //参见的HSI还未分析( 参见的HSI的步骤表LHstep数据还未存在时)
				lhStep2.setLh5(ComacConstants.STEP_INVALID);
			}
		} else {///本HSI 已经开始了分析,(lhstep表数据已经开始创建了)
			if (lhstept != null) {//参见的HSI已经分析了,(参见的HSI的步骤表LhStep表的数据已经存在了时)
				if (lhstept.getLh5().equals(ComacConstants.STEP_INVALID)) { ///此时不需要 LH5 分析了  状态也会发生变化
					lhStep2.setLh5(ComacConstants.STEP_INVALID);
					///
				} else {
					if (!lhStep2.getLh5().equals(ComacConstants.STEP_FINISH)) {
						if (lhStep.getLh1a().equals(ComacConstants.STEP_FINISH)) {
							lhStep2.setLh5(ComacConstants.STEP_NOW);
						} else {
							lhStep2.setLh5(ComacConstants.STEP_NO);
						}

					}

				}
			} else {//参见的HSI还未分析( 参见的HSI的步骤表LHstep数据还未存在时)
				lhStep2.setLh5(ComacConstants.STEP_INVALID);
			}
		}
		lhStepBo.saveOrUpdate(lhStep2, ComacConstants.DB_INSERT, user
				.getUserId());

	}

	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh4Bo getLh4Bo() {
		return lh4Bo;
	}

	public void setLh4Bo(ILh4Bo lh4Bo) {
		this.lh4Bo = lh4Bo;
	}

	public ILh5Bo getLh5Bo() {
		return lh5Bo;
	}

	public void setLh5Bo(ILh5Bo lh5Bo) {
		this.lh5Bo = lh5Bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public ILh1aDao getLh1aDao() {
		return lh1aDao;
	}

	public void setLh1aDao(ILh1aDao lh1aDao) {
		this.lh1aDao = lh1aDao;
	}
}
