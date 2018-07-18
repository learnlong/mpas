package com.rskytech.lhirf.dao.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.dao.ILhTreeDao;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public class LhTreeDao extends BaseDAO implements ILhTreeDao {
	public List searchSubAreaOrHsiTree(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select area_id,");
			sb.append("       area_code,");
			sb.append("       area_name,");
			sb.append("       area_level,");
			sb.append("       status,");
			sb.append("       quanxian,");
			sb.append("       AREAORHSI,");
			sb.append("       IS_REF");
			sb.append("  from (select a.area_id,");
			sb.append("               a.area_code,");
			sb.append("               a.area_name,");
			sb.append("               a.area_level,");
			sb.append("               null status,");
			sb.append("               fun_is_owner_auth(a.model_series_id, '" + ComacConstants.LHIRF_CODE + "', a.area_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') quanxian,");
			sb.append("               'AREA' AREAORHSI,");
			sb.append("               NULL IS_REF");
			sb.append("          from com_area a");
			sb.append("         where a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
			if("0".equals(parentAreaId)){
				sb.append("           and a.parent_area_id is null");
			}else {
				sb.append("           and a.parent_area_id = '" + parentAreaId + "'");
			}
			sb.append("        union");
			sb.append("        select b.hsi_id area_id,");
			sb.append("               b.hsi_code area_code,");
			sb.append("               b.hsi_name area_name,");
			sb.append("               null area_level,");
			sb.append("               b.status,");
			sb.append("               '0' quanxian,");
			sb.append("               'HSI' AS AREAORHSI,");
			sb.append("               b.ref_hsi_code IS_REF");
			sb.append("          from lh_main b");
			sb.append("         where b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			if("0".equals(parentAreaId)){
				sb.append("           and b.area_id is null");
			}else {
				sb.append("           and b.area_id = '" + parentAreaId + "'");
			}
			sb.append("           and b.model_series_id = '" + modelSeriesId + "')");
			sb.append(" order by AREAORHSI, area_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || (ComacConstants.REPORT.equals(searchType))){
			sb.append("select area_id,");
			sb.append("       area_code,");
			sb.append("       area_name,");
			sb.append("       area_level,");
			sb.append("       status,");
			sb.append("       quanxian,");
			sb.append("       AREAORHSI,");
			sb.append("       IS_REF");
			sb.append("  from (select a.area_id,");
			sb.append("               a.area_code,");
			sb.append("               a.area_name,");
			sb.append("               a.area_level,");
			sb.append("               null status,");
			sb.append("               '0' quanxian,");
			sb.append("               'AREA' AREAORHSI,");
			sb.append("               NULL IS_REF");
			sb.append("          from com_area a");
			sb.append("         where a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
			if("0".equals(parentAreaId)){
				sb.append("           and a.parent_area_id is null");
			}else {
				sb.append("           and a.parent_area_id = '" + parentAreaId + "'");
			}
			sb.append("        union");
			sb.append("        select b.hsi_id area_id,");
			sb.append("               b.hsi_code area_code,");
			sb.append("               b.hsi_name area_name,");
			sb.append("               null area_level,");
			sb.append("               b.status,");
			sb.append("               case when b.ana_user = '" + user.getUserId() + "' then '1' else '0' end quanxian,");
			sb.append("               'HSI' AS AREAORHSI,");
			sb.append("               b.ref_hsi_code IS_REF");
			sb.append("          from lh_main b");
			sb.append("         where b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			if("0".equals(parentAreaId)){
				sb.append("           and b.area_id is null");
			}else {
				sb.append("           and b.area_id = '" + parentAreaId + "'");
			}
			sb.append("           and b.model_series_id = '" + modelSeriesId + "')");
			sb.append(" order by AREAORHSI, area_code");
			return this.executeQueryBySql(sb.toString());
			
		}
		return null;
	}
	
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select a.area_id,");
			sb.append("       a.area_code,");
			sb.append("       a.area_name,");
			sb.append("       (select case when count(1) = 0 then '' else '" + ComacConstants.ANALYZE_STATUS_NEW + "' end aa from lh_main b where b.area_id = a.area_id) status,");
			sb.append("       '' ref_hsi_code");
			sb.append("  from com_area a");
			sb.append(" where a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.LHIRF_CODE + "', a.area_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append(" order by a.area_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select hsi_id, hsi_code, hsi_name, status, ref_hsi_code");
			sb.append("  from lh_main");
			sb.append(" where valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and ana_user = '" + user.getUserId() + "'");
			sb.append("   and model_series_id = '" + modelSeriesId + "'");
			sb.append(" order by hsi_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}
}