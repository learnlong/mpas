package com.rskytech.report.excel.mpd.bo;

import com.rskytech.pojo.ComModelSeries;

public interface IMpdExcelBo {

	public String createMpdExcel(String reportName,  ComModelSeries comModelSeries);
}
