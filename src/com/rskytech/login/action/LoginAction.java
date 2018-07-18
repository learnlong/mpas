package com.rskytech.login.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.zefer.b.e;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComModelSeriesBo;
import com.rskytech.login.bo.ILoginBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 3173794657742392241L;

	private ILoginBo loginBo;
	private IComModelSeriesBo comModelSeriesBo;
	
	private String userCode;
	private String password;
	private String modelSeriesId;
	
	//登陆执行的方法
	public String login() {
		if (ServletActionContext.getContext().getSession().containsKey("USERMSG")) {
			ServletActionContext.getContext().getSession().remove("USERMSG");
		}
		
		if ("".equals(userCode) || "".equals(password)) {
			return "loginFailed";
		}
		
		ComUser user = loginBo.checkLoginUser(userCode, password);
		if (user == null){
			ServletActionContext.getContext().getSession().put("USERMSG", "1");
			return "loginFailed";
		} else {
			List<ComModelSeries> msList = loginBo.searchModelSeries();
			if (msList != null){
				for (ComModelSeries ms : msList){
					if (ms.getDefaultModelSeries() == 1){
						ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_NOW_MODEL_SERIES, ms);//在SESSION中保存当前机型系列
					}
				}
			}
			
			user.setAdmin(loginBo.checkSuperAdmin(user.getUserId()));//判断用户是否超级管理员
//			user.setProfessionAdmin(loginBo.checkProfessionAdmin(user.getUserId()));//判断用户是否专业组管理员
			//判断用户是否有各种职务
			List lst = loginBo.getPositionList(user.getUserId());
			if (lst != null) {
				for (Object obj : lst) {
					Object []objs=(Object[])obj;
					String tmpPositionId = String.valueOf(objs[0]);
					if(ComacConstants.POSITION_ID_PROFESSION_ADMIN.equals(tmpPositionId)){
						user.setProfessionAdmin(true);
					} else if(ComacConstants.POSITION_ID_PROFESSION_ENGINEER.equals(tmpPositionId)) {
						user.setProfessionEngineer(true);
					} else if(ComacConstants.POSITION_ID_PROFESSION_ANAYIST.equals(tmpPositionId)) {
						user.setProfessionAnalysis(true);
					}
				}
			}
					
			ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_MODEL_SERIES_LIST, msList);//在SESSION中保存首页中机型系列列表
			ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_USER_KEY, user);//在SESSION中保存当前用户信息
			return SUCCESS;
		}
	}
	
	//首页菜单生成方法
	public String getParentMenu() {
		StringBuffer sb = loginBo.getAllMenu(this.getSysUser(), this.getComModelSeries().getModelSeriesId());
		writeToResponse(sb.toString());
		return null;
	}


	public String logout() {
		ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_USER_KEY, null);
		if (ServletActionContext.getContext().getSession().containsKey("USERMSG")) {
			ServletActionContext.getContext().getSession().remove("USERMSG");
		}
		return null;
	}

	/**
	 * 获取机型
	* @Title: jsonLoadModelSeries
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月1日 上午10:31:09
	* @throws
	 */
	public String jsonLoadModelSeries(){
		List<HashMap> listJsonFV = new ArrayList();
		List<ComModelSeries> msList = loginBo.searchModelSeries();
		if (msList != null) {
			for (ComModelSeries ms : msList) {
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("modelSeriesId", ms.getModelSeriesId());
				hm.put("modelSeriesCode", ms.getModelSeriesCode());
				hm.put("modelSeriesName", ms.getModelSeriesName());
				hm.put("defaultModelSeries", ms.getDefaultModelSeries());
				listJsonFV.add(hm);
			}
		}
		JSONObject json = new JSONObject();
		json.put("comModelSeries", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public String changeCurModelSeries(){
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		ComModelSeries comModelSeries = (ComModelSeries) comModelSeriesBo.loadById(ComModelSeries.class, this.modelSeriesId);
		if(comModelSeries != null){
			ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_NOW_MODEL_SERIES, comModelSeries);
			json = this.putJsonOKFlag(json, true);
		}else {
			json = this.putJsonOKFlag(json, false);
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	public ILoginBo getLoginBo() {
		return loginBo;
	}

	public void setLoginBo(ILoginBo loginBo) {
		this.loginBo = loginBo;
	}

	public IComModelSeriesBo getComModelSeriesBo() {
		return comModelSeriesBo;
	}

	public void setComModelSeriesBo(IComModelSeriesBo comModelSeriesBo) {
		this.comModelSeriesBo = comModelSeriesBo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getModelSeriesId() {
		return modelSeriesId;
	}

	public void setModelSeriesId(String modelSeriesId) {
		this.modelSeriesId = modelSeriesId;
	}
	
}
