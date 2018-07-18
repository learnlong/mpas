package com.rskytech.login.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.login.bo.ILoginBo;
import com.rskytech.login.dao.ILoginDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.user.dao.IComUserDao;

public class LoginBo extends BaseBO implements ILoginBo {

	private IComUserDao comUserDao;
	private ILoginDao loginDao;
	
	public ComUser checkLoginUser(String userCode, String password){
		return comUserDao.getUser(userCode, password);
	}
	
	public List<ComModelSeries> searchModelSeries(){
		return loginDao.searchModelSeries();
	}
	
	public StringBuffer getAllMenu(ComUser user, String modelSeriesId){
		List<Object[]> list = loginDao.getMenu(user,modelSeriesId, null);
		
		StringBuffer sb = new StringBuffer();
		int menuCount = 10;//需要显示的菜单个数(写死)，没有动态计算
		
		sb.append("<div id=\"dh\"><ul id=\"ulmenu\">");
		if (list != null && list.size() > menuCount){
			sb.append("<li><a href=\"javascript:void(0)\" id=\"mb100000\" class=\"easyui-menubutton\" menu=\"#mm100000\" > >> </a></li>");					
		}
		
		for (int i = list.size() - 1; i >= 0 ; i--) {
			Object[] obj = list.get(i);
			if (i < menuCount){ //	menuCount个菜单			
				sb.append("<li><a href=\"javascript:void(0)\" id=\"mb" +  obj[0] + "\" class=\"easyui-menubutton\" menu=\"#mm" + obj[0] +"\" >" +  obj[1] + "</a></li>");
			}
		}
		sb.append("</ul></div>");
		
		//menuCount个菜单下的子菜单
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			if (i < menuCount){				
				StringBuffer bfTemp = getMenu(user,modelSeriesId, obj[0].toString(), true);
				if (bfTemp != null) 
					sb.append(bfTemp.toString());
			}
		}
		
		//menuCount个菜单以后的子菜单
		if (list.size() >= menuCount){
			sb.append("<div id=\"mm100000\"  style=\"width:170px;\">");
		}
		for (int i = menuCount; i < list.size(); i++){
			Object[] obj = list.get(i);
			sb.append("<div><span>");
			sb.append(obj[1]); 
			sb.append("</span>");
			StringBuffer bfTemp = getMenu(user,modelSeriesId, obj[0].toString(), false);
			if (bfTemp != null)
				sb.append( bfTemp.toString());
			sb.append("</div>");
		}
		
		if (list.size() >= menuCount){
			sb.append("</div>");
		}
		return sb;
	}
	
	public StringBuffer getMenu(ComUser user,String modelSeriesId, String parentId, boolean isAppendId) {
		List<Object[]> list = loginDao.getMenu(user,modelSeriesId, parentId);
		if (list == null || list.size() == 0){
			return null;
		}
		
		StringBuffer bf = new StringBuffer();	
		bf.append("<div ").append(isAppendId ? "id=\"mm" + parentId + "\"" : "").append( "style=\"width:170px;\">");
		
		for (Object[] cm : list) {
			if (cm[2] != null){
				String urlString = cm[2].toString();
				long timstamp = new Date().getTime();
				urlString = urlString.indexOf("?") > -1 ? urlString + "&timestamp=" + timstamp : urlString + "?timestamp=" + timstamp;			
				if (!cm[2].toString().startsWith("/")){
					cm[2] = "/" + urlString; 
				}
			}
			
			bf.append("<div onclick=\"javascript:document.getElementById('ifrm').src='" + ServletActionContext.getRequest().getContextPath()+ cm[2] + "';void(0);\">"+cm[1]+"</div>");
		}

		bf.append("</div>");
		
		return bf;
	}
	
	public boolean checkSuperAdmin(String userId){
		return loginDao.checkSuperAdmin(userId);
	}

	@Override
	public boolean checkProfessionAdmin(String userId) {
		return loginDao.checkProfessionAdmin(userId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPositionList(String userId) {
		return loginDao.getPositionList(userId);
	}

	public IComUserDao getComUserDao() {
		return comUserDao;
	}

	public void setComUserDao(IComUserDao comUserDao) {
		this.comUserDao = comUserDao;
	}

	public ILoginDao getLoginDao() {
		return loginDao;
	}

	public void setLoginDao(ILoginDao loginDao) {
		this.loginDao = loginDao;
	}
	
}
