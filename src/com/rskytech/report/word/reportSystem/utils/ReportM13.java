package com.rskytech.report.word.reportSystem.utils;

import java.util.List;
import java.util.Set;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MMain;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.util.StringUtil;

public class ReportM13 extends SystemReportBase{
	private MMain mMain;
	private ComModelSeries ms;
	private String userName;
	private List<M13> list;
	private IM13Bo m13Bo;
	
	public ReportM13(Document document, ComModelSeries ms, MMain mMain, String userName, List<M13> list, IM13Bo m13Bo) {
		super(document);
		this.mMain=mMain;
		this.ms=ms;
		this.userName = userName;
		this.list=list;
		this.m13Bo=m13Bo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		Paragraph title = new Paragraph(getReportName()+"分系统故障模式与原因分析表格见下表。"); 
		title.setFont(fontCnNormal); 
		document.add(title); 
		ta.addCell(setCell("表4  "+getReportName()+"分系统故障模式与原因分析表格", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 7,2, 0, null));
		ta.addCell(setCell("初始约定层次：", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 6,1, null, null));
		ta.addCell(setCell("分析人员："+userName, fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("约定层次：", fontCnNormal,
				Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 6,1, null, null));
		ta.addCell(setCell("分析时间："+StringUtil.formatDate(mMain.getModifyDate(), "yyyy/MM/dd"), fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		ta.addCell(setCell("产品名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("功能故障编码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("功能故障", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("故障影响编码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("故障影响", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("故障原因编码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell("故障原因", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		
		int count2=0;
		int total = 0;
		if(list.size()>0){
			for(M13 m13 : list){
				Set<M13F> setM13f = m13.getM13Fs();
				if(setM13f.size()>0){
					for(M13F m13f : setM13f ){
						Set<M13C> setM13c = m13f.getM13Cs();
						if(setM13c.size()>0){
							total += setM13c.size();
						}
					}
				}
			}
		}
		if(total>0){
			String descCode;
			String descValue;
			String failureCode;
			String failureDesc;
			ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentName()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,total, null, null));
			for(M13 m13 : list){
				List<M13F> setM13f = m13Bo.getM13fListByM13Id(m13.getM13Id());
				if(setM13f.size()>0){
					for(M13F m13f : setM13f ){
						descCode = getStr(m13f.getEffectCode());
						descValue = getStr(m13f.getEffectDesc());
						failureCode = getStr(m13f.getFailureCode());
						failureDesc = getStr(m13f.getFailureDesc());
						List<M13C> setM13c = m13Bo.getM13cListByM13FId(m13f.getM13fId());
						if(setM13c.size()>0){
							for(M13C m13c : setM13c){
								if(count2>0){
									ta.addCell(setCell("",fontCnNormal));
									ta.addCell(setCell("",fontCnNormal));
									ta.addCell(setCell("",fontCnNormal));
									ta.addCell(setCell("",fontCnNormal));
								}else{
									ta.addCell(setCell(failureCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
									ta.addCell(setCell(failureDesc, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
									ta.addCell(setCell(descCode, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));	
									ta.addCell(setCell(descValue, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));	
								}
								ta.addCell(setCell(m13c.getCauseCode(), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
								String CauseDesc;
								CauseDesc = m13c.getCauseDesc();
								ta.addCell(setCell(CauseDesc.substring(CauseDesc.lastIndexOf("/")+1), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
								count2++;
							}
						}
					  count2=0;
					}
				}
				count2=0;
			}
		}else{
			ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentName()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,5, null, null));
			for(int i=0;i<55;i++){
				ta.addCell(setCell("",fontCnNormal));
			}
		}
		return ta;
	}
	
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 7;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.16f,0.13f,0.13f,0.13f,0.13f,0.13f,0.172f};
	}


}
