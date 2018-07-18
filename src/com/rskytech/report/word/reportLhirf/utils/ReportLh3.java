package com.rskytech.report.word.reportLhirf.utils;

import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Lh3;
import com.rskytech.pojo.LhMain;

public class ReportLh3 extends LhirfReportBase {

	private List<Lh3> list;
	
	public ReportLh3(Document document, ComModelSeries ms,
			ComArea area, LhMain lhMain, String effective, List <Lh3> list) {
		super(document, ms, area, lhMain, effective);
		this.list = list;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell(getStr("退化模式"), fontNowTitle));
		ta.addCell(setCell(getStr("退化说明"), fontNowTitle));
		
		if (list != null && list.size() > 0){
			for (Lh3 l : list){
				ta.addCell(setCell(getStr(l.getDefectModel()), fontNowNormal));
				ta.addCell(setCell(getStr(l.getDefectDesc()), fontNowNormal));
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
		return 2;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.5f, 0.5f};
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-3";
	}

	@Override
	public String getTableName() {
		return "HSI功能退化模式分析";
	}

}
