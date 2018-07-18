package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IZaTreeDao extends IDAO {
	
	/**
	 * 查询指定区域节点的下级子节点
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @param Integer parentAreaId 当前区域节点，即父节点
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchSubAreaTree(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException;
	
	/**
	 * 查询该用户是否有需要维护的区域
	 * @param ComUser user 用户信息
	 * @param String searchType 当前页面的查询信息   版本页面：VERSION   分析页面：ANALYSIY
	 * @return List
	 * @author 张建民
	 * @createdate 2012-8-28
	 */
	public List searchMyMaintain(ComUser user, String modelSeriesId, String searchType) throws BusinessException;
}
