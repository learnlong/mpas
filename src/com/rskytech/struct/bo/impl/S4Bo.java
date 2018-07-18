package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.S4;
import com.rskytech.struct.bo.IS4Bo;
import com.rskytech.struct.dao.IS4Dao;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class S4Bo extends BaseBO implements IS4Bo {
	private IS4Dao s4Dao;

	@Override
	public int getItemCount(String itemName, String modelId,String aOrb)
			throws BusinessException {
		int count = 0;
		List<Object[]> list = s4Dao.getItemCount(itemName, modelId,aOrb);
		if (list != null) {
			for (Object[] obj : list) {
				List ls = s4Dao.getLevelCount(obj[1].toString(), modelId,aOrb);
				if (ls != null) {
					count += ls.size();
				}
			}
		}
		return count;
	}

	public IS4Dao getS4Dao() {
		return s4Dao;
	}

	public void setS4Dao(IS4Dao dao) {
		s4Dao = dao;
	}

	@Override
	public List getItemName(String itemName, String modelId,String aOrb)
			throws BusinessException {

		return s4Dao.getItemCount(itemName, modelId,aOrb);
	}

	@Override
	public List getS1(String sssiId, int isMetal, int inOrOut)
			throws BusinessException {
		return this.s4Dao.getS1(sssiId, isMetal, inOrOut);
	}

	@Override
	public List getLevelCount(String itemId, String modelId,String aOrb)
			throws BusinessException {
		return s4Dao.getLevelCount(itemId, modelId,aOrb);
	}

	@Override
	public List getS4BySsiId(String ssiId, Integer inOrOut)
			throws BusinessException {
		return this.s4Dao.getS4BySsiId(ssiId, inOrOut);
	}

	@Override
	public boolean saveList(List<Object[]> list,ComUser user,String flag) throws BusinessException {
		if (list != null && !list.isEmpty()) {
			for (Object[] obj : list) {
				this.saveOrUpdate(obj[0], (String) obj[1], (String) obj[2]);
			}
			this.saveComLogOperate(user, flag, ComacConstants.STRUCTURE_CODE);
			return true;
		}
		return false;
	}

	@Override
	public S4 isExistForS4(String ssiId, String id, String inOrOut)
			throws BusinessException {
		List<S4> list=this.s4Dao.getS4ListBySsiIdAndS1Id(ssiId, id, inOrOut);
		if(list!=null&&!list.isEmpty()){
			return	list.get(0);
		}
		return null;
	}

	@Override
	public Integer getCusStep(String step,String modelSeriesId) throws BusinessException {
		List<CusEdrAdr> list=this.s4Dao.getCusStepList(step, modelSeriesId);
		if(list!=null&&!list.isEmpty()){
			return list.get(0).getOperateFlg();
		}
		return null;
	}


}
