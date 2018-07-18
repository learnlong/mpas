package com.rskytech.report.word.reportArea.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;

public class ReportZa7 extends AreaReportBase {

	private List<Object[]> za7List;
	
	public ReportZa7(Document document, ComModelSeries ms, ComArea area, List<Object[]> za7List) {
		super(document, ms, area);
		this.za7List = za7List;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell("来自其它工作组的候选任务", fontCnTitle, Element.ALIGN_CENTER,  Element.ALIGN_MIDDLE, getCol(), null));
		
		ta.addCell(setCell("提出人", fontCnNormal));
		ta.addCell(setCell("单位", fontCnNormal));
		ta.addCell(setCell("SSI/MSI号", fontCnNormal));
		ta.addCell(setCell("接收人", fontCnNormal));
		ta.addCell(setCell("单位", fontCnNormal));
		ta.addCell(setCell("间隔", fontCnNormal));
		ta.addCell(setCell("转移原因", fontCnNormal));
		ta.addCell(setCell("接受否？", fontCnNormal));
		ta.addCell(setCell("不接受的原因", fontCnNormal));
		ta.addCell(setCell("任务描述", fontCnNormal));
		if(za7List!=null){
			for (int i = 0; i < za7List.size(); i++){
				Object[] obj = za7List.get(i);
				ta.addCell(setCell(getStr(obj[8]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[7]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[1]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[10]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[9]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[3]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[4]), fontCnNormal));
				
				if ("1".equals(getStr(obj[5]))){
					ta.addCell(setCell("是", fontCnNormal));
				} else if ("0".equals(getStr(obj[5]))){
					ta.addCell(setCell("否", fontCnNormal));
				} else {
					ta.addCell(setCell("", fontCnNormal));
				}
				
				ta.addCell(setCell(getStr(obj[6]), fontCnNormal));
				ta.addCell(setCell(getStr(obj[2]), fontCnNormal));
			}
		}
		return ta;
	}
	
	@Override
	public int getCol() {
		return 10;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f};
	}

	@Override
	public String getReportName() {
		return "表7  候选转移项目合并";
	}

	@Override
	public String getTableAbbreviation() {
		return "ZA-5";
	}

	@Override
	public String getTableName() {
		return "区域分析-候选转移项目合并";
	}

}
