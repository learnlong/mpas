package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.Za41;

public interface IZa41Dao extends IDAO {

	/**
	 * 通过区域主表ID，查询ZA41
	 * @param zaId 区域主表ID
	 * @return Za41
	 * @author zhangjianmin
	 */
	public Za41 getZa41ByZaId(String zaId) throws BusinessException;
}
