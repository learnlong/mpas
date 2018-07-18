package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.S3Crack;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IS3Dao;

public class S3Dao extends BaseDAO implements IS3Dao {

	@Override
	public List getS3Records(String ssiId) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT B.S3_ID,A.SSI_FORM,B.IS_AD_EFFECT,B.TASK_TYPE," +
				"B.BASIC_CRACK,B.MATERIAL_SIZE,B.EDGE_EFFECT,B.DETECT_CRACK,B.LC,B.LO,B.DETAIL_CRACK," +
				"B.TASK_INTERVAL_REPEAT,B.IS_OK,B.REMARK,B.PIC_URL,B.DETAIL_SDI,B.TASK_INTERVAL," +
				" A.S1_ID,B.INT_OUT,A.OWN_AREA,t.task_id");
		sql.append(" FROM S_1 A");
		sql.append(" LEFT JOIN S_3 B ON A.S1_ID = B.S1_ID");
		sql.append(" LEFT JOIN task_msg t ON  a.s1_id||'FD' = t.any_content1");
		sql.append(" WHERE A.SSI_ID='"+ssiId+"'");
		sql.append(" AND A.IS_METAL=1 AND A.DESIGN_PRI=2 AND A.IS_FD=1");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List<S3Crack> getS3Crack(String id) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(S3Crack.class);
		dc.add(Restrictions.eq("s3.id",id));
		return this.findByCriteria(dc);
	}

	@Override
	public List getMissionCount(String modelId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelId));
		dc.add(Restrictions.eq("sourceSystem",ComacConstants.STRUCTURE_CODE));
		dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("taskValid",ComacConstants.TASK_VALID_TEMPORARY));
		return this.findByCriteria(dc);
	}

	@Override
	public List getMatrixByAnaFlg(String anaFlg, String modelId)
			throws BusinessException {
		String hql=" from CusMatrix t where t.anaFlg='"+anaFlg+"' and t.comModelSeries.modelSeriesId ='"+modelId+"' order by t.matrixRow,t.matrixCol";
		return this.findByHql(hql, null);
	}

}
