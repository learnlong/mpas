package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ComAta entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class ComAta implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 8643130727933304660L;
	private String ataId;
	private ComAta comAta;
	private ComModelSeries comModelSeries;
	private String ataCode;
	private String ataName;
	private Integer ataLevel;
	private String equipmentName;
	private String equipmentPicNo;
	private String equipmentTypeNo;
	private String equipmentPosition;
	private String remark;
	private Integer validFlag;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set MSelects = new HashSet(0);
	private Set MMains = new HashSet(0);
	private Set SMains = new HashSet(0);
	private Set comAtas = new HashSet(0);
	private Integer excelRow;//ata导入导出用.行号，为了报错
	private String tmpCode;//存放excel中第一列code，去掉了“-”

	// Constructors

	/** default constructor */
	public ComAta() {
	}

	/** full constructor */
	public ComAta(ComAta comAta, ComModelSeries comModelSeries, String ataCode,
			String ataName, Integer ataLevel, String equipmentName,
			String equipmentPicNo, String equipmentTypeNo,
			String equipmentPosition, String remark, Integer validFlag,
			String createUser, Date createDate, String modifyUser,
			Date modifyDate, Set MSelects, Set MMains, Set SMains, Set comAtas) {
		this.comAta = comAta;
		this.comModelSeries = comModelSeries;
		this.ataCode = ataCode;
		this.ataName = ataName;
		this.ataLevel = ataLevel;
		this.equipmentName = equipmentName;
		this.equipmentPicNo = equipmentPicNo;
		this.equipmentTypeNo = equipmentTypeNo;
		this.equipmentPosition = equipmentPosition;
		this.remark = remark;
		this.validFlag = validFlag;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.MSelects = MSelects;
		this.MMains = MMains;
		this.SMains = SMains;
		this.comAtas = comAtas;
	}

	// Property accessors

	public String getAtaId() {
		return this.ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public ComAta getComAta() {
		return this.comAta;
	}

	public void setComAta(ComAta comAta) {
		this.comAta = comAta;
	}

	public ComModelSeries getComModelSeries() {
		return this.comModelSeries;
	}

	public void setComModelSeries(ComModelSeries comModelSeries) {
		this.comModelSeries = comModelSeries;
	}

	public String getAtaCode() {
		return this.ataCode;
	}

	public void setAtaCode(String ataCode) {
		this.ataCode = ataCode;
	}

	public String getAtaName() {
		return this.ataName;
	}

	public void setAtaName(String ataName) {
		this.ataName = ataName;
	}

	public Integer getAtaLevel() {
		return this.ataLevel;
	}

	public void setAtaLevel(Integer ataLevel) {
		this.ataLevel = ataLevel;
	}

	public String getEquipmentName() {
		return this.equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentPicNo() {
		return this.equipmentPicNo;
	}

	public void setEquipmentPicNo(String equipmentPicNo) {
		this.equipmentPicNo = equipmentPicNo;
	}

	public String getEquipmentTypeNo() {
		return this.equipmentTypeNo;
	}

	public void setEquipmentTypeNo(String equipmentTypeNo) {
		this.equipmentTypeNo = equipmentTypeNo;
	}

	public String getEquipmentPosition() {
		return this.equipmentPosition;
	}

	public void setEquipmentPosition(String equipmentPosition) {
		this.equipmentPosition = equipmentPosition;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Set getMSelects() {
		return this.MSelects;
	}

	public void setMSelects(Set MSelects) {
		this.MSelects = MSelects;
	}

	public Set getMMains() {
		return this.MMains;
	}

	public void setMMains(Set MMains) {
		this.MMains = MMains;
	}

	public Set getSMains() {
		return this.SMains;
	}

	public void setSMains(Set SMains) {
		this.SMains = SMains;
	}

	public Set getComAtas() {
		return this.comAtas;
	}

	public void setComAtas(Set comAtas) {
		this.comAtas = comAtas;
	}

	public Integer getExcelRow() {
		return excelRow;
	}

	public void setExcelRow(Integer excelRow) {
		this.excelRow = excelRow;
	}

	public String getTmpCode() {
		return tmpCode;
	}

	public void setTmpCode(String tmpCode) {
		this.tmpCode = tmpCode;
	}

}