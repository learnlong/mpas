package com.rskytech.struct.action;



import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.struct.bo.ISsiSelectBo;

public class SsiSearchAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1667771158967815307L;
	private ISsiSelectBo ssiSelectBo;
	private Integer isSsi;
	private Integer isOwn;
	private String ssiName;
	public String init(){
		
		return SUCCESS;
	}
	

	public String getSsiName() {
		return ssiName;
	}

	public void setSsiName(String ssiName) {
		this.ssiName = ssiName;
	}
	
	public String getRecords(){
		 String model=getComModelSeries().getModelSeriesId();
		JSONObject json=this.ssiSelectBo.getSsiRecords(isSsi,isOwn,ssiName,model,start,limit);
		writeToResponse(json.toString());
		return null;
	}


	public ISsiSelectBo getSsiSelectBo() {
		return ssiSelectBo;
	}


	public void setSsiSelectBo(ISsiSelectBo ssiSelectBo) {
		this.ssiSelectBo = ssiSelectBo;
	}


	public Integer getIsSsi() {
		return isSsi;
	}


	public void setIsSsi(Integer isSsi) {
		this.isSsi = isSsi;
	}


	public Integer getIsOwn() {
		return isOwn;
	}


	public void setIsOwn(Integer isOwn) {
		this.isOwn = isOwn;
	}
	
}
