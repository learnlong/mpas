package com.rskytech.paramdefinemanage.bo.impl;

import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.ibm.icu.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IMrbVersionBo;
import com.rskytech.paramdefinemanage.bo.IMrbrReportBo;
import com.rskytech.paramdefinemanage.bo.ITaskMrbMaintainBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusMpdSection;
import com.rskytech.pojo.CusMrbChapter;
import com.rskytech.pojo.CusMrbPs;
import com.rskytech.pojo.CusMrbSection;
import com.rskytech.pojo.TaskMpdVersion;
import com.rskytech.pojo.TaskMrb;
import com.rskytech.report.pdf.reportBase.ReportBase;
import com.rskytech.util.CustomMrbPageNumHelper;
import com.rskytech.util.StringUtil;

public class MrbrReportBo extends BaseBO implements IMrbrReportBo{
	
	private IMrbVersionBo mrbVersionBo;
	private ITaskMrbMaintainBo taskMrbMaintainBo;
	
	
	
	public IMrbVersionBo getMrbVersionBo() {
		return mrbVersionBo;
	}

	public void setMrbVersionBo(IMrbVersionBo mrbVersionBo) {
		this.mrbVersionBo = mrbVersionBo;
	}
		

	public ITaskMrbMaintainBo getTaskMrbMaintainBo() {
		return taskMrbMaintainBo;
	}

	public void setTaskMrbMaintainBo(ITaskMrbMaintainBo taskMrbMaintainBo) {
		this.taskMrbMaintainBo = taskMrbMaintainBo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CusMrbChapter> getChapterByCode(String msId, String chapterCode) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbChapter.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("chapterCode", chapterCode));
		return this.findByCritera(dc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusMrbSection> getSectionByChapter(String chapterId, String msId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbSection.class);
		dc.add(Restrictions.eq("modelSeriesId", msId));
		dc.add(Restrictions.eq("cusMrbChapter.chapterId", chapterId));
		return this.findByCritera(dc);
	}
	
	@Override
	public boolean saveChapter(CusMrbChapter c, String flg, String userid) throws BusinessException {
		this.saveOrUpdate(c, flg, userid);
		return true;
	}
	
	@Override
	public boolean saveSection(CusMrbSection s, String operateFlg, String userid) throws BusinessException {
		saveOrUpdate(s, operateFlg, userid);
		return true;
	}
	
	@Override
	public boolean saveMrbPs(CusMrbPs ps, String operateFlg, String userid) throws BusinessException {
		saveOrUpdate(ps, operateFlg, userid);
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean deleteMrbPsById(Class clazz, String id, String userid) throws BusinessException {
		CusMrbPs ps = (CusMrbPs) this.loadById(clazz, id);
		this.deletePsFile(ps.getPsUrl());
		this.delete(clazz, id, userid);
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean deleteSectionById(Class clazz, String sectionId, String userId) throws BusinessException {
		this.delete(clazz, sectionId, userId);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkSectionCode(String sectionId, String chapterId, String sectionCode) throws BusinessException {
 		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbSection.class);
		dc.add(Restrictions.eq("cusMrbChapter.chapterId", chapterId));
		if (sectionId != null && !("").equals(sectionId)) {
			dc.add(Restrictions.ne("sectionId", sectionId));
		}
		dc.add(Restrictions.eq("sectionCode", sectionCode));
		List<CusMpdSection> cusMpdSections = this.findByCritera(dc);
		if (cusMpdSections.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusMrbPs> findByModelSeriesId(String msid) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msid));
		dc.addOrder(Order.asc("psFlg"));
		dc.addOrder(Order.asc("psSort"));
		return this.findByCritera(dc);
	}
	
	public void deletePsFile(String urlString) throws BusinessException {
		if (urlString != null && !urlString.equals("")) {
			String filePath = urlString.substring(urlString.lastIndexOf(ComacConstants.MPD_PS_SAVE_PATH));
			String realPath = ServletActionContext.getServletContext().getRealPath(filePath);
			File file = new File(realPath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isPsFlgUnique(String modelSeriesId, String cusMrbPsId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		if (cusMrbPsId != null && !"".equals(cusMrbPsId)) {
			dc.add(Restrictions.ne("psId", cusMrbPsId));
		}
		dc.add(Restrictions.eq("psFlg", ComacConstants.PSFLG_0));
		List<CusMrbPs> list = this.findByCritera(dc);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkMrbPsCode(String msId, String mrbPsId, Integer mrbPsCode) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (mrbPsId != null && !("").equals(mrbPsId)) {
			dc.add(Restrictions.ne("psId", mrbPsId));
		}
		dc.add(Restrictions.eq("psSort", mrbPsCode));
		List<CusMpdSection> cusMpdSections = this.findByCritera(dc);
		if (cusMpdSections.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * 获取mrb附录数据
	 * 
	 * @param msid
	 *            机型
	 * @param psFlag
	 *            附录区分（0报表首页、1报表附录）
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<CusMrbPs> getCusMrbPs(String msid, Integer psFlg) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMrbPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msid));
		dc.add(Restrictions.eq("psFlg", psFlg));
		return this.findByCritera(dc);
	}
	
	/**
	 * 设置目录(每一章下面的节)
	 */
	@SuppressWarnings("unchecked")
	public void setContents(List<MrbContents> mrbContents, CusMrbChapter currentChapter, String language, String path, int allPage) {
		TreeSet<CusMrbSection> ts = new TreeSet<CusMrbSection>(new Comparator<CusMrbSection>() {
			@Override
			public int compare(CusMrbSection o1, CusMrbSection o2) {
				Double d1 = Double.parseDouble(o1.getSectionCode());
				Double d2 = Double.parseDouble(o2.getSectionCode());
				Double result = d1 - d2;
				if (result > 0) {// 节排序
					return 1;
				}
				else if (result < 0) {
					return -1;
				}
				return 0;
			}
		});
		ts.addAll(currentChapter.getCusMrbSections());
		for (Iterator<CusMrbSection> iterator2 = ts.iterator(); iterator2.hasNext();) {
			CusMrbSection section = iterator2.next();
			String text = section.getSectionCode() + " " +  section.getSectionName();
			int temppage = searchText(path, text);
			mrbContents.add(new MrbContents(text, temppage + allPage, 0));
		}
	}
	
	// 由于MRB的章是固定的 所以判断 MRB的顾定章是否存在，不存在添加上
	private void checkChapter(String modelSeriesId) {
		List<CusMrbChapter> chaters = this.getChapterByCode(modelSeriesId, ComacConstants.MRBCHAPTERCODE[0]);
		if (chaters != null && chaters.size() > 0) {
			
		}
		else {
			ComModelSeries ms = (ComModelSeries) this.loadById(ComModelSeries.class, modelSeriesId);
			for (int i = 0; i < ComacConstants.MRBCHAPTERCODE.length; i++) {
				CusMrbChapter c = new CusMrbChapter();
				c.setComModelSeries(ms);
				c.setChapterCode(ComacConstants.MRBCHAPTERCODE[i]);
				c.setChapterName(ComacConstants.MRBCHAPTERNAMECN[i]);
	
				this.saveOrUpdate(c, ComacConstants.DB_INSERT, modelSeriesId);
				
			}
		}
	}
	
	/**
	 * 生成报表
	 */
	@Override
	public String generateMrbPdf(String modelSeriesId, String language) throws BusinessException {
		checkChapter(modelSeriesId);
		ComModelSeries comModelSeries = (ComModelSeries) this.loadById(ComModelSeries.class, modelSeriesId);
		String dataString = StringUtil.getDataString();
		String timeString = StringUtil.getTimesString();
		
		String tempDirPath = ServletActionContext.getServletContext().getRealPath(ComacConstants.MPD_PS_PDF_PATH) + "/" + dataString;
		File tempDir = new File(tempDirPath);
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		tempDir = null;// 销毁无用对象
		
		String filePath = tempDirPath + "/mrb" + timeString + ".pdf";
		
		/** 开始生成mrb的pdf文件 **/
		List<String> pdfPathList = new ArrayList<String>();
		// 上传的报表首页
		HttpServletRequest request = ServletActionContext.getRequest();
		List<CusMrbPs> mrbHomePage = this.getCusMrbPs(modelSeriesId, ComacConstants.PSFLG_0);
		String HomePageRealFilePath = null;
		if (mrbHomePage != null && mrbHomePage.size() > 0) {
			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + mrbHomePage.get(0).getPsUrl();
			HomePageRealFilePath = ServletActionContext.getServletContext().getRealPath(url.substring(url.lastIndexOf(ComacConstants.MPD_PS_SAVE_PATH)));
			pdfPathList.add(HomePageRealFilePath);
		}
		
		// 用户更改单记录
		List<TaskMpdVersion> mrbVersionsLists = mrbVersionBo.getMRBList(modelSeriesId, null);
		if (mrbVersionsLists != null && mrbVersionsLists.size() > 0) {
			String path = tempDirPath + "/ROR.pdf";
			generateROR(mrbVersionsLists, comModelSeries, path, language);
			pdfPathList.add(path);
		}
		
		CusMrbChapter currentChapter = null;
		
		for (int i = 0; i < 2; i++) {
			
			// 更改摘要 和 有效页清单
			String socTempPdf = tempDirPath + "/temp" + i + ".pdf";
			currentChapter = getChapter(modelSeriesId, i);
			this.generateNotChpater(comModelSeries, language, currentChapter, socTempPdf);
			pdfPathList.add(socTempPdf);
		}
		
		// 目录
		List<MrbContents> mrbContents = new ArrayList<MrbContents>();
		
		List<String> tempList = new ArrayList<String>();
		// 第一章 介绍
		
		String section1TempPdf = tempDirPath + "/section1TempPdf.pdf";
		currentChapter = getChapter(modelSeriesId, 2);
		this.generateChpater1(comModelSeries, language, currentChapter, section1TempPdf);
		mrbContents.add(new MrbContents(currentChapter.getChapterName(), 1, 1));
		setContents(mrbContents, currentChapter, language, section1TempPdf, 0);
		
		tempList.add(section1TempPdf);
		
		int allPage = 0;
		allPage += getPdfPage(section1TempPdf);
		
		for (int i = 3; i < 6; i++) {
			
			// 第二章,第三章,第四章
			String sectionHeaderTempPdf = tempDirPath + "/section" + i + "HeaderTempPdf.pdf";
			String sectionTempPdf = tempDirPath + "/section" + i + "TempPdf.pdf";
			currentChapter = getChapter(modelSeriesId, i);
			// 章头
			this.generateChpaterHeader(comModelSeries, language, currentChapter, sectionHeaderTempPdf, allPage + 1);
			mrbContents.add(new MrbContents( currentChapter.getChapterName(),
					allPage + 1, 1));
			tempList.add(sectionHeaderTempPdf);
			// 内容
			this.generateChpater2To4(comModelSeries, language, currentChapter, sectionTempPdf, allPage + 2);
			this.setContents(mrbContents, currentChapter, language, sectionTempPdf, allPage + 1);
			tempList.add(sectionTempPdf);
			allPage += getPdfPage(sectionTempPdf) + 1;
			// 任务
			String code = null;
			int chapterNumber = 2;
			List<TaskMrb> mrbLists = null;
			if (i == 3) {
				code = ComacConstants.SYSTEM_CODE;
				chapterNumber = 2;
				mrbLists = taskMrbMaintainBo.getMrbTaskBySourceSystem(modelSeriesId, code);
			}
			else if (i == 4) {
				code = ComacConstants.STRUCTURE_CODE;
				chapterNumber = 3;
				mrbLists = taskMrbMaintainBo.getMrbTaskBySourceSystem(modelSeriesId, code);
			}
			else {
				code = ComacConstants.ZONAL_CODE;
				chapterNumber = 4;
				mrbLists = taskMrbMaintainBo.getMrbTaskBySourceSystem(modelSeriesId, code);
				List<TaskMrb> temp = taskMrbMaintainBo.getMrbTaskBySourceSystem(modelSeriesId, ComacConstants.LHIRF_CODE);
				mrbLists.addAll(temp);
			}
			if (mrbLists != null && mrbLists.size() > 0) {
				String chapterMrbTask = tempDirPath + "/chapter" + i + "MrbTask.pdf";
				this.generateMRBTask(comModelSeries, mrbLists, chapterMrbTask, language, allPage, chapterNumber);
				allPage += getPdfPage(chapterMrbTask);
				tempList.add(chapterMrbTask);
			}
		}
		
		// 生成目录的PDF
		String contentsPdf = tempDirPath + "/contentsPdf.pdf";
		generateContent(comModelSeries, language, mrbContents, contentsPdf);
		pdfPathList.add(contentsPdf);
		
		pdfPathList.addAll(tempList);
		
		List<String> deleteList = new ArrayList<String>();
		deleteList.addAll(pdfPathList);
		deleteList.remove(HomePageRealFilePath);
		// 上传的报表附录
		List<CusMrbPs> mrbPS = this.getCusMrbPs(modelSeriesId, ComacConstants.PSFLG_1);
		if (mrbPS != null && mrbPS.size() > 0) {
			for (CusMrbPs cusMrbPs : mrbPS) {
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cusMrbPs.getPsUrl();
				String realFilePath = ServletActionContext.getServletContext().getRealPath(url.substring(url.lastIndexOf(ComacConstants.MPD_PS_SAVE_PATH)));
				pdfPathList.add(realFilePath);
			}
		}
		
		MpdPdfHelper.mergePdfFiles(pdfPathList, filePath);
		MpdPdfHelper.deleteTempPdfFile(deleteList);
		
		String path = request.getContextPath();
		return path + ComacConstants.MPD_PS_PDF_PATH + dataString + "/mrb" + timeString + ".pdf";
	}
	
	/**
	 * 生成用户更改记录
	 * 
	 * @return
	 */
	public String generateROR(List<TaskMpdVersion> MrbLists, ComModelSeries ms, String path, String language) {
		Document document = null;
		try {
			document = new Document();
			ReportBase.setPage(document);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			CustomMrbPageNumHelper event = new CustomMrbPageNumHelper(ReportBase.A4WIWTH, ReportBase.A4HEIGHT, 0);
			writer.setPageEvent(event);
			document.open();
			
			BaseFont bfChinese = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontTitle = new Font(bfChinese, 12.5f, Font.BOLD);
			Font fontNormal = new Font(bfChinese, 12.5f, Font.NORMAL);
			
			PdfPTable table = new PdfPTable(new float[] { 0.1f, 0.7f, 0.1f, 0.1f });
			table.setHeaderRows(3);
			table.setWidthPercentage(100);
			
			PdfPCell cell = new PdfPCell(new Phrase( ms.getModelSeriesName() + " 飞机维修大纲建议稿", fontNormal));
			cell.setColspan(4);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.enableBorderSide(Rectangle.BOTTOM);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(""));
			cell.setFixedHeight(30f);
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.setColspan(4);
			table.addCell(cell);
			
		
				table.addCell(setCellParam("版本", fontTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
				table.addCell(setCellParam("更改说明", fontTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
				table.addCell(setCellParam("修改日期", fontTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
				table.addCell(setCellParam("责任人", fontTitle, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
			
			
			table.setSplitLate(false);
			table.setSplitRows(true);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for (TaskMpdVersion taskMpdVersion : MrbLists) {
				table.addCell(setCellParam(taskMpdVersion.getVersionNo().toString(), fontNormal, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
				table.addCell(setCellParam( taskMpdVersion.getVersionDescCn(), fontNormal, null, Element.ALIGN_MIDDLE));
				table.addCell(setCellParam(taskMpdVersion.getModifyDate() == null ? "" : sdf.format(taskMpdVersion.getModifyDate()), fontNormal,
						Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
				ComUser user = (ComUser) this.loadById(ComUser.class, taskMpdVersion.getModifyUser());
				table.addCell(setCellParam(user.getUserName(), fontNormal, Element.ALIGN_CENTER,
						Element.ALIGN_MIDDLE));
			}
			document.add(table);
			
		}
		catch (Exception e) {
			
		}
		finally {
			document.close();
		}
		
		return null;
	}
	
	public PdfPCell setCellParam(String context, Font font, Integer align, Integer valign) {
		PdfPCell cell = new PdfPCell(new Paragraph(context, font));;
		if (align != null)
			cell.setHorizontalAlignment(align);
		if (valign != null)
			cell.setVerticalAlignment(valign);
		cell.setFixedHeight(25f);
		return cell;
	}
	
	/**
	 * 生成目录前的SOC(更改摘要),LEP(有效页清单)
	 * 
	 * @param ms
	 *            机型
	 * @param language
	 *            语言
	 * @param chapter
	 *            章
	 * @param filePath
	 *            路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String generateNotChpater(ComModelSeries ms, String language, CusMrbChapter chapter, String filePath) {
		PD4ML html = new PD4ML();
		FileOutputStream os = null;
		try {
			String content = chapter.getChapterContent();
			os = new FileOutputStream(filePath);
			StringBuffer sb = new StringBuffer();
			sb.append("<div style='width:100%;text-align:center;padding:10px'><font size='20px'>"
					+ chapter.getChapterName() + "</font></div>");
			if (!("null").equals(content) && ("").equals(content)) {
				sb.append("<div style='width:100%;padding:15px'>" + content + "</div>");
			}
			
			if (chapter.getChapterId() != null) {
				TreeSet<CusMrbSection> ts = new TreeSet<CusMrbSection>(new Comparator<CusMrbSection>() {
					@Override
					public int compare(CusMrbSection o1, CusMrbSection o2) {
						Double d1 = Double.parseDouble(o1.getSectionCode());
						Double d2 = Double.parseDouble(o2.getSectionCode());
						Double result = d1 - d2;
						if (result > 0) {// 节排序
							return 1;
						}
						else if (result < 0) {
							return -1;
						}
						return 0;
					}
				});
				ts.addAll(chapter.getCusMrbSections());
				
				if (ts != null && ts.size() > 0) {
					for (Iterator<CusMrbSection> iterator2 = ts.iterator(); iterator2.hasNext();) {
						CusMrbSection section = iterator2.next();
						sb.append("<div style style='width:100%;padding:5px'>" + section.getSectionCode() + " "
								+ section.getSectionName() + "</div>");
						String desc = section.getSectionContent();
						desc = StringUtil.recoveryContextPath(desc);
						sb.append("<div style style='width:100%;padding:15px'>" + (desc == null || ("null").equals(desc) ? "" : desc) + "</div>");
					}
				}
			}
			
			StringReader strReader = new StringReader(sb.toString());
			
			// 设置PDF的属性及页眉
			setPD4Ml(language, ms, html);
			
			// 页脚 没有页码的页脚
			PD4PageMark markFooter = new PD4PageMark();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			markFooter.setHtmlTemplate("<html><body>" + sdf.format(new Date()) + "</body></html>");
			markFooter.setAreaHeight(50);
			html.setPageFooter(markFooter);
			
			html.enableImgSplit(false);
			html.render(strReader, os);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 生成第一章的pdf
	 * 
	 * @param ms
	 * @param language
	 * @param chapter
	 * @param filePath
	 * @return
	 */
	public String generateChpater1(ComModelSeries ms, String language, CusMrbChapter chapter, String filePath) {
		
		PD4ML html = new PD4ML();
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			StringReader strReader = new StringReader(pdfContent(language, chapter));
			
			// 设置PDF的属性及页眉
			setPD4Ml(language, ms, html);
			
			// 当生成的Pdf有页码时使用这个方法
			getPdfFooter(html, 1);
			
			html.enableImgSplit(false);
			html.render(strReader, os);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 生成第二章到第四章的pdf
	 * 
	 * @param ms
	 * @param language
	 * @param chapter
	 * @param filePath
	 * @param beginPage
	 *            开始页
	 * @return
	 */
	public String generateChpater2To4(ComModelSeries ms, String language, CusMrbChapter chapter, String filePath, int beginPage) {
		
		PD4ML html = new PD4ML();
		FileOutputStream os = null;
		try {
			String content = pdfContent(language, chapter);
			if (!("").equals(content)) {
				os = new FileOutputStream(filePath);
				StringReader strReader = new StringReader(content);
				// 设置PDF的属性及页眉
				setPD4Ml(language, ms, html);
				
				// 当生成的Pdf有页码时使用这个方法
				getPdfFooter(html, beginPage);
				
				html.enableImgSplit(false);
				html.render(strReader, os);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据序列取Chapter
	 * 
	 * @param count
	 * @return
	 */
	public CusMrbChapter getChapter(String modelSeriesId, int count) {
		List<CusMrbChapter> chapters = this.getChapterByCode(modelSeriesId, ComacConstants.MRBCHAPTERCODE[count]);
		CusMrbChapter currentChapter = null;
		if (chapters != null && chapters.size() > 0) {
			currentChapter = chapters.get(0);
		}
		else {
			currentChapter = new CusMrbChapter();
			currentChapter.setChapterCode(ComacConstants.MRBCHAPTERCODE[count]);
			currentChapter.setChapterContent(ComacConstants.MRBCHAPTERNAMECN[count]);
		
		}
		return currentChapter;
	}
	
	/**
	 * 生成Pdf的内容
	 * 
	 * @param language
	 * @param chapter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String pdfContent(String language, CusMrbChapter chapter) {
		String content = chapter.getChapterContent();
		content = StringUtil.recoveryContextPath(content);
		StringBuffer sb = new StringBuffer();
		if ((ComacConstants.MRBCHAPTERCODE[2]).equals(chapter.getChapterCode())) {
			sb.append("<div style='width:100%;text-align:center;padding:10px'><font size='5px'>"
					+ chapter.getChapterName() + "</font></div>");
		}
		if (!("null").equals(content) && ("").equals(content)) {
			sb.append("<div style='width:100%;padding:15px'>" + content + "</div>");
		}
		
		if (chapter.getChapterId() != null) {
			TreeSet<CusMrbSection> ts = new TreeSet<CusMrbSection>(new Comparator<CusMrbSection>() {
				@Override
				public int compare(CusMrbSection o1, CusMrbSection o2) {
					Double d1 = Double.parseDouble(o1.getSectionCode());
					Double d2 = Double.parseDouble(o2.getSectionCode());
					Double result = d1 - d2;
					if (result > 0) {// 节排序
						return 1;
					}
					else if (result < 0) {
						return -1;
					}
					return 0;
				}
			});
			ts.addAll(chapter.getCusMrbSections());
			if (ts != null && ts.size() > 0) {
				for (Iterator<CusMrbSection> iterator2 = ts.iterator(); iterator2.hasNext();) {
					CusMrbSection section = iterator2.next();
					sb.append("<div style style='width:100%;padding:5px'>" + section.getSectionCode() + " "
							+ section.getSectionName()  + "</div>");
					String desc =  section.getSectionContent() ;
					sb.append("<div style style='width:100%;padding:15px'>" + (desc == null || ("null").equals(desc) ? "" : desc) + "</div>");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 设置有页码的页脚
	 * 
	 * @param language
	 * @param ms
	 */
	public void getPdfFooter(PD4ML html, int beginPage) {
		// 页脚
		PD4PageMark markFooter = new PD4PageMark();
		if (beginPage > 0) {
			markFooter.setInitialPageNumber(beginPage);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		markFooter.setHtmlTemplate("<html><body>" + "<table width='100%'>" + "<tr>" + "<td width='30%'>" + sdf.format(new Date()) + "</td>"
				+ "<td width='40%' align='center'>$[page]</td>" + "<td width='30%'>&nbsp;</td>" + "</tr></table></body></html>");
		markFooter.setAreaHeight(50);
		html.setPageFooter(markFooter);
	}
	
	/**
	 * 设置Html属性 及页眉
	 */
	public void setPD4Ml(String language, ComModelSeries ms, PD4ML html) {
		try {
			html.setPageSize(html.changePageOrientation(PD4Constants.A4));
			html.useTTF("java:resource", true);
			html.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "SIMSUN");
			html.enableDebugInfo();
			html.setPageInsets(new Insets(55, 48, 5, 50));
			html.setHtmlWidth(950);
			// 页眉
			PD4PageMark markHeader = new PD4PageMark();
			markHeader.setHtmlTemplate("<html><style type='text/css'>.topTable{border-bottom:black solid 1px;}</style><body>"
					+ "<table style='width:100%;' class='topTable' cellpadding='1' cellspacing='0'>"
					+ "<tr><td width='100%'>"
					+  ms.getModelSeriesName() + " 飞机维修大纲建议稿"  + "</td></tr>" + "</table></body></html>");
			markHeader.setAreaHeight(40);
			html.setPageHeader(markHeader);
		}
		catch (Exception e) {
			
		}
	}
	
	/**
	 * 生成章头
	 * 
	 * @param ms
	 *            机型
	 * @param language
	 *            语言
	 * @param chapter
	 *            章
	 * @param filePath
	 *            路径
	 * @param beginPage
	 *            开始页
	 * @return
	 */
	public String generateChpaterHeader(ComModelSeries ms, String language, CusMrbChapter chapter, String filePath, int beginPage) {
		PD4ML html = new PD4ML();
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body><table width='100%'><tr><td width='100%' height='500px' align='center' valign='center'><font size='20px'>"
					+ chapter.getChapterName() + "</font></td></tr></table></body></html>");
			StringReader strReader = new StringReader(sb.toString());
			
			// 设置PDF的属性及页眉
			setPD4Ml(language, ms, html);
			
			// 当生成的Pdf有页码时使用这个方法
			getPdfFooter(html, beginPage);
			
			html.enableImgSplit(false);
			html.render(strReader, os);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 生成目录的PDF
	 * 
	 * @param ms
	 *            机型
	 * @param language
	 *            语言
	 * @param chapter
	 *            章
	 * @param filePath
	 *            路径
	 * @return
	 */
	public String generateContent(ComModelSeries ms, String language, List<MrbContents> contents, String filePath) {
		PD4ML html = new PD4ML();
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			StringBuffer sb = new StringBuffer();
			sb.append("<html><body><table width='100%'><tr><td align='center' colspan=2><font size='3'>"
					+ "文件目录"  + "</font></td></tr>");
			
			if (contents != null && contents.size() > 0) {
				for (MrbContents mrbContents : contents) {
					sb.append("<tr><td");
					if (mrbContents.isChapter == 0) {
						sb.append(" style='padding-left:15px'>");
					}
					else {
						sb.append(">");
					}
					sb.append(mrbContents.getName());
					sb.append("</td>");
					sb.append("<td align='right'>");
					sb.append(mrbContents.getPage());
					sb.append("</td></tr>");
				}
			}
			sb.append("</table></body></html>");
			
			StringReader strReader = new StringReader(sb.toString());
			html.setPageSize(html.changePageOrientation(PD4Constants.A4));
			html.useTTF("java:resource", true);
			html.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "SIMSUN");
			html.enableDebugInfo();
			html.setPageInsets(new Insets(55, 48, 5, 50));
			html.setHtmlWidth(950);
			// 页眉
			PD4PageMark markHeader = new PD4PageMark();
			markHeader.setHtmlTemplate("<html><style type='text/css'>.topTable{border-bottom:black solid 1px;}</style><body>"
					+ "<table style='width:100%;' class='topTable' cellpadding='1' cellspacing='0'>"
					+ "<tr><td width='100%'>"
					+ ms.getModelSeriesName() + " 飞机维修大纲建议稿" + "</td></tr>" + "</table></body></html>");
			markHeader.setAreaHeight(20);
			html.setPageHeader(markHeader);
			
			// 页脚
			PD4PageMark markFooter = new PD4PageMark();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			markFooter.setHtmlTemplate("<html><body>" + sdf.format(new Date()) + "</body></html>");
			markFooter.setAreaHeight(50);
			html.setPageFooter(markFooter);
			
			html.enableImgSplit(false);
			html.render(strReader, os);			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 生成MRB任务
	 * 
	 * @param chapterNumber
	 *            是第几章的任务表
	 * @param beginPage
	 *            从第几页开始
	 * @return
	 */
	public String generateMRBTask(ComModelSeries comModelSeries, List<TaskMrb> mrbLists, String path, String language, int startPageNum, int chapterNumber) {
		Document document = null;
		try {
			document = new Document();
			ReportBase.setPage(document);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			CustomMrbPageNumHelper event = new CustomMrbPageNumHelper(ReportBase.A4WIWTH, ReportBase.A4HEIGHT, startPageNum);
			writer.setPageEvent(event);
			document.open();
			
			BaseFont bfChinese = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			Font fontTitle = new Font(bfChinese, 12.5f, Font.BOLD);
			Font fontNormal = new Font(bfChinese, 12.5f, Font.NORMAL);
			
			PdfPTable table = null;
			if (chapterNumber == 2) {
				// 第二章任务
				float[] nameWidth = { 0.12f, 0.09f, 0.08f, 0.08f, 0.08f, 0.18f, 0.1f, 0.18f, 0.09f };
				String[] name = MpdPdfHelper.MRBSYSTASKHEADCN;
				
				table = new PdfPTable(nameWidth);
				if (mrbLists != null && mrbLists.size() > 1) {
					table.setHeaderRows(3);
				}
				table.setHeaderRows(3);
				table.setWidthPercentage(100);
				
				PdfPCell cell = new PdfPCell(new Phrase(comModelSeries.getModelSeriesName() + " 飞机维修大纲建议稿", fontNormal));
				cell.setColspan(nameWidth.length);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.enableBorderSide(Rectangle.BOTTOM);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(""));
				cell.setFixedHeight(30f);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(nameWidth.length);
				table.addCell(cell);
				
				for (int i = 0; i < name.length; i++) {
					cell = new PdfPCell(new Phrase(name[i], fontTitle));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}
				
				for (TaskMrb mrb : mrbLists) {
					cell = new PdfPCell(new Phrase(mrb.getMrbCode(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getFailureCauseType()) ? "" : mrb.getFailureCauseType(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getTaskType()) ? "" : mrb.getTaskType(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					// 门槛值
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getTaskIntervalOriginal()) ? "" : mrb.getTaskIntervalOriginal(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					// 区域Code
					if (mrb.getOwnArea() == null) {
						cell = new PdfPCell(new Phrase("", fontNormal));
					}
					else {
						String[] areas = mrb.getOwnArea().split(",");
						StringBuffer sbff = new StringBuffer();
						for (int k = 0; k < areas.length; k++) {
							ComArea area = (ComArea) this.loadById(ComArea.class, areas[k].trim());
							if (area != null) {
								sbff.append(area.getAreaCode() + ",\n");
							}
						}
						if (sbff.length() == 0) {
							sbff.append(",\n");
						}
						cell = new PdfPCell(new Phrase(sbff.toString().substring(0, sbff.length() - 2), fontNormal));
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String reachWay = mrb.getReachWay();
					cell = new PdfPCell(new Phrase(("null").equals(reachWay) ? "" : reachWay, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getEffectiveness()) ? "" : mrb.getEffectiveness(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String desc =  mrb.getTaskDesc();
					cell = new PdfPCell(new Phrase(("null").equals(desc) ? "" : desc, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase( ComacConstants.SYS_CN, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
				}
			}
			else if (chapterNumber == 3) {
				// 第三章任务
				float[] nameWidth = { 0.12f, 0.09f, 0.08f, 0.08f, 0.08f, 0.18f, 0.1f, 0.18f, 0.09f };
				String[] name =  MpdPdfHelper.MRBSTRUCTTASKHEADCN ;
				
				table = new PdfPTable(nameWidth);
				if (mrbLists != null && mrbLists.size() > 1) {
					table.setHeaderRows(4);
				}
				table.setWidthPercentage(100);
				
				PdfPCell cell = new PdfPCell(new Phrase( comModelSeries.getModelSeriesName() + " 飞机维修大纲建议稿", fontNormal));
				cell.setColspan(nameWidth.length);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.enableBorderSide(Rectangle.BOTTOM);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(""));
				cell.setFixedHeight(30f);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(nameWidth.length);
				table.addCell(cell);
				
				for (int i = 0; i < name.length; i++) {
					cell = new PdfPCell(new Phrase(name[i], fontTitle));
					if (i < 8) {
						if (i == 2) {
							cell.setColspan(2);
						}
						else {
							cell.setRowspan(2);
						}
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}
				
				for (TaskMrb mrb : mrbLists) {
					cell = new PdfPCell(new Phrase(mrb.getMrbCode(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(mrb.getTaskType(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					// 门槛值
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getTaskIntervalOriginal()) ? "" : mrb.getTaskIntervalOriginal(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					// 间隔
					table.addCell(cell);
					
					// 区域Code
					if (mrb.getOwnArea() == null) {
						cell = new PdfPCell(new Phrase("", fontNormal));
					}
					else {
						String[] areas = mrb.getOwnArea().split(",");
						StringBuffer sbff = new StringBuffer();
						for (int k = 0; k < areas.length; k++) {
							ComArea area = (ComArea) this.loadById(ComArea.class, areas[k].trim());
							if (area != null) {
								sbff.append(area.getAreaCode() + ",\n");
							}
						}
						if (sbff.length() == 0) {
							sbff.append(",\n");
						}
						cell = new PdfPCell(new Phrase(sbff.toString().substring(0, sbff.length() - 2), fontNormal));
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String reachWay =  mrb.getReachWay();
					cell = new PdfPCell(new Phrase(("null").equals(reachWay) ? "" : reachWay, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getEffectiveness()) ? "" : mrb.getEffectiveness(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String desc = mrb.getTaskDesc();
					cell = new PdfPCell(new Phrase(("null").equals(desc) ? "" : desc, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(ComacConstants.STRUCTURE_CN, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
				}
			}
			else {
				// 第四章任务
				float[] nameWidth = { 0.12f, 0.09f, 0.08f, 0.26f, 0.1f, 0.26f, 0.09f };
				String[] name = MpdPdfHelper.MRBAREATASKHEADCN ;
				
				table = new PdfPTable(nameWidth);
				if (mrbLists != null && mrbLists.size() > 1) {
					table.setHeaderRows(3);
				}
				table.setWidthPercentage(100);
				
				PdfPCell cell = new PdfPCell(new Phrase(comModelSeries.getModelSeriesName() + " 飞机维修大纲建议稿", fontNormal));
				cell.setColspan(nameWidth.length);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.enableBorderSide(Rectangle.BOTTOM);
				table.addCell(cell);
				
				cell = new PdfPCell(new Phrase(""));
				cell.setFixedHeight(30f);
				cell.setBorder(PdfPCell.NO_BORDER);
				cell.setColspan(nameWidth.length);
				table.addCell(cell);
				
				for (int i = 0; i < name.length; i++) {
					cell = new PdfPCell(new Phrase(name[i], fontTitle));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}
				
				for (TaskMrb mrb : mrbLists) {
					cell = new PdfPCell(new Phrase(mrb.getMrbCode(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					// 门槛值
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getTaskIntervalOriginal()) ? "" : mrb.getTaskIntervalOriginal(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					// 区域Code
					if (mrb.getOwnArea() == null) {
						cell = new PdfPCell(new Phrase("", fontNormal));
					}
					else {
						String[] areas = mrb.getOwnArea().split(",");
						StringBuffer sbff = new StringBuffer();
						for (int k = 0; k < areas.length; k++) {
							ComArea area = (ComArea) this.loadById(ComArea.class, areas[k].trim());
							if (area != null) {
								sbff.append(area.getAreaCode() + ",\n");
							}
						}
						if (sbff.length() == 0) {
							sbff.append(",\n");
						}
						cell = new PdfPCell(new Phrase(sbff.toString().substring(0, sbff.length() - 2), fontNormal));
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String reachWay =  mrb.getReachWay() ;
					cell = new PdfPCell(new Phrase(("null").equals(reachWay) ? "" : reachWay, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(("null").equals(mrb.getEffectiveness()) ? "" : mrb.getEffectiveness(), fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					String desc = mrb.getTaskDesc();
					cell = new PdfPCell(new Phrase(("null").equals(desc) ? "" : desc, fontNormal));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(""));
					if ((ComacConstants.ZONAL_CODE).equals(mrb.getSourceSystem())) {
						cell = new PdfPCell(new Phrase(ComacConstants.AREA_CN, fontNormal));
					}
					else if (ComacConstants.LHIRF_CODE.equals(mrb.getSourceSystem())) {
						cell = new PdfPCell(new Phrase(ComacConstants.LHIRF_CN, fontNormal));
					}
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
				}
			}
			document.add(table);
			
		}
		catch (Exception e) {
			
		}
		finally {
			document.close();
		}
		
		return null;
	}
	
	/**
	 * 取Pdf的页数
	 * 
	 * @param path
	 * @return
	 */
	private int getPdfPage(String path) {
		PdfReader reader = null;
		int count = 0;
		try {
			reader = new PdfReader(path);
			count = reader.getNumberOfPages();
		}
		catch (IOException e) {
			
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}
		return count;
	}
	
	/**
	 * 在pdf搜文字
	 * 
	 * @param pdfFile
	 * @param text
	 * @return
	 */
	private int searchText(String pdfFile, String text) {
		PDDocument pddDocument = null;
		try {
			pddDocument = PDDocument.load(pdfFile);
			PDFTextStripper textStripper = new PDFTextStripper();
			int lastpage = textStripper.getEndPage();
			String page = null;
			int found = 0;
			for (int i = 1; i <= lastpage; i++) {
				textStripper.setStartPage(i);
				textStripper.setEndPage(i);
				page = textStripper.getText(pddDocument);
				found = page.indexOf(text);
				if (found > 0) {
					return i;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (pddDocument != null) {
				try {
					pddDocument.close();
				}
				catch (Exception e2) {
				}
			}
		}
		return 0;
	}
	
	public class MrbContents {
		private String name;// 目录名称
		private int page;// 所在页数
		private int isChapter;// 是不是章1是,0不是
		
		public MrbContents(String name, int page, int isChapter) {
			super();
			this.name = name;
			this.page = page;
			this.isChapter = isChapter;
		}
		
		public MrbContents() {
			super();
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public int getPage() {
			return page;
		}
		
		public void setPage(int page) {
			this.page = page;
		}
		
		public int getIsChapter() {
			return isChapter;
		}
		
		public void setIsChapter(int isChapter) {
			this.isChapter = isChapter;
		}
		
	}
	
	
}
