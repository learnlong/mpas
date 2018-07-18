package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.MStep;

public interface IMStepDao extends IDAO{
	public List<MStep> getMStepByMsiId(String msiId)throws BusinessException;
}
