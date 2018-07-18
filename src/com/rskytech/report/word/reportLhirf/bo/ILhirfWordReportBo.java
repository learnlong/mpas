package com.rskytech.report.word.reportLhirf.bo;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.LhMain;

public interface ILhirfWordReportBo extends IBo {

	/**
	 * @param LhMain lhirf主表
	 */
	public String createReport(LhMain lhMain,String reportName);
}
