package com.rskytech.task.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.struct.bo.ISsiMainBo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.pojo.TaskMsgDetail;

public class TransferAreaTaskAction extends BaseAction{

	private static final long serialVersionUID = 1L;
	private ITaskMsgBo taskMsgBo;
	private IComAreaBo comAreaBo;
    private IMsiMainBo msiMainBo;
    private ISsiMainBo ssiMainBo;
    private ILhMainBo lhMainBo;
    private String firstTextField;
	private String secondTextField;
	private ITaskMsgDetailBo taskMsgDetailBo;

	public String initSys(){
		return "sys";
	}
	public String  initStruct(){
			return "struct";
	}
	public String  initLhirf(){
			return "lhirf";
	}

	//<===============================系统转区域=====================================
	/**
	 * 查询系统转区域的任务
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findSysTask(){
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<TaskMsg> listTask = this.taskMsgBo.getTaskMsgIsSysTransfer(modelSeriesId,
							ComacConstants.SYSTEM_CODE,firstTextField,page);
		JSONObject json = new JSONObject();
		int i=0;
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		if(listTask!=null&&listTask.size()>0){
			for (TaskMsg taskMsg : listTask) {
				HashMap map = new HashMap();
				if (secondTextField != null && !"".equals(secondTextField)) {
					if(taskMsg.getOwnArea()!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea());
						if(areaCode.contains(secondTextField)){
							map.put("ownArea", areaCode);
						}else{
							i++;
							continue;
						}
					}else{
						i++;
						continue;
					}
				}else{
					if(taskMsg.getOwnArea()!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea());
						map.put("ownArea", areaCode);
					}else{
						i++;
						continue;
					}
				}
				MMain mMain = (MMain) this.msiMainBo.loadById(MMain.class, taskMsg.getSourceAnaId());
				if(mMain!=null){
					ComAta comAta = mMain.getComAta();
					if(comAta!=null){
						map.put("msiCode", comAta.getAtaCode());
						map.put("msiName", comAta.getAtaName());
					}else{
						i++;
						continue;
					}
				}else{
					i++;
					continue;
				}
				map.put("taskId", taskMsg.getTaskId());
				map.put("taskCode", taskMsg.getTaskCode());
				map.put("taskType", wipeNull(taskMsg.getTaskType()));// 任务类型（GVI、DET...）
				map.put("taskDesc", wipeNull(taskMsg.getTaskDesc()));// 任务描述
				map.put("reachWay", wipeNull(taskMsg.getReachWay()));// 接近方式
				map.put("taskInterval", wipeNull(taskMsg.getTaskInterval()));// 任务间隔
				map.put("hasAccept", taskMsg.getHasAccept());// 是否接收
				map.put("rejectReason", wipeNull(taskMsg.getRejectReason()));// 退回原因
				map.put("causeType", wipeNull(taskMsg.getAnyContent2()));
				map.put("causeCode",wipeNull( taskMsg.getAnyContent3()));
				map.put("whyTransfer", wipeNull(taskMsg.getWhyTransfer()));// 转移原因
				List<TaskMsgDetail> set = taskMsgDetailBo.getListDetailTaskBytaskId(taskMsg.getTaskId());
				String areaTaskDesc = "";//区域任务说明	
				String areaReachWay = "";//区域接近方式
				String areaTaskInterval ="";//区域任务间隔
				String areaTaskIntervalMerge ="";//合并后任务间隔
				for (TaskMsgDetail taskMsgDetail : set) {
					if(	!BasicTypeUtils.isNullorBlank(taskMsgDetail.getDestTask())){
						TaskMsg task =(TaskMsg)this.comAreaBo.loadById(TaskMsg.class,taskMsgDetail.getDestTask());
						areaTaskDesc += task.getTaskDesc()== null ?"":task.getTaskDesc()+"\n";
						areaReachWay += task.getReachWay()== null ?"":task.getReachWay()+"\n";
						areaTaskInterval += task.getTaskInterval()== null ?"":task.getTaskInterval()+"\n";
						areaTaskIntervalMerge += task.getTaskIntervalMerge()== null ?"":task.getTaskIntervalMerge()+"\n";
					}
				}
				map.put("areaTaskDesc",areaTaskDesc);
				map.put("areaReachWay",areaReachWay);
				map.put("areaTaskInterval",areaTaskInterval);
				map.put("areaTaskIntervalMerge",areaTaskIntervalMerge);
				listJsonFV.add(map);
			}
		}
		json.element("total", this.getPage().getTotalCount()-i);
		json.element("task", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	//<===============================结构转区域=====================================
	/**
	 * 查询结构转区域任务
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String findStructTask(){
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List<TaskMsg> listTask = this.taskMsgBo.getTaskMsgIsStructTransfer(modelSeriesId,
									ComacConstants.STRUCTURE_CODE,firstTextField,page);
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		int i=0;
		if(listTask!=null&&listTask.size()>0){
			for (TaskMsg taskMsg : listTask) {
				HashMap map = new HashMap();
				if (secondTextField != null && !"".equals(secondTextField)) {
					if(taskMsg.getOwnArea()!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea());
						if(areaCode.contains(secondTextField)){
							map.put("ownArea", areaCode);
						}else{
							i++;
							continue;
						}
					}else{
						i++;
						continue;
					}
				}else{
					if(taskMsg.getOwnArea()!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea());
						map.put("ownArea", areaCode);
					}else{
						i++;
						continue;
					}
				}
				map.put("taskId", taskMsg.getTaskId());
				SMain sMain = (SMain) this.ssiMainBo.loadById(SMain.class, taskMsg.getSourceAnaId());
				if(sMain!=null){
					ComAta comAta = sMain.getComAta();
					if(comAta!=null){
						map.put("ssiCode", comAta.getAtaCode());
						map.put("ssiName", comAta.getAtaName());
					}else{
						map.put("ssiCode", sMain.getAddCode());
						map.put("ssiName", sMain.getAddName());
					}
				}else{
					i++;
					continue;
				}
				map.put("taskCode", taskMsg.getTaskCode());
				map.put("taskCode", taskMsg.getTaskCode());//任务编号
				map.put("taskType", taskMsg.getTaskType());// 任务类型（GVI、DET...）
				map.put("taskDesc", taskMsg.getTaskDesc());// 任务描述
				map.put("reachWay", taskMsg.getReachWay());// 接近方式
				map.put("taskInterval", taskMsg.getTaskInterval());// 任务间隔
				map.put("hasAccept", taskMsg.getHasAccept());// 是否接收
				map.put("rejectReason", taskMsg.getRejectReason());// 退回原因
				map.put("whyTransfer", taskMsg.getWhyTransfer());// 转移原因
				List<TaskMsgDetail> set = taskMsgDetailBo.getListDetailTaskBytaskId(taskMsg.getTaskId());
				String areaTaskDesc = "";//区域任务说明	
				String areaReachWay = "";//区域接近方式
				String areaTaskInterval ="";//区域任务间隔
				String areaTaskIntervalMerge ="";//合并后任务间隔
				for (TaskMsgDetail taskMsgDetail : set) {
					if(	!BasicTypeUtils.isNullorBlank(taskMsgDetail.getDestTask())){
						TaskMsg task =(TaskMsg)this.comAreaBo.loadById(TaskMsg.class,taskMsgDetail.getDestTask());
						areaTaskDesc += task.getTaskDesc()== null ?"":task.getTaskDesc()+"\n";
						areaReachWay += task.getReachWay()== null ?"":task.getReachWay()+"\n";
						areaTaskInterval += task.getTaskInterval()== null ?"":task.getTaskInterval()+"\n";
						areaTaskIntervalMerge += task.getTaskIntervalMerge()== null ?"":task.getTaskIntervalMerge()+"\n";
					}
				}
				map.put("areaTaskDesc",areaTaskDesc);
				map.put("areaReachWay",areaReachWay);
				map.put("areaTaskInterval",areaTaskInterval);
				map.put("areaTaskIntervalMerge",areaTaskIntervalMerge);
				listJsonFV.add(map);
			}
			
		}
		json.element("total", this.getPage().getTotalCount()-i);
		json.element("task", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	
	/**
	 * 查询Lhirf 转区域任务
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String findLhirfTask() throws Exception{
		this.getPage().setStartIndex(getStart());
		if (getLimit() > 0) {
			this.getPage().setPageSize(getLimit());
		}
		List listJsonFV = new ArrayList();
		JSONObject json = new JSONObject();
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		List list =lhMainBo.getLhrifTaskId(modelSeriesId, firstTextField, page);
		int t=0;
		if(list!=null){
			for (int i = 0; i < list.size(); i++){
				Object[] ob = (Object[]) list.get(i);
				HashMap jsonFeildList = new HashMap();
				if (secondTextField != null && !"".equals(secondTextField)) {
					if(ob[18]!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(ob[18].toString());
						if(areaCode.contains(secondTextField)){
							jsonFeildList.put("taskArea", areaCode);
						}else{
							t++;
							continue;
						}
					}else{
						t++;
						continue;
					}
				}else{
					if(ob[18]!=null){
						String areaCode = this.comAreaBo.getAreaCodeByAreaId(ob[18].toString());
						jsonFeildList.put("taskArea", areaCode);
					}else{
						t++;
						continue;
					}
				}
				jsonFeildList.put("hsiCode", ob[0]); /////**************
				jsonFeildList.put("hsiName", ob[1]) ;//
				jsonFeildList.put("ownArea", ob[2]);
				jsonFeildList.put("taskId", ob[3]);
				jsonFeildList.put("taskCode", ob[4]);///
				jsonFeildList.put("taskType", ob[5]) ;////
				if(null ==ob[6]){
					jsonFeildList.put("taskDesc", "");
				}else{
					jsonFeildList.put("taskDesc",ob[6]);
				}
				if(null ==ob[7]){
					jsonFeildList.put("reachWay", "");
				}else{
					jsonFeildList.put("reachWay", ob[7]);
				}
				jsonFeildList.put("taskInterval", ob[8]);
				
				jsonFeildList.put("hasAccept", ob[9]); /////
				if( ob[10]== null){
					jsonFeildList.put("rejectReason", "");///////////
				}else{
					jsonFeildList.put("rejectReason", ob[10]);
				}
				jsonFeildList.put("whyTransfer", ob[11]);
				TaskMsg msg  =  new TaskMsg();
				if(ob[3] != null){
					msg = (TaskMsg) lhMainBo.loadById(TaskMsg.class, ob[3].toString());
				}else{
					t++;
					continue;
				}
				
				List<TaskMsgDetail> set = taskMsgDetailBo.getListDetailTaskBytaskId(msg.getTaskId());
				String areaTaskDesc = "";//区域任务说明	
				String areaReachWay = "";//区域接近方式
				String areaTaskInterval ="";//区域任务间隔
				String areaTaskIntervalMerge ="";//合并后任务间隔
				for (TaskMsgDetail taskMsgDetail : set) {
					if(	!BasicTypeUtils.isNullorBlank(taskMsgDetail.getDestTask())){
						TaskMsg task =(TaskMsg)this.comAreaBo.loadById(TaskMsg.class,taskMsgDetail.getDestTask());
						areaTaskDesc += task.getTaskDesc()== null ?"":task.getTaskDesc()+"\n";
						areaReachWay += task.getReachWay()== null ?"":task.getReachWay()+"\n";
						areaTaskInterval += task.getTaskInterval()== null ?"":task.getTaskInterval()+"\n";
						areaTaskIntervalMerge += task.getTaskIntervalMerge()== null ?"":task.getTaskIntervalMerge()+"\n";
					}
				}
				jsonFeildList.put("areaTaskDesc",areaTaskDesc);
				jsonFeildList.put("areaReachWay",areaReachWay);
				jsonFeildList.put("areaTaskInterval",areaTaskInterval);
				jsonFeildList.put("areaTaskIntervalMerge",areaTaskIntervalMerge);
				listJsonFV.add(jsonFeildList);
			}
		}
		json.element("totalCount",  this.getPage().getTotalCount()-t);
		json.element("task", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	/**
	 * 将空字符串替换为“”
	 * @param str
	 * @return
	 */
	private String wipeNull(String str){
		if(str!=null&&!"null".equals(str)){
			return str;
		}
		return "";
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
	public IMsiMainBo getMsiMainBo() {
		return msiMainBo;
	}
	public void setMsiMainBo(IMsiMainBo msiMainBo) {
		this.msiMainBo = msiMainBo;
	}
	public ISsiMainBo getSsiMainBo() {
		return ssiMainBo;
	}
	public void setSsiMainBo(ISsiMainBo ssiMainBo) {
		this.ssiMainBo = ssiMainBo;
	}
	public ILhMainBo getLhMainBo() {
		return lhMainBo;
	}
	public void setLhMainBo(ILhMainBo lhMainBo) {
		this.lhMainBo = lhMainBo;
	}
	public String getFirstTextField() {
		return firstTextField;
	}
	public void setFirstTextField(String firstTextField) {
		this.firstTextField = firstTextField;
	}
	public String getSecondTextField() {
		return secondTextField;
	}
	public void setSecondTextField(String secondTextField) {
		this.secondTextField = secondTextField;
	}
	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}
	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}
	
}
