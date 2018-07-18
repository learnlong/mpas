package com.rskytech.report.word.reportSystem.utils;

import java.io.StringReader;
import java.util.List;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.itextword.text.html.simpleparser.HTMLWorker;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.M11;
import com.rskytech.pojo.MMain;

public class ReportM0 extends SystemReportBase{
	private ComModelSeries ms;
	private List<M0> list;
	private  M11 m11;
	private MMain mMain;
	private IComAtaBo comAtaBo;
	
	public ReportM0(Document document, ComModelSeries ms,List<M0> list, M11 m11, MMain mMain, IComAtaBo comAtaBo){
		super(document);
		this.ms=ms;
		this.list=list;
		this.m11=m11;
		this.mMain= mMain;
		this.comAtaBo=comAtaBo;
	}
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(),getColWidth());
		Paragraph title = new Paragraph("5  重要功能产品分析"); 
		title.setFont(rpsTitle); 
		document.add(title); 
		ta.addCell(setCell("表2  重要功能产品分析表", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 8,1, 0, null));
		ta.addCell(setCell("系统名称", fontCnNormal,
					Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell(getReportName(), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,2, null, null));
		ta.addCell(setCell("系统图(代)号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,2, null, null));
		ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentPicNo()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 3,2, null, null));
		ta.addCell(setCell("产品标识码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("产品名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("产品型(图)号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("安装位置", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("故障影响安全性吗？(包括地面和空中)", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("在正常职责范围，故障对使用飞机的人员来说是无法发现或不易察觉吗？", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("故障影响任务完成吗？", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("故障可能导致重大的经济损失吗？", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		if(list.size()>0){
			ComAta comAta;
			String msId=ms.getModelSeriesId();
			for(M0 m0 : list){
				String safetyAnswer="";
				String detectableAnswer="";
				String taskAnswer="";
				String economicAnswer="";
				ta.addCell(setCell(getStr(m0.getProCode()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(getStr(m0.getProName()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				comAta=comAtaBo.getComAtaByAtaCode(m0.getProCode(), msId);
				if(comAta==null){
					ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
					ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				}else{
					ta.addCell(setCell(getStr(comAta.getEquipmentPicNo()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
					ta.addCell(setCell(getStr(comAta.getEquipmentPosition()), fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				}
//				影响安全性
				if(m0.getSafety()!=null&&m0.getSafety()==1){
					if(m0.getSafetyAnswer()!=null){
						safetyAnswer ="是，"+ m0.getSafetyAnswer();
					}else{
						safetyAnswer ="是 "+ safetyAnswer;
					}
				}else if(m0.getSafety()!=null&&m0.getSafety()==0){
					if(m0.getSafetyAnswer()!=null){
						safetyAnswer ="否，"+ m0.getSafetyAnswer();
					}else{
						safetyAnswer ="否 "+ safetyAnswer;
					}
				}else{
					if(m0.getSafetyAnswer()!=null){
						safetyAnswer =m0.getSafetyAnswer();
					}
				}
//				是否容易检测
				if(m0.getDetectable()!=null&&m0.getDetectable()==1){
					if(m0.getDetectableAnswer()!=null){
						detectableAnswer ="是，"+ m0.getDetectableAnswer();
					}else{
						detectableAnswer ="是 "+ detectableAnswer;
					}
				}else if(m0.getDetectable()!=null&&m0.getDetectable()==0){
					if(m0.getDetectableAnswer()!=null){
						detectableAnswer ="否，"+ m0.getDetectableAnswer();
					}else{
						detectableAnswer ="否 "+ detectableAnswer;
					}
				}else{
					if(m0.getDetectableAnswer()!=null){
						detectableAnswer =m0.getDetectableAnswer();
					}
				}
//				影响任务完成
				if(m0.getTask()!=null&&m0.getTask()==1){
					if(m0.getTaskAnswer()!=null){
						taskAnswer ="是，"+ m0.getTaskAnswer();
					}else{
						taskAnswer ="是 "+ taskAnswer;
					}
				}else if(m0.getTask()!=null&&m0.getTask()==0){
					if(m0.getTaskAnswer()!=null){
						taskAnswer ="否，"+ m0.getTaskAnswer();
					}else{
						taskAnswer ="否 "+ taskAnswer;
					}
				}else{
					if(m0.getTaskAnswer()!=null){
						taskAnswer =m0.getTaskAnswer();
					}
				}
//				可能造成的危害
				if(m0.getEconomic()!=null&&m0.getEconomic()==1){
					if(m0.getEconomicAnswer()!=null){
						economicAnswer ="是，"+ m0.getEconomicAnswer();
					}else{
						economicAnswer ="是 "+ economicAnswer;
					}
				}else if(m0.getEconomic()!=null&&m0.getEconomic()==0){
					if(m0.getEconomicAnswer()!=null){
						economicAnswer ="否，"+ m0.getEconomicAnswer();
					}else{
						economicAnswer ="否 "+ economicAnswer;
					}
				}else{
					if(m0.getEconomicAnswer()!=null){
						economicAnswer =m0.getEconomicAnswer();
					}
				}
				ta.addCell(setCell(safetyAnswer, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(detectableAnswer, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(taskAnswer, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
				ta.addCell(setCell(economicAnswer, fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
			}
		}else{
			for(int i=0;i<16;i++){
				ta.addCell(setCell("", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1,null, null));
			}
		}
		Table ta3 = setTableAndColumn(1,null);
		ta3.insertTable(ta);
		return ta3;
	}
	
	public Table getM11Table() throws Exception{
		Table ta1 = setTableAndColumn(1,new float[]{1.0f});
		ta1.setBorder(0);
		if(m11!=null){
			HTMLWorker worker = new HTMLWorker(this.document,ta1);
			worker.parse( new StringReader( m11.getPicContent() ) );
		}else{
			ta1.addCell(setCell("", fontCnNormal,Element.ALIGN_LEFT, Element.ALIGN_TOP, 1,4, 0, null));
		}
		return ta1;
	}
	
	@Override
	public int getCol() {
		return 8;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.1f,0.125f,0.125f,0.125f,0.1f,0.125f,0.125f,0.125f};
	}
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}
}
