package com.rskytech.report.word.reportStruct.utils;


import java.io.StringReader;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Element;
import com.itextword.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.Document;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.SMain;

public class ReportCover extends StructReportBase{
	private ComModelSeries ms;
	private List<SMain> list;
	private S2 s2;
	
	public ReportCover(Document document,ComModelSeries ms, List<SMain> list, S2 s2) {
		super(document);
		this.ms=ms;
		this.list=list;
		this.s2=s2;
	}

	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(),getColWidth());
		Table ta2 = setTableAndColumn(getCol(),getColWidth());
		ta2.addCell(setCell(getReportName()+"保障性分析报告", fontCnLarge,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1, 3,0, null));
		ta2.addCell(setCell("1    前起落架介绍", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 2, 0, null));
		ta2.addCell(setCell("1.1    概述", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 2, 0, null));
		ta.addCell(setCell("", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 8,0, null));
		
		ta.addCell(setCell("1.2   "+getReportName()+"维修性分析", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 2, 0, null));
		ta.addCell(setCell("1.2.1   "+getReportName()+"组成层次图", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 2, 0, null));
		ta.addCell(setCell("", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 12,0, null));
		ta.addCell(setCell("", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 1,0, null));
		ta.addCell(setCell("", fontCnTitle,Element.ALIGN_LEFT, Element.ALIGN_CENTER, 1, 1,0, null));
		
		ta.insertTable(getTableContent1());
		
		Table ta3 = setTableAndColumn(1,null);
		ta3.insertTable(ta2);
		ta3.insertTable(getS2Table());
		ta3.insertTable(ta);
		return ta3;
	}
	
	private Table getS2Table() throws Exception {
		Table ta1 = setTableAndColumn(1,new float[]{1.0f});
		ta1.setBorder(0);
		if(s2!=null){
			HTMLWorker worker = new HTMLWorker(this.document,ta1);
			worker.parse( new StringReader( s2.getPicContent() ) );
		}else{
			ta1.addCell(setCell("", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_TOP, 1,4, 0, null));
		}
		return ta1;
	}

	@SuppressWarnings("unchecked")
	public Table getTableContent1() throws Exception {
		Table ta = setTableAndColumn(7,new float[]{0.1f,0.1f,0.08f,0.18f,0.18f,0.18f,0.18f});
		SMain sMain = list.get(0);
		ta.addCell(setCell("1.2.2   "+getReportName()+"结构组成", fontCnTitle, 
				Element.ALIGN_LEFT, Element.ALIGN_CENTER, 7, 2, 0, null));
		ta.addCell(setCell(getReportName()+"采用单传力布置的安全寿命设计，集所有功能组成件及成品。"+getReportName()+
				"的主要结构组成见表一", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 7, 1, 0, null));
		ta.addCell(setCell("表1  主要结构组成表", fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 7, 2, 0, null));
		ta.addCell(setCell("飞机型号："+ms.getModelSeriesCode()+"                                            专业：结构" +
				"                                                          共  页，第  页",  
				fontCnNormal, Element.ALIGN_LEFT, Element.ALIGN_CENTER, 7, 1, 0, null));
		
		ta.addCell(setCell("部件名称/标识码",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 2, null, null));
		if(sMain.getComAta()!=null){
			ta.addCell(setCell(getStr(sMain.getComAta().getAtaName())+"/"+sMain.getComAta().getAtaCode(),fontCnNormal, 
					Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 2, null, null));
		}else{
			ta.addCell(setCell(getStr(sMain.getAddName())+"/"+sMain.getAddCode(),fontCnNormal, 
					Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 2, null, null));
		}
		ta.addCell(setCell("分部件名称/标识码",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 2, null, null));
		ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 2, null, null));
		
		ta.addCell(setCell("序号",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("重要结构项目名称",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
		ta.addCell(setCell("图号",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("标识码",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("安装位置",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		ta.addCell(setCell("备注",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
		
		int i=0;
		Set<S1> setS1 = sMain.getS1s();
		if(setS1.size()>0){
			for(SMain sMain1 : list){
				i++;
				ta.addCell(setCell(String.valueOf(i),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				if(sMain1.getComAta()!=null){
					ta.addCell(setCell(getStr(sMain1.getComAta().getAtaName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
				}else{
					ta.addCell(setCell(getStr(sMain1.getAddName()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
				}
				ta.addCell(setCell(getStr(sMain1.getComAta()==null?"":sMain1.getComAta().getEquipmentPicNo()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				if(sMain1.getIsSsi()==1){
					if(sMain1.getComAta()!=null){
						ta.addCell(setCell(sMain1.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
					}else{
						ta.addCell(setCell(sMain1.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
					}
				}else if(sMain1.getIsSsi()==0&&sMain1.getIsAna()!=null&&sMain1.getIsAna()==1){
					if(sMain1.getComAta()!=null){
						ta.addCell(setCell("Q"+sMain1.getComAta().getAtaCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
					}else{
						ta.addCell(setCell("Q"+sMain1.getAddCode(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
					}
				}
				ta.addCell(setCell(getStr(sMain1.getComAta()==null?"":sMain1.getComAta().getEquipmentPosition()),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell(sMain1.getRemark(),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
			}
		}else{
			for(int a=1;a<=3;a++){
				ta.addCell(setCell(String.valueOf(a),fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 1, 1, null, null));
				ta.addCell(setCell("",fontCnNormal, Element.ALIGN_CENTER, Element.ALIGN_CENTER, 2, 1, null, null));
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
		return 1;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{1.0f};
	}


}
