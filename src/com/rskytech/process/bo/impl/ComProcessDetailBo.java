package com.rskytech.process.bo.impl;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.process.bo.IComProcessDetailBo;
import com.rskytech.process.dao.IComProcessDetailDao;

public class ComProcessDetailBo extends BaseBO implements IComProcessDetailBo{
	private IComProcessDetailDao comProcessDetailDao;

	
	public IComProcessDetailDao getComProcessDetailDao() {
		return comProcessDetailDao;
	}

	public void setComProcessDetailDao(IComProcessDetailDao comProcessDetailDao) {
		this.comProcessDetailDao = comProcessDetailDao;
	}
	
	
}
