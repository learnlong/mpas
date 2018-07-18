package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M11;
import com.rskytech.sys.dao.IM11Dao;

public class M11Dao extends BaseDAO implements IM11Dao {
	/**
	 * 根据MSI查询M11
	 * 
	 * @param msiId
	 * @return
	 */

	public List<M11> getM11ByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M11.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}
}
