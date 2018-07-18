package com.rskytech.paramdefinemanage.action;

import java.io.File;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;

import com.rskytech.paramdefinemanage.bo.ICusMpdChapterBo;
import com.rskytech.paramdefinemanage.bo.ICusMpdPsBo;
import com.rskytech.basedata.bo.IFileUploadBo;
import com.rskytech.paramdefinemanage.bo.ITaskMpdVersionBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusMpdChapter;
import com.rskytech.pojo.CusMpdPs;
import com.rskytech.pojo.CusMpdSection;
import com.rskytech.pojo.TaskMpdVersion;
import com.rskytech.util.StringUtil;

public class MpdReportAction extends BaseAction{


	private static final long serialVersionUID = 5018952455187315851L;
	
	private IFileUploadBo fileUploadBo;
	private ICusMpdPsBo cusMpdPsBo;
	private ICusMpdChapterBo cusMpdChapterBo;
	private ITaskMpdVersionBo taskMpdVersionBo;
	
	
	private Integer modelSeries;
	
	private String chapterId;
	private String chapterCode;
	private String chapterNameCn;

	private String chapterContentCn;

	private String chapterFlg;
	
	private String sectionId;
	private String sectionCode;
	private String sectionNameCn;

	private String sectionContentCn;

	
	private File pdfFile;
	private String pdfFileFileName;
	private String pdfFileContentType;
	
	private String psId;
	private String psNameCn;

	private Integer psFlg;
	private Integer psSort;
	
	private String chapter_chapterId;
	private String section_chapterId;
	private String section_sectionId;
	
	/**
	 * 初始化加载树
	 * 
	 * @return
	 */
	public String loadTree() {
		
		List<CusMpdChapter> list = cusMpdChapterBo.findMPDListByModelSeriesId(this.getComModelSeries().getModelSeriesId());
		TreeSet<CusMpdChapter> chapterts = new TreeSet<CusMpdChapter>(new Comparator<CusMpdChapter>() {
			@Override
			public int compare(CusMpdChapter o1, CusMpdChapter o2) {
				return new BigInteger(o1.getChapterCode()).subtract(new BigInteger(o2.getChapterCode())).intValue();
			}
		});
		chapterts.addAll(list);
		JSONArray arrChapter = new JSONArray();
		for (Iterator<CusMpdChapter> iteratorChpater = chapterts.iterator(); iteratorChpater.hasNext();) {
			CusMpdChapter chapter = iteratorChpater.next();
			JSONObject objChapter = new JSONObject();
			objChapter.put("text", chapter.getChapterName());
			objChapter.put("id", "c" + chapter.getChapterId());
			objChapter.put("leaf", false);
			objChapter.put("expanded", false);
			objChapter.put("sort", chapter.getChapterCode());
			JSONArray arrSection = new JSONArray();
			TreeSet<CusMpdSection> ts = new TreeSet<CusMpdSection>(new Comparator<CusMpdSection>() {
				@Override
				public int compare(CusMpdSection o1, CusMpdSection o2) {
					return new BigInteger(o1.getSectionCode()).subtract(new BigInteger(o2.getSectionCode())).intValue();
				}
			});
			ts.addAll(chapter.getCusMpdSections());
			for (Iterator<CusMpdSection> iterator2 = ts.iterator(); iterator2.hasNext();) {
				CusMpdSection section = iterator2.next();
				JSONObject objSection = new JSONObject();
				objSection.put("text", section.getSectionName());
				objSection.put("id", "s" + section.getSectionId());
				objSection.put("leaf", true);
				objSection.put("expanded", false);
				objSection.put("sort", section.getSectionCode());
				arrSection.add(objSection);
			}
			ts.clear();
			ts = null;
			objChapter.put("children", arrSection);
			arrChapter.add(objChapter);
		}
		writeToResponse(arrChapter.toString());
		return null;
	}
	
	/**
	 * 加载MPD附录生成树
	 * 
	 * @return
	 */
	public String loadMpdPsTree() {
		
		JSONArray arrPs = new JSONArray();
		List<CusMpdPs> list = cusMpdPsBo.findByModelSeriesId(this.getComModelSeries().getModelSeriesId());
		for (CusMpdPs ps : list) {
			JSONObject objPs = new JSONObject();
			objPs.put("text", ps.getPsName() );
			objPs.put("id", "ps" + ps.getPsId());
			objPs.put("leaf", true);
			objPs.put("expanded", true);
			objPs.put("sort", ps.getPsSort());
			arrPs.add(objPs);
		}
		writeToResponse(arrPs.toString());
		return null;
	}
	
	/**
	 * 保存或更新章
	 * 
	 * @return
	 */
	public String saveChapter() {
		boolean isUpdate = !("").equals(this.getChapter_chapterId()) && this.getChapter_chapterId() != null;
		CusMpdChapter chapter = isUpdate ? this.getCusMpdChapterBo().findById(this.getChapter_chapterId()) : new CusMpdChapter();
		chapter.setChapterFlg(this.getChapterFlg());
		chapter.setChapterCode(this.getChapterCode());
		chapter.setChapterName(this.getChapterNameCn());
	
		chapter.setComModelSeries(this.getComModelSeries());
		boolean result = false;
		if (isUpdate) {
			chapter.setChapterId(this.getChapter_chapterId());
			chapter.setModifyDate(new Date());
			chapter.setModifyUser(this.getSysUser().getUserId());
			result = this.getCusMpdChapterBo().saveChapter(chapter, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		}
		else {
			chapter.setCreateUser(this.getSysUser().getUserId());
			chapter.setCreateDate(new Date());
			result = this.getCusMpdChapterBo().saveChapter(chapter, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
		}
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 更新章内容
	 * 
	 * @return
	 */
	public String updateChapter() {
		CusMpdChapter chapter = this.getCusMpdChapterBo().findById(this.getChapterId());
		chapter.setModifyDate(new Date());
		chapter.setModifyUser(this.getSysUser().getUserId());
		// String language = this.getSysUser().getLanguage().trim();
		// if (language.equals("Cn")) {
		chapter.setChapterContent(this.getChapterContentCn());
		// lse if (language.equals("En")) {
		
		// }
		boolean result = this.getCusMpdChapterBo().saveChapter(chapter, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 加载章内容
	 * 
	 * @return
	 */
	public String loadChapter() {
		// String language = this.getSysUser().getLanguage().trim();
		CusMpdChapter chapter = this.getCusMpdChapterBo().findById(this.getChapterId());
		JSONObject object = new JSONObject();
		object.put("chapterId", chapter.getChapterId());
		object.put("nameCn", chapter.getChapterName());
	
		object.put("code", chapter.getChapterCode());
		object.put("chapterFlg", chapter.getChapterFlg());
		// object.put("content", language.equals("Cn") ?
		// chapter.getChapterContentCn() : chapter.getChapterContentEn());
		object.put("contentCn", StringUtil.recoveryContextPath(chapter.getChapterContent()));
	
		writeToResponse(object.toString());
		return null;
	}
	
	/**
	 * 加载节内容
	 * 
	 * @return
	 */
	public String loadSection() {
		// String language = this.getSysUser().getLanguage().trim();
		CusMpdSection section = this.getCusMpdChapterBo().findSectionById(this.getSectionId());
		JSONObject object = new JSONObject();
		object.put("nameCn", section.getSectionName());
		
		object.put("code", section.getSectionCode());
		// object.put("content", language.equals("Cn") ?
		// section.getSectionContentCn() : section.getSectionContentEn());
		object.put("contentCn", StringUtil.recoveryContextPath(section.getSectionContent()));
		
		writeToResponse(object.toString());
		return null;
	}
	
	/**
	 * 删除章
	 * 
	 * @return
	 */
	public String deleteChapter() {
		boolean result = this.getCusMpdChapterBo().deleteChapterById(CusMpdChapter.class, this.getChapterId(), this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 删除节
	 * 
	 * @return
	 */
	public String deleteSection() {
		boolean result = this.getCusMpdChapterBo().deleteSectionById(CusMpdSection.class, this.getSectionId(), this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 更新或保存节
	 * 
	 * @return
	 */
	public String saveSection() {
		boolean isUpdate = !("").equals(this.getSection_sectionId()) && this.getSection_sectionId() != null;
		CusMpdChapter chapter = this.getCusMpdChapterBo().findById(this.getSection_chapterId());
		CusMpdSection section = isUpdate ? this.getCusMpdChapterBo().findSectionById(this.getSection_sectionId()) : new CusMpdSection();
		section.setCusMpdChapter(chapter);
		section.setSectionCode(this.getSectionCode());
		section.setSectionName(this.getSectionNameCn());
		
		//section.setComModelSeries(this.getComModelSeries());
		boolean result = false;
		if (isUpdate) {
			section.setSectionId(this.getSection_sectionId());
			section.setModifyDate(new Date());
			section.setModifyUser(this.getSysUser().getUserId());
			System.out.println(cusMpdChapterBo);
			System.out.println(this.getCusMpdChapterBo());
			result = this.getCusMpdChapterBo().saveSection(section, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		}
		else {
			section.setCreateUser(this.getSysUser().getUserId());
			section.setCreateDate(new Date());
			result = this.getCusMpdChapterBo().saveSection(section, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
		}
		
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 更新MPD章，节内容
	 * 
	 * @return
	 */
	public String updateSection() {
		
		CusMpdSection section = this.getCusMpdChapterBo().findSectionById(this.getSectionId());
		section.setModifyDate(new Date());
		section.setModifyUser(this.getSysUser().getUserId());
		// if (language.equals("Cn")) {
		section.setSectionContent(this.getSectionContentCn());
		// }
		// else if (language.equals("En")) {
		
		// }
		boolean result = this.getCusMpdChapterBo().saveSection(section, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 上传MPD附录，保存地址到附录表
	 * 
	 * @return
	 */
	public String saveMpdPs() {
		JSONObject object = new JSONObject();
		boolean isValid = true;
		if (pdfFile != null && !"application/pdf".equals(pdfFileContentType)) {
			object.put("message", "上传失败！您上传的文件非PDF格式!");
			isValid = false;
		}
		boolean isPsFlgUnique = this.getPsFlg() == 0 && this.getCusMpdPsBo().isPsFlgUnique(this.getComModelSeries().getModelSeriesId(), this.getPsId());
		if (isPsFlgUnique) {
			Object message = object.get("message") == null ? "" : object.get("message");
			object.put("message", message + "已存在类型为\"报表首页\"的记录！");
			isValid = false;
		}
		if (!isValid) {
			object.put("success", false);
			writeToResponse(object.toString());
			return null;
		}
		try {
			String url = null;
			if (pdfFile != null) {
				HttpServletRequest request = ServletActionContext.getRequest();
				String path = request.getContextPath();
				ServletContext context = ServletActionContext.getServletContext();
				String uploadPath = context.getRealPath(ComacConstants.MPD_PS_SAVE_PATH);
				String fileName = StringUtil.getRandomString() + pdfFileFileName.substring(pdfFileFileName.lastIndexOf("."));
				this.getFileUploadBo().saveFile(pdfFile, uploadPath, fileName);
				url = path + ComacConstants.MPD_PS_SAVE_PATH + fileName;
			}
			boolean isUpdate = this.getPsId() != null && !"".equals(this.getPsId());
			CusMpdPs ps = isUpdate ? this.getCusMpdPsBo().findById(this.getPsId()) : new CusMpdPs();
			ps.setComModelSeries(this.getComModelSeries());
			boolean result = false;
			ps.setPsName(this.getPsNameCn());
			
			ps.setPsSort(this.getPsSort());
			ps.setPsFlg(this.getPsFlg());
			if (url != null) {
				this.getCusMpdPsBo().deletePsFile(ps.getPsUrl());// 更新状态时 删除旧文件
				ps.setPsUrl(url);
			}
			if (isUpdate) {
				ps.setPsId(this.getPsId());
				ps.setModifyDate(new Date());
				ps.setModifyUser(this.getSysUser().getUserId());
				result = this.getCusMpdPsBo().saveMpdPs(ps, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
			}
			else {
				ps.setCreateUser(this.getSysUser().getUserId());
				ps.setCreateDate(new Date());
				result = this.getCusMpdPsBo().saveMpdPs(ps, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
			}
			object.put("success", result);
			object.put("url", url);
			writeToResponse(object.toString());
		}
		catch (Exception e) {
			object.put("success", false);
			object.put("message", "上传失败！");
			writeToResponse(object.toString());
		}
		return null;
	}
	
	/**
	 * 加载MPD附录用于修改附录基础信息
	 * 
	 * @return
	 */
	public String loadMpdPs() {
		CusMpdPs ps = this.getCusMpdPsBo().findById(this.getPsId());
		if (ps != null) {
			JSONObject object = new JSONObject();
			object.put("psSort", ps.getPsSort());
			object.put("psNameCn", ps.getPsName());
			
			object.put("psFlg", ps.getPsFlg());
			object.put("psId", ps.getPsId());
			writeToResponse(object.toString());
		}
		return null;
	}
	
	/**
	 * 删除MPD附录
	 * 
	 * @return
	 */
	public String deleteMpdPs() {
		boolean result = this.getCusMpdPsBo().deleteMpdPsById(CusMpdPs.class, this.getPsId(), this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 生成MPD报表，保存报表路径到TAST_MPD_VERSIN表
	 * 
	 * @return
	 */
	public String exportPdf() {
		JSONObject object = new JSONObject();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
			String mpdPath = this.cusMpdChapterBo.generateMpdPdf(this.getComModelSeries().getModelSeriesId());
			TaskMpdVersion taskMpdVersion = new TaskMpdVersion();
			taskMpdVersion.setComModelSeries(this.getComModelSeries());
			taskMpdVersion.setCreateDate(new Date());
			taskMpdVersion.setCreateUser(this.getSysUser().getUserId());
			taskMpdVersion.setReportUrl(basePath + mpdPath);
			taskMpdVersion.setValidFlag(ComacConstants.YES);
			taskMpdVersion.setVersionType(ComacConstants.TASK_MPD_VERSION_TYPE_MPD);
			taskMpdVersion.setVersionNo(this.taskMpdVersionBo.getMaxVersionCode(this.getComModelSeries().getModelSeriesId(),
					ComacConstants.TASK_MPD_VERSION_TYPE_MPD, ComacConstants.YES) + "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		writeToResponse(object.toString());
		return null;
	}
	
	public Integer getModelSeries() {
		return modelSeries;
	}
	
	public void setModelSeries(Integer modelSeries) {
		this.modelSeries = modelSeries;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String init() {
		ComUser thisUser = getSysUser();
		if (thisUser == null) {
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	/**
	 * 获取章类型选择
	 * 
	 * @return
	 */
	public String loadChapterFlg() {
		StringBuffer sb = new StringBuffer();
		
			sb.append("{\"chapter_flg\":[{\"key\":\"无\",\"val\":\"null\"}" + ",{\"key\":\"" + ComacConstants.SYS_CN + "\",\"val\":\""
					+ ComacConstants.SYSTEM_CODE + "\"},{\"key\":\"" + ComacConstants.AREA_CN + "\",\"val\":\"" + ComacConstants.ZONAL_CODE
					+ "\"},{\"key\":\"" + ComacConstants.STRUCTURE_CN + "\",\"val\":\"" + ComacConstants.STRUCTURE_CODE + "\"},{\"key\":\""
					+ ComacConstants.LHIRF_CN + "\",\"val\":\"" + ComacConstants.LHIRF_CODE + "\"}]}");
		
		writeToResponse(sb.toString());
		return null;
	}
	
	/**
	 * 检查章的Code是否存在
	 * 
	 * @return
	 */
	public String checkChapterCode() {
		JSONObject jsonObject = new JSONObject();
		boolean flag = cusMpdChapterBo.checkChapterCode(this.chapterId, this.chapterCode, this.getComModelSeries().getModelSeriesId());
		if (!flag) {
			jsonObject.put("message", "章的编号已存在");
			jsonObject.put("success", false);
			writeToResponse(jsonObject.toString());
			return null;
		}
		else {
			// 验证"章节内容区分"是否存在
			if (this.chapterFlg != null && !"".equals(this.chapterFlg) && !("null").equals(this.chapterFlg)) {
				boolean f = cusMpdChapterBo.checkChapterFlg(this.chapterId, this.chapterFlg, this.getComModelSeries().getModelSeriesId());
				if (!f) {
					jsonObject.put("message", "所选择的章节内容区分已存在");
					jsonObject.put("success", f);
					writeToResponse(jsonObject.toString());
					return null;
				}
				else {
					jsonObject.put("success", true);
					writeToResponse(jsonObject.toString());
					return null;
				}
			}
		}
		jsonObject.put("success", true);
		writeToResponse(jsonObject.toString());
		return null;
	}
	
	/**
	 * 检查同一个章下的节Code是否存在
	 * 
	 * @return
	 */
	public String checkSectionCode() {
		boolean flag = cusMpdChapterBo.checkSectionCode(this.sectionId, this.chapterId, this.sectionCode);
		if (!flag) {
			writeToResponse("false");
			return null;
		}
		else {
			writeToResponse("true");
			return null;
		}
	}
	
	private String mrbPsId;
	private Integer mrbPsCode;
	
	/**
	 * 判断同意机型下的附录的PsSort
	 * 
	 * @return
	 */
	public String checkMrbPsCode() {
		boolean flag = cusMpdChapterBo.checkMrbPsCode(this.getComModelSeries().getModelSeriesId(), this.mrbPsId, this.mrbPsCode);
		if (!flag) {
			writeToResponse("false");
			return null;
		}
		else {
			writeToResponse("true");
			return null;
		}
	}
	
	
	
	public String getChapterId() {
		return chapterId;
	}
	
	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	
	public String getChapterCode() {
		return chapterCode;
	}
	
	public void setChapterCode(String chapterCode) {
		this.chapterCode = chapterCode;
	}
	
	public String getChapterNameCn() {
		return chapterNameCn;
	}
	
	public void setChapterNameCn(String chapterNameCn) {
		this.chapterNameCn = chapterNameCn;
	}
	
	
	public String getChapterFlg() {
		return chapterFlg;
	}
	
	public void setChapterFlg(String chapterFlg) {
		this.chapterFlg = chapterFlg;
	}
	
	public String getSectionId() {
		return sectionId;
	}
	
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	
	public String getSectionCode() {
		return sectionCode;
	}
	
	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	
	public String getSectionNameCn() {
		return sectionNameCn;
	}
	
	public void setSectionNameCn(String sectionNameCn) {
		this.sectionNameCn = sectionNameCn;
	}
	
	
	public String getChapterContentCn() {
		return chapterContentCn;
	}
	
	//报表最大宽和高
	public static final int WIDTH = 910;
	public static final int HEIGHT = 520;
	
	public void setChapterContentCn(String chapterContentCn) {
		this.chapterContentCn = StringUtil.replaceContextPath(chapterContentCn, WIDTH, HEIGHT);
	}
	
	public String getSectionContentCn() {
		return sectionContentCn;
	}
	
	public void setSectionContentCn(String sectionContentCn) {
		this.sectionContentCn = StringUtil.replaceContextPath(sectionContentCn, WIDTH, HEIGHT);
	}
	

	
	public ICusMpdPsBo getCusMpdPsBo() {
		return cusMpdPsBo;
	}
	
	public void setCusMpdPsBo(ICusMpdPsBo cusMpdPsBo) {
		this.cusMpdPsBo = cusMpdPsBo;
	}
	
	public File getPdfFile() {
		return pdfFile;
	}
	
	public void setPdfFile(File pdfFile) {
		this.pdfFile = pdfFile;
	}
	
	public String getPdfFileFileName() {
		return pdfFileFileName;
	}
	
	public void setPdfFileFileName(String pdfFileFileName) {
		this.pdfFileFileName = pdfFileFileName;
	}
	
	public String getPdfFileContentType() {
		return pdfFileContentType;
	}
	
	public void setPdfFileContentType(String pdfFileContentType) {
		this.pdfFileContentType = pdfFileContentType;
	}
	
	public IFileUploadBo getFileUploadBo() {
		return fileUploadBo;
	}
	
	public void setFileUploadBo(IFileUploadBo fileUploadBo) {
		this.fileUploadBo = fileUploadBo;
	}
	
	public String getPsId() {
		return psId;
	}
	
	public void setPsId(String psId) {
		this.psId = psId;
	}
	
	public String getPsNameCn() {
		return psNameCn;
	}
	
	public void setPsNameCn(String psNameCn) {
		this.psNameCn = psNameCn;
	}
	
	
	
	public Integer getPsFlg() {
		return psFlg;
	}
	
	public void setPsFlg(Integer psFlg) {
		this.psFlg = psFlg;
	}
	
	public Integer getPsSort() {
		return psSort;
	}
	
	public void setPsSort(Integer psSort) {
		this.psSort = psSort;
	}
	
	
	
	public String getChapter_chapterId() {
		return chapter_chapterId;
	}
	
	public void setChapter_chapterId(String chapter_chapterId) {
		this.chapter_chapterId = chapter_chapterId;
	}
	
	public String getSection_chapterId() {
		return section_chapterId;
	}
	
	public void setSection_chapterId(String section_chapterId) {
		this.section_chapterId = section_chapterId;
	}
	
	public String getSection_sectionId() {
		return section_sectionId;
	}
	
	public void setSection_sectionId(String section_sectionId) {
		this.section_sectionId = section_sectionId;
	}
	
	public String getMrbPsId() {
		return mrbPsId;
	}
	
	public void setMrbPsId(String mrbPsId) {
		this.mrbPsId = mrbPsId;
	}
	
	public Integer getMrbPsCode() {
		return mrbPsCode;
	}
	
	public void setMrbPsCode(Integer mrbPsCode) {
		this.mrbPsCode = mrbPsCode;
	}

	public ICusMpdChapterBo getCusMpdChapterBo() {
		return cusMpdChapterBo;
	}

	public void setCusMpdChapterBo(ICusMpdChapterBo cusMpdChapterBo) {
		this.cusMpdChapterBo = cusMpdChapterBo;
	}

	public ITaskMpdVersionBo getTaskMpdVersionBo() {
		return taskMpdVersionBo;
	}

	public void setTaskMpdVersionBo(ITaskMpdVersionBo taskMpdVersionBo) {
		this.taskMpdVersionBo = taskMpdVersionBo;
	}

	
	
	
	
	
	
	

}
