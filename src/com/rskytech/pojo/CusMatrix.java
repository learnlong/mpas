package com.rskytech.pojo;

import java.util.Date;

/**
 * CusMatrix entity. @author MyEclipse Persistence Tools
 */

public class CusMatrix implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 8221557045937012606L;
	private String matrixId;
	private ComModelSeries comModelSeries;
	private String anaFlg;
	private Integer matrixFlg;
	private Integer matrixRow;
	private String matrixRowName;
	private Integer matrixCol;
	private String matrixColName;
	private Integer matrixValue;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusMatrix() {
	}

	/** full constructor */
	public CusMatrix(ComModelSeries comModelSeries, String anaFlg,
			Integer matrixFlg, Integer matrixRow, String matrixRowName,
			Integer matrixCol, String matrixColName, Integer matrixValue,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.anaFlg = anaFlg;
		this.matrixFlg = matrixFlg;
		this.matrixRow = matrixRow;
		this.matrixRowName = matrixRowName;
		this.matrixCol = matrixCol;
		this.matrixColName = matrixColName;
		this.matrixValue = matrixValue;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getMatrixId() {
		return this.matrixId;
	}

	public void setMatrixId(String matrixId) {
		this.matrixId = matrixId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getAnaFlg() {
		return this.anaFlg;
	}

	public void setAnaFlg(String anaFlg) {
		this.anaFlg = anaFlg;
	}

	public Integer getMatrixFlg() {
		return this.matrixFlg;
	}

	public void setMatrixFlg(Integer matrixFlg) {
		this.matrixFlg = matrixFlg;
	}

	public Integer getMatrixRow() {
		return this.matrixRow;
	}

	public void setMatrixRow(Integer matrixRow) {
		this.matrixRow = matrixRow;
	}

	public String getMatrixRowName() {
		return this.matrixRowName;
	}

	public void setMatrixRowName(String matrixRowName) {
		this.matrixRowName = matrixRowName;
	}

	public Integer getMatrixCol() {
		return this.matrixCol;
	}

	public void setMatrixCol(Integer matrixCol) {
		this.matrixCol = matrixCol;
	}

	public String getMatrixColName() {
		return this.matrixColName;
	}

	public void setMatrixColName(String matrixColName) {
		this.matrixColName = matrixColName;
	}

	public Integer getMatrixValue() {
		return this.matrixValue;
	}

	public void setMatrixValue(Integer matrixValue) {
		this.matrixValue = matrixValue;
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

}