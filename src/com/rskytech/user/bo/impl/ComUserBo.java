package com.rskytech.user.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.user.bo.IComUserBo;
import com.rskytech.user.dao.IComUserDao;

@SuppressWarnings("rawtypes")
public class ComUserBo extends BaseBO implements IComUserBo {
	
	private IComUserDao comUserDao;

	@Override
	public List getComUserList(String userCode, String keyword, Page page, String validFlag) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(ComUser.class);
		if (!BasicTypeUtils.isNullorBlank(userCode)) {
			dc.add(Restrictions.like("userCode", "%" + userCode + "%"));
		}
		if (!BasicTypeUtils.isNullorBlank(keyword)) {
			dc.add(Restrictions.like("userName", "%" + keyword + "%"));
		}
		if (!BasicTypeUtils.isNullorBlank(validFlag)) {
			if (validFlag.equals("0")) {
				dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_NO));
			}else if (validFlag.equals("1")) {
				dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
			}
		}
		dc.add(Restrictions.ne("userId", ComacConstants.USER_ID_ADMIN));//不查询admin用户
		return this.findByCritera(dc, page).getResult();
	}

	@Override
	public boolean jsonChangeUserPassWord(String userId, String curUserId, String oldPassWord, String newPassWord) {
		String dbOperate = ComacConstants.DB_UPDATE;
		ComUser comUser = (ComUser) this.loadById(ComUser.class, userId);
		if (comUser.getPassword().equals(oldPassWord)) {
			comUser.setPassword(newPassWord);
			this.saveOrUpdate(comUser, dbOperate, curUserId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean changeUserPassWordByAdmin(String userId, String newPassWord, String curUserId) {
		if(userId == null || "".equals(userId)){
			return false;
		}
		ComUser comUser = (ComUser) this.loadById(ComUser.class, userId);
		String dbOperate = ComacConstants.DB_UPDATE;
		if (comUser != null) {
			comUser.setPassword(newPassWord);
			this.saveOrUpdate(comUser, dbOperate, curUserId);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkPassWord(String userId, String oldPassWord) {
		ComUser comUser = (ComUser) this.loadById(ComUser.class, userId);
		if (comUser.getPassword().equals(oldPassWord)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean resetUserPassWord(String userId, String curUserId) {
		if(userId == null || "".equals(userId)){
			return false;
		}
		ComUser comUser = (ComUser) this.loadById(ComUser.class, userId);
		if (comUser != null) {
			comUser.setPassword(ComacConstants.DEFAULT_PASSWORD);
			this.saveOrUpdate(comUser, ComacConstants.DB_UPDATE, curUserId);
			return true;
		} else {
			return false;
		}
	}

	public IComUserDao getComUserDao() {
		return comUserDao;
	}

	public void setComUserDao(IComUserDao comUserDao) {
		this.comUserDao = comUserDao;
	}

}
