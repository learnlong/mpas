package com.rskytech.pojo;

/**
 * ComAreaDetail entity. @author MyEclipse Persistence Tools
 */

public class ComAreaDetail implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 8706111321827677501L;
	private String detailId;
	private ComArea comArea;
	private String equipmentName;
	private String equipmentPicNo;
	private String equipmentTypeNo;

	// Constructors

	/** default constructor */
	public ComAreaDetail() {
	}

	/** full constructor */
	public ComAreaDetail(ComArea comArea,
			String equipmentName, String equipmentPicNo, String equipmentTypeNo) {
		this.comArea = comArea;
		this.equipmentName = equipmentName;
		this.equipmentPicNo = equipmentPicNo;
		this.equipmentTypeNo = equipmentTypeNo;
	}

	// Property accessors

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public ComArea getComArea() {
		return this.comArea;
	}

	public void setComArea(ComArea comArea) {
		this.comArea = comArea;
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

	@Override
	public boolean equals(Object o) { //如果和自身比较，返回TRUE  
		   if(this == o) return true;
		   //如果不是这个类的实例，返回FALSE  
		   if(!(o instanceof ComAreaDetail)) return false;
		   ComAreaDetail that = (ComAreaDetail)o;
		   String thisName = this.equipmentName;
		   if(thisName == null){
			   thisName = "";
		   }
		   String thisPicNo = this.equipmentPicNo;
		   if(thisPicNo == null){
			   thisPicNo = "";
		   }
		   String thisTypeNo = this.equipmentTypeNo;
		   if(thisTypeNo == null){
			   thisTypeNo = "";
		   }
		   String thatName = that.getEquipmentName();
		   if(thatName == null){
			   thatName = "";
		   }
		   String thatPicNo = that.getEquipmentPicNo();
		   if(thatPicNo == null){
			   thatPicNo = "";
		   }
		   String thatTypeNo = that.getEquipmentTypeNo();
		   if(thatTypeNo == null){
			   thatTypeNo = "";
		   }
		   if(thisName.equals(thatName) && thisPicNo.equals(thatPicNo) && thisTypeNo.equals(thatTypeNo)){
			   return true;
		   } else {
			   return false;
		   }
	}

}