package com.rskytech.sys.dao.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.sys.dao.ISysTreeDao;

@SuppressWarnings("rawtypes")
public class SysTreeDao extends BaseDAO implements ISysTreeDao {

	@Override
	public List searchSubAtaOrMsiTree(ComUser user, String parentAtaId, String modelSeriesId, String searchType) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       b.msi_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       a.ata_level,");
			sb.append("       b.status,");
//			sb.append("       case when b.msi_id is not null then 1 else 0 end is_msi,");
			sb.append("       (select to_char(count(1))");
			sb.append("           from com_ata c");
			sb.append("         where c.parent_ata_id = a.ata_id");
			sb.append("           and c.valid_flag = " + ComacConstants.VALIDFLAG_YES + ") isChild,");
			sb.append("       fun_is_owner_auth(a.model_series_id, '" + ComacConstants.SYSTEM_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') quanxian");
			sb.append("  from com_ata a, m_main b");
			sb.append(" where a.ata_id = b.ata_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			if("0".equals(parentAtaId)){
				sb.append("   and a.parent_ata_id is null");
			}else{
				sb.append("   and a.parent_ata_id = '" + parentAtaId + "'");
			}
			sb.append(" order by a.ata_code");			
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       b.msi_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       a.ata_level,");
			sb.append("       b.status,");
//			sb.append("       case when b.msi_id is not null then 1 else 0 end is_msi,");
			sb.append("       (select to_char(count(1))");
			sb.append("           from com_ata c");
			sb.append("         where c.parent_ata_id = a.ata_id");
			sb.append("           and c.valid_flag = " + ComacConstants.VALIDFLAG_YES + ") isChild,");
			sb.append("       case when fun_is_owner_auth(a.model_series_id, '" + ComacConstants.SYSTEM_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1' and b.msi_id is not null and d.is_msi = 1 then '1' else '0' end  quanxian");
			sb.append("  from com_ata a, m_main b, m_select d");
			sb.append(" where a.ata_id = b.ata_id(+)");
			sb.append("   and a.ata_id = d.ata_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and d.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			if("0".equals(parentAtaId)){
				sb.append("   and a.parent_ata_id is null");
			}else{
				sb.append("   and a.parent_ata_id = '" + parentAtaId + "'");
			}
			sb.append(" order by a.ata_code");			
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}

	@Override
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType)
			throws BusinessException {
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       a.ata_level,");
			sb.append("       b.status,");
			sb.append("       b.msi_id");
			sb.append("  from com_ata a, m_main b");
			sb.append(" where a.ata_id = b.ata_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.SYSTEM_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append(" order by a.ata_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       a.ata_level,");
			sb.append("       b.status,");
			sb.append("       b.msi_id");
			sb.append("  from com_ata a, m_main b, m_select c");
			sb.append(" where a.ata_id = b.ata_id");
			sb.append("   and a.ata_id = c.ata_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and c.is_msi = 1");
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.SYSTEM_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append(" order by a.ata_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}}