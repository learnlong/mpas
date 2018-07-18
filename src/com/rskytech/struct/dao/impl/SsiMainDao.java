package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.dao.ISsiMainDao;

@SuppressWarnings("unchecked")
public class SsiMainDao extends BaseDAO implements ISsiMainDao {

	@Override
	public List<Object[]> getSSsiListByModelSeriesId(String modelSeriesId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT B.SSI_ID,");
		sql.append(" CASE WHEN b.ata_id IS NULL THEN b.add_code ELSE a.ata_code END ata_code");
		sql.append(" FROM  COM_ATA  A");
		sql.append(" RIGHT JOIN  S_MAIN  B");
		sql.append(" ON A.ATA_ID=B.ATA_ID AND  A.VALID_FLAG = "+ComacConstants.VALIDFLAG_YES);
		sql.append(" WHERE  B.VALID_FLAG = "+ComacConstants.VALIDFLAG_YES);
		sql.append(" AND B.MODEL_SERIES_ID = '"+ modelSeriesId+"'");
		sql.append(" ORDER By A.ATA_CODE");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List<S1> getS1ListBySsiId(String ssiId) {
		DetachedCriteria dc = DetachedCriteria.forClass(S1.class);
		dc.add(Restrictions.eq("SMain.id", ssiId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<SStep> getSStepListBySsiId(String ssiId) {
		DetachedCriteria dc = DetachedCriteria.forClass(SStep.class);
		dc.add(Restrictions.eq("SMain.id", ssiId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<SMain> getSMainListByParentAtaId(String parentAtaId, String modelSeriesId,
			String userId) {
		StringBuffer sb = new StringBuffer();
		if(parentAtaId!=null){
			sb.append("select c.* from ( ");
			sb.append("SELECT s.* ,");
			sb.append(" fun_is_owner_auth('"+modelSeriesId+"','"+ComacConstants.STRUCTURE_CODE+"'," +
				"s.ata_id,'"+userId+"','"+ComacConstants.POSITION_ID_PROFESSION_ANAYIST+"') flag,");
			sb.append(" case when (s.is_ssi = 1 or s.is_ana = 1) then 1 else 0 end isTrue ");
			sb.append("FROM s_Main s WHERE s.parent_ata_id = '"+parentAtaId+"' and s.ata_id is not null and s.valid_flag = 1 ");
			sb.append(" union all select t.*,'1',");
			sb.append(" case when (t.is_ssi = 1 or t.is_ana = 1) then 1 else 0 end isTrue ");
			sb.append("FROM s_Main t WHERE t.parent_ata_id = '"+parentAtaId+"' and t.ata_id is null and t.valid_flag = 1 ");
			sb.append(" ) c where c.flag='1' and c.isTrue=1");
		}
		return this.executeQueryBySql(sb.toString(), SMain.class);
	}

}
