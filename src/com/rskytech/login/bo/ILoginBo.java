package com.rskytech.login.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface ILoginBo extends IBo {

	/**
	 * 验证登陆的用户名和密码
	 * @param userCode 用户名
	 * @param password 密码
	 * @return ComUser
	 * @author zhangjianmin
	 */
	public ComUser checkLoginUser(String userCode, String password);
	
	/**
	 * 查询机型系列
	 * @return 机型系列列表
	 * @author zhangjianmin
	 */
	public List<ComModelSeries> searchModelSeries();
	
	/**
	 * 通过用户ID查询其所对应的菜单
	 * @param userId 用户ID
	 * @return 菜单列表
	 * @author zhangjianmin
	 */
	public StringBuffer getAllMenu(ComUser user, String modelSeriesId);
	
	/**
	 * 验证用户是否是超级管理员
	 * @param userId 用户ID
	 * @return 是/否
	 * @author zhangjianmin
	 */
	public boolean checkSuperAdmin(String userId);

	/**
	 * 验证用户是否是超级管理员
	 * @param userId 用户ID
	 * @return 是/否
	 * @author zhangjianmin
	 */
	public boolean checkProfessionAdmin(String userId);

	/**
	 * 得到当前用户所有的职位，不管专业室
	* @Title: getPositionList
	* @Description:
	* @param userId
	* @return
	* @author samual
	* @date 2014年12月25日 上午9:34:10
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public List getPositionList(String userId);

}
