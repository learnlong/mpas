package com.rskytech.report.word.reportSystem.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM5Bo;

public class ReportM5 extends SystemReportBase{
	private ComModelSeries ms;
	private MMain mMain;
	private List<TaskMsg> listTask;

	private String failureCauseType;
	private IComAreaBo comAreaBo;
	private M3 m3;
	private IM3Bo m3Bo;
	private IM5Bo m5Bo;
	public ReportM5(Document document, ComModelSeries ms,MMain mMain, List<TaskMsg> listTask,IComAreaBo comAreaBo,IM5Bo m5Bo) {
		super(document);
		this.ms=ms;
		this.mMain=mMain;
		this.listTask=listTask;
		this.comAreaBo=comAreaBo;
		this.m5Bo=m5Bo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Table getTableContent() throws Exception {
		Table ta = setTableAndColumn(getCol(), getColWidth());
		Paragraph title = new Paragraph("6.3   系统预防性维修工作汇总"); 
		title.setFont(rpsTitle); 
		document.add(title); 
		ta.addCell(setCell("      "+getReportName()+"分系统维修工作汇总见下表。", fontCnNormal, 
				Element.ALIGN_LEFT, Element.ALIGN_TOP, 10, 1, 0, null));
		ta.addCell(setCell("表5   "+getReportName()+"系统预防性维修工作汇总表", fontCnNormal,
				Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 10,3, 0, null));

		
		ta.addCell(setCell("产品标识码", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("产品名称", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("故障影响类别", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("区域", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("维修工作类型", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("维修工作描述", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("检查间隔期", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("维修通道", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("MPD任务号", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		ta.addCell(setCell("备注", fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1, null, null));
		
		if(listTask.size()>0){
			for(TaskMsg tm : listTask){
				ta.addCell(setCell(getStr(mMain.getComAta().getAtaCode()),fontCnNormal));
				ta.addCell(setCell(getStr(mMain.getComAta().getEquipmentName()),fontCnNormal));
		          String CasueType="";
			        	if ("M3".equals(tm.getSourceStep())) {
							// 获取任务的故障影响类别及故障原因编号
							List<Object[]> list = this.m5Bo.getCauseCodeAndCauseTypeByTaskId(tm.getTaskId());
							if (list != null) {
								for (Object[] objects : list) {
									
									CasueType += objects[1]+"\n";//故障影响类别
								}
							}
			            }  
			        	
			       
			        ta.addCell(setCell(getStr(CasueType.trim()),fontCnNormal));
					ta.addCell(setCell(getStr(this.comAreaBo.getAreaCodeByAreaId(tm.getOwnArea())), fontCnNormal));
					ta.addCell(setCell(getStr(tm.getTaskType()),fontCnNormal));
					ta.addCell(setCell(getStr(tm.getTaskDesc()), fontCnNormal));
					ta.addCell(setCell(getStr(tm.getTaskInterval()),fontCnNormal));
					ta.addCell(setCell(getStr(tm.getReachWay()),fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1));
					ta.addCell(setCell(getStr(tm.getTaskCode()),fontCnNormal));
					ta.addCell(setCell("",fontCnNormal));
			}
		}else{
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal,Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 1,1));
			ta.addCell(setCell("",fontCnNormal));
			ta.addCell(setCell("",fontCnNormal));
		}
		return ta;
	}
	
	@Override
	public String getReportName() {
		return getStr(ms.getModelSeriesName());
	}

	@Override
	public int getCol() {
		return 10;
	}

	@Override
	public float[] getColWidth() {
		return new float[]{0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f,0.1f};
	}


}
