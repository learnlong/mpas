package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.sys.dao.IM3Dao;
public class M3Dao  extends BaseDAO implements IM3Dao{

	@Override
	public List getVendorCountByMsi(String msiId) throws BusinessException {
		String sql = "SELECT VENDOR, count(VENDOR) c FROM M_1_2 where Msi_id = '"+msiId+"' and vendor is not null group by vendor order by c desc";
		return this.executeQueryBySql(sql.toString());
	}

	public List<M3Additional> getM3AdditionalByM3Id(String id, String taskId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M3Additional.class);
		dc.add(Restrictions.eq("m3.id", id));
		dc.add(Restrictions.eq("addTaskId", taskId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M3> getM3ListByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M3.class);
		dc.createAlias("m13C", "m13C");
		dc.add(Restrictions.eq("MMain.id", msiId));
		dc.addOrder(Order.asc("m13C.causeCode"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M3> getM3ByM13cId(String m13cId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M3.class);
		dc.add(Restrictions.eq("m13C.id", m13cId));
		return this.findByCriteria(dc);
	}
	

}
