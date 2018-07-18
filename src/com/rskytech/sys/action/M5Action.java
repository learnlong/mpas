package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;


@SuppressWarnings(value={"unchecked","rawtypes"})
public class M5Action extends BaseAction {
	private static final long serialVersionUID = 1L;
	public static final String M5 = "M5";
	private IMStepBo mstepBo;
	private String msiId;
	private ComAta showAta;
	private String ataId;
	private String pagename;
	private ITaskMsgBo taskMsgBo;
	private IM5Bo m5Bo;
	private IComAreaBo comAreaBo;
	private String deleteId;
	private String isMaintain;
	private String msiEff ;
	private String taskCode;
	private String taskId;
	private IMsiMainBo msiMainBo;
	private IM0Bo m0Bo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;

	public IM0Bo getM0Bo() {
		return m0Bo;
	}


	public void setM0Bo(IM0Bo m0Bo) {
		this.m0Bo = m0Bo;
	}


	public String init() {
		this.pagename = M5;
		ComUser thisUser = getSysUser();
		if (thisUser == null){
			return SUCCESS;
		}
		
		
		MMain mMain1=this.msiMainBo.getMMainByAtaIdAndModelSeries(this.ataId,getComModelSeries().getModelSeriesId());
		M0 m0=null;
		if(mMain1!=null){
			List<M0> lst1=this.m0Bo.getMsiATAListByMsiId(mMain1.getMsiId());
			m0=lst1.size()>0?lst1.get(0):null;
		}
		if(m0!=null){
			msiEff = mMain1.getEffectiveness()==null?"":JavaScriptUtils.javaScriptEscape(mMain1.getEffectiveness());
		}
		MStep mStep =(MStep)this.mstepBo.getMStepByMsiId(msiId);
		MMain mMain =mStep.getMMain();
		//如果状态为“正在分析"设置状态为"分析已完成"
		if(ComacConstants.STEP_NOW.equals(mStep.getM5())||ComacConstants.ANALYZE_STATUS_MAINTAIN.equals(mMain.getStatus())){
			mMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
			mStep.setM5(ComacConstants.STEP_FINISH);
			this.mstepBo.update(mStep, getSysUser().getUserId());
		}

		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}


	/**
	 * 初始化M5
	 * @return
	 * @author chendexu
	 * createdate 2012-08-30
	 */
	public String loadM5() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<TaskMsg> listTask = this.taskMsgBo.getTaskMsgListByMainId(getComModelSeries().getModelSeriesId(),
				msiId,ComacConstants.SYSTEM_CODE,null);
		for (TaskMsg taskMsg : listTask) {
			HashMap map = new HashMap();
			map.put("taskId", taskMsg.getTaskId());
			map.put("taskCode", wipeNull(taskMsg.getTaskCode()));
			map.put("taskType", wipeNull(taskMsg.getTaskType()));// 任务类型（GVI、DET...）
			map.put("ownArea", wipeNull(this.comAreaBo.getAreaCodeByAreaId(taskMsg.getOwnArea())));// 区域
			map.put("taskDesc", wipeNull(taskMsg.getTaskDesc()));// 任务描述
			map.put("reachWay", wipeNull(taskMsg.getReachWay()));// 接近方式
			map.put("taskInterval", wipeNull(taskMsg.getTaskInterval()));// 任务间隔
			map.put("effectiveness", wipeNull(taskMsg.getEffectiveness()));// 有效性，适用性
			if (ComacConstants.YES.equals(taskMsg.getNeedTransfer())) {
				map.put("needTransfer", ComacConstants.YES);// 是否转移
			} else {
				map.put("needTransfer", ComacConstants.NO);
			}
			map.put("hasAccept", taskMsg.getHasAccept());//是否接收
			map.put("whyTransfer", wipeNull(taskMsg.getWhyTransfer()));// 转移原因
			map.put("rejectReason", wipeNull(taskMsg.getRejectReason()));// 退回原因
			String causeCode = "";// 故障原因
			String causeType = "";// 故障类别
			
			if ("M3".equals(taskMsg.getSourceStep())) {
				// 获取任务的故障影响类别及故障原因编号
				List<Object[]> list = this.m5Bo.getCauseCodeAndCauseTypeByTaskId(taskMsg.getTaskId());
				if (list != null) {
					for (Object[] objects : list) {
						causeCode += objects[0] + "\n";//故障原因编号
						causeType += objects[1]+ "\n";//故障影响类别
					}
				}
				if (!causeCode.equals(taskMsg.getAnyContent2())
						|| !causeType.equals(taskMsg.getAnyContent3())) {
					if(causeCode.length()>1){
						causeCode = causeCode.substring(0, causeCode.length()-1);
					}
					if(causeType.length()>1){
						causeType = causeType.substring(0, causeType.length()-1);
					}
					// 同步修改任务编号与故障类型
					taskMsg.setAnyContent2(causeType);
					taskMsg.setAnyContent3(causeCode);
					this.taskMsgBo.update(taskMsg, getSysUser().getUserId());
				}
			}
			if("M5".equals(taskMsg.getSourceStep())){
				List<TaskMsg> list = this.m5Bo.searchM5HeBing(taskMsg.getSourceAnaId());
				for(TaskMsg task : list){
					causeType += task.getAnyContent2() + "\n";//故障原因编号
					causeCode += task.getAnyContent3()+ "\n";//故障影响类别
				}
				if (!causeCode.equals(taskMsg.getAnyContent2())
						|| !causeType.equals(taskMsg.getAnyContent3())) {
					// 同步修改任务编号与故障类型
					if(causeCode.length()>1){
						causeCode = causeCode.substring(0, causeCode.length()-1);
					}
					if(causeType.length()>1){
						causeType = causeType.substring(0, causeType.length()-1);
					}
					taskMsg.setAnyContent2(causeType);
					taskMsg.setAnyContent3(causeCode);
					this.taskMsgBo.update(taskMsg, getSysUser().getUserId());
				}
			}
			map.put("causeType", wipeNull(taskMsg.getAnyContent2()));
			map.put("causeCode", wipeNull(taskMsg.getAnyContent3()));
			map.put("merge", wipeNull(taskMsg.getAnyContent1()));
			if (ComacConstants.YES.equals(taskMsg.getTaskValid())) {
				// 是否是被合并的任务合并
				map.put("isHeBing", ComacConstants.YES);
			} else if (taskMsg.getAnyContent1() == null) {
				// 不是被合并任务且不是合并后的任务
				map.put("isHeBing", ComacConstants.NO);
			}
			listJsonFV.add(map);
		}
		json.element("m5", listJsonFV);
		writeToResponse(json.toString());
		return null;

	}
	/**
	 * 保存M5
	 * @return
	 * @author chendexu
	 * createdate 2012-08-30
	 */
	public String saveM5() {
		ArrayList<String> arr=this.m5Bo.saveM5(this.getSysUser(),ComacConstants.SYSTEM_CODE,jsonData,msiId,M5, getComModelSeries());
		if(arr.size()>1){
			String[] areaId=null;
			for(int i =0;i<arr.size()-1;i++){
				areaId = arr.get(i).split(",");
				for(String str : areaId){
					taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(), str);
				}
			}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		boolean flag = true;
		if(arr.get(arr.size()-1).equals("yes")){
			flag = false;
		}
		JSONObject json =  this.putJsonOKFlag(null, flag);
		this.writeToResponse(json.toString());
		return null;
	}



	/**
	 * 删除合并任务
	 * @return
	 * @author chendexu
	 * createdate 2012-08-30 
	 */
	public String deleteM5HeBing() {
		this.m5Bo.deleteM5HeBing(deleteId, getSysUser(), getComModelSeries());
	
		return null;
	}


	/**
	 * 验证任务编号是否重复
	 * @author chendexu
	 * createdate 2012-10-18
	 */
	 public String verifyTaskCode(){
		String modelSeriesId =getComModelSeries().getModelSeriesId();
		TaskMsg taskMsg = this.taskMsgBo.getTaskByTaskCode(modelSeriesId,taskCode);
		if(taskMsg != null && !taskMsg.getTaskId().equals(taskId)){
			writeToResponse(this.taskCode);
		}
		 return null;
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

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}
	public String getMsiId() {
		return msiId;
	}

	public void setMsiId(String msiId) {
		this.msiId = msiId;
	}

	public ComAta getShowAta() {
		return showAta;
	}

	public void setShowAta(ComAta showAta) {
		this.showAta = showAta;
	}

	public String getAtaId() {
		return ataId;
	}

	public void setAtaId(String ataId) {
		this.ataId = ataId;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public IM5Bo getM5Bo() {
		return m5Bo;
	}

	public void setM5Bo(IM5Bo bo) {
		m5Bo = bo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String getDeleteId() {
		return deleteId;
	}

	public void setDeleteId(String deleteId) {
		this.deleteId = deleteId;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getMsiEff() {
		return msiEff;
	}


	public void setMsiEff(String msiEff) {
		this.msiEff = msiEff;
	}


	public String getTaskCode() {
		return taskCode;
	}


	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}


	public String getTaskId() {
		return taskId;
	}


	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IMsiMainBo getMsiMainBo() {
		return msiMainBo;
	}

	public void setMsiMainBo(IMsiMainBo msiMainBo) {
		this.msiMainBo = msiMainBo;
	}

	public ITaskMsgDetailBo getTaskMsgDetailBo() {
		return taskMsgDetailBo;
	}

	public void setTaskMsgDetailBo(ITaskMsgDetailBo taskMsgDetailBo) {
		this.taskMsgDetailBo = taskMsgDetailBo;
	}

	public IZa7Dao getZa7Dao() {
		return za7Dao;
	}

	public void setZa7Dao(IZa7Dao za7Dao) {
		this.za7Dao = za7Dao;
	}
	
}
