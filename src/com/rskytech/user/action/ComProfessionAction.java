package com.rskytech.user.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComProfession;
import com.rskytech.user.bo.IComProfessionBo;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ComProfessionAction extends BaseAction {
	private static final long serialVersionUID = 7825421712346268970L;
	
	private IComProfessionBo comProfessionBo;
	private String professionId;
	private String professionCode;
	private String analysisType;
	private String choosedIds;
	private String userCode;
	private String userName;
	private String userId;
	private String positionId;
	private String insertOrDelteFlag;

	public String init() {
		return method;
	}
	
	/**
	 * 查询专业组列表
	* @Title: showProfessionList
	* @Description:
	* @return
	* @throws IOException
	* @author samual
	* @date 2014年11月7日 上午9:30:41
	* @throws
	 */
	public String showProfessionList() throws IOException {
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProfessionBo.showProfessionList(page, professionCode, keyword, false);
		List lst=page1.getResult();
		
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("professionId", objs[0]);
				hm.put("professionCode", objs[1]);
				hm.put("professionName",  objs[2]);
				hm.put("validFlag",  objs[3]);
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
	 * 批量修改专业组，包括新增和修改
	* @Title: jsonPofessionUpdate
	* @Description:
	* @return
	* @throws Exception
	* @author samual
	* @date 2014年11月7日 上午9:31:23
	* @throws
	 */
	public String jsonPofessionUpdate() throws Exception {
		JSONObject json = new JSONObject();
		String jsonData = this.getJsonData();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		System.out.println(jsonArray.size());
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String professionId = jsonObject.getString("professionId");
			String professionCode = jsonObject.getString("professionCode");
			String professionName = jsonObject.getString("professionName");

			ComProfession comProfession;
			List<ComProfession> comProfessionList =  null;
			//判断是存在code
			DetachedCriteria dc = DetachedCriteria.forClass(ComProfession.class); // 检查用户编号是否存在(新增数据)
			dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
			dc.add(Restrictions.eq("professionCode", professionCode));
			comProfessionList = this.comProfessionBo.findByCritera(dc);
			// db操作区分
			String dbOperate = "";
			// 修改操作时
			if (!BasicTypeUtils.isNullorBlank(professionId)) {// && !"0".equals(id)) {
				//修改判断是存在codex
				if (comProfessionList.size() == 1 && !comProfessionList.get(0).getProfessionId().equals(professionId)) {
					json.put("success", "exits");
					writeToResponse(json.toString());
					return null;
				}
				dbOperate = ComacConstants.DB_UPDATE;
				comProfession = (ComProfession) comProfessionBo.loadById(ComProfession.class, professionId);
			} else {// 追加操作时
				//新增判断是存在code
				if (comProfessionList.size() > 0) {
					json.put("success", "exits");
					writeToResponse(json.toString());
					return null;
				}
				dbOperate = ComacConstants.DB_INSERT;
				comProfession = new ComProfession();
				comProfession.setValidFlag(Integer.parseInt(jsonObject.getString("validFlag")));
			}
			comProfession.setProfessionCode(professionCode);
			comProfession.setProfessionName(professionName);
			comProfessionBo.saveOrUpdate(comProfession, dbOperate, getSysUser().getUserId());
		}

		json.put("success", true);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 删除专业室
	* @Title: delComProfessionById
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 上午9:31:49
	* @throws
	 */
	public String delComProfessionById() {
		if (this.professionId != null && !"".equals(this.professionId)) {
			//id=1默认为综保部
			if (professionId.equals(ComacConstants.PROEFSSION_ID_ZONGBAO)) {
				writeToResponse(professionId);
				return null;
			}
			ComProfession comProfession = (ComProfession) comProfessionBo.loadById(ComProfession.class, this.professionId);
			if (comProfession != null) {
				comProfession.setValidFlag(ComacConstants.VALIDFLAG_NO);
				comProfessionBo.saveOrUpdate(comProfession, ComacConstants.DB_DELETE, getSysUser().getUserId());
			}
		}
		JSONObject json = new JSONObject();
		json.element("success", true);
		this.writeToResponse(json.toString());
		return null;
	}
	
	public String jsonCheckProfessionCode(){
		boolean flag = comProfessionBo.checkProfessionIsExist(professionId, professionCode);
		if (flag) {
			writeToResponse("true");
			return null;
		} else {
			writeToResponse("false");
			return null;
		}
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
		//管理员--得到所有的专业组
		List lst = null;
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
		Page page1 = comProfessionBo.getUserByProfessonId(page,this.professionId, this.userCode, this.userName);
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", objs[0]);
				hm.put("userCode",  objs[1]);
				hm.put("userName",  objs[2]);
				hm.put("positionAdmin",  objs[3]);
				hm.put("positionEngineer",  objs[4]);
				hm.put("positionAnalyst",  objs[5]);
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
	 * 根据专业组得到不存在的用户
	* @Title: jsonLoadUserByProfessonIdOther
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 上午9:34:51
	* @throws
	 */
	public String jsonLoadUserByProfessonIdOther(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProfessionBo.getOtherUserByProfessonId(page,this.professionId, this.userCode, this.userName);
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", objs[0]);
				hm.put("userCode",  objs[1]);
				hm.put("userName",  objs[2]);
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
	
	public String jsonUpdatePositionByParm(){
		JSONObject json = new JSONObject();
		if(insertOrDelteFlag == null || "".equals(insertOrDelteFlag) || userId == null || "".equals(userId) || positionId == null || "".equals(positionId) || professionId == null || "".equals(professionId)){
			json.element("success", false);
		}else{
			if("true".equals(insertOrDelteFlag)){//插入
				comProfessionBo.insertUserPositionRel(userId, positionId, professionId, getSysUser().getUserId());
			}else{//删除
				comProfessionBo.deleteUserPositionRel(userId, positionId, professionId, getSysUser().getUserId());
			}	
			json.element("success", true);
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 删除专业组中已有的用户
	* @Title: jsonDelUserInPofession
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 下午2:13:51
	* @throws
	 */
	public String jsonDelUserInPofession(){
		JSONObject json = new JSONObject();
		if(userId == null || "".equals(userId) || professionId == null || "".equals(professionId)){
			json.element("success", false);
		}else{
			comProfessionBo.delUserInProfession(userId, professionId, getSysUser().getUserId());
			json.element("success", true);
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 给专业组添加用户
	* @Title: jsonAddUserToPofession
	* @Description:
	* @return
	* @author samual
	* @date 2014年11月7日 下午2:14:45
	* @throws
	 */
	public String jsonAddUserToPofession(){
		JSONObject json = new JSONObject();
		if(userId == null || "".equals(userId) || professionId == null || "".equals(professionId)){
			json.element("success", false);
		}else{
			comProfessionBo.addUserInProfession(userId, professionId, getSysUser().getUserId());
			json.element("success", true);
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	public IComProfessionBo getComProfessionBo() {
		return comProfessionBo;
	}

	public void setComProfessionBo(IComProfessionBo comProfessionBo) {
		this.comProfessionBo = comProfessionBo;
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

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getInsertOrDelteFlag() {
		return insertOrDelteFlag;
	}

	public void setInsertOrDelteFlag(String insertOrDelteFlag) {
		this.insertOrDelteFlag = insertOrDelteFlag;
	}

	public String getProfessionCode() {
		return professionCode;
	}

	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}
	
}
