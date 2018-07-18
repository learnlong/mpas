package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.lhirf.dao.ILhSelectDao;
import com.rskytech.pojo.LhMain;

public class LhSelectDao extends BaseDAO implements ILhSelectDao {

	@Override
	public List<LhMain> searchLhMainByHsiCode(String hsiCode, String modelSeriesId) {
		DetachedCriteria dc = DetachedCriteria.forClass(LhMain.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("hsiCode", hsiCode));
		return this.findByCriteria(dc);
	}

	@Override
	public void deleteLhMain(LhMain lhMain) {
		String hsiId = lhMain.getHsiId();
		String sql = "";
		sql="DELETE FROM lh_1 l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_2 l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_3 l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_4 l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_5 l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_1a l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_step l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		sql="DELETE FROM lh_main l WHERE l.hsi_id='"+hsiId+"'" ;
		this.executeBySql(sql);
		
	}

}
