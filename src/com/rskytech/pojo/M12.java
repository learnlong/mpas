package com.rskytech.pojo;

import java.util.Date;

/**
 * M12 entity. @author MyEclipse Persistence Tools
 */

public class M12 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1289300536984339315L;
	private String m12Id;
	private MMain MMain;
	private String proCode;
	private String proName;
	private Integer quantity;
	private String vendor;
	private String partNo;
	private String similar;
	private String historicalMtbf;
	private String predictedMtbf;
	private String zonal;
	private String zonalChannel;
	private Integer mmel;
	private Integer isAddAta;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public M12() {
	}

	/** full constructor */
	public M12(MMain MMain, String proCode, String proName, Integer quantity,
			String vendor, String partNo, String similar,
			String historicalMtbf, String predictedMtbf, String zonal,
			String zonalChannel, Integer mmel, Integer isAddAta,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate) {
		this.MMain = MMain;
		this.proCode = proCode;
		this.proName = proName;
		this.quantity = quantity;
		this.vendor = vendor;
		this.partNo = partNo;
		this.similar = similar;
		this.historicalMtbf = historicalMtbf;
		this.predictedMtbf = predictedMtbf;
		this.zonal = zonal;
		this.zonalChannel = zonalChannel;
		this.mmel = mmel;
		this.isAddAta = isAddAta;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
	}

	// Property accessors

	public String getM12Id() {
		return this.m12Id;
	}

	public void setM12Id(String m12Id) {
		this.m12Id = m12Id;
	}

	public MMain getMMain() {
		return this.MMain;
	}

	public void setMMain(MMain MMain) {
		this.MMain = MMain;
	}

	public String getProCode() {
		return this.proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getPartNo() {
		return this.partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getSimilar() {
		return this.similar;
	}

	public void setSimilar(String similar) {
		this.similar = similar;
	}

	public String getHistoricalMtbf() {
		return this.historicalMtbf;
	}

	public void setHistoricalMtbf(String historicalMtbf) {
		this.historicalMtbf = historicalMtbf;
	}

	public String getPredictedMtbf() {
		return this.predictedMtbf;
	}

	public void setPredictedMtbf(String predictedMtbf) {
		this.predictedMtbf = predictedMtbf;
	}

	public String getZonal() {
		return this.zonal;
	}

	public void setZonal(String zonal) {
		this.zonal = zonal;
	}

	public String getZonalChannel() {
		return this.zonalChannel;
	}

	public void setZonalChannel(String zonalChannel) {
		this.zonalChannel = zonalChannel;
	}

	public Integer getMmel() {
		return this.mmel;
	}

	public void setMmel(Integer mmel) {
		this.mmel = mmel;
	}

	public Integer getIsAddAta() {
		return this.isAddAta;
	}

	public void setIsAddAta(Integer isAddAta) {
		this.isAddAta = isAddAta;
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