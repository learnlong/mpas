package com.rskytech.report.word.reportArea.utils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.Za2;

public class ReportZa1 extends AreaReportBase {

	private Za1 za1;
	private Za2 za2;
	
	public ReportZa1(Document document, ComModelSeries ms, ComArea area, Za1 za1, Za2 za2) {
		super(document, ms, area);
		this.za1 = za1;
		this.za2 = za2;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		String hasStrucOnlyYes = (za1.getHasStrucOnly() == 1 ? "√" : "");
		String hasStrucOnlyNo = (za1.getHasStrucOnly() == 0 ? "√" : "");
		String needAreaAnalyzeYes = (za1.getNeedAreaAnalyze() == 1 ? "√" : "");
		String needAreaAnalyzeNo = (za1.getNeedAreaAnalyze() == 0 ? "√" : "");
		String hasPipe = (za1.getHasPipe() == 1 ? "√" : "");
		String hasMaterial = (za1.getHasMaterial() == 1 ? "√" : "");
		String closeToSystem = (za1.getCloseToSystem() == 1 ? "√" : "");
		
		String buWei = "";
		if (za2.getPosition() == 1){
			buWei = "内部";
		} else if (za2.getPosition() == 2){
			buWei = "外部";
		} else if (za2.getPosition() == 3){
			buWei = "内部；外部";
		}
		
		Table ta = setTableAndColumn(getCol(), getColWidth());
		
		ta.addCell(setCell(getStr("边界：" + za1.getBorder()), fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE));
		ta.addCell(setCell("部位：" + buWei, fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE));
		
		ta.addCell(setCell("区域说明", fontCnNormal, Element.ALIGN_CENTER,  Element.ALIGN_MIDDLE, 2, null));
		
		ta.addCell(setCell(getStr("环境：" + za1.getEnv()), fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE, 2, null));
		
		ta.addCell(setCell("区域包括点火源/导线吗？     [ " + hasPipe + " ]                              区域中有可燃材料的堆积吗？     [ " + hasMaterial + " ]\n" 
				+ "导线是否同时靠近液压、机械或电飞控系统的主要及备份系统？     [ " + closeToSystem + " ]", fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE, 2, null));
		
		ta.addCell(setCell(getStr("接近方式：" + za1.getReachWay()), fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE, 2, null));
		
		ta.addCell(setCell("设备/结构项目：详见表1\n" + "此区域只包含结构吗？       是 [ " + hasStrucOnlyYes +" ]  否 [ " + hasStrucOnlyNo + " ]\n"
				+ "是否需要进行区域分析？     是 [ " + needAreaAnalyzeYes + " ]  否 [ " + needAreaAnalyzeNo + " ]", fontCnNormal, Element.ALIGN_LEFT,  Element.ALIGN_MIDDLE, 2, null));
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
	public String getReportName() {
		return "表2  " + area.getAreaCode() + "区域分析数据表";
	}

	@Override
	public String getTableName() {
		return "区域分析-数据表";
	}
	
	@Override
	public String getTableAbbreviation() {
		return "ZA-1";
	}

}
