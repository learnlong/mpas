package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M4;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM4Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
@SuppressWarnings(value={"unchecked","rawtypes"})
public class M4Action extends BaseAction {
	private static final long serialVersionUID = 1L;
	public static final String M4 = "M4";
	private MStep mstep;// 导航栏步骤
	private String msiId;// Main的Id
	private ComAta showAta;// msi所属的ATA
	private String ataId; // msi所属的ATA的Id
	private String pagename;// 页面名称;
	private String taskId;
	private IMStepBo mstepBo;
	private IM4Bo m4Bo;
	private String isMaintain; // 用户权限(1:修改,0:查看)
	private ITaskMsgBo taskMsgBo;
	public String init(){
	    mstep = mstepBo.getMStepByMsiId(msiId);
		this.pagename = M4;
		showAta = (ComAta)this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}
	/**
	 * 加载M4显示的数据
	 * 
	 * @return
	 * @author chendexu createdate 2012-08-29
	 */
	public String searchM4() {
		JSONObject json = new JSONObject();
		List<String> desc = new ArrayList<String>();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<M2> listM2 = this.m4Bo.getListM2ByTaskId(taskId);
		Set<Integer> set = new TreeSet<Integer>();
		for (M2 m2 : listM2) {
			// 获取无重复值的故障影响类别
			set.add(m2.getFailureCauseType());
		}
		String str = "";
		for (Integer integer : set) {
			// 将多个故障影响类别用/隔开
			str += "/" + integer;
		}
		TaskMsg taskMsg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class,
				taskId);
		desc.add(wipeNull(taskMsg.getEffectiveness()));// 任务有效性
		desc.add(wipeNull(taskMsg.getTaskCode())); // 任务编号
		desc.add(wipeNull(taskMsg.getTaskDesc())); // 任务名称说明中文
		if (str.length() > 0) {
			desc.add(str.substring(1));
		} else {
			desc.add(str);
		}
		desc.add(wipeNull(taskMsg.getTaskInterval()));
		desc.add(taskMsg.getTaskId().toString());
		json.element("desc", desc);
		M4 m4 = this.m4Bo.getM4ByTaskId(taskId);
		if (m4 != null) {
			listJsonFV.add(getJsonFieldValueMap(m4));
		}
		json.element("m4", listJsonFV);
		writeToResponse(json.toString());
		return null;

	}
	private HashMap getJsonFieldValueMap(M4 m4) {
		HashMap<String, Object> jsonFeildList = new HashMap();
		jsonFeildList.put("m4Id", m4.getM4Id());
		jsonFeildList.put("ana", wipeNull(m4.getAna()));// 分析结果
		jsonFeildList.put("similar", wipeNull(m4.getSimilar()));// 类似机型
		jsonFeildList.put("engineerReview",
				wipeNull(m4.getEngineerReview()));// 工程师评论
		jsonFeildList.put("engineerSuggest", wipeNull(m4
				.getEngineerSuggest()));// 工程师建议
		jsonFeildList.put("groupReview", wipeNull(m4.getGroupReview()));// 局方建议
		jsonFeildList.put("other", wipeNull(m4.getOther()));// 其他
		jsonFeildList.put("remark", wipeNull(m4.getRemark()));// 备注
		return jsonFeildList;
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
	/**
	 * 保存修改的M4数据及任务表中的间隔时间字段
	 * @return
	 * @author chendexu
	 * createdate 2012-08-29
	 */
	public String saveM4(){
		String pageId="M4";
		this.m4Bo.saveM4(this.getSysUser(),ComacConstants.SYSTEM_CODE,pageId,jsonData,taskId,msiId,getComModelSeries());
		return null;
	}
	public MStep getMstep() {
		return mstep;
	}
	public void setMstep(MStep mstep) {
		this.mstep = mstep;
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public IMStepBo getMstepBo() {
		return mstepBo;
	}
	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}
	public IM4Bo getM4Bo() {
		return m4Bo;
	}
	public void setM4Bo(IM4Bo m4Bo) {
		this.m4Bo = m4Bo;
	}
	public ITaskMsgBo gettaskMsgBo() {
		return taskMsgBo;
	}
	public void settaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	public String getIsMaintain() {
		return isMaintain;
	}
	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}
	
}
