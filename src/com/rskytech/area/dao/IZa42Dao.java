package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Za42;

public interface IZa42Dao extends IDAO {

	/**
	 * 通过区域主表ID，查询ZA42
	 * @param zaId 区域主表ID
	 * @return Za42
	 * @author zhangjianmin
	 */
	public Za42 getZa42ByZaId(String zaId) throws BusinessException;
}
