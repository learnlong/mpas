package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;

public interface IIncreaseRegionParamDao extends IDAO{
	
	public List getLevelCount(String modelSeriesId, Integer vaildFlg);

}
