package com.rskytech.struct.bo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.bo.IComAreaBo;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.S3;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SRemark;
import com.rskytech.pojo.SStep;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.process.bo.IComCoordinationBo;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS2Bo;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.bo.IS5Bo;
import com.rskytech.struct.bo.IS6Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.struct.bo.ISyBo;
import com.rskytech.struct.dao.IS1Dao;
import com.rskytech.struct.dao.IS6Dao;
import com.rskytech.task.bo.ITaskMsgBo;

public class S1Bo extends BaseBO implements IS1Bo {
	private IS1Dao s1Dao;
	private ITaskMsgBo taskMsgBo;
	private IS6Dao s6Dao;
	private IComAreaBo comAreaBo;
	private ISsiStepBo ssiStepBo;
	private IComCoordinationBo comCoordinationBo;
	private IS2Bo s2Bo;
	private IS4Bo s4Bo;
	private IS5Bo s5Bo;
	private IS6Bo s6Bo;
	private ISyBo syBo;
	
	@Override
	public SRemark getRemarkBySssi(String sssiId)
			throws BusinessException {
		List<SRemark> list=this.s1Dao.getRemarkBySssi(sssiId);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public List<S1> getS1Records(String sssiId) throws BusinessException {
		return this.s1Dao.getS1ListBySssiId(sssiId);
	}
	
	@Override
	public List<SStep> getSstepBySssiId(String sssiId) throws BusinessException {
		return this.s1Dao.getSstepBySssiId(sssiId);
	}

	/**
	 *删除临时任务或者最终任务
	 */
	private void deleteTask(ArrayList<String> arr,List<TaskMsg> taskList,String modelSeriesId){
		if(taskList.size()>0){
			TaskMsg task;
			for(TaskMsg tsk : taskList){
				task = (TaskMsg) taskMsgBo.loadById(TaskMsg.class, tsk.getTaskId());
				if(s6Dao.searchTempTaskCode(tsk.getAnyContent2(),modelSeriesId,null).size()==1){//只存在一条合并任务
					TaskMsg task1 = taskMsgBo.getTaskByTaskCode(modelSeriesId, tsk.getAnyContent2());
					taskMsgBo.deleteTaskMsgById(task1.getTaskId());//删除最终任务
					if(task1.getNeedTransfer()!=null&&task1.getNeedTransfer()==1){
						arr.add(task1.getOwnArea());
					}
				}
				taskMsgBo.delete(task);//删除临时任务
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> saveS1(String ssiId,String s1Remark,String jsonData,String defaultEff
			,ComUser user,String modelSeriesId,String modifiedJson){
		SRemark sRmaker = this.getRemarkBySssi(ssiId);
		//保存备注信息
		if(sRmaker!=null){
			sRmaker.setS1Remark(s1Remark);
			this.saveOrUpdate(sRmaker, ComacConstants.DB_UPDATE, user.getUserId());
		}else{
			sRmaker = new SRemark();
			sRmaker.setS1Remark(s1Remark);
			sRmaker.setSMain((SMain) this.loadById(SMain.class, ssiId));
			this.saveOrUpdate(sRmaker, ComacConstants.DB_INSERT, user.getUserId());
		}
		List<SStep> step1=this.getSstepBySssiId(ssiId);
		SStep step2 = step1.get(0);
		int oldIn = 0;
		int oldOut = 0;
		int oldNonmetalIn = 0;
		int oldNonmetalOut = 0;
		int oldMetalOut =0;
		int oldMetalIn =0;
		int oldMetalDamage = 0;
		List<S1> s1List = this.getS1Records(ssiId);
		if(s1List!=null&&s1List.size()>0){
			for(S1 s : s1List){
				if(s.getIsMetal()==1){//是金属
					if(s.getDesignPri()==2&&null!=s.getIsFD()&&s.getIsFD()==1){
						oldMetalDamage++;
					}
					if(s.getInternal() == 1){
						oldMetalIn++;
					}
					if(s.getOuternal() == 1){
						oldMetalOut++;
					}
				}else{
					if(s.getInternal() == 1){
						oldNonmetalIn++;
					}
					if(s.getOuternal() == 1){
						oldNonmetalOut++;
					}
				}
				if(s.getInternal() == 1){
					oldIn++;
				}
				if(s.getOuternal() == 1){
					oldOut++;
				}
			}
		}else{
			step2=ssiStepBo.changeStep(step2,ssiId);
		}
		
		JSONArray jsonArray = JSONArray.fromObject(modifiedJson);
		JSONObject jsonObject = null;
		Object[] obj=null;
		//保存适用性和分析状态
		SMain sMain=(SMain)this.loadById(SMain.class, ssiId);
		sMain.setEffectiveness(defaultEff);
		sMain.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		this.saveOrUpdate(sMain, ComacConstants.DB_UPDATE, user.getUserId());
		int needChange =0;
		int needChangeDamage =0;
		String status = ComacConstants.DB_INSERT;
		S1 s1;
		ArrayList<String> arr = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.getString("id")!=null&&!"".equals(jsonObject.getString("id"))){	
				status=ComacConstants.DB_UPDATE;
				s1=(S1)this.loadById(S1.class, jsonObject.getString("id"));
				if(jsonObject.getInt("isMetal")!=s1.getIsMetal()){//是否为金属的值发生改变
					needChange++;
					List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),null,null);
					deleteTask(arr, taskList,modelSeriesId);
					obj=s1.getS3s().toArray();
					for(Object o:obj){
						this.delete((S3)o);
					}
					obj=s1.getS4s().toArray();
					for(Object o:obj){
						this.delete((S4)o);
					}
					obj=s1.getSys().toArray();
					for(Object o:obj){
						this.delete((Sy)o);
					}
					obj=s1.getS5s().toArray();
					for(Object o:obj){
						this.delete((S5)o);
					}
					obj=s1.getS6Eas().toArray();
					for(Object o:obj){
						this.delete((S6Ea)o);
					}
					List<Object[]> obj1 = s6Bo.getS6EaRecords(ssiId, "all");
					if(obj1==null){
						List<S6> s6List = this.s6Bo.getS6BySsiId(ssiId, null);
						if(s6List!=null&&s6List.size()>0){
							this.delete(s6List.get(0));
						}
					}
				}else{//是否为金属的值没变化
					if(jsonObject.getInt("isMetal")==1){//为金属
						if(jsonObject.getInt("internal")!=s1.getInternal()){//内部发生改变
							needChange++;
							List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),"S6","in");
							deleteTask(arr, taskList,modelSeriesId);
							S4 s4 = this.s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
							if(s4!=null){
								this.delete(s4);
							}
							Sy sy = this.syBo.isExistForSy(ssiId, s1.getS1Id(), "IN");
							if(sy!=null){
								this.delete(sy);
							}
							S5 s5 = this.s5Bo.isExistForS5(ssiId, s1.getS1Id(), "IN");
							if(s5!=null){
								this.delete(s5);
							}
							List<S6Ea> listS6Ea = s6Bo.getS6EaByS1Id("IN", s1.getS1Id());
							if(listS6Ea!=null&&listS6Ea.size()>0){
								for(S6Ea s6Ea : listS6Ea){
									this.delete(s6Ea);
								}
							}
							List<Object[]> obj1 = s6Bo.getS6EaRecords(ssiId, "in");
							if(obj1==null){
								List<S6> s6List = this.s6Bo.getS6BySsiId(ssiId, "IN");
								if(s6List!=null&&s6List.size()>0){
									this.delete(s6List.get(0));
								}
							}
						}
						if(jsonObject.getInt("outernal")!=s1.getOuternal()){//外部发生改变
							needChange++;
							List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),"S6","out");
							deleteTask(arr, taskList,modelSeriesId);
							S4 s4 = this.s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
							if(s4!=null){
								this.delete(s4);
							}
							Sy sy = this.syBo.isExistForSy(ssiId, s1.getS1Id(), "OUT");
							if(sy!=null){
								this.delete(sy);
							}
							S5 s5 = this.s5Bo.isExistForS5(ssiId, s1.getS1Id(), "OUT");
							if(s5!=null){
								this.delete(s5);
							}
							List<S6Ea> listS6Ea = s6Bo.getS6EaByS1Id("OUT", s1.getS1Id());
							if(listS6Ea!=null&&listS6Ea.size()>0){
								for(S6Ea s6Ea : listS6Ea){
									this.delete(s6Ea);
								}
							}
							List<Object[]> obj1 = s6Bo.getS6EaRecords(ssiId, "out");
							if(obj1==null){
								List<S6> s6List = this.s6Bo.getS6BySsiId(ssiId, "OUT");
								if(s6List!=null&&s6List.size()>0){
									this.delete(s6List.get(0));
								}
							}
						}
						if(jsonObject.getInt("designPri")!=s1.getDesignPri()||jsonObject.getInt("isFD")!=s1.getIsFD()){//设计原则是否发生改变或者是否FD分析发生改变
							List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),"S3",null);
							deleteTask(arr, taskList,modelSeriesId);
							obj=s1.getS3s().toArray();
							for(Object o:obj){
								this.delete((S3)o);
							}
						}
					}else{//非金属
						if(jsonObject.getInt("internal")!=s1.getInternal()){//内部发生改变
							needChange++;
							List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),"S6","in");
							deleteTask(arr, taskList,modelSeriesId);
							S4 s4 = this.s4Bo.isExistForS4(ssiId, s1.getS1Id(), "IN");
							if(s4!=null){
								this.delete(s4);
							}
							Sy sy = this.syBo.isExistForSy(ssiId, s1.getS1Id(), "IN");
							if(sy!=null){
								this.delete(sy);
							}
							S5 s5 = this.s5Bo.isExistForS5(ssiId, s1.getS1Id(), "IN");
							if(s5!=null){
								this.delete(s5);
							}
							List<S6Ea> listS6Ea = s6Bo.getS6EaByS1Id("IN", s1.getS1Id());
							if(listS6Ea!=null&&listS6Ea.size()>0){
								for(S6Ea s6Ea : listS6Ea){
									this.delete(s6Ea);
								}
							}
							List<Object[]> obj1 = s6Bo.getS6EaRecords(ssiId, "in");
							if(obj1==null){
								List<S6> s6List = this.s6Bo.getS6BySsiId(ssiId, "IN");
								if(s6List!=null&&s6List.size()>0){
									this.delete(s6List.get(0));
								}
							}
						}
						if(jsonObject.getInt("outernal")!=s1.getOuternal()){//外部发生改变
							needChange++;
							List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(jsonObject.getString("id"),"S6","out");
							deleteTask(arr, taskList,modelSeriesId);
							S4 s4 = this.s4Bo.isExistForS4(ssiId, s1.getS1Id(), "OUT");
							if(s4!=null){
								this.delete(s4);
							}
							Sy sy = this.syBo.isExistForSy(ssiId, s1.getS1Id(), "OUT");
							if(sy!=null){
								this.delete(sy);
							}
							S5 s5 = this.s5Bo.isExistForS5(ssiId, s1.getS1Id(), "OUT");
							if(s5!=null){
								this.delete(s5);
							}
							List<S6Ea> listS6Ea = s6Bo.getS6EaByS1Id("OUT", s1.getS1Id());
							if(listS6Ea!=null&&listS6Ea.size()>0){
								for(S6Ea s6Ea : listS6Ea){
									this.delete(s6Ea);
								}
							}
							List<Object[]> obj1 = s6Bo.getS6EaRecords(ssiId, "out");
							if(obj1==null){
								List<S6> s6List = this.s6Bo.getS6BySsiId(ssiId, "OUT");
								if(s6List!=null&&s6List.size()>0){
									this.delete(s6List.get(0));
								}
							}
						}
					}
					if(jsonObject.getInt("isMetal")==1){
						if(needChange==0){
							needChangeDamage++;
						}
					}
				}
			}else{
				s1= new S1();
				s1.setSMain(sMain);
				needChange++;
			}
			if(jsonObject.get("ssiName")!=null){
				s1.setSsiForm(jsonObject.getString("ssiName").trim());
			}
			if(jsonObject.get("material")!=null){
				s1.setMaterial(jsonObject.getString("material"));
			}
			if(jsonObject.get("repairPassageway")!=null){
				s1.setRepairPassageway(jsonObject.getString("repairPassageway"));
			}
			if(jsonObject.get("surface")!=null){
				s1.setSurface(jsonObject.getString("surface"));
			}
			s1.setOwnArea(comAreaBo.
					getAreaIdByAreaCode(jsonObject.getString("ownArea"),modelSeriesId));
			s1.setIsMetal(jsonObject.getInt("isMetal"));
			s1.setInternal(jsonObject.getInt("internal"));
			s1.setOuternal(jsonObject.getInt("outernal"));
			s1.setDesignPri(jsonObject.getInt("designPri"));
			s1.setIsFD(jsonObject.getInt("isFD"));
			this.saveOrUpdate(s1, status, user.getUserId());
		}
		if(needChange>0||needChangeDamage>0){
			jsonArray = JSONArray.fromObject(jsonData);
			int in = 0;
			int out = 0;
			int nonmetalIn = 0;
			int nonmetalOut = 0;
			int metalOut =0;
			int metalIn =0;
			int metalDamage = 0;
			for(int i =0;i<jsonArray.size();i++){
				jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.getInt("isMetal") == 1){//是金属
					if(jsonObject.getInt("designPri")==2&&jsonObject.getInt("isFD")==1){
						metalDamage++;
					}
					if(jsonObject.getInt("internal") == 1){
						metalIn++;
					}
					if(jsonObject.getInt("outernal") == 1){
						metalOut++;
					}
				}else{
					if(jsonObject.getInt("internal") == 1){
						nonmetalIn++;
					}
					if(jsonObject.getInt("outernal") == 1){
						nonmetalOut++;
					}
				}
				//属于内部
				if(jsonObject.getInt("internal") == 1){
					in++;
				}
				//属于外部
				if(jsonObject.getInt("outernal") == 1){
					out++;
				}
			}
			updateStep(step2, in, out, nonmetalIn, nonmetalOut, metalOut, metalIn, metalDamage, ssiId, user.getUserId()
				    	,oldIn,oldOut,oldNonmetalIn,oldNonmetalOut,oldMetalOut,oldMetalIn,oldMetalDamage,needChangeDamage);
		}
		return arr;
	}
	
	//更新步骤
	private void updateStep(SStep step2,int in,int out,int nonmetalIn,int nonmetalOut,int metalOut,int metalIn,
			int metalDamage,String ssiId,String userId, int oldIn, int oldOut,
			int oldNonmetalIn, int oldNonmetalOut, int oldMetalOut, int oldMetalIn, int oldMetalDamage, int needChangeDamage){
			step2.setS1(1);
			if(metalOut>0){
				if(metalOut>oldMetalOut){
					if (step2.getS4aOut() == 1 || step2.getS5aOut() == 1||step2.getSyaOut() == 1) {
						step2.setS4aOut(2);
						step2.setSyaOut(2);
						step2.setS5aOut(2);
					} else if (step2.getS4aOut() == 3 || step2.getS4aOut() == null) {
						step2.setS4aOut(0);
						step2.setSyaOut(0);
						step2.setS5aOut(0);
					}
				}else{
					if(needChangeDamage==0){
						if(step2.getS4aOut() == 2){
							step2.setS4aOut(1);
						}
						if(step2.getSyaOut() == 2){
							step2.setSyaOut(1);
						}
						if(step2.getS5aOut() == 2){
							step2.setS5aOut(1);
						}
					}
				}
			}else{
				step2.setS4aOut(3);
				step2.setSyaOut(3);
				step2.setS5aOut(3);
			}
			if(metalIn>0){
				if(metalIn>oldMetalIn){
					if (step2.getS4aIn() == 1 || step2.getS5aIn() == 1||step2.getSyaIn() == 1) {
						step2.setS4aIn(2);
						step2.setSyaIn(2);
						step2.setS5aIn(2);
					} else if (step2.getS4aIn() == 3 || step2.getS4aIn() == null) {
						step2.setS4aIn(0);
						step2.setSyaIn(0);
						step2.setS5aIn(0);
					}
				}else{
					if(needChangeDamage==0){
						if(step2.getS4aIn() == 2){
							step2.setS4aIn(1);
						}
						if(step2.getSyaIn() == 2){
							step2.setSyaIn(1);
						}
						if(step2.getS5aIn() == 2){
							step2.setS5aIn(1);
						}
					}
				}
			}else{
				step2.setS4aIn(3);
				step2.setSyaIn(3);
				step2.setS5aIn(3);
			}
			if(nonmetalOut>0){
				if(nonmetalOut>oldNonmetalOut){
					if (step2.getS4bOut() == 1 || step2.getS5bOut() == 1||step2.getSybOut() == 1) {
						step2.setS4bOut(2);
						step2.setSybOut(2);
						step2.setS5bOut(2);
					} else if (step2.getS4bOut() == 3 || step2.getS4bOut() == null) {
						step2.setS4bOut(0);
						step2.setSybOut(0);
						step2.setS5bOut(0);
					}
				}else{
					if(needChangeDamage==0){
						if(step2.getS4bOut() == 2){
							step2.setS4bOut(1);
						}
						if(step2.getSybOut() == 2){
							step2.setSybOut(1);
						}
						if(step2.getS5bOut() == 2){
							step2.setS5bOut(1);
						}
					}
				}
			}else{
				step2.setS4bOut(3);
				step2.setSybOut(3);
				step2.setS5bOut(3);
			}
			if(nonmetalIn>0){
				if(nonmetalIn>oldNonmetalIn){
					if (step2.getS4bIn() == 1 || step2.getS5bIn() == 1||step2.getSybIn() == 1) {
						step2.setS4bIn(2);
						step2.setSybIn(2);
						step2.setS5bIn(2);
					} else if (step2.getS4bIn() == 3 || step2.getS4bIn() == null) {
						step2.setS4bIn(0);
						step2.setSybIn(0);
						step2.setS5bIn(0);
					}
				}else{
					if(needChangeDamage==0){
						if(step2.getS4bIn() == 2){
							step2.setS4bIn(1);
						}
						if(step2.getSybIn() == 2){
							step2.setSybIn(1);
						}
						if(step2.getS5bIn() == 2){
							step2.setS5bIn(1);
						}
					}
				}
			}else{
				step2.setS4bIn(3);
				step2.setSybIn(3);
				step2.setS5bIn(3);
			}
			if(metalDamage>0){
				if(metalDamage>oldMetalDamage){
					if (step2.getS3() == 1) {
						step2.setS3(2);
					}else if(step2.getS3() == 3||step2.getS3() == null){
						step2.setS3(0);
					}
				}else{
					if(step2.getS3() == 2){
						step2.setS3(1);
					}
				}
			}else{
				step2.setS3(3);
			}
			if(in>0){
				if(metalIn>oldMetalIn||nonmetalIn>oldNonmetalIn){
					if (step2.getS6In() == 1) {
						step2.setS6In(2);
					} else if(step2.getS6In() == 3||step2.getS6In() == null){
						step2.setS6In(0);
					}
				}else{
					List<S1> list = s6Bo.getS1IdBySssiId(ssiId, ComacConstants.INNER);
					List<Object[]> s6eaList = s6Bo.getS6EaRecords(ssiId, "in");
					if(s6eaList!=null&&list!=null&&s6eaList.size()==list.size()){
						if (step2.getS6In() == 2&&step2.getS4aIn()==1&&step2.getS4bIn()==1
								&&step2.getSyaIn()==1&&step2.getSybIn()==1
								&&step2.getS5aIn()==1&&step2.getS5bIn()==1) {
							step2.setS6In(1);
						}
					}
				}
			}else{
				step2.setS6In(3);
			}
			if(out>0){
				if(metalOut>oldMetalOut||nonmetalOut>oldNonmetalOut){
					if (step2.getS6Out() == 1) {
						step2.setS6Out(2);
					} else if(step2.getS6Out() == 3||step2.getS6Out() ==null){
						step2.setS6Out(0);
					}
				}else{
					List<S1> list = s6Bo.getS1IdBySssiId(ssiId, ComacConstants.OUTTER);
					List<Object[]> s6eaList = s6Bo.getS6EaRecords(ssiId, "out");
					if(s6eaList!=null&&list!=null&&s6eaList.size()==list.size()){
						if (step2.getS6Out() == 2&&step2.getS4aOut()==1&&step2.getS4bOut()==1
								&&step2.getSyaOut()==1&&step2.getSybOut()==1
								&&step2.getS5aOut()==1&&step2.getS5bOut()==1) {
							step2.setS6Out(1);
						}
					}
				}
			}else{
				step2.setS6Out(3);
			}
			if(step2.getS2()==1){
				if(step2.getS3()==0){
					step2.setS3(2);
				}
				if(step2.getS3()!=2){
					if(step2.getS4aIn()==0){
						step2.setS4aIn(2);
						step2.setSyaIn(0);
						step2.setS5aIn(0);
					}else if(step2.getS4bIn()==0){
						step2.setS4bIn(2);
						step2.setSybIn(0);
						step2.setS5bIn(0);
					}else if(step2.getS4aOut()==0){
						step2.setS4aOut(2);
						step2.setSyaOut(0);
						step2.setS5aOut(0);
					}else if(step2.getS4bOut()==0){
						step2.setS4bOut(2);
						step2.setSybOut(0);
						step2.setS5bOut(0);
					}
				}
			}
		this.saveOrUpdate(step2, ComacConstants.DB_UPDATE, userId);
	}

	@Override
	public SMain getSMainByAtaId(String ataId, String modelSeriesId) {
		List<SMain>  list = this.s1Dao.getSMainByAtaId(ataId, modelSeriesId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> deleteS1Record(String s1Id,String modelSeriesId, String ssiId){
		ArrayList<String> arr = new ArrayList<String>();
		S1 s1=(S1)this.loadById(S1.class, s1Id);
		List<TaskMsg> taskList = taskMsgBo.getTempTaskMsgByS1Id(s1Id,null,null);
		TaskMsg task;
		if(taskList.size()>0){
			for(TaskMsg tsk : taskList){
				task = (TaskMsg) taskMsgBo.loadById(TaskMsg.class, tsk.getTaskId());
				if(s6Dao.searchTempTaskCode(tsk.getAnyContent2(),modelSeriesId,null).size()==1){
					TaskMsg task1 = taskMsgBo.getTaskByTaskCode(modelSeriesId, tsk.getAnyContent2());
					taskMsgBo.deleteTaskMsgById(task1.getTaskId());//删除最终任务
					if(task1.getNeedTransfer()!=null&&task1.getNeedTransfer()==1){
						arr.add(task1.getOwnArea());
					}
				}
				taskMsgBo.delete(task);//删除临时任务
			}
		}
		this.delete(s1);
		List<S1> listIn = s6Bo.getS1IdBySssiId(ssiId,ComacConstants.INNER);
		if(listIn==null||listIn.size()==0){
			S6 s6 = s6Bo.getS6Records(ssiId, ComacConstants.INNER);
			if(s6!=null){
				this.delete(s6);
			}
		}
		List<S1> listOut = s6Bo.getS1IdBySssiId(ssiId,ComacConstants.OUTTER);
		if(listOut==null||listOut.size()==0){
			S6 s6 = s6Bo.getS6Records(ssiId, ComacConstants.OUTTER);
			if(s6!=null){
				this.delete(s6);
			}
		}
		return arr;
	}
	
	
	@Override
	public void deleteAnalysisData(String ssiId,String modelSeriesId,String userId) {
		List<SStep> step1=this.getSstepBySssiId(ssiId);
		SStep step2 = step1.get(0);
		step2.getSMain().setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
		SRemark sr = this.getRemarkBySssi(ssiId);
		sr.setS1Remark(null);
		sr.setS4aInRemark(null);
		sr.setS4aOutRemark(null);
		sr.setS4bInRemark(null);
		sr.setS4bOutRemark(null);
		sr.setS5aInRemark(null);
		sr.setS5aOutRemark(null);
		sr.setS5bInRemark(null);
		sr.setS5bOutRemark(null);
		sr.setS7Remark(null);
		sr.setSyaInRemark(null);
		sr.setSyaOutRemark(null);
		sr.setSybInRemark(null);
		sr.setSybOutRemark(null);
		this.update(sr);
		List<S2> listS2 = this.s2Bo.getS2BySssId(ssiId);
		if(listS2!=null&&listS2.size()>0){
			S2 s2 = listS2.get(0);
			if(s2!=null){
				this.delete(s2);
			}
		}
		
		//删除S6产生的协调单数据
		List<ComCoordination> listCoo = this.comCoordinationBo.
				findCoordinationById(ssiId, null,modelSeriesId, 1);
		if(listCoo!=null&&listCoo.size()>0){
			for(ComCoordination coo : listCoo){
				this.comCoordinationBo.delete(coo);
			}
		}
		saveOrUpdate(ssiStepBo.changeStep(step2,ssiId), ComacConstants.DB_UPDATE, userId);
	}
	
	public IS1Dao getS1Dao() {
		return s1Dao;
	}

	public void setS1Dao(IS1Dao s1Dao) {
		this.s1Dao = s1Dao;
	}

	public ITaskMsgBo getTaskMsgBo() {
		return taskMsgBo;
	}

	public void setTaskMsgBo(ITaskMsgBo taskMsgBo) {
		this.taskMsgBo = taskMsgBo;
	}

	public IS6Dao getS6Dao() {
		return s6Dao;
	}

	public void setS6Dao(IS6Dao s6Dao) {
		this.s6Dao = s6Dao;
	}

	public IComAreaBo getComAreaBo() {
		return comAreaBo;
	}

	public void setComAreaBo(IComAreaBo comAreaBo) {
		this.comAreaBo = comAreaBo;
	}

	public ISsiStepBo getSsiStepBo() {
		return ssiStepBo;
	}

	public void setSsiStepBo(ISsiStepBo ssiStepBo) {
		this.ssiStepBo = ssiStepBo;
	}

	public IComCoordinationBo getComCoordinationBo() {
		return comCoordinationBo;
	}

	public void setComCoordinationBo(IComCoordinationBo comCoordinationBo) {
		this.comCoordinationBo = comCoordinationBo;
	}

	public IS2Bo getS2Bo() {
		return s2Bo;
	}

	public void setS2Bo(IS2Bo s2Bo) {
		this.s2Bo = s2Bo;
	}

	public IS4Bo getS4Bo() {
		return s4Bo;
	}

	public void setS4Bo(IS4Bo s4Bo) {
		this.s4Bo = s4Bo;
	}

	public IS5Bo getS5Bo() {
		return s5Bo;
	}

	public void setS5Bo(IS5Bo s5Bo) {
		this.s5Bo = s5Bo;
	}

	public IS6Bo getS6Bo() {
		return s6Bo;
	}

	public void setS6Bo(IS6Bo s6Bo) {
		this.s6Bo = s6Bo;
	}

	public ISyBo getSyBo() {
		return syBo;
	}

	public void setSyBo(ISyBo syBo) {
		this.syBo = syBo;
	}
	
}
