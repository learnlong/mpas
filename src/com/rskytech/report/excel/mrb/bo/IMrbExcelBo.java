package com.rskytech.report.excel.mrb.bo;

import com.rskytech.pojo.ComModelSeries;

public interface IMrbExcelBo {
	public String createMrbExcel(String reportName, ComModelSeries comModelSeries);
}
