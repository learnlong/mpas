package com.rskytech.basedata.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComHelp;

public interface IComHelpDao extends IDAO {

	/**
	 * 通过机型ID和具体的帮助页面代码，查询帮助信息
	 * @param helpWhere 帮助页面代码
	 * @param msId 机型ID
	 * @return 帮助信息
	 * @author zhangjianmin
	 */
	public ComHelp getHelp(String helpWhere, String msId) throws BusinessException;
}
