package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.MSelect;
import com.rskytech.sys.bo.IMsiSelectBo;
@SuppressWarnings({ "unchecked" })
public class QueryIsMsiAction extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
     private IMsiSelectBo msiSelectBo;
     private String ataName;
     private String isNoMsi;
     public String init(){
    	 return SUCCESS;
     }
     
	@SuppressWarnings("rawtypes")
	public String loadMsi() {
		JSONObject json = new JSONObject();
		List<HashMap> listJson = new ArrayList<HashMap>();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		List<MSelect> list = this.msiSelectBo.getListMSelectByModelSeriesId(modelSeriesId,ataName,isNoMsi);
		HashMap jsonFeildList;
		if(list.size()>0){
			for (MSelect select : list) {
				jsonFeildList = new HashMap();
				jsonFeildList.put("id",select.getSelectId());
				ComAta comAta = (ComAta)this.msiSelectBo.loadById(ComAta.class,select.getComAta().getAtaId());
				jsonFeildList.put("proCode",comAta.getAtaCode());
				jsonFeildList.put("proName",comAta.getAtaName() );
				jsonFeildList.put("safetyAnswer", select.getSafetyAnswer());
				jsonFeildList.put("detectableAnswer", select.getDetectableAnswer());
				jsonFeildList.put("economicAnswer", select.getEconomicAnswer());
				jsonFeildList.put("isMsi",select.getIsMsi());
				jsonFeildList.put("highestLevel",select.getHighestLevel());
				jsonFeildList.put("remark",select.getRemark() );
				listJson.add(jsonFeildList);
			}
		}
		json.element("msi",listJson);
		writeToResponse(json.toString());
		return null;
	}
	public IMsiSelectBo getMsiSelectBo() {
		return msiSelectBo;
	}
	public void setMsiSelectBo(IMsiSelectBo msiSelectBo) {
		this.msiSelectBo = msiSelectBo;
	}

	public String getIsNoMsi() {
		return isNoMsi;
	}
	public void setIsNoMsi(String isNoMsi) {
		this.isNoMsi = isNoMsi;
	}
	public String getAtaName() {
		return ataName;
	}
	public void setAtaName(String ataName) {
		this.ataName = ataName;
	}
	
	
	

}
