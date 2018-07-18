package com.rskytech.area.bo.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.bo.IZaTreeBO;
import com.rskytech.area.dao.IZaTreeDao;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ZaMain;

@SuppressWarnings({"unchecked","rawtypes"})
public class ZaTreeBO extends BaseBO implements IZaTreeBO {

	private IZaTreeDao zaTreeDao;
	
	/**
	 * 组装树节点的显示信息：有维护权限，则加小图片；显示文字=编号+名称；不同状态显示不同颜色。
	 * 
	 * @param Object
	 *            nodeCode 树节点编号
	 * @param Object
	 *            nodeName 树节点中文名称
	 * @param Object
	 *            isOwnAna 当前用户是否有修改权限 1：有权限 0：没有权限
	 * @param Object
	 *            nodeStatus 树节点的分析状态
	 * @return String
	 */
	public String getTreeNodeStatus(Object nodeCode, Object nodeName, Object isOwnAna,
			Object nodeStatus) {
		String contextPath = ServletActionContext.getRequest().getContextPath();
		String pic = "<image src=\"" + contextPath
				+ "/js/extjs/images/icons/pencil.gif\">";// 维护图片地址
		String text = (nodeCode == null ? "" : nodeCode.toString());// 最终的显示文字
		String isAna = (isOwnAna == null ? "" : isOwnAna.toString());
		String status = (nodeStatus == null ? "" : nodeStatus.toString());
		String str = "";// 最终输出信息

		// 有维护权限的信息需要添加小图片
		if ("1".equals(isAna)) {
			str = str + pic;
		}

		// 根据不同的选择语言，显示不同语言的名称。
		// 如果名称为空，则只显示编号
		if ( nodeName != null&& !"".equals(nodeName.toString())) {
			text = text + "(" + nodeName+")";
		} 

		// 根据不同的状态，显示不同颜色的信息
		if (ComacConstants.ANALYZE_STATUS_NEW.equals(status)) {
			str = str + "<font color='" + ComacConstants.ANALYZE_COLOR_NEW
					+ "'><b>" + text + "</b></font>";
		} else if (ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(status)) {
			str = str + "<font color='" + ComacConstants.ANALYZE_COLOR_MAINTAIN
					+ "'><b>" + text + "</b></font>";
		} else if (ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(status)) {
			str = str + "<font color='"
					+ ComacConstants.ANALYZE_COLOR_MAINTAINOK + "'><b>" + text
					+ "</b></font>";
		} else if (ComacConstants.ANALYZE_STATUS_APPROVED.equals(status)) {
			str = str + "<font color='"
					+ ComacConstants.ANALYZE_COLOR_APPROVED + "'><b>"
					+ text + "</b></font>";
		} else {
			str = str + text;
		}
		return str;
	}
	
	/**
	 * 通过分析状态编号取得显示的名称
	 * 
	 * @param String
	 *            statusCode 分析状态编号
	 * @return String
	 */
	public String getStatusNameByStatusCode(String statusCode) {
			if(statusCode == null ||"".equals(statusCode)){
				return "未建";
			} else if (ComacConstants.ANALYZE_STATUS_NEW.equals(statusCode)) {
				return ComacConstants.ANALYZE_STATUS_NEW_SHOW;
			} else if (ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(statusCode)) {
				return ComacConstants.ANALYZE_STATUS_MAINTAIN_SHOW;
			} else if (ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(statusCode)) {
				return ComacConstants.ANALYZE_STATUS_MAINTAINOK_SHOW;
			} else if (ComacConstants.ANALYZE_STATUS_APPROVED.equals(statusCode)) {
				return ComacConstants.ANALYZE_STATUS_APPROVED_SHOW;
			} else if (ComacConstants.ANALYZE_STATUS_HOLD.equals(statusCode)) {
				return ComacConstants.ANALYZE_STATUS_HOLD_SHOW;
			}
		return null;
	}

	@Override
	public List searchSubAreaTreeList(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException {
		List list = this.zaTreeDao.searchSubAreaTree(user, parentAreaId, modelSeriesId, searchType);
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
			jsonFeildList.put("text", getTreeNodeStatus(obj[1], obj[2], obj[5], obj[4]));
			jsonFeildList.put("isMaintain", obj[5]);
			jsonFeildList.put("status", obj[4]);
			jsonFeildList.put("level", level);
			jsonFeildList.put("leaf", "0".equals(obj[6].toString()) ? true : false);
			jsonFeildList.put("zaId", obj[7]);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}

	@Override
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType)
			throws BusinessException {
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		List list = this.zaTreeDao.searchMyMaintain(user, modelSeriesId, searchType);
		if (list == null) {
			return null;
		}

		List listJson = new ArrayList<HashMap>();
		HashMap<String, Object> jsonFeildList = null;
		for (int i = 0; i < list.size(); i++) {
			jsonFeildList = new HashMap<String, Object>();
			Object[] obj = (Object[]) list.get(i);
			jsonFeildList.put("areaId", obj[0]);
			jsonFeildList.put("areaCode", obj[1]);
			jsonFeildList.put("areaName", obj[2]);
			jsonFeildList.put("status", getStatusNameByStatusCode(obj[3] == null ? "" : obj[3].toString()));
			jsonFeildList.put("zaId", obj[4]);
			String openStatus = "";
			if(obj[3] != null && (ComacConstants.ANALYZE_STATUS_APPROVED.equals(String.valueOf(obj[3])) || ComacConstants.ANALYZE_STATUS_HOLD.equals(String.valueOf(obj[3])))){
				openStatus = "<a title='解锁状态' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/maintain.gif'"
						+ " onclick='openAnalysisStratus(\""
						+ String.valueOf(obj[4])
						+ "\")'/></a>";
			}
			jsonFeildList.put("openStatus", openStatus);
			listJson.add(jsonFeildList);
		}
		return listJson;
	}

	@Override
	public boolean openAnalysisStatus(String zaId) {
		ZaMain zaMain = (ZaMain) this.dao.loadById(ZaMain.class, zaId);
		if(zaMain != null){
			zaMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			this.dao.save(zaMain);
		}else {
			return false;
		}
		return true;
	}

	public IZaTreeDao getZaTreeDao() {
		return zaTreeDao;
	}

	public void setZaTreeDao(IZaTreeDao zaTreeDao) {
		this.zaTreeDao = zaTreeDao;
	}
	
}
