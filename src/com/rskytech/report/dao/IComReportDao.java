package com.rskytech.report.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComReport;

public interface IComReportDao extends IDAO {

	public Page loadComReportList(Page page, String modelSeriesId, String reportType, String generateId);
	/**
	 * 根据机型Id，报告类型，四大分析的MainId查询所有对应的报告数据
	 * @param modelSeriesId
	 * @param reportType
	 * @param mainId
	 * @return
	 */
	List<ComReport> loadAllReportListNoPage(String modelSeriesId, String reportType, String mainId);
}
