package com.rskytech.sys.bo.impl;


import java.util.List;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;

import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MSet;
import com.rskytech.pojo.MSetF;
import com.rskytech.pojo.MStep;

import com.rskytech.sys.bo.IMSetBo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IMSetDao;

public class MSetBo extends BaseBO implements IMSetBo {
	private IMStepBo mstepBo;
	private IMSetDao mSetDao;
	private IComAtaDao comAtaDao;
	
	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}
	
	/**
	 * 根据msiId和ataId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiIdAndAtaId(String msiId,String ataId)
			throws BusinessException{
		return mSetDao.getMsetListByMsiIdAndAtaId(msiId, ataId);
	}

	/**
	 * 根据msiId查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	public List<MSet> getMsetListByMsiId(String msiId) throws BusinessException {
		return mSetDao.getMsetListByMsiId(msiId);
	}

	/**
	 * 根据功能ID查找故障
	 * @param msetId
	 * @return
	 * @throws BusinessException
	 */
	public List<MSetF> getMsetfListByMsetId(String msetId)throws BusinessException{
		return mSetDao.getMsetfListByMsetId(msetId);
	}
	
	
	/**
	 * 根据msiId与功能编号查询MSet
	 * 
	 * @param msiId
	 * @return
	 */
	@Override
	public MSet getMsetByfunctionCode(String msiId, String functionCode)
			throws BusinessException {
		List<MSet> list = mSetDao.getMsetByfunctionCode(msiId, functionCode);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	/**保存mset操作以及com_log_operate
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 * @param msiId com_msi id
	 * @param jsonData 
	 * @param isSaveLog 
	 * @param comModelSeries 
	 */
	@Override
	public void saveOrUpdateMset(ComUser user, String sourceSystem,
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
				String msetId = jsonObject.getString("msetId");
				MSet mSet = new MSet();
				if (!BasicTypeUtils.isNullorBlank(msetId)) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
					mSet = (MSet) this.loadById(MSet.class, msetId);
				} else {
					// 添加操作
					dbOperate = ComacConstants.DB_INSERT;
					mSet.setMMain(new MMain());
					mSet.getMMain().setMsiId(msiId);
					List<ComAta> atas=this.comAtaDao.getComAtaByAtaCode(functionCode.substring(0, 11), comModelSeries.getModelSeriesId());
					mSet.setComAta(atas.get(0));
				}
				if(mSet!=null){
				mSet.setFunctionCode(functionCode);
				mSet.setFunctionDesc(jsonObject.getString("functionDesc"));
				this.saveOrUpdate(mSet, dbOperate, user.getUserId());
				}
			}
			String failureCode = jsonObject.getString("failureCode");
			if (!BasicTypeUtils.isNullorBlank(failureCode)) {
				// 保存故障及影响
				String msetfId = jsonObject.getString("msetfId");
				MSetF mSetf = new MSetF();
				if (!BasicTypeUtils.isNullorBlank(msetfId)) {
					// 修改操作
					dbOperate = ComacConstants.DB_UPDATE;
					mSetf = (MSetF) this.loadById(MSetF.class, msetfId);
				} else {
					// 添加操作
					dbOperate = ComacConstants.DB_INSERT;
					MSet mSet = this.getMsetByfunctionCode(msiId, failureCode
							.substring(0, failureCode.length() - 2));
					mSetf.setMset(mSet);
				}
				if(mSetf!=null){
				mSetf.setFailureCode(jsonObject.getString("failureCode"));
				mSetf.setFailureDesc(jsonObject.getString("failureDesc"));
				this.saveOrUpdate(mSetf, dbOperate, user.getUserId());
				}
			}
		}
	}

	@Override
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries) {
		MStep mstep = mstepBo.getMStepByMsiId(msiId);
		mstep.setMset(ComacConstants.STEP_FINISH);
		if(mstep.getM13().equals(ComacConstants.STEP_NO)){
			mstep.setM13(ComacConstants.STEP_NOW);
		}
		this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
				.getUserId());

	}

	/**
	 * 删除mset并且重新生成编号
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
	public void deleteMsetAndSave(String deljson, ComUser user,
			String sourceSystem, String pageId, String msiId, String jsonData,ComModelSeries comModelSeries) {
		JSONArray jsonArray = JSONArray.fromObject(deljson);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String msetId = jsonObject.getString("msetId");
			String msetfId = jsonObject.getString("msetfId");
			if (!BasicTypeUtils.isNullorBlank(msetId)) {
				// 如果功能Id不为空
				MSet mSet = (MSet) this.loadById(MSet.class, msetId);
				if (mSet!=null)
				this.delete(mSet, user.getUserId());
			}else if (!BasicTypeUtils.isNullorBlank(msetfId)) {
				// 如果功能故障Id不为空
				MSetF mSetF = (MSetF) this.loadById(MSetF.class, msetfId);
				if(mSetF!=null)
				this.delete(mSetF, user.getUserId());
			} 
		}
		this.saveOrUpdateMset(user, sourceSystem, pageId, msiId, jsonData,false, comModelSeries);
	}



	
	/**
	 * 根据该msi下的ATA的条数初始化功能、功能故障条目
	 * @param modelSeriesId
	 * @param msiAtaId
	 * @param msiId
	 * @param userId
	 */
	public void initMset(String modelSeriesId,String msiAtaId,String msiId,String userId){
		MMain mmainTemp=(MMain) this.dao.loadById(MMain.class, msiId);
		List<ComAta> caList = comAtaDao.loadChildAta(modelSeriesId, msiAtaId);
		
		if (caList != null && caList.size() > 0){
			for (ComAta ca : caList){
				String ataId = ca.getAtaId();
				List<MSet> msets=this.mSetDao.getMsetListByMsiIdAndAtaId(msiId, ataId);
				if (msets==null||msets.size()<1) {
					MSet mSet=new MSet();
					mSet.setComAta(ca);
					mSet.setMMain(mmainTemp);
					mSet.setFunctionCode(ca.getAtaCode()+"_"+1);
					this.saveOrUpdate(mSet, ComacConstants.DB_INSERT , userId);
					
					MSetF mSetF=new MSetF();
					mSetF.setMset(mSet);
					mSetF.setFailureCode(ca.getAtaCode()+"_"+1+"AA");
					this.saveOrUpdate(mSetF, ComacConstants.DB_INSERT , userId);
				}
			}
		} else {//如果当前MSI没有下级ATA，为了保证系统能够顺利运行下去，则添加自己本身
			List<MSet> msets=this.mSetDao.getMsetListByMsiIdAndAtaId(msiId, msiAtaId);
			if (msets==null||msets.size()<1) {
				ComAta ca = (ComAta) this.loadById(ComAta.class, msiAtaId);
				
				MSet mSet=new MSet();
				mSet.setComAta(ca);
				mSet.setMMain(mmainTemp);
				mSet.setFunctionCode(ca.getAtaCode()+"_"+1);
				this.saveOrUpdate(mSet, ComacConstants.DB_INSERT , userId);
				
				MSetF mSetF=new MSetF();
				mSetF.setMset(mSet);
				mSetF.setFailureCode(ca.getAtaCode()+"_"+1+"AA");
				this.saveOrUpdate(mSetF, ComacConstants.DB_INSERT , userId);
			}
		}
	}
	

	public IMSetDao getmSetDao() {
		return mSetDao;
	}

	public void setmSetDao(IMSetDao mSetDao) {
		this.mSetDao = mSetDao;
	}

	public IComAtaDao getComAtaDao() {
		return comAtaDao;
	}

	public void setComAtaDao(IComAtaDao comAtaDao) {
		this.comAtaDao = comAtaDao;
	}

}
