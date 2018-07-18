package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.ComVendor;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IMStepBo;

public class MstepAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msiId;
	private IMStepBo mstepBo;
	private MStep mstep;
	private IComVendorBo comVendorBo;
	private IComMmelBo comMmelBo;
	public String init() {
		JSONObject json = new JSONObject();
		mstep = mstepBo.getMStepByMsiId(msiId);
		List<ComVendor> venList = comVendorBo.getVendorList(getComModelSeries().getModelSeriesId());
		List<ComMmel> mmelList = this.comMmelBo.getMmelList(getComModelSeries().getModelSeriesId());
		if(mstep.getM12().equals(ComacConstants.STEP_NO)&&
				mstep.getM11().equals(ComacConstants.STEP_FINISH)&&
				venList.size()>0){
			mstep.setM12(ComacConstants.STEP_NOW);
		}
		if(mmelList.size()>0){
			if(mstep.getM2().equals(ComacConstants.STEP_NO)&&
					mstep.getM13().equals(ComacConstants.STEP_FINISH)){
				mstep.setM2(ComacConstants.STEP_NOW);
			}	
		}else{
			mstep.setM2(ComacConstants.STEP_NO);
		}
		mstepBo.update(mstep,getSysUser().getUserId());
		List<Integer> step = new ArrayList<Integer>();
		step.add(mstep.getM11());
		step.add(mstep.getM12());
		step.add(mstep.getM13());
		step.add(mstep.getM2());
		step.add(mstep.getM3());
		step.add(mstep.getM4());
		step.add(mstep.getM5());
		step.add(mstep.getMset());
		json.element("step", step);
		writeToResponse(json.toString());
		return null;
	}
	public String getMsiId() {
		return msiId;
	}
	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}
	public IMStepBo getMstepBo() {
		return mstepBo;
	}
	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}
	
	public IComVendorBo getComVendorBo() {
		return comVendorBo;
	}
	public void setComVendorBo(IComVendorBo comVendorBo) {
		this.comVendorBo = comVendorBo;
	}
	public MStep getMstep() {
		return mstep;
	}
	public void setMstep(MStep mstep) {
		this.mstep = mstep;
	}
	public IComMmelBo getComMmelBo() {
		return comMmelBo;
	}
	public void setComMmelBo(IComMmelBo comMmelBo) {
		this.comMmelBo = comMmelBo;
	}
}
