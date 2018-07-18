package com.rskytech.lhirf.bo.impl;

import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh3Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh3Dao;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.Lh3;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Lh3Bo extends BaseBO implements ILh3Bo {
	private ILhStepBo lhStepBo;
	private ICusEdrAdrBo cusEdrAdrBo;
	private ILh3Dao lh3Dao;
	private IDefineBaseCrackLenBo  defineBaseCrackLenBo;
	
	
	@Override
	public List<Lh3> getLh3ListByHsiId(String hsiId, Page page)
			throws BusinessException {
		return 	this.lh3Dao.getLh3ListByHsiId(hsiId, page);
	}

	@Override
	public void saveLh3andStep(ComUser user, String hsiId, String jsonData,ComModelSeries comModelSeries)
			throws BusinessException {
		this.saveComLogOperate(user, "LH3", ComacConstants.LHIRF_CODE);
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = "";
		Lh3 lh33 = null;
		 	for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("lh3Id");
				lh33 = new Lh3();
				// 修改操作时
				if (!BasicTypeUtils.isNullorBlank(id) && !"0".equals(id)){
					dbOperate = ComacConstants.DB_UPDATE;
					lh33 =  (Lh3) this.loadById(Lh3.class, id);
				}else{
					dbOperate =ComacConstants.DB_INSERT;
					lh33.setLhMain((LhMain) this.loadById(LhMain.class, hsiId));
				}
				lh33.setDefectModel(jsonObject.getString("defectModel"));
				lh33.setDefectDesc(jsonObject.getString("defectDesc"));
				this.saveOrUpdate(lh33, dbOperate, user.getUserId());
			}
		 	this.updateStep(hsiId, user, comModelSeries);
		
	}

	/*
	 * LH3 在保存时分析步骤的状态变化
	 */
	   private void updateStep(String hsiId ,ComUser user,ComModelSeries comModelSeries){
	    LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
		String modelSeriesId = comModelSeries.getModelSeriesId();
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, ComacConstants.LH4);
		CusDisplay lh4showremark = defineBaseCrackLenBo.getLH4CusDisplay(modelSeriesId);
		Integer isFull = -1;
		if (cusEdrAdrList.size() == 0
				|| cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.VALIDFLAG_NO
				|| lh4showremark == null) {
			isFull = 0;
		}
		if (lhStep.getLh4().equals(ComacConstants.STEP_NO)) {
			lhStep.setLh3(ComacConstants.STEP_FINISH);
			if (!isFull.equals(0)) {
				lhStep.setLh4(ComacConstants.STEP_NOW);
			}
			this.lhStepBo.saveOrUpdate(lhStep, ComacConstants.DB_UPDATE, user
					.getUserId());
		}
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

	public ILh3Dao getLh3Dao() {
		return lh3Dao;
	}

	public void setLh3Dao(ILh3Dao lh3Dao) {
		this.lh3Dao = lh3Dao;
	}

	@Override
	public List<Lh3> getLh3lListByHsiIdNoPage(String hsiId)
			throws BusinessException {
		return this.lh3Dao.getLh3ListByHsiIdNoPage(hsiId);
	}

}
