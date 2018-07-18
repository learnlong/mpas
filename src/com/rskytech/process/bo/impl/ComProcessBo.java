package com.rskytech.process.bo.impl;

import java.util.Date;
import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComProcess;
import com.rskytech.pojo.ComProcessDetail;
import com.rskytech.process.bo.IComProcessBo;
import com.rskytech.process.dao.IComProcessDao;

@SuppressWarnings("rawtypes")
public class ComProcessBo extends BaseBO implements IComProcessBo {
	private IComProcessDao comProcessDao;

	@Override
	public List getAnalysisTypeByuserId(String modelSeriesId, String userId) {
		if(userId == null || "".equals(userId)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct aa.authority_type");
		sb.append("  from (select distinct a.profession_id, a.authority_type");
		sb.append("          from com_authority a, com_user_authority b");
		sb.append("         where a.authority_id = b.authority_id");
		sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("           and b.user_id = '" + userId + "') aa,");
		sb.append("       (select distinct m.profession_id");
		sb.append("          from com_user_position m");
		sb.append("         where m.user_id = '" + userId + "'");
		sb.append("           and m.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and (m.position_id = '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "' or m.profession_id = '" + ComacConstants.PROEFSSION_ID_ADMIN + "')");
		sb.append("        union");
		sb.append("        select n.profession_id");
		sb.append("          from com_profession_user n");
		sb.append("         where n.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and n.user_id = '" + userId + "'");
		sb.append("           and n.profession_id = '" + ComacConstants.PROEFSSION_ID_ADMIN + "') mm");
		sb.append(" where aa.profession_id = mm.profession_id");
		return this.comProcessDao.executeQueryBySql(sb.toString());
	}

	@Override
	public List getProfessionForAnalysisByuserId(String modelSeriesId, String analysisType, String userId) {
		if(analysisType == null || "".equals(analysisType) || userId == null || "".equals(userId)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct c.profession_id, c.profession_code, c.profession_name");
		sb.append("  from com_authority a, com_user_authority b, com_profession c");
		sb.append(" where a.profession_id = c.profession_id");
		sb.append("   and a.authority_id = b.authority_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("   and a.authority_type = '" + analysisType + "'");
		sb.append("   and b.user_id = '" + userId + "'");
		sb.append("  order by c.profession_code");
		return this.comProcessDao.executeQueryBySql(sb.toString());
	}

	@Override
	public List getCheckUserByProfessionId(String professionId) {
		if(professionId == null || "".equals(professionId)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.PROEFSSION_ID_ADMIN.equals(professionId)){
			sb.append("select distinct n.user_id, n.user_name, n.user_code");
			sb.append("  from com_profession_user m, com_user n");
			sb.append(" where m.user_id = n.user_id");
			sb.append("   and m.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and n.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and m.profession_id = '" + ComacConstants.PROEFSSION_ID_ADMIN + "'");
			sb.append("   and m.user_id <> '" + ComacConstants.USER_ID_ADMIN + "'");
			sb.append(" order by n.user_code");
		} else {
			sb.append("select distinct b.user_id, b.user_name, b.user_code");
			sb.append("  from com_user_position a, com_user b");
			sb.append(" where a.user_id = b.user_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.position_id = '" + ComacConstants.POSITION_ID_PROFESSION_ENGINEER + "'");
			sb.append("   and a.profession_id = '" + professionId + "'");
			sb.append(" order by b.user_code");
		}
		return this.comProcessDao.executeQueryBySql(sb.toString());
	}

	@Override
	public Page getAnalysisOver(Page page, String modelSeriesId, String analysisType, String userId) {
		StringBuffer sb = new StringBuffer();
		if(analysisType == null){
			return null;
		} else if (ComacConstants.SYSTEM_CODE.equals(analysisType)) {
			sb.append("select a.msi_id, b.ata_code, b.ata_name, a.status, to_char(a.modify_date,'yyyy-mm-dd hh24:mm:ss') modify_date, b.ata_id");
			sb.append("  from m_main a, com_ata b");
			sb.append(" where a.ata_id = b.ata_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth('" + modelSeriesId + "', '" + analysisType + "', a.ata_id, '" + userId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append("   and a.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "'");
			sb.append(" order by b.ata_code");
		} else if (ComacConstants.STRUCTURE_CODE.equals(analysisType)) {
			sb.append("select ssi_id, ata_code, ata_name, status, modify_date, ata_id, is_ssi params");
			sb.append("  from (select a.ssi_id, b.ata_code, b.ata_name, a.status, to_char(a.modify_date,'yyyy-mm-dd hh24:mm:ss') modify_date, b.ata_id,");
			sb.append("				  case when (a.is_ssi = 1) then 1 when (a.is_ssi = 0 and a.is_ana = 1) then 2 else 0 end is_ssi");
			sb.append("          from s_main a, com_ata b");
			sb.append("         where a.ata_id = b.ata_id");
			sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and fun_is_owner_auth('" + modelSeriesId + "', '" + analysisType + "', a.ata_id, '" + userId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append("           and a.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "'");
			sb.append("        union");
			sb.append("        select m.ssi_id, m.add_code ata_code, m.add_name ata_name, m.status, to_char(m.modify_date,'yyyy-mm-dd hh24:mm:ss') modify_date, m.ata_id,");
			sb.append("				  case when (m.is_ssi = 1) then 1 when (m.is_ssi = 0 and m.is_ana = 1) then 2 else 0 end is_ssi");
			sb.append("          from s_main m");
			sb.append("         where m.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and m.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and m.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "'");
			sb.append("           and m.add_user = '" + userId + "')");
			sb.append(" order by ata_code");
		} else if (ComacConstants.ZONAL_CODE.equals(analysisType)) {
			sb.append("select a.za_id, b.area_code, b.area_name, a.status, to_char(a.modify_date,'yyyy-mm-dd hh24:mm:ss') modify_date, b.area_id");
			sb.append("  from za_main a, com_area b");
			sb.append(" where a.area_id = b.area_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and fun_is_owner_auth('" + modelSeriesId + "', '" + analysisType + "', a.area_id, '" + userId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
			sb.append("   and a.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "'");
			sb.append(" order by b.area_code");
		} else if (ComacConstants.LHIRF_CODE.equals(analysisType)) {
			sb.append("select a.hsi_id, a.hsi_code, a.hsi_name, a.status, to_char(a.modify_date,'yyyy-mm-dd hh24:mm:ss') modify_date, a.area_id");
			sb.append("  from lh_main a");
			sb.append(" where a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and a.ana_user = '" + userId + "'");
			sb.append("   and a.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "'");
			sb.append(" order by a.hsi_code");
		} else {
			return null;
		}
		return this.comProcessDao.findBySql(page, sb.toString(), null);
	}

	@Override
	public boolean startProcess(ComModelSeries comModelSeries,
			String analysisType, String checkUser, String checkOpinion,
			String choosedIds, String curUserId) {
		//1.保存流程主表
		ComProcess comProcess = new ComProcess();
		comProcess.setComModelSeries(comModelSeries);
		comProcess.setLaunchUser(curUserId);
		comProcess.setLaunchOpinion(checkOpinion);
		comProcess.setCheckUser(checkUser);
		comProcess.setProcessStatus(ComacConstants.PROCESS_STATUS_WAIT_CHECK);
		comProcess.setValidFlag(ComacConstants.VALIDFLAG_YES);
		comProcess.setCreateUser(curUserId);
		comProcess.setCreateDate(BasicTypeUtils.getCurrentDateforSQL());
		comProcess.setModifyUser(curUserId);
		comProcess.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
		comProcess.setAnalysisType(analysisType);
		this.comProcessDao.save(comProcess);
		String[] mainIds = choosedIds.split(",");
		//2.保存流程字表
		for(int i = 0; i < mainIds.length; i++){
			ComProcessDetail comProcessDetail = new ComProcessDetail();
			comProcessDetail.setComProcess(comProcess);
			comProcessDetail.setAnalysisType(analysisType);
			comProcessDetail.setMainId(mainIds[i]);
			comProcessDetail.setIsOk(ComacConstants.PROCESS_CHECK_STATUS_NULL);
			comProcessDetail.setValidFlag(ComacConstants.VALIDFLAG_YES);
			comProcessDetail.setCreateUser(curUserId);
			comProcessDetail.setCreateDate(BasicTypeUtils.getCurrentDateforSQL());
			comProcessDetail.setModifyUser(curUserId);
			comProcessDetail.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
			this.comProcessDao.save(comProcessDetail);
		}
		//3.修改main表状态
		String inStr = "'" + choosedIds.replaceAll(",", "','") + "'";
		StringBuffer sb = new StringBuffer();
		if(ComacConstants.SYSTEM_CODE.equals(analysisType)){
			sb.append("update m_main");
			sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_HOLD + "'");
			sb.append(" where msi_id in (" + inStr + ")");
		} else if (ComacConstants.STRUCTURE_CODE.equals(analysisType)) {
			sb.append("update s_main");
			sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_HOLD + "'");
			sb.append(" where ssi_id in (" + inStr + ")");
		} else if (ComacConstants.ZONAL_CODE.equals(analysisType)) {
			sb.append("update za_main");
			sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_HOLD + "'");
			sb.append(" where za_id in (" + inStr + ")");
		} else if (ComacConstants.LHIRF_CODE.equals(analysisType)) {
			sb.append("update lh_main");
			sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_HOLD + "'");
			sb.append(" where hsi_id in (" + inStr + ")");
		}
		this.comProcessDao.executeBySql(sb.toString());
		return true;
	}

	@Override
	public Page loadCheckProcess(Page page, String modelSeriesId,
			String analysisType, String processStatus, String launchUserName,
			String fromDate, String toDate, String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select process_id,");
		sb.append("       analysis_type,");
		sb.append("       process_status,");
		sb.append("       user_name,");
		sb.append("       launch_date,");
		sb.append("       launch_opinion,");
		sb.append("       ataorareacode");
		sb.append("  from (select a.process_id,");
		sb.append("               a.analysis_type,");
		sb.append("               a.process_status,");
		sb.append("               b.user_name,");
		sb.append("               to_char(a.create_date, 'yyyy-mm-dd hh24:mi:ss') launch_date,");
		sb.append("               a.launch_opinion,");
		sb.append("               case");
		sb.append("                 when a.process_status = '" + ComacConstants.PROCESS_STATUS_CHECKING + "' then");
		sb.append("                  1");
		sb.append("                 when a.process_status = '" + ComacConstants.PROCESS_STATUS_WAIT_CHECK + "' then");
		sb.append("                  2");
		sb.append("                 else");
		sb.append("                  3");
		sb.append("               end shownum,");
		sb.append("               a.create_date,");
		sb.append("               fun_getprocess_ataorareacod(a.model_series_id, a.process_id, a.analysis_type) ataorareacode");
		sb.append("          from com_process a, com_user b");
		sb.append("         where a.launch_user = b.user_id");
		sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("           and a.check_user = '" + curUserId + "'");
		if(analysisType != null && !"".equals(analysisType)){
			sb.append("           and a.analysis_type = '" + analysisType + "'");
		}
		if(processStatus != null && !"".equals(processStatus)){
			sb.append("           and a.process_status = '" + processStatus + "'");
		}
		if(launchUserName != null && !"".equals(launchUserName)){
			sb.append("           and b.user_name like '%" + launchUserName + "%'");
		}
		if(fromDate != null && !"".equals(fromDate)){
			if(fromDate.length() > 10){
				fromDate = fromDate.substring(0, 10);
				sb.append("           and a.create_date >= to_date('" + fromDate + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			}
		}
		if(toDate != null && !"".equals(toDate)){
			if(toDate.length() > 10){
				toDate = toDate.substring(0, 10);
				sb.append("           and a.create_date <= to_date('" + toDate + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			}
		}
		sb.append("    )");
		sb.append(" order by shownum asc, create_date desc");
		return this.comProcessDao.findBySql(page, sb.toString(), null);
	}

	@Override
	public boolean batchCheckPass(ComModelSeries comModelSeries,
			String choosedIds, String curUserId) {
		if(choosedIds == null || "".equals(choosedIds)){
			return false;
		}
		String[] processIds = choosedIds.split(",");
		for(String processId : processIds){
			ComProcess comProcess = (ComProcess) this.loadById(ComProcess.class, processId);
			StringBuffer sb = null;
			//1.修改明细
			sb = new StringBuffer();
			sb.append("update com_process_detail");
			sb.append("   set is_ok         = " + ComacConstants.PROCESS_CHECK_STATUS_YES + ",");
			sb.append("       check_opinion = '" + ComacConstants.PROCESS_CHECK_STATUS_YES_SHOW + "',");
			sb.append("       modify_date   = sysdate,");
			sb.append("       modify_user   = '" + curUserId + "'");
			sb.append(" where process_id = '" + comProcess.getProcessId() + "'");
			this.comProcessDao.executeBySql(sb.toString());
			//2.修改分析
			sb = new StringBuffer();
			if(ComacConstants.SYSTEM_CODE.equals(comProcess.getAnalysisType())){
				sb.append("update m_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_APPROVED + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where msi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.STRUCTURE_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update s_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_APPROVED + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where ssi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.ZONAL_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update za_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_APPROVED + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where za_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.LHIRF_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update lh_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_APPROVED + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where hsi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			}
			this.comProcessDao.executeBySql(sb.toString());
			//3.修改主流程数据
			comProcess.setProcessStatus(ComacConstants.PROCESS_STATUS_FINISH_CHECK);
			comProcess.setModifyDate(new Date());
			comProcess.setModifyUser(curUserId);
			this.save(comProcess);
		}
		return true;
	}

	@Override
	public boolean batchCheckNotPass(ComModelSeries comModelSeries,
			String choosedIds, String curUserId) {
		if(choosedIds == null || "".equals(choosedIds)){
			return false;
		}
		String[] processIds = choosedIds.split(",");
		for(String processId : processIds){
			ComProcess comProcess = (ComProcess) this.loadById(ComProcess.class, processId);
			StringBuffer sb = null;
			//1.修改明细
			sb = new StringBuffer();
			sb.append("update com_process_detail");
			sb.append("   set is_ok         = " + ComacConstants.PROCESS_CHECK_STATUS_NO + ",");
			sb.append("       check_opinion = '" + ComacConstants.PROCESS_CHECK_STATUS_NO_SHOW + "',");
			sb.append("       modify_date   = sysdate,");
			sb.append("       modify_user   = '" + curUserId + "'");
			sb.append(" where process_id = '" + comProcess.getProcessId() + "'");
			this.comProcessDao.executeBySql(sb.toString());
			//2.修改分析
			sb = new StringBuffer();
			if(ComacConstants.SYSTEM_CODE.equals(comProcess.getAnalysisType())){
				sb.append("update m_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where msi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.STRUCTURE_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update s_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where ssi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.ZONAL_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update za_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where za_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.LHIRF_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update lh_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where hsi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			}
			this.comProcessDao.executeBySql(sb.toString());
			//3.修改主流程数据
			comProcess.setProcessStatus(ComacConstants.PROCESS_STATUS_FINISH_CHECK);
			comProcess.setModifyDate(new Date());
			comProcess.setModifyUser(curUserId);
			this.save(comProcess);
		}
		return true;
	}

	@Override
	public boolean batchCheckCancel(ComModelSeries comModelSeries,
			String choosedIds, String curUserId) {
		if(choosedIds == null || "".equals(choosedIds)){
			return false;
		}
		String[] processIds = choosedIds.split(",");
		for(String processId : processIds){
			ComProcess comProcess = (ComProcess) this.loadById(ComProcess.class, processId);
			StringBuffer sb = null;
			//1.修改明细
			sb = new StringBuffer();
			sb.append("update com_process_detail");
			sb.append("   set is_ok         = " + ComacConstants.PROCESS_CHECK_STATUS_CANCEL + ",");
			sb.append("       check_opinion = '" + ComacConstants.PROCESS_CHECK_STATUS_CANCEL_SHOW + "',");
			sb.append("       modify_date   = sysdate,");
			sb.append("       modify_user   = '" + curUserId + "'");
			sb.append(" where process_id = '" + comProcess.getProcessId() + "'");
			this.comProcessDao.executeBySql(sb.toString());
			//2.修改分析
			sb = new StringBuffer();
			if(ComacConstants.SYSTEM_CODE.equals(comProcess.getAnalysisType())){
				sb.append("update m_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where msi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.STRUCTURE_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update s_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where ssi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.ZONAL_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update za_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where za_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			} else if (ComacConstants.LHIRF_CODE.equals(comProcess.getAnalysisType())) {
				sb.append("update lh_main");
				sb.append("   set status = '" + ComacConstants.ANALYZE_STATUS_MAINTAINOK + "',");
				sb.append("       modify_date = sysdate,");
				sb.append("       modify_user = '" + curUserId + "'");
				sb.append(" where hsi_id in");
				sb.append("       (select main_id");
				sb.append("          from com_process_detail");
				sb.append("         where process_id = '" + comProcess.getProcessId() + "')");
			}
			this.comProcessDao.executeBySql(sb.toString());
			//3.修改主流程数据
			comProcess.setProcessStatus(ComacConstants.PROCESS_STATUS_CANCEL_CHECK);
			comProcess.setModifyDate(new Date());
			comProcess.setModifyUser(curUserId);
			this.save(comProcess);
		}
		return true;
	}

	@Override
	public List getProcessDetail(String modelSeriesId, String processId, String analysisType) {
		StringBuffer sb = new StringBuffer();
		if(processId == null || "".equals(processId) || analysisType == null || "".equals(analysisType)){
			return null;
		} else if(ComacConstants.SYSTEM_CODE.equals(analysisType)){
			sb.append("select a.detail_id,");
			sb.append("       a.analysis_type,");
			sb.append("       a.is_ok,");
			sb.append("       a.check_opinion,");
			sb.append("       b.msi_id,");
			sb.append("       c.ata_code,");
			sb.append("       c.ata_name,");
			sb.append("       b.msi_id || ',' || c.ata_id urlParam");
			sb.append("  from com_process_detail a, m_main b, com_ata c");
			sb.append(" where a.main_id = b.msi_id");
			sb.append("   and b.ata_id = c.ata_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and c.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and a.process_id = '" + processId + "'");
			sb.append(" order by c.ata_code");
		} else if (ComacConstants.STRUCTURE_CODE.equals(analysisType)) {
			sb.append("select detail_id,");
			sb.append("       analysis_type,");
			sb.append("       is_ok,");
			sb.append("       check_opinion,");
			sb.append("       ssi_id,");
			sb.append("       ata_code,");
			sb.append("       ata_name,");
			sb.append("       urlParam,");
			sb.append("       qcode");
			sb.append("  from (select a.detail_id,");
			sb.append("               a.analysis_type,");
			sb.append("               a.is_ok,");
			sb.append("               a.check_opinion,");
			sb.append("               b.ssi_id,");
			sb.append("               b.add_code      ata_code,");
			sb.append("               b.add_name      ata_name,");
			sb.append("               b.is_ssi || ',' || b.ssi_id urlParam,");
			sb.append("               case when (b.is_ssi = 0 and b.is_ana = 1) then 'Q'||b.add_code else b.add_code end qcode");
			sb.append("          from com_process_detail a, s_main b");
			sb.append("         where a.main_id = b.ssi_id");
			sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and b.is_add = 1");
			sb.append("           and a.process_id = '" + processId + "'");
			sb.append("        union");
			sb.append("        select a.detail_id,");
			sb.append("               a.analysis_type,");
			sb.append("               a.is_ok,");
			sb.append("               a.check_opinion,");
			sb.append("               b.ssi_id,");
			sb.append("               c.ata_code,");
			sb.append("               c.ata_name,");
			sb.append("               b.is_ssi || ',' || b.ssi_id urlParam,");
			sb.append("               case when (b.is_ssi = 0 and b.is_ana = 1) then 'Q'||c.ata_code else c.ata_code end qcode");
			sb.append("          from com_process_detail a, s_main b, com_ata c");
			sb.append("         where a.main_id = b.ssi_id");
			sb.append("           and b.ata_id = c.ata_id");
			sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("           and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and c.model_series_id = '" + modelSeriesId + "'");
			sb.append("           and (b.is_add is null or b.is_add = '0')");
			sb.append("           and a.process_id = '" + processId + "')");
			sb.append(" order by ata_code");
		} else if (ComacConstants.ZONAL_CODE.equals(analysisType)) {
			sb.append("select a.detail_id,");
			sb.append("       a.analysis_type,");
			sb.append("       a.is_ok,");
			sb.append("       a.check_opinion,");
			sb.append("       b.za_id,");
			sb.append("       c.area_code,");
			sb.append("       c.area_name,");
			sb.append("       b.area_id urlParam");
			sb.append("  from com_process_detail a, za_main b, com_area c");
			sb.append(" where a.main_id = b.za_id");
			sb.append("   and b.area_id = c.area_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and c.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and a.process_id = '" + processId + "'");
			sb.append(" order by c.area_code");
		} else if (ComacConstants.LHIRF_CODE.equals(analysisType)) {
			sb.append("select a.detail_id,");
			sb.append("       a.analysis_type,");
			sb.append("       a.is_ok,");
			sb.append("       a.check_opinion,");
			sb.append("       b.hsi_id,");
			sb.append("       b.hsi_code,");
			sb.append("       b.hsi_name,");
			sb.append("       b.ref_hsi_code || ',' || b.hsi_id urlParam");
			sb.append("  from com_process_detail a, lh_main b");
			sb.append(" where a.main_id = b.hsi_id");
			sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
			sb.append("   and b.model_series_id = '" + modelSeriesId + "'");
			sb.append("   and a.process_id = '" + processId + "'");
			sb.append(" order by b.hsi_code");
		} else {
			return null;
		}
		return this.comProcessDao.executeQueryBySql(sb.toString());
	}

	@Override
	public boolean singleCheckProcessDetail(String modelSeriesId,
			String processId, String analysisType, String isOkType,
			String isOkCheckpinion, String choosedIds, String curUserId) {
		if(choosedIds == null || "".equals(choosedIds)){
			return false;
		}
		String sqlChoosedIds = "'" + choosedIds.replaceAll(",", "','") + "'";
		StringBuffer sb = null;
		//1.更新流程明细信息
		sb = new StringBuffer();
		sb.append("update com_process_detail");
		sb.append("   set is_ok         = " + isOkType + ",");
		sb.append("       check_opinion = '" + isOkCheckpinion + "',");
		sb.append("       modify_user   = '" + curUserId + "',");
		sb.append("       modify_date   = sysdate");
		sb.append(" where detail_id in (" + sqlChoosedIds + ")");
		this.comProcessDao.executeBySql(sb.toString());
		//2.修改分析
		String tmpAnalysysType = ComacConstants.ANALYZE_STATUS_APPROVED;
		//审批部通过，和取消审核时候，分析状态改为“分析完成”
		if(ComacConstants.PROCESS_CHECK_STATUS_NO == Integer.parseInt(isOkType) || ComacConstants.PROCESS_CHECK_STATUS_CANCEL == Integer.parseInt(isOkType)){
			tmpAnalysysType = ComacConstants.ANALYZE_STATUS_MAINTAINOK;
		}
		sb = new StringBuffer();
		if(ComacConstants.SYSTEM_CODE.equals(analysisType)){
			sb.append("update m_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where msi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where detail_id in");
			sb.append("                         (" + sqlChoosedIds + "))");
		} else if (ComacConstants.STRUCTURE_CODE.equals(analysisType)) {
			sb.append("update s_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where ssi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where detail_id in");
			sb.append("                         (" + sqlChoosedIds + "))");
		} else if (ComacConstants.ZONAL_CODE.equals(analysisType)) {
			sb.append("update za_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where za_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where detail_id in");
			sb.append("                         (" + sqlChoosedIds + "))");
		} else if (ComacConstants.LHIRF_CODE.equals(analysisType)) {
			sb.append("update lh_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where hsi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where detail_id in");
			sb.append("                         (" + sqlChoosedIds + "))");
		}
		this.comProcessDao.executeBySql(sb.toString());
		//3.修改主流程数据
		sb = new StringBuffer();
		sb.append("select a.is_ok, count(a.is_ok)");
		sb.append("  from com_process_detail a");
		sb.append(" where a.process_id = '" + processId + "'");
		sb.append(" group by a.is_ok");
		sb.append(" order by a.is_ok");
		List lst = this.comProcessDao.executeQueryBySql(sb.toString());
		String tmpAnalysisStatus = ComacConstants.PROCESS_STATUS_FINISH_CHECK;//审核完成
		if (lst != null) {
			for (Object obj : lst) {
				Object[] objs=(Object[])obj;
				if(ComacConstants.PROCESS_CHECK_STATUS_NULL == Integer.parseInt(String.valueOf(objs[0]))){
					tmpAnalysisStatus = ComacConstants.PROCESS_STATUS_CHECKING;//审核中
					break;
				}
			}
			if(lst.size() == 1){
				Object[] objs=(Object[])lst.get(0);
				if(ComacConstants.PROCESS_CHECK_STATUS_CANCEL == Integer.parseInt(String.valueOf(objs[0]))){
					tmpAnalysisStatus = ComacConstants.PROCESS_STATUS_CANCEL_CHECK;//取消审核
				}
			}
		}
		ComProcess comProcess = (ComProcess) this.loadById(ComProcess.class, processId);
		comProcess.setProcessStatus(tmpAnalysisStatus);
		comProcess.setModifyDate(new Date());
		comProcess.setModifyUser(curUserId);
		this.save(comProcess);
		return true;
	}

	@Override
	public boolean allCheckProcessDetail(String modelSeriesId,
			String processId, String analysisType, String isOkType,
			String isOkCheckpinion, String curUserId) {
		StringBuffer sb = null;
		//1.更新流程明细信息
		sb = new StringBuffer();
		sb.append("update com_process_detail");
		sb.append("   set is_ok         = " + isOkType + ",");
		sb.append("       check_opinion = '" + isOkCheckpinion + "',");
		sb.append("       modify_user   = '" + curUserId + "',");
		sb.append("       modify_date   = sysdate");
		sb.append(" where process_id = '" + processId + "'");
		this.comProcessDao.executeBySql(sb.toString());
		//2.修改分析
		String tmpAnalysysType = ComacConstants.ANALYZE_STATUS_APPROVED;
		//审批部通过，和取消审核时候，分析状态改为“分析完成”
		if(ComacConstants.PROCESS_CHECK_STATUS_NO == Integer.parseInt(isOkType) || ComacConstants.PROCESS_CHECK_STATUS_CANCEL == Integer.parseInt(isOkType)){
			tmpAnalysysType = ComacConstants.ANALYZE_STATUS_MAINTAINOK;
		}
		sb = new StringBuffer();
		if(ComacConstants.SYSTEM_CODE.equals(analysisType)){
			sb.append("update m_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where msi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where process_id = '" + processId + "')");
		} else if (ComacConstants.STRUCTURE_CODE.equals(analysisType)) {
			sb.append("update s_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where ssi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where process_id = '" + processId + "')");
		} else if (ComacConstants.ZONAL_CODE.equals(analysisType)) {
			sb.append("update za_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where za_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where process_id = '" + processId + "')");
		} else if (ComacConstants.LHIRF_CODE.equals(analysisType)) {
			sb.append("update lh_main");
			sb.append("   set status = '" + tmpAnalysysType + "', ");
			sb.append("       modify_user = '" + curUserId + "', ");
			sb.append("       modify_date = sysdate");
			sb.append(" where hsi_id in (select main_id");
			sb.append("                    from com_process_detail");
			sb.append("                   where process_id = '" + processId + "')");
		}
		this.comProcessDao.executeBySql(sb.toString());
		//3.修改主流程数据
		String tmpAnalysisStatus = ComacConstants.PROCESS_STATUS_FINISH_CHECK;//审核完成
		if(ComacConstants.PROCESS_CHECK_STATUS_CANCEL == Integer.parseInt(isOkType)){
			tmpAnalysisStatus = ComacConstants.PROCESS_STATUS_CANCEL_CHECK;//取消审核
		}
		ComProcess comProcess = (ComProcess) this.loadById(ComProcess.class, processId);
		comProcess.setProcessStatus(tmpAnalysisStatus);
		comProcess.setModifyDate(new Date());
		comProcess.setModifyUser(curUserId);
		this.save(comProcess);
		return true;
	}

	@Override
	public Page loadProcessForQuery(Page page, String modelSeriesId,
			String analysisType, String processStatus, String launchUserName,
			String checkUserName, String fromDate, String toDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.process_id,");
		sb.append("       a.analysis_type,");
		sb.append("       a.process_status,");
		sb.append("       b.user_name launch_user,");
		sb.append("       to_char(a.create_date, 'yyyy-mm-dd hh24:mi:ss') launch_date,");
		sb.append("       c.user_name check_user,");
		sb.append("       to_char(a.modify_date, 'yyyy-mm-dd hh24:mi:ss') check_date,");
		sb.append("       a.launch_opinion,");
		sb.append("       a.create_date,");
		sb.append("       fun_getprocess_ataorareacod(a.model_series_id, a.process_id, a.analysis_type) ataorareacode");
		sb.append("  from com_process a, com_user b, com_user c");
		sb.append(" where a.launch_user = b.user_id");
		sb.append("   and a.check_user = c.user_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		if(analysisType != null && !"".equals(analysisType)){
			sb.append("   and a.analysis_type = '" + analysisType + "'");
		}
		if(processStatus != null && !"".equals(processStatus)){
			sb.append("   and a.process_status = '" + processStatus + "'");
		}
		if(fromDate != null && !"".equals(fromDate)){
			if(fromDate.length() > 10){
				fromDate = fromDate.substring(0, 10);
				sb.append("   and a.create_date >= to_date('" + fromDate + " 00:00:00', 'yyyy-mm-dd hh24:mi:ss')");
			}
		}
		if(toDate != null && !"".equals(toDate)){
			if(toDate.length() > 10){
				toDate = toDate.substring(0, 10);
				sb.append("   and a.create_date <= to_date('" + toDate + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
			}
		}
		if(launchUserName != null && !"".equals(launchUserName)){
			sb.append("   and b.user_name like '%" + launchUserName + "%'");
		}
		if(checkUserName != null && !"".equals(checkUserName)){
			sb.append("   and c.user_name like '%" + checkUserName + "%'");
		}
		sb.append(" order by a.create_date desc");
		return this.comProcessDao.findBySql(page, sb.toString(), null);
	}

	@Override
	public Page loadCheckProcessForHomePage(Page page, String modelSeriesId,
			String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.process_id,");
		sb.append("       a.analysis_type,");
		sb.append("       a.process_status,");
		sb.append("       b.user_name,");
		sb.append("       to_char(a.create_date, 'yyyy-mm-dd hh24:mi:ss') launch_date,");
		sb.append("       a.launch_opinion,");
		sb.append("       a.create_date,");
		sb.append("       fun_getprocess_ataorareacod(a.model_series_id, a.process_id, a.analysis_type) ataorareacode");
		sb.append("  from com_process a, com_user b");
		sb.append(" where a.launch_user = b.user_id");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("   and a.check_user = '" + curUserId + "'");
		sb.append("   and (a.process_status = '" + ComacConstants.PROCESS_STATUS_WAIT_CHECK + "' or a.process_status = '" + ComacConstants.PROCESS_STATUS_CHECKING + "')");
		sb.append(" order by create_date desc");
		return this.comProcessDao.findBySql(page, sb.toString(), null);
	}

	public IComProcessDao getComProcessDao() {
		return comProcessDao;
	}

	public void setComProcessDao(IComProcessDao comProcessDao) {
		this.comProcessDao = comProcessDao;
	}
	
	
}
