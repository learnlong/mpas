package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M11;

public interface IM11Dao extends IDAO {
	/**
	 * 根据MSI查询M11
	 * 
	 * @param msiId
	 * @return
	 */

	public List<M11> getM11ByMsiId(String msiId) throws BusinessException;
}

