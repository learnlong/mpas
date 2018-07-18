package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.struct.bo.ISsiMainBo;
import com.rskytech.struct.dao.ISsiMainDao;

public class SsiMainBo extends BaseBO implements ISsiMainBo {
	private ISsiMainDao ssiMainDao;

	@Override
	public List<Object[]> getSSsiListByModelSeriesId(String modelSeriesId) {
		return this.ssiMainDao.getSSsiListByModelSeriesId(modelSeriesId);
	}

	public ISsiMainDao getSsiMainDao() {
		return ssiMainDao;
	}

	public void setSsiMainDao(ISsiMainDao ssiMainDao) {
		this.ssiMainDao = ssiMainDao;
	}
	
	
}
