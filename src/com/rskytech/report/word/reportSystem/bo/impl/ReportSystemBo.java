package com.rskytech.report.word.reportSystem.bo.impl;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.struts2.ServletActionContext;

import com.itextpdf.text.Header;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.field.RtfPageNumber;
import com.lowagie.text.rtf.field.RtfTotalPageNumber;
import com.lowagie.text.rtf.headerfooter.RtfHeaderFooter;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.basedata.bo.IComVendorBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.M11;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.report.word.ReportBase;
import com.rskytech.report.word.reportSystem.bo.IReportSystemBo;
import com.rskytech.report.word.reportSystem.utils.ReportM0;
import com.rskytech.report.word.reportSystem.utils.ReportM1;
import com.rskytech.report.word.reportSystem.utils.ReportM13;
import com.rskytech.report.word.reportSystem.utils.ReportM2;
import com.rskytech.report.word.reportSystem.utils.ReportM3;
import com.rskytech.report.word.reportSystem.utils.ReportM5;
import com.rskytech.report.word.reportSystem.utils.ReportM131;
import com.rskytech.report.word.reportSystem.utils.ReportMT;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IM12Bo;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMSetBo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.util.StringUtil;

public class ReportSystemBo  extends BaseBO implements IReportSystemBo{
	private IMsiMainBo msiMainBo;
	private IM0Bo m0Bo;
	private IM13Bo m13Bo;
	private ITaskMsgBo taskMsgBo;
	private IComVendorBo comVendorBo;
	private IComAreaBo comAreaBo;
	private IComAtaBo comAtaBo;
	private IM12Bo m12Bo;
	private IM2Bo m2Bo;;
	private IM3Bo m3Bo;
	private IM5Bo m5Bo;
	private IMSetBo mSetBo;

	public IMSetBo getmSetBo() {
		return mSetBo;
	}
	public void setmSetBo(IMSetBo mSetBo) {
		this.mSetBo = mSetBo;
	}
	@SuppressWarnings("unchecked")
	@Override
	public String createReport(String msiId,String reportName) {
		String returnValue =null;
		String dataString = StringUtil.getDataString();
		String path = ServletActionContext.getRequest().getContextPath() + getReportPath(dataString);
		String filePath = ServletActionContext.getServletContext().getRealPath("/") + getReportPath(dataString);
		MMain mMain = (MMain) msiMainBo.loadById(MMain.class, msiId);
		reportName = reportName +"_"+StringUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".doc";
		File file = new File(filePath);
		if (!file.exists()){
			file.mkdirs();
		}
		try {
			ComModelSeries ms = mMain.getComModelSeries();
			Document document = new Document();
			ReportBase.setHoriPage(document);
			RtfWriter2.getInstance(document, new FileOutputStream(filePath + reportName));
			document.open();
			List<M0> list = m0Bo.getMsiATAListByMsiId(msiId);
			Set<M11> setM11 = mMain.getM11s();
			M11 m11 = null;
			if(setM11.size()>0){
				Iterator<M11> it = setM11.iterator();
				m11 = (M11) it.next();
			}


			new ReportMT(document,ms,mMain).generate("Hori", false, null);

			generateM1(document,ms,mMain);
			new ReportM0(document, ms,list,m11,mMain,comAtaBo).generate("Hori", false, null);
//			List<M12> listM12 = m12Bo.getM12AListByMsiId(msiId);
//			new ReportM12(document, ms,listM12,comVendorBo,comAreaBo).generate("Hori", false, null);
			generateM131(document,ms,mMain);
			generateM13(document, ms,mMain);
			generateM2(document, ms, mMain);
			generateM3(document, ms, mMain);
			generateM5(document, ms, mMain);
			  // 添加页眉   
		  
		 /*   HeaderFooter header = new HeaderFooter(new Phrase("第一飞机设计研究院                          " +
		    		"                                                   XXX-XXXXX-XXX  版次：A"), false);  
			//HeaderFooter header = new HeaderFooter(new Phrase("第一飞机设计研究院"), new Phrase("第一飞机设计研究院") );
	        header.setAlignment(Rectangle.ALIGN_LEFT); 
	        header.setBorderColorBottom(Color.blue);
	        header.setBorderWidthBottom(2f);
	        header.setBorder(30);
	        header.setPageNumber(2);
	        document.setHeader(header); */
		
	        
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
			returnValue = path + reportName;

//			ReportContents  rc = new ReportContents();
//			rc.CreateContents(filePath + reportName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	/*
	 * xxx系统设备清单
	 * @param document
     * @param mMain 
     * @param ms 
	 */
    private void generateM1(Document document, ComModelSeries ms, MMain mMain) {
    	ComUser user = (ComUser) this.loadById(ComUser.class, mMain.getModifyUser());
		String userName="";
		if(user!=null){
			userName=user.getUserName();
		}
		new ReportM1(document,ms,mMain,userName,comAtaBo,m12Bo,comVendorBo).generate("Hori", false, null);
	}
	/*表3  XX分系统功能/功能故障描述表格
     * @param document
     * @param mMain 
     * @param ms 
     */
	private void generateM131(Document document, ComModelSeries ms, MMain mMain) {
		ComUser user = (ComUser) this.loadById(ComUser.class, mMain.getModifyUser());
		String userName="";
		if(user!=null){
			userName=user.getUserName();
		}
		List<M0> list = m0Bo.getMsiATAListByMsiId(mMain.getMsiId());
		new ReportM131(document,ms,mMain,userName,list,m0Bo,comAtaBo,mSetBo).generate("Hori", false, null);
	}

	/**
	 * 功能故障模式与原因分析
	 * @param document
	 * @param mMain 
	 * @param ms 
	 */
	public void generateM13(Document document, ComModelSeries ms, MMain mMain){
		ComUser user = (ComUser) this.loadById(ComUser.class, mMain.getModifyUser());
		String userName="";
		if(user!=null){
			userName=user.getUserName();
		}
		List<M13> list = m13Bo.getM13ListByMsiId(mMain.getMsiId());
		new ReportM13(document,ms,mMain,userName,list,m13Bo).generate("Hori", false, null);
	}
	
	/**
	 * 系统RCMA 分析决断（故障影响类型分析表（第一层次决断））
	 * @param document
	 * @param ms
	 * @param mMain
	 */
	public void generateM2(Document document, ComModelSeries ms, MMain mMain){
		List<M2> setM2 = m2Bo.getM2ListByMsiId(mMain.getMsiId());
		for(M2 m2 : setM2){
			M13F m13f = m2.getM13F();
			new ReportM2(document, ms,mMain,m13f).generate("Hori", false, null);
		}
	}
	
	/**
	 * 系统RCMA 分析决断（预防性维修工作类型选择表（第二层次决断））
	 * @param document
	 * @param ms
	 * @param mMain
	 */
	@SuppressWarnings("unchecked")
	public void generateM3(Document document, ComModelSeries ms, MMain mMain){
		List<M3> setM3 = m3Bo.getM3ListByMsiId(mMain.getMsiId());
		String failureCauseType ="";
		String functionCode ="";//产品功能编号
		String effectCode ="";//故障模式编号
		String causeCode ="";//故障原因编号
		for(M3 m3 : setM3){
			M13C m13c = m3.getM13C();
			M13F m13f = m13c.getM13F();
			M13 m13 = m13f.getM13();
			
			Set<M2> setM2 = m13f.getM2s();
			Iterator<M2> it = setM2.iterator();
			while(it.hasNext()){
				M2 m2 = it.next();
				failureCauseType = String.valueOf(m2.getFailureCauseType());
			}
			causeCode = m13c.getCauseCode();
			effectCode = m13f.getEffectCode();
			functionCode = m13.getFunctionCode();
			String code = functionCode+"、"+effectCode+"、"+causeCode;
			new ReportM3(document, ms,m3,code,failureCauseType,mMain,m3Bo,this.comAreaBo).generate("Hori", false, null);
		}
	}
	
	/**
	 * 预防性维修工作汇总表
	 * @param document
	 * @param ms
	 * @param mMain
	 */

	@SuppressWarnings("unchecked")
	public void generateM5(Document document, ComModelSeries ms, MMain mMain){
		List<TaskMsg> listTask = this.taskMsgBo.getTaskMsgListByMainId(ms.getModelSeriesId(),
				mMain.getMsiId(),ComacConstants.SYSTEM_CODE,null);
		   
		        new ReportM5(document, ms,mMain,listTask,comAreaBo,m5Bo).generate("Hori", false, null);
		  
	}
	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH +"system/"+ dataString + "/";
	}
	
	public String getStr(String s){
		return (s == null || "null".equals(s)) ? "" : s.replaceAll("\r", "");
	}

	public IMsiMainBo getMsiMainBo() {
		return msiMainBo;
	}
	public void setMsiMainBo(IMsiMainBo msiMainBo) {
		this.msiMainBo = msiMainBo;
	}
	public IM0Bo getM0Bo() {
		return m0Bo;
	}
	public void setM0Bo(IM0Bo m0Bo) {
		this.m0Bo = m0Bo;
	}

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo m13Bo) {
		this.m13Bo = m13Bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}
	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IComVendorBo getComVendorBo() {
		return comVendorBo;
	}

	public void setComVendorBo(IComVendorBo comVendorBo) {
		this.comVendorBo = comVendorBo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}

	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}

	public IM12Bo getM12Bo() {
		return m12Bo;
	}

	public void setM12Bo(IM12Bo m12Bo) {
		this.m12Bo = m12Bo;
	}

	public IM2Bo getM2Bo() {
		return m2Bo;
	}

	public void setM2Bo(IM2Bo m2Bo) {
		this.m2Bo = m2Bo;
	}

	public IM3Bo getM3Bo() {
		return m3Bo;
	}

	public void setM3Bo(IM3Bo m3Bo) {
		this.m3Bo = m3Bo;
	}
	public IM5Bo getM5Bo() {
		return m5Bo;
	}
	public void setM5Bo(IM5Bo m5Bo) {
		this.m5Bo = m5Bo;
	}
}
