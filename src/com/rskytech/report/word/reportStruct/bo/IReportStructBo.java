package com.rskytech.report.word.reportStruct.bo;

import com.richong.arch.bo.IBo;

public interface IReportStructBo extends IBo {
	public String createReport(String ssiId, String reportName,String userId);
}

