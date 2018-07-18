package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.pojo.CusEdrAdr;

public class CusEdrAdrBo extends BaseBO implements ICusEdrAdrBo{

	/**
	 * 根据机型、分析步骤查询EDR、ADR选择表的数据
	 * @param modelSeriesId 机型系列ID
	 * @param stepFlg 分析步骤
	 * @return 当前机型分析步骤的选择表数据List
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusEdrAdr> getCusEdrAdrList(String modelSeriesId,
			String stepFlg) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusEdrAdr.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId));
		dc.add(Restrictions.eq("stepFlg",stepFlg));
		return this.findByCritera(dc);
	}
}
