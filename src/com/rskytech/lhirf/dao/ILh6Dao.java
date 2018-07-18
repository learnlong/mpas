package com.rskytech.lhirf.dao;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
public interface ILh6Dao extends IDAO {
	
	
	
	/**
	 * 通过区域机型ID号 区域id 得到其下任务信息
	 * @param modelSeriesId 
	 * @param AreaId 区域ID
	 * @return objectlist[]
	 */
	public Page getLhirfTaskMsgList(String modelSeriesId,String AreaId,Page page) throws BusinessException ;
	
	/**
	 * 通过区域机型ID号 区域id 得到其任务信息(不分页)
	 * @param modelSeriesId 
	 * @param AreaId 区域ID
	 * @return objectlist[]
	 */
	public List<Object[]> getLhirfListByIdNoPage(String modelSeriesId,String AreaId) throws BusinessException ;

}
