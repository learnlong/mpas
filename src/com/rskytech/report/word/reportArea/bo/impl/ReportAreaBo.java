package com.rskytech.report.word.reportArea.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.field.RtfTotalPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa1Dao;
import com.rskytech.area.dao.IZa2Dao;
import com.rskytech.area.dao.IZa41Dao;
import com.rskytech.area.dao.IZa42Dao;
import com.rskytech.area.dao.IZa43Dao;
import com.rskytech.area.dao.IZa5Dao;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.area.dao.IZa8Dao;
import com.rskytech.basedata.dao.IComAreaDao;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.paramdefinemanage.bo.IIncreaseRegionParamBo;
import com.rskytech.paramdefinemanage.bo.IStandardRegionParamBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.Za2;
import com.rskytech.pojo.Za41;
import com.rskytech.pojo.Za42;
import com.rskytech.pojo.Za43;
import com.rskytech.pojo.Za5;
import com.rskytech.pojo.ZaMain;
import com.rskytech.report.word.ReportBase;
import com.rskytech.report.word.reportArea.bo.IReportAreaBo;
import com.rskytech.report.word.reportArea.utils.ReportAreaDetail;
import com.rskytech.report.word.reportArea.utils.ReportZa1;
import com.rskytech.report.word.reportArea.utils.ReportZa2;
import com.rskytech.report.word.reportArea.utils.ReportZa41;
import com.rskytech.report.word.reportArea.utils.ReportZa42;
import com.rskytech.report.word.reportArea.utils.ReportZa43;
import com.rskytech.report.word.reportArea.utils.ReportZa5;
import com.rskytech.report.word.reportArea.utils.ReportZa7;
import com.rskytech.report.word.reportArea.utils.ReportZa8;
import com.rskytech.util.StringUtil;

public class ReportAreaBo extends BaseBO implements IReportAreaBo {

	private IComAreaDao comAreaDao;
	private IIncreaseRegionParamBo increaseRegionParamBo;
	private IStandardRegionParamBo standardRegionParamBo;
	private ICusLevelBo cusLevelBo;
	private IZa1Dao za1Dao;
	private IZa2Dao za2Dao;
	private IZa41Dao za41Dao;
	private IZa42Dao za42Dao;
	private IZa43Dao za43Dao;
	private IZa5Dao za5Dao;
	private IZa7Dao za7Dao;
	private IZa8Dao za8Dao;
	
	public String createReport(String zaId, String reportName) {
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
		
		try {
			Document document = new Document();
			ReportBase.setHoriPage(document);
			RtfWriter2.getInstance(document, new FileOutputStream(filePath + reportName));
			document.open();
			
			ZaMain zaMain = (ZaMain) this.loadById(ZaMain.class, zaId);
			ComModelSeries ms = zaMain.getComModelSeries();
			ComArea area = zaMain.getComArea();
			
			Za1 za1 = za1Dao.getZa1ByZaId(zaId);
			Za2 za2 = za2Dao.getZa2ByZaId(zaId);
			Za41 za41 = za41Dao.getZa41ByZaId(zaId);
			Za42 za42 = za42Dao.getZa42ByZaId(zaId);
			List<TaskMsg> za43List = za43Dao.getTaskList(ms.getModelSeriesId(), zaId);
			Za5 za5a = za5Dao.getZa5ByZaId(zaId, "ZA5A");
			Za5 za5b = za5Dao.getZa5ByZaId(zaId, "ZA5B");
			List<Object[]> za7List = za7Dao.getReportZa7List(ms.getModelSeriesId(), area.getAreaId());
			List<TaskMsg> za8List = za8Dao.searchTask(ms.getModelSeriesId(), zaId);
			
			new ReportZa2(document, ms, area, za2).generate("Hori", false, null);
			new ReportAreaDetail(document, ms, area, comAreaDao).generate("Hori", false, null);
			new ReportZa1(document, ms, area, za1, za2).generate("Hori", false, null);
			new ReportZa41(document, ms, area, za41).generate("Hori", false, null);
			new ReportZa42(document, ms, area, za42).generate("Hori", false, null);
			
			for (TaskMsg tm : za43List){
				Za43 za43 = za43Dao.getZa43ByZaIdAndTaskId(zaId, tm.getTaskId());
				if (za43 != null){
					new ReportZa43(document, ms, area, za43, increaseRegionParamBo, cusLevelBo, tm).generate("Hori", false, null);
				}
			}
			
			if (za2.getPosition() == 1 && za5a != null){
				new ReportZa5(document, ms, area, za5a, standardRegionParamBo, cusLevelBo).generate("HORI", false, null);
			} else if(za2.getPosition() == 2 && za5b != null){
				new ReportZa5(document, ms, area, za5b, standardRegionParamBo, cusLevelBo).generate("HORI", false, null);
			} else if(za2.getPosition() == 3){
				if (za5a!=null){
					new ReportZa5(document, ms, area, za5a, standardRegionParamBo, cusLevelBo).generate("HORI", false, null);
				}
				if (za5b!=null){
					new ReportZa5(document, ms, area, za5b, standardRegionParamBo, cusLevelBo).generate("HORI", false, null);
				}
			}
			
			new ReportZa7(document, ms, area, za7List).generate("HORI", false, null);
			new ReportZa8(document, ms, area, za8List, za8Dao).generate("HORI", false, null);
			
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
	
	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH + "area/" + dataString + "/";
	}

	public IComAreaDao getComAreaDao() {
		return comAreaDao;
	}

	public void setComAreaDao(IComAreaDao comAreaDao) {
		this.comAreaDao = comAreaDao;
	}

	public IZa1Dao getZa1Dao() {
		return za1Dao;
	}

	public void setZa1Dao(IZa1Dao za1Dao) {
		this.za1Dao = za1Dao;
	}

	public IZa41Dao getZa41Dao() {
		return za41Dao;
	}

	public void setZa41Dao(IZa41Dao za41Dao) {
		this.za41Dao = za41Dao;
	}

	public IZa42Dao getZa42Dao() {
		return za42Dao;
	}

	public void setZa42Dao(IZa42Dao za42Dao) {
		this.za42Dao = za42Dao;
	}

	public IZa2Dao getZa2Dao() {
		return za2Dao;
	}

	public void setZa2Dao(IZa2Dao za2Dao) {
		this.za2Dao = za2Dao;
	}

	public ICusLevelBo getCusLevelBo() {
		return cusLevelBo;
	}

	public void setCusLevelBo(ICusLevelBo cusLevelBo) {
		this.cusLevelBo = cusLevelBo;
	}

	public IZa43Dao getZa43Dao() {
		return za43Dao;
	}

	public void setZa43Dao(IZa43Dao za43Dao) {
		this.za43Dao = za43Dao;
	}

	public IIncreaseRegionParamBo getIncreaseRegionParamBo() {
		return increaseRegionParamBo;
	}

	public void setIncreaseRegionParamBo(
			IIncreaseRegionParamBo increaseRegionParamBo) {
		this.increaseRegionParamBo = increaseRegionParamBo;
	}

	public IStandardRegionParamBo getStandardRegionParamBo() {
		return standardRegionParamBo;
	}

	public void setStandardRegionParamBo(
			IStandardRegionParamBo standardRegionParamBo) {
		this.standardRegionParamBo = standardRegionParamBo;
	}

	public IZa5Dao getZa5Dao() {
		return za5Dao;
	}

	public void setZa5Dao(IZa5Dao za5Dao) {
		this.za5Dao = za5Dao;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}

	public IZa8Dao getZa8Dao() {
		return za8Dao;
	}

	public void setZa8Dao(IZa8Dao za8Dao) {
		this.za8Dao = za8Dao;
	}
	
}
