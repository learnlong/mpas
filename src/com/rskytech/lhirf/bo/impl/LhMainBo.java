package com.rskytech.lhirf.bo.impl;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.lhirf.bo.ILhMainBo;
import com.rskytech.lhirf.dao.ILhMainDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.LhMain;

public class LhMainBo extends BaseBO implements ILhMainBo {
	private ILhMainDao lhMainDao;
	
	/**
	 * 根据区域ID，得到LhMain对象
	 * @param AreaId
	 * @return lhMain
	 * @throws BusinessException
	 */
	@Override
	public List<LhMain> getLhMainList(String areaId ,String modelId,String hsiCode,String hsiName) throws BusinessException {
		return this.lhMainDao.getLhMainListByAreaId(areaId, modelId,hsiCode,hsiName);
	}
	
	
	@Override
	public List<LhMain> getLhMainByRefHsiCode(String refHsiCode,String comModelSeriesId)
			throws BusinessException {
		return this.lhMainDao.getLhMainByRefHsiCode(refHsiCode,comModelSeriesId);
	}
	
	@Override
	public LhMain getLhMainByHsiCode(String hsiCode, String comModelSeriesId)
			throws BusinessException {
		List<LhMain> list = this.lhMainDao.getLhMainByHsiCode(hsiCode,comModelSeriesId);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**根据机型   区域ID获取LH_ HSI详细信息
	 * @throws BusinessException
	 */
	@Override
	public List getLhHsiListByAreaId(String modelSeriesId,
			String parentNodeOneId, String parentNodeTwoId,
			String parentNodeThreeId,Page page) throws BusinessException {
		return this.lhMainDao.getLhHsiListByAreaId(modelSeriesId, parentNodeOneId, parentNodeTwoId, parentNodeThreeId, page);
	}

	@Override
	public List<ComArea> getAreaNodeList(String modelSeriesId,
			String parentNodeId, Integer areaLevel) throws BusinessException {
		
		return this.lhMainDao.getAreaNodeList(modelSeriesId, parentNodeId, areaLevel);
	}
	
	@Override
	public List getLhrifTaskId(String modelSeriesId, String firstTextField, Page page)
			throws BusinessException {
		return this.lhMainDao.getLhrifTaskId(modelSeriesId,firstTextField, page);
	}
	
	@Override
	public List<LhMain> getLhHsiByModelSeriesId(String modelSeriesId)
			throws BusinessException {
		return this.lhMainDao.getLhHsiByModelSeriesId(modelSeriesId);
	}

	public ILhMainDao getLhMainDao() {
		return lhMainDao;
	}

	public void setLhMainDao(ILhMainDao lhMainDao) {
		this.lhMainDao = lhMainDao;
	}


}
