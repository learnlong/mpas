package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S5;
import com.rskytech.struct.dao.IS5Dao;

public class S5Dao extends BaseDAO implements IS5Dao {

	@Override
	public List getItemCount(String itemName, String modelId,String aOrb)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT A.ITEM_NAME,A.ITEM_S45_ID,A.ITEM_ALGORITHM,B.ALGORITHM_FLG FROM  CUS_ITEM_S45 A LEFT JOIN CUS_EDR_ADR B ON A.STEP_FLG=B.STEP_FLG WHERE A.ITEM_FLG ='"
						+ itemName
						+ "' AND A.STEP_FLG='S5"+aOrb+"' AND A.MODEL_SERIES_ID= '"+modelId+"'"
						+ " AND B.MODEL_SERIES_ID= '"+modelId+"'"
						+ "   AND A.VALID_FLAG=1 ORDER BY A.ITEM_SORT");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List getLevelCount(String itemId, String modelId,String aOrb)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT LEVEL_NAME,LEVEL_ID,LEVEL_VALUE FROM CUS_LEVEL WHERE ITEM_ID='"+itemId+"'" 
						+ " AND MODEL_SERIES_ID= '"+modelId+"'"
						+ " AND ANA_FLG='S5"+aOrb+"'  AND VALID_FLAG=1 ORDER BY LEVEL_VALUE");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List<S1> getS1(String sssiId, int isMetal, int inOrOut)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(S1.class);
		dc.add(Restrictions.eq("SMain.id", sssiId));
		dc.add(Restrictions.eq("isMetal", isMetal));
		if (inOrOut == 0) {// 0代表内部
			dc.add(Restrictions.eq("internal", 1));
		}
		if (inOrOut == 1) {// 1代表外部
			dc.add(Restrictions.eq("outernal", 1));
		}
		 dc.addOrder(Order.asc("s1Id"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<S5> getS5BySsiId(String ssiId, Integer inOrOut)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(S5.class);
		dc.createAlias("s1", "s1");
		dc.add(Restrictions.eq("SMain.id", ssiId));
		if (inOrOut == 0) {
			dc.add(Restrictions.eq("inOrOut", ComacConstants.INNER));
		}
		if (inOrOut == 1) {
			dc.add(Restrictions.eq("inOrOut", ComacConstants.OUTTER));
		}
		dc.addOrder(Order.asc("s1.ssiForm"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<S5> isExistForS5(String ssiId, String s1Id, String inOrOut)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(S5.class);
		dc.add(Restrictions.eq("SMain.id", ssiId));
		dc.add(Restrictions.eq("s1.s1Id", s1Id));
		dc.add(Restrictions.eq("inOrOut", inOrOut));
		return this.findByCriteria(dc);
	}

}
