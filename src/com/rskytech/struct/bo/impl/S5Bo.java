package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S5;
import com.rskytech.struct.bo.IS5Bo;
import com.rskytech.struct.dao.IS5Dao;

@SuppressWarnings("unchecked")
public class S5Bo extends BaseBO implements IS5Bo {
	private IS5Dao s5Dao;

	@Override
	public int getItemCount(String itemName, String modelId,String aOrb)
			throws BusinessException {
		int count = 0;
		List<Object[]> list = s5Dao.getItemCount(itemName, modelId,aOrb);
		if (list != null) {
			for (Object[] obj : list) {
				List ls = s5Dao.getLevelCount(obj[1].toString(), modelId,aOrb);
				if (ls != null) {
					count += ls.size();
				}
			}
		}
		return count;
	}

	public IS5Dao getS5Dao() {
		return s5Dao;
	}

	public void setS5Dao(IS5Dao dao) {
		s5Dao = dao;
	}

	@Override
	public List getItemName(String itemName, String modelId,String aOrb)
			throws BusinessException {

		return s5Dao.getItemCount(itemName, modelId,aOrb);
	}

	@Override
	public List<S1> getS1(String sssiId, int isMetal, int inOrOut)
			throws BusinessException {
		return this.s5Dao.getS1(sssiId, isMetal, inOrOut);
	}

	@Override
	public List getLevelCount(String itemId, String modelId,String aOrb)
			throws BusinessException {
		return s5Dao.getLevelCount(itemId, modelId,aOrb);
	}

	@Override
	public List<S5> getS5BySsiId(String ssiId, Integer inOrOut)
			throws BusinessException {
		return this.s5Dao.getS5BySsiId(ssiId, inOrOut);
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
	public S5 isExistForS5(String ssiId, String id, String inOrOut)
			throws BusinessException {
		List<S5> list=this.s5Dao.isExistForS5(ssiId, id, inOrOut);
		if(list!=null&&!list.isEmpty()){
			return	list.get(0);
		}
		return null;
	}

}
