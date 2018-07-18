package com.rskytech.struct.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.Sy;
import com.rskytech.struct.bo.ISyBo;
import com.rskytech.struct.dao.IS4Dao;
import com.rskytech.struct.dao.ISyDao;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SyBo extends BaseBO implements ISyBo {
	private ISyDao syDao;

	@Override
	public int getItemCount(String itemName, String modelId,String aOrb) 
			throws BusinessException {
		int count = 0;
		List<Object[]> list = syDao.getItemCount(itemName, modelId,aOrb);
		if (list != null) {
			for (Object[] obj : list) {
				List ls = syDao.getLevelCount(obj[1].toString(), modelId,aOrb);
				if (ls != null) {
					count += ls.size();
				}
			}
		}
		return count;
	}



	public ISyDao getSyDao() {
		return syDao;
	}



	public void setSyDao(ISyDao syDao) {
		this.syDao = syDao;
	}



	@Override
	public List getItemName(String itemName, String modelId,String aOrb)
			throws BusinessException {

		return syDao.getItemCount(itemName, modelId,aOrb);
	}

	@Override
	public List getS1(String sssiId, int isMetal, int inOrOut)
			throws BusinessException {
		return this.syDao.getS1(sssiId, isMetal, inOrOut);
	}

	@Override
	public List getLevelCount(String itemId, String modelId,String aOrb)
			throws BusinessException {
		return syDao.getLevelCount(itemId, modelId,aOrb);
	}

	@Override
	public List getSyBySsiId(String ssiId, Integer inOrOut)
			throws BusinessException {
		return this.syDao.getSyBySsiId(ssiId, inOrOut);
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
	public Sy isExistForSy(String ssiId, String id, String inOrOut)
			throws BusinessException {
		List<Sy> list=this.syDao.getSyListBySsiIdAndS1Id(ssiId, id, inOrOut);
		if(list!=null&&!list.isEmpty()){
			return	list.get(0);
		}
		return null;
	}

	@Override
	public Integer getCusStep(String step,String modelSeriesId) throws BusinessException {
		List<CusEdrAdr> list=this.syDao.getCusStepList(step, modelSeriesId);
		if(list!=null&&!list.isEmpty()){
			return list.get(0).getOperateFlg();
		}
		return null;
	}


}
