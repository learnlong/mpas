package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.area.dao.IZa41Dao;
import com.rskytech.pojo.Za41;

public class Za41Dao extends BaseDAO implements IZa41Dao {

	@SuppressWarnings("unchecked")
	public Za41 getZa41ByZaId(String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za41.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		List<Za41> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
