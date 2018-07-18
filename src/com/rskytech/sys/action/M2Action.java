package com.rskytech.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import com.richong.arch.action.BaseAction;
import com.richong.arch.base.BasicTypeUtils;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.basedata.bo.IComMmelBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComMmel;
import com.rskytech.pojo.M13;
import com.rskytech.pojo.M13F;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.MReferAfm;
import com.rskytech.pojo.MReferMmel;
import com.rskytech.pojo.MStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.bo.IM13Bo;
import com.rskytech.sys.bo.IM2Bo;
import com.rskytech.sys.bo.IM5Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.task.bo.ITaskMsgBo;
import com.rskytech.task.bo.ITaskMsgDetailBo;

@SuppressWarnings(value={"unchecked","rawtypes"})
public class M2Action extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String M2 = "M2";
	private IMStepBo mstepBo;
	private MStep mstep;// 导航栏步骤
	private String msiId;// Main的Id
	private ComAta showAta;// msi所属的ATA
	private String ataId; // msi所属的ATA的Id
	private String pagename;// 页面名称;
	private IM2Bo m2Bo;
	private IM13Bo m13Bo;
	private IComMmelBo comMmelBo;
	private String m13fId;// 故障影响Id
	private String afmJsonData;// 故障分析参考的afm
	private String mmelJsonData;// 故障分析参考的mmel
	private Integer isSaveAfm;// 是否要保存afm
	private Integer isSaveMmel;// 是否要保存mmel
	private String isMaintain; // 用户权限(1:修改,0:查看)
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IZa7Dao za7Dao;
	private IM5Bo m5Bo;
	private ITaskMsgBo taskMsgBo;

	public String init() {
		mstep = mstepBo.getMStepByMsiId(msiId);
		this.pagename = M2;
		showAta = (ComAta) this.mstepBo.loadById(ComAta.class, ataId);
		return SUCCESS;
	}

	/**
	 * 加载M2画面显示的数据
	 * 
	 * @return
	 */
	public String loadM2() {
		M2 m2 = this.m2Bo.getM2ByM13fId(m13fId);
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		if (m2 != null) {
			listJsonFV.add(getJsonFieldValueMap(m2));
		}
		json.element("m2", listJsonFV);
		List<String> funAndFail = new ArrayList<String>();
		M13F m13f = (M13F) this.m2Bo.loadById(M13F.class, m13fId);
		M13 m13 = m13f.getM13();
		funAndFail.add(doChangeString(m13.getFunctionDesc()));
		funAndFail.add(doChangeString(m13f.getFailureDesc()));
		funAndFail.add(doChangeString(m13f.getEffectDesc()));
		funAndFail.add(String.valueOf(m13fId));
		json.element("desc", funAndFail);

		json.element("afm", searchAfm(m2));
		json.element("mmel", searchMmel(m2));
		writeToResponse(json.toString());

		return null;
	}

	/**
	 * 截取字符串的前一百个字符
	 * 
	 * @param str
	 * @return
	 */
	private String doChangeString(String str) {
		if (str != null && str.length() > 100) {
			str = str.substring(0, 100) + "...";
		}
		if (str == null || "null".equals(str)) {
			str = "";
		}
		return str;
	}

	/**
	 * 加载画面显示的afm参考数据
	 * 
	 * @param m2
	 * @return
	 * @author chendexu createdate 2012-08-23
	 */
	public List<HashMap> searchAfm(M2 m2) {
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		HashMap jsonFeildList = new HashMap();
		if (m2 != null) {
			Set<MReferAfm> set = m2.getMReferAfms();
			for (MReferAfm afm : set) {
				jsonFeildList.put("afmId", afm.getAfmId());
				if(afm.getRefAfm()!=null){
					jsonFeildList.put("refAfm", afm.getRefAfm());
				 }else{
					 jsonFeildList.put("refAfm", ""); 
				 }
				if(afm.getReviewResult()!=null){
					jsonFeildList.put("reviewResult", afm.getReviewResult());	 
				 }else{
					 jsonFeildList.put("reviewResult", "");	 
				 }
				if (afm.getReviewDate() != null) {
					jsonFeildList.put("reviewDate", BasicTypeUtils
							.getShortFmtDate(afm.getReviewDate()));
				} else {
					jsonFeildList.put("reviewDate", "");
				}
				if(afm.getRemark()!=null){
					jsonFeildList.put("remark", afm.getRemark());
				 }else{
					 jsonFeildList.put("remark", "");
				 }
				
				listJsonFV.add(jsonFeildList);
			}
		}

		return listJsonFV;
	}

	/**
	 * 加载画面显示的MMEL参考数据
	 * 
	 * @param m2
	 * @return
	 * @author chendexu createdate 2012-08-23
	 */
	public List<HashMap> searchMmel(M2 m2) {
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		HashMap jsonFeildList = new HashMap();
		if (m2 != null) {
			Set<MReferMmel> set = m2.getMReferMmels();
			for (MReferMmel referMmel : set) {
				jsonFeildList.put("mmelId", referMmel.getMmelId());
				if(referMmel.getIsRefPmmel()!=null){
					jsonFeildList.put("isRefPmmel", referMmel.getIsRefPmmel()); 
				 }else{
					jsonFeildList.put("isRefPmmel", ""); 
				 }
		
				if(referMmel.getReviewResult()!=null){
					jsonFeildList.put("reviewResult", referMmel.getReviewResult());
				 }else{
					 jsonFeildList.put("reviewResult", "");
				 }
				
				if (referMmel.getReviewDate() != null) {
					jsonFeildList.put("reviewDate", BasicTypeUtils
							.getShortFmtDate(referMmel.getReviewDate()));
				} else {
					jsonFeildList.put("reviewDate", "");
				}
				if(referMmel.getRemark()!=null){
					jsonFeildList.put("remark", referMmel.getRemark()); 
				 }else{
					 jsonFeildList.put("remark", ""); 
				 }
				
				if(referMmel.getPmmelId()!=null){
					jsonFeildList.put("pmmelId", referMmel.getPmmelId()); 
				 }else{
					 jsonFeildList.put("pmmelId", ""); 
				 }
				
				listJsonFV.add(jsonFeildList);
			}
		}

		return listJsonFV;
	}

	/**
	 * mmel下拉框
	 * 
	 * @return
	 * @author chendexu createdate 2012-08-23
	 */
	public String mmelCombo() {
		JSONObject json = new JSONObject();
		List<HashMap> listJsonFV = new ArrayList<HashMap>();
		List<ComMmel> list = this.comMmelBo.getMmelList(this.getComModelSeries().getModelSeriesId());
		for (ComMmel comMmel : list) {
			HashMap<String, Object> jsonFeildList = new HashMap<String, Object>();
			jsonFeildList.put("id", comMmel.getMmelId());
			jsonFeildList.put("name", comMmel.getMmelName());
			listJsonFV.add(jsonFeildList);
		}
		json.element("mmelCombo", listJsonFV);
		writeToResponse(json.toString());
		return null;
	}

	public HashMap getJsonFieldValueMap(M2 m2) {
		HashMap jsonFeildList = new HashMap();
		jsonFeildList.put("m2Id", m2.getM2Id());
		jsonFeildList.put("q1", m2.getQ1());
		jsonFeildList.put("q1Desc", wipeNull(m2.getQ1Desc()));
		jsonFeildList.put("q2", m2.getQ2());
		jsonFeildList.put("q2Desc", wipeNull(m2.getQ2Desc()));
		jsonFeildList.put("q3", m2.getQ3());
		jsonFeildList.put("q3Desc", wipeNull(m2.getQ3Desc()));
		jsonFeildList.put("q4", m2.getQ4());
		jsonFeildList.put("q4Desc", wipeNull(m2.getQ4Desc()));
		jsonFeildList.put("q7", m2.getQ7());
		jsonFeildList.put("q7Desc", wipeNull(m2.getQ7Desc()));
		jsonFeildList.put("failureCauseType", m2.getFailureCauseType());
		jsonFeildList.put("mmel",  wipeNull(m2.getMmel()));
		jsonFeildList.put("remark", wipeNull(m2.getRemark()));
		jsonFeildList.put("isRefAfm", m2.getIsRefAfm());
		jsonFeildList.put("isRefMmel", m2.getIsRefMmel());

		return jsonFeildList;
	}

	/**
	 * 将null替换为“”
	 * 
	 * @param str
	 * @return
	 * @author chendexu createdate 2012-08-23
	 */
	private String wipeNull(String str) {
		if (str != null) {
			return str;
		}
		return "";
	}

	/**
	 * 保存修改后的M2数据
	 * 
	 * @param str
	 * @return
	 * @author chendexu createdate 2012-08-23
	 */
	public String saveM2() {
		String pageId = "M2";
		ArrayList<String> array = this.m2Bo.saveM2(this.getSysUser(), pageId,
										ComacConstants.SYSTEM_CODE, jsonData, msiId, m13fId,
										isSaveAfm, afmJsonData, isSaveMmel, mmelJsonData,getComModelSeries());
		this.m2Bo.updataMStep(getSysUser(), msiId, this.getComModelSeries());
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

	public String getM13fId() {
		return m13fId;
	}

	public void setM13fId(String id) {
		m13fId = id;
	}

	public String getAfmJsonData() {
		return afmJsonData;
	}

	public void setAfmJsonData(String afmJsonData) {
		this.afmJsonData = afmJsonData;
	}

	public String getMmelJsonData() {
		return mmelJsonData;
	}

	public void setMmelJsonData(String mmelJsonData) {
		this.mmelJsonData = mmelJsonData;
	}

	public Integer getIsSaveAfm() {
		return isSaveAfm;
	}

	public void setIsSaveAfm(Integer isSaveAfm) {
		this.isSaveAfm = isSaveAfm;
	}

	public Integer getIsSaveMmel() {
		return isSaveMmel;
	}

	public void setIsSaveMmel(Integer isSaveMmel) {
		this.isSaveMmel = isSaveMmel;
	}

	public IM13Bo getM13Bo() {
		return m13Bo;
	}

	public void setM13Bo(IM13Bo bo) {
		m13Bo = bo;
	}

	public String getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(String isMaintain) {
		this.isMaintain = isMaintain;
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

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

}
