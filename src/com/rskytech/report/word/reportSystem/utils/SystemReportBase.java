package com.rskytech.report.word.reportSystem.utils;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.report.word.ReportBase;

public abstract class SystemReportBase extends ReportBase {

	public SystemReportBase(Document document) {
		super(document);
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public String getTableAbbreviation() {
		return null;
	}
	@Override
	public Table getTableTop() throws Exception {
		return null;
	}
	@Override
	public Table getTableBottom() throws Exception {
		return null;
	}
}
