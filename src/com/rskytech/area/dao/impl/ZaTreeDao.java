package com.rskytech.area.dao.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZaTreeDao;
import com.rskytech.pojo.ComUser;

@SuppressWarnings( "rawtypes" )
public class ZaTreeDao extends BaseDAO implements IZaTreeDao {
	
	/**
	 * 查询指定区域节点的下级子节点
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @param Integer parentAreaId 当前区域节点，即父节点
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchSubAreaTree(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
			sb.append("select a.area_id,");
			sb.append("       a.area_code,");
			sb.append("       a.area_name,");
			sb.append("       a.area_level,");
			sb.append("       b.status,");
			sb.append("       fun_is_owner_auth(a.model_series_id, '" + ComacConstants.ZONAL_CODE + "', a.area_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') quanxian,");
			sb.append("       (select to_char(count(1))");
			sb.append("          from com_area c");
			sb.append("         where c.parent_area_id = a.area_id");
			sb.append("           and c.valid_flag = 1) isChild,");
			sb.append("		  b.za_id");
			sb.append("  from com_area a, za_main b");
			sb.append(" where a.area_id = b.area_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			if("0".equals(parentAreaId)){
				sb.append("   and a.parent_area_id is null");
			}else {
				sb.append("   and a.parent_area_id = '" + parentAreaId + "'");
			}
			sb.append(" order by a.area_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}
	
	/**
	 * 查询该用户是否有需要维护的区域
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.REPORT.endsWith(searchType)){
			sb.append("select a.area_id, a.area_code, a.area_name, b.status, b.za_id");
			sb.append("  from com_area a, za_main b");
			sb.append(" where a.area_id = b.area_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.ZONAL_CODE + "', a.area_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append(" order by a.area_code");
			return this.executeQueryBySql(sb.toString());
		}else if(ComacConstants.ANALYSIS.endsWith(searchType)){
			sb.append("select a.area_id, a.area_code, a.area_name, b.status, b.za_id");
			sb.append("  from com_area a, za_main b");
			sb.append(" where a.area_id = b.area_id(+)");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id(+) = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.ZONAL_CODE + "', a.area_id, '" + user.getUserId() + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append(" order by a.area_code");
			return this.executeQueryBySql(sb.toString());
		}
		return null;
	}
}
