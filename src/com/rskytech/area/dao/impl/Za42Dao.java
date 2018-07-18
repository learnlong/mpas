package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.area.dao.IZa42Dao;
import com.rskytech.pojo.Za42;

public class Za42Dao extends BaseDAO implements IZa42Dao {

	@SuppressWarnings("unchecked")
	public Za42 getZa42ByZaId(String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za42.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		List<Za42> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
}
