package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Lh1;

public interface ILh1Dao extends IDAO {
	
	/*
	 * 根据hsiId 查询 lh1表
	 */
	public List<Lh1> getLh1ByHsiId(String hsiId)throws BusinessException ;

}
