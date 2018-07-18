package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.area.dao.IZaStepDao;
import com.rskytech.pojo.ZaStep;

public class ZaStepDao extends BaseDAO implements IZaStepDao {

	@SuppressWarnings("unchecked")
	public ZaStep getZaStep(String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ZaStep.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		List<ZaStep> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
