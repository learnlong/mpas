package com.rskytech.lhirf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public class LhSearchHsiAction extends BaseAction {

private static final long serialVersionUID = -1217361674340176361L;
private ILhMainBo lhMainBo;
private String parentAreaId ;
private String parentNodeOneId;
private String parentNodeTwoId;
private String parentNodeThreeId;
 private Integer areaLevel;

	public String init(){
			ComUser thisUser = this.getSysUser();
			if ( null == thisUser) {
				return SUCCESS;
			}
		return SUCCESS;
	}
	
	
@SuppressWarnings({ "unchecked", "rawtypes" })
  public String sesrchAreaNodeList(){
	  List<ComArea> areaList  = this.lhMainBo.getAreaNodeList(this.getComModelSeries().getModelSeriesId(), parentAreaId,areaLevel);
	  List<HashMap> listJsonFV = new ArrayList();
	  if(areaList !=null ){
		  for (ComArea comarea :areaList) {
			  //第一节点
			  HashMap map = new HashMap();
				map.put("nodeId",  comarea.getAreaId());
				map.put("nodeCode",  comarea.getAreaCode()+"-"+comarea.getAreaName());
				listJsonFV.add(map);
			}
		  }
	   JSONObject json = new JSONObject();
	   json.put("nodeSearchList", listJsonFV);
	   writeToResponse(json.toString());
	   return null;
  }
	
	
///查询 HSI 详细信息
	@SuppressWarnings({ "unchecked", "rawtypes" })
  public String getSearchHsiList(){
		  if (this.getPage() == null)
				this.setPage(new com.richong.arch.web.Page());
			this.getPage().setStartIndex(getStart());
			if (getLimit() > 0) {
				this.getPage().setPageSize(getLimit());
			}
			JSONObject json = new JSONObject();
			List<HashMap> listJsonFV = new ArrayList<HashMap>();
			List lhList = this.lhMainBo.getLhHsiListByAreaId(this.getComModelSeries().getModelSeriesId(), parentNodeOneId, parentNodeTwoId, parentNodeThreeId, page);
			if(lhList != null){
				for (int i = 0; i < lhList.size(); i++){
					Object[] ob = (Object[]) lhList.get(i);
					HashMap map = new HashMap();
					map.put("hsiId",  ob[0].toString());
					map.put("hsiCode",  ob[1]);
					map.put("hsiName",  ob[2]);
					map.put("lhCompName",  ob[3]);
					map.put("ataCode",  ob[4]);
					map.put("ipvOpvpOpve", ob[5]);
					map.put("refHsiCode", ob[6]);
					map.put("status",  ob[7]);
					if(ob[8] != null){
						if(!BasicTypeUtils.isNullorBlank(ob[8].toString())){
							ComUser user =(ComUser) this.lhMainBo.loadById(ComUser.class, ob[8].toString());
							if(user != null){
								map.put("anaUserName",  user.getUserName());
							}
						}
					}else{
						map.put("anaUserName",  "");
					}
					if(!BasicTypeUtils.isNullorBlank(ob[9].toString())){
						ComModelSeries comModel =(ComModelSeries) this.lhMainBo.loadById(ComModelSeries.class, ob[9].toString());
						if(comModel != null){
							map.put("modelName",  comModel.getModelSeriesName());
						}
					}else{
						map.put("modelName",  "");
					}
					map.put("areaCode",  ob[11]);
					listJsonFV.add(map);
				}
			}
			json.element("total", this.getPage().getTotalCount());
			json.element("lhsearch", listJsonFV);
			 writeToResponse(json.toString());
			return null;
	  }

	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}

	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}

	public String getParentAreaId() {
		return parentAreaId;
	}

	public void setParentAreaId(String parentAreaId) {
		this.parentAreaId = parentAreaId;
	}

	public String getParentNodeOneId() {
		return parentNodeOneId;
	}

	public void setParentNodeOneId(String parentNodeOneId) {
		this.parentNodeOneId = parentNodeOneId;
	}

	public String getParentNodeTwoId() {
		return parentNodeTwoId;
	}

	public void setParentNodeTwoId(String parentNodeTwoId) {
		this.parentNodeTwoId = parentNodeTwoId;
	}

	public String getParentNodeThreeId() {
		return parentNodeThreeId;
	}

	public void setParentNodeThreeId(String parentNodeThreeId) {
		this.parentNodeThreeId = parentNodeThreeId;
	}

	public Integer getAreaLevel() {
		return areaLevel;
	}

	public void setAreaLevel(Integer areaLevel) {
		this.areaLevel = areaLevel;
	}
}
