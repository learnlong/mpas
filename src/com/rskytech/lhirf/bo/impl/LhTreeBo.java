package com.rskytech.lhirf.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaTreeBO;
import com.rskytech.lhirf.bo.ILhTreeBo;
import com.rskytech.lhirf.dao.ILhTreeDao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;

@SuppressWarnings({"unchecked","rawtypes"})
public class LhTreeBo extends BaseBO implements ILhTreeBo {
	
	private IZaTreeBO zaTreeBO;
	private ILhTreeDao lhTreeDao;
	
	public List searchSubAreaOrHsiTreeList(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException{
		List list = this.lhTreeDao.searchSubAreaOrHsiTree(user, parentAreaId, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}
		
		HashMap<String, Object> jsonFeildList = null;
		List listJson = new ArrayList<HashMap>();
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			String level = (obj[3] == null ? "" : obj[3].toString());
			jsonFeildList.put("id", obj[0]);
			jsonFeildList.put("text", zaTreeBO.getTreeNodeStatus(obj[1], obj[2], obj[5], obj[4]));
			jsonFeildList.put("isMaintain", obj[5]);
			jsonFeildList.put("status", obj[4]);
			jsonFeildList.put("level", level);                            //N/A
			jsonFeildList.put("isRef", (obj[7] == null || ComacConstants.EMPTY.equals(obj[7].toString())) ? "0" : "1");
			jsonFeildList.put("leaf", "HSI".equals(obj[6].toString()) ? true : false);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}
	
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException{
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		List list = this.lhTreeDao.searchMyMaintain(user, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}
		List listJson = new ArrayList<HashMap>();
		HashMap<String, Object> jsonFeildList = null;
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
//			System.out.println("obj[3]=="+obj[3]);
//			System.out.println("obj[4]=="+obj[4]);
			jsonFeildList.put("areaId", obj[0]);
			jsonFeildList.put("areaCode", obj[1]);
			jsonFeildList.put("areaName", obj[2]);
			jsonFeildList.put("status", zaTreeBO.getStatusNameByStatusCode(obj[3] == null ? "" : obj[3].toString()));
			jsonFeildList.put("isRef", (obj[4] == null || ComacConstants.EMPTY.equals(obj[4].toString())) ? "0" : "1");
			String openStatus = "";
			if(obj[3] != null && (ComacConstants.ANALYZE_STATUS_APPROVED.equals(String.valueOf(obj[3])) || ComacConstants.ANALYZE_STATUS_HOLD.equals(String.valueOf(obj[3])))){
				openStatus = "<a title='解锁状态' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/maintain.gif'"
						+ " onclick='openAnalysisStratus(\""
						+ String.valueOf(obj[0])
						+ "\")'/></a>";
			}
			jsonFeildList.put("openStatus", openStatus);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}

	@Override
	public boolean openAnalysisStatus(String lhId) {
		LhMain lhMain = (LhMain) this.dao.loadById(LhMain.class, lhId);
		if(lhMain != null){
			lhMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			this.dao.save(lhMain);
		}else {
			return false;
		}
		return true;
	}

	public ILhTreeDao getLhTreeDao() {
		return lhTreeDao;
	}

	public void setLhTreeDao(ILhTreeDao lhTreeDao) {
		this.lhTreeDao = lhTreeDao;
	}

	public IZaTreeBO getZaTreeBO() {
		return zaTreeBO;
	}

	public void setZaTreeBO(IZaTreeBO zaTreeBO) {
		this.zaTreeBO = zaTreeBO;
	}
	
}
