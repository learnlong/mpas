package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S2;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;
import com.rskytech.struct.bo.IS1Bo;
import com.rskytech.struct.bo.IS2Bo;
import com.rskytech.struct.dao.IS2Dao;

public class S2Bo extends BaseBO implements IS2Bo {
	private IS1Bo s1Bo;
	private IS2Dao s2Dao;

	@Override
	public List<S2> getS2BySssId(String sssId) throws BusinessException {
		return this.s2Dao.getS2BySssId(sssId);
	}

	@Override
	public String saveS2Record(String ssiId,String id,ComUser user,String content) throws BusinessException {	
		String stat=ComacConstants.DB_INSERT;
		List<SStep> step1=s1Bo.getSstepBySssiId(ssiId);
		S2 s2 = new S2();
		if(id!=null&&!"".equals(id)){
			s2.setPicId(id);
			stat=ComacConstants.DB_UPDATE;
			s2.setModifyDate(BasicTypeUtils.getCurrentDateforSQL());
			s2.setModifyUser(user.getUserId());
		}
		if(content!=null){
		s2.setPicContent(content);}
		s2.setSMain((SMain) this.loadById(SMain.class,ssiId));
		this.saveOrUpdate(s2, stat, user.getUserId());
		step1.get(0).setS2(1);
		String index =null;
		if(step1.get(0).getS3()==0){
			step1.get(0).setS3(2);
			index ="3";
		}
		if(step1.get(0).getS3()==3){
			if(step1.get(0).getS4aIn()!=3){
				if(step1.get(0).getS4aIn()==0){
					step1.get(0).setS4aIn(2);
					//step1.get(0).setS5aIn(2);
				}
				index = "4";
			}else if(step1.get(0).getS4aOut()!=3){
				if(step1.get(0).getS4aOut()==0){
					step1.get(0).setS4aOut(2);
					//step1.get(0).setS5aOut(2);
				}
				index = "10";
			}else if(step1.get(0).getS4bIn()!=3){
				if(step1.get(0).getS4bIn()==0){
					step1.get(0).setS4bIn(2);
					//step1.get(0).setS5bIn(2);
				}
				index = "6";
			}else if(step1.get(0).getS4bOut()!=3){
				if(step1.get(0).getS4bOut()==0){
					step1.get(0).setS4bOut(2);
					//step1.get(0).setS5bOut(2);
				}
				index = "11";
			}
		}
		this.saveOrUpdate(step1.get(0), ComacConstants.DB_UPDATE,user.getUserId());
		this.saveComLogOperate(user, "S2", ComacConstants.STRUCTURE_CODE);
		return index;
		}

	public IS1Bo getS1Bo() {
		return s1Bo;
	}

	public void setS1Bo(IS1Bo bo) {
		s1Bo = bo;
	}

	public IS2Dao getS2Dao() {
		return s2Dao;
	}

	public void setS2Dao(IS2Dao s2Dao) {
		this.s2Dao = s2Dao;
	}


}
