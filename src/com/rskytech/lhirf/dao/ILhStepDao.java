package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.LhStep;

public interface ILhStepDao extends IDAO {

	
	public List<LhStep> getLhStepListBylhHsId(String lhHsi) throws BusinessException;
}
