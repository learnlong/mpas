package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaTreeBO;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.SMain;
import com.rskytech.struct.bo.IStructTreeBo;
import com.rskytech.struct.dao.IStructTreeDao;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class StructTreeBo extends BaseBO implements IStructTreeBo {
	
	private IZaTreeBO zaTreeBO;
	private IStructTreeDao structTreeDao;

	public List searchSubAtaOrSsiTreeList(ComUser user, String parentAtaId, String level, String modelSeriesId, String searchType) throws BusinessException{
		List list = this.structTreeDao.searchSubAtaOrSsiTree(user, parentAtaId, level, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}
		
		HashMap<String, Object> jsonFeildList = null;
		List listJson = new ArrayList<HashMap>();
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			//String level = (obj[4] == null ? "" : obj[4].toString());
			jsonFeildList.put("id", obj[0]==null?obj[8]:obj[0]);
			String strNameTmp = "";
			if(ComacConstants.CHOOSE.equals(searchType)){
				strNameTmp = strNameTmp + zaTreeBO.getTreeNodeStatus(obj[1], obj[2], obj[5], null);
				jsonFeildList.put("isMaintain", obj[5]);
			}else if (ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)) {
				if(obj[7] != null && "2".equals(String.valueOf(obj[7]))){
					strNameTmp = "Q" + String.valueOf(obj[1]);
				}else{
					strNameTmp = String.valueOf(obj[1]);
				}
				String isauth = String.valueOf(obj[5]);//等于0时候不可以分析
				String isssi = String.valueOf(obj[7]);
				if("1".equals(isauth) && !"0".equals(isssi)){
					jsonFeildList.put("isMaintain", "1");
					strNameTmp = zaTreeBO.getTreeNodeStatus(strNameTmp, obj[2], "1", obj[4]);
				}else {
					jsonFeildList.put("isMaintain", "0");
					strNameTmp = zaTreeBO.getTreeNodeStatus(strNameTmp, obj[2], "0", obj[4]);
				}
			}
			jsonFeildList.put("text", strNameTmp);
			jsonFeildList.put("isMaintain", obj[5]);
			jsonFeildList.put("status", obj[4]);
			jsonFeildList.put("level", obj[3]);
			jsonFeildList.put("leaf", "0".equals(obj[6].toString()) ? true : false);
			jsonFeildList.put("isSsi", obj[7]);
			jsonFeildList.put("ssiId", obj[8]);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}
	
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException{
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		List list = this.structTreeDao.searchMyMaintain(user,modelSeriesId, searchType);
		if (list == null) {
			return null;
		}
		
		List listJson = new ArrayList<HashMap>();
		HashMap<String, Object> jsonFeildList = null;
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			jsonFeildList.put("ataId", obj[0]==null?obj[6]:obj[0]);
			String strCodeTmp = "";
			if(ComacConstants.ANALYSIS.equals(searchType) || ComacConstants.REPORT.equals(searchType)) {
				if(obj[5] != null && "2".equals(String.valueOf(obj[5]))){
					strCodeTmp = "Q";
				}
			}
			strCodeTmp = strCodeTmp + String.valueOf(obj[1]);
			jsonFeildList.put("ataCode", strCodeTmp);
			jsonFeildList.put("level", obj[4]);
			jsonFeildList.put("status", zaTreeBO.getStatusNameByStatusCode(obj[3] == null ? "" : obj[3].toString()));
			jsonFeildList.put("statusDB", obj[3] == null ? "" : obj[3].toString());
			jsonFeildList.put("isSsi", obj[5]);
			jsonFeildList.put("ataName", obj[2]);
			jsonFeildList.put("ssiId", obj[6]);
			String openStatus = "";
			if(obj[3] != null && (ComacConstants.ANALYZE_STATUS_APPROVED.equals(String.valueOf(obj[3])) || ComacConstants.ANALYZE_STATUS_HOLD.equals(String.valueOf(obj[3])))){
				openStatus = "<a title='解锁状态' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/maintain.gif'"
						+ " onclick='openAnalysisStratus(\""
						+ String.valueOf(obj[6])
						+ "\")'/></a>";
			}
			jsonFeildList.put("openStatus", openStatus);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}
	
	@Override
	public boolean openAnalysisStatus(String ssiId) {
		SMain sMain = (SMain) this.dao.loadById(SMain.class, ssiId);
		if(sMain != null){
			sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			this.dao.save(sMain);
		}else {
			return false;
		}
		return true;
	}

	public IZaTreeBO getZaTreeBO() {
		return zaTreeBO;
	}

	public void setZaTreeBO(IZaTreeBO zaTreeBO) {
		this.zaTreeBO = zaTreeBO;
	}

	public IStructTreeDao getStructTreeDao() {
		return structTreeDao;
	}

	public void setStructTreeDao(IStructTreeDao structTreeDao) {
		this.structTreeDao = structTreeDao;
	}	
}
