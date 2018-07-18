package com.rskytech.user.dao;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComUser;

public interface IComUserDao extends IDAO {

	/**
	 * 通过用户名和密码查询用户信息
	 * @param userCode 用户名
	 * @param password 密码
	 * @return ComUser
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public ComUser getUser(String userCode, String password) throws BusinessException;
	}
