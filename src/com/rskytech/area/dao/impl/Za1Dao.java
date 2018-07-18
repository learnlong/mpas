package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.ZaMain;

public class Za1Dao extends BaseDAO implements IZa1Dao {

	@SuppressWarnings("unchecked")
	public ZaMain getZaMainByAreaId(String msId, String areaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ZaMain.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("comArea.areaId", areaId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ZaMain> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Za1 getZa1ByZaId(String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za1.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		List<Za1> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public void deleteAreaAnalysis(String zaId) throws BusinessException{
		String s = "delete za_2 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_41 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_42 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_43 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_5 where za_id = '" + zaId + "'";
		this.executeBySql(s);
	}
	
	public void deleteAreaAnalysisAll(String zaId) throws BusinessException{
		String s = "delete za_1 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_2 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_41 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_42 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_43 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_5 where za_id = '" + zaId + "'";
		this.executeBySql(s);
		
		s = "delete za_step where za_id = '" + zaId + "'";
		this.executeBySql(s);
	}
}
