package com.rskytech.report.word.reportArea.utils;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za41;

public class ReportZa41 extends AreaReportBase {

	private Za41 za41;
	
	public ReportZa41(Document document, ComModelSeries ms, ComArea area, Za41 za41) {
		super(document, ms, area);
		this.za41 = za41;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.insertTable(getTableLeft());
		ta.insertTable(getTableRight());
		return ta;
	}
	
	private Table getTableLeft() throws Exception{
		Table ta = setTableAndColumn(1, new float[]{1.0f});
		String str="/com/rskytech/report/word/reportArea/utils/imgs/ZA41.png";
		InputStream stream = ReportZa41.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 370, 300);
		ta.addCell(setCell(img, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, null, 6));
		return ta;
	}
	
	private Table getTableRight() throws Exception{
		String c;
		
		Table ta = setTableAndColumn(2, new float[]{0.3f,0.7f});
		ta.addCell(setCell("步骤", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
		ta.addCell(setCell("说明", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
		
		if (za41.getStep1Desc() != null && !"".equals(za41.getStep1Desc())){
			c = getYesOrNo(za41.getStep1()) + "，" + za41.getStep1Desc();
		} else {
			c = getYesOrNo(za41.getStep1());
		}
		ta.addCell(setCell("\n1\n", fontCnNormal));
		ta.addCell(setCell(getStr(c), fontCnNormal));
		
		if (za41.getStep2Desc() != null && !"".equals(za41.getStep2Desc())){
			c = getYesOrNo(za41.getStep2()) + "，" + za41.getStep2Desc();
		} else {
			c = getYesOrNo(za41.getStep2());
		}
		ta.addCell(setCell("\n2\n", fontCnNormal));
		ta.addCell(setCell(getStr(c), fontCnNormal));
		
		if (za41.getStep3Desc() != null && !"".equals(za41.getStep3Desc())){
			c = getYesOrNo(za41.getStep3()) + "，" + za41.getStep3Desc();
		} else {
			c = getYesOrNo(za41.getStep3());
		}
		ta.addCell(setCell("\n3\n", fontCnNormal));
		ta.addCell(setCell(getStr(c), fontCnNormal));
		
		if (za41.getStep4Desc() != null && !"".equals(za41.getStep4Desc())){
			c = getYesOrNo(za41.getStep4()) + "，" + za41.getStep4Desc();
		} else {
			c = getYesOrNo(za41.getStep4());
		}
		ta.addCell(setCell("\n4\n", fontCnNormal));
		ta.addCell(setCell(getStr(c), fontCnNormal));
		
		ta.addCell(setCell("\n5\n", fontCnNormal));
		ta.addCell(setCell(getStr(za41.getStep5Desc()), fontCnNormal));
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
		return "表3  增强区域分析表一";
	}

	@Override
	public String getTableName() {
		return "区域分析-增强区域分析";
	}
	
	@Override
	public String getTableAbbreviation() {
		return "ZA-2";
	}

}
