package com.rskytech.area.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ZaStep;

public interface IZaStepDao extends IDAO {

	/**
	 * 通过区域主表ID，查询区域步骤表
	 * @param zaId 区域主表ID
	 * @return ZaStep
	 * @author zhangjianmin
	 */
	public ZaStep getZaStep(String zaId) throws BusinessException;
}
