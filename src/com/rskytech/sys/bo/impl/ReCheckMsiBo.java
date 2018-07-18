package com.rskytech.sys.bo.impl;



import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.sys.bo.IReCheckMsiBo;
import com.rskytech.sys.dao.IReCheckMsiDao;
public class ReCheckMsiBo  extends BaseBO implements IReCheckMsiBo  {
	private IReCheckMsiDao reCheckMsiDao;

	@Override
	public List<MReferMsi> getReferListMsiByModelSeriesId(String modelSeriesId)
			throws BusinessException {
		return this.reCheckMsiDao.getReferListMsiByModelSeriesId(modelSeriesId);
	}

	public IReCheckMsiDao getReCheckMsiDao() {
		return reCheckMsiDao;
	}

	public void setReCheckMsiDao(IReCheckMsiDao reCheckMsiDao) {
		this.reCheckMsiDao = reCheckMsiDao;
	}
}
