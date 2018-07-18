package com.rskytech.struct.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface IStructTreeBo extends IBo {

	public List searchSubAtaOrSsiTreeList(ComUser user, String parentAtaId, String level, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException;

	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @param ssiId
	* @return
	* @author samual
	* @date 2014年12月15日 上午11:21:53
	* @throws
	 */
	public boolean openAnalysisStatus(String ssiId);
}
