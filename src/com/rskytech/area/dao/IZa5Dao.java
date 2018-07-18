package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Za5;

public interface IZa5Dao extends IDAO {

	/**
	 * 通过区域主表ID，查询ZA5
	 * @param zaId 区域主表ID
	 * @param step ZA5A或ZA5B
	 * @return Za5
	 * @author zhangjianmin
	 */
	public Za5 getZa5ByZaId(String zaId, String step) throws BusinessException;
}
