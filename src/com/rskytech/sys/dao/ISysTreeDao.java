package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface ISysTreeDao extends IDAO {

	public List searchSubAtaOrMsiTree(ComUser user, String parentAtaId, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException;
}
