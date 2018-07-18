package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComModelSeries entity. @author MyEclipse Persistence Tools
 */

public class ComModelSeries implements java.io.Serializable {

	// Fields

	private String modelSeriesId;
	private String modelSeriesCode;
	private String modelSeriesName;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Integer defaultModelSeries;
	private Set comMmels = new HashSet(0);
	private Set cusEdrAdrs = new HashSet(0);
	private Set comAtas = new HashSet(0);
	private Set cusIntervals = new HashSet(0);
	private Set cusMpdPses = new HashSet(0);
	private Set lhMains = new HashSet(0);
	private Set MSelects = new HashSet(0);
	private Set taskMpds = new HashSet(0);
	private Set taskMrbs = new HashSet(0);
	private Set comHelps = new HashSet(0);
	private Set cusCracks = new HashSet(0);
	private Set cusItemZa5s = new HashSet(0);
	private Set cusMpdChapters = new HashSet(0);
	private Set zaMains = new HashSet(0);
	private Set comAreas = new HashSet(0);
	private Set cusDisplaies = new HashSet(0);
	private Set cusLevels = new HashSet(0);
	private Set comDirectories = new HashSet(0);
	private Set comAuthorities = new HashSet(0);
	private Set SMains = new HashSet(0);
	private Set cusMrbPses = new HashSet(0);
	private Set cusItemS45s = new HashSet(0);
	private Set comReports = new HashSet(0);
	private Set comCoordinations = new HashSet(0);
	private Set comLogOperates = new HashSet(0);
	private Set comVendors = new HashSet(0);
	private Set cusMatrixes = new HashSet(0);
	private Set taskMsgs = new HashSet(0);
	private Set cusMrbChapters = new HashSet(0);
	private Set MMains = new HashSet(0);

	// Constructors

	/** default constructor */
	public ComModelSeries() {
	}

	/** full constructor */
	public ComModelSeries(String modelSeriesCode, String modelSeriesName,
			Integer validFlag, String createUser, Date createDate,
			String modifyUser, Date modifyDate, Integer defaultModelSeries,
			Set comMmels, Set cusEdrAdrs, Set comAtas, Set cusIntervals,
			Set cusMpdPses, Set lhMains, Set MSelects, Set taskMpds,
			Set taskMrbs, Set comHelps, Set cusCracks, Set cusItemZa5s,
			Set cusMpdChapters, Set zaMains, Set comAreas, Set cusDisplaies,
			Set cusLevels, Set comDirectories, Set comAuthorities, Set SMains,
			Set cusMrbPses, Set cusItemS45s, Set comReports,
			Set comCoordinations, Set comLogOperates, Set comVendors,
			Set cusMatrixes, Set taskMsgs, Set cusMrbChapters, Set MMains) {
		this.modelSeriesCode = modelSeriesCode;
		this.modelSeriesName = modelSeriesName;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.defaultModelSeries = defaultModelSeries;
		this.comMmels = comMmels;
		this.cusEdrAdrs = cusEdrAdrs;
		this.comAtas = comAtas;
		this.cusIntervals = cusIntervals;
		this.cusMpdPses = cusMpdPses;
		this.lhMains = lhMains;
		this.MSelects = MSelects;
		this.taskMpds = taskMpds;
		this.taskMrbs = taskMrbs;
		this.comHelps = comHelps;
		this.cusCracks = cusCracks;
		this.cusItemZa5s = cusItemZa5s;
		this.cusMpdChapters = cusMpdChapters;
		this.zaMains = zaMains;
		this.comAreas = comAreas;
		this.cusDisplaies = cusDisplaies;
		this.cusLevels = cusLevels;
		this.comDirectories = comDirectories;
		this.comAuthorities = comAuthorities;
		this.SMains = SMains;
		this.cusMrbPses = cusMrbPses;
		this.cusItemS45s = cusItemS45s;
		this.comReports = comReports;
		this.comCoordinations = comCoordinations;
		this.comLogOperates = comLogOperates;
		this.comVendors = comVendors;
		this.cusMatrixes = cusMatrixes;
		this.taskMsgs = taskMsgs;
		this.cusMrbChapters = cusMrbChapters;
		this.MMains = MMains;
	}

	// Property accessors

	public String getModelSeriesId() {
		return this.modelSeriesId;
	}

	public void setModelSeriesId(String modelSeriesId) {
		this.modelSeriesId = modelSeriesId;
	}

	public String getModelSeriesCode() {
		return this.modelSeriesCode;
	}

	public void setModelSeriesCode(String modelSeriesCode) {
		this.modelSeriesCode = modelSeriesCode;
	}

	public String getModelSeriesName() {
		return this.modelSeriesName;
	}

	public void setModelSeriesName(String modelSeriesName) {
		this.modelSeriesName = modelSeriesName;
	}

	public Integer getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(Integer validFlag) {
		this.validFlag = validFlag;
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

	public Integer getDefaultModelSeries() {
		return this.defaultModelSeries;
	}

	public void setDefaultModelSeries(Integer defaultModelSeries) {
		this.defaultModelSeries = defaultModelSeries;
	}

	public Set getComMmels() {
		return this.comMmels;
	}

	public void setComMmels(Set comMmels) {
		this.comMmels = comMmels;
	}

	public Set getCusEdrAdrs() {
		return this.cusEdrAdrs;
	}

	public void setCusEdrAdrs(Set cusEdrAdrs) {
		this.cusEdrAdrs = cusEdrAdrs;
	}

	public Set getComAtas() {
		return this.comAtas;
	}

	public void setComAtas(Set comAtas) {
		this.comAtas = comAtas;
	}

	public Set getCusIntervals() {
		return this.cusIntervals;
	}

	public void setCusIntervals(Set cusIntervals) {
		this.cusIntervals = cusIntervals;
	}

	public Set getCusMpdPses() {
		return this.cusMpdPses;
	}

	public void setCusMpdPses(Set cusMpdPses) {
		this.cusMpdPses = cusMpdPses;
	}

	public Set getLhMains() {
		return this.lhMains;
	}

	public void setLhMains(Set lhMains) {
		this.lhMains = lhMains;
	}

	public Set getMSelects() {
		return this.MSelects;
	}

	public void setMSelects(Set MSelects) {
		this.MSelects = MSelects;
	}

	public Set getTaskMpds() {
		return this.taskMpds;
	}

	public void setTaskMpds(Set taskMpds) {
		this.taskMpds = taskMpds;
	}

	public Set getTaskMrbs() {
		return this.taskMrbs;
	}

	public void setTaskMrbs(Set taskMrbs) {
		this.taskMrbs = taskMrbs;
	}

	public Set getComHelps() {
		return this.comHelps;
	}

	public void setComHelps(Set comHelps) {
		this.comHelps = comHelps;
	}

	public Set getCusCracks() {
		return this.cusCracks;
	}

	public void setCusCracks(Set cusCracks) {
		this.cusCracks = cusCracks;
	}

	public Set getCusItemZa5s() {
		return this.cusItemZa5s;
	}

	public void setCusItemZa5s(Set cusItemZa5s) {
		this.cusItemZa5s = cusItemZa5s;
	}

	public Set getCusMpdChapters() {
		return this.cusMpdChapters;
	}

	public void setCusMpdChapters(Set cusMpdChapters) {
		this.cusMpdChapters = cusMpdChapters;
	}

	public Set getZaMains() {
		return this.zaMains;
	}

	public void setZaMains(Set zaMains) {
		this.zaMains = zaMains;
	}

	public Set getComAreas() {
		return this.comAreas;
	}

	public void setComAreas(Set comAreas) {
		this.comAreas = comAreas;
	}

	public Set getCusDisplaies() {
		return this.cusDisplaies;
	}

	public void setCusDisplaies(Set cusDisplaies) {
		this.cusDisplaies = cusDisplaies;
	}

	public Set getCusLevels() {
		return this.cusLevels;
	}

	public void setCusLevels(Set cusLevels) {
		this.cusLevels = cusLevels;
	}

	public Set getComDirectories() {
		return this.comDirectories;
	}

	public void setComDirectories(Set comDirectories) {
		this.comDirectories = comDirectories;
	}

	public Set getComAuthorities() {
		return this.comAuthorities;
	}

	public void setComAuthorities(Set comAuthorities) {
		this.comAuthorities = comAuthorities;
	}

	public Set getSMains() {
		return this.SMains;
	}

	public void setSMains(Set SMains) {
		this.SMains = SMains;
	}

	public Set getCusMrbPses() {
		return this.cusMrbPses;
	}

	public void setCusMrbPses(Set cusMrbPses) {
		this.cusMrbPses = cusMrbPses;
	}

	public Set getCusItemS45s() {
		return this.cusItemS45s;
	}

	public void setCusItemS45s(Set cusItemS45s) {
		this.cusItemS45s = cusItemS45s;
	}

	public Set getComReports() {
		return this.comReports;
	}

	public void setComReports(Set comReports) {
		this.comReports = comReports;
	}

	public Set getComCoordinations() {
		return this.comCoordinations;
	}

	public void setComCoordinations(Set comCoordinations) {
		this.comCoordinations = comCoordinations;
	}

	public Set getComLogOperates() {
		return this.comLogOperates;
	}

	public void setComLogOperates(Set comLogOperates) {
		this.comLogOperates = comLogOperates;
	}

	public Set getComVendors() {
		return this.comVendors;
	}

	public void setComVendors(Set comVendors) {
		this.comVendors = comVendors;
	}

	public Set getCusMatrixes() {
		return this.cusMatrixes;
	}

	public void setCusMatrixes(Set cusMatrixes) {
		this.cusMatrixes = cusMatrixes;
	}

	public Set getTaskMsgs() {
		return this.taskMsgs;
	}

	public void setTaskMsgs(Set taskMsgs) {
		this.taskMsgs = taskMsgs;
	}

	public Set getCusMrbChapters() {
		return this.cusMrbChapters;
	}

	public void setCusMrbChapters(Set cusMrbChapters) {
		this.cusMrbChapters = cusMrbChapters;
	}

	public Set getMMains() {
		return this.MMains;
	}

	public void setMMains(Set MMains) {
		this.MMains = MMains;
	}

}