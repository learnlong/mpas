package com.rskytech.lhirf.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh1Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh1Dao;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.Lh1;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

public class Lh1Bo extends BaseBO implements ILh1Bo {
	private ILh1Dao lh1Dao;
	private ILhStepBo lhStepBo;
	private ICusEdrAdrBo cusEdrAdrBo;
	private IDefineBaseCrackLenBo  defineBaseCrackLenBo;
	
	/**
	 * 通过Lhsid 查询lh1 表中的数据
	 * 
	 * @param HsiId
	 *            表lh_HSI ID
	 * @return Lhirf 中 lh1对象
	 * @throws BusinessException
	 */
	@Override
	public Lh1 getLh1ByHsiId(String hsiId) throws BusinessException {
		List<Lh1> list = this.lh1Dao.getLh1ByHsiId(hsiId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	
	@Override
	public void saveLh1andStep(ComUser user, String hsiId, String picContent, String lheff) throws BusinessException {
		this.saveComLogOperate(user, "LH1", ComacConstants.LHIRF_CODE);
		LhMain lhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		String dbOperate = "";
		Lh1 lh11 = this.getLh1ByHsiId(hsiId);
		if (lh11 != null) {
			// 修改操作
			dbOperate = ComacConstants.DB_UPDATE;
		}
		else { // 新数据 添加操作
			lh11 = new Lh1();
			dbOperate = ComacConstants.DB_INSERT;
			if ((lhHsi.getStatus()).equals(ComacConstants.ANALYZE_STATUS_NEW)) {
				lhHsi.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
			}
		
		}
		lhHsi.setEffectiveness(lheff);
		lh11.setPicContent(picContent);
		lh11.setLhMain(lhHsi);
		this.saveOrUpdate(lh11, dbOperate, user.getUserId());
		this.updateStep(hsiId, user.getUserId());
	}
	
	private void updateStep(String hsiId, String userId) {
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (lhStep.getLh2().equals(ComacConstants.STEP_NO)) {
			lhStep.setLh1(ComacConstants.STEP_FINISH);
			lhStep.setLh2(ComacConstants.STEP_NOW);
			this.saveOrUpdate(lhStep, ComacConstants.DB_UPDATE, userId);
		}
	}
	
	@Override
	public void lh1Stepshow(String hsiId, ComUser user, ComModelSeries comModelSeries) throws BusinessException {
		LhStep lhStep1 = lhStepBo.getLhStepBylhHsId(hsiId);
		LhMain lhHsi = (LhMain) this.loadById(LhMain.class, hsiId);
		if (lhStep1 != null) {// 本操作时为了 如果hsi在第一次有 参见其他HSi情况 下 第一进入Lh1a步骤中
								// 这时步骤表中已经有数据了,
			// //后期 依然还在 新建状态下 hsi 修改成 没有参见的 HSi 在进入LH1页面之前要把之前 lh1a步骤表给删除
			if (ComacConstants.STEP_NOW.equals(lhStep1.getLh1a())) {
				this.delete(lhStep1);
			}
			// 如果因为自定义数据不完整 lh3 分析完成 / lh4拦截判断自定义不完整 不可进
			// 3.自定义数据录入完成. LH3分析完成, 则Lh4应该是正在分析状态
			if (ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(lhHsi.getStatus())) {
				
				// 1)如果是判断自定义数据录入完整 2),Lh3步骤状态 完成 LH4 未分析状态时 变更 -->LH4 状态 分析完成
				if (this.chackFull(user, comModelSeries) && (ComacConstants.STEP_FINISH.equals(lhStep1.getLh3())) && (ComacConstants.STEP_NO.equals(lhStep1.getLh4()))) {
					lhStep1.setLh4(ComacConstants.STEP_NOW);
					this.save(lhStep1, user.getUserId());
				}
			}
			
		}
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		if (null == lhStep) {
			lhStep = new LhStep();
			lhStep.setLh1a(ComacConstants.STEP_INVALID);
			lhStep.setLh1(ComacConstants.STEP_NOW);
			lhStep.setLh2(ComacConstants.STEP_NO);
			lhStep.setLh3(ComacConstants.STEP_NO);
			lhStep.setLh4(ComacConstants.STEP_NO);
			lhStep.setLh5(ComacConstants.STEP_INVALID); // 初始分析是lh5
			// 页面时不确定有没有
			lhStep.setLh6(ComacConstants.STEP_FINISH);
			lhStep.setLhMain(lhHsi);
			lhStepBo.saveOrUpdate(lhStep, ComacConstants.DB_INSERT, user.getUserId());
		}
		
	}
	
	private boolean chackFull(ComUser user,ComModelSeries comModelSeries) {
		boolean flag = false;
		String modelSeriesId = comModelSeries.getModelSeriesId();
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(modelSeriesId, ComacConstants.LH4);
		CusDisplay lh4showremark = defineBaseCrackLenBo.getLH4CusDisplay(modelSeriesId);
		if (cusEdrAdrList.size() == 0 || cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.VALIDFLAG_NO || lh4showremark == null) {
			flag = false;
		}
		else {
			flag = true;
		}
		return flag;
	}
	

	public ILh1Dao getLh1Dao() {
		return lh1Dao;
	}


	public void setLh1Dao(ILh1Dao lh1Dao) {
		this.lh1Dao = lh1Dao;
	}


	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}


	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}


	public ICusEdrAdrBo getCusEdrAdrBo() {
		return cusEdrAdrBo;
	}


	public void setCusEdrAdrBo(ICusEdrAdrBo cusEdrAdrBo) {
		this.cusEdrAdrBo = cusEdrAdrBo;
	}

	public IDefineBaseCrackLenBo getDefineBaseCrackLenBo() {
		return defineBaseCrackLenBo;
	}

	public void setDefineBaseCrackLenBo(IDefineBaseCrackLenBo defineBaseCrackLenBo) {
		this.defineBaseCrackLenBo = defineBaseCrackLenBo;
	}
	
}
