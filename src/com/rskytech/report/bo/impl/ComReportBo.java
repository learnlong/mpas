package com.rskytech.report.bo.impl;

import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComReport;
import com.rskytech.report.bo.IComReportBo;
import com.rskytech.report.dao.IComReportDao;

public class ComReportBo extends BaseBO implements IComReportBo {
	
	private IComReportDao comReportDao;
	
	@Override
	public Page loadComReportList(Page page, String modelSeriesId,
			String reportType, String generateId) {
		return this.comReportDao.loadComReportList(page, modelSeriesId, reportType, generateId);
	}
	
	@Override
	public List<ComReport> loadAllReportListNoPage(String modelSeriesId,
			String reportType, String mainId) {
		return this.comReportDao.loadAllReportListNoPage(modelSeriesId, reportType, mainId);
	}
	
	public IComReportDao getComReportDao() {
		return comReportDao;
	}

	public void setComReportDao(IComReportDao comReportDao) {
		this.comReportDao = comReportDao;
	}

}
