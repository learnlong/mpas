package com.rskytech.report.word.reportSystem.utils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.MMain;

public class ReportMT extends SystemReportBase{
	private ComModelSeries ms;
	private MMain mMain;
	public ReportMT(Document document, ComModelSeries ms, MMain mMain) {
		super(document);
		this.ms=ms;
		this.mMain=mMain;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(1, null);
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("X X X技术文件", fontCnBigLarge,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("X X X", fontCnBigLarge,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("X X X X X X X X", fontCnBigLarge,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("设计：_____________", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnTitle,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("校对：_____________", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnTitle,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("审核：_____________", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("", fontCnTitle,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, 0, null));
		ta.addCell(setCell("批准：_____________", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, 0, null));
		return ta;
	}
	
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 1;
	}

	@Override
	public float[] getColWidth() {
		return null;
	}


}
