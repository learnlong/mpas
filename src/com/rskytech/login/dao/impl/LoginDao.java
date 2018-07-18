package com.rskytech.login.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.login.dao.ILoginDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class LoginDao extends BaseDAO implements ILoginDao {

	@SuppressWarnings("unchecked")
	public List<ComModelSeries> searchModelSeries() throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComModelSeries.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getMenu(ComUser user, String modelSeriesId, String parentId) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		if(user.isAdmin()){
			sb.append("select menu_id, menu_name, action_url");
			sb.append("  from com_menu");
			sb.append(" where valid_flag = 1");
			if(parentId == null){
				sb.append("   and parent_id is null");
			}else{
				sb.append("   and parent_id = '" + parentId + "'");
			}
			sb.append(" order by menu_code");
		}else{
			sb.append("select menu_id, menu_name, action_url");
			sb.append("  from (select menu_id, menu_name, action_url, menu_code");
			sb.append("          from view_menu_role");
			sb.append("         where model_series_id = '" + modelSeriesId + "'");
			sb.append("           and user_id = '" + user.getUserId() + "'");
			if(parentId == null){
				sb.append("           and parent_id is null");
			}else {
				sb.append("           and parent_id = '" + parentId + "'");
			}
			if(user.isProfessionAdmin()){
				sb.append("        union");
				sb.append("        select menu_id, menu_name, action_url, menu_code");
				sb.append("          from view_menu_professionadmin");
				if(parentId == null){
					sb.append("         where parent_id is null");
				}else {
					sb.append("         where parent_id = '" + parentId + "'");
				}
			}
			sb.append("        union");
			sb.append("        select menu_id, menu_name, action_url, menu_code");
			sb.append("          from view_menu_general");
			if(parentId == null){
				sb.append("         where parent_id is null)");
			}else {
				sb.append("         where parent_id = '" + parentId + "')");
			}
			sb.append(" order by menu_code");
		}
		return this.executeQueryBySql(sb.toString());
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkSuperAdmin(String userId) throws BusinessException{
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1)");
		sb.append("  from com_profession a, com_profession_user b");
		sb.append(" where a.profession_id = b.profession_id");
		sb.append("   and b.profession_id = '" + ComacConstants.PROEFSSION_ID_ADMIN + "'");
		sb.append("   and b.user_id = '"+ userId + "'");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		List<Object> list = this.executeQueryBySql(sb.toString());
		if (list != null && list.size() > 0){
			Object obj = list.get(0);
			Integer s = Integer.valueOf(obj.toString());
			if (s == 0){
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean checkProfessionAdmin(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1)");
		sb.append("  from com_user_position a, com_profession b");
		sb.append(" where a.profession_id = b.profession_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.user_id = '"+ userId + "'");
		sb.append("   and a.position_id = '" + ComacConstants.POSITION_ID_PROFESSION_ADMIN + "'");
		List<Object> list = this.executeQueryBySql(sb.toString());
		if (list != null && list.size() > 0){
			Object obj = list.get(0);
			Integer s = Integer.valueOf(obj.toString());
			if (s == 0){
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPositionList(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct a.position_id, a.user_id");
		sb.append("  from com_user_position a, com_profession b");
		sb.append(" where a.profession_id = b.profession_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.user_id = '"+ userId + "'");
		return this.executeQueryBySql(sb.toString());
	}
	
}
