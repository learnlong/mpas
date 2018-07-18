package com.rskytech.paramdefinemanage.bo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.rskytech.ComacConstants;

public class MpdPdfHelper {

	/**
	 * MRB任务表头
	 */
	/**开始**/
	//第二章
	public static final String[] MRBSYSTASKHEADCN = {"MRB任务号","故障影响类别","任务类型","间隔","区域","接近方式","适用性","任务描述 ","任务来源"};
	public static final String[] MRBSYSTASKHEADEN = {"MRB ITEM NUMBER ","FEC","TASK TYPE","INTERVAL","ZONE","ACCESS","APPLICABILITY","TASK DESCRIPTIO ","SOURCES"};
	//第三章
	public static final String[] MRBSTRUCTTASKHEADCN = {"MRB任务号","任务类型","检查间隔","区域","接近方式","适用性","任务描述 ","任务来源","门槛值","间隔"};
	public static final String[] MRBSTRUCTTASKHEADEN = {"MRB ITEM NUMBER ","TASK TYPE","INTERVAL","ZONE","ACCESS","APPLICABILITY","TASK DESCRIPTIO ","SOURCES","THRESHOLD","INTERVAL"};
	//第四章
	public static final String[] MRBAREATASKHEADCN = {"MRB任务号","间隔","区域","接近方式","适用性","任务描述","任务来源"};
	public static final String[] MRBAREATASKHEADEN = {"MRB ITEM NUMBER ","INTERVAL","ZONE","ACCESS","APPLICABILITY","TASK DESCRIPTIO ","SOURCES"};
	/**结束**/
	
	public static final String[] COLUMN_NAME_CN = { "MPD项目号", "CAT", "任务\n类型", "区域", "工作通道", "任务描述", "周期", "任务参考", "人力-工时", "适用性" };
	public static final String[] COLUMN_NAME_EN = { "MPD PROJECT NUMBER", "CAT", "TASK\nTYPE", "ZONE", "WORKING CHANNEL", "TASK DESCRIPTIO ", "CYCLE", "TASK REFERENCE", "WORK TIME", "PRACTICABILITY" };
	public static final String[] COLUMN_FIELD_NAME_CN = { "mpdCode", "failureCauseType", "taskType", "ownArea", "reachWayCn", "taskDescCn", "taskIntervalOriginal",
			"amm", "workTime", "effectiveness" };
	public static final String[] COLUMN_FIELD_NAME_EN = { "mpdCode", "failureCauseType", "taskType", "ownArea", "reachWayEn", "taskDescEn", "taskIntervalOriginal",
			"amm", "workTime", "effectiveness" };
	
	/**
	 * 获取不同系统下 列索引 引用本类中（COLUMN_NAME_CN，COLUMN_NAME_EN，COLUMN_FIELD_NAME_CN，
	 * COLUMN_FIELD_NAME_EN）四个常量
	 * 
	 * @param sourceSystem
	 * @return
	 */
	public static int[] getColumnIndex(String sourceSystem) {
		if (ComacConstants.SYSTEM_CODE.equals(sourceSystem)) {
			return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		}
		else if (ComacConstants.ZONAL_CODE.equals(sourceSystem)) {
			return new int[] { 0, 3, 4, 5, 6, 7, 8, 9 };
		}
		else if (ComacConstants.LHIRF_CODE.equals(sourceSystem)) {
			return new int[] { 0, 2, 3, 4, 5, 6, 7, 8, 9 };
		}
		else if (ComacConstants.STRUCTURE_CODE.equals(sourceSystem)) {
			return new int[] { 0, 3, 4, 5, 6, 7, 8, 9 };
		}
		return null;
	}
	
	/**
	 * 获取不同系统下 列宽百分比，引用本类中COLUMN_NAME_CN
	 * 
	 * @param sourceSystem
	 * @return
	 */
	public static float[] getColumnWidthPercent(String sourceSystem) {
		if (ComacConstants.SYSTEM_CODE.equals(sourceSystem)) {
			return new float[] { 3f, 1f, 1f, 1f, 2f, 7f, 2f, 3f, 1f, 2f };
		}
		else if (ComacConstants.ZONAL_CODE.equals(sourceSystem)) {
			return new float[] { 3f, 3f, 3f, 7f, 2f, 3f, 1f, 2f };
		}
		else if (ComacConstants.LHIRF_CODE.equals(sourceSystem)) {
			return new float[] { 3f, 1f, 1f, 2f, 7f, 2f, 3f, 1f, 2f };
		}
		else if (ComacConstants.STRUCTURE_CODE.equals(sourceSystem)) {
			return new float[] { 3f, 1f, 3f, 7f, 2f, 3f, 1f, 2f };
		}
		return null;
	}
	
	/**
	 * 得到生成的PDF文件（多个合并）
	 * 
	 * @param savepath
	 *            文件保存的路径
	 * @param rName
	 *            报表名称
	 * @param files
	 *            生成的文件
	 * @return String
	 */
	public String getPDF(String savepath, String rName, String[] files) {
		String getResultName = null;
		savepath = savepath + rName + ".pdf";
		// 如果是多个PDF文件进行合并
		if (files.length > 1) {
			// 多个PDF文件合并
			mergePdfFiles(files, savepath);
			getResultName = savepath.subSequence(savepath.lastIndexOf("/"), savepath.length()).toString();
		}
		else {
			if (reNameFile(files, savepath)) {
				getResultName = rName + ".pdf";
			}
		}
		return getResultName;
	}
	
	/**
	 * 多个PDF合并功能
	 * 
	 * @param files
	 *            多个PDF的路径
	 * @param savepath
	 *            生成的新PDF路径
	 * @return boolean boolean
	 */
	public static boolean mergePdfFiles(String[] files, String savepath) {
		try {
			Document document = new Document(new PdfReader(files[0]).getPageSize(1));
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
			document.open();
			PdfReader reader = null;
			for (int i = 0; i < files.length; i++) {
				reader = new PdfReader(files[i]);
				if (reader.getFileLength() <= 0)
					continue;
				int n = reader.getNumberOfPages();
				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}
			document.close();
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		catch (DocumentException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 多个PDF合并功能
	 * 
	 * @param files
	 *            多个PDF的路径
	 * @param savepath
	 *            生成的新PDF路径
	 * @return boolean boolean
	 */
	public static boolean mergePdfFiles(List<String> files, String savepath) {
		File file = null;
		List<String> newList = new ArrayList<String>();
		for (String filePath : files) {
			if (filePath.indexOf("[notdelete]") != -1) {
				filePath = filePath.substring(0, filePath.indexOf("[notdelete]"));
			}
			file = new File(filePath);
			if (file.exists()) {
				PdfReader reader = null;
				try {
					reader = new PdfReader(filePath);
				} catch (IOException e) {
					
				}
				//判断生成的PDF文件是否损坏
				if(reader!=null && reader.getNumberOfPages()>0){
					newList.add(filePath);
				}
			}
		}
		return mergePdfFiles(newList.toArray(new String[] {}), savepath);
	}
	
	/**
	 * 文件重命名
	 */
	public static boolean reNameFile(String[] oldName, String newName) {
		File oldfile = null;
		if (oldName != null) {
			for (int i = 0; i < oldName.length; i++) {
				oldfile = new File(oldName[i]);
			}
			if (oldfile == null) {
				return false;
			}
			File newfile = new File(newName);
			if (oldfile.renameTo(newfile)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 批量删除临时pdf文件
	 * 
	 * @param files
	 * @return
	 */
	public static boolean deleteTempPdfFile(List<String> files) {
		try {
			File pdfFile = null;
			for (String file : files) {
				pdfFile = new File(file);
				if (pdfFile != null && pdfFile.exists() && pdfFile.isFile() && file.indexOf("[notdelete]") == -1) {
					pdfFile.delete();
				}
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public static class Contents {
		private String seriesId;
		private String name;
		private String pageNo;
		private boolean isChapter;
		
		public Contents() {
			
		}
		
		public Contents(String seriesId, String name, String pageNo, boolean isChapter) {
			this.seriesId = seriesId;
			this.name = name;
			this.pageNo = pageNo;
			this.isChapter = isChapter;
		}
		
		public String getSerireId() {
			return seriesId;
		}
		
		public void setSerireId(String serireId) {
			this.seriesId = serireId;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getPageNo() {
			return pageNo;
		}
		
		public void setPageNo(String pageNo) {
			this.pageNo = pageNo;
		}
		
		public boolean isChapter() {
			return isChapter;
		}
		
		public void setChapter(boolean isChapter) {
			this.isChapter = isChapter;
		}
		
	}	
}
