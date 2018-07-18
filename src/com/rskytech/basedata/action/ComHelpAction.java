package com.rskytech.basedata.action;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.basedata.bo.IComHelpBo;

public class ComHelpAction extends BaseAction {

	private static final long serialVersionUID = 8341143450869448068L;

	private IComHelpBo comHelpBo;
	
	private String helpWhere;
	private String content;
	
	public String init() {
		return SUCCESS;
	}
	
	public String getHelp() {
		String msId = getComModelSeries().getModelSeriesId();
		
		JSONObject json = new JSONObject();
		json.element("content", comHelpBo.getHelpContent(helpWhere, msId));
		writeToResponse(json.toString());
		return null;
	}
	
	public String saveHelp() {
		comHelpBo.saveHelp(getSysUser(), getComModelSeries(), helpWhere, content);
		writeToResponse("{'success':'true','failure':'false'}");
		return null;
	}

	public IComHelpBo getComHelpBo() {
		return comHelpBo;
	}

	public void setComHelpBo(IComHelpBo comHelpBo) {
		this.comHelpBo = comHelpBo;
	}

	public String getHelpWhere() {
		return helpWhere;
	}

	public void setHelpWhere(String helpWhere) {
		this.helpWhere = helpWhere;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
