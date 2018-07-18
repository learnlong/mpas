package com.rskytech.sys.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComUser;

@SuppressWarnings("rawtypes")
public interface ISysTreeBo extends IBo {
	
	public List searchSubAtaOrMsiTreeList(ComUser user, String parentAtaId, String modelSeriesId, String searchType) throws BusinessException;
	
	public List searchMyMaintainList(ComUser user, String modelSeriesId, String searchType) throws BusinessException;

	/**
	 * 状态为审核完成或者冻结的，转为分析完成
	* @Title: openAnalysisStatus
	* @Description:
	* @param msiId
	* @return
	* @author samual
	* @date 2014年12月15日 上午11:19:46
	* @throws
	 */
	public boolean openAnalysisStatus(String msiId);

	/**
	 * 得到首页代办分析
	* @Title: getWaitAnalysisLisForHomePage
	* @Description:
	* @param page
	* @param modelSeriesId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年12月23日 下午4:37:47
	* @throws
	 */
	public Page getWaitAnalysisLisForHomePage(Page page, String modelSeriesId,
			String curUserId);
}
