package com.rskytech.basedata.action;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComMmelAction extends BaseAction {

	private static final long serialVersionUID = 5474456732843779631L;

	private IComMmelBo comMmelBo;
	
	private String mmelId;
	
	public String init(){
		return SUCCESS;
	}
	
	//查询MMEL列表信息
	@SuppressWarnings("unchecked")
	public String loadMmel(){
		String msId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = comMmelBo.loadMmelList(msId);
		
		JSONObject json = new JSONObject();
		json.element("ComMmel", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	//新增、删除、保存MMEL
	public String saveMmel(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			String msg = comMmelBo.newOrUpdateMmel(user, ms, jsonData);
			
			json.put("msg", msg);
			writeToResponse(json.toString());
			return null;
		}
		
		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (mmelId != null && !"".equals(mmelId)) {
				ComMmel cm = (ComMmel) comMmelBo.loadById(ComMmel.class, mmelId);	
				if (cm != null){
					cm.setValidFlag(ComacConstants.VALIDFLAG_NO);
					comMmelBo.saveOrUpdate(cm, ComacConstants.DB_DELETE, user.getUserId());

					json.put("msg", "true");
					writeToResponse(json.toString());
					return null;
				} else {
					json.put("msg", "false");
					writeToResponse(json.toString());
					return null;
				}
			}
		}
		return null;
	}

	public IComMmelBo getComMmelBo() {
		return comMmelBo;
	}

	public void setComMmelBo(IComMmelBo comMmelBo) {
		this.comMmelBo = comMmelBo;
	}

	public String getMmelId() {
		return mmelId;
	}

	public void setMmelId(String mmelId) {
		this.mmelId = mmelId;
	}
	
}
