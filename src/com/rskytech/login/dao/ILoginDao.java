package com.rskytech.login.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface ILoginDao extends IDAO {

	/**
	 * 查询机型系列
	 * @return 机型系列列表
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public List<ComModelSeries> searchModelSeries() throws BusinessException;
	
	/**
	 * 通过用户ID查询其所对应的菜单
	 * @param userId 用户ID
	 * @param parentId 上级菜单ID（NULL：查询主菜单；非空：查询parentId的子菜单）
	 * @return 菜单列表
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public List<Object[]> getMenu(ComUser user, String modelSeriesId, String parentId) throws BusinessException;
	
	/**
	 * 验证用户是否是超级管理员
	 * @param userId 用户ID
	 * @return 是/否
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public boolean checkSuperAdmin(String userId) throws BusinessException;

	/**
	 * 验证用户是否是专业室管理员
	 * @param userId 用户ID
	 * @return 是/否
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public boolean checkProfessionAdmin(String userId);

	@SuppressWarnings("rawtypes")
	public List getPositionList(String userId);
}
