package com.rskytech.basedata.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComVendorDao;
import com.rskytech.pojo.ComVendor;

public class ComVendorDao extends BaseDAO implements IComVendorDao {

	@SuppressWarnings("unchecked")
	public List<ComVendor> loadVendorList(String msId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComVendor.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("vendorCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkVendor(String msId, String vendorId, String vendorCode) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComVendor.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("vendorCode", vendorCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComVendor> list = this.findByCriteria(dc);
		
		if (list == null || list.size() == 0){
			return false;
		} else if (vendorId == null || "".equals(vendorId)){
			return true;
		} else {
			ComVendor vendor = list.get(0);
			if (vendor.getVendorId().equals(vendorId)){
				return false;
			}
		}
		return true;
	}
}
