package com.rskytech.paramdefinemanage.action;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusMpdPsBo;
import com.rskytech.basedata.bo.IFileUploadBo;
import com.rskytech.paramdefinemanage.bo.IMrbrReportBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusMrbChapter;
import com.rskytech.pojo.CusMrbPs;
import com.rskytech.pojo.CusMrbSection;
import com.rskytech.util.StringUtil;

public class MrbrReportAction extends BaseAction{

	private static final long serialVersionUID = -2607281935673877723L;
	
	private IMrbrReportBo mrbrReportBo;
	private IFileUploadBo fileUploadBo;
	private ICusMpdPsBo cusMpdPsBo;
	
	
    private Integer modelSeries;
	
	private String chapterId;
	private String chapterContentCn;

	
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
	
	private String section_chapterId;
	private String section_sectionId;
	private String mrbPsId;
	private Integer mrbPsCode;
	// 报表最大宽和高
	public static final int WIDTH = 910;
	public static final int HEIGHT = 520;
	
	
	public String init() {
		List<CusMrbChapter> chaters = mrbrReportBo.getChapterByCode(this.getComModelSeries().getModelSeriesId(), ComacConstants.MRBCHAPTERCODE[0]);
		if (chaters != null && chaters.size() > 0) {
			
		}
		else {
			ComModelSeries ms = (ComModelSeries) this.mrbrReportBo.loadById(ComModelSeries.class, this.getComModelSeries().getModelSeriesId());
			for (int i = 0; i < ComacConstants.MRBCHAPTERCODE.length; i++) {
				CusMrbChapter c = new CusMrbChapter();
				c.setComModelSeries(ms);
				c.setChapterCode(ComacConstants.MRBCHAPTERCODE[i]);
				c.setChapterName(ComacConstants.MRBCHAPTERNAMECN[i]);
			
				this.mrbrReportBo.saveOrUpdate(c, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
				
			}
		}
		
		ComUser thisUser = getSysUser();
		if (thisUser == null) {
			return SUCCESS;
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * 初始化加载树
	 * 
	 * @return
	 */
	public String loadTree() {
	
		
		JSONArray arrChapter = new JSONArray();
		for (int i = 0; i < ComacConstants.MRBCHAPTERCODE.length; i++) {
			List<CusMrbChapter> chaters = mrbrReportBo.getChapterByCode(this.getComModelSeries().getModelSeriesId(), ComacConstants.MRBCHAPTERCODE[i]);
			CusMrbChapter currentChapter = null;
			JSONObject objChapter = new JSONObject();
			
			if (chaters != null && chaters.size() > 0) {
				// 数据库中有章的数据
				currentChapter = chaters.get(0);
				objChapter.put("id", "c" + currentChapter.getChapterId());
			}
			else {
				// 数据库中暂时还没有章的数据
				objChapter.put("id", "t" + ComacConstants.MRBCHAPTERCODE[i]);
			}
			objChapter.put("text", ComacConstants.MRBCHAPTERNAMECN[i]);
			objChapter.put("leaf", false);
			objChapter.put("expanded", false);
			objChapter.put("sort", "test");
			
			JSONArray arrSection = new JSONArray();
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
				JSONObject objSection = new JSONObject();
				objSection.put("text",  section.getSectionName());
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
	 * 加载MRB附录生成树
	 * 
	 * @return
	 */
	public String loadMrbPsTree() {

		List<CusMrbPs> list = mrbrReportBo.findByModelSeriesId(this.getComModelSeries().getModelSeriesId());
		JSONArray arrPs = new JSONArray();
		for (CusMrbPs ps : list) {
			JSONObject objPs = new JSONObject();
			objPs.put("text", ps.getPsName());
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
	 * 更新章内容
	 * 
	 * @return
	 */
	public String updateChapter() {
		String cid = this.getChapterId();
		String dbOperate = null;
		CusMrbChapter chapter = new CusMrbChapter();
		if ("t".equals(cid.substring(0, 1))) {
			String cCode = cid.substring(1);
			for (int i = 0; i < ComacConstants.MRBCHAPTERCODE.length; i++) {
				if (cCode.equals(ComacConstants.MRBCHAPTERCODE[i])) {
					chapter.setChapterCode(ComacConstants.MRBCHAPTERCODE[i]);
					chapter.setChapterName(ComacConstants.MRBCHAPTERNAMECN[i]);
			
					break;
				}
			}
			dbOperate = ComacConstants.DB_INSERT;
		}
		else {
			chapter = (CusMrbChapter) this.mrbrReportBo.loadById(CusMrbChapter.class, cid.substring(1));
			chapter.setModifyDate(new Date());
			chapter.setModifyUser(this.getSysUser().getUserId());
			dbOperate = ComacConstants.DB_UPDATE;
		}
		chapter.setComModelSeries(this.getComModelSeries());

		chapter.setChapterContent(this.getChapterContentCn());
	

	
		boolean result = this.mrbrReportBo.saveChapter(chapter, dbOperate, this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	
	/**
	 * 加载章内容
	 * 
	 * @return
	 */
	public String loadChapter() {

		String cid = this.getChapterId();
		CusMrbChapter chapter = (CusMrbChapter) this.mrbrReportBo.loadById(CusMrbChapter.class, cid.substring(1));
		JSONObject object = new JSONObject();
		if (chapter != null) {
			object.put("chapterId", chapter.getChapterId());
			object.put("nameCn", chapter.getChapterName());
	
			object.put("code", chapter.getChapterCode());
			object.put("chapterFlg", chapter.getChapterFlg());
			// object.put("content", language.equals("Cn") ?
			// chapter.getChapterContentCn() : chapter.getChapterContentEn());
			object.put("contentCn", StringUtil.recoveryContextPath(chapter.getChapterContent()));

		}
		writeToResponse(object.toString());
		return null;
	}
	
	/**
	 * 加载节内容
	 * 
	 * @return
	 */
	public String loadSection() {
	
		CusMrbSection section = (CusMrbSection) this.mrbrReportBo.loadById(CusMrbSection.class, this.getSectionId().substring(1));
		JSONObject object = new JSONObject();
		object.put("nameCn", section.getSectionName());

		object.put("code", section.getSectionCode());
		object.put("contentCn", StringUtil.recoveryContextPath(section.getSectionContent()));


		writeToResponse(object.toString());
		return null;
	}
	
	/**
	 * 删除节
	 * 
	 * @return
	 */
	public String deleteSection() {
		boolean result = this.mrbrReportBo.deleteSectionById(CusMrbSection.class, this.getSectionId(), this.getSysUser().getUserId());
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
		CusMrbChapter chapter = (CusMrbChapter) this.mrbrReportBo.loadById(CusMrbChapter.class, this.getSection_chapterId().substring(1));
		CusMrbSection section = isUpdate ? (CusMrbSection) this.mrbrReportBo.loadById(CusMrbSection.class, this.getSection_sectionId().substring(1))
				: new CusMrbSection();
		section.setCusMrbChapter(chapter);
		section.setSectionCode(this.getSectionCode());
		section.setSectionName(this.getSectionNameCn());

		//section.setModelSeriesId(this.getComModelSeries().getModelSeriesId());
		boolean result = false;
		if (isUpdate) {
			section.setSectionId(this.getSection_sectionId().substring(1));
			section.setModifyDate(new Date());
			section.setModifyUser(this.getSysUser().getUserId());
			result = this.mrbrReportBo.saveSection(section, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		}
		else {
			section.setCreateUser(this.getSysUser().getUserId());
			section.setCreateDate(new Date());
			result = this.mrbrReportBo.saveSection(section, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
		}
		
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 更新MRB节内容
	 * 
	 * @return
	 */
	public String updateSection() {
	
		CusMrbSection section = (CusMrbSection) this.mrbrReportBo.loadById(CusMrbSection.class, this.getSectionId().substring(1));
		section.setModifyDate(new Date());
		section.setModifyUser(this.getSysUser().getUserId());

		section.setSectionContent(this.getSectionContentCn());
	
		boolean result = this.mrbrReportBo.saveSection(section, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 上传MPD附录，保存地址到附录表
	 * 
	 * @return
	 */
	public String saveMrbPs() {
		JSONObject object = new JSONObject();
		boolean isValid = true;
		if (pdfFile != null && !"application/pdf".equals(pdfFileContentType)) {
			object.put("message", "上传失败！您上传的文件非PDF格式!");
			isValid = false;
		}
		boolean isPsFlgUnique = this.getPsFlg() == 0 && this.mrbrReportBo.isPsFlgUnique(this.getComModelSeries().getModelSeriesId(), this.getPsId());
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
			boolean isUpdate = this.getPsId() != null && !("").equals(this.getPsId());
			CusMrbPs ps = isUpdate ? (CusMrbPs) this.mrbrReportBo.loadById(CusMrbPs.class, this.getPsId()) : new CusMrbPs();
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
				result = this.mrbrReportBo.saveMrbPs(ps, ComacConstants.DB_UPDATE, this.getSysUser().getUserId());
			}
			else {
				ps.setCreateUser(this.getSysUser().getUserId());
				ps.setCreateDate(new Date());
				result = this.mrbrReportBo.saveMrbPs(ps, ComacConstants.DB_INSERT, this.getSysUser().getUserId());
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
	 * 加载MRB附录用于修改附录基础信息
	 * 
	 * @return
	 */
	public String loadMrbPs() {
		CusMrbPs ps = (CusMrbPs) this.mrbrReportBo.loadById(CusMrbPs.class, this.getPsId());
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
	 * 删除MRB附录
	 * 
	 * @return
	 */
	public String deleteMrbPs() {
		boolean result = this.mrbrReportBo.deleteMrbPsById(CusMrbPs.class, this.getPsId(), this.getSysUser().getUserId());
		writeToResponse("{\"success\":" + result + "}");
		return null;
	}
	
	/**
	 * 检查同一个章下的节Code是否存在
	 * 
	 * @return
	 */
	public String checkSectionCode() {
		boolean flag = mrbrReportBo.checkSectionCode(this.sectionId, this.chapterId, this.sectionCode);
		if (!flag) {
			writeToResponse("false");
			return null;
		}
		else {
			writeToResponse("true");
			return null;
		}
	}
	
	
	
	/**
	 * 检查同一个机型下的附录的Code是否存在
	 * 
	 * @return
	 */
	public String checkMrbPsCode() {
		boolean flag = mrbrReportBo.checkMrbPsCode(this.getComModelSeries().getModelSeriesId(), this.mrbPsId, this.mrbPsCode);
		if (!flag) {
			writeToResponse("false");
			return null;
		}
		else {
			writeToResponse("true");
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public Integer getModelSeries() {
		return modelSeries;
	}
	public void setModelSeries(Integer modelSeries) {
		this.modelSeries = modelSeries;
	}
	public String getChapterId() {
		return chapterId;
	}
	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}
	public String getChapterContentCn() {
		return chapterContentCn;
	}
	public void setChapterContentCn(String chapterContentCn) {
		this.chapterContentCn = StringUtil.replaceContextPath(chapterContentCn, WIDTH, HEIGHT);
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
	
	public String getSectionContentCn() {
		return sectionContentCn;
	}
	public void setSectionContentCn(String sectionContentCn) {
		this.sectionContentCn = StringUtil.replaceContextPath(sectionContentCn, WIDTH, HEIGHT);
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



	public IMrbrReportBo getMrbrReportBo() {
		return mrbrReportBo;
	}



	public void setMrbrReportBo(IMrbrReportBo mrbrReportBo) {
		this.mrbrReportBo = mrbrReportBo;
	}



	public IFileUploadBo getFileUploadBo() {
		return fileUploadBo;
	}



	public void setFileUploadBo(IFileUploadBo fileUploadBo) {
		this.fileUploadBo = fileUploadBo;
	}



	public ICusMpdPsBo getCusMpdPsBo() {
		return cusMpdPsBo;
	}



	public void setCusMpdPsBo(ICusMpdPsBo cusMpdPsBo) {
		this.cusMpdPsBo = cusMpdPsBo;
	}
	
	
	
	
	
	
	
	
	

}
