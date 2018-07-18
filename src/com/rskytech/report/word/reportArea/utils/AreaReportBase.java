package com.rskytech.report.word.reportArea.utils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.report.word.ReportBase;

public abstract class AreaReportBase extends ReportBase {

	public ComModelSeries ms;
	public ComArea area;
	
	public AreaReportBase(Document document, ComModelSeries ms, ComArea area) {
		super(document);
		this.area = area;
		this.ms = ms;
	}
	
	public Table getTableTop() throws Exception {
		Table ta = setTableAndColumn(3, new float[]{0.24f, 0.38f, 0.38f});
		
		ta.addCell(setCell(getReportName(), fontCnTitle, Element.ALIGN_CENTER,  Element.ALIGN_MIDDLE, 3, null, 0, null));
		
		ta.addCell(setCell("型号：" + ms.getModelSeriesCode(), fontCnTitle));
		ta.addCell(setCell("RCM分析", fontCnTitle));
		ta.addCell(setCell("区域号: " + area.getAreaCode(), fontCnTitle));
	
		ta.addCell(setCell(getTableAbbreviation(), fontCnTitle));
		ta.addCell(setCell(getTableName(), fontCnTitle));
		ta.addCell(setCell("区域名称: " + area.getAreaName(), fontCnTitle));
	
		return ta;
	}
	
	public Table getTableBottom() throws Exception {
		Table ta = setTableAndColumn(2, new float[]{0.5f, 0.5f});
		
		ta.addCell(setCell("区域（ATA）：", fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE));
		ta.addCell(setCell("第      页             共      页", fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE));
		return ta;
	}
}
