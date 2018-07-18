package com.rskytech.sys.bo.impl;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IMStepDao;
public class MStepBo extends BaseBO implements IMStepBo {
	private IMStepDao mStepDao; 
	@Override
	public MStep getMStepByMsiId(String msiId)throws BusinessException{
		List<MStep> list = mStepDao.getMStepByMsiId(msiId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	public IMStepDao getmStepDao() {
		return mStepDao;
	}
	public void setmStepDao(IMStepDao mStepDao) {
		this.mStepDao = mStepDao;
	}
	
}
