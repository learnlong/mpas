package com.rskytech.report.word.reportLhirf.utils;

import java.io.StringReader;

import com.itextword.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.LhMain;

public class ReportLh1a extends LhirfReportBase {

	private Lh1a lh1a;
	
	public ReportLh1a(Document document, ComModelSeries ms, ComArea area, LhMain lhMain, String effective, Lh1a lh1a) {
		super(document, ms, area, lhMain, effective);
		this.lh1a = lh1a;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell(getStr("HSI引用：") + lhMain.getRefHsiCode(), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell(getStr("部件性能相似性说明："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		ta.addCell(setCell(getStr(lh1a.getContent1()), fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));

		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell(getStr("屏蔽/搭接通路相似性说明："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		ta.addCell(setCell(getStr(lh1a.getContent2()), fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell(getStr("区域环境相似性说明："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		ta.addCell(setCell(getStr(lh1a.getContent3()), fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		ta.addCell(setCell(getStr("部件信息说明："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null, 0, null));
		
		HTMLWorker worker = new HTMLWorker(this.document, ta);
		worker.parse(new StringReader(getStr(lh1a.getPicContent())));
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
		return "LH-1A";
	}

	@Override
	public String getTableName() {
		return "L/HRIF防护部件信息说明";
	}
}
