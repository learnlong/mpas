package com.rskytech.process.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.richong.arch.action.BaseAction;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComProcess;
import com.rskytech.pojo.ComUser;
import com.rskytech.process.bo.IComProcessBo;
import com.rskytech.process.bo.IComProcessDetailBo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ComProcessAction extends BaseAction{

	private static final long serialVersionUID = 8932051685689984755L;
	private IComProcessBo comProcessBo;
	private IComProcessDetailBo comProcessDetailBo;
	private String professionId;//专业室id
	private String analysisType;//分析类型
	private String checkUser;//审核人员
	private String checkOpinion;//审批意见
	private String choosedIds;//选中的分析id
	private String processStatus;//审批流程状态
	private String launchUserName;//发起人
	private String fromDate;//发起开始时间
	private String toDate;//发起结束时间
	private String processId;//流程id
	private String isOkType;//选中是否通过
	private String isOkCheckpinion;//审核意见
	private String checkUserName;//审核人
	
	public String init(){
		return method;
	}

	/**
	 * 得到分析类型
	* @Title: getAnalysisTypeByuserId
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月2日 下午3:01:00
	* @throws
	 */
	public String getAnalysisTypeByuserId(){
//		List<HashMap> listJsonFV = new ArrayList();
		List lst = null;
		//管理员--得到所有的专业组
		lst = comProcessBo.getAnalysisTypeByuserId(getComModelSeries().getModelSeriesId(), getSysUser().getUserId());
//		if (lst != null) {
//			for (Object obj : lst) {
//				HashMap<String, Object> hm = new HashMap<String, Object>();
//				hm.put("analysisCode", obj);
//				hm.put("analysisName", this.getAnalysisNameByCode(String.valueOf(obj)));
//				listJsonFV.add(hm);
//			}
//		}
		JSONObject json = new JSONObject();
		json.put("analysisType", this.getAnalysisTypeCodeAndName(lst));
		writeToResponse(json.toString());
		return null;
	}
	
	private List getAnalysisTypeCodeAndName(List lst){
		List<HashMap> listJsonFV = new ArrayList();;
		if(lst == null || lst.size() == 0){
			return listJsonFV;
		}
		for (Object obj : lst) {//系统
			if(obj != null && ComacConstants.SYSTEM_CODE.equals(String.valueOf(obj))){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("analysisCode", ComacConstants.SYSTEM_CODE);
				hm.put("analysisName", ComacConstants.SYSTEM_SHOW);
				listJsonFV.add(hm);
				break;
			}
		}
		for (Object obj : lst) {//结构
			if(obj != null && ComacConstants.STRUCTURE_CODE.equals(String.valueOf(obj))){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("analysisCode", ComacConstants.STRUCTURE_CODE);
				hm.put("analysisName", ComacConstants.STRUCTURE_SHOW);
				listJsonFV.add(hm);
				break;
			}
		}
		for (Object obj : lst) {//区域
			if(obj != null && ComacConstants.STRUCTURE_CODE.equals(String.valueOf(obj))){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("analysisCode", ComacConstants.ZONAL_CODE);
				hm.put("analysisName", ComacConstants.ZONAL_SHOW);
				listJsonFV.add(hm);
				break;
			}
		}
		for (Object obj : lst) {//LHRIF
			if(obj != null && ComacConstants.LHIRF_CODE.equals(String.valueOf(obj))){
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("analysisCode", ComacConstants.LHIRF_CODE);
				hm.put("analysisName", ComacConstants.LHIRF_SHOW);
				listJsonFV.add(hm);
				break;
			}
		}
		return listJsonFV;
	}
	
	/**
	 * 根据分析类型code得到name
	* @Title: getAnalysisNameByCode
	* @Description:
	* @param analysisCode
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:11:35
	* @throws
	 */
	@SuppressWarnings("unused")
	private String getAnalysisNameByCode(String analysisCode){
		if(analysisCode == null){
			return "";
		}else if(ComacConstants.SYSTEM_CODE.equals(analysisCode)){
			return ComacConstants.SYSTEM_SHOW;
		}else if(ComacConstants.STRUCTURE_CODE.equals(analysisCode)){
			return ComacConstants.STRUCTURE_SHOW;
		}else if(ComacConstants.LHIRF_CODE.equals(analysisCode)){
			return ComacConstants.LHIRF_SHOW;
		}else if(ComacConstants.ZONAL_CODE.equals(analysisCode)){
			return ComacConstants.ZONAL_SHOW;
		}else {
			return analysisCode;
		}
	}
	
	/**
	 * 得到专业室
	* @Title: getProfessionForAnalysisByuserId
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月2日 下午3:00:21
	* @throws
	 */
	public String getProfessionForAnalysisByuserId(){
		List<HashMap> listJsonFV = new ArrayList();
		List lst = null;
		//管理员--得到所有的专业组
		lst = comProcessBo.getProfessionForAnalysisByuserId(getComModelSeries().getModelSeriesId(), analysisType, getSysUser().getUserId());
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
		json.put("profession", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到部门的审核人员
	* @Title: getCheckUserByProfessionId
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月2日 下午3:44:33
	* @throws
	 */
	public String getCheckUserByProfessionId(){
		List<HashMap> listJsonFV = new ArrayList();
		List lst = null;
		//管理员--得到所有的专业组
		lst = comProcessBo.getCheckUserByProfessionId(professionId);
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("userId", objs[0]);
				hm.put("userName",  objs[1]);
				listJsonFV.add(hm);
			}
		}
		JSONObject json = new JSONObject();
		json.put("checkUser", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 得到分析完成的分析，用来发起审批
	* @Title: getAnalysisOver
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月3日 上午10:43:02
	* @throws
	 */
	public String getAnalysisOver(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProcessBo.getAnalysisOver(page, this.getComModelSeries().getModelSeriesId(), analysisType, getSysUser().getUserId());
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("mainId", objs[0]);
				if(ComacConstants.STRUCTURE_CODE.equals(analysisType) && "2".equals(String.valueOf(objs[6]))){
					hm.put("analysisCode",  "Q"+String.valueOf(objs[1]));//结构的非SSi加Q
				}else {
					hm.put("analysisCode",  objs[1]);
				}
				hm.put("analysisName",  objs[2]);
				hm.put("status",  getAnalysisStatusNameBycode(String.valueOf(objs[3])));
				hm.put("endDate",  objs[4]);
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
		json.element("analysis", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 根据状态code得到name
	* @Title: getAnalysisStatusNameBycode
	* @Description:
	* @param code
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:08:33
	* @throws
	 */
	private String getAnalysisStatusNameBycode(String code){
		if(code == null || "".equals(code)){
			return "";
		} else if (ComacConstants.ANALYZE_STATUS_NEW.equals(code)) {
			return ComacConstants.ANALYZE_STATUS_NEW_SHOW;
		} else if (ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(code)) {
			return ComacConstants.ANALYZE_STATUS_MAINTAIN_SHOW;
		} else if (ComacConstants.ANALYZE_STATUS_MAINTAINOK.equals(code)) {
			return ComacConstants.ANALYZE_STATUS_MAINTAINOK_SHOW;
		} else {
			return "";
		}
	}
	
	/**
	 * 发起流程
	* @Title: startProcess
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:07:57
	* @throws
	 */
	public String startProcess(){
		JSONObject json = new JSONObject();
		boolean flag = comProcessBo.startProcess(this.getComModelSeries(), analysisType, checkUser, checkOpinion, choosedIds, getSysUser().getUserId());
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
		
	}
	
	/**
	 * 审批流程得到列表
	* @Title: loadCheckProcess
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:08:16
	* @throws
	 */
	public String loadCheckProcess(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProcessBo.loadCheckProcess(page, this.getComModelSeries().getModelSeriesId(), analysisType, processStatus, launchUserName, fromDate, toDate, getSysUser().getUserId());
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("processId", objs[0]);
				hm.put("analysisType",  objs[1]);
				hm.put("processStatus",  objs[2]);
				hm.put("launchUser",  objs[3]);
				hm.put("launchDate",  objs[4]);
				hm.put("launchOpinion",  objs[5]);
				String checkOperate = "<a title='审核流程' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/toAuditBtn.gif'"
						+ " onclick='checkSingleOpenWindow(\""
						+ String.valueOf(objs[0])
						+ "\",\""
						+ String.valueOf(objs[1])
						+ "\",\""
						+ String.valueOf(objs[2])
						+ "\")'/></a>";
				hm.put("checkOperate",  checkOperate);
				hm.put("ataorareaCode",  objs[6]);
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
		json.element("comProcess", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	

	/**
	 * 审批流程得到列表——(首页代办)
	* @Title: loadCheckProcessForHomePage
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:08:16
	* @throws
	 */
	public String loadCheckProcessForHomePage(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProcessBo.loadCheckProcessForHomePage(page, this.getComModelSeries().getModelSeriesId(), getSysUser().getUserId());
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("processId", objs[0]);
				hm.put("analysisType",  objs[1]);
				hm.put("processStatus",  objs[2]);
				hm.put("launchUser",  objs[3]);
				hm.put("launchDate",  objs[4]);
				hm.put("launchOpinion",  objs[5]);
				String checkOperate = "<a title='审核流程' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/toAuditBtn.gif'"
						+ " onclick='checkSingleOpenWindow(\""
						+ String.valueOf(objs[0])
						+ "\",\""
						+ String.valueOf(objs[1])
						+ "\",\""
						+ String.valueOf(objs[2])
						+ "\")'/></a>";
				hm.put("checkOperate",  checkOperate);
				hm.put("ataorareaCode",  objs[7]);
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
		json.element("comProcess", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 批量审批通过
	* @Title: batchCheckPass
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月10日 下午2:01:54
	* @throws
	 */
	public String batchCheckPass(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		if(choosedIds != null){
			flag = comProcessBo.batchCheckPass(this.getComModelSeries(), choosedIds, getSysUser().getUserId());
		}
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 批量审批不通过
	* @Title: batchCheckNotPass
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月10日 下午3:38:45
	* @throws
	 */
	public String batchCheckNotPass(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		if(choosedIds != null){
			flag = comProcessBo.batchCheckNotPass(this.getComModelSeries(), choosedIds, getSysUser().getUserId());
		}
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 批量取消审核
	* @Title: batchCheckCancel
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月10日 下午4:06:26
	* @throws
	 */
	public String batchCheckCancel(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		if(choosedIds != null){
			flag = comProcessBo.batchCheckCancel(this.getComModelSeries(), choosedIds, getSysUser().getUserId());
		}
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	
	public String getComPrcessInfo(){
		JSONObject json = new JSONObject();
		if(processId != null && !"".equals(processId)){
			ComProcess comProcess = (ComProcess) comProcessBo.loadById(ComProcess.class, processId);
			if(comProcess != null){
				json.put("processId", processId);
				json.put("analysisType", comProcess.getAnalysisType());
				json.put("launchOpinion", comProcess.getLaunchOpinion());
				json.put("launchOpinion", comProcess.getLaunchOpinion());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startShenPiData = sdf.format(comProcess.getCreateDate());
				json.put("startShenPiData", startShenPiData);
				if(comProcess.getCreateUser() != null && !"".equals(comProcess.getCreateUser())){
					ComUser comUser = (ComUser) comProcessBo.loadById(ComUser.class, comProcess.getCreateUser());
					if(comUser != null){
						json.put("startShenPiUser", comUser.getUserName());
					}
				}
			}
		}
		this.writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 审批时得到所有的明细
	* @Title: getProcessDetail
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月11日 上午9:11:33
	* @throws
	 */
	public String getProcessDetail(){
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		List lst = comProcessBo.getProcessDetail(this.getComModelSeries().getModelSeriesId(), processId, analysisType);
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("detailId", objs[0]);
				hm.put("analysisType",  objs[1]);
				hm.put("isOk", this.getIsOkNameBycode(String.valueOf(objs[2])));
				hm.put("checkOpinion",  objs[3]);
				hm.put("mainId",  objs[4]);
				if(ComacConstants.STRUCTURE_CODE.equals(analysisType)){
					hm.put("mainCode",  objs[8]);//结构非ssi + Q
				}else {
					hm.put("mainCode",  objs[5]);
				}
				hm.put("mainName",  objs[6]);
				hm.put("urlParam",  objs[7]);
				listJsonFV.add(hm);
			}
		}
		json.element("comProcessDetail", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	
	/**
	 * 根据是否审核code得到name
	* @Title: getAnalysisStatusNameBycode
	* @Description:
	* @param code
	* @return
	* @author samual
	* @date 2014年12月4日 上午10:08:33
	* @throws
	 */
	private String getIsOkNameBycode(String code){
		if(code == null || "".equals(code)){
			return "";
		} else if (String.valueOf(ComacConstants.PROCESS_CHECK_STATUS_NULL).equals(code)) {
			return ComacConstants.PROCESS_CHECK_STATUS_NULL_SHOW;
		} else if (String.valueOf(ComacConstants.PROCESS_CHECK_STATUS_YES).equals(code)) {
			return ComacConstants.PROCESS_CHECK_STATUS_YES_SHOW;
		} else if (String.valueOf(ComacConstants.PROCESS_CHECK_STATUS_NO).equals(code)) {
			return ComacConstants.PROCESS_CHECK_STATUS_NO_SHOW;
		} else if (String.valueOf(ComacConstants.PROCESS_CHECK_STATUS_CANCEL).equals(code)) {
			return ComacConstants.PROCESS_CHECK_STATUS_CANCEL_SHOW;
		} else {
			return "";
		}
	}
	
	/**
	 * 单个流程审核时，审核单条数据
	* @Title: singleCheckProcessDetail
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月11日 下午1:34:40
	* @throws
	 */
	public String singleCheckProcessDetail(){
		JSONObject json = new JSONObject();
		boolean flag = false;
		if(choosedIds != null){
			flag = comProcessBo.singleCheckProcessDetail(this.getComModelSeries().getModelSeriesId(), processId, analysisType, isOkType, isOkCheckpinion, choosedIds, getSysUser().getUserId());
		}
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}

	/**
	 * 单个流程审核时，审核所有数据
	* @Title: allCheckProcessDetail
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月11日 下午2:29:06
	* @throws
	 */
	public String allCheckProcessDetail(){
		JSONObject json = new JSONObject();
		boolean flag = comProcessBo.allCheckProcessDetail(this.getComModelSeries().getModelSeriesId(), processId, analysisType, isOkType, isOkCheckpinion, getSysUser().getUserId());
		json = this.putJsonOKFlag(json, flag);
		this.writeToResponse(json.toString());
		return null;
	}
	

	/**
	 * 查看流程得到列表
	* @Title: loadProcessForQuery
	* @Description:
	* @return
	* @author samual
	* @date 2014年12月12日 上午10:57:15
	* @throws
	 */
	public String loadProcessForQuery(){
		if (this.getPage() == null)
			this.setPage(new com.richong.arch.web.Page());
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		String contextPath = ServletActionContext.getServletContext().getContextPath();
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList();
		Page page1 = comProcessBo.loadProcessForQuery(page, this.getComModelSeries().getModelSeriesId(), analysisType, processStatus, launchUserName, checkUserName, fromDate, toDate);
		List lst=page1.getResult();
		if (lst != null) {
			for (Object obj : lst) {
				Object []objs=(Object[])obj;
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("processId", objs[0]);
				hm.put("analysisType",  objs[1]);
				hm.put("processStatus",  objs[2]);
				hm.put("launchUser",  objs[3]);
				hm.put("launchDate",  objs[4]);
				hm.put("checkUser",  objs[5]);
				if(ComacConstants.PROCESS_STATUS_CANCEL_CHECK.equals(String.valueOf( objs[2])) || ComacConstants.PROCESS_STATUS_FINISH_CHECK.equals(String.valueOf( objs[2]))){
					hm.put("checkDate",  objs[6]);
				}else {
					hm.put("checkDate",  "");
				}
				hm.put("launchOpinion",  objs[7]);
				String checkOperate = "<a title='审核流程' href='javascript:void(0)'><img src='"
						+ contextPath
						+ "/images/toCheckBtn.gif'"
						+ " onclick='checkSingleOpenWindow(\""
						+ String.valueOf(objs[0])
						+ "\",\""
						+ String.valueOf(objs[1])
						+ "\",\""
						+ String.valueOf(objs[2])
						+ "\")'/></a>";
				hm.put("checkOperate",  checkOperate);
				hm.put("ataorareaCode",  objs[9]);
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
		json.element("comProcess", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	public IComProcessBo getComProcessBo() {
		return comProcessBo;
	}

	public void setComProcessBo(IComProcessBo comProcessBo) {
		this.comProcessBo = comProcessBo;
	}

	public IComProcessDetailBo getComProcessDetailBo() {
		return comProcessDetailBo;
	}

	public void setComProcessDetailBo(IComProcessDetailBo comProcessDetailBo) {
		this.comProcessDetailBo = comProcessDetailBo;
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

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getChoosedIds() {
		return choosedIds;
	}

	public void setChoosedIds(String choosedIds) {
		this.choosedIds = choosedIds;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getLaunchUserName() {
		return launchUserName;
	}

	public void setLaunchUserName(String launchUserName) {
		this.launchUserName = launchUserName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getIsOkType() {
		return isOkType;
	}

	public void setIsOkType(String isOkType) {
		this.isOkType = isOkType;
	}

	public String getIsOkCheckpinion() {
		return isOkCheckpinion;
	}

	public void setIsOkCheckpinion(String isOkCheckpinion) {
		this.isOkCheckpinion = isOkCheckpinion;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	
}
