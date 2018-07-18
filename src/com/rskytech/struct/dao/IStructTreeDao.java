package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IStructTreeDao extends IDAO {

	public List searchSubAtaOrSsiTree(ComUser user, String parentAtaId, String level, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException;
}
