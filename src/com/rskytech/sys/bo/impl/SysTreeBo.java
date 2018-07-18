package com.rskytech.sys.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaTreeBO;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.MMain;
import com.rskytech.sys.bo.ISysTreeBo;
import com.rskytech.sys.dao.IMsiSelectDao;
import com.rskytech.sys.dao.ISysTreeDao;

public class SysTreeBo extends BaseBO implements ISysTreeBo {

	private ISysTreeDao sysTreeDao;
	private IZaTreeBO zaTreeBO;
	private IMsiSelectDao msiSelectDao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List searchSubAtaOrMsiTreeList(ComUser user, String parentAtaId, String modelSeriesId, String searchType) throws BusinessException{
		List list = this.sysTreeDao.searchSubAtaOrMsiTree(user, parentAtaId, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}

		HashMap<String, Object> jsonFeildList = null;
		List listJson = new ArrayList<HashMap>();
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			List<Object[]> li=this.msiSelectDao.getHighLevelByAta(obj[0].toString(), modelSeriesId);
			Object isMaintain=null;
			if(null!=li&&li.size()>0&&"1".equals(obj[7].toString())){//最高可管理层为自己的MSI才分析
				isMaintain=1;
			}
			
			String isMsi = (obj[6] == null ? "" : obj[6].toString());
			jsonFeildList.put("id", obj[0]);
			jsonFeildList.put("msiId", obj[1]);
			if(ComacConstants.CHOOSE.equals(searchType)){
				jsonFeildList.put("text", zaTreeBO.getTreeNodeStatus(obj[2], obj[3], obj[7], null));
				jsonFeildList.put("isMaintain", obj[7]);
			}else if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)){
				jsonFeildList.put("text", zaTreeBO.getTreeNodeStatus(obj[2], obj[3], isMaintain, obj[5]));
				jsonFeildList.put("isMaintain", isMaintain);
			}
			jsonFeildList.put("status", obj[5]);
			jsonFeildList.put("level", obj[4]);
			jsonFeildList.put("leaf", "0".equals(isMsi) ? true : false);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException{
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		List list = this.sysTreeDao.searchMyMaintain(user, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}
		
		List listJson = new ArrayList<HashMap>();
		HashMap<String, Object> jsonFeildList = null;
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			jsonFeildList.put("ataId", obj[0]);
			jsonFeildList.put("msiId", obj[5]);
			jsonFeildList.put("ataCode", obj[1]);
			jsonFeildList.put("status", zaTreeBO.getStatusNameByStatusCode( obj[4] == null ? "" : obj[4].toString()));
			jsonFeildList.put("ataName", obj[2]);
			String openStatus = "";
			if(obj[4] != null && (ComacConstants.ANALYZE_STATUS_APPROVED.equals(String.valueOf(obj[4])) || ComacConstants.ANALYZE_STATUS_HOLD.equals(String.valueOf(obj[4])))){
				openStatus = "<a title='解锁状态' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/maintain.gif'"
						+ " onclick='openAnalysisStratus(\""
						+ String.valueOf(obj[5])
						+ "\")'/></a>";
			}
			jsonFeildList.put("openStatus", openStatus);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}

	@Override
	public boolean openAnalysisStatus(String msiId) {
		MMain mMain = (MMain) this.dao.loadById(MMain.class, msiId);
		if(mMain != null){
			mMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			this.dao.save(mMain);
		}else {
			return false;
		}
		return true;
	}

	@Override
	public Page getWaitAnalysisLisForHomePage(Page page, String modelSeriesId,
			String curUserId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select main_id,");
		sb.append("       ataorarea_id,");
		sb.append("       ataorarea_code,");
		sb.append("       ataorarea_name,");
		sb.append("       status,");
		sb.append("       to_char(create_date, 'yyyy-mm-dd HH24:mi:ss') create_date,");
		sb.append("       urlParam,");
		sb.append("       analysis_type");
		sb.append("  from (select b.msi_id main_id,");
		sb.append("               a.ata_id ataorarea_id,");
		sb.append("               a.ata_code ataorarea_code,");
		sb.append("               a.ata_name ataorarea_name,");
		sb.append("               b.status,");
		sb.append("               b.create_date,");
		sb.append("               b.msi_id || ',' || a.ata_id urlParam,");
		sb.append("               '" + ComacConstants.SYSTEM_CODE + "' analysis_type");
		sb.append("          from com_ata a, m_main b, m_select c");
		sb.append("         where a.ata_id = b.ata_id");
		sb.append("           and a.ata_id = c.ata_id");
		sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and c.is_msi = 1");
		sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("           and b.model_series_id = '" + modelSeriesId + "'");
		sb.append("           and (b.status = '" + ComacConstants.ANALYZE_STATUS_NEW + "' or b.status = '" + ComacConstants.ANALYZE_STATUS_MAINTAIN + "')");
		sb.append("           and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.SYSTEM_CODE + "', a.ata_id, '" + curUserId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
		sb.append("        union");
		sb.append("        select ssi_id main_id,");
		sb.append("               ata_id ataorarea_id,");
		sb.append("               ata_code ataorarea_code,");
		sb.append("               ata_name ataorarea_name,");
		sb.append("               status,");
		sb.append("               create_date,");
		sb.append("               is_ssi || ',' || ssi_id urlParam,");
		sb.append("               '" + ComacConstants.STRUCTURE_CODE + "' analysis_type");
		sb.append("          from (select b.ssi_id,");
		sb.append("                       a.ata_id,");
		sb.append("                       a.ata_code,");
		sb.append("                       a.ata_name,");
		sb.append("                       b.status,");
		sb.append("                       b.create_date,");
		sb.append("                       b.is_ssi || ',' || b.ssi_id urlParam,");
		sb.append("                       case when (b.is_ssi = 1) then 1 when (b.is_ssi = 0 and b.is_ana = 1) then 0 else 2 end is_ssi");
		sb.append("                  from com_ata a, s_main b");
		sb.append("                 where a.ata_id = b.ata_id");
		sb.append("                   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("                   and b.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("                   and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.STRUCTURE_CODE + "', a.ata_id, '" + curUserId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
		sb.append("                   and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("                   and b.model_series_id = '" + modelSeriesId + "'");
		sb.append("					  and (b.is_ssi = 1 or b.is_ana = 1)");
		sb.append("                   and (b.is_add is null or b.is_add = '0')");
		sb.append("                union");
		sb.append("                select c.ssi_id,");
		sb.append("                       null ata_id,");
		sb.append("                       c.add_code ata_code,");
		sb.append("                       c.add_name ata_name,");
		sb.append("                       c.status,");
		sb.append("                       c.create_date,");
		sb.append("                       c.is_ssi || ',' || c.ssi_id urlParam,");
		sb.append("                       case when (c.is_ssi = 1) then 1 when (c.is_ssi = 0 and c.is_ana = 1) then 0 else 2 end is_ssi");
		sb.append("                  from s_main c");
		sb.append("                 where c.is_add = 1");
		sb.append("                   and c.add_user = '" + curUserId + "'");
		sb.append("                   and c.model_series_id = '" + modelSeriesId + "'");
		sb.append("                   and c.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("					  and (c.is_ssi = 1 or c.is_ana = 1)");
		sb.append("                   and c.is_add = 1)");
		sb.append("         where is_ssi <> 2");
		sb.append("           and (status = '" + ComacConstants.ANALYZE_STATUS_NEW + "' or status = '" + ComacConstants.ANALYZE_STATUS_MAINTAIN + "')");
		sb.append("        union");
		sb.append("        select b.za_id main_id,");
		sb.append("               a.area_id ataorarea_id,");
		sb.append("               a.area_code ataorarea_code,");
		sb.append("               a.area_name ataorarea_name,");
		sb.append("               b.status,");
		sb.append("               b.create_date,");
		sb.append("               a.area_id urlParam,");
		sb.append("               '" + ComacConstants.ZONAL_CODE + "' analysis_type");
		sb.append("  		  from com_area a, za_main b");
		sb.append("         where a.area_id = b.area_id(+)");
		sb.append("           and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and b.valid_flag(+) = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("           and b.model_series_id(+) = '" + modelSeriesId + "'");
		sb.append("           and fun_is_owner_auth(a.model_series_id, '" + ComacConstants.ZONAL_CODE + "', a.area_id, '" + curUserId + "', '" + ComacConstants.POSITION_ID_PROFESSION_ANAYIST + "') = '1'");
		sb.append("           and b.status(+) = '" + ComacConstants.ANALYZE_STATUS_MAINTAIN + "'");
		sb.append("        union");
		sb.append("        select hsi_id main_id,");
		sb.append("               '' ataorarea_id,");
		sb.append("               hsi_code ataorarea_code,");
		sb.append("               hsi_name ataorarea_name,");
		sb.append("               status,");
		sb.append("               create_date,");
		sb.append("               ref_hsi_code || ',' || hsi_id urlParam,");
		sb.append("               '" + ComacConstants.LHIRF_CODE + "' analysis_type");
		sb.append("          from lh_main");
		sb.append("         where valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("           and ana_user = '" + curUserId + "'");
		sb.append("           and model_series_id = '" + modelSeriesId + "'");
		sb.append("           and (status = '" + ComacConstants.ANALYZE_STATUS_NEW + "' or status = '" + ComacConstants.ANALYZE_STATUS_MAINTAIN + "'))");
		sb.append(" order by analysis_type, create_date");
		return this.sysTreeDao.findBySql(page, sb.toString(), null);
	}

	public ISysTreeDao getSysTreeDao() {
		return sysTreeDao;
	}

	public void setSysTreeDao(ISysTreeDao sysTreeDao) {
		this.sysTreeDao = sysTreeDao;
	}

	public IZaTreeBO getZaTreeBO() {
		return zaTreeBO;
	}

	public void setZaTreeBO(IZaTreeBO zaTreeBO) {
		this.zaTreeBO = zaTreeBO;
	}

	public IMsiSelectDao getMsiSelectDao() {
		return msiSelectDao;
	}

	public void setMsiSelectDao(IMsiSelectDao msiSelectDao) {
		this.msiSelectDao = msiSelectDao;
	}
	
}
