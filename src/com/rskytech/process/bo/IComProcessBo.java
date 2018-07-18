package com.rskytech.process.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;

@SuppressWarnings("rawtypes")
public interface IComProcessBo extends IBo {

	/**
	 * 得到专业室
	* @Title: getProfessionForAnalysisByuserId
	* @Description:
	* @param modelSeriesId 机型ID
	* @param analysisType 分析类型
	* @param userId 用户id
	* @return
	* @author samual
	* @date 2014年12月2日 下午2:37:05
	* @throws
	 */
	public List getProfessionForAnalysisByuserId(String modelSeriesId, String analysisType, String userId);

	/**
	 * 得到用户的分析类型
	* @Title: getAnalysisTypeByuserId
	* @Description:
	* @param modelSeriesId
	* @param userId
	* @return
	* @author samual
	* @date 2014年12月2日 下午3:01:41
	* @throws
	 */
	public List getAnalysisTypeByuserId(String modelSeriesId, String userId);

	/**
	 * 得到审核人员（总监职位）
	* @Title: getCheckUserByProfessionId
	* @Description:
	* @param professionId
	* @return
	* @author samual
	* @date 2014年12月4日 上午11:10:05
	* @throws
	 */
	public List getCheckUserByProfessionId(String professionId);

	/**
	 * 得到可以发起审批的分析（分析状态已完成）
	* @Title: getAnalysisOver
	* @Description:
	* @param page
	* @param modelSeriesId
	* @param analysisType
	* @param userId
	* @return
	* @author samual
	* @date 2014年12月4日 上午11:09:06
	* @throws
	 */
	public Page getAnalysisOver(Page page, String modelSeriesId, String analysisType, String userId);
	
	/**
	 * 启动分析流程
	* @Title: startProcess
	* @Description:
	* @param comModelSeries 机型
	* @param analysisType 四大分析类型
	* @param checkUser 审批人员id
	* @param checkOpinion 审批意见
	* @param choosedIds 选中的分析
	* @param curUserId 当前用户ID
	* @return
	* @author samual
	* @date 2014年12月3日 下午2:08:33
	* @throws
	 */
	public boolean startProcess(ComModelSeries comModelSeries,
			String analysisType, String checkUser, String checkOpinion,
			String choosedIds, String curUserId);

	/**
	 * 查询流程审批的列表数据
	* @Title: loadCheckProcess
	* @Description:
	* @param page
	* @param modelSeriesId 机型id
	* @param analysisType 四大分析
	* @param processStatus 流程状态
	* @param launchUserName 发起人
	* @param fromDate 发起开始时间
	* @param toDate 发起结束时间
	* @param curUserId 当前用户
	* @return
	* @author samual
	* @date 2014年12月4日 上午11:23:17
	* @throws
	 */
	public Page loadCheckProcess(Page page, String modelSeriesId,
			String analysisType, String processStatus, String launchUserName,
			String fromDate, String toDate, String curUserId);

	/**
	 * 批量审核通过
	* @Title: batchCheckPass
	* @Description:
	* @param comModelSeries 当前机型
	* @param choosedIds 选中的审批id
	* @param curUserId 当前用户id
	* @return
	* @author samual
	* @date 2014年12月10日 下午2:02:27
	* @throws
	 */
	public boolean batchCheckPass(ComModelSeries comModelSeries, String choosedIds, String curUserId);

	/**
	 * 批量审核不通过
	* @Title: batchCheckNotPass
	* @Description:
	* @param comModelSeries 当前机型
	* @param choosedIds 选中的审批id
	* @param curUserId 当前用户id
	* @return
	* @author samual
	* @date 2014年12月10日 下午3:40:21
	* @throws
	 */
	public boolean batchCheckNotPass(ComModelSeries comModelSeries, String choosedIds, String curUserId);

	/**
	 * 批量取消审核
	* @Title: batchCheckCancel
	* @Description:
	* @param comModelSeries
	* @param choosedIds
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年12月10日 下午4:06:59
	* @throws
	 */
	public boolean batchCheckCancel(ComModelSeries comModelSeries,
			String choosedIds, String curUserId);

	/**
	 * 得到审批的流程的明细数据
	* @Title: getProcessDetail
	* @Description:
	* @param modelSeriesId 机型id
	* @param processId 流程id
	* @param analysisType 四大分析类型
	* @return
	* @author samual
	* @date 2014年12月11日 上午9:15:24
	* @throws
	 */
	public List getProcessDetail(String modelSeriesId, String processId, String analysisType);

	/**
	 * 单个流程审核时，审核单条数据
	* @Title: singleCheckProcessDetail
	* @Description:
	* @param modelSeriesId 当前机型id
	* @param processId 流程id
	* @param analysisType 分析类型
	* @param isOkType 是否通过
	* @param isOkCheckpinion 审批意见
	* @param choosedIds 选中的mailId
	* @param curUserId 当前用户
	* @return
	* @author samual
	* @date 2014年12月11日 下午1:35:42
	* @throws
	 */
	public boolean singleCheckProcessDetail(String modelSeriesId,
			String processId, String analysisType, String isOkType,
			String isOkCheckpinion, String choosedIds, String curUserId);

	/**
	 * 单个流程审核时，审核所有数据
	* @Title: allCheckProcessDetail
	* @Description:
	* @param modelSeriesId 当前机型id
	* @param processId 流程id
	* @param analysisType 四大分析类型
	* @param isOkType 是否通过
	* @param isOkCheckpinion 审核意见
	* @param curUserId 当前用户id
	* @return
	* @author samual
	* @date 2014年12月11日 下午2:29:38
	* @throws
	 */
	public boolean allCheckProcessDetail(String modelSeriesId,
			String processId, String analysisType, String isOkType,
			String isOkCheckpinion, String curUserId);

	/**
	 * 查询审核信息
	* @Title: loadProcessForQuery
	* @Description:
	* @param page 分页
	* @param modelSeriesId 机型id
	* @param analysisType 四大分析类型
	* @param processStatus 流程状态
	* @param launchUserName 发起人
	* @param checkUserName 审核人
	* @param fromDate 发起流程开始日期
	* @param toDate 发起流程结束日期
	* @return
	* @author samual
	* @date 2014年12月12日 上午11:00:53
	* @throws
	 */
	public Page loadProcessForQuery(Page page, String modelSeriesId,
			String analysisType, String processStatus, String launchUserName,
			String checkUserName, String fromDate, String toDate);

	/**
	 * 首页审核的代办列表
	* @Title: loadCheckProcessForHomePage
	* @Description:
	* @param page
	* @param modelSeriesId
	* @param curUserId
	* @return
	* @author samual
	* @date 2014年12月22日 下午2:40:34
	* @throws
	 */
	public Page loadCheckProcessForHomePage(Page page, String modelSeriesId,
			String curUserId);
	
}
