package com.rskytech.basedata.bo.impl;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComHelpBo;
import com.rskytech.basedata.dao.IComHelpDao;
import com.rskytech.pojo.ComHelp;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComHelpBo extends BaseBO implements IComHelpBo {

	private IComHelpDao comHelpDao;
	
	public String getHelpContent(String helpWhere, String msId){
		ComHelp help = comHelpDao.getHelp(helpWhere, msId);
		if (help == null){
			return "";
		} else {
			return help.getContent() == null ? "" : help.getContent();
		}
	}
	
	public void saveHelp(ComUser user, ComModelSeries ms, String helpWhere, String content){
		ComHelp help = comHelpDao.getHelp(helpWhere, ms.getModelSeriesId());
		if (help == null){
			help = new ComHelp();
			help.setComModelSeries(ms);
			help.setHelpWhere(helpWhere);
			help.setContent(content);
			help.setValidFlag(ComacConstants.VALIDFLAG_YES);
			this.saveOrUpdate(help, ComacConstants.DB_INSERT, user.getUserId());	
		} else {
			help.setContent(content);
			this.saveOrUpdate(help, ComacConstants.DB_UPDATE, user.getUserId());	
		}
	}

	public IComHelpDao getComHelpDao() {
		return comHelpDao;
	}

	public void setComHelpDao(IComHelpDao comHelpDao) {
		this.comHelpDao = comHelpDao;
	}
	
}
