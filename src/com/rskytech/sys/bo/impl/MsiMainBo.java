package com.rskytech.sys.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.pojo.MMain;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.sys.dao.IMsiMainDao;
public class MsiMainBo extends BaseBO implements IMsiMainBo {
	private IMsiMainDao msiMainDao;


	@Override
	public List<MMain> getMMainByStatus(String modelSeriesId)
			throws BusinessException {
	    return  this.msiMainDao.getMMainByStatus(modelSeriesId);
	}

	@Override
	public MMain getMMainByAtaIdAndModelSeries(String ataId, String modelSeriesId) {
		List<MMain> lst=this.msiMainDao.getMMainByAtaIdAndModelSeries(ataId, modelSeriesId);
		if(lst.size()!=0){
			return lst.get(0);
		}
		return null;
	}

	@Override
	public List<Object[]> getMSIAll(String modelSeriesId)
			throws BusinessException {
		return this.msiMainDao.getMSIAll(modelSeriesId);
	}

	public IMsiMainDao getMsiMainDao() {
		return msiMainDao;
	}

	public void setMsiMainDao(IMsiMainDao msiMainDao) {
		this.msiMainDao = msiMainDao;
	}

}
