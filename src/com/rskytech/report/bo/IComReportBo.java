package com.rskytech.report.bo;

import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComReport;

public interface IComReportBo extends IBo {

	/**
	 * 得到报告
	* @Title: loadComReportList
	* @Description:
	* @param page
	* @param modelSeriesId 机型id
	* @param reportType 报告类型（系统，区域..）
	* @param generateId 对应的id
	* @return
	* @author samual
	* @date 2014年11月21日 上午10:40:51
	* @throws
	 */
	Page loadComReportList(Page page, String modelSeriesId, String reportType, String generateId);
	
	/**
	 * 根据机型Id，报告类型，四大分析的MainId查询所有对应的报告数据
	 * @param modelSeriesId
	 * @param reportType
	 * @param mainId
	 * @return
	 */
	List<ComReport> loadAllReportListNoPage(String modelSeriesId, String reportType, String mainId);
}
