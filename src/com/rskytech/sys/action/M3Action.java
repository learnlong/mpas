package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.web.util.JavaScriptUtils;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M3Additional;
import com.rskytech.pojo.MMain;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM0Bo;
import com.rskytech.sys.bo.IM3Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.pojo.M3;

@SuppressWarnings(value={"unchecked", "rawtypes"})
public class M3Action extends BaseAction {

	private static final long serialVersionUID = 1L;
	public static final String M3 = "M3";
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称;
	private String m13cId; //故障原因Id
	private String effectResult;//故障影响类别
	private String jsonTask;   //任务json
	private String taskId;     //任务Id
	private String whichPro;   //问题号
	private String isMaintain;//用户权限(1:修改,0:查看)
	private String select;    //问题回答(0:否,1:是,2:不适用)
	private String taskCode; //任务编号
	private String msiEff ;   //机型有效性
	private IM3Bo m3Bo;
	private IComAreaBo comAreaBo;
	private ITaskMsgBo taskMsgBo;
	private IMStepBo mstepBo;
	private IMsiMainBo msiMainBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	private IM5Bo m5Bo;

	public IM0Bo getM0Bo() {
		return m0Bo;
	}

	public void setM0Bo(IM0Bo m0Bo) {
		this.m0Bo = m0Bo;
	}


	private IM0Bo m0Bo;

	public String init() {
		ComUser user = getSysUser();
		if(user == null){
			return SUCCESS;
		}
		MMain mMain=this.msiMainBo.getMMainByAtaIdAndModelSeries(this.ataId,getComModelSeries().getModelSeriesId());
		M0 m0=null;
		if(mMain!=null){
			List<M0> lst1=this.m0Bo.getMsiATAListByMsiId(mMain.getMsiId());
			m0=lst1.size()>0?lst1.get(0):null;
		}
		if(m0!=null){
			msiEff = mMain.getEffectiveness()==null?getComModelSeries().getModelSeriesName():JavaScriptUtils.javaScriptEscape(mMain.getEffectiveness());
		}
		mstep = mstepBo.getMStepByMsiId(msiId);
		this.pagename = M3;
		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}

	/**
	 * 加载M3画面显示的数据
	 * @return
	 * @author chendexu
	 * createdate 2012-08-25 
	 */
	public String searchM3() {
		M13C m13c = (M13C) this.m3Bo.loadById(M13C.class, m13cId);
		M13F m13f = m13c.getM13F();
		M13 m13 = m13f.getM13();
		JSONObject json = new JSONObject();
		List<String> funAndFail = new ArrayList<String>();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		funAndFail.add(doChangeString(m13.getFunctionDesc()));
		funAndFail.add(doChangeString(m13f.getFailureDesc()));
		funAndFail.add(doChangeString(m13f.getEffectDesc()));
		funAndFail.add(doChangeString(m13c.getCauseDesc()));
		funAndFail.add(String.valueOf(m13cId));
		json.element("desc", funAndFail);
		M3 m3 = this.m3Bo.getM3ByM13cId(m13cId);
		if (m3 != null) {
			listJsonFV.add(getJsonFieldValueMap(m3));
		}
		json.element("m3", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	private HashMap getJsonFieldValueMap(M3 m3) {
		HashMap<String, Object> jsonFeildList = new HashMap();
		jsonFeildList.put("m3Id", m3.getM3Id());
		//1保养是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("baoyang", m3.getBaoyang());
		jsonFeildList.put("baoyangDesc",wipeNull( m3.getBaoyangDesc()));
		jsonFeildList.put("baoyangTaskId", m3.getBaoyangTaskId());
		//2状态检验是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("jianyan", m3.getJianyan());
		jsonFeildList.put("jianyanDesc",wipeNull( m3.getJianyanDesc()));
		jsonFeildList.put("jianyanTaskId", m3.getJianyanTaskId());
		//3用正常的操作人员监控来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("jiankong", m3.getJiankong());
		jsonFeildList.put("jiankongDesc",wipeNull( m3.getJiankongDesc()));
		jsonFeildList.put("jiankongTaskId", m3.getJiankongTaskId());
		//4用原位或离位检查来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("jiancha",m3.getJiancha());
		jsonFeildList.put("jianchaDesc", wipeNull(m3.getJianchaDesc()));
		jsonFeildList.put("jianchaTaskId", m3.getJianchaTaskId());
		//5定时拆修是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("chaixiu", m3.getChaixiu());
		jsonFeildList.put("chaixiuDesc", wipeNull(m3.getChaixiuDesc()));
		jsonFeildList.put("chaixiuTaskId", m3.getChaixiuTaskId());
		//6定时报废是适用的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("Baofei", m3.getBaofei());
		jsonFeildList.put("BaofeiDesc",wipeNull( m3.getBaofeiDesc()));
		jsonFeildList.put("BaofeiTaskId", m3.getBaofeiTaskId());
		//7有一种工作或综合工作是适用和有效的吗？的回答,解释,产生的任务Id
		jsonFeildList.put("zonghe",m3.getZonghe());
		jsonFeildList.put("zongheDesc", wipeNull(m3.getZongheDesc()));
		jsonFeildList.put("zongheTaskId", m3.getZongheTaskId());
		
		jsonFeildList.put("gaijin",m3.getGaijin());
		jsonFeildList.put("remark",wipeNull( m3.getRemark()));
		return jsonFeildList;
	}
	/**
	 * 将空字符串替换为“”
	 * @param str
	 * @return
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	private String wipeNull(String str){
		if(str!=null){
			return str;
		}
		return "";
	}
	/**
	 * 截取字符串的前一百个字符
	 * @param str
	 * @return
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	private String doChangeString(String str) {
		if (str!=null&&str.length() > 100) {
			str = str.substring(0, 100) + "...";
		}   
		if(str == null || "null".equals(str)){
			str = ""; 
		}
		return str;
	}

    /**
     * 加载由当M3产生的任务
     * @return
     * @author chendexu
	 * createdate 2012-08-25
     */
	public String searchTask() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		M3 m3 = this.m3Bo.getM3ByM13cId(m13cId);
		if (m3 != null) {
			List<String> list = new ArrayList();
			List<String> listNum = new ArrayList();
			//获取由当前M3产生任务问题顺序及任务ID
			if (!BasicTypeUtils.isNullorBlank(m3.getBaoyangTaskId())) {
				//1保养是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getBaoyangTaskId());
				listNum.add("1");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJianyanTaskId())) {
				//2状态检验是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJianyanTaskId());
				listNum.add("2");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJiankongTaskId())) {
				//3用正常的操作人员监控来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJiankongTaskId());
				listNum.add("3");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getJianchaTaskId())) {
				//4用原位或离位检查来探测功能恶化是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getJianchaTaskId());
				listNum.add("4");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getChaixiuTaskId())) {
				//5定时拆修是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getChaixiuTaskId());
				listNum.add("5");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getBaofeiTaskId())) {
				//6定时报废是适用的吗？的回答,解释,产生的任务Id
				list.add(m3.getBaofeiTaskId());
				listNum.add("6");
			}
			if (!BasicTypeUtils.isNullorBlank(m3.getZongheTaskId())) {
				//7有一种工作或综合工作是适用和有效的吗？的回答,解释,产生的任务Id
				list.add(m3.getZongheTaskId());
				listNum.add("7");
			}
			for (int i = 0; i < list.size(); i++) {
				//加载由问题一到问题7产生的任务
				TaskMsg msg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class, list.get(i));
				listJsonFV.add(getJsonFieldValueMap(msg, listNum.get(i)));
			}

			Set<M3Additional> set = m3.getM3Additionals();
			if(set.size()>0){
				for (M3Additional additional : set) {
					//获取附加任务
					TaskMsg msg = (TaskMsg) this.taskMsgBo.loadById(TaskMsg.class,additional.getAddTaskId());
					listJsonFV.add(getJsonFieldValueMap(msg, "8"));
				}
			}
		}
		json.element("task", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	/**
	 * 根据taskMsg创建一个hashMap
	 * @param msg
	 * @param whichPro
	 * @return
	 */
	private HashMap getJsonFieldValueMap(TaskMsg msg, String num) {
		HashMap hm = new HashMap();
		hm.put("taskId", msg.getTaskId());
		hm.put("hasSave", ComacConstants.YES);
		hm.put("whichPro", num);//产生任务的问题号
		hm.put("taskCode", "#&" + msg.getTaskId());// 工作号，加"#&"是为了判断是否是从数据库中取得的数据
		hm.put("taskType", msg.getTaskType());// 任务类型
		hm.put("taskDesc", wipeNull(msg.getTaskDesc()));// 任务描述
		hm.put("reachWay",wipeNull(msg.getReachWay()));//接近方式
		hm.put("effectType", effectResult);
		hm.put("effectiveness",  wipeNull(msg.getEffectiveness()));//有效性，适用性
		hm.put("zoneTransfer", wipeNull(this.comAreaBo.getAreaCodeByAreaId(msg.getOwnArea())));
		hm.put("needTransfer", msg.getNeedTransfer());
		return hm;
	}
	/**
	 * 加载当前MSI中的所有任务编号列表
	 * @return
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public String searchTaskCode(){
		JSONObject json = new JSONObject();
		String modelSeriesId = getComModelSeries().getModelSeriesId();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<TaskMsg> list = this.taskMsgBo.getTaskMsgListByMainId(modelSeriesId,
				msiId,ComacConstants.SYSTEM_CODE,M3);
		for (TaskMsg msg : list) {
			HashMap hm = new HashMap();
			hm.put("id", "#&" + msg.getTaskId());
			hm.put("code", msg.getTaskCode());
			hm.put("taskType", msg.getTaskType());//产生此条任务的任务类型
			listJsonFV.add(hm);
		}
		json.element("root", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	/**
	 * 加载下拉框中选中的任务编号所对应的任务
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public String searchChooseTask(){
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		if(jsonTask!=null&&jsonTask.length()>2){
			jsonTask=jsonTask.substring(2);
		}
		TaskMsg msg = (TaskMsg) taskMsgBo.loadById(TaskMsg.class,jsonTask);
		if(msg!=null){
			listJsonFV.add(getJsonFieldValueMap(msg, msg.getAnyContent4()));
		}else{
			HashMap hm = new HashMap();
			hm.put("notExist", true);
			listJsonFV.add(hm);
		}
		json.element("chooseTask", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}
	/**
	 * 保存M3数据
	 * @return
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public String saveM3(){
		
		String pageId="M3";
		ArrayList<String> array = this.m3Bo.saveM3(this.getSysUser(),pageId,ComacConstants.SYSTEM_CODE,jsonData,msiId,m13cId,jsonTask, getComModelSeries());
		List<TaskMsg> list = m5Bo.searchM5HeBing(msiId);
		int cou=0;
		for (TaskMsg taskMsg : list) {
			if(taskMsg.getTaskValid()!=null&&taskMsg.getTaskValid()==1){
				cou++;
			}
		}
		if(cou==0){//不存在需要合并的任务时
			List<TaskMsg> taskList= taskMsgBo.getTaskMsgListByMainId(getComModelSeries().getModelSeriesId(), 
										msiId, ComacConstants.SYSTEM_CODE, "M5");
			if(taskList.size()>0){
				TaskMsg task = taskList.get(0);
				if(task.getNeedTransfer()!=null&&task.getOwnArea()!=null&&task.getNeedTransfer()==1){
					array.add(task.getOwnArea());
				}
				this.taskMsgBo.deleteTaskMsgById(task.getTaskId());//删除合并任务
			}
			/*if(taska!=null){
				taska.setTaskValid(null);
				m5Bo.addMrbAndMpd(taska, this.getSysUser().getUserId());
				this.m3Bo.saveOrUpdate(taska);
			}*/
			
		}
		if(array!=null&&array.size()>0){
			for(String String : array){
					String[] arr = String.split(",");
					for(String string : arr){
						taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
					}
				}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		JSONObject json =  null;
	  	json = this.putJsonOKFlag(json, true);
		this.writeToResponse(json.toString());
		return null;
	}
	  

	/**
	 * 删除任务与M3问题间的关系
	 * @return
	 * @author chendexu
	 */
	public String deleteTask(){
		ArrayList<String> array = this.m3Bo.deleteTask(m13cId, select, effectResult, taskId, getSysUser(), msiId,getComModelSeries());
		List<TaskMsg> list = m5Bo.searchM5HeBing(msiId);
		int cou=0;
		for (TaskMsg taskMsg : list) {
			if(taskMsg.getTaskValid()!=null&&taskMsg.getTaskValid()==1){
				cou++;
			}
		}
		if(cou==0){//不存在需要合并的任务时
			List<TaskMsg> taskList= taskMsgBo.getTaskMsgListByMainId(getComModelSeries().getModelSeriesId(), 
										msiId, ComacConstants.SYSTEM_CODE, "M5");
			if(taskList.size()>0){
				TaskMsg task = taskList.get(0);
				if(task.getNeedTransfer()!=null&&task.getOwnArea()!=null&&task.getNeedTransfer()==1){
					array.add(task.getOwnArea());
				}
				this.taskMsgBo.deleteTaskMsgById(task.getTaskId());//删除合并任务
			}
			/*if(taska!=null){
				taska.setTaskValid(null);
				m5Bo.addMrbAndMpd(taska, this.getSysUser().getUserId());
				this.m3Bo.saveOrUpdate(taska);
			}*/
			
		}
		if(array!=null&&array.size()>0){
			for(String String : array){
					String[] arr = String.split(",");
					for(String string : arr){
						taskMsgDetailBo.updateZa7Status(getComModelSeries().getModelSeriesId(), getSysUser().getUserId(),string);
					}
				}
			za7Dao.cleanTaskInterval(getComModelSeries().getModelSeriesId());
		}
		return null;
	}
	
	/**
	 * 验证任务编号是否重复
	 * @author chendexu
	 * createdate 2012-10-18
	 */
	 public String verifyTaskCode(){
		   if (taskCode.length() >= 2 && "#&".equals(taskCode.substring(0, 2))){
			   return null;
		   }else{
			   String modelSeriesId = getComModelSeries().getModelSeriesId();
				TaskMsg taskMsg = this.taskMsgBo.getTaskByTaskCode(modelSeriesId,taskCode);
				if(taskMsg != null&&(!this.whichPro.equals(taskMsg.getAnyContent4())||
						!taskMsg.getSourceAnaId().equals(msiId))){
					writeToResponse(this.taskCode);
				}
		   }
		   return null;
	   }
	 
	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
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

	public IM3Bo getM3Bo() {
		return m3Bo;
	}

	public void setM3Bo(IM3Bo bo) {
		m3Bo = bo;
	}

	public String getM13cId() {
		return m13cId;
	}

	public void setM13cId(String id) {
		m13cId = id;
	}

	public String getEffectResult() {
		return effectResult;
	}

	public void setEffectResult(String effectResult) {
		this.effectResult = effectResult;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public String getJsonTask() {
		return jsonTask;
	}

	public void setJsonTask(String jsonTask) {
		this.jsonTask = jsonTask;
	}

	public String getWhichPro() {
		return whichPro;
	}

	public void setWhichPro(String whichPro) {
		this.whichPro = whichPro;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getMsiEff() {
		return msiEff;
	}

	public void setMsiEff(String msiEff) {
		this.msiEff = msiEff;
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

	public IM5Bo getM5Bo() {
		return m5Bo;
	}

	public void setM5Bo(IM5Bo m5Bo) {
		this.m5Bo = m5Bo;
	}

}
