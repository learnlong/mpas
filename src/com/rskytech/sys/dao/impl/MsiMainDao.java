package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.MMain;
import com.rskytech.sys.dao.IMsiMainDao;
@SuppressWarnings({ "unchecked", "unused" })
public class MsiMainDao extends BaseDAO implements IMsiMainDao {

	@Override
	public List<Object[]> getMSIAll(String modelSeriesId)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT B.MSI_ID,A.ATA_CODE");
		sql.append(" FROM  COM_ATA  A,M_MAIN B ");
		sql.append(" WHERE  A.ATA_ID=B.ATA_ID ");
		sql.append(" AND  EXISTS (select 1 from M_1_3 C WHERE  C.MSI_ID=B.MSI_ID) ");
		sql.append(" AND  B.VALID_FLAG = "+ComacConstants.VALIDFLAG_YES);
		sql.append(" AND  A.VALID_FLAG = "+ComacConstants.VALIDFLAG_YES);
		sql.append(" AND B.MODEL_SERIES_ID = '"+ modelSeriesId+"'");
		sql.append(" ORDER By A.ATA_CODE");
		return this.executeQueryBySql(sql.toString());

	}

	@Override
	public List<MMain> getMMainByStatus(String modelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MMain.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
	    return  this.findByCriteria(dc);
	}

	@Override
	public List<MMain> getMMainByAtaIdAndModelSeries(String ataId,
			String modelSeriesId) {
		DetachedCriteria dc=DetachedCriteria.forClass(MMain.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("comAta.ataId", ataId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}

}
