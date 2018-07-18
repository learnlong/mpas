package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.LhStep;

public interface ILh1aDao extends IDAO {
	/**
	 * 根据refcode和hsiId查询LhStep数据
	 * @param refCode
	 * @param hsiId
	 * @return
	 * @throws BusinessException
	 */
	 public List<LhStep> getLh5stepByCode( String refCode,String hsiId)throws BusinessException ;
	 
	 public List<Lh1a> getLh1aListByHsiId(String hsiId);

}
