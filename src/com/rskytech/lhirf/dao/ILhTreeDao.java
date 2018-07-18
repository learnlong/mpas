package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface ILhTreeDao extends IDAO {

	public List searchSubAreaOrHsiTree(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException;
}
