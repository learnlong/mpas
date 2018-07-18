package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS7Bo;
import com.rskytech.struct.bo.IS8Bo;
import com.rskytech.struct.dao.IS7Dao;
import com.rskytech.task.bo.ITaskMsgDetailBo;

public class S7Bo extends BaseBO implements IS7Bo {
	private IS1Bo s1Bo;
	private IComCoordinationBo comCoordinationBo;
	private IComAreaBo comAreaBo;
	private ITaskMsgDetailBo taskMsgDetailBo;
	private IS7Dao s7Dao;
	private IS8Bo s8Bo;

	/**
	 * 得到S7grid页面需要的数据
	 * @param ssiId 组成ID
	 * @return 任务列表
	 * @throws BusinessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<HashMap> getS7Records(String ssiId) throws BusinessException {
		List<TaskMsg> list=this.s7Dao.getS7Records(ssiId);
		HashMap jsonFieldList=null;
		List<HashMap> jsonList = new ArrayList();
		String areaCode=null;
		if(list!=null&&!list.isEmpty()){
			for(TaskMsg tm:list){
				if(tm.getOwnArea()!=null){
					areaCode=this.comAreaBo.getAreaCodeByAreaId(tm.getOwnArea());
				}
				jsonFieldList= new HashMap();	
				jsonFieldList.put("taskId",tm.getTaskId());
				jsonFieldList.put("taskCode", wipeNull(tm.getTaskCode()));
				jsonFieldList.put("hasInter", wipeNull(tm.getAnyContent3()));//内外部
				jsonFieldList.put("oneZone", wipeNull(areaCode));//区域
				jsonFieldList.put("taskDesc", wipeNull(tm.getTaskDesc()));//任务描述
				jsonFieldList.put("rejectReason", wipeNull(tm.getRejectReason()));
				jsonFieldList.put("whyTransfer", wipeNull(tm.getWhyTransfer()));
				jsonFieldList.put("needTransferStr", tm.getNeedTransfer());//是否区域候选
				String str = "";
				if (tm.getHasAccept() == null){
					str = "";
				} else if (tm.getHasAccept() == 1){
					str = "是";
				} else if (tm.getHasAccept() == 0){
					str = "否";
				}
				jsonFieldList.put("hasAcceptStr",str);//区域是否接受
				jsonList.add(jsonFieldList);
			}
		}
		return jsonList;
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
	/**
	 *解析字符串里面的数字
	 * @param str 传进来的字符串
	 * @return
	 */
	 public  Integer getNumsFromStr(String str) throws BusinessException {

		  String[] ary = str.replaceAll("[^\\d]", " ").split("\\s+");
		  
		  Set<Integer> set = new TreeSet<Integer>();
		  
		  for(String num: ary){
		   if(!num.trim().equals("")){
		    set.add(new Integer(num.trim()));
		   }
		  }
		  for (Iterator<Integer> iter = set.iterator(); iter.hasNext();) {
			   Integer element = (Integer) iter.next();
			    return element;
			  }
		  return null;
		 }
	 
	@Override
	public Page getStructToAreaRecords(Integer needTransfer, Integer hasAccept,Page page,String modelId)
			throws BusinessException {
		return this.s7Dao.getStructToAreaRecords(needTransfer, hasAccept, page, modelId);
	}
	
	@Override
	public ArrayList<String> saveS7Records(String jsonData,String ssiId,ComUser user,
									String remarkId,String remark,String modelSeriesId) throws BusinessException {
		TaskMsg tm=null;
		SMain s=(SMain)this.loadById(SMain.class, ssiId);
		s.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
		s.setModifyUser(user.getUserId());
		s.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
		this.saveOrUpdate(s, ComacConstants.DB_UPDATE, user.getUserId());
		SRemark sr =(SRemark)this.loadById(SRemark.class, remarkId);
		String dbOperate = ComacConstants.DB_INSERT;
		if(sr==null){
			sr=new SRemark();
		}else{
			 dbOperate = ComacConstants.DB_UPDATE;
		}
		sr.setS7Remark(remark);
		this.saveOrUpdate(sr,dbOperate,user.getUserId());
		JSONObject jsonObject = null;
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0;i<jsonArray.size();i++){
			jsonObject = jsonArray.getJSONObject(i);
			tm=(TaskMsg)this.loadById(TaskMsg.class, jsonObject.getString("taskId"));
			String areaId=this.comAreaBo.getAreaIdByAreaCode(jsonObject.getString("oneZone"),modelSeriesId);
			tm.setOwnArea(areaId);
			tm.setTaskDesc(jsonObject.getString("taskDesc"));
			tm.setWhyTransfer(jsonObject.getString("whyTransfer"));
			if(BasicTypeUtils.isNumberString(jsonObject.getString("needTransferStr"))){
				if(tm.getNeedTransfer()==null){
					if(jsonObject.getInt("needTransferStr")==1){
						if(areaId!=null){
							arr.add(areaId);
						}
					}
				}else{
					if(tm.getNeedTransfer()!=jsonObject.getInt("needTransferStr")){
						if(areaId!=null){
							arr.add(areaId);
						}
					}
				}
				tm.setNeedTransfer(jsonObject.getInt("needTransferStr"));
				if(jsonObject.getInt("needTransferStr")==1){
					//如果任务区域和协调单区域不相同，将协调单的接收区域改为msg-3任务区域
					this.comCoordinationBo.modifyCoordination(jsonObject.getString("taskId"), user.getUserId(), jsonObject.getString("oneZone"), modelSeriesId);
					tm.setSysTransfer(ComacConstants.ZONAL_CODE);
				}
				if(jsonObject.getInt("needTransferStr")==0){
					//删除msg-3任务对应的协调单
					comCoordinationBo.deleteCoordination(tm.getTaskId(), user.getUserId(), modelSeriesId);
					tm.setSysTransfer(null);// 转移到的系统
					tm.setHasAccept(null);
					if (tm.getTaskValid()!=null&&tm.getTaskValid() == 2){
						tm.setTaskValid(null);
					}
					tm.setRejectReason(null);
					tm.setDestTask(null);
				}
			}
			this.update(tm, user.getUserId());
			if (ComacConstants.YES.equals(tm.getNeedTransfer())) {
				// 任务转移到区域
				taskMsgDetailBo.addTaskMsgDetail(tm, user.getUserId());
			} else {
				taskMsgDetailBo.delTaskMsgDetail(tm,user.getUserId());
				//删除msg-3任务对应的协调单
				comCoordinationBo.deleteCoordination(tm.getTaskId(), user.getUserId(),modelSeriesId);
			}
		}		
		//流程变换
		String index = null;
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		SStep step = step1.get(0);
		step.setS7(1);
		if(s8Bo.getS8Records(ssiId, "S8")!=null){
			step.setS8(1);
			index ="15";
		}else{
			step.setS8(3);
			index ="100";
		}
		if(step.getS1()==0||step.getS1()==2||step.getS2()==0||step.getS2()==2||step.getS3()==0||step.getS3()==2
				||step.getS4aIn()==0||step.getS4aIn()==2||step.getS4bIn()==0||step.getS4bIn()==2
				||step.getS4aOut()==0||step.getS4aOut()==2||step.getS4bOut()==0||step.getS4bOut()==2
				||step.getS5aIn()==0||step.getS5aIn()==2||step.getS5bIn()==0||step.getS5bIn()==2
				||step.getS5aOut()==0||step.getS5aOut()==2||step.getS5bOut()==0||step.getS5bOut()==2
				||step.getS6In()==0||step.getS6In()==2||step.getS6Out()==0||step.getS6Out()==2){
			index="101";
		}
		this.saveOrUpdate(step, ComacConstants.DB_UPDATE, user.getUserId());
		saveComLogOperate(user, "S7", ComacConstants.STRUCTURE_CODE);
		arr.add(index);
		return arr;
	}
	
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo bo) {
		s1Bo = bo;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
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
	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}
	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}
	public IS8Bo getS8Bo() {
		return s8Bo;
	}
	public void setS8Bo(IS8Bo s8Bo) {
		this.s8Bo = s8Bo;
	}
	
}
