package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.Sy;
import com.rskytech.struct.dao.IS4Dao;
import com.rskytech.struct.dao.ISyDao;

public class SyDao extends BaseDAO implements ISyDao {

	@Override
	public List getItemCount(String itemName, String modelId,String aOrb)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT A.ITEM_NAME,A.ITEM_S45_ID,A.ITEM_ALGORITHM,B.ALGORITHM_FLG FROM  CUS_ITEM_S45 A,CUS_EDR_ADR B WHERE A.ITEM_FLG ='"
						+ itemName
						+ "' AND A.STEP_FLG='SY"+aOrb+"' AND A.VALID_FLAG=1 AND A.MODEL_SERIES_ID='"+modelId+"'"
						+ "  AND B.MODEL_SERIES_ID='"+modelId+"'"
						+ "   AND B.STEP_FLG='SY"+aOrb+"' ORDER BY A.ITEM_SORT");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List getLevelCount(String itemId, String modelId,String aOrb)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT LEVEL_NAME,LEVEL_ID,LEVEL_VALUE FROM CUS_LEVEL WHERE ITEM_ID='"+itemId+"'"
						+ " AND MODEL_SERIES_ID= '"+modelId+"'"
						+ " AND ANA_FLG='SY"+aOrb+"' AND VALID_FLAG=1 ORDER BY LEVEL_VALUE");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List<Sy> getSyListBySsiIdAndS1Id(String ssiId, String id, String inOrOut)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Sy.class);
		dc.add(Restrictions.eq("SMain.id", ssiId));
		dc.add(Restrictions.eq("s1.s1Id", id));
		dc.add(Restrictions.eq("inOrOut", inOrOut));
		return this.findByCriteria(dc);
	}

	@Override
	public List<CusEdrAdr> getCusStepList(String step, String modelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusEdrAdr.class);
		dc.add(Restrictions.eq("stepFlg", step));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		return this.findByCriteria(dc);
	}

	@Override
	public List getSyBySsiId(String ssiId, Integer inOrOut)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(Sy.class);
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
	public List getS1(String sssiId, int isMetal, int inOrOut)
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
	
}
