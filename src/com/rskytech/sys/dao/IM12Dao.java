package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M12;

public interface IM12Dao extends IDAO {
	/**
	 *根据msiId查询M12
	 * @param msiId
	 * @return
	 */
	public List<M12> getM12AListByMsiId(String msiId) throws BusinessException;
}

