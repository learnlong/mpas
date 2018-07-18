package com.rskytech.struct.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.bo.IS8Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.struct.dao.IS7Dao;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

/**
 * S6Action
 * 
 * @author 赵涛
 * @careatedate 2012年8月28日
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class S6Action extends BaseAction {
	private static final long serialVersionUID = -5302038340240701412L;
	private String considerWear;
	private IS6Bo s6Bo;
	private String ssiId;
	private String siCode;
	private String delId;
	private String siTitle;
	private String verifyStr;
	private String s1Remark;
	private String finalRemark;
	private String coverCpcp;
	private String cpcp;
	private int isMaintain;
	private IS1Bo s1Bo;
	private String taskId;
	private int[] step;
	private String s6Id;
	private String otherJsonData;
	private String resultJsonData;
	private String ssiList;
	private String ssiNumberList;
	private String listJsonData;
	private String inorOut;
	private String region;// 用来区分是S6内还是S6外的数据查询
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
	public String taskCode;
	private ISsiStepBo ssiStepBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IS7Dao s7Dao;
	private IZa7Dao za7Dao;
	private IS8Bo s8Bo;
	private String miniVal;
	
	/**
	 * 得到S6的备注信息
	 * 
	 * @return
	 */
	public String getS6Remark() {
		HashMap jsonFeildList = null;
		List listJson = new ArrayList<HashMap>();
		JSONArray json = new JSONArray();
		S6 s6 = s6Bo.getS6Records(ssiId, inorOut);
		if (s6 != null) {
			jsonFeildList = new HashMap();
			jsonFeildList.put("finalRemark", wipeNull(s6.getFinalRemark()));
			jsonFeildList.put("cpcp", wipeNull(s6.getCpcp()));
			jsonFeildList.put("s6Id", s6.getS6Id());
			jsonFeildList.put("isCpcp", s6.getIsCpcp());
			jsonFeildList.put("considerWear", s6.getConsiderWear());
			listJson.add(jsonFeildList);
			json.addAll(listJson);
		}

		writeToResponse(json.toString());
		return null;
	}

	public String initS6() {
		ComUser thisUser = getSysUser();
		if (thisUser == null) {
			return "s6in";// 现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		String userId = getSysUser().getUserId();
		S6Ea s6Ea = null;
		List<S1> list = s6Bo.getS1IdBySssiId(ssiId, inorOut);
		List<S6> s6List = s6Bo.getS6BySsiId(ssiId, inorOut);
		S6 s6 = null;
		if (s6List != null) {
			s6 = s6List.get(0);
		}
		if(list!=null){
			for (S1 i : list) {
				List<S6Ea> s6EaList = s6Bo.getS6EaByS1Id(inorOut, i.getS1Id());
				if (s6EaList == null || s6EaList.isEmpty()) {
					if (s6 == null) {
						s6 = new S6();
						s6.setSMain((SMain) s6Bo.loadById(SMain.class, ssiId));
						s6.setInOrOut(inorOut);
						s6Bo.saveOrUpdate(s6, ComacConstants.DB_INSERT, userId);
					}
					s6Ea = new S6Ea();
					s6Ea.setS1(i);
					s6Ea.setS6(s6);
					s6Bo.saveOrUpdate(s6Ea, ComacConstants.DB_INSERT, userId);
				}
			}
		}
		List<Object[]> sList = s6Bo.searchSsi(ssiId, "0");
		StringBuffer sb = new StringBuffer();
		if (sList != null && !sList.isEmpty()) {
			sb.append("[");
			for (Object[] o : sList) {
				sb.append("{name:'" + o[0] + "',number:" + o[1] + "},");
			}
			ssiList = sb.substring(0, sb.length() - 1) + "]";
		}
		if (ssiList == null) {
			ssiList = "[]";
		}
		SMain  sMain= (SMain) s6Bo.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		SRemark sRmaker = s1Bo.getRemarkBySssi(ssiId);
		if (sRmaker != null) {
			this.s1Remark = sRmaker.getS1Remark();
		}
		List<SStep> step1 = s1Bo.getSstepBySssiId(ssiId);
		step = this.ssiStepBo.initStep(ssiId, step1.get(0), "S6IN");
		return "s6in";
	}

	public String getOtherCount() {
		List<TaskMsg> tkList = s6Bo.getOtherRecords(ssiId, getComModelSeries().getModelSeriesId(),
				ComacConstants.STRUCTURE_CODE, 3, null);

		writeToResponse(tkList.size() + "");

		return null;
	}

	/**
	 * 初始化S6外
	 * 
	 * @return
	 */
	public String initS6Out() {
		ComUser thisUser = getSysUser();
		if (thisUser == null) {
			return "s6out";// 现在返回success都是直接跳转jsp,我们共同的jsp中存在session判断与提示
		}
		String userId = getSysUser().getUserId();
		S6Ea s6Ea = null;
		List<S1> list = s6Bo.getS1IdBySssiId(ssiId, inorOut);
		List<S6> s6List = s6Bo.getS6BySsiId(ssiId, inorOut);
		S6 s6 = null;
		if (s6List != null) {
			s6 = s6List.get(0);
		}
		if(list!=null){
			for (S1 i : list) {
				List<S6Ea> s6EaList = s6Bo.getS6EaByS1Id(inorOut, i.getS1Id());
				if (s6EaList == null || s6EaList.isEmpty()) {
					if (s6 == null) {
						s6 = new S6();
						s6.setSMain((SMain) s6Bo.loadById(SMain.class, ssiId));
						s6.setInOrOut(inorOut);
						s6Bo.saveOrUpdate(s6, ComacConstants.DB_INSERT, userId);
					}
					s6Ea = new S6Ea();
					s6Ea.setS1(i);
					s6Ea.setS6(s6);
					s6Bo.saveOrUpdate(s6Ea, ComacConstants.DB_INSERT, userId);
				}
			}
		}

		List<Object[]> sList = s6Bo.searchSsi(ssiId, "1");
		StringBuffer sb = new StringBuffer();
		if (sList != null && !sList.isEmpty()) {
			sb.append("[");
			for (Object[] o : sList) {
				sb.append("{name:'" + o[0] + "',number:" + o[1] + "},");
			}
			ssiList = sb.substring(0, sb.length() - 1) + "]";
		}
		if (ssiList == null) {
			ssiList = "[]";

		}
		SMain  sMain= (SMain) s6Bo.loadById(SMain.class, ssiId);
		if(sMain.getComAta()!=null){
			ComAta comAta = sMain.getComAta();
			this.siCode=comAta.getAtaCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(comAta.getAtaName());
		}else{
			this.siCode=sMain.getAddCode();
			this.siTitle=JavaScriptUtils.javaScriptEscape(sMain.getAddName());
		}
		List<SStep> step1 = s1Bo.getSstepBySssiId(ssiId);
		step = this.ssiStepBo.initStep(ssiId, step1.get(0), "S6OUT");

		return "s6out";
	}

	Object[] str = null;

	/**
	 * 保存S6
	 * 
	 * @return
	 */
	public String saveS6() {
		String index = null;
		List<String> listArr = s6Bo.saveS6Records(delId, getComModelSeries().getModelSeriesId(), otherJsonData,
				resultJsonData, listJsonData, finalRemark
				, cpcp, ssiId, s6Id, inorOut, getSysUser(), coverCpcp,considerWear);
		if(listArr.size()>1){
			int cou=0;
			for(String str : listArr){
				String[] arr = str.split(",");
				for(String string : arr){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
				}
				cou++;
			}
			if(cou>0){
				za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
			}
		}
		index = listArr.get(listArr.size()-1);
		int needChange=0;
		List<TaskMsg> listTask = s7Dao.getS7Records(ssiId);
		List<SStep> step1List = s1Bo.getSstepBySssiId((ssiId));
		SStep step1 = step1List.get(0);
		SMain sMain = (SMain) this.s6Bo.loadById(SMain.class, ssiId);
		if(listTask==null||listTask.size()==0){
			 sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			 step1.setS7(3);
			 step1.setS8(3);
			this.s6Bo.saveOrUpdate(sMain, ComacConstants.DB_UPDATE,  getSysUser().getUserId());
			if("in".equals(inorOut)&&step1.getS6Out()!=3){
				
			}else{
				index = "100";
			}
		}else{
			for(TaskMsg task : listTask){
				if(task.getNeedTransfer()==null||"".equals(task.getNeedTransfer())){
					needChange++;
				}
			}
			if(needChange>0){
				if(step1.getS7()!=2){
					step1.setS7(2);
					step1.setS8(0);
					this.s6Bo.update(step1,getSysUser().getUserId());
				}
				if(!sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAIN)){
					sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					this.s6Bo.saveOrUpdate(sMain, ComacConstants.DB_UPDATE,  getSysUser().getUserId());
				}
			}else{
				if(step1.getS7()!=1){
					step1.setS7(1);
					if(s8Bo.getS8Records(ssiId, "S8")!=null){
						step1.setS8(1);
					}else{
						step1.setS8(3);
					}
					this.s6Bo.update(step1,getSysUser().getUserId());
				}
				if(!sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)){
					sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
					this.s6Bo.saveOrUpdate(sMain, ComacConstants.DB_UPDATE,  getSysUser().getUserId());
				}
			}
		}
		this.ssiStepBo.changeStatus(ssiId);
		writeToResponse(index);
		return null;
	}


	/**
	 * 得到AD/ED的全部数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAdEdRecords() throws Exception {
		HashMap jsonFieldList = null;
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		List<Object[]> s6eaList = s6Bo.getS6EaRecords(ssiId, region);
		if (s6eaList != null) {
			double miniValue;
			for (Object[] obj : s6eaList) {
				jsonFieldList = new HashMap();
				jsonFieldList.put("id", obj[0]);
				jsonFieldList.put("composition", obj[1]);
				if ("0".equals(obj[11].toString())) {//非金属
					if (obj[2] == null) {
						List<S4> s4List =  s6Bo.getS4Records(obj[10].toString(),region);
						List<Sy> syList =  s6Bo.getSyRecords(obj[10].toString(),region);
						if(s4List!=null&&s4List.size()>0&&syList!=null&&syList.size()>0){
							S4 s4=s4List.get(0);
							Sy sy=syList.get(0);
							if (s4.getGdr()<=sy.getScr()) {
								jsonFieldList.put("edrFalse",  s4.getGdr());
							} else {
								jsonFieldList.put("edrFalse",  sy.getScr());
							}
						}else{
							jsonFieldList.put("edrFalse",  "");
						}
					} else {
						jsonFieldList.put("edrFalse", obj[2]);
					}
					if (obj[3] == null) {
						List<S5> s5List = s6Bo.getS5Records(obj[10].toString(),region);
						if(s5List!=null&&s5List.size()>0){
							jsonFieldList.put("adrFalse",  s5List.get(0).getAdr());
						}else{
							jsonFieldList.put("adrFalse",  "");
						}
					} else {
						jsonFieldList.put("adrFalse", obj[3]);
					}
					jsonFieldList.put("eaTypeFalse", obj[5]);
					if("".equals(jsonFieldList.get("adrFalse"))||"".equals(jsonFieldList.get("edrFalse"))||
							jsonFieldList.get("adrFalse")==null||jsonFieldList.get("edrFalse")==null){
						jsonFieldList.put("miniLevel", "");
						jsonFieldList.put("intervalFalse", "");
					}else{
						double adrFalse =  Double.parseDouble(jsonFieldList.get("adrFalse").toString());
						double edrFalse =  Double.parseDouble(jsonFieldList.get("edrFalse").toString());
						if(adrFalse>=edrFalse){
							miniValue = edrFalse;
						}else{
							miniValue = adrFalse;
						}
						jsonFieldList.put("miniLevel", miniValue);
						List<Object[]> cusInList = s6Bo.getS6CusInList(region,(int)miniValue,this.getComModelSeries().getModelSeriesId());
						if(cusInList!=null&&cusInList.size()>0){
							for(Object[] oj : cusInList){
								jsonFieldList.put("intervalFalse", oj[1]);
							}
						}else{
							jsonFieldList.put("intervalFalse", "");
						}
					}
					
				} else {
					jsonFieldList.put("miniLevel", "");
					jsonFieldList.put("edrFalse", "");
					jsonFieldList.put("adrFalse", "");
					jsonFieldList.put("intervalFalse", "");
					jsonFieldList.put("eaTypeFalse", "");
				}
				if ("1".equals(obj[11].toString())) {
					if (obj[6] == null) {
						List<S4> s4List =  s6Bo.getS4Records(obj[10].toString(),region);
						List<Sy> syList =  s6Bo.getSyRecords(obj[10].toString(),region);
						if(s4List!=null&&s4List.size()>0&&syList!=null&&syList.size()>0){
							S4 s4=s4List.get(0);
							Sy sy=syList.get(0);
							if (s4.getGdr()<=sy.getScr()) {
								jsonFieldList.put("edrTrue",  s4.getGdr());
							} else {
								jsonFieldList.put("edrTrue",  sy.getScr());
							}
						}else{
							jsonFieldList.put("edrTrue",  "");
						}
					} else {
						jsonFieldList.put("edrTrue", obj[6]);
					}
					if (obj[7] == null) {
						try {
							List<S5> s5List = s6Bo.getS5Records(obj[10].toString(),region);
							if(s5List!=null&&s5List.size()>0){
								jsonFieldList.put("adrTrue",  s5List.get(0).getAdr());
							}else{
								jsonFieldList.put("adrTrue","");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						jsonFieldList.put("adrTrue", obj[7]);
					}
					jsonFieldList.put("eaTypeTrue", obj[9]);
					if("".equals(jsonFieldList.get("adrTrue"))||jsonFieldList.get("adrTrue")==null
							||"".equals(jsonFieldList.get("edrTrue"))||jsonFieldList.get("edrTrue")==null){
						jsonFieldList.put("miniLevelMetal", "");
						jsonFieldList.put("intervalTrue", "");
					}else{
						double adrTrue =  Double.parseDouble(jsonFieldList.get("adrTrue").toString());
						double edrTrue =  Double.parseDouble(jsonFieldList.get("edrTrue").toString());
						if(adrTrue>=edrTrue){
							miniValue = edrTrue;
						}else{
							miniValue = adrTrue;
						}
						jsonFieldList.put("miniLevelMetal", miniValue);
						List<Object[]> cusInList = s6Bo.getS6CusInList(region,(int)miniValue,this.getComModelSeries().getModelSeriesId());
						if(cusInList!=null&&cusInList.size()>0){
							for(Object oj1[] : cusInList){
								jsonFieldList.put("intervalTrue", oj1[1]);
							}
						}else{
							jsonFieldList.put("intervalTrue", "");
						}
					}
				} else {
					jsonFieldList.put("miniLevelMetal", "");
					jsonFieldList.put("edrTrue", "");
					jsonFieldList.put("adrTrue", "");
					jsonFieldList.put("intervalTrue", "");
					jsonFieldList.put("eaTypeTrue", "");
				}
				jsonFieldList.put("ownArea", wipeNull(comAreaBo.getAreaCodeByAreaId(obj[12].toString())));
				if(obj[11]!=null){
					jsonFieldList.put("metal", obj[11]); 
				 }else{
					 jsonFieldList.put("metal", ""); 
				 }
				
				jsonFieldList.put("s1Id", obj[10]);
				if(obj[13]!=null){
					jsonFieldList.put("eff", obj[13]);	 
				 }else{
					 jsonFieldList.put("eff", ""); 
				 }
				
				listJson.add(jsonFieldList);

			}
			json.element("s1List", listJson);

		}
		
		List<TaskMsg> tkList = s6Bo.getOtherRecords(ssiId,getComModelSeries().getModelSeriesId(),
				ComacConstants.STRUCTURE_CODE, 3, region);
		if (tkList != null && tkList.size() > 0 && listJson !=null && listJson.size()>0) {
			for(int i=0;i<listJson.size();i++){
				String siId=listJson.get(i).get("s1Id")+"AD/ED";
				String jgVal=listJson.get(i).get("intervalFalse")+"";
				for (TaskMsg tk : tkList) {
					if(siId.equals(tk.getAnyContent1()) ){
						if(!jgVal.equals(tk.getTaskInterval())){
							tk.setTaskInterval(jgVal);
							taskMsgBo.saveOrUpdate(tk);
						}
					}
				}
			}
		}
		writeToResponse(json.toString());
		return null;
	}
 
	public String getMiniValueInterval(){
		if(miniVal!=null&&!"".equals(miniVal)){
			double miniValue =  Double.parseDouble(miniVal);
			List<Object[]> cusInList = s6Bo.getS6CusInList(region,(int)miniValue,this.getComModelSeries().getModelSeriesId());
			if(cusInList!=null&&cusInList.size()>0){
				for(Object[] oj : cusInList){
					writeToResponse("{success:'"+ oj[1].toString()+"'}");
					break;
				}
			}
		}
		return null;
	}
	
	/**
	 * 得到临时表数据
	 * 
	 * @return
	 */
	public String getOtherRecords() {
		HashMap jsonFieldList = null;
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		List<TaskMsg> tkList = s6Bo.getOtherRecords(ssiId,getComModelSeries().getModelSeriesId(),
				ComacConstants.STRUCTURE_CODE, 3, region);
		String area = null;
		if (tkList != null && tkList.size() > 0) {
			for (TaskMsg tk : tkList) {
				jsonFieldList = new HashMap();
				jsonFieldList.put("taskId", tk.getTaskId());
				jsonFieldList.put("taskType", wipeNull(tk.getTaskType()));
				jsonFieldList.put("taskCode", wipeNull(tk.getTaskCode()));
				jsonFieldList.put("sourceStep", wipeNull(tk.getSourceStep()));
				jsonFieldList.put("eff", wipeNull(tk.getEffectiveness()));
				area = comAreaBo.getAreaCodeByAreaId(tk.getOwnArea());
				jsonFieldList.put("ownArea", wipeNull(area));
				jsonFieldList.put("occures", wipeNull(tk.getTaskInterval()));// 任务间隔
				jsonFieldList.put("reachWayn", wipeNull(tk.getReachWay()));// 接近方式
				jsonFieldList.put("taskDesc", wipeNull(tk.getTaskDesc()));// 任务描述
				jsonFieldList.put("mrbId",wipeNull(tk.getAnyContent2()));
				jsonFieldList.put("s1Id", wipeNull(tk.getAnyContent1()));
				jsonFieldList.put("inOrOut", wipeNull(tk.getAnyContent3()));
				listJson.add(jsonFieldList);
			}

		}
		json.element("otherList", listJson);
		writeToResponse(json.toString());

		return null;
	}

	/**
	 * 得到最终任务列表数据
	 * 
	 * @return
	 */
	public String getResultRecords() {

		HashMap jsonFieldList = null;
		List<HashMap> listJson = new ArrayList<HashMap>();
		JSONObject json = new JSONObject();
		List<TaskMsg> tkList = s6Bo.getOtherRecords(ssiId,getComModelSeries().getModelSeriesId(),
				ComacConstants.STRUCTURE_CODE, null, region);
		String areaCode = null;
		if (tkList != null && tkList.size() > 0) {
			for (TaskMsg tk : tkList) {
				jsonFieldList = new HashMap();
				jsonFieldList.put("taskId", tk.getTaskId());
				jsonFieldList.put("eff", wipeNull(tk.getEffectiveness()));
				jsonFieldList.put("taskType", wipeNull(tk.getTaskType()));// 任务类型
				areaCode = comAreaBo.getAreaCodeByAreaId(tk.getOwnArea());
				jsonFieldList.put("ownArea", wipeNull(areaCode));// 所属区域
				jsonFieldList.put("taskCode", wipeNull(tk.getTaskCode()));// 任务编号
				jsonFieldList.put("occures", wipeNull(tk.getTaskInterval()));// 任务间隔
				jsonFieldList.put("reachWayn", wipeNull(tk.getReachWay()));// 接近方式
				jsonFieldList.put("taskDesc", wipeNull(tk.getTaskDesc()));// 任务描述
				jsonFieldList.put("intOut", wipeNull(tk.getAnyContent3()));// 内外部
				jsonFieldList.put("remark", wipeNull(tk.getRemark()));// 备注
				listJson.add(jsonFieldList);
			}

		}
		json.element("taskList", listJson);
		writeToResponse(json.toString());

		return null;

	}

	public String deleteResultList() {
		if(taskId!=null){
			this.taskMsgBo.deleteTaskMsgById(taskId);
			writeToResponse("{success:true}");
		}else{
			writeToResponse("{success:false}");
		}
		return null;
	}

	/**
	 * 得到间隔下拉框数据
	 * 
	 * @return
	 */
	public String getIntervalRecords() {
		List<CusInterval> list = s6Bo.getIntervalRecords(inorOut, getComModelSeries().getModelSeriesId());
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		sb.append("{intervals:[");
		if(list!=null){
			for (CusInterval cusi : list) {
				sb1.append("{intervalName:'" + cusi.getIntervalLevel()
						+ "',intervalId:'" + cusi.getIntervalId()
						+ "',intervalValue:'" + cusi.getIntervalValue() + "'},");
			}
		}
		if (!BasicTypeUtils.isNullorBlank(sb1.toString())) {
			sb.append(sb1.substring(0, sb1.length() - 1));
		}
		sb.append("]}");
		writeToResponse(sb.toString());
		return null;
	}

	/**
	 * 校验任务临时任务表中任务编号是否存在
	 */
	public String checkTempTaskCodeExist(){
		if(this.s6Bo.checkTempTaskCodeExist(taskCode,getComModelSeries().getModelSeriesId(),region)){
			writeToResponse("true");
		}else{
			writeToResponse("false");
		}
		return null;
	}

	
	
	/**
	 * 校验任务是否存在
	 * 
	 * @return
	 */
	public String verifyTaskCode() {
		TaskMsg task = taskMsgBo.getTaskByTaskCode(getComModelSeries().getModelSeriesId(), verifyStr);
		if (task!=null) {
			writeToResponse(verifyStr);
			return null;
		}
		return null;
	}

	public boolean arrayCheck(String[] a, String[] b) {
		String str = null;
		String str1 = null;
		if (a.length == b.length) {
			for (String s : a) {
				str += s;
			}
			for (String s : b) {
				str1 += s;
			}
			if (str.equals(str1)) {
				return true;
			}
		}
		return false;
	}
	 /**
	 * 将空字符串替换为“”
	 * @param str
	 * @return
	 * @author chendexu
	 * createdate 2012-08-29
	 */
	private String wipeNull(String str){
		if(str!=null&&!"null".equals(str)){
			return str;
		}
		return "";
	}
	public String getConsiderWear() {
		return considerWear;
	}

	public void setConsiderWear(String considerWear) {
		this.considerWear = considerWear;
	}

	public IS6Bo getS6Bo() {
		return s6Bo;
	}

	public void setS6Bo(IS6Bo s6Bo) {
		this.s6Bo = s6Bo;
	}

	public String getSsiId() {
		return ssiId;
	}

	public void setSsiId(String ssiId) {
		this.ssiId = ssiId;
	}

	public String getSiCode() {
		return siCode;
	}

	public void setSiCode(String siCode) {
		this.siCode = siCode;
	}

	public String getDelId() {
		return delId;
	}

	public void setDelId(String delId) {
		this.delId = delId;
	}

	public String getSiTitle() {
		return siTitle;
	}

	public void setSiTitle(String siTitle) {
		this.siTitle = siTitle;
	}

	public String getVerifyStr() {
		return verifyStr;
	}

	public void setVerifyStr(String verifyStr) {
		this.verifyStr = verifyStr;
	}

	public String getS1Remark() {
		return s1Remark;
	}

	public void setS1Remark(String s1Remark) {
		this.s1Remark = s1Remark;
	}

	public String getFinalRemark() {
		return finalRemark;
	}

	public void setFinalRemark(String finalRemark) {
		this.finalRemark = finalRemark;
	}

	public String getCoverCpcp() {
		return coverCpcp;
	}

	public void setCoverCpcp(String coverCpcp) {
		this.coverCpcp = coverCpcp;
	}

	public String getCpcp() {
		return cpcp;
	}

	public void setCpcp(String cpcp) {
		this.cpcp = cpcp;
	}

	public int getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(int isMaintain) {
		this.isMaintain = isMaintain;
	}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int[] getStep() {
		return step;
	}

	public void setStep(int[] step) {
		this.step = step;
	}

	public String getS6Id() {
		return s6Id;
	}

	public void setS6Id(String s6Id) {
		this.s6Id = s6Id;
	}

	public String getOtherJsonData() {
		return otherJsonData;
	}

	public void setOtherJsonData(String otherJsonData) {
		this.otherJsonData = otherJsonData;
	}

	public String getResultJsonData() {
		return resultJsonData;
	}

	public void setResultJsonData(String resultJsonData) {
		this.resultJsonData = resultJsonData;
	}

	public String getSsiList() {
		return ssiList;
	}

	public void setSsiList(String ssiList) {
		this.ssiList = ssiList;
	}

	public String getSsiNumberList() {
		return ssiNumberList;
	}

	public void setSsiNumberList(String ssiNumberList) {
		this.ssiNumberList = ssiNumberList;
	}

	public String getListJsonData() {
		return listJsonData;
	}

	public void setListJsonData(String listJsonData) {
		this.listJsonData = listJsonData;
	}

	public String getInorOut() {
		return inorOut;
	}

	public void setInorOut(String inorOut) {
		this.inorOut = inorOut;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}

	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IS7Dao getS7Dao() {
		return s7Dao;
	}

	public void setS7Dao(IS7Dao s7Dao) {
		this.s7Dao = s7Dao;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}

	public IS8Bo getS8Bo() {
		return s8Bo;
	}

	public void setS8Bo(IS8Bo s8Bo) {
		this.s8Bo = s8Bo;
	}

	public String getMiniVal() {
		return miniVal;
	}

	public void setMiniVal(String miniVal) {
		this.miniVal = miniVal;
	}

}
