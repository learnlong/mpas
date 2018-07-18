package com.rskytech.report.word.reportArea.utils;

import java.io.StringReader;

import com.itextpdf.text.Element;
import com.itextword.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za2;

public class ReportZa2 extends AreaReportBase {

	private Za2 za2;
	
	public ReportZa2(Document document, ComModelSeries ms, ComArea area, Za2 za2) {
		super(document, ms, area);
		this.za2 = za2;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(1, null);
		ta.setBorder(0);
		ta.insertTable(getTitle());
		ta.insertTable(getZa2Table());
		return ta;
	}
	
	public Table getTitle() throws Exception {
		Table ta = setTableAndColumn(1, null);
		ta.setBorder(0);
		ta.addCell(setCell("区域检查分析报告", fontCnLarge, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0, null));
		ta.addCell(setCell("1  " + ms.getModelSeriesCode() + "区域概述", fontCnTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		return ta;
	}
	
	public Table getZa2Table() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		ta.setBorder(0);
		if (za2 != null){
			HTMLWorker worker = new HTMLWorker(this.document, ta);
			worker.parse(new StringReader(za2.getPicContent()));
		} else {
			ta.addCell(setCell("", fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_TOP, 1, 4, 0, null));
		}
		return ta;
	}
	
	@Override
	public int getCol() {
		return 0;
	}

	@Override
	public float[] getColWidth() {
		return null;
	}

	@Override
	public String getReportName() {
		return null;
	}

	@Override
	public String getTableAbbreviation() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public Table getTableTop() {
		return null;
	}
	
	@Override
	public Table getTableBottom() {
		return null;
	}
}
