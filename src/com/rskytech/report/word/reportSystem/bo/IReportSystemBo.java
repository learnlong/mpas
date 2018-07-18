package com.rskytech.report.word.reportSystem.bo;

import com.richong.arch.bo.IBo;

public interface IReportSystemBo extends IBo {
	/**
	 * 创建system报表文件
	 * @param msiId
	 * @param reportName 
	 */
	public String createReport(String msiId, String reportName);
}
