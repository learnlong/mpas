package com.rskytech.basedata.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComHelpDao;
import com.rskytech.pojo.ComHelp;

public class ComHelpDao extends BaseDAO implements IComHelpDao {

	@SuppressWarnings("unchecked")
	public ComHelp getHelp(String helpWhere, String msId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComHelp.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("helpWhere", helpWhere));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComHelp> list = this.findByCriteria(dc);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
