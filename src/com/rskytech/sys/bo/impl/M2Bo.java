package com.rskytech.sys.bo.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MReferAfm;
import com.rskytech.pojo.MReferMmel;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM2Dao;

@SuppressWarnings("unchecked")
public class M2Bo extends BaseBO implements IM2Bo {
	private IMStepBo mstepBo;
	private IM13Bo m13Bo;
	private IM3Bo m3Bo;
	private ITaskMsgBo taskMsgBo;
	private IM4Bo m4Bo;
	private IM2Dao m2Dao;
	private IM5Bo m5Bo;

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo m13Bo) {
		this.m13Bo = m13Bo;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	public IM3Bo getM3Bo() {
		return m3Bo;
	}

	public void setM3Bo(IM3Bo bo) {
		m3Bo = bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IM4Bo getM4Bo() {
		return m4Bo;
	}

	public void setM4Bo(IM4Bo bo) {
		m4Bo = bo;
	}

	@Override
	public M2 getM2ByM13fId(String m13fId) throws BusinessException {
		List<M2> list = m2Dao.getM2ByM13fId(m13fId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void deleteAfm(String id, String userId) throws BusinessException {
		List<MReferAfm> list =m2Dao.searchAfm(id);
		if(list.size()>0){
			for(MReferAfm referAfm : list){
				this.delete(referAfm, userId);
			}
		}
	}

	@Override
	public void deleteMmel(String id, String userId) throws BusinessException {
		List<MReferMmel> list =m2Dao.searchMmel(id);
		if(list.size()>0){
			for(MReferMmel mmel : list){
				this.delete(mmel, userId);
			}
		}
	}

	@Override
	public List<M2> getM2ListByMsiId(String msiId) throws BusinessException {
		return m2Dao.getM2ListByMsiId(msiId);
	}

	@Override
	public ArrayList<String> saveM2(ComUser user, String pageId, String sourceSystem,
			String jsonData, String msiId, String m13fId, Integer isSaveAfm,
			String afmJsonData, Integer isSaveMmel, String mmelJsonData,ComModelSeries comModelSeries) {
		this.saveComLogOperate(user, pageId, sourceSystem);
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		// db操作区分
		String dbOperate = "";
		Integer failureCauseType = null;
		String modelSeriesId = comModelSeries.getModelSeriesId();
		MStep mstep = mstepBo.getMStepByMsiId(msiId);
		ArrayList<String> arr = new ArrayList<String>();
		if (BasicTypeUtils.isNumberString(jsonObject
				.getString("failureCauseType"))) {// 当故障影响类别更改时
			failureCauseType = jsonObject.getInt("failureCauseType");
		}
		M2 m2 = this.getM2ByM13fId(m13fId);
		if (m2 != null) {
			// 修改操作
			dbOperate = ComacConstants.DB_UPDATE;
			if (!m2.getFailureCauseType().equals(failureCauseType)) {
				arr.addAll(delM3(m2, msiId, user.getUserId(), modelSeriesId));
				if (ComacConstants.STEP_FINISH.equals(mstep.getM3())) {
					mstep.setM3(ComacConstants.STEP_NOW);
				}
				mstep.setM4(ComacConstants.STEP_NO);
				mstep.setM5(ComacConstants.STEP_NO);
				mstep.getMMain().setStatus(
						ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}
		} else {
			// 添加操作
			m2 = new M2();
			dbOperate = ComacConstants.DB_INSERT;
			m2.setM13F(new M13F());
			m2.setMMain(new MMain());
			m2.getMMain().setMsiId(msiId);
			m2.getM13F().setM13fId(m13fId);
			// 修改步骤状态

		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("q1"))) {
			m2.setQ1(jsonObject.getInt("q1"));
		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("q2"))) {
			m2.setQ2(jsonObject.getInt("q2"));
		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("q3"))) {
			m2.setQ3(jsonObject.getInt("q3"));
		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("q4"))) {
			m2.setQ4(jsonObject.getInt("q4"));
		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("q7"))) {
			m2.setQ7(jsonObject.getInt("q7"));
		}
		m2.setQ1Desc(jsonObject.getString("q1Desc"));
		m2.setQ2Desc(jsonObject.getString("q2Desc"));
		m2.setQ3Desc(jsonObject.getString("q3Desc"));
		m2.setQ4Desc(jsonObject.getString("q4Desc"));
		m2.setQ7Desc(jsonObject.getString("q7Desc"));
		if (BasicTypeUtils.isNumberString(jsonObject
				.getString("failureCauseType"))) {
			m2.setFailureCauseType(jsonObject.getInt("failureCauseType"));
		}
		m2.setMmel(jsonObject.getString("mmel"));
		m2.setRemark(jsonObject.getString("remark"));
		if (BasicTypeUtils.isNumberString(jsonObject.getString("isRefAfm"))) {
			m2.setIsRefAfm(jsonObject.getInt("isRefAfm"));
		}
		if (BasicTypeUtils.isNumberString(jsonObject.getString("isRefMmel"))) {
			m2.setIsRefMmel(jsonObject.getInt("isRefMmel"));
		}
		this.saveOrUpdate(m2, dbOperate, user.getUserId());

		if (this.m13Bo.getM13fListByMsiId(msiId).size() == getM2ListByMsiId(
				msiId).size()) {
			// 故障影响都已分析
			mstep.setM2(ComacConstants.STEP_FINISH);
			Integer tsakSize = this.taskMsgBo.getTaskMsgListByMainId(
					modelSeriesId, msiId,ComacConstants.SYSTEM_CODE, "M3").size();
			Integer m4Size = this.m4Bo.getListM4ByMsiId(msiId).size();
			if (ComacConstants.STEP_FINISH.equals(mstep.getM3())) {
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
				}else{
					mstep.setM4(ComacConstants.STEP_FINISH);
					mstep.setM5(ComacConstants.STEP_FINISH);
				}
			}
		} else {
			mstep.setM2(ComacConstants.STEP_NOW);
		}
		if (ComacConstants.STEP_NO.equals(mstep.getM3())) {
			mstep.setM3(ComacConstants.STEP_NOW);
		}
		this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
				.getUserId());
		if (m2.getIsRefAfm() == ComacConstants.YES) {
			saveAfm(m2, isSaveAfm, afmJsonData, user);
		} else {
			deleteAfm(m2.getM2Id(), user.getUserId());
		}
		if (m2.getIsRefMmel() == ComacConstants.YES) {
			saveMmel(m2, isSaveMmel, mmelJsonData, user);
		} else {
			deleteMmel(m2.getM2Id(), user.getUserId());
		}
		return arr;
	}

	@Override
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries) {
		String modelSeriesId = comModelSeries.getModelSeriesId();

		MStep mstep = mstepBo.getMStepByMsiId(msiId);
		Integer m2Length = getM2ListByMsiId(msiId).size();
		Integer m13fLength=0;
		List<M13F> m13fList = this.m13Bo.getM13fListByMsiId(msiId);
		if(m13fList!=null&&m13fList.size()>0){
			Set<M13C> setM13c;
			for(M13F m13f: m13fList){
				setM13c = m13f.getM13Cs();
				if(setM13c!=null&&setM13c.size()==1){
					M13C m13c = setM13c.iterator().next();
					if(m13c.getIsRef()!=null&&m13c.getIsRef()==1){
						continue;
					}
				}
				m13fLength++;
			}
		}
		if (m13fLength == m2Length && m2Length > 0) {
			// 故障影响都已分析
			mstep.setM2(ComacConstants.STEP_FINISH);
		}
		Integer m13Size = m13Bo.getM13cListByMsiIdNoidNoisRef(msiId).size();
		Integer m3Size = m3Bo.getM3ListByMsiId(msiId).size();
		if (m13Size > m3Size) {
			mstep.setM3(ComacConstants.STEP_NOW);
			mstep.getMMain().setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		} else if (ComacConstants.STEP_FINISH.equals(mstep.getM2())) {
			// m2已分析完成
			mstep.setM3(ComacConstants.STEP_FINISH);
		}
		Integer taskSize = this.taskMsgBo.getTaskMsgListByMainId(modelSeriesId,
				msiId, ComacConstants.SYSTEM_CODE,"M3").size();
		Integer m4Size = this.m4Bo.getListM4ByMsiId(msiId).size();
		if (ComacConstants.STEP_FINISH.equals(mstep.getM2())
				&& ComacConstants.STEP_FINISH.equals(mstep.getM3())) {
			if (taskSize > m4Size) {
				mstep.setM4(ComacConstants.STEP_NOW);
				mstep.setM5(ComacConstants.STEP_NO);
				mstep.getMMain().setStatus(
						ComacConstants.ANALYZE_STATUS_MAINTAIN);
			} else if(taskSize==0){
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
		} else {
			mstep.setM4(ComacConstants.STEP_NO);
			mstep.setM5(ComacConstants.STEP_NO);
			mstep.getMMain().setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		}
		this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
				.getUserId());

	}
	
	/**
	 * 修改故障影响类别后重置M3的分析
	 * 
	 * @param m2
	 */
	private ArrayList<String> delM3(M2 m2, String msiId, String userId, String modelSeriesId) {
		Set<M13C> m13CSet = m2.getM13F().getM13Cs();
		List list=null;
		ArrayList<String> arr = new ArrayList<String>();
		for (M13C m13c : m13CSet) {
			M3 m3 = m3Bo.getM3ByM13cId(m13c.getM13cId());
			if (m3 != null) {
				TaskMsg task;
				if(m3.getZongheTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getZongheTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getZongheTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getZongheTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getZongheTaskId());
					}
				}
				if(m3.getBaoyangTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getBaoyangTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getBaoyangTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getBaoyangTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getBaoyangTaskId());
					}
				}
				if(m3.getJiankongTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getJiankongTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getJiankongTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getJiankongTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getJiankongTaskId());
					}
				}
				if(m3.getJianyanTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getJianyanTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getJianyanTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getJianyanTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getJianyanTaskId());
					}
				}
				if(m3.getJianchaTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getJianchaTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getJianchaTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getJianchaTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getJianchaTaskId());
					}
				}
				if(m3.getBaofeiTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getBaofeiTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getBaofeiTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getBaofeiTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getBaofeiTaskId());
					}
				}
				if(m3.getChaixiuTaskId()!=null){
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(m3.getChaixiuTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, m3.getChaixiuTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(m3.getChaixiuTaskId());
						taskMsgBo.deleteTaskMsgById(m3.getChaixiuTaskId());
					}
				}
				Set<M3Additional> set = m3.getM3Additionals();
				for (M3Additional additional : set) {
					list=m5Bo.getCauseCodeAndCauseTypeByTaskId(additional.getAddTaskId());
					if(list!=null&&list.size()==1){
						task = (TaskMsg) this.loadById(TaskMsg.class, additional.getAddTaskId());
						if(task.getNeedTransfer()!=null&&task.getNeedTransfer()==1){
							arr.add(task.getOwnArea());
						}
						this.m4Bo.deleteM4ByTaskId(additional.getAddTaskId());
						taskMsgBo.deleteTaskMsgById(additional.getAddTaskId());
					}
				}
				this.delete(m3);
			}
		}
		return arr;

	}
	
	/**
	 * 保存参考afm数据
	 * 
	 * @param m2
	 * @author chendexu createdate 2012-08-23
	 */
	private void saveAfm(M2 m2, Integer isSaveAfm, String afmJsonData,
			ComUser user) {
	
			// 修改或添加了参考AFM
			JSONArray jsonArray = JSONArray.fromObject(afmJsonData);
			JSONObject jsonObject = null;
			// db操作区分
			String dbOperate = "";
			MReferAfm afm = null;
			Set<MReferAfm> set = m2.getMReferAfms();
			for (MReferAfm afm1 : set) {
				afm = afm1;
			}
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				if (afm != null) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;

				} else {
					// 添加操作
					afm = new MReferAfm();
					dbOperate = ComacConstants.DB_INSERT;
					afm.setM2(m2);
				}
				afm.setRefAfm(jsonObject.getString("refAfm"));
				afm.setReviewResult(jsonObject.getString("reviewResult"));
				if (!BasicTypeUtils.isNullorBlank(jsonObject
						.getString("reviewDate"))) {
					try {
						Date date = BasicTypeUtils
								.getCurrentDateforSQL(jsonObject
										.getString("reviewDate"));
						afm.setReviewDate(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				afm.setRemark(jsonObject.getString("remark"));
				saveOrUpdate(afm, dbOperate, user.getUserId());
			}
		

	}

	private void saveMmel(M2 m2, Integer isSaveMmel, String mmelJsonData,
			ComUser user) {
			// 修改或添加了参考MMEl
			JSONArray jsonArray = JSONArray.fromObject(mmelJsonData);
			JSONObject jsonObject = null;
			// db操作区分
			String dbOperate = "";
			MReferMmel mmel = null;
			Set<MReferMmel> set = m2.getMReferMmels();
			for (MReferMmel referMmel : set) {
				mmel = referMmel;
			}
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				if (mmel != null) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
				} else {
					// 添加操作
					mmel = new MReferMmel();
					dbOperate = ComacConstants.DB_INSERT;
					mmel.setM2(m2);
				}
				if (BasicTypeUtils.isNumberString(jsonObject
						.getString("isRefPmmel"))) {
					mmel.setIsRefPmmel(jsonObject.getInt("isRefPmmel"));
				}
				mmel.setReviewResult(jsonObject.getString("reviewResult"));
				Date date;
				if (!BasicTypeUtils.isNullorBlank(jsonObject
						.getString("reviewDate"))) {
					try {
						date = BasicTypeUtils.getCurrentDateforSQL(jsonObject
								.getString("reviewDate"));
						mmel.setReviewDate(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				mmel.setRemark(jsonObject.getString("remark"));
				if (!BasicTypeUtils.isNullorBlank(jsonObject
						.getString("pmmelId"))) {
					mmel.setPmmelId(jsonObject.getString("pmmelId"));
				}
				saveOrUpdate(mmel, dbOperate, user.getUserId());
			}
	}

	public IM2Dao getM2Dao() {
		return m2Dao;
	}

	public void setM2Dao(IM2Dao m2Dao) {
		this.m2Dao = m2Dao;
	}

	public IM5Bo getM5Bo() {
		return m5Bo;
	}

	public void setM5Bo(IM5Bo m5Bo) {
		this.m5Bo = m5Bo;
	}
	
}
