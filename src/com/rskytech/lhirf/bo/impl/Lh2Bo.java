package com.rskytech.lhirf.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.bo.ILh2Bo;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILh2Dao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh2;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.LhStep;


public class Lh2Bo extends BaseBO implements ILh2Bo {
	private ILhStepBo lhStepBo;
	private ILh2Dao lh2Dao;
	/**
	 * 通过Lhsid 查询lh2 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 */
	@Override
	public Lh2 getLh2ByHsiId(String hsiId) throws BusinessException {
		List<Lh2> list = this.lh2Dao.getLh2ListByHsiId(hsiId);
		if (list!=null&&list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void saveLh2andStep(ComUser user, String hsiId, String env,String picContent)
			throws BusinessException {
		this.saveComLogOperate(user, "LH2", ComacConstants.LHIRF_CODE);
		String dbOperate = "";
		Lh2 lh22 = this.getLh2ByHsiId(hsiId);
		if(lh22 != null){
    		//修改操作
    		dbOperate = ComacConstants.DB_UPDATE;
    	}else { //新数据 添加操作
			dbOperate =ComacConstants.DB_INSERT;
			lh22 = new Lh2();
			LhMain lhHsi= (LhMain) this.loadById(LhMain.class, hsiId);
			lh22.setLhMain(lhHsi);
    	  }	
		lh22.setEnv(env);
		lh22.setPicContent(picContent);
		this.saveOrUpdate(lh22, dbOperate, user.getUserId());
		LhStep lhStep = lhStepBo.getLhStepBylhHsId(hsiId);
	 	   if(lhStep.getLh3().equals(ComacConstants.STEP_NO)){
	 		   lhStep.setLh2(ComacConstants.STEP_FINISH);
	 		   lhStep.setLh3(ComacConstants.STEP_NOW);
	 		   this.lhStepBo.saveOrUpdate(lhStep,ComacConstants.DB_UPDATE, user.getUserId());
	 	   }
		
	}

	public ILhStepBo getLhStepBo() {
		return lhStepBo;
	}

	public void setLhStepBo(ILhStepBo lhStepBo) {
		this.lhStepBo = lhStepBo;
	}

	public ILh2Dao getLh2Dao() {
		return lh2Dao;
	}

	public void setLh2Dao(ILh2Dao lh2Dao) {
		this.lh2Dao = lh2Dao;
	}

}
