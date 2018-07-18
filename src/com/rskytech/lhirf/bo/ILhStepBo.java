package com.rskytech.lhirf.bo;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.LhStep;

public interface ILhStepBo extends IBo {
	//lhirf 步骤 
	public LhStep getLhStepBylhHsId(String lhHsi)throws BusinessException;
}
