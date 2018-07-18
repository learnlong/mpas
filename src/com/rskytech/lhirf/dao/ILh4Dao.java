package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Lh4;

public interface ILh4Dao extends IDAO {
	
	
	public List<Lh4> getLh4ListBylhHsId(String hsiId) throws BusinessException; 
}
