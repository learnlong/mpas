package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS2Bo;
import com.rskytech.struct.bo.IS3Bo;
import com.rskytech.struct.bo.IS8Bo;
import com.rskytech.struct.bo.ISsiStepBo;
import com.rskytech.struct.dao.IS7Dao;

public class SsiStepBo extends BaseBO implements ISsiStepBo {

	private IS3Bo s3Bo;
	private IS8Bo s8Bo;
	private IS7Dao s7Dao;
	private IS2Bo s2Bo;
	private IS1Bo s1Bo;
	
	@Override
	public int[] initStep(String ssiId,SStep ssiStep,String stepPage){
		int[] initStep =null;
		if("S1".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,1);
		}
		if("S2".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,2);
		}
		if("S3".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,3);
		}
		if("S4AIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,4);
		}
		if("S4AOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,9);
		}
		if("S4BIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,6);
		}
		if("S4BOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,11);
		}
		if("S5AIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,5);
		}
		if("S5AOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,10);
		}
		if("S5BIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,7);
		}
		if("S5BOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,12);
		}
		if("S6IN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,8);
		}
		if("S6OUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,13);
		}
		if("S7".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,14);
		}
		if("S8".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,15);
		}
		if("SYAIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,16);
		}
		if("SYAOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,18);
		}
		if("SYBIN".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,17);
		}
		if("SYBOUT".equals(stepPage)){
			initStep=initStepBySsiId(ssiId, ssiStep,19);
		}
		return initStep;
	}
	
	private int[] initStepBySsiId(String ssiId,SStep ssiStep, int i){
		int stepS3 = 3;
		int stepS8 = 3;
		int stepS7 = 3;
		/*int stepS6In = 3;
		int stepS6Out = 3;*/
		if(s3Bo.getS3Records(ssiId)!=null&&s3Bo.getS3Records(ssiId).size()>0){
			stepS3 = ssiStep.getS3();
		}
		if(ssiStep.getS2()!=1&&stepS3!=3){
			stepS3 = 0;
		}
		if(s7Dao.getS7Records(ssiId).size()>0){
			stepS7 = ssiStep.getS7();
		}
		if(s8Bo.getS8Records(ssiId, "S8")!=null){
			stepS8 = ssiStep.getS8();
		}
		ssiStep.setS3(stepS3);
		ssiStep.setS7(stepS7);
		ssiStep.setS8(stepS8);
		this.update(ssiStep);
		/*if(ssiStep.getS5bIn()==1||ssiStep.getS5aIn()==1){
			stepS6In = ssiStep.getS6In();
		}
		if(ssiStep.getS5bOut()==1||ssiStep.getS5aOut()==1){
			stepS6Out = ssiStep.getS6Out();
		}*/
		int[] s1Step = {i, 
				ssiStep.getS1(), 
				ssiStep.getS2(),
				stepS3,
				ssiStep.getS4aIn(),
				ssiStep.getS5aIn(),
				ssiStep.getS4bIn(),
				ssiStep.getS5bIn(),
				ssiStep.getS6In(),
				ssiStep.getS4aOut(), 
				ssiStep.getS5aOut(),
				ssiStep.getS4bOut(),
				ssiStep.getS5bOut(),
				ssiStep.getS6Out(),
				stepS7,
				stepS8,
				ssiStep.getSyaIn(),
				ssiStep.getSybIn(),
				ssiStep.getSyaOut(),
				ssiStep.getSybOut()
				};
		return s1Step;
	}

	@Override
	public SStep changeStep(SStep sStep,String ssiId) {
		sStep.setS1(2);
		if(this.s2Bo.getS2BySssId(ssiId)!=null&&this.s2Bo.getS2BySssId(ssiId).size()>0){
			sStep.setS2(1);
		}else{
			sStep.setS2(2);
		}
		if(s3Bo.getS3Records(ssiId)!=null&&s3Bo.getS3Records(ssiId).size()>0){
			sStep.setS3(2);
		}else{
			sStep.setS3(3);
		}
		sStep.setS4aIn(3);
		sStep.setS4aOut(3);
		sStep.setS4bIn(3);
		sStep.setS4bOut(3);
		sStep.setS5aIn(3);
		sStep.setS5aOut(3);
		sStep.setS5bIn(3);
		sStep.setS5bOut(3);
		sStep.setS6In(3);
		sStep.setS6Out(3);
		sStep.setS7(3);
		sStep.setS8(3);
		sStep.setSyaIn(3);
		sStep.setSybIn(3);
		sStep.setSyaOut(3);
		sStep.setSybOut(3);
		return sStep;
	}
	
	@Override
	public void changeStatus(String ssiId){
		SMain s=(SMain)this.loadById(SMain.class, ssiId);
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		SStep step =step1.get(0);
		int analysisCount =0;
		if(step.getS1()==0||step.getS1()==2||step.getS2()==0||step.getS2()==2||step.getS3()==0||step.getS3()==2
				||step.getS4aIn()==0||step.getS4aIn()==2||step.getS4bIn()==0||step.getS4bIn()==2
				||step.getS4aOut()==0||step.getS4aOut()==2||step.getS4bOut()==0||step.getS4bOut()==2
				||step.getS5aIn()==0||step.getS5aIn()==2||step.getS5bIn()==0||step.getS5bIn()==2
				||step.getS5aOut()==0||step.getS5aOut()==2||step.getS5bOut()==0||step.getS5bOut()==2
				||step.getS6In()==0||step.getS6In()==2||step.getS6Out()==0||step.getS6Out()==2
				||step.getS7()==0||step.getS7()==2
				||step.getSyaIn()==0||step.getSyaIn()==2||step.getSybIn()==0||step.getSybIn()==2
				||step.getSyaOut()==0||step.getSyaOut()==2||step.getSybOut()==0||step.getSybOut()==2
				){
			analysisCount++;
		}
		if(analysisCount>0){
			if(s.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)){
				s.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAIN);
				this.update(s);
			}
		}else{
			if(!s.getStatus().equals(ComacConstants.ANALYZE_STATUS_MAINTAINOK)){
				s.setStatus(ComacConstants.ANALYZE_STATUS_MAINTAINOK);
				this.update(s);
			}
		}
	}

	public IS3Bo getS3Bo() {
		return s3Bo;
	}

	public void setS3Bo(IS3Bo s3Bo) {
		this.s3Bo = s3Bo;
	}

	public IS8Bo getS8Bo() {
		return s8Bo;
	}

	public void setS8Bo(IS8Bo s8Bo) {
		this.s8Bo = s8Bo;
	}

	public IS7Dao getS7Dao() {
		return s7Dao;
	}

	public void setS7Dao(IS7Dao s7Dao) {
		this.s7Dao = s7Dao;
	}

	public IS2Bo getS2Bo() {
		return s2Bo;
	}

	public void setS2Bo(IS2Bo s2Bo) {
		this.s2Bo = s2Bo;
	}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo s1Bo) {
		this.s1Bo = s1Bo;
	}
	
	
}
