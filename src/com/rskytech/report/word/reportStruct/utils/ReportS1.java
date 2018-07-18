package com.rskytech.report.word.reportStruct.utils;

import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.struct.bo.IS1Bo;

public class ReportS1 extends StructReportBase{

	private ComModelSeries ms;
	private SMain sMain;
	private String ssiCode;
	private String ssiName;
	private IComAreaBo comAreaBo;
	private IS1Bo s1Bo;
	
	public ReportS1(Document document,ComModelSeries ms, SMain sMain, String ssiCode, String ssiName, IComAreaBo comAreaBo, IS1Bo s1Bo) {
		super(document);
		this.ms=ms;
		this.sMain=sMain;
		this.ssiCode=ssiCode;
		this.ssiName=ssiName;
		this.comAreaBo=comAreaBo;
		this.s1Bo = s1Bo;
	}
	
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(),getColWidth());
		ta.addCell(setCell("2     以可靠性为中心的维修分析（RCMA）", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 12, 2, 0, null));
		ta.addCell(setCell("2.1   "+ssiName+"项目的分类", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 12, 2, 0, null));
		ta.addCell(setCell("     按照"+ssiName+"，列出前起落架重要结构项目，见表3。", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 12, 1, 0, null));
		ta.addCell(setCell("", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 12, 1, 0, null));
		ta.addCell(setCell("表3   主要结构组成表", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 12, 2, 0, null));
		ta.addCell(setCell("飞机型号："+ms.getModelSeriesCode()+"                                            专业：结构   " +
				"                                                       共  页，第  页", 
				fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_CENTER, 12, 1, 0, null));
		
		ta.addCell(setCell("结构项目名称/标识码",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 4, 1, null, null));
		ta.addCell(setCell(ssiName+"/"+ssiCode,fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
		
		ta.addCell(setCell("图  号",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
		ta.addCell(setCell(getStr(sMain.getComAta()==null?"":sMain.getComAta().getEquipmentPicNo()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 4, 1, null, null));
		
		ta.addCell(setCell("序号",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("重要结构项目名称",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("标识码",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("图号",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("安装位置",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("SSI组成",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("区域",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("是否金属",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("内部",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("外部",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("设计原则",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("备注",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		
		int i=0;
		List<S1> setS1 = s1Bo.getS1Records(sMain.getSsiId());
		if(setS1!=null&&setS1.size()>0){
			for(S1 s1 : setS1){
				i++;
				ta.addCell(setCell(String.valueOf(i),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(ssiName,fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(ssiCode,fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(sMain.getComAta()==null?"":sMain.getComAta().getEquipmentPicNo()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(sMain.getComAta()==null?"":sMain.getComAta().getEquipmentPosition()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(s1.getSsiForm()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(comAreaBo.getAreaCodeByAreaId(s1.getOwnArea())),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(s1.getIsMetal()==0?"否":"是"),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(s1.getInternal()==0?"否":"是"),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(s1.getOuternal()==0?"否":"是"),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(getStr(s1.getDesignPri()==2?"损伤容限设计":"安全寿命设计"),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
			}
		}else{
			for(int a=1;a<=3;a++){
				ta.addCell(setCell(String.valueOf(a),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
			}
		}
		return ta;
	}

	@Override
	public String getReportName() {
		return ms.getModelSeriesName();
	}

	@Override
	public int getCol() {
		return 12;
	}

	@Override
	public float[] getColWidth() {
		return null;
	}


}
