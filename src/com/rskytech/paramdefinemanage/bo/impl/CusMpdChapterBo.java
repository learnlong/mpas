package com.rskytech.paramdefinemanage.bo.impl;

import java.awt.Insets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.impl.MpdPdfHelper;
import com.rskytech.paramdefinemanage.bo.ICusMpdChapterBo;
import com.rskytech.paramdefinemanage.bo.ICusMpdPsBo;
import com.rskytech.paramdefinemanage.bo.ITaskMpdBo;
import com.rskytech.paramdefinemanage.dao.ICusMpdChapterDao;
import com.rskytech.pojo.CusMpdChapter;
import com.rskytech.pojo.CusMpdPs;
import com.rskytech.pojo.CusMpdSection;
import com.rskytech.pojo.TaskMpd;
import com.rskytech.util.CustomPageNumHelper;
import com.rskytech.util.StringUtil;

public class CusMpdChapterBo extends BaseBO implements ICusMpdChapterBo{
	
	private ICusMpdChapterDao  cusMpdChapterDao;
	private ICusMpdPsBo cusMpdPsBo;
	private ITaskMpdBo taskMpdBo;
	private ResourceBundle rb;
	
	
	

	public ICusMpdChapterDao getCusMpdChapterDao() {
		return cusMpdChapterDao;
	}
	public void setCusMpdChapterDao(ICusMpdChapterDao cusMpdChapterDao) {
		this.cusMpdChapterDao = cusMpdChapterDao;
	}
	
	public ResourceBundle getRb() {
		return rb;
	}
	public void setRb(ResourceBundle rb) {
		this.rb = rb;
	}
	
	public ICusMpdPsBo getCusMpdPsBo() {
		return cusMpdPsBo;
	}
	public void setCusMpdPsBo(ICusMpdPsBo cusMpdPsBo) {
		this.cusMpdPsBo = cusMpdPsBo;
	}
	public ITaskMpdBo getTaskMpdBo() {
		return taskMpdBo;
	}
	public void setTaskMpdBo(ITaskMpdBo taskMpdBo) {
		this.taskMpdBo = taskMpdBo;
	}
	
	
	
	
	@Override
	public List<CusMpdChapter> findMPDListByModelSeriesId(String modelSeriesId) throws BusinessException {
		return this.getCusMpdChapterDao().findByModelSeriesId(modelSeriesId);
	}
	
	@Override
	public boolean saveChapter(CusMpdChapter c, String flg, String userid) throws BusinessException {
		this.saveOrUpdate(c, flg, userid);
		return true;
	}
	
	@Override
	public CusMpdChapter findById(String id) throws BusinessException {
		return this.getCusMpdChapterDao().findById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteChapterById(Class clazz, String chapterId, String userId) throws BusinessException {
		this.delete(clazz, chapterId, userId);
		return true;
	}
	
	@Override
	public boolean saveSection(CusMpdSection s, String operateFlg, String userid) throws BusinessException {
		saveOrUpdate(s, operateFlg, userid);
		return true;
	}
	
	@Override
	public CusMpdSection findSectionById(String id) throws BusinessException {
		return this.getCusMpdChapterDao().findBySectionId(id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean deleteSectionById(Class clazz, String sectionId, String userId) throws BusinessException {
		this.deleteChapterById(clazz, sectionId, userId);
		return true;
	}
	
	private String getChapterName(CusMpdChapter c) {
		String retString = c.getChapterName();
		return retString == null ? "" : retString;
	}
	
	private String getChapterContent(CusMpdChapter c) {
		String retString = c.getChapterContent();
		return retString == null ? "" : StringUtil.recoveryContextPath(retString);
	}
	
	private String getSectionName(CusMpdSection s) {
		String retString =  s.getSectionName();
		return retString == null ? "" : retString;
	}
	
	private String getSectionContent(CusMpdSection s) {
		String retString = s.getSectionContent();
		return retString == null ? "" : StringUtil.recoveryContextPath(retString);
	}
	
	private String getPsName(CusMpdPs s) {
		String retString =  s.getPsName() ;
		return retString == null ? "" : retString;
	}
	
	/**
	 * 使用pd4ml输出PDF
	 * 
	 * @param content
	 *            html格式的文本内容
	 * @param savePath
	 *            文件保存路径
	 * @param isWriteFooter
	 *            是否输出页脚
	 * @param chapterName
	 *            章节名称
	 * @param chapterCode
	 *            章节号
	 */
	private void writePdfFile(String content, String savePath, boolean isWriteFooter, String chapterName, String chapterCode) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(savePath);
			StringReader strReader = new StringReader(content.toString());
			PD4ML html = new PD4ML();
			html.setPageSize(html.changePageOrientation(PD4Constants.A4));
			html.useTTF("java:resource", true);
			html.setDefaultTTFs("KaiTi_GB2312", "KaiTi_GB2312", "SIMSUN");
			html.enableDebugInfo();
			html.setPageInsets(new Insets(30, 20, 15, 30));
			html.setHtmlWidth(950);
			
			if (isWriteFooter) {
				PD4PageMark mark = new PD4PageMark();
				mark.setInitialPageNumber(1);
				mark.setPageNumberAlignment(2);
				mark.setHtmlTemplate("<html><body><table width='100%'><tr><td align='right' width='100%' style='font-size:14px;padding-right:40px'><span style='font-size:24px;'>"
						+ chapterName
						+ "</span><br /><span style='font-weight:bold'>"
						+ chapterCode
						+ "$[page]<br />"
						+ StringUtil.getNowDate()
						+ "</span></td></tr></table></body></html>");
				mark.setFontSize(15);
				mark.setAreaHeight(80);
				// System.out.println("********************************************"+mark.getPagesToSkip());
				// System.out.println("********************************************"+mark.getPageNumberAlignment());
				html.setPageFooter(mark);
			}
			
			html.enableImgSplit(false);
			html.render(strReader, os);
			// html.clearCache();
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
	}
	
	/**
	 * 章 台头
	 * 
	 * @param content
	 * @return
	 */
	public String genChapterStyle(String content) {
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("<html><head>");
		contentBuffer.append("<style type='text/css'>.chapter_title{font-size:75;text-align:center;width:980px;height:620px;vertical-align:middle;}</style>");
		contentBuffer.append("</head><body>");
		contentBuffer.append("<table>");
		contentBuffer.append("<tr>");
		contentBuffer.append("<td class='chapter_title'>");
		contentBuffer.append(content);
		contentBuffer.append("</td>");
		contentBuffer.append("</tr>");
		contentBuffer.append("</table>");
		contentBuffer.append("</body></html>");
		return contentBuffer.toString();
	}
	
	/**
	 * 章 内容
	 * 
	 * @param content
	 * @return
	 */
	public String genChapterContentStyle(String content) {
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("<html><head>");
		contentBuffer.append("<style type='text/css'>.chapter_content{width:980px;}</style>");
		contentBuffer.append("</head><body>");
		contentBuffer.append("<div class='chapter_content'>");
		contentBuffer.append(content);
		contentBuffer.append("</div>");
		contentBuffer.append("</body></html>");
		return contentBuffer.toString();
	}
	
	/**
	 * 节 内容
	 * 
	 * @param chapter
	 * @param content
	 * @return
	 */
	public String getSectionStyle(String chapterContent, String content) {
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("<html><head>");
		contentBuffer.append("<style type='text/css'>.section_title{width:980px;padding:7px 0px}.section_content{width:980px;padding:0px 50px 0px 25px}</style>");
		contentBuffer.append("</head><body>");
		contentBuffer.append("<div class='section_content'>");
		contentBuffer.append(chapterContent);
		contentBuffer.append("</div>");
		contentBuffer.append(content);
		contentBuffer.append("</body></html>");
		return contentBuffer.toString();
	}
	
	/**
	 * 目录页面样式定义
	 * 
	 * @param list
	 * @return
	 */
	public String getContentsStyle(List<MpdPdfHelper.Contents> list, List<String> sectionPageNOAll) {
		int tableRowCount = 19;
		int rowCount = tableRowCount - 2;
		int listSize = list.size();
		StringBuffer contentBuffer = new StringBuffer();
		contentBuffer.append("<html><head>");
		contentBuffer.append("<style type='text/css'>td{height:23px;font-size:15}.underline{text-decoration:underline}</style>");
		//contentBuffer.append("<script type='text/javascript'>window.onload = function(){document.getElementById('test').style.color = '#FF0000';}</script>");
		contentBuffer.append("</head><body>");
		// 目录所占页数
		int pageSize = listSize % (rowCount * 2) == 0 ? listSize / (rowCount * 2) : listSize / (rowCount * 2) + 1;
		for (int i = 0; i < pageSize; i++) {
			contentBuffer
					.append("<table style='width:960px;margin:0px 30px;'><tr><td colspan='6' style='text-align:center;font-size:25;font-weight:bold;height:50px'>"
							+ rb.getString("catalogue") + "</td></tr>");
			contentBuffer.append("<tr><td width='110px' class='underline'>" + rb.getString("chapterDay") + "</td><td width='260px' class='underline'>"
					+ rb.getString("subject") + "</td><td width='110px' class='underline'>" + rb.getString("page") + "</td><td width='110px' class='underline'>"
					+ rb.getString("chapterDay") + "</td><td width='260px' class='underline'>" + rb.getString("subject")
					+ "</td><td width='110px' class='underline'>" + rb.getString("page") + "</td></tr>");
			int nowIndex = 2 * i * rowCount;
			// 循环次数
			int loopCount = (nowIndex + rowCount) < listSize ? rowCount + nowIndex : listSize;
			for (int j = nowIndex; j < loopCount; j++) {
				MpdPdfHelper.Contents mulu = list.get(j);
				if (mulu == null)
					continue;
				if (!mulu.isChapter())
					mulu.setPageNo(sectionPageNOAll.get(j));
				contentBuffer.append("<tr>");
				writeTableCell(contentBuffer, mulu);
				int rightIndex = j + rowCount;
				if (rightIndex >= listSize)
					continue;
				mulu = list.get(rightIndex);
				if (!mulu.isChapter())
					mulu.setPageNo(sectionPageNOAll.get(rightIndex));
				writeTableCell(contentBuffer, mulu);
				contentBuffer.append("</tr>");
			}
			contentBuffer.append("</table>");
		}
		contentBuffer.append("</body></html>");
		return contentBuffer.toString();
	}
	
	/**
	 * 输出表格单元格与内容
	 * 
	 * @param contentBuffer
	 * @param mulu
	 */
	private void writeTableCell(StringBuffer contentBuffer, MpdPdfHelper.Contents mulu) {
		if (mulu.isChapter()) {
			contentBuffer.append("<td style='font-weight:bold'>");
		}
		else {
			contentBuffer.append("<td>");
		}
		contentBuffer.append(mulu.getSerireId());
		contentBuffer.append("</td>");
		if (mulu.isChapter()) {
			contentBuffer.append("<td style='font-weight:bold'>");
		}
		else {
			contentBuffer.append("<td>");
		}
		contentBuffer.append(mulu.getName());
		contentBuffer.append("</td>");
		contentBuffer.append("<td>");
		if (!mulu.isChapter()) {
			contentBuffer.append(mulu.getPageNo());
		}
		contentBuffer.append("</td>");
	}
	
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
	
	@SuppressWarnings("unchecked")
	@Override
	public String generateMpdPdf(String modelSeriesId) throws Exception {
		rb = BasicTypeUtils.getProperties("Cn", "cusMpdChapterBO");
		String dataString = StringUtil.getDataString();
		String timeString = StringUtil.getTimesString();
		String tempDirPath = ServletActionContext.getServletContext().getRealPath(ComacConstants.MPD_PS_PDF_PATH) + "/" + dataString;
		File tempDir = new File(tempDirPath);
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		tempDir = null;// 销毁无用对象
		List<CusMpdChapter> chapterList = findMPDListByModelSeriesId(modelSeriesId);
		TreeSet<CusMpdChapter> tsChapter = new TreeSet<CusMpdChapter>(new Comparator<CusMpdChapter>() {
			@Override
			public int compare(CusMpdChapter o1, CusMpdChapter o2) {
				return new BigInteger(o1.getChapterCode()).subtract(new BigInteger(o2.getChapterCode())).intValue();// 节排序
			}
		});
		tsChapter.addAll(chapterList);
		
		List<MpdPdfHelper.Contents> muluList = new ArrayList<MpdPdfHelper.Contents>();
		int chapterIndex = 0;
		List<String> pdfPathList = new ArrayList<String>();// 产生临时PDF文件路径列表
		List<String> sectionPageNo = new ArrayList<String>();
		List<String> sectionPageNoAll = new ArrayList<String>();
		for (Iterator<CusMpdChapter> iterator = tsChapter.iterator(); iterator.hasNext();) {
			CusMpdChapter chapter = iterator.next();
			sectionPageNoAll.add("");// 用于索引占位
			String chapterPdfPath = tempDirPath + "/chapter_" + (++chapterIndex) + ".pdf";
			String chapterName = getChapterName(chapter);
			if (!chapterName.equals("")) {
				// 输出章台头
				pdfPathList.add(chapterPdfPath);
				writePdfFile(genChapterStyle(chapterName), chapterPdfPath, false, "", "");
				muluList.add(new MpdPdfHelper.Contents(rb.getString("di") + chapter.getChapterCode() + rb.getString("chapter"), chapterName, "", true));
			}
			TreeSet<CusMpdSection> tsSections = new TreeSet<CusMpdSection>(new Comparator<CusMpdSection>() {
				@Override
				public int compare(CusMpdSection o1, CusMpdSection o2) {
					return new BigInteger(o1.getSectionCode()).subtract(new BigInteger(o2.getSectionCode())).intValue();// 节排序
				}
			});
			tsSections.addAll(chapter.getCusMpdSections());
			StringBuffer sectionBuffer = new StringBuffer();
			boolean isSectionContentEmpty = false;
			for (Iterator<CusMpdSection> iterator2 = tsSections.iterator(); iterator2.hasNext();) {
				isSectionContentEmpty = true;
				CusMpdSection section = iterator2.next();
				String sectionName = getSectionName(section);
				String sectionCodeName = section.getSectionCode() + " " + sectionName;
				sectionBuffer.append("<div class='section_title'>" + sectionCodeName + "</div>");
				sectionPageNo.add(sectionCodeName);
				muluList.add(new MpdPdfHelper.Contents(chapter.getChapterCode() + "." + section.getSectionCode(), sectionName, "", false));
				
				String sectionContent = getSectionContent(section);
				if (!sectionContent.equals("")) {
					sectionBuffer.append("<div class='section_content'>" + sectionContent + "</div>");
				}
			}
			tsSections.clear();
			tsSections = null;
			// 输出节
			String sectionPdfPath = tempDirPath + "/section_" + (chapterIndex) + ".pdf";
			String chapterContent = getChapterContent(chapter);
			int startPageNum = 0;
			if (!chapterContent.equals("") || isSectionContentEmpty) {
				pdfPathList.add(sectionPdfPath);
				writePdfFile(getSectionStyle(chapterContent, sectionBuffer.toString()), sectionPdfPath, true, chapterName, chapter.getChapterCode() + "-");
				PdfReader reader = new PdfReader(sectionPdfPath);
				startPageNum = reader.getNumberOfPages();
			}
			for (String pageNo : sectionPageNo) {
				sectionPageNoAll.add(chapter.getChapterCode() + "-" + searchText(sectionPdfPath, pageNo));
			}
			sectionPageNo.clear();
			// ====================
			
			Map<TaskMpd, String> map = this.getTaskMpdBo().findTaskMpdList(modelSeriesId, chapter.getChapterFlg());
			if (map.size() >= 1) {
				Set<Map.Entry<TaskMpd, String>> set = map.entrySet();
				Document document = new Document(new Rectangle(842, 595), 60, 60, 60, 60);// 左右上下
				try {
					BaseFont bfChinese = BaseFont.createFont("resource/SIMKAI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);// 设置中文字体
					Font headFont = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小
					Font normalFont = new Font(bfChinese, 10, Font.NORMAL);
					String taskMpdPath = tempDirPath + "/taskmpd_" + chapterIndex + ".pdf";
					pdfPathList.add(taskMpdPath);
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(taskMpdPath));
					writer.setPageEvent(new CustomPageNumHelper(chapterName, chapter.getChapterCode(), startPageNum));
					document.open();
					PdfPTable table = new PdfPTable(MpdPdfHelper.getColumnWidthPercent(chapter.getChapterFlg()));
					table.setWidthPercentage(100);
					table.setHeaderRows(1);
					table.setSplitLate(false);
					table.setSplitRows(true);
					int[] columnIndexs = MpdPdfHelper.getColumnIndex(chapter.getChapterFlg());
					String[] column_names = MpdPdfHelper.COLUMN_NAME_CN;
					String[] column_fields = MpdPdfHelper.COLUMN_FIELD_NAME_CN;
					PdfPCell cell = null;
					for (int i = 0; i < columnIndexs.length; i++) {
						cell = new PdfPCell(new Paragraph(column_names[columnIndexs[i]], headFont));
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);
					}
					// 遍历一次为一个任务记录
					for (Iterator<Map.Entry<TaskMpd, String>> iterator3 = set.iterator(); iterator3.hasNext();) {
						Map.Entry<TaskMpd, String> entry = iterator3.next();
						TaskMpd taskMpd = entry.getKey();
						String areaCode = entry.getValue();
						for (int i = 0; i < columnIndexs.length; i++) {
							String columnFiled = column_fields[columnIndexs[i]];
							try {
								if (columnFiled.equals("ownArea")) {
									cell = new PdfPCell(new Paragraph(areaCode, normalFont));
								}
								else {
									Method method = TaskMpd.class.getMethod("get" + columnFiled.substring(0, 1).toUpperCase() + columnFiled.substring(1));
									Object object = method.invoke(taskMpd);
									String valueString = method == null ? "" : (object == null ? "" : object.toString());
									cell = new PdfPCell(new Paragraph(("null").equals(valueString) ? "" : valueString, normalFont));
								}
							}
							catch (Exception e) {
								cell = new PdfPCell(new Paragraph("", normalFont));
							}
							finally {
								cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setPadding(5f);
								table.addCell(cell);
							}
						}
					}
					document.add(table);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					document.close();
				}
			}
			// ====================
		}
		// 目录处理
		String contentsPath = tempDirPath + "/contents.pdf";
		writePdfFile(getContentsStyle(muluList, sectionPageNoAll), contentsPath, true, rb.getString("catalogue"), "");
		pdfPathList.add(0, contentsPath);
		// 附录处理
		HttpServletRequest request = ServletActionContext.getRequest();
		
		List<String> deletePdfList = new ArrayList<String>();
		deletePdfList.addAll(pdfPathList);
		
		List<CusMpdPs> cusMpdPs = cusMpdPsBo.findByModelSeriesId(modelSeriesId);
		for (CusMpdPs ps : cusMpdPs) {
			String psFilePath = ps.getPsUrl();
			if (!(psFilePath == null || ("").equals(psFilePath))) {
				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + psFilePath;
				String realFilePath = ServletActionContext.getServletContext().getRealPath(url.substring(url.lastIndexOf(ComacConstants.MPD_PS_SAVE_PATH)));
				
				if (ps.getPsFlg() == 0) {
					pdfPathList.add(0, realFilePath);
				}
				else {
					pdfPathList.add(realFilePath);
				}
				muluList.add(new MpdPdfHelper.Contents(ps.getPsSort().toString(), getPsName(ps), "", true));
			}
		}
		
		MpdPdfHelper.mergePdfFiles(pdfPathList, tempDirPath + "/mpd" + timeString + ".pdf");
		MpdPdfHelper.deleteTempPdfFile(deletePdfList);
		
		String path = request.getContextPath();
		return path + ComacConstants.MPD_PS_PDF_PATH + dataString + "/mpd" + timeString + ".pdf";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkChapterCode(String chapterId, String chapterCode, String msId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMpdChapter.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (chapterId != null && !"".equals(chapterId)) {
			dc.add(Restrictions.ne("chapterId", chapterId));
		}
		dc.add(Restrictions.eq("chapterCode", chapterCode));
		List<CusMpdChapter> cusMpdChapters = this.findByCritera(dc);
		if (cusMpdChapters.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkSectionCode(String sectionId, String chapterId, String sectionCode) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMpdSection.class);
		dc.add(Restrictions.eq("cusMpdChapter.chapterId", chapterId));
		if (sectionId != null && !"".equals(sectionId)) {
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
	public boolean checkMrbPsCode(String msId, String mrbPsId, Integer mrbPsCode) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMpdPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (mrbPsId != null && !("").equals(mrbPsId)) {
			dc.add(Restrictions.ne("psId", mrbPsId));
		}
		dc.add(Restrictions.eq("psSort", mrbPsCode));
		List<CusMpdPs> cusMpdPs = this.findByCritera(dc);
		if (cusMpdPs.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkChapterFlg(String chapterId, String chapterFlg, String msId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusMpdChapter.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (chapterId != null && !("").equals(chapterId)) {
			dc.add(Restrictions.ne("chapterId", chapterId));
		}
		dc.add(Restrictions.eq("chapterFlg", chapterFlg));
		List<CusMpdChapter> cusMpdChapters = this.findByCritera(dc);
		if (cusMpdChapters.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	
	
	
	
}
