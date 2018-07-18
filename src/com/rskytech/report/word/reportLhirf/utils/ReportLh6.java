package com.rskytech.report.word.reportLhirf.utils;

import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.LhMain;

public class ReportLh6 extends LhirfReportBase {

	private List list;
	
	public ReportLh6(Document document,  ComModelSeries ms,
			ComArea area, LhMain lhMain, String effective, List list) {
		super(document, ms, area, lhMain, effective);
		this.list = list;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell(getStr("HSI编号"), fontNowTitle));
		ta.addCell(setCell(getStr("MSG-3任务编号"), fontNowTitle));
		ta.addCell(setCell(getStr("任务类型"), fontNowTitle));
		ta.addCell(setCell(getStr("IPV/OPVP/OPVE"), fontNowTitle));
		ta.addCell(setCell(getStr("间隔值"), fontNowTitle));
		ta.addCell(setCell(getStr("接近方式"), fontNowTitle));
		ta.addCell(setCell(getStr("任务描述"), fontNowTitle));
		ta.addCell(setCell(getStr("是否区域候选任务"), fontNowTitle));
		ta.addCell(setCell(getStr("区域是否接受"), fontNowTitle));
		ta.addCell(setCell(getStr("退回原因"), fontNowTitle));
		ta.addCell(setCell(getStr("适用性"), fontNowTitle));
		
		for (int i = 0; list != null && i < list.size(); i++) {
			Object obj[] = (Object[]) list.get(i);
			ta.addCell(setCell(getStr(obj[1]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[3]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[4]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[5]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[8]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[6]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[7]), fontNowNormal));
			ta.addCell(setCell(getStr(getYesOrNo(obj[9] == null ? null : Integer.valueOf(obj[9].toString()))), fontNowNormal));
			ta.addCell(setCell(getStr(hasAccept(obj[9], obj[10])), fontNowNormal));
			ta.addCell(setCell(getStr(obj[11]), fontNowNormal));
			ta.addCell(setCell(getStr(obj[12]), fontNowNormal));
		}
		return ta;
	}
	
	private String hasAccept(Object obj11, Object obj12) {
		String str = "N/A";
		if (obj11 != null && "1".equals(obj11.toString())) {
			if (obj12 == null) {
				str = "待处理";
			} else if ("0".equals(obj12.toString())) {
				str = "否";
			} else if ("1".equals(obj12.toString())) {
				str = "是";
			} else if ("2".equals(obj12.toString())) {
				str = "待定";
			}
		}
		return str;
	}

	@Override
	public int getCol() {
		return 11;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.1f, 0.12f, 0.07f, 0.07f, 0.09f, 0.14f, 0.14f, 0.06f, 0.06f, 0.07f, 0.08f};
	}

	@Override
	public String getTableAbbreviation() {
		return "LH-6";
	}

	@Override
	public String getTableName() {
		return "任务汇总";
	}

}
