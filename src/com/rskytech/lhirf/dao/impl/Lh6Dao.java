package com.rskytech.lhirf.dao.impl;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.dao.ILh6Dao;

public class Lh6Dao extends BaseDAO implements ILh6Dao {
	
	/**
	 * 通过区域机型ID号 区域id 得到其下任务信息
	 * 
	 * @param modelSeriesId
	 * @param AreaId
	 *            区域ID
	 * @return objectlist[]
	 */
	@Override
	public Page getLhirfTaskMsgList(String modelSeriesId, String areaId, Page page) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT lh.hsi_id,lh.hsi_code,t.task_id,t.task_code,t.task_type,") ;
		sb.append("t.any_content1,t.reach_way,t.task_desc,t.task_interval,t.need_transfer,");
		sb.append("t.has_accept,t.reject_reason,t.effectiveness FROM task_msg t JOIN lh_main lh");
		sb.append(" ON lh.hsi_id=t.source_ana_id AND lh.model_series_id=t.model_series_id");
		sb.append(" WHERE lh.area_id='"+areaId+"'");
		sb.append(" AND lh.valid_flag="+ComacConstants.VALIDFLAG_YES);
		sb.append(" AND lh.model_series_id='"+modelSeriesId+"'");
		sb.append(" AND lh.status !='"+ComacConstants.ANALYZE_STATUS_NEW+"'");
		sb.append(" AND t.source_system='"+ComacConstants.LHIRF_CODE+"'");
		sb.append(" ORDER BY lh.hsi_code");
		return this.findBySql(page, sb.toString(), new Object[] {});
	}
	
	
	@Override
	public List<Object[]> getLhirfListByIdNoPage(String modelSeriesId, String areaId) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT lh.hsi_id,lh.hsi_code,t.task_id,t.task_code,t.task_type,") ;
		sb.append("t.any_content1,t.reach_way,t.task_desc,t.task_interval,t.need_transfer,");
		sb.append("t.has_accept,t.reject_reason,t.effectiveness FROM task_msg t JOIN lh_main lh");
		sb.append(" ON lh.hsi_id=t.source_ana_id AND lh.model_series_id=t.model_series_id");
		sb.append(" WHERE lh.area_id='"+areaId+"'");
		sb.append(" AND lh.valid_flag="+ComacConstants.VALIDFLAG_YES);
		sb.append(" AND lh.model_series_id='"+modelSeriesId+"'");
		sb.append(" AND lh.status !='"+ComacConstants.ANALYZE_STATUS_NEW+"'");
		sb.append(" AND t.source_system='"+ComacConstants.LHIRF_CODE+"'");
		sb.append(" ORDER BY lh.hsi_code");
		return this.executeQueryBySql(sb.toString());
	}
}
