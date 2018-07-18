package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S3;
import com.rskytech.pojo.S3Crack;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS3Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.dao.IS3Dao;
import com.rskytech.struct.dao.IS6Dao;
import com.rskytech.task.bo.ITaskMsgBo;

@SuppressWarnings("rawtypes")
public class S3Bo extends BaseBO implements IS3Bo {
	private IS3Dao s3Dao;
	private IS4Bo s4Bo;
	private IS1Bo s1Bo;
	private IComAreaBo comAreaBo;
	private IS6Dao s6Dao;
	private ITaskMsgBo taskMsgBo;
	
	@Override
	public List getS3Records(String ssiId) throws BusinessException {
		
		return s3Dao.getS3Records(ssiId);
	}
	public IS3Dao getS3Dao() {
		return s3Dao;
	}
	public void setS3Dao(IS3Dao dao) {
		s3Dao = dao;
	}
	
	@Override
	public List getMatrixByAnaFlg(String anaFlg,String modelId) throws BusinessException {
		return this.s3Dao.getMatrixByAnaFlg(anaFlg, modelId);
	}
	
	
	@Override
	public S3Crack getS3Crack(String id) throws BusinessException {
		List<S3Crack> list=this.s3Dao.getS3Crack(id);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int getMissionCount(String modelId) throws BusinessException {
		List list=this.s3Dao.getMissionCount(modelId);
		return 	list.size();
	}
	
	@Override
	public List<String> saveS3Records(String jsonData,String ssiId,ComUser user,ComModelSeries comModelSeries)  throws BusinessException {
		int count=0;
		int isOk=0;
		S3 s3 =null;
		S3Crack s3Crack=null;
		TaskMsg taskMsg=null;
		String state=ComacConstants.DB_INSERT;
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		List<String> arr = new ArrayList<String>();
		int inCheck=0;
		int outCheck=0;
		int inOrOutCheck=0;
		SMain sMain=(SMain)this.loadById(SMain.class, ssiId);
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if(ComacConstants.DET.equals(jsonObject.get("taskType").toString())&&"0".equals(jsonObject.get("isOk").toString())){
				continue;
			}
			s3=new S3();
			if(!"null".equals(jsonObject.get("id").toString())&&jsonObject.get("id")!=null){
				s3=(S3) this.loadById(S3.class, jsonObject.getString("id"));
				state=ComacConstants.DB_UPDATE;
			}
			if(jsonObject.get("isAdEffect")!=null&&!"".equals(jsonObject.get("isAdEffect"))){
				s3.setIsAdEffect(jsonObject.getInt("isAdEffect"));
			}
			s3.setSMain((SMain)this.loadById(SMain.class,ssiId));
			s3.setTaskType(jsonObject.getString("taskType"));
			if(jsonObject.get("basicCrack")!=null&&!"".equals(jsonObject.get("basicCrack"))){
				s3.setBasicCrack(jsonObject.getDouble("basicCrack"));
			}
			if(jsonObject.get("materialSize")!=null&&!"".equals(jsonObject.get("materialSize"))){
				s3.setMaterialSize(jsonObject.getDouble("materialSize"));
			}
			if(jsonObject.get("edgeEffect")!=null&&!"".equals(jsonObject.get("edgeEffect"))){
				s3.setEdgeEffect(jsonObject.getDouble("edgeEffect"));
			}
			if(jsonObject.get("detectCrack")!=null&&!"".equals(jsonObject.get("detectCrack"))&&!"null".equals(jsonObject.get("detectCrack").toString())){
				s3.setDetectCrack(jsonObject.getDouble("detectCrack"));
			}
			if(jsonObject.get("lc")!=null&&!"".equals(jsonObject.get("lc"))){
				s3.setLc(jsonObject.getDouble("lc"));
			}
			if( jsonObject.get("lo")!=null&&!"".equals(jsonObject.get("lo"))){
				s3.setLo(jsonObject.getDouble("lo"));
			}
			if( jsonObject.get("detailCrack")!=null&&!"".equals(jsonObject.get("detailCrack"))&&!"null".equals(jsonObject.get("detailCrack").toString())){
				s3.setDetailCrack(jsonObject.getDouble("detailCrack"));
			}
			s3.setTaskInterval(jsonObject.getString("taskInterval"));
			s3.setTaskIntervalRepeat(jsonObject.getString("taskIntervalRepeat"));
			if( jsonObject.get("isOk")!=null&&!"".equals(jsonObject.get("isOk"))&&!"null".equals(jsonObject.get("isOk").toString())){
				isOk=jsonObject.getInt("isOk");
				s3.setIsOk(isOk);
			}
			if( jsonObject.get("picUrl")!=null&&!"".equals(jsonObject.get("picUrl"))&&!"null".equals(jsonObject.get("picUrl").toString())){
				s3.setPicUrl(jsonObject.getString("picUrl"));
			}
			s3.setRemark(jsonObject.getString("remark"));
			s3.setIntOut(jsonObject.getInt("intOut"));
			s3.setS1((S1) this.loadById(S1.class, jsonObject.getString("s1Id")));
			if( jsonObject.get("detailSdi")!=null&&!"".equals(jsonObject.get("detailSdi"))&&!"null".equals(jsonObject.get("detailSdi").toString())){
				s3.setDetailSdi(jsonObject.get("detailSdi").toString());
			}
			this.saveOrUpdate(s3, state, user.getUserId());
			String ataCode="";
			String tempTaskType=null;
			TaskMsg tempTaskMsg = null;
			if(isOk==1){
				if(state.equals(ComacConstants.DB_INSERT)){
					if(jsonObject.getInt("intOut")==0){
						inCheck ++;
					}else if(jsonObject.getInt("intOut")==1){
						outCheck ++;
					}else if(jsonObject.getInt("intOut")==2){
						inOrOutCheck++;
					}
				}
				String msgState = ComacConstants.DB_INSERT;
				if(sMain.getComAta()!=null&&!"".equals(sMain.getComAta().getAtaId())){
					ComAta comAta=(ComAta)this.loadById(ComAta.class,sMain.getComAta().getAtaId());
					ataCode=comAta.getAtaCode();
				}else{
					ataCode = sMain.getAddCode();
				}
				if(jsonObject.get("taskId")==null||"null".equals(jsonObject.get("taskId").toString())||"".equals(jsonObject.get("taskId").toString())){
					taskMsg=new TaskMsg();
					if(count==0){
						//得到当前在任务列表的数据总量
							count=this.getMissionCount(comModelSeries.getModelSeriesId());
							if(count==0){
								count=1;
							}
						}
						if((count+"").length()<2){
							taskMsg.setTaskCode("P"+ataCode+"-0"+(++count));
						}else{
							taskMsg.setTaskCode("P"+ataCode+"-"+(++count));
						}
				}else{
					taskMsg = (TaskMsg) this.loadById(TaskMsg.class, jsonObject.get("taskId").toString());
					msgState = ComacConstants.DB_UPDATE;
				}
				if(jsonObject.get("tempTaskId")!=null&&!"".equals(jsonObject.getString("tempTaskId"))){
					tempTaskMsg = (TaskMsg) this.loadById(TaskMsg.class, jsonObject.getString("tempTaskId"));
					tempTaskType = tempTaskMsg.getTaskType();
				}
				taskMsg.setComModelSeries(comModelSeries);
				taskMsg.setSourceSystem(ComacConstants.STRUCTURE_CODE);
				taskMsg.setSourceAnaId(ssiId);
				taskMsg.setSourceStep("S3");
				taskMsg.setTaskInterval(jsonObject.getString("taskInterval"));
				String defaultEff=sMain.getEffectiveness();
				taskMsg.setEffectiveness(defaultEff);
				taskMsg.setTaskValid(3);
				taskMsg.setValidFlag(ComacConstants.VALIDFLAG_YES);
				String areaCode=comAreaBo.getAreaIdByAreaCode(jsonObject.getString("ownArea"), comModelSeries.getModelSeriesId());
				taskMsg.setOwnArea(areaCode);
				taskMsg.setAnyContent1(jsonObject.getString("s1Id")+"FD");
				taskMsg.setTaskType(jsonObject.getString("taskType"));
				taskMsg.setAnyContent3(jsonObject.getString("intOut"));
				this.saveOrUpdate(taskMsg,msgState, user.getUserId());
				if(tempTaskType!=null&&!tempTaskType.equals(jsonObject.getString("taskType"))){
					if(tempTaskMsg!=null){
						if(s6Dao.searchTempTaskCode(tempTaskMsg.getAnyContent2(),comModelSeries.getModelSeriesId(),null).size()==1){
							TaskMsg task1 = taskMsgBo.getTaskByTaskCode(comModelSeries.getModelSeriesId(), tempTaskMsg.getAnyContent2());
							if(task1.getNeedTransfer()!=null&&task1.getNeedTransfer()==1){//更新Za7的状态
								arr.add(task1.getOwnArea());
							}
							taskMsgBo.deleteTaskMsgById(task1.getTaskId());//删除最终任务
						}
						this.delete(tempTaskMsg);
					}
				}
				if(s3.getIntOut()!=null&&!s3.getIntOut().equals(jsonObject.getString("intOut"))){
					if(jsonObject.getInt("intOut")==0){
						inCheck ++;
					}else if(jsonObject.getInt("intOut")==1){
						outCheck ++;
					}else if(jsonObject.getInt("intOut")==2){
						inOrOutCheck++;
					}
					if(msgState.equals(ComacConstants.DB_UPDATE)){
						if(s6Dao.searchTempTaskCode(taskMsg.getAnyContent2(),comModelSeries.getModelSeriesId(),null).size()==1){
							TaskMsg task1 = taskMsgBo.getTaskByTaskCode(comModelSeries.getModelSeriesId(), taskMsg.getAnyContent2());
							if(jsonObject.getInt("intOut")==0){
								task1.setAnyContent3("0");
							}else if(jsonObject.getInt("intOut")==1){
								task1.setAnyContent3("1");
							}else if(jsonObject.getInt("intOut")==2){
								task1.setAnyContent3("2");
							}
							this.update(task1,user.getUserId());
						}
					}
				}
			}else{
				if(jsonObject.get("taskId")!=null&&!"null".equals(jsonObject.get("taskId").toString())&&!"".equals(jsonObject.get("taskId").toString())){
					taskMsg = (TaskMsg) this.loadById(TaskMsg.class, jsonObject.get("taskId").toString());
					if(s6Dao.searchTempTaskCode(taskMsg.getAnyContent2(),comModelSeries.getModelSeriesId(),null).size()==1){
						TaskMsg task1 = taskMsgBo.getTaskByTaskCode(comModelSeries.getModelSeriesId(), taskMsg.getAnyContent2());
						if(task1.getNeedTransfer()!=null&&task1.getNeedTransfer()==1){//更新Za7的状态
							arr.add(task1.getOwnArea());
						}
						taskMsgBo.deleteTaskMsgById(task1.getTaskId());//删除最终任务
					}
					this.delete(taskMsg);
				}
			}
			/**
			 * 属于SDI数据
			 */
			if(!ComacConstants.SDI.equals(jsonObject.getString("taskType"))){
				if (jsonObject.get("s3cId") == null) {
					s3Crack = new S3Crack();
					state=ComacConstants.DB_INSERT;
				} else {
					if(jsonObject.get("s3cId")!=null&&!"".equals(jsonObject.get("s3cId"))){
					s3Crack = (S3Crack) this.loadById(S3Crack.class, jsonObject.getString("s3cId"));
					state=ComacConstants.DB_UPDATE;
					s3Crack.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
					s3Crack.setModifyUser(user.getUserId());}else{
						s3Crack=new S3Crack();
					}
				}
				s3Crack.setDensityLevel(jsonObject.getInt("densityLevel"));
				s3Crack.setLookLevel(jsonObject.getInt("lookLevel"));
				s3Crack.setReachLevel(jsonObject.getInt("reachLevel"));//接近等级
				s3Crack.setLightLevel(jsonObject.getInt("lightLevel"));
				s3Crack.setConditionLevel(jsonObject.getInt("conditionLevel"));
				s3Crack.setSurfaceLevel(jsonObject.getInt("surfaceLevel"));
				s3Crack.setSizeLevel(jsonObject.getInt("sizeLevel"));
				s3Crack.setDoLevel(jsonObject.getInt("doLevel"));
				s3Crack.setS3(s3);
				this.saveOrUpdate(s3Crack, state,  user.getUserId());
			}
			
		}
		String index=null;;
		List<SStep> step1=s1Bo.getSstepBySssiId((ssiId));
		boolean isS45ExistFlag = true;
		String[] S45 = {"S4A", "S4B", "S5A", "S5B"};
		for (int j = 0; j < S45.length; j++) {
			String step = S45[j];
			Integer	flag =  s4Bo.getCusStep(step,comModelSeries.getModelSeriesId());
			if(flag == null || flag == 0){
				isS45ExistFlag  = false;
			  }
			}
		step1.get(0).setS3(1);
		if(isS45ExistFlag){
			if(inOrOutCheck>0){
				if(step1.get(0).getS6In()==1||step1.get(0).getS6Out()==1){
					step1.get(0).setS6In(2);
					step1.get(0).setS6Out(2);
				}
			}else if(inCheck>0){
				if(step1.get(0).getS6In()==1){
					step1.get(0).setS6In(2);
				}
			}else if(outCheck>0){
				if(step1.get(0).getS6Out()==1){
					step1.get(0).setS6Out(2);
				}
			}
			if(step1.get(0).getS4aIn()!=3){
				if(step1.get(0).getS4aIn()!=1){
					step1.get(0).setS4aIn(2);
				}
				index="4";
			}else if(step1.get(0).getS5aIn()!=3){
				if(step1.get(0).getS5aIn()!=1){
					step1.get(0).setS5aIn(2);
				}
				index="5";
			}else if(step1.get(0).getS4bIn()!=3){
				if(step1.get(0).getS4bIn()!=1){
					step1.get(0).setS4bIn(2);
				}
				index="6";
			}else if(step1.get(0).getS5bIn()!=3){
				if(step1.get(0).getS5bIn()!=1){
					step1.get(0).setS5bIn(2);
				}
				index="7";
			}else if(step1.get(0).getS6In()!=3){
				if(step1.get(0).getS6In()!=1){
				step1.get(0).setS6In(2);}
				index="8";
			}else if(step1.get(0).getS4aOut()!=3){
				if(step1.get(0).getS4aOut()!=1){
					step1.get(0).setS4aOut(2);
				}
				index="9";
			} else if(step1.get(0).getS5aOut()!=3){
				if(step1.get(0).getS5aOut()!=1){
					step1.get(0).setS5aOut(2);
				}
				index="10";
			}  else if(step1.get(0).getS4bOut()!=3){
				if(step1.get(0).getS4bOut()!=1){
					step1.get(0).setS4bOut(2);
				}
				index="11";
			}  else if(step1.get(0).getS5bOut()!=3){
				if(step1.get(0).getS5bOut()!=1){
					step1.get(0).setS5bOut(2);
				}
				index="12";
			}
		}else{
			index="false";
			step1.get(0).setS3(1);
		}
		if(inCheck>0||outCheck>0||inOrOutCheck>0){
			if(sMain.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)){
				sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				this.update(sMain);
			}
		}
		arr.add(index);
		this.saveOrUpdate(step1.get(0), ComacConstants.DB_UPDATE, user.getUserId());
		this.saveComLogOperate(user, "S3", ComacConstants.STRUCTURE_CODE);
		return arr;
	}
	public IS1Bo getS1Bo() {
		return s1Bo;
	}
	public void setS1Bo(IS1Bo bo) {
		s1Bo = bo;
	}
	public IS4Bo getS4Bo() {
		return s4Bo;
	}
	public void setS4Bo(IS4Bo bo) {
		s4Bo = bo;
	}
	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}
	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}
	public IS6Dao getS6Dao() {
		return s6Dao;
	}
	public void setS6Dao(IS6Dao s6Dao) {
		this.s6Dao = s6Dao;
	}
	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}
	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}
	
}
