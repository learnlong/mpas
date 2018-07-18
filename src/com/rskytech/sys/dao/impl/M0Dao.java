package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M0;
import com.rskytech.sys.dao.IM0Dao;

public class M0Dao extends BaseDAO implements IM0Dao {
	/**
	 * 查询当前MSI的MSI及子ATA
	 * @param msiId   系统Main的Id
	 * @return
	 */
	public List<M0> getMsiATAListByMsiId(String msiId)throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M0.class);
		dc.add(Restrictions.eq("MMain.msiId", msiId));
		dc.addOrder(Order.asc("proCode"));
		return this.findByCriteria(dc);
	}
}
