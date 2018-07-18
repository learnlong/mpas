package com.rskytech.basedata.bo;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface IComHelpBo extends IBo {

	/**
	 * 通过机型ID和具体的帮助页面代码，查询帮助内容
	 * @param helpWhere 帮助页面代码
	 * @param msId 机型ID
	 * @return 帮助内容
	 * @author zhangjianmin
	 */
	public String getHelpContent(String helpWhere, String msId);
	
	/**
	 * 保存帮助信息
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param helpWhere 帮助页面代码
	 * @param content 帮助信息
	 * @author zhangjianmin
	 */
	public void saveHelp(ComUser user, ComModelSeries ms, String helpWhere, String content);
}
