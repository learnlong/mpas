package com.rskytech.basedata.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComModelSeriesBo;
import com.rskytech.login.bo.ILoginBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class ComModelSeriesAction extends BaseAction {

	private static final long serialVersionUID = -5186143713716884500L;

	private IComModelSeriesBo comModelSeriesBo;
	
	private String msId;
	private String msCode;
	private String msName;
	private ILoginBo loginBo;
	private String needCopy;
	
	public String init(){
		return SUCCESS;
	}
	
	//查询页面显示的机型系列数据
	@SuppressWarnings("unchecked")
	public String loadModelSeriesList(){
		if (this.getPage() == null) 
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		
		List<HashMap> listJsonFV = comModelSeriesBo.loadModelSeriesList(msCode, msName, page);
		
		JSONObject json = new JSONObject();
		json.element("total", this.getPage().getTotalCount());
		json.element("ComModelSeries", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	//验证机型编号是否已经存在
	public String checkModelSeries(){
		boolean flag = comModelSeriesBo.checkModelSeries(msCode, msId);
		if (flag){		
			writeToResponse("true");
		} else {
			writeToResponse("false");
		}	
		return null;
	}
	
	//新增和修改机型
	public String updateModelSeries(){
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		
		ComUser user = getSysUser();
		ComModelSeries ms = getComModelSeries();
		//新增和修改操作
		if (ComacConstants.DB_UPDATE.equals(this.getMethod())) {
			String msg = comModelSeriesBo.newOrUpdateMs(user, ms, jsonData);
			
			//将默认自定义数据copy到当前机型
			if(this.getNeedCopy()!=null&&"yes".equals(this.getNeedCopy())){
				JSONArray jsonArray = JSONArray.fromObject(jsonData);
				String modelSeriesCode;
				for (int i = 0; i < jsonArray.size(); i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id = jsonObject.getString("modelSeriesId");;
					if(BasicTypeUtils.isNullorBlank(id)){
						modelSeriesCode = jsonArray.getJSONObject(i).getString("modelSeriesCode").trim();
						ComModelSeries cm = comModelSeriesBo.getMsByMsCode(modelSeriesCode);
						if(cm!=null){
							comModelSeriesBo.copyDefaultCustomData(cm.getModelSeriesId(), this.getSysUser().getUserId());
						}
					}
				}
			}
			List<HashMap<String,String>> listJsonFV = new ArrayList<HashMap<String,String>>();
			if(msg.equals("success")){
				List<ComModelSeries> msList = loginBo.searchModelSeries();
				ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_MODEL_SERIES_LIST, msList);
				for(ComModelSeries cm : msList){
					HashMap<String,String> hm = new HashMap<String,String>();
					hm.put("msCode", cm.getModelSeriesCode());
					hm.put("msId", cm.getModelSeriesId());
					listJsonFV.add(hm);
				}
			}
			json.put("msg", msg);
			json.element("ms", listJsonFV);
			writeToResponse(json.toString());
			return null;
		}

		//删除操作
		if (ComacConstants.DB_DELETE.equals(this.getMethod())) {
			if (msId != null && !"".equals(msId)) {
				ComModelSeries comModelSeries = (ComModelSeries) comModelSeriesBo.loadById(ComModelSeries.class, msId);	
				if (comModelSeries != null){
					if (ms.getModelSeriesCode().equals(comModelSeries.getModelSeriesCode())){
						json.put("msg", "nowMs");
						writeToResponse(json.toString());
						return null;
					}
					
					if (comModelSeries.getDefaultModelSeries() == 1){
						json.put("msg", "defaultMs");
						writeToResponse(json.toString());
						return null;
					}
					
//					boolean bool = comModelSeriesBo.deleteModelSeries(comModelSeries.getModelSeriesId());
					comModelSeries.setValidFlag(0);
					comModelSeriesBo.update(comModelSeries,getSysUser().getUserId());
					List<HashMap<String,String>> listJsonFV = new ArrayList<HashMap<String,String>>();
					List<ComModelSeries> msList = loginBo.searchModelSeries();
					ServletActionContext.getContext().getSession().put(ComacConstants.SESSION_MODEL_SERIES_LIST, msList);
					for(ComModelSeries cm : msList){
						HashMap<String,String> hm = new HashMap<String,String>();
						hm.put("msCode", cm.getModelSeriesCode());
						hm.put("msId", cm.getModelSeriesId());
						listJsonFV.add(hm);
					}
					json.element("ms", listJsonFV);
					boolean bool = true;
					
					if (bool){
						json.put("msg", "true");
						writeToResponse(json.toString());
						return null;
					} else {
						json.put("msg", "false");
						writeToResponse(json.toString());
						return null;
					}
				} else {
					json.put("msg", "false");
					writeToResponse(json.toString());
					return null;
				}
			}
		}
		return null;
	}
	
	//默认机型
	public String defaultModelSeries(){
		String msg = comModelSeriesBo.defaultModelSeries(getSysUser(), msId);
		
		JSONObject json = new JSONObject();
		
		json.put("msg", msg);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到机型系列名称
	 * @return
	 */
	public String showModelList(){
		List<HashMap<String, String>> listJsonFV = new ArrayList<HashMap<String, String>>();
		List<ComModelSeries> comModelSeriesList = loginBo.searchModelSeries();
		for (ComModelSeries comModelSeries : comModelSeriesList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("msId", comModelSeries.getModelSeriesId());
			map.put("modelName", comModelSeries.getModelSeriesName());
			listJsonFV.add(map);
		}
		JSONObject json = new JSONObject();
		json.put("comModelSeries", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public IComModelSeriesBo getComModelSeriesBo() {
		return comModelSeriesBo;
	}

	public void setComModelSeriesBo(IComModelSeriesBo comModelSeriesBo) {
		this.comModelSeriesBo = comModelSeriesBo;
	}

	public String getMsCode() {
		return msCode;
	}

	public void setMsCode(String msCode) {
		this.msCode = msCode;
	}

	public String getMsName() {
		return msName;
	}

	public void setMsName(String msName) {
		this.msName = msName;
	}

	public String getMsId() {
		return msId;
	}

	public void setMsId(String msId) {
		this.msId = msId;
	}

	public ILoginBo getLoginBo() {
		return loginBo;
	}

	public void setLoginBo(ILoginBo loginBo) {
		this.loginBo = loginBo;
	}

	public String getNeedCopy() {
		return needCopy;
	}

	public void setNeedCopy(String needCopy) {
		this.needCopy = needCopy;
	}
	
}
