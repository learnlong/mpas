package com.rskytech.lhirf.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.lhirf.bo.ILhStepBo;
import com.rskytech.lhirf.dao.ILhStepDao;
import com.rskytech.pojo.LhStep;

public class LhStepBo extends BaseBO implements ILhStepBo {
	private ILhStepDao lhStepDao;

	/*
	 * lhirf  步骤  
	 */
	@Override
	public LhStep getLhStepBylhHsId(String lhHsi) throws BusinessException {
		List<LhStep> list = this.lhStepDao.getLhStepListBylhHsId(lhHsi);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public ILhStepDao getLhStepDao() {
		return lhStepDao;
	}

	public void setLhStepDao(ILhStepDao lhStepDao) {
		this.lhStepDao = lhStepDao;
	}
	
	
}
	

