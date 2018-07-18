package com.rskytech.report.word.reportLhirf.utils;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.LhMain;
import com.rskytech.report.word.ReportBase;

public abstract class LhirfReportBase extends ReportBase {

	public ComModelSeries ms;
	public ComArea area;
	public LhMain lhMain;
	public String effective; // 适用性

	public LhirfReportBase(Document document, ComModelSeries ms, ComArea area,
			LhMain lhMain, String effective) {
		super(document);
		this.ms = ms;
		this.area = area;
		this.lhMain = lhMain;
		this.effective = effective;

		fontNowTitle = fontCnTitle;
		fontNowNormal = fontCnNormal;
		fontNowLarge = fontCnLarge;
	}

	/**
	 * 构建系统表格中默认表头
	 */
	public Table getTableTop() throws Exception {
		if (lhMain != null && lhMain.getHsiId() != null && ! "".equals(lhMain.getHsiId())) {// 含有HSI信息的表头
			return getTableTopHsi();
		} else {// 含有区域信息的表头
			return getTableTopArea();
		}
	}

	/**
	 * 构建含有HSI信息的表头
	 */
	public Table getTableTopHsi() throws Exception {
		Table ta = setTableAndColumn(3, new float[] { 0.2f, 0.5f, 0.3f });

		if (isTe) {
			ta.addCell(setCell(getStr(ms.getModelSeriesName()), fontNowTitle,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
					Rectangle.RIGHT | Rectangle.BOTTOM));
			ta.addCell(setCell(getReportName(), fontNowTitle,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
					Rectangle.RIGHT | Rectangle.BOTTOM));
			Cell cell = new Cell(new Phrase(getStr("HSI编号：")
					+ lhMain.getHsiCode(), fontNowTitle));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.enableBorderSide(Rectangle.BOTTOM);
			ta.addCell(cell);
		} else {
			ta.addCell(setCell(getStr(ms.getModelSeriesName()), fontNowTitle));
			ta.addCell(setCell(getReportName(), fontNowTitle));
			ta.addCell(setCell(getStr("HSI编号：") + lhMain.getHsiCode(),
					fontNowTitle));
		}

		if (isTe) {
			ta.addCell(setCell(getTableAbbreviation(), fontNowTitle,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
					Rectangle.RIGHT | Rectangle.BOTTOM));
			ta.addCell(setCell(getTableName(), fontNowTitle,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, null, 0,
					Rectangle.RIGHT | Rectangle.BOTTOM));
			Cell cell = new Cell(new Phrase(getStr("HSI名称："
					+ lhMain.getHsiName()), fontNowTitle));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.enableBorderSide(Rectangle.BOTTOM);
			ta.addCell(cell);
		} else {
			ta.addCell(setCell(getTableAbbreviation(), fontNowTitle));
			ta.addCell(setCell(getTableName(), fontNowTitle));
			ta.addCell(setCell(getStr("HSI名称：" + lhMain.getHsiName()),
					fontNowTitle));
		}

		if (isTe) {
			Cell cell = new Cell(new Phrase(getStr("适用性：" + effective),
					fontNowTitle));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.enableBorderSide(Rectangle.BOTTOM);
			cell.setColspan(3);
			ta.addCell(cell);
		} else {
			ta.addCell(setCell(getStr("适用性：" + effective), fontNowTitle,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3, null));
		}
		return ta;
	}

	/**
	 * 构建含有区域信息的表头
	 */
	public Table getTableTopArea() throws Exception {
		Table ta = setTableAndColumn(4, new float[] { 0.22f, 0.356f, 0.108f,
				0.216f });

		ta.addCell(setCell(getStr(ms.getModelSeriesName()), fontNowTitle));
		ta.addCell(setCell(getReportName(), fontNowTitle));
		ta.addCell(setCell(getStr("区域编号：" + area.getAreaCode()), fontNowTitle,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 2));
		if (isTe) {
			Cell cell = new Cell(new Phrase(
					getStr("区域名称：" + area.getAreaName()), fontNowTitle));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setRowspan(2);
			cell.enableBorderSide(Rectangle.BOTTOM);
			ta.addCell(cell);
		} else {
			ta.addCell(setCell(getStr("区域名称：" + area.getAreaName()),
					fontNowTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE,
					null, 2));
		}

		ta.addCell(setCell(getTableAbbreviation(), fontNowTitle));
		ta.addCell(setCell(getTableName(), fontNowTitle));

		return ta;
	}

	/**
	 * 构建系统表格中默认表底
	 */
	public Table getTableBottom() throws Exception {
		return null;
	}

	/**
	 * 得到当前报告的名称
	 */
	public String getReportName() {
		return "L/HIRF MSG-3 分析";
	}
}
