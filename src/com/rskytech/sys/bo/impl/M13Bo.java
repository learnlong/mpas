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
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM13Dao;

public class M13Bo extends BaseBO implements IM13Bo {
	private IMStepBo mstepBo;
	private IM2Bo m2Bo;
	private IM3Bo m3Bo;
	private ITaskMsgBo taskMsgBo;
	private IM4Bo m4Bo;
	private IM13Dao m13Dao;
	private IM5Bo m5Bo;


	public IM4Bo getM4Bo() {
		return m4Bo;
	}

	public void setM4Bo(IM4Bo bo) {
		m4Bo = bo;
	}

	public IM3Bo getM3Bo() {
		return m3Bo;
	}

	public void setM3Bo(IM3Bo bo) {
		m3Bo = bo;
	}

	public IM2Bo getM2Bo() {
		return m2Bo;
	}

	public void setM2Bo(IM2Bo m2Bo) {
		this.m2Bo = m2Bo;
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	/**
	 * 根据msiId查询M13
	 * 
	 * @param msiId
	 * @return
	 */
	public List<M13> getM13ListByMsiId(String msiId) throws BusinessException {
		return m13Dao.getM13ListByMsiId(msiId);
	}

	/**
	 * 根据msiId与功能编号查询M13
	 * 
	 * @param msiId
	 * @return
	 * @author chendexu createdate 2012-08-22
	 */
	@Override
	public M13 getM13ByfunctionCode(String msiId, String functionCode)
			throws BusinessException {
		List<M13> list = m13Dao.getM13ByfunctionCode(msiId, functionCode);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	
	public M13F getM13FByEffectCode(String msiId, String effectCode)
			throws BusinessException {
		List<M13F> list = m13Dao.getM13FByEffectCode(msiId, effectCode);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<M13C> getM13cListByM13FId(String m13fId)
			throws BusinessException {
		return m13Dao.getM13cListByM13FId(m13fId);
	}

	@Override
	public List<M13F> getM13fListByM13Id(String m13Id) throws BusinessException {
		return this.m13Dao.getM13fListByM13Id(m13Id);

	}

	/**
	 * 根据m13cId查询MReferMsi
	 */
	public MReferMsi getMReferMsiByM13cId(String m13cId)
			throws BusinessException {
		List<MReferMsi> list = this.m13Dao.getMReferMsiByM13cId(m13cId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	/**
	 * 删除参考MSI
	 */
	@Override
	public void deleteRef(String refId, String m13cId, ComUser user) throws BusinessException {
		M13C m13c = (M13C) this.loadById(M13C.class, m13cId);
		MReferMsi ref=(MReferMsi) this.loadById(MReferMsi.class, refId);
		this.delete(ref, user.getUserId());
		m13c.setIsRef(ComacConstants.NO);
		this.update(m13c, user.getUserId());
	}

	public List<M13F> getM13fListByMsiId(String msiId) throws BusinessException {
		return m13Dao.getM13fListByMsiId(msiId);
	}

	@Override
	public List<M13C> getM13cListByMsiId(String msiId) throws BusinessException {
		return m13Dao.getM13cListByMsiId(msiId);
	}

	@Override
	public List<M13C> getM13cListByMsiIdNoidNoisRef(String msiId)
			throws BusinessException {
		return m13Dao.getM13cListByMsiIdNoidNoisRef(msiId);
	}

	/**
	 * 保存m13的数据
	 * 
	 * @param user
	 *            当前用户
	 * @param sourceSystem
	 *            (系统/area/lhirf/struct)
	 * @param pageId
	 *            操作页面
	 * @param msiId
	 *            cbr_msi msiId
	 * @param jsonData
	 *            字符串对象
	 * @param isSaveLog
	 *            是否保存islog对象
	 * 
	 */
	@Override
	public void saveOrUpdateM13(ComUser user, String sourceSystem,
			String pageId, String msiId, String jsonData, boolean isSaveLog,ComModelSeries comModelSeries) {
		String dbOperate = "";
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = new JSONObject();
		if (isSaveLog) {
			this.saveComLogOperate(user, pageId, sourceSystem);
		}
		// db操作区分
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String functionCode = jsonObject.getString("functionCode");
			if (!BasicTypeUtils.isNullorBlank(functionCode)) {
				// 保存功能
				String m13Id = jsonObject.getString("m13Id");
				M13 m13 = new M13();
				if (!BasicTypeUtils.isNullorBlank(m13Id)) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
					m13 = (M13) this.loadById(M13.class, m13Id);
				} else {
					// 添加操作
					dbOperate = ComacConstants.DB_INSERT;
					m13.setMMain(new MMain());
					m13.getMMain().setMsiId(msiId);
				}
				m13.setFunctionCode(functionCode);
				m13.setFunctionDesc(jsonObject.getString("functionDesc"));
				this.saveOrUpdate(m13, dbOperate, user.getUserId());
			}
			String failureCode = jsonObject.getString("failureCode");
			if (!BasicTypeUtils.isNullorBlank(failureCode)) {
				// 保存故障及影响
				String m13fId = jsonObject.getString("m13fId");
				M13F m13f = new M13F();
				if (!BasicTypeUtils.isNullorBlank(m13fId)) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
					m13f = (M13F) this.loadById(M13F.class, m13fId);
				} else {
					// 添加操作
					dbOperate = ComacConstants.DB_INSERT;
					M13 m13 = this.getM13ByfunctionCode(msiId, failureCode
							.substring(0, failureCode.length() - 1));
					m13f.setM13(m13);
				}
				m13f.setFailureCode(jsonObject.getString("failureCode"));
				m13f.setFailureDesc(jsonObject.getString("failureDesc"));
				m13f.setEffectCode(jsonObject.getString("effectCode"));
				m13f.setEffectDesc(jsonObject.getString("effectDesc"));
				this.saveOrUpdate(m13f, dbOperate, user.getUserId());
			}
			String causeCode = jsonObject.getString("causeCode");
			if (!BasicTypeUtils.isNullorBlank(causeCode)) {
				// 保存故障原因
				String m13cId = jsonObject.getString("m13cId");
				M13C m13c = new M13C();
				if (!BasicTypeUtils.isNullorBlank(m13cId)) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
					m13c = (M13C) this.loadById(M13C.class, m13cId);
				} else {
					// 添加操作
					dbOperate = ComacConstants.DB_INSERT;
					M13F m13f = this.getM13FByEffectCode(msiId, causeCode
							.substring(0, causeCode.length() - 1));
					m13c.setM13F(m13f);
				}
				m13c.setCauseCode(jsonObject.getString("causeCode"));
				m13c.setCauseDesc(jsonObject.getString("causeDesc"));
				m13c.setMsetfId(jsonObject.getString("msetfId"));
				if (!BasicTypeUtils.isNumberString(jsonObject.getString("isRef"))) {
					m13c.setIsRef(ComacConstants.NO);

				}else{
					m13c.setIsRef(jsonObject.getInt("isRef"));
				}
				      
				this.saveOrUpdate(m13c, dbOperate, user.getUserId());
			}
		}
	}

	@Override
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries) {
		String modelSeriesId = comModelSeries.getModelSeriesId();

		MStep mstep = mstepBo.getMStepByMsiId(msiId);
		mstep.setM13(ComacConstants.STEP_FINISH);
		mstep.setM2(ComacConstants.STEP_NOW);
		Integer m13fLength=0;
		List<M13F> m13fList = this.getM13fListByMsiId(msiId);
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
		
		if (m13fLength == 0){//所有故障原因都参考了其他MSI
			mstep.setM13(ComacConstants.STEP_FINISH);
			mstep.setM2(ComacConstants.STEP_FINISH);
			mstep.setM3(ComacConstants.STEP_FINISH);
			mstep.setM4(ComacConstants.STEP_FINISH);
			mstep.setM5(ComacConstants.STEP_FINISH);
			mstep.getMMain().setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		} else {
			Integer m2Length = this.m2Bo.getM2ListByMsiId(msiId).size();
			if (m13fLength == m2Length && m2Length > 0) {
				// 故障影响都已分析
				mstep.setM2(ComacConstants.STEP_FINISH);
			}
			Integer m13Size = getM13cListByMsiIdNoidNoisRef(msiId).size();
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
				}else{
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
		}
		
		this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
				.getUserId());

	}

	/**
	 * 删除m13并且重新生成编号
	 * 
	 * @param deljson
	 *            删除的jsonString字符串
	 * @param user
	 *            当前用户对象
	 * @param sourceSystem
	 *            操作方式(系统/lhirf/area/struct)
	 * @param pageId
	 *            操作页面
	 * @param msiId
	 *            msi 的msiId
	 * @param jsonData
	 *            保存的jsonString字符串
	 */
	@Override
	public ArrayList<String> deleteM13AndSave(String deljson, ComUser user,
			String sourceSystem, String pageId, String msiId, String jsonData,ComModelSeries comModelSeries) {
		JSONArray jsonArray = JSONArray.fromObject(deljson);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		String m13Id = jsonObject.getString("m13Id");
		String m13cId = jsonObject.getString("m13cId");
		String m13fId = jsonObject.getString("m13fId");
		ArrayList<String> delArr = new ArrayList<String>();
		if (!BasicTypeUtils.isNullorBlank(m13Id)) {
			// 如果功能Id不为空
			M13 m13 = (M13) this.loadById(M13.class, m13Id);
			this.delete(m13, user.getUserId());
		}else if (!BasicTypeUtils.isNullorBlank(m13fId)) {
			// 如果功能故障Id不为空
			M13F m13f = (M13F) this.loadById(M13F.class, m13fId);
			this.delete(m13f, user.getUserId());
		} else if (!BasicTypeUtils.isNullorBlank(m13cId)) {
			// 如果功能原因Id不为空
			Object[] obj =null;
			M13C m13C = (M13C) this.loadById(M13C.class, m13cId);
			obj = m13C.getM3s().toArray();
			M3 m3;
			for(Object o : obj){
				m3= (M3) o;
				delArr.addAll(deleteM3Task(m3));
			}
			this.delete(m13C, user.getUserId());
		}
		this.saveOrUpdateM13(user, sourceSystem, pageId, msiId, jsonData,false, comModelSeries);
		return delArr;
	}

	/**
	 * 保存ref操作,并且保存结束调用save方法把编号更新
	 * 
	 * @param jsonData保存json字符串
	 * @param m13cId
	 * @param user
	 *            当前用户
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> saveRef(String jsonData, String m13cId, ComUser user,
			String msiId,ComModelSeries comModelSeries) {
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		ArrayList<String> delArr = new ArrayList<String>();
		M13C m13c = (M13C) this.loadById(M13C.class, m13cId);
		// db操作区分
		String dbOperate = "";
		MReferMsi ref = this.getMReferMsiByM13cId(m13cId);
		if (ref != null) {
			// 修改操作
			dbOperate = ComacConstants.DB_UPDATE;
		} else {
			// 添加操作
			ref = new MReferMsi();
			dbOperate = ComacConstants.DB_INSERT;
			ref.setM13C(new M13C());
			ref.getM13C().setM13cId(m13cId);
			M13F m13f = m13c.getM13F();
			Set<M13C> setM13c = m13f.getM13Cs();
			if(setM13c!=null&&setM13c.size()==1){
				Set<M2> setM2 = m13f.getM2s();
				if(setM2!=null&&setM2.size()>0){
					for(M2 m2 : setM2){
						this.delete(m2);
					}
				}
			}
			m13c.setIsRef(ComacConstants.YES);
			Set<M3> set = m13c.getM3s();
			for (M3 m3 : set) {
				delArr.addAll(deleteM3Task(m3));
				this.delete(m3, user.getUserId());
			}
			this.saveOrUpdate(m13c, ComacConstants.DB_UPDATE, user.getUserId());
		}
		ref.setRefMsiId(jsonObject.getString("refMsiId"));
		ref.setRefFunctionId(jsonObject.getString("refFunctionId"));
		ref.setRefFailureId(jsonObject.getString("refFailureId"));
		ref.setRefEffectId(jsonObject.getString("refEffectId"));
		ref.setRefCauseId(jsonObject.getString("refCauseId"));
		ref.setIsAna(jsonObject.getString("isAna"));
		ref.setRemark(jsonObject.getString("remark"));
		this.saveOrUpdate(ref, dbOperate, user.getUserId());
		return delArr;
	}

	/**
	 * 删除M3产生的任务
	 * @param m3
	 */
	private ArrayList<String> deleteM3Task(M3 m3){
		List list=null;
		ArrayList<String> arr = new ArrayList<String>();
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
		return arr;
	}
	public IM13Dao getM13Dao() {
		return m13Dao;
	}

	public void setM13Dao(IM13Dao m13Dao) {
		this.m13Dao = m13Dao;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IM5Bo getM5Bo() {
		return m5Bo;
	}

	public void setM5Bo(IM5Bo m5Bo) {
		this.m5Bo = m5Bo;
	}

	@Override
	public List<M13C> getM13cByMsetfIdAndmsId(String msetfId, String msiId)
			throws BusinessException {
		return this.m13Dao.getM13cByMsetfIdAndmsId(msetfId, msiId);
	}
	
}
