package com.rskytech.pojo;

import java.util.Date;

/**
 * CusEdrAdr entity. @author MyEclipse Persistence Tools
 */

public class CusEdrAdr implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3624295292434645733L;
	private String algorithmId;
	private ComModelSeries comModelSeries;
	private String stepFlg;
	private String algorithmFlg;
	private Integer operateFlg;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public CusEdrAdr() {
	}

	/** full constructor */
	public CusEdrAdr(ComModelSeries comModelSeries, String stepFlg,
			String algorithmFlg, Integer operateFlg, String createUser,
			Date createDate, String modifyUser, Date modifyDate) {
		this.comModelSeries = comModelSeries;
		this.stepFlg = stepFlg;
		this.algorithmFlg = algorithmFlg;
		this.operateFlg = operateFlg;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getAlgorithmId() {
		return this.algorithmId;
	}

	public void setAlgorithmId(String algorithmId) {
		this.algorithmId = algorithmId;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getStepFlg() {
		return this.stepFlg;
	}

	public void setStepFlg(String stepFlg) {
		this.stepFlg = stepFlg;
	}

	public String getAlgorithmFlg() {
		return this.algorithmFlg;
	}

	public void setAlgorithmFlg(String algorithmFlg) {
		this.algorithmFlg = algorithmFlg;
	}

	public Integer getOperateFlg() {
		return this.operateFlg;
	}

	public void setOperateFlg(Integer operateFlg) {
		this.operateFlg = operateFlg;
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