package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M12;
import com.rskytech.sys.dao.IM12Dao;

public class M12Dao extends BaseDAO implements IM12Dao {
	/**
	 *根据msiId查询M12
	 * @param msiId
	 * @return
	 */
	public List<M12> getM12AListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M12.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("proCode"));
		return this.findByCriteria(dc);

	}
}
