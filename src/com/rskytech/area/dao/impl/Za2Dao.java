package com.rskytech.area.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.area.dao.IZa2Dao;
import com.rskytech.pojo.Za2;

public class Za2Dao extends BaseDAO implements IZa2Dao {

	@SuppressWarnings("unchecked")
	public Za2 getZa2ByZaId(String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(Za2.class);
		dc.add(Restrictions.eq("zaMain.zaId", zaId));
		List<Za2> list = this.findByCriteria(dc);
		
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public void deleteAreaAnalysis(String zaId, Integer position) throws BusinessException{
		String s = "delete za_5 where za_id = '" + zaId + "' ";
		if (position == 1){//内部，则删除外部数据
			s += "and step = 'ZA5B'";
			this.executeBySql(s);
		} else if (position == 2){//外部，则删除内部数据
			s += "and step = 'ZA5A'";
			this.executeBySql(s);
		}
	}
}
