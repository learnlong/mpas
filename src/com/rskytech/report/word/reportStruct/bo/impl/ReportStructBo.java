package com.rskytech.report.word.reportStruct.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.report.word.ReportBase;
import com.rskytech.report.word.reportStruct.bo.IReportStructBo;
import com.rskytech.report.word.reportStruct.dao.IReportStructDao;
import com.rskytech.report.word.reportStruct.utils.ReportCorrosion;
import com.rskytech.report.word.reportStruct.utils.ReportCover;
import com.rskytech.report.word.reportStruct.utils.ReportMCorrosion;
import com.rskytech.report.word.reportStruct.utils.ReportS1;
import com.rskytech.report.word.reportStruct.utils.ReportS3;
import com.rskytech.report.word.reportStruct.utils.ReportS4a;
import com.rskytech.report.word.reportStruct.utils.ReportS4b;
import com.rskytech.report.word.reportStruct.utils.ReportS5a;
import com.rskytech.report.word.reportStruct.utils.ReportS5b;
import com.rskytech.report.word.reportStruct.utils.ReportS6;
import com.rskytech.report.word.reportStruct.utils.ReportS7;
import com.rskytech.report.word.reportStruct.utils.ReportUnSsi;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS4a;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS4b;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS5a;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS5b;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS6;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableS7;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableSya;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableSyb;
import com.rskytech.report.word.reportStruct.utilsTable.ReportTableUnSsi;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.IS5Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.bo.ISyBo;
import com.rskytech.struct.dao.IS8Dao;
import com.rskytech.struct.dao.ISsiMainDao;
import com.rskytech.struct.dao.IUnSsiDao;
import com.rskytech.util.StringUtil;

@SuppressWarnings("unchecked")
public class ReportStructBo extends BaseBO implements IReportStructBo {
	
	private ISsiMainDao ssiMainDao;
	private IComAreaBo comAreaBo;
	private IS4Bo s4Bo;
	private IS5Bo s5Bo;
	private IS6Bo s6Bo;
	private ISyBo syBo;
	private IS8Dao s8Dao;
	private IReportStructDao reportStructDao;
	private IUnSsiDao unSsiDao;
	private IS1Bo s1Bo;
	
	/**
	 * 某个SSI及其下级所有SSI的生成报告方法
	 */
	public String createReport(String ssiId, String reportName, String userId) {
		String returnValue =null;
		String ssiCode = "";
		String ssiName = "";
		String dataString = StringUtil.getDataString();
		String path = ServletActionContext.getRequest().getContextPath() + getReportPath(dataString);
		String filePath = ServletActionContext.getServletContext().getRealPath("/") + getReportPath(dataString);
		reportName = reportName +"_"+StringUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".doc";
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		SMain sMain = (SMain) this.loadById(SMain.class, ssiId);//本级SSI
		ComModelSeries ms = sMain.getComModelSeries();
		if(sMain.getComAta()!=null){
			ssiCode = sMain.getComAta().getAtaCode();
			ssiName = getStr(sMain.getComAta().getAtaName());
		}else{
			ssiCode = sMain.getAddCode();
			ssiName = getStr(sMain.getAddName());
		}
		
		List<SMain> sMainList = new ArrayList<SMain>();//本级SSI及其下级所有SSI
		if (sMain.getComAta()!=null){
			sMainList.add(sMain);
		}
		getSMainList(sMain, ms, userId, sMainList);
		
		List<SStep> sStepList = new ArrayList<SStep>();//本级SSI及其下级所有SSI的步骤
		for (SMain sm : sMainList){
			List<SStep> ssList = ssiMainDao.getSStepListBySsiId(sm.getSsiId());
			if (ssList != null && ssList.size() > 0){
				sStepList.add(ssList.get(0));
			} else {
				sStepList.add(new SStep());
			}
		}
		
		String areaCode = getAreaCode(sMain);
		
		try {
			Document document = new Document();
			ReportBase.setHoriPage(document);
			RtfWriter2.getInstance(document, new FileOutputStream(filePath + reportName));
			document.open();
			
			new ReportTableS4a(document,ms,ssiCode,ssiName,areaCode,s4Bo,sStepList).generate("Hori", false, null);
			new ReportTableS4b(document,ms,ssiCode,ssiName,areaCode,s4Bo,sStepList).generate("Hori", false, null);
			new ReportTableSya(document, ms,ssiCode, ssiName,areaCode, sStepList,syBo).generate("Hori", false, null);
			new ReportTableSyb(document, ms,ssiCode, ssiName,areaCode, sStepList,syBo).generate("Hori", false, null);
			new ReportTableS5a(document,ms,ssiCode,ssiName,areaCode,s5Bo,sStepList).generate("Hori", false, null);
			new ReportTableS5b(document,ms,ssiCode,ssiName,areaCode,s5Bo,sStepList).generate("Hori", false, null);
			new ReportTableS6(document, ms, ssiCode, ssiName, areaCode, sStepList, reportStructDao, s6Bo).generate("Hori", false, null);
			new ReportTableUnSsi(document,ssiCode, ssiName, sMainList, ms, comAreaBo, unSsiDao).generate("Hori", false, null);
			new ReportTableS7(document, ms, sMainList, s8Dao,comAreaBo,unSsiDao).generate("Hori", false, null);
			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	
////////////////////////////////////////////////////////////////////////////////	
	
	/**
	 * 单个SSI的生成报告方法
	 */
	public String createReportOne(String ssiId,String reportName,String userId) {
		String returnValue =null;
		String ssiCode = "";
		String ssiName = "";
		String dataString = StringUtil.getDataString();
		String path = ServletActionContext.getRequest().getContextPath() + getReportPath(dataString);
		String filePath = ServletActionContext.getServletContext().getRealPath("/") + getReportPath(dataString);
		SMain sMain = (SMain) this.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ssiCode = sMain.getComAta().getAtaCode();
			ssiName = getStr(sMain.getComAta().getAtaName());
		}else{
			ssiCode = sMain.getAddCode();
			ssiName = getStr(sMain.getAddName());
		}
		reportName = reportName +"_"+StringUtil.formatDate(new Date(), "yyyyMMddHHmmss")+".doc";
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
		}
		try {
			ComModelSeries ms = sMain.getComModelSeries();
			List<SMain> list = new ArrayList<SMain>();
			if(sMain.getComAta()!=null){
				list.add(sMain);
			}
			getSMainList(sMain, ms, userId, list);
			Document document = new Document();
			ReportBase.setHoriPage(document);
			RtfWriter2.getInstance(document, new FileOutputStream(filePath + reportName));
			document.open();
			int isSsi=0;
			if(sMain.getIsSsi()==1){
				Set<S2> setS2 = sMain.getS2s();
				S2 s2=null;
				if(setS2.size()>0){
					Iterator<S2> it = setS2.iterator();
					s2= it.next();
				}
				String areaCode = getAreaCode(sMain);
				generateReportCover(document,ms,list,s2);
				generateReportS1(document, ms, sMain, ssiCode, ssiName);
				generateReportS4(document, ms,sMain,ssiCode, ssiName,areaCode);
				new ReportMCorrosion(document, ms,ssiCode, ssiName,areaCode, sMain,syBo).generate("Hori", false, null);
				new ReportCorrosion(document, ms,ssiCode, ssiName,areaCode, sMain,syBo).generate("Hori", false, null);
				generateReportS5(document, ms,sMain,ssiCode, ssiName,areaCode);
				reportS3(document, ms, sMain, ssiCode, ssiName, areaCode);
				//创建环境损伤和偶然损伤检查要求确定
				new ReportS6(document, ms, ssiCode, ssiName, areaCode, sMain, reportStructDao, s6Bo).generate("Hori", false, null);
				new ReportS7(document, ms,list, s8Dao,comAreaBo,isSsi,unSsiDao).generate("Hori", false, null);
			}else if(sMain.getIsSsi()==0&&sMain.getIsAna()!=null&&sMain.getIsAna()==1){
				isSsi++;
				new ReportUnSsi(document,sMain, ms,comAreaBo,unSsiDao).generate("Hori", false, null);
			}
			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	/**
	 * 封面
	 * @param document
	 * @param ms
	 * @param list 
	 * @param list 
	 * @param s2 
	 */
	public void generateReportCover(Document document, ComModelSeries ms, List<SMain> list, S2 s2){
		new ReportCover(document,ms,list,s2).generate("Hori", false, null);
	}
	
	/**
	 * 结构组成
	 * @param document
	 * @param ms
	 * @param sMain
	 */
	public void generateReportS1(Document document, ComModelSeries ms,SMain sMain,String ssiCode,String ssiName){
		new ReportS1(document, ms,sMain,ssiCode,ssiName,comAreaBo,s1Bo).generate("Hori", false, null);
	}
	
	/**
	 * 创建环境损伤评级
	 * @param document
	 * @param ms
	 * @param sMain
	 * @param ssiCode
	 * @param ssiName
	 * @param areaCode 
	 */
	public void generateReportS4(Document document, ComModelSeries ms, SMain sMain, String ssiCode, String ssiName, String areaCode){
		Set<SStep> setSStep = sMain.getSSteps();
		SStep sStep=null;
		if(setSStep!=null&&setSStep.size()>0){
			Iterator<SStep> sStepIt = setSStep.iterator();
			sStep=sStepIt.next();
		}
		if(sStep!=null){
			int isFirst=0;
			if(sStep.getS4aIn()!=3||sStep.getS4aOut()!=3){
				new ReportS4a(document,ms,ssiCode,ssiName,areaCode,s4Bo,sMain).generate("Hori", false, null);
				isFirst++;
			}
			if(sStep.getS4bIn()!=3||sStep.getS4bOut()!=3){
				new ReportS4b(document,ms,ssiCode,ssiName,areaCode,s4Bo,sMain,isFirst).generate("Hori", false, null);
			}
		}
	}
	
	/**
	 * 创建偶然性损伤评级
	 * @param document
	 * @param ms
	 * @param sMain
	 * @param ssiCode
	 * @param ssiName
	 * @param areaCode 
	 */
	public void generateReportS5(Document document, ComModelSeries ms, SMain sMain, String ssiCode, String ssiName, String areaCode){
		Set<SStep> setSStep = sMain.getSSteps();
		SStep sStep=null;
		if(setSStep!=null&&setSStep.size()>0){
			Iterator<SStep> sStepIt = setSStep.iterator();
			sStep=sStepIt.next();
		}
		if(sStep!=null){
			if(sStep.getS5aIn()!=3||sStep.getS5aOut()!=3){
				new ReportS5a(document,ms,ssiCode,ssiName,areaCode,s5Bo,sMain).generate("Hori", false, null);
			}
			if(sStep.getS5bIn()!=3||sStep.getS5bOut()!=3){
				new ReportS5b(document,ms,ssiCode,ssiName,areaCode,s5Bo,sMain).generate("Hori", false, null);
			}
		}
	}
	
	/**
	 * 疲劳损伤检查
	 * @param document
	 * @param ms
	 * @param sMain
	 * @param ssiCode
	 * @param ssiName
	 * @param areaCode
	 */
	private void reportS3(Document document, ComModelSeries ms, SMain sMain, String ssiCode, String ssiName, String areaCode){
		new ReportS3(document,ms,ssiCode,ssiName,areaCode,sMain,reportStructDao).generate("Hori", false, null);
	}
	
	public String getReportPath(String dataString){
		return ComacConstants.REPORT_FILE_PATH +"struct/"+ dataString + "/";
	}
	
	public String getAreaCode(SMain sMain){
		Set<S1> setS1 = sMain.getS1s();
		Iterator<S1> it = setS1.iterator();
		S1 s1;
		String areaId="";
		Set<String> areaIdSet = new HashSet<String>();
		String areaCode ="";
		while(it.hasNext()){
			s1=it.next();
			if(s1!=null&&s1.getOwnArea()!=null){
				String[] areaIds= s1.getOwnArea().split(",");
				for(String str : areaIds){
					areaIdSet.add(str);
				}
			}
		}
		if(areaIdSet.size()>0){
			Iterator<String> it1 = areaIdSet.iterator();
			while(it1.hasNext()){
				areaId += it1.next()+",";
			}
			areaId = areaId.substring(0, areaId.length()-1);
			areaCode = comAreaBo.getAreaCodeByAreaId(areaId);
			
		}
		return areaCode;
	}
	
	/**
	 * 查询当前ata下所有SMain的数据
	 * @param sMain
	 * @param ms
	 * @param userId
	 * @param list
	 */
	public void getSMainList(SMain sMain,ComModelSeries ms,String userId,List<SMain> list){
		if(sMain.getComAta()!=null){
		  List<SMain> list1 =this.ssiMainDao.getSMainListByParentAtaId(
				  sMain.getComAta().getAtaId(), ms.getModelSeriesId(), userId);
		  if(list1!=null||list.size()>0){
			  ComAta ata;
			  for(SMain sMain1 : list1){
				  if(sMain1.getComAta()!=null){
					  ata = (ComAta) this.loadById(ComAta.class, sMain1.getComAta().getAtaId());
					  sMain1.setComAta(ata);
					  list.add(sMain1);
					  getSMainList(sMain1,ms,userId,list);
				  }else{
					  list.add(sMain1);
				  }
			  }
		  }
		}else{
			list.add(sMain);
		}
	}
	
	public String getStr(String s){
		return (s == null || "null".equals(s)) ? "" : s.replaceAll("\r", "");
	}
	
	public ISsiMainDao getSsiMainDao() {
		return ssiMainDao;
	}
	public void setSsiMainDao(ISsiMainDao ssiMainDao) {
		this.ssiMainDao = ssiMainDao;
	}
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	public IS4Bo getS4Bo() {
		return s4Bo;
	}
	public void setS4Bo(IS4Bo s4Bo) {
		this.s4Bo = s4Bo;
	}
	public IS5Bo getS5Bo() {
		return s5Bo;
	}
	public void setS5Bo(IS5Bo s5Bo) {
		this.s5Bo = s5Bo;
	}
	public IReportStructDao getReportStructDao() {
		return reportStructDao;
	}
	public void setReportStructDao(IReportStructDao reportStructDao) {
		this.reportStructDao = reportStructDao;
	}
	public IS6Bo getS6Bo() {
		return s6Bo;
	}
	public void setS6Bo(IS6Bo s6Bo) {
		this.s6Bo = s6Bo;
	}
	public IS8Dao getS8Dao() {
		return s8Dao;
	}
	public void setS8Dao(IS8Dao s8Dao) {
		this.s8Dao = s8Dao;
	}
	public IUnSsiDao getUnSsiDao() {
		return unSsiDao;
	}
	public void setUnSsiDao(IUnSsiDao unSsiDao) {
		this.unSsiDao = unSsiDao;
	}
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}
	public ISyBo getSyBo() {
		return syBo;
	}
	public void setSyBo(ISyBo syBo) {
		this.syBo = syBo;
	}
	
}
