package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;

public interface ILhirfParamDao extends IDAO {
	
	public List getCusInList(Object[] ob) throws BusinessException ;

}
