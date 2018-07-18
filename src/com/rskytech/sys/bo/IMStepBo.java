package com.rskytech.sys.bo;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.MStep;

public interface IMStepBo extends IBo {
	/**
	 * 根据MsiId查询步骤表
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-15
	 */
	public MStep getMStepByMsiId(String msiId)throws BusinessException;

}
