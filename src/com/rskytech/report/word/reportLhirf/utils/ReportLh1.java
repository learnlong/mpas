package com.rskytech.report.word.reportLhirf.utils;

import java.io.StringReader;

import com.itextword.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Lh1;
import com.rskytech.pojo.LhMain;

public class ReportLh1 extends LhirfReportBase {

	private Lh1 lh1;
	
	public ReportLh1(Document document,  ComModelSeries ms, ComArea area, LhMain lhMain, String effective, Lh1 lh1) {
		super(document,  ms, area, lhMain, effective);
		this.lh1 = lh1;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());

		HTMLWorker worker = new HTMLWorker(this.document, ta);
		worker.parse(new StringReader(getStr(lh1.getPicContent())));
		return ta;
	}

	@Override
	public int getCol() {
		return 1;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{1.0f};
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-1";
	}

	@Override
	public String getTableName() {
		return "L/HIRF防护部件信息说明(一)：部件性能描述与屏蔽/搭接通路描述";
	}
}
