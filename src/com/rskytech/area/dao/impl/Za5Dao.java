package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.area.dao.IZa5Dao;
import com.rskytech.pojo.Za5;

public class Za5Dao extends BaseDAO implements IZa5Dao {

	@SuppressWarnings("unchecked")
	public Za5 getZa5ByZaId(String zaId, String step) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za5.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		dc.add(Restrictions.eq("step", step));
		List<Za5> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
