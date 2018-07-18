package com.rskytech.user.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.user.dao.IComUserDao;

@SuppressWarnings("unchecked")
public class ComUserDao extends BaseDAO implements IComUserDao {

	public ComUser getUser(String userCode, String password) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComUser.class);
		dc.add(Restrictions.eq("userCode", userCode));
		dc.add(Restrictions.eq("password", password));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComUser> list = this.findByCriteria(dc);
		if (list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
}
