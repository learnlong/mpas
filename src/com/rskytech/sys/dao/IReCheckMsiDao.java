package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.MReferMsi;

public interface IReCheckMsiDao  extends IDAO{
	public List<MReferMsi> getReferListMsiByModelSeriesId(String modelSeriesId)throws BusinessException;
}
