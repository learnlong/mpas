package com.rskytech.sys.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.MReferMsi;

public interface IReCheckMsiBo extends IBo {
	
	public List<MReferMsi> getReferListMsiByModelSeriesId(String modelSeriesId)throws BusinessException;

}
