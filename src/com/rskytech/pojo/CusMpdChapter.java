package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * CusMpdChapter entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class CusMpdChapter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4468072624920265148L;
	private String chapterId;
	private ComModelSeries comModelSeries;
	private String chapterCode;
	private String chapterName;
	private String chapterContent;
	private String chapterFlg;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set cusMpdSections = new HashSet(0);

	// Constructors

	/** default constructor */
	public CusMpdChapter() {
	}

	/** full constructor */
	public CusMpdChapter(ComModelSeries comModelSeries, String chapterCode,
			String chapterName, String chapterContent, String chapterFlg,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set cusMpdSections) {
		this.comModelSeries = comModelSeries;
		this.chapterCode = chapterCode;
		this.chapterName = chapterName;
		this.chapterContent = chapterContent;
		this.chapterFlg = chapterFlg;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.cusMpdSections = cusMpdSections;
	}

	// Property accessors

	public String getChapterId() {
		return this.chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getChapterCode() {
		return this.chapterCode;
	}

	public void setChapterCode(String chapterCode) {
		this.chapterCode = chapterCode;
	}

	public String getChapterName() {
		return this.chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getChapterContent() {
		return this.chapterContent;
	}

	public void setChapterContent(String chapterContent) {
		this.chapterContent = chapterContent;
	}

	public String getChapterFlg() {
		return this.chapterFlg;
	}

	public void setChapterFlg(String chapterFlg) {
		this.chapterFlg = chapterFlg;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Set getCusMpdSections() {
		return this.cusMpdSections;
	}

	public void setCusMpdSections(Set cusMpdSections) {
		this.cusMpdSections = cusMpdSections;
	}

}