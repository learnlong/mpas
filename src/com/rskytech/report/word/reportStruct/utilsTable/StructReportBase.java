package com.rskytech.report.word.reportStruct.utilsTable;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.report.word.ReportBase;

public abstract class StructReportBase extends ReportBase {

	public StructReportBase(Document document) {
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
