package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.M13C;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.MReferMsi;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMsiMainBo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;
import com.rskytech.pojo.M13;

public class M13Action extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String M13 = "M13";
	private IMStepBo mstepBo;
	private String id;
	private MStep mstep;//导航栏步骤
	private String msiId;//Main的Id
	private ComAta showAta;//msi所属的ATA
	private String ataId;  //msi所属的ATA的Id
	private String pagename;//页面名称;
	private IM13Bo m13Bo;
	private IMsiMainBo msiMainBo;
	private String deljson;
	private String m13cId;//故障原因Id
	private String isMaintain;//用户权限(1:修改,0:查看)
	private IM2Bo m2Bo;
	private IComMmelBo comMmelBo;
	private Boolean noM2; 
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	private String nowId;
	private IM5Bo m5Bo;
	private ITaskMsgBo taskMsgBo;
	
	public String init() {
		this.pagename = M13;
		List<ComMmel> list = this.comMmelBo.getMmelList(getComModelSeries().getModelSeriesId());
		if(list.size()>0){
			noM2 = true;
		}else{
			noM2 =false;
		}
		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}

	/**
	 * 加载画面显示的数据
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22 
	 */
	public String loadM13() {
		List<M13> listM13 = this.m13Bo.getM13ListByMsiId(msiId);
		JSONObject json = new JSONObject();
		List<HashMap<String, Object>> listJson = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> jsonFeildList;
		for (M13 m13 : listM13) {
			//遍历功能
			jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("m13Id", m13.getM13Id());
			jsonFeildList.put("functionCode", m13.getFunctionCode());
			jsonFeildList.put("functionDesc", m13.getFunctionDesc());
			List<M13F> setf = m13Bo.getM13fListByM13Id(m13.getM13Id());
			if (setf.size() > 0) {
				//功能下的功能故障大于0
				for (M13F m13f : setf) {
					//遍历功能故障
					jsonFeildList.put("m13fId", m13f.getM13fId());
					jsonFeildList.put("failureCode", m13f.getFailureCode());
					jsonFeildList.put("failureDesc", m13f.getFailureDesc());
					jsonFeildList.put("effectCode", m13f.getEffectCode());
					jsonFeildList.put("effectDesc", m13f.getEffectDesc());
					List<M13C> setc = m13Bo.getM13cListByM13FId(m13f.getM13fId());
					if (setc.size() > 0) {
						//功能故障下的故障原因大于0
						for (M13C m13c : setc) {
							//遍历故障原因
							jsonFeildList.put("m13cId", m13c.getM13cId());
							jsonFeildList.put("causeCode", m13c.getCauseCode());
							jsonFeildList.put("causeDesc", m13c.getCauseDesc());
							jsonFeildList.put("msetfId", m13c.getMsetfId());
							jsonFeildList.put("isRef", m13c.getIsRef());
							listJson.add(jsonFeildList);
							jsonFeildList = new HashMap<String, Object>();
						}
					} else {//功能故障下的故障原因不大于0
						listJson.add(jsonFeildList);
						jsonFeildList = new HashMap<String, Object>();
					}
				}
			} else {//功能下的功能故障不大于0
				listJson.add(jsonFeildList);
				jsonFeildList = new HashMap<String, Object>();
			}
		}
		json.element("m13", listJson);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 保存操作
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22 
	 */
	public String saveM13() {
		String pageId="M1.3";
		this.m13Bo.saveOrUpdateM13( this.getSysUser(), ComacConstants.SYSTEM_CODE, 
									pageId, msiId, jsonData,true,this.getComModelSeries());
		this.m13Bo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
		return null;

	}
	
	/**
	 * 暂存操作
	 * @return
	 * @author zhangjianmin
	 * createdate 2015-6-15
	 */
	public String saveZan() {
		String pageId="M1.3";
		this.m13Bo.saveOrUpdateM13( this.getSysUser(), ComacConstants.SYSTEM_CODE, 
									pageId, msiId, jsonData,true,this.getComModelSeries());
		return null;
	}
	
	/**
	 * 删除操作
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	public String delM13() {
		String pageId="M1.3";
		ArrayList<String> array = this.m13Bo.deleteM13AndSave(this.deljson, this.getSysUser(), ComacConstants.SYSTEM_CODE, 
												pageId, msiId, jsonData,this.getComModelSeries());
		this.m13Bo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
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
	 * 初始化添加MSI选择窗口
	 * @author chendexu
	 * createdate 2012-08-22
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String searchRef() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		MReferMsi ref = m13Bo.getMReferMsiByM13cId(id);
		if (ref != null) {
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("refId", ref.getRefId());
			jsonFeildList.put("refMsiId", ref.getRefMsiId());//参考的MSIId
			jsonFeildList.put("refFunctionId", ref.getRefFunctionId());//参考的功能Id
			jsonFeildList.put("refFailureId", ref.getRefFailureId()); //参考的故障Id
			jsonFeildList.put("refEffectId", ref.getRefEffectId());//参考的故障影响Id
			jsonFeildList.put("refCauseId", ref.getRefCauseId());  //参考的故障原因Id
			jsonFeildList.put("isAna", ref.getIsAna()); //是否分析完成中文
			jsonFeildList.put("remark", ref.getRemark());//备注中文
			listJsonFV.add(jsonFeildList);
		}
		json.element("ref", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * msi下拉框
	 * @author chendexu
	 * createdate 2012-08-22
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String msiCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<Object[]> list = msiMainBo.getMSIAll(getComModelSeries().getModelSeriesId());
		for (Object[] objects : list) {
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", objects[0]);
			jsonFeildList.put("name", objects[1]);
			listJsonFV.add(jsonFeildList);
		}
		json.element("msi", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 故障下拉框
	 * @author chendexu
	 * createdate 2012-08-22 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String failureCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<M13F> list = m13Bo.getM13fListByM13Id(id);
		for (M13F m13f : list) {
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", m13f.getM13fId());
			jsonFeildList.put("name", m13f.getFailureDesc());
			listJsonFV.add(jsonFeildList);
		}
		json.element("failure", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 功能下拉框
	 * @author chendexu
	 * createdate 2012-08-22
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String functionCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<M13> list = m13Bo.getM13ListByMsiId(id);
		for (M13 m13 : list) {
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", m13.getM13Id());
			jsonFeildList.put("name", m13.getFunctionDesc());
			listJsonFV.add(jsonFeildList);
		}
		json.element("function", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 影响下拉框
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	@SuppressWarnings("rawtypes")
	public String effectCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		M13F m13f = (M13F) this.m13Bo.loadById(M13F.class, id);
		HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
		jsonFeildList.put("id", m13f.getM13fId());
		jsonFeildList.put("name", m13f.getEffectDesc());
		listJsonFV.add(jsonFeildList);
		json.element("effect", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 原因下拉框
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22
	 */
	@SuppressWarnings("rawtypes")
	public String causeCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<M13C> list = m13Bo.getM13cListByM13FId(id);
		for (M13C m13c : list) {
			if(nowId!=null&&nowId.equals(m13c.getM13cId())){
				continue;
			}
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", m13c.getM13cId());
			jsonFeildList.put("name", m13c.getCauseDesc());
			listJsonFV.add(jsonFeildList);
		}
		json.element("cause", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	/**
	 * 保存Ref
	 * @return
	 * @author chendexu
	 * createdate 2012-08-22 
	 */
	public String saveRef() {
		ArrayList<String> array = this.m13Bo.saveRef(jsonData,m13cId,this.getSysUser(),msiId,this.getComModelSeries());
		this.m13Bo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
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
	

	public String delRef() {
		this.m13Bo.deleteRef(id, m13cId,getSysUser());
		this.m13Bo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
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

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo bo) {
		m13Bo = bo;
	}

	public String getDeljson() {
		return deljson;
	}

	public void setDeljson(String deljson) {
		this.deljson = deljson;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getM13cId() {
		return m13cId;
	}

	public void setM13cId(String id) {
		m13cId = id;
	}
	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
	}

	public IM2Bo getM2Bo() {
		return m2Bo;
	}

	public void setM2Bo(IM2Bo bo) {
		m2Bo = bo;
	}

	public IComMmelBo getComMmelBo() {
		return comMmelBo;
	}

	public void setComMmelBo(IComMmelBo comMmelBo) {
		this.comMmelBo = comMmelBo;
	}

	public Boolean getNoM2() {
		return noM2;
	}

	public void setNoM2(Boolean noM2) {
		this.noM2 = noM2;
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

	public String getNowId() {
		return nowId;
	}

	public void setNowId(String nowId) {
		this.nowId = nowId;
	}

	public IM5Bo getM5Bo() {
		return m5Bo;
	}

	public void setM5Bo(IM5Bo m5Bo) {
		this.m5Bo = m5Bo;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	
}
