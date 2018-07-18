package com.rskytech.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.basedata.bo.IComAtaBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComAuthority;
import com.rskytech.user.bo.IComAuthorityBo;
import com.rskytech.user.bo.IComProfessionBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComAuthorityAction extends BaseAction {
	
	private static final long serialVersionUID = 1196013312033795157L;

	private IComAtaBo comAtaBo;
	private IComAreaBo comAreaBo;
	private IComAuthorityBo comAuthorityBo;
	private IComProfessionBo comProfessionBo;
	private String nodeId;//节点ID（父类的ata或者area的id）
	private String analysisType;//四种分析类型
	private String choosedIds;//选中的ID
	private String choosedNoChilrdIds;//没有渲染子节点且选中的节点ID
	private String professionCode;//专业室编号（用于查询）
	private String professionId;//选中的专业室ID
	private String userCode;
	private String userName;
	private String userId;
	
	public String init(){
		return method;
	}
	
	/**
	 * 得到专业室列表（有翻页）
	* @Title: jsonLoadAllProfession
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月11日 上午10:28:58
	* @throws
	 */
	public String jsonLoadAllProfession(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProfessionBo.showProfessionList(page, professionCode, keyword, true);
		List lst=page1.getResult();
		
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("professionId", objs[0]);
				hm.put("professionCode", objs[1]);
				hm.put("professionName",  objs[2]);
				listJsonFV.add(hm);
			}
		}
		if(page1.getTotalCount()<page1.getPageSize()){
			page1.setTotalPages(1);
		}else{
			page1.setTotalPages(page1.getTotalCount()%page1.getPageSize()==0?page1.getTotalCount()/page1.getPageSize():page1.getTotalCount()/page1.getPageSize()+1);
		}
		page.setTotalPages(page1.getTotalPages());
		json.element("total", page1.getTotalCount());
		json.element("comProfession", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 专业室分配权限时，得到树
	* @Title: loadTreeForSelects
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月11日 上午10:29:46
	* @throws
	 */
	public String loadTreeForSelectsByProfessionId() {
		if(analysisType == null || "".equals(analysisType)){
			this.writeToResponse("false");
			return null;
		}
		//得到机型
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		// 获取专业组的权限
		Set<String> setForProfession = new HashSet<String>();
		List<ComAuthority> comAuthorityForProfessionList = new ArrayList<ComAuthority>();
		if(professionId != null && !"".equals(professionId)){
			comAuthorityForProfessionList = comAuthorityBo.findAuthorityByParam(modelSeriesId, professionId, analysisType);
			if (null != comAuthorityForProfessionList && comAuthorityForProfessionList.size() > 0) {
				for (ComAuthority comAuthority : comAuthorityForProfessionList) {
					setForProfession.add(comAuthority.getContent());
				}
			}
		}
		// 获取机型下所有权限
		Set<String> setForAll = new HashSet<String>();
		List<ComAuthority> comAuthorityForAllList = new ArrayList<ComAuthority>();
		if(professionId != null && !"".equals(professionId)){
			comAuthorityForAllList = comAuthorityBo.findAuthorityByParam(modelSeriesId, null, analysisType);
			if (null != comAuthorityForAllList && comAuthorityForAllList.size() > 0) {
				for (ComAuthority comAuthority : comAuthorityForAllList) {
					setForAll.add(comAuthority.getContent());
				}
			}
		}
		List<HashMap> listJsonFV = new ArrayList();
		if(analysisType.equals(ComacConstants.SYSTEM_CODE) || analysisType.equals(ComacConstants.STRUCTURE_CODE)){
			List<ComAta> comAtaList = comAtaBo.loadAtaListByParentId(modelSeriesId, nodeId);
			for(ComAta comAta : comAtaList){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("id", comAta.getAtaId());
				hm.put("text", comAta.getAtaCode());
				hm.put("aLevel", comAta.getAtaLevel());
				if (setForProfession.contains(comAta.getAtaId())) {
					hm.put("checked", true);
				} else {
					hm.put("checked", false);
				}
				if (setForAll.contains(comAta.getAtaId())) {
					hm.put("iconCls", "icon_gif_search");
				}
//				if (comAta.getAtaLevel() != null && comAta.getAtaLevel() == 4) {
//					hm.put("leaf", "true");
//				} else 
				if (comAtaBo.loadAtaListByParentId(modelSeriesId, comAta.getAtaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				//hm.put("leaf", "true");
				listJsonFV.add(hm);
			}
		}else if(analysisType.equals(ComacConstants.ZONAL_CODE) || analysisType.equals(ComacConstants.LHIRF_CODE)){
			List<ComArea> areaList = comAreaBo.loadAreaListByParentId(modelSeriesId, nodeId);
			for(ComArea comArea : areaList){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("id", comArea.getAreaId());
				hm.put("text", comArea.getAreaCode() + "-" + comArea.getAreaName());
				hm.put("aLevel", comArea.getAreaLevel());
				if (setForProfession.contains(comArea.getAreaId())) {
					hm.put("checked", true);
				} else {
					hm.put("checked", false);
				}
				if (setForAll.contains(comArea.getAreaId())) {
					hm.put("iconCls", "icon_gif_search");
				}
				if (comArea.getAreaLevel() != null && comArea.getAreaLevel() == 4) {
					hm.put("leaf", "true");
				} else if (comAreaBo.loadAreaListByParentId(modelSeriesId, comArea.getAreaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				//hm.put("leaf", "true");
				listJsonFV.add(hm);
			}
		}
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	
	/**
	 * 为专业室保存权限
	* @Title: updateTreeForSelectsByProfessionId
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月11日 下午1:49:33
	* @throws
	 */
	public String updateTreeForSelectsByProfessionId() {
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = comAuthorityBo.updateAuthorityForProfession(modelSeriesId, professionId, analysisType, choosedIds,choosedNoChilrdIds,getSysUser().getUserId());
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}

	/**
	 * 根据用户ID得到可以维护的专业组
	* @Title: jsonLoadProfessionByUserId
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 上午9:32:13
	* @throws
	 */
	public String jsonLoadProfessionByUserId(){
		List<HashMap> listJsonFV = new ArrayList();
		List lst = null;
		//管理员--得到所有的专业组
		if(getSysUser().isAdmin()){
			lst = comProfessionBo.getAllProsession(professionCode, keyword, true);
		}else{
			lst = comProfessionBo.getProsessionByUserId(getSysUser().getUserId());
		}
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("professionId", objs[0]);
				hm.put("professionName",  objs[2]);
				listJsonFV.add(hm);
			}
		}
		JSONObject json = new JSONObject();
		json.put("ownProfession", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 根据专业组id，得到已存在的用户
	* @Title: jsonLoadUserByProfessonId
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 上午9:34:09
	* @throws
	 */
	public String jsonLoadUserByProfessonId(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProfessionBo.getUserByProfessonIdForAuth(page,this.professionId, this.userCode, this.userName);
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", objs[0]);
				hm.put("userCode",  objs[1]);
				hm.put("userName",  objs[2]);
				hm.put("positionNames",  objs[3]);
				listJsonFV.add(hm);
			}
		}

		JSONObject json = new JSONObject();
		if(page1.getTotalCount()<page1.getPageSize()){
			page1.setTotalPages(1);
		}else{
			page1.setTotalPages(page1.getTotalCount()%page1.getPageSize()==0?page1.getTotalCount()/page1.getPageSize():page1.getTotalCount()/page1.getPageSize()+1);
		}
		page.setTotalPages(page1.getTotalPages());
		json.element("total", page1.getTotalCount());
		json.element("comUsers", listJsonFV);
		json.put("comUsers", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}


	/**
	 * 专业室分配权限时，得到树
	* @Title: loadTreeForSelects
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月11日 上午10:29:46
	* @throws
	 */
	public String loadTreeForSelectsByUserId() {
		if(analysisType == null || "".equals(analysisType)){
			this.writeToResponse("false");
			return null;
		}
		//得到机型
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		//获取专业组的权限
		Set<String> professionContntsSet = new HashSet<String>();
		List<ComAuthority> comAuthorityList = new ArrayList<ComAuthority>();
		if(professionId != null && !"".equals(professionId)){
			comAuthorityList = comAuthorityBo.findAuthorityByParam(modelSeriesId, professionId, analysisType);
			if (null != comAuthorityList && comAuthorityList.size() > 0) {
				for (ComAuthority comAuthority : comAuthorityList) {
					professionContntsSet.add(comAuthority.getContent());
				}
			}
		}
		// 获取用户的权限
		Set<String> userContentsSet = new HashSet<String>();
		List userContentsLists = comAuthorityBo.getContentsByProsessionIdAndUserID(modelSeriesId, professionId, userId, analysisType);
		if (null != userContentsLists && userContentsLists.size() > 0) {
			for (Object obj : userContentsLists) {
				Object []objs=(Object[])obj;
				userContentsSet.add(String.valueOf(objs[1]));
			}
		}
		// 获取专业室下已分配的所有权限
		Set<String> allContentsForProfessionSet = new HashSet<String>();
		List allContentsForProfessionLists = comAuthorityBo.getContentsByProsessionIdAndUserID(modelSeriesId, professionId, null, analysisType);
		if (null != allContentsForProfessionLists && allContentsForProfessionLists.size() > 0) {
			for (Object obj : allContentsForProfessionLists) {
				Object []objs=(Object[])obj;
				allContentsForProfessionSet.add(String.valueOf(objs[1]));
			}
		}
		List<HashMap> listJsonFV = new ArrayList();
		if(analysisType.equals(ComacConstants.SYSTEM_CODE) || analysisType.equals(ComacConstants.STRUCTURE_CODE)){
			List<ComAta> comAtaList = comAtaBo.loadAtaListByParentId(modelSeriesId, nodeId);
			for(ComAta comAta : comAtaList){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("id", comAta.getAtaId());
				hm.put("text", comAta.getAtaCode());
				hm.put("aLevel", comAta.getAtaLevel());
				if (professionContntsSet.contains(comAta.getAtaId())) {
					if (userContentsSet.contains(comAta.getAtaId())) {
						hm.put("checked", true);
					} else {
						//if(comAta.getAtaLevel() != null && 1 != comAta.getAtaLevel().intValue()){
							hm.put("checked", false);
						//}
					}
					if (allContentsForProfessionSet.contains(comAta.getAtaId())) {
						hm.put("iconCls", "icon_gif_search");
					}
				}
//				if (comAta.getAtaLevel() != null && comAta.getAtaLevel() == 4) {
//					hm.put("leaf", "true");
//				} else 
				if (comAtaBo.loadAtaListByParentId(modelSeriesId, comAta.getAtaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				//hm.put("leaf", "true");
				listJsonFV.add(hm);
			}
		}else if(analysisType.equals(ComacConstants.ZONAL_CODE) || analysisType.equals(ComacConstants.LHIRF_CODE)){
			List<ComArea> areaList = comAreaBo.loadAreaListByParentId(modelSeriesId, nodeId);
			for(ComArea comArea : areaList){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("id", comArea.getAreaId());
				hm.put("text", comArea.getAreaCode() + "-" + comArea.getAreaName());
				hm.put("aLevel", comArea.getAreaLevel());
				if (professionContntsSet.contains(comArea.getAreaId())) {
					if (userContentsSet.contains(comArea.getAreaId())) {
						hm.put("checked", true);
					} else {
						//if(comArea.getAreaLevel() != null && 1 != comArea.getAreaLevel().intValue()){
							hm.put("checked", false);
						//}
					}
					if (allContentsForProfessionSet.contains(comArea.getAreaId())) {
						hm.put("iconCls", "icon_gif_search");
					}
				}
				if (comArea.getAreaLevel() != null && comArea.getAreaLevel() == 4) {
					hm.put("leaf", "true");
				} else if (comAreaBo.loadAreaListByParentId(modelSeriesId, comArea.getAreaId()).size() == 0) {
					hm.put("leaf", "true");
				}
				//hm.put("leaf", "true");
				listJsonFV.add(hm);
			}
		}
		String jsonStr = JSONArray.fromObject(listJsonFV).toString();
		this.writeToResponse(jsonStr);
		return null;
	}
	
	/**
	 * 为专业室保存权限
	* @Title: updateTreeForSelectsByProfessionId
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月11日 下午1:49:33
	* @throws
	 */
	public String updateTreeForSelectsByUserId() {
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = comAuthorityBo.updateAuthorityForUser(modelSeriesId, professionId, userId, analysisType, choosedIds,choosedNoChilrdIds, getSysUser().getUserId());
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}

	/**
	 * 判断树上是否已选中--专业室权限
	* @Title: getTreeIsCheckBeforeLoad
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月26日 上午10:06:54
	* @throws
	 */
	public String getTreeIsCheckBeforeLoad(){
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = comAuthorityBo.getTreeIsCheckBeforeLoad(modelSeriesId, professionId, analysisType);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}

	/**
	 * 判断树上是否已选中--用户权限
	* @Title: getTreeIsCheckBeforeLoadForUser
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月26日 下午1:26:25
	* @throws
	 */
	public String getTreeIsCheckBeforeLoadForUser(){
		JSONObject json = new JSONObject();
		String modelSeriesId = this.getComModelSeries().getModelSeriesId();
		boolean flag = comAuthorityBo.getTreeIsCheckBeforeLoadForUser(modelSeriesId, professionId, analysisType, userId);
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	public IComAtaBo getComAtaBo() {
		return comAtaBo;
	}
	public void setComAtaBo(IComAtaBo comAtaBo) {
		this.comAtaBo = comAtaBo;
	}
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public IComAuthorityBo getComAuthorityBo() {
		return comAuthorityBo;
	}
	public void setComAuthorityBo(IComAuthorityBo comAuthorityBo) {
		this.comAuthorityBo = comAuthorityBo;
	}
	public IComProfessionBo getComProfessionBo() {
		return comProfessionBo;
	}
	public void setComProfessionBo(IComProfessionBo comProfessionBo) {
		this.comProfessionBo = comProfessionBo;
	}
	
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getProfessionId() {
		return professionId;
	}

	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}

	public String getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	public String getChoosedIds() {
		return choosedIds;
	}
	public void setChoosedIds(String choosedIds) {
		this.choosedIds = choosedIds;
	}
	public String getProfessionCode() {
		return professionCode;
	}
	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChoosedNoChilrdIds() {
		return choosedNoChilrdIds;
	}

	public void setChoosedNoChilrdIds(String choosedNoChilrdIds) {
		this.choosedNoChilrdIds = choosedNoChilrdIds;
	}
	
}
