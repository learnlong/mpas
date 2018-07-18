package com.rskytech.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("rawtypes")
public class MSet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8993420994628984521L;
	private String msetId;
	private MMain MMain;
	private String functionCode;
	private String functionDesc;
	private String createUser;
	private Date createDate;
	private String modifyUser;
	private Date modifyDate;
	private Set mSetFs = new HashSet(0);
	private ComAta comAta;
	
	public MSet() {
		super();
	}
	
	public MSet(String msetId, com.rskytech.pojo.MMain mMain,
			String functionCode, String functionDesc, String createUser,
			Date createDate, String modifyUser, Date modifyDate, Set mSetFs,
			ComAta comAta) {
		super();
		this.msetId = msetId;
		MMain = mMain;
		this.functionCode = functionCode;
		this.functionDesc = functionDesc;
		this.createUser = createUser;
		this.createDate = createDate;
		this.modifyUser = modifyUser;
		this.modifyDate = modifyDate;
		this.mSetFs = mSetFs;
		this.comAta = comAta;
	}




	public String getMsetId() {
		return msetId;
	}
	public void setMsetId(String msetId) {
		this.msetId = msetId;
	}
	public MMain getMMain() {
		return MMain;
	}
	public void setMMain(MMain mMain) {
		MMain = mMain;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getFunctionDesc() {
		return functionDesc;
	}
	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public Set getmSetFs() {
		return mSetFs;
	}

	public void setmSetFs(Set mSetFs) {
		this.mSetFs = mSetFs;
	}

	public ComAta getComAta() {
		return comAta;
	}

	public void setComAta(ComAta comAta) {
		this.comAta = comAta;
	}

}