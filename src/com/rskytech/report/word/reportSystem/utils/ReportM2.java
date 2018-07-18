package com.rskytech.report.word.reportSystem.utils;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MMain;

public class ReportM2 extends SystemReportBase{
	private ComModelSeries ms;
	private MMain mMain;
	private M13F m13f;
	public ReportM2(Document document, ComModelSeries ms, MMain mMain, M13F m13f){
		super(document);
		this.ms=ms;
		this.mMain=mMain;
		this.m13f=m13f;
	}
	@Override
	public Table getTableContent() throws Exception {
		M2 m2;
		String failureCauseType="";
		String failureCauseValue="";
		String Q1 = "";
		String Q2 = "";
		String Q3 = "";
		String Q4 = "";
		String Q5 = "";
		String Q1Desc = "";
		String Q2Desc = "";
		String Q3Desc = "";
		String Q4Desc = "";
		String Q5Desc = "";
		ArrayList<String> arr = new ArrayList<String>();
		ArrayList<String> arrDesc = new ArrayList<String>();
		Set<M2> setM2 = m13f.getM2s();
		Iterator<M2> it = setM2.iterator();
		while(it.hasNext()){
			m2 = it.next();
			failureCauseType = String.valueOf(m2.getFailureCauseType());
			if(failureCauseType.equals("6")){
				failureCauseValue = "明显安全性";
				Q1 = "是";
				Q1Desc = m2.getQ1Desc();
				Q2 = "是";
				Q2Desc = m2.getQ2Desc();
			}else if(failureCauseType.equals("7")){
				failureCauseValue = "明显任务性";
				Q1 = "是";
				Q1Desc = m2.getQ1Desc();
				Q2 = "否";
				Q2Desc = m2.getQ2Desc();
				Q4 = "是";
				Q4Desc = m2.getQ4Desc();
			}else if(failureCauseType.equals("8")){
				failureCauseValue = "明显经济性";
				Q1 = "是";
				Q1Desc = m2.getQ1Desc();
				Q2 = "否";
				Q2Desc = m2.getQ2Desc();
				Q4 = "否";
				Q4Desc = m2.getQ4Desc();
			}else if(failureCauseType.equals("9")){
				failureCauseValue = "隐蔽安全性";
				Q1 = "否";
				Q1Desc = m2.getQ1Desc();
				Q3 = "是";
				Q3Desc = m2.getQ3Desc();
			}else if(failureCauseType.equals("10")){
				failureCauseValue = "隐蔽任务性";
				Q1 = "否";
				Q1Desc = m2.getQ1Desc();
				Q3 = "否";
				Q3Desc = m2.getQ3Desc();
				Q5 = "是";
				Q5Desc = m2.getQ7Desc();
			}else if(failureCauseType.equals("11")){
				failureCauseValue = "隐蔽经济性";
				Q1 = "否";
				Q1Desc = m2.getQ1Desc();
				Q3 = "否";
				Q3Desc = m2.getQ3Desc();
				Q5 = "否";
				Q5Desc = m2.getQ7Desc();
			}
		}
		arr.add(Q1);
		arr.add(Q2);
		arr.add(Q3);
		arr.add(Q4);
		arr.add(Q5);
		arrDesc.add(Q1Desc);
		arrDesc.add(Q2Desc);
		arrDesc.add(Q3Desc);
		arrDesc.add(Q4Desc);
		arrDesc.add(Q5Desc);
		Table ta = setTableAndColumn(getCol(), getColWidth());
		ta.addCell(setCell("  "+getReportName()+"系统RCMA 分析决断", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_TOP, 13, 1, 0, null));
		ta.addCell(setCell(getReportName()+"故障影响类型分析表（第一层次决断）", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 13,2, 0, null));
		ta.addCell(setCell("系统名称", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getAtaName()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("系统图（代）号", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentPicNo()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 8,2, null, null));
		ta.addCell(setCell("产品标识码", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getAtaCode()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("产品名称", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentName()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 8,2, null, null));
		ta.addCell(setCell("故障模式（来源于FMEA分析）", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,2, null, null));
		ta.addCell(setCell(getStr(m13f.getEffectDesc()), fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 10,2, null, null));
		ta.addCell(setCell("故障影响问题", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 5,1, null, null));
		ta.addCell(setCell("问题", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
		ta.addCell(setCell("回答", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
		ta.addCell(setCell("说明", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 4,1, null, null));
		
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,13, 0, Rectangle.LEFT));
		String str = "/com/rskytech/report/word/reportSystem/utils/imgs/" + failureCauseType + ".png";
		InputStream stream = ReportM2.class.getResourceAsStream(str); 
		Image img = Image.getInstance(IOUtils.toByteArray(stream));	
		stream.close();
		img = getScale(img, 470, 400);
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, 0, null));//分隔
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,13, 0, null));//分隔
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,13, 0, null));//分隔
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 6,1, 0, null));//分隔
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,13, null, Rectangle.RIGHT));//最靠近右边的列
		ta.addCell(setCell(img, 
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,12, 0, null));
		for(int a=1;a<=5;a++){
			ta.addCell(setCell(String.valueOf(a), fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			ta.addCell(setCell(arr.get(a-1), fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 2,1, null, null));
			ta.addCell(setCell(arrDesc.get(a-1), fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, null, null));
		}
		ta.addCell(setCell("故障影响类别", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, null, null));
		ta.addCell(setCell(failureCauseValue, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, null, null));
		ta.addCell(setCell("故障影响类别编号", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, null, null));
		ta.addCell(setCell(failureCauseType, fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,1, null, null));
		
		ta.addCell(setCell("注意：故障影响类别与编号对应表", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 6,2, null, null));
		ta.addCell(setCell("明显\n安全", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("明显\n任务", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("明显\n经济", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n安全", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n任务", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("隐蔽\n经济", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 1,2, null, null));
		ta.addCell(setCell("6",fontCnNormal));
		ta.addCell(setCell("7",fontCnNormal));
		ta.addCell(setCell("8",fontCnNormal));
		ta.addCell(setCell("9",fontCnNormal));
		ta.addCell(setCell("10",fontCnNormal));
		ta.addCell(setCell("11",fontCnNormal));
		ta.addCell(setCell("", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_BOTTOM, 13,1,0, Rectangle.LEFT|Rectangle.RIGHT|Rectangle.BOTTOM));
		return ta;
	}
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 13;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.01f,0.15f,0.3f,0.2f,0.05f,0.02f,0.06f,0.06f,0.06f,0.06f,0.06f,0.06f,0.01f};
	}

}
