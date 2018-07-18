package com.rskytech.user.bo.impl;

import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComProfession;
import com.rskytech.user.bo.IComProfessionBo;
import com.rskytech.user.dao.IComProfessionDao;

@SuppressWarnings("rawtypes")
public class ComProfessionBo extends BaseBO implements IComProfessionBo {
	
	private IComProfessionDao comProfessionDao;
	
	@Override
	public Page showProfessionList(Page page, String professionCode, String professionName, boolean includeAdminFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append("select profession_id, profession_code, profession_name, valid_flag");
		sb.append("  from COM_PROFESSION");
		sb.append(" where valid_flag = " + ComacConstants.VALIDFLAG_YES);
		if(!includeAdminFlag){
			sb.append("   and profession_id <> '" + ComacConstants.PROEFSSION_ID_ADMIN + "'");
		}
		if(professionCode != null && !"".equals(professionCode)){
			sb.append("   and profession_code like '%" + professionCode + "%'");
		}
		if(professionName != null && !"".equals(professionName)){
			sb.append("   and profession_name like '%" + professionName + "%'");
		}
		sb.append(" order by profession_code");
		return this.dao.findBySql(page, sb.toString(), null);
	}

	public IComProfessionDao getComProfessionDao() {
		return comProfessionDao;
	}

	public void setComProfessionDao(IComProfessionDao comProfessionDao) {
		this.comProfessionDao = comProfessionDao;
	}

	/**
	 * 管理员得到所有可以维护的专业室
	 */
	@Override
	public List getAllProsession(String professionCode, String professionName, boolean includeAdminFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append("select profession_id, profession_code, profession_name, valid_flag");
		sb.append("  from COM_PROFESSION");
		sb.append(" where valid_flag = " + ComacConstants.VALIDFLAG_YES);
		if(!includeAdminFlag){
			sb.append("   and profession_id <> '" + ComacConstants.PROEFSSION_ID_ADMIN + "'");
		}
		if(professionCode != null && !"".equals(professionCode)){
			sb.append("   and profession_code like '%" + professionCode + "%'");
		}
		if(professionName != null && !"".equals(professionName)){
			sb.append("   and profession_name like '%" + professionName + "%'");
		}
		sb.append(" order by profession_code");
		return this.comProfessionDao.executeQueryBySql(sb.toString());
	}

	/**
	 * 管理员以外的人员得到可以维护的专业室
	 */
	@Override
	public List getProsessionByUserId(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.profession_id, a.profession_code, a.profession_name");
		sb.append("  from com_profession a, com_user_position b");
		sb.append(" where a.profession_id = b.profession_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.position_id = '" + ComacConstants.POSITION_ID_PROFESSION_ADMIN + "'");
		sb.append("   and b.user_id = '" + userId + "'");
		return this.comProfessionDao.executeQueryBySql(sb.toString());
	}

	/**
	 * 根据用户Id查询它所在的全部专业室(无权限区分)
	 */
	@Override
	public List getProsByUserId(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct a.profession_id, a.profession_code, a.profession_name");
		sb.append("  from com_profession a, com_user_position b");
		sb.append(" where a.profession_id = b.profession_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.user_id = '" + userId + "'");
		return this.comProfessionDao.executeQueryBySql(sb.toString());
	}

	/**
	 * 根据专业室id得到拥有的用户（admin不顯示）
	 */
	@Override
	public Page getUserByProfessonId(Page page, String professionId, String userCode, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.user_id,");
		sb.append("       a.user_code,");
		sb.append("       a.user_name,");
		sb.append("       fun_getposition(b.user_id, b.profession_id, '" + ComacConstants.POSITION_ID_PROFESSION_ADMIN + "') positions_admin,");
		sb.append("       fun_getposition(b.user_id, b.profession_id, '" + ComacConstants.POSITION_ID_PROFESSION_ENGINEER + "') positions_engineer,");
		sb.append("       fun_getposition(b.user_id, b.profession_id, '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') positions_analist");
		sb.append("  from com_user a, com_profession_user b");
		sb.append(" where a.user_id = b.user_id");
		sb.append("   and b.profession_id = '" + professionId + "'");
		sb.append("   and b.user_id <> '" + ComacConstants.USER_ID_ADMIN + "'");//admin不顯示
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		if(userCode != null && !"".equals(userCode)){
			sb.append("   and a.user_code like '%" + userCode + "%'");
		}
		if(userName != null && !"".equals(userName)){
			sb.append("   and a.user_name like '%" + userName + "%'");
		}
		return this.dao.findBySql(page, sb.toString(), null);
	}


	/**
	 * 根据专业室id得到不拥有的用户(admin不顯示)
	 */
	@Override
	public Page getOtherUserByProfessonId(Page page, String professionId, String userCode, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select user_id, user_code, user_name");
		sb.append("  from (select user_id, user_code, user_name");
		sb.append("          from com_user");
		sb.append("         where valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and user_id <> '" + ComacConstants.USER_ID_ADMIN + "'");//admin不顯示
		sb.append("         minus ");
		sb.append("        select a.user_id, a.user_code, a.user_name");
		sb.append("          from com_user a, com_profession_user b");
		sb.append("         where a.user_id = b.user_id");
		sb.append("           and b.profession_id = '" + professionId + "'");
		sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES + ")");
		sb.append(" where 0 = 0");
		if(userCode != null && !"".equals(userCode)){
			sb.append("   and user_code like '%" + userCode + "%'");
		}
		if(userName != null && !"".equals(userName)){
			sb.append("   and user_name like '%" + userName + "%'");
		}
		return this.dao.findBySql(page, sb.toString(), null);
	}

	/**
	 * 插入com_user_position表
	 */
	@Override
	public boolean insertUserPositionRel(String userId, String positionId, String professionId, String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into com_user_position");
		sb.append("  (user_id,");
		sb.append("   position_id,");
		sb.append("   profession_id,");
		sb.append("   valid_flag,");
		sb.append("   create_user,");
		sb.append("   create_date,");
		sb.append("   modify_user,");
		sb.append("   modify_date)");
		sb.append("values");
		sb.append("  ('" + userId + "',");
		sb.append("   '" + positionId + "',");
		sb.append("   '" + professionId + "',");
		sb.append("   1,");
		sb.append("   '" + curUserId + "',");
		sb.append("   sysdate,");
		sb.append("   '" + curUserId + "',");
		sb.append("   sysdate)");
		this.dao.executeBySql(sb.toString());
		return true;
	}

	/**
	 * 删除com_user_position关系
	 */
	@Override
	public boolean deleteUserPositionRel(String userId, String positionId, String professionId, String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from COM_USER_POSITION");
		sb.append(" where user_id = '" + userId + "'");
		sb.append("   and position_id = '" + positionId + "'");
		sb.append("   and profession_id = '" + professionId + "'");
		this.dao.executeBySql(sb.toString());
		return true;
	}

	/**
	 * 删除专业组已有的用户
	 */
	@Override
	public boolean delUserInProfession(String userId, String professionId, String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from COM_USER_POSITION");
		sb.append(" where user_id = '" + userId + "'");
		sb.append("   and profession_id = '" + professionId + "'");
		this.dao.executeBySql(sb.toString());
		sb = new StringBuffer();
		sb.append("delete from COM_PROFESSION_USER");
		sb.append(" where user_id = '" + userId + "'");
		sb.append("   and profession_id = '" + professionId + "'");
		this.dao.executeBySql(sb.toString());
		return true;
	}

	/**
	 * 给专业组天剑用户
	 */
	@Override
	public boolean addUserInProfession(String userId, String professionId, String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into com_profession_user");
		sb.append("  (profession_id,");
		sb.append("   user_id,");
		sb.append("   valid_flag,");
		sb.append("   create_user,");
		sb.append("   create_date,");
		sb.append("   modify_user,");
		sb.append("   modify_date)");
		sb.append("values");
		sb.append("  ('" + professionId + "',");
		sb.append("   '" + userId + "',");
		sb.append("   1,");
		sb.append("   '" + curUserId + "',");
		sb.append("   sysdate,");
		sb.append("   '" + curUserId + "',");
		sb.append("   sysdate)");
		this.dao.executeBySql(sb.toString());
		return true;
	}

	/**
	 * 判斷專業組是否存在
	 */
	@Override
	public boolean checkProfessionIsExist(String professionId, String professionCode) {
		ComProfession comProfession = (ComProfession) this.loadById(ComProfession.class, professionId);
		if (comProfession != null && comProfession.getProfessionId().equals(professionCode)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Page getUserByProfessonIdForAuth(Page page, String professionId,String userCode, String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.user_id,");
		sb.append("       a.user_code,");
		sb.append("       a.user_name,");
		sb.append("       fun_getpositionnames(b.user_id, b.profession_id) positionnames");
		sb.append("  from com_user a, com_profession_user b");
		sb.append(" where a.user_id = b.user_id");
		sb.append("   and b.profession_id = '" + professionId + "'");
		sb.append("   and b.user_id <> '" + ComacConstants.USER_ID_ADMIN + "'");//admin不顯示
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		if(userCode != null && !"".equals(userCode)){
			sb.append("   and a.user_code like '%" + userCode + "%'");
		}
		if(userName != null && !"".equals(userName)){
			sb.append("   and a.user_name like '%" + userName + "%'");
		}
		return this.dao.findBySql(page, sb.toString(), null);
	}

}
