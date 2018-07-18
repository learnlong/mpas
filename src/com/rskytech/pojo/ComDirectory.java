package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComDirectory entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComDirectory implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 86125327954459789L;
	private String directoryId;
	private ComModelSeries comModelSeries;
	private ComDirectory comDirectory;
	private Integer dirLevel;
	private String dirName;
	private Integer validFlag;
	private Date createDate;
	private String createUser;
	private Date modifyDate;
	private String modifyUser;
	private Set comDirectories = new HashSet(0);
	private Set comFiles = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComDirectory() {
	}

	/** full constructor */
	public ComDirectory(ComModelSeries comModelSeries,
			ComDirectory comDirectory, Integer dirLevel, String dirName,
			Integer validFlag, Date createDate, String createUser,
			Date modifyDate, String modifyUser, Set comDirectories, Set comFiles) {
		this.comModelSeries = comModelSeries;
		this.comDirectory = comDirectory;
		this.dirLevel = dirLevel;
		this.dirName = dirName;
		this.validFlag = validFlag;
		this.createDate = createDate;
		this.createUser = createUser;
		this.modifyDate = modifyDate;
		this.modifyUser = modifyUser;
		this.comDirectories = comDirectories;
		this.comFiles = comFiles;
	}

	// Property accessors

	public String getDirectoryId() {
		return this.directoryId;
	}

	public void setDirectoryId(String directoryId) {
		this.directoryId = directoryId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public ComDirectory getComDirectory() {
		return this.comDirectory;
	}

	public void setComDirectory(ComDirectory comDirectory) {
		this.comDirectory = comDirectory;
	}

	public Integer getDirLevel() {
		return this.dirLevel;
	}

	public void setDirLevel(Integer dirLevel) {
		this.dirLevel = dirLevel;
	}

	public String getDirName() {
		return this.dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Set getComDirectories() {
		return this.comDirectories;
	}

	public void setComDirectories(Set comDirectories) {
		this.comDirectories = comDirectories;
	}

	public Set getComFiles() {
		return this.comFiles;
	}

	public void setComFiles(Set comFiles) {
		this.comFiles = comFiles;
	}

}