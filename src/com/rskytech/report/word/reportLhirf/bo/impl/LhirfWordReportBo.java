package com.rskytech.report.word.reportLhirf.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.field.RtfTotalPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILh1Bo;
import com.rskytech.lhirf.bo.ILh1aBo;
import com.rskytech.lhirf.bo.ILh2Bo;
import com.rskytech.lhirf.bo.ILh3Bo;
import com.rskytech.lhirf.bo.ILh4Bo;
import com.rskytech.lhirf.bo.ILh5Bo;
import com.rskytech.lhirf.bo.ILh6Bo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IDefineBaseCrackLenBo;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusDisplay;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.Lh1;
import com.rskytech.pojo.Lh1a;
import com.rskytech.pojo.Lh2;
import com.rskytech.pojo.Lh3;
import com.rskytech.pojo.Lh4;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.word.ReportBase;
import com.rskytech.report.word.reportLhirf.bo.ILhirfWordReportBo;
import com.rskytech.report.word.reportLhirf.utils.ReportLh1;
import com.rskytech.report.word.reportLhirf.utils.ReportLh1a;
import com.rskytech.report.word.reportLhirf.utils.ReportLh2;
import com.rskytech.report.word.reportLhirf.utils.ReportLh3;
import com.rskytech.report.word.reportLhirf.utils.ReportLh4;
import com.rskytech.report.word.reportLhirf.utils.ReportLh5;
import com.rskytech.report.word.reportLhirf.utils.ReportLh6;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.util.StringUtil;

@SuppressWarnings("all")
public class LhirfWordReportBo extends BaseBO implements ILhirfWordReportBo {

	private ILhMainBo lhMainBo;
	private ILh1Bo lh1Bo;
	private ILh1aBo lh1aBo;
	private ILh2Bo lh2Bo;
	private ILh3Bo lh3Bo;
	private ILh4Bo lh4Bo;
	private ILh5Bo lh5Bo;
	private ILh6Bo lh6Bo;
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
	private ICusLevelBo cusLevelBo;
	private IDefineStructureParameterBo defineStructureParameterBo;
	private IDefineBaseCrackLenBo  defineBaseCrackLenBo;
	
	private Document document  = null;	
	
	@Override
	public String createReport(LhMain lhMain, String reportName) {
		//设置导出的word报表的路径和名称
		String returnValue =null;
		String dataString = StringUtil.getDataString();
		String path = ServletActionContext.getRequest().getContextPath() + getReportPath(dataString);
		String filePath = ServletActionContext.getServletContext().getRealPath("/") + getReportPath(dataString);
		reportName = reportName +"_"+StringUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".doc";
		returnValue = path + reportName;
		
		File file = new File(filePath);
		if (!file.exists()){
			file.mkdirs();
		}
		
		ComArea area = (ComArea) lhMainBo.loadById(ComArea.class, lhMain.getComArea().getAreaId());
		try {
			document = new Document();
			ReportBase.setHoriPage(document);
			// 建立一个书写器与document对象关联，通过书写器可以将文档写入到输出流中 
			RtfWriter2.getInstance(document, new FileOutputStream(filePath + reportName));
			document.open();
			
			ComModelSeries ms = (ComModelSeries) lhMainBo.loadById(ComModelSeries.class, lhMain.getComModelSeries().getModelSeriesId());
			List<LhMain> lhMainList = lhMainBo.getLhMainList(lhMain.getComArea().getAreaId(), ms.getModelSeriesId(), lhMain.getHsiCode(), lhMain.getHsiName());

			for (LhMain l : lhMainList){
				if (l.getRefHsiCode() == null || "N/A".equalsIgnoreCase(l.getRefHsiCode()) || "".equals(l.getRefHsiCode())){
					Lh1 lh1 = lh1Bo.getLh1ByHsiId(l.getHsiId());
					Lh2 lh2 = lh2Bo.getLh2ByHsiId(l.getHsiId());
					List<Lh3> lh3List = lh3Bo.getLh3lListByHsiIdNoPage(l.getHsiId());
					Lh4 lh4 = lh4Bo.getLh4BylhHsId(l.getHsiId());
					String effective = (lh1.getLhMain().getEffectiveness() == null ? "" : lh1.getLhMain().getEffectiveness());
					
					new ReportLh1(document, ms, area, l, effective, lh1).generate("HORI", true, 2);
					new ReportLh2(document, ms, area, l, effective, lh2).generate("HORI", true, 2);
					new ReportLh3(document, ms, area, l, effective, lh3List).generate("HORI", false, 2);
					
					if (lh4 != null) {
						CusDisplay lh4show = defineBaseCrackLenBo.getLH4CusDisplay(l.getComModelSeries().getModelSeriesId());
						List<CusItemS45> ED_itemList = defineStructureParameterBo.getS45List(ms.getModelSeriesId(),ComacConstants.LH4,"ED");
						List<CusItemS45> AD_itemList =  defineStructureParameterBo.getS45List(ms.getModelSeriesId(),ComacConstants.LH4,"AD");
						new ReportLh4(document, ms, area, l, effective, ED_itemList, AD_itemList, lh4show, lh4, cusLevelBo).generate("HORI", false, 2);
						
						doReportLh5(ms, area, l, lh4, effective);
					}
				} else {
					Lh1a lh1a = lh1aBo.getLh1aByHsiId(l.getHsiId());
					Lh4 lh4 = lh4Bo.getLh4BylhHsId(l.getHsiId());
					String effective = (lh1a.getLhMain().getEffectiveness() == null ? "" : lh1a.getLhMain().getEffectiveness());
					
					new ReportLh1a(document, ms, area, l, effective, lh1a).generate("HORI", true, 2);
					
					doReportLh5(ms, area, l, lh4, effective);
				}
			}
			
			List list = lh6Bo.getLhirfListByIdNoPage(ms.getModelSeriesId(), lhMain.getComArea().getAreaId());
			new ReportLh6(document, ms, area, null, null, list).generate("HORI", false, 1);
			
			RtfPageNumber number = new RtfPageNumber();
			Paragraph parafooter = new Paragraph();
			parafooter.add(new Phrase("第"));
			parafooter.add(number);
   
			parafooter.add(new Phrase("页   共"));
			parafooter.add(new RtfTotalPageNumber());
			parafooter.add(new Phrase("页")); 

			HeaderFooter footer = new RtfHeaderFooter(parafooter);
			footer.setAlignment(Element.ALIGN_RIGHT);
			footer.setBorder(Rectangle.NO_BORDER);
			document.setFooter(footer);
			
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public void doReportLh5( ComModelSeries ms, ComArea area, LhMain lhMain, Lh4 lh4, String effective){
		if (lh4 != null){
			Lh5 lh5 = lh5Bo.getLh5ByHsiId(lhMain.getHsiId());
			if (lh5 != null){
				List<TaskMsg> listMsg = taskMsgBo.getTaskMsgListByMainId(ms.getModelSeriesId(), lhMain.getHsiId(), 
						ComacConstants.LHIRF_CODE, ComacConstants.LHIRF_LH5);
				List<CusInterval> cusList = null;
				if (lh4.getIsSafe() != 1){
					cusList = lh5Bo.getCusIntervalbyFlg("LH5",
							"B", lhMain.getComModelSeries().getModelSeriesId());
				} else {
					cusList = lh5Bo.getCusIntervalbyFlg("LH5",
							"A", lhMain.getComModelSeries().getModelSeriesId());
				}
				new ReportLh5(document, ms, area, lhMain, effective, lh4, lh5, listMsg, cusList, comAreaBo).generate("HORI", false, 2);
			}
		}
	}

	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH + "lhirf/" + dataString + "/";
	}

	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}

	public ILh1Bo getLh1Bo() {
		return lh1Bo;
	}

	public void setLh1Bo(ILh1Bo lh1Bo) {
		this.lh1Bo = lh1Bo;
	}

	public ILh1aBo getLh1aBo() {
		return lh1aBo;
	}

	public void setLh1aBo(ILh1aBo lh1aBo) {
		this.lh1aBo = lh1aBo;
	}

	public ILh2Bo getLh2Bo() {
		return lh2Bo;
	}

	public void setLh2Bo(ILh2Bo lh2Bo) {
		this.lh2Bo = lh2Bo;
	}

	public ILh3Bo getLh3Bo() {
		return lh3Bo;
	}

	public void setLh3Bo(ILh3Bo lh3Bo) {
		this.lh3Bo = lh3Bo;
	}

	public ILh4Bo getLh4Bo() {
		return lh4Bo;
	}

	public void setLh4Bo(ILh4Bo lh4Bo) {
		this.lh4Bo = lh4Bo;
	}

	public ICusLevelBo getCusLevelBo() {
		return cusLevelBo;
	}

	public void setCusLevelBo(ICusLevelBo cusLevelBo) {
		this.cusLevelBo = cusLevelBo;
	}

	public ILh5Bo getLh5Bo() {
		return lh5Bo;
	}

	public void setLh5Bo(ILh5Bo lh5Bo) {
		this.lh5Bo = lh5Bo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public ILh6Bo getLh6Bo() {
		return lh6Bo;
	}

	public void setLh6Bo(ILh6Bo lh6Bo) {
		this.lh6Bo = lh6Bo;
	}
	
	public IDefineStructureParameterBo getDefineStructureParameterBo() {
		return defineStructureParameterBo;
	}

	public void setDefineStructureParameterBo(
			IDefineStructureParameterBo defineStructureParameterBo) {
		this.defineStructureParameterBo = defineStructureParameterBo;
	}

	public IDefineBaseCrackLenBo getDefineBaseCrackLenBo() {
		return defineBaseCrackLenBo;
	}

	public void setDefineBaseCrackLenBo(IDefineBaseCrackLenBo defineBaseCrackLenBo) {
		this.defineBaseCrackLenBo = defineBaseCrackLenBo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
}
