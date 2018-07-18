package com.rskytech.report.word.reportLhirf.utils;

import java.io.StringReader;
import com.itextpdf.text.Rectangle;
import com.itextword.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Lh2;
import com.rskytech.pojo.LhMain;

public class ReportLh2 extends LhirfReportBase {

	private Lh2 lh2;
	
	public ReportLh2(Document document, ComModelSeries ms,
			ComArea area, LhMain lhMain, String effective, Lh2 lh2) {
		super(document,  ms, area, lhMain, effective);
		this.lh2 = lh2;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.insertTable(getTableLeft());
		ta.insertTable(getTableRight());
		return ta;
	}
	
	public Table getTableLeft() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		
		ta.addCell(setCell(getStr("区域环境说明："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null,0,Rectangle.RIGHT));
		
		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null,0,Rectangle.RIGHT));
		Table ta1 = setTableAndColumn(1, null);
		HTMLWorker worker = new HTMLWorker(this.document, ta1);
		worker.parse(new StringReader(getStr(lh2.getPicContent()))); 		
		ta.addCell(setCell(getStr(lh2.getEnv()), fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, worker.getRows()));
		return ta;
	}
	
	public Table getTableRight() throws Exception {
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		
		ta.addCell(setCell(getStr("安装位置原理："), fontNowTitle, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null,0,null));
		
		ta.addCell(setCell("", fontNowNormal, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, null, null,0,null));
		
		HTMLWorker worker = new HTMLWorker(this.document, ta);
		worker.parse(new StringReader(getStr(lh2.getPicContent())));
		return ta;
	}

	@Override
	public int getCol() {
		return 2;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.4f, 0.6f};
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-2";
	}

	@Override
	public String getTableName() {
		return "L/HIRF防护部件信息说明(二)";
	}

}
