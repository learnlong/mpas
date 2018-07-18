package com.rskytech.lhirf.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface ILhTreeBo extends IBo {

	public List searchSubAreaOrHsiTreeList(ComUser user, String parentAreaId, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException;

	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @param lhId
	* @return
	* @author samual
	* @date 2014年12月15日 下午1:38:32
	* @throws
	 */
	public boolean openAnalysisStatus(String lhId);
}
