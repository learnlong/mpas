package com.rskytech.report.word.reportArea.bo;

import com.richong.arch.bo.IBo;

public interface IReportAreaBo extends IBo {

	/**
	 * 创建区域报表文件
	 * @param zaId
	 * @param reportName 
	 */
	public String createReport(String zaId, String reportName);
}
