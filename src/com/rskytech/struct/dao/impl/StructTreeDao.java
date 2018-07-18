package com.rskytech.struct.dao.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.struct.dao.IStructTreeDao;

@SuppressWarnings( "rawtypes" )
public class StructTreeDao extends BaseDAO implements IStructTreeDao {

	public List searchSubAtaOrSsiTree(ComUser user, String parentAtaId, String level, String modelSeriesId, String searchType) throws BusinessException{
		String userId = user.getUserId();
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       a.ata_level,");
			sb.append("       b.status,");
			sb.append("       fun_is_owner_auth(a.model_series_id, '" + ComacConstants.STRUCTURE_CODE + "', a.ata_id, '" + userId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') isedit,");
			sb.append("       (select to_char(count(1)) from com_ata c where c.parent_ata_id = a.ata_id and c.valid_flag = " + ComacConstants.VALIDFLAG_YES + ") isChild,");
			sb.append("       case when (b.is_ssi = 1) then 1 when (b.is_ssi = 0 and b.is_ana = 1) then 2 else 0 end is_ssi,");
			sb.append("       b.ssi_id");
			sb.append("  from com_ata a, s_main b");
			sb.append(" where a.ata_id = b.ata_id(+)");
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			if("0".equals(parentAtaId)){
				sb.append("   and a.parent_ata_id is null");
			}else{
				sb.append("   and a.parent_ata_id = '" + parentAtaId + "'");
			}
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append(" order by ata_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select ata_id, ata_code, ata_name, ata_level, status, isedit, isChild, is_ssi, ssi_id");
			sb.append("  from (select a.ata_id,");
			sb.append("               a.ata_code,");
			sb.append("               a.ata_name,");
			sb.append("               a.ata_level,");
			sb.append("               b.status,");
			sb.append("               case when fun_is_owner_auth(a.model_series_id, '" + ComacConstants.STRUCTURE_CODE + "', a.ata_id, '" + userId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1' and b.ssi_id is not null then '1' else '0' end  isedit,");
			sb.append("               (select to_char(count(1))");
			sb.append("                  from com_ata c");
			sb.append("                 where c.parent_ata_id = a.ata_id");
			sb.append("                   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES + ") isChild,");
			sb.append("               case when (b.is_ssi = 1) then 1 when (b.is_ssi = 0 and b.is_ana = 1) then 2 else 0 end is_ssi,");
			sb.append("				  b.ssi_id");
			sb.append("          from com_ata a, s_main b");
			sb.append("         where a.ata_id = b.ata_id(+)");
			sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and b.model_series_id(+) = '" + modelSeriesId + "'");
			if("0".equals(parentAtaId)){
				sb.append("           and a.parent_ata_id is null");
			}else{
				sb.append("           and a.parent_ata_id = '" + parentAtaId + "'");
			}
			sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("        union");
			sb.append("        select null ata_id,");
			sb.append("               d.add_code ata_code,");
			sb.append("               d.add_name ata_name,");
			sb.append("               0 ata_level,");
			sb.append("               d.status,");
			sb.append("               case when d.add_user = '" + userId + "' then '1' else '0' end isedit,");
			sb.append("               '0' isChild,");
			sb.append("               case when (d.is_ssi = 1) then 1 when (d.is_ssi = 0 and d.is_ana = 1) then 2 else 0 end is_ssi,");
			sb.append("				  d.ssi_id");
			sb.append("          from s_main d");
			sb.append("         where d.parent_ata_id = '" + parentAtaId + "'");
			sb.append("           and d.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and is_add = 1");
			sb.append("           and d.model_series_id = '" + modelSeriesId + "')");
			sb.append(" order by ata_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}
	
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.CHOOSE.equals(searchType)){
			sb.append("select a.ata_id,");
			sb.append("       a.ata_code,");
			sb.append("       a.ata_name,");
			sb.append("       b.status,");
			sb.append("       a.ata_level,");
			sb.append("       case when (b.is_ssi = 1) then 1 when (b.is_ssi = 0 and b.is_ana = 1) then 2 else 0 end is_ssi,");
			sb.append("       b.ssi_id");
			sb.append("  from com_ata a, s_main b");
			sb.append(" where a.ata_id = b.ata_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.STRUCTURE_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			sb.append(" order by ata_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select ata_id, ata_code, ata_name, status, ata_level, is_ssi, ssi_id");
			sb.append("  from (select a.ata_id, a.ata_code, a.ata_name, b.status, a.ata_level, case when  (b.is_ssi = 1) then 1 when (b.is_ssi = 0 and b.is_ana = 1) then 2 else 0 end is_ssi,b.ssi_id");
			sb.append("          from com_ata a, s_main b");
			sb.append("         where a.ata_id = b.ata_id");
			sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.STRUCTURE_CODE + "', a.ata_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("			  and (b.is_ssi = 1 or b.is_ana = 1)");
			sb.append("        union");
			sb.append("        select null ata_id,");
			sb.append("               c.add_code ata_code,");
			sb.append("               c.add_name ata_name,");
			sb.append("               c.status,");
			sb.append("               (select d.ata_level + 1");
			sb.append("                  from com_ata d");
			sb.append("                 where d.ata_id = c.parent_ata_id) ata_level,");
			sb.append("               case when (c.is_ssi = 1) then 1 when (c.is_ssi = 0 and c.is_ana = 1) then 2 else 0 end is_ssi,");
			sb.append("				  c.ssi_id");
			sb.append("          from s_main c");
			sb.append("         where c.is_add = 1");
			sb.append("           and c.add_user = '" + user.getUserId() + "'");
			sb.append("           and c.model_series_id = '" + modelSeriesId + "'");
			sb.append("			  and (c.is_ssi = 1 or c.is_ana = 1)");
			sb.append("           and c.valid_flag = " + ComacConstants.VALIDFLAG_YES + ")");
			sb.append(" order by ata_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}	
}
