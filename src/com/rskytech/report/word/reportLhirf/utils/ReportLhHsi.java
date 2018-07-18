package com.rskytech.report.word.reportLhirf.utils;

import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.LhMain;

public class ReportLhHsi extends LhirfReportBase {

	private List<LhMain> list;
	
	public ReportLhHsi(Document document, ComModelSeries ms,
			ComArea area, LhMain lhMain, String effective, List<LhMain> list) {
		super(document, ms, area, lhMain, effective);
		this.list = list;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell(getStr("HSI编号"), fontNowTitle));
		ta.addCell(setCell(getStr("HSI名称"), fontNowTitle));
		ta.addCell(setCell(getStr("保护系统/组件"), fontNowTitle));
		ta.addCell(setCell(getStr("ATA编号"), fontNowTitle));
		ta.addCell(setCell(getStr("IPV/OPVP/OPVE"), fontNowTitle));
		ta.addCell(setCell(getStr("HSI引用"), fontNowTitle));
		
		if (list != null && list.size() > 0){
			for (LhMain l : list){
				ta.addCell(setCell(getStr(l.getHsiCode()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getHsiName()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getLhCompName()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getAtaCode()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getIpvOpvpOpve()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getRefHsiCode()), fontNowNormal));
			}
		} else {
			for (int i = 0; i < getCol() * 4; i++){
				ta.addCell(setCell("", fontNowNormal));
			}
		}
		return ta;
	}
	
	@Override
	public int getCol() {
		return 6;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.12f, 0.26f, 0.26f, 0.12f, 0.12f, 0.12f};
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-0";
	}

	@Override
	public String getTableName() {
		return "项目选择和相似性评价";
	}

}
