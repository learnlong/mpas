package com.rskytech.lhirf.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.LhMain;

public interface ILhMainDao extends IDAO {
	
	/**
	 * 根据区域ID，得到LhMain对象
	 * @param AreaId
	 * @return lhMain
	 * @throws BusinessException
	 */
	public List<LhMain> getLhMainListByAreaId(String areaId ,String modelId,String hsiCode,String hsiName) throws BusinessException;
	/**
	 * 根据参见的hsi编号查询hsi
	 * @param refHsiCode
	 * @return
	 * @throws BusinessException
	 */
	public List<LhMain> getLhMainByRefHsiCode(String refHsiCode,String comModelSeriesId)
			throws BusinessException ;
	/**
	 * 根据hsi编号查询hsi
	 * @param hsiCode
	 * @param comModelSeriesId 
	 * @return
	 * @throws BusinessException
	 */
	public List<LhMain> getLhMainByHsiCode(String hsiCode, String comModelSeriesId)
			throws BusinessException ;
	
	/**根据机型获取区域 节点
	 * @param modelSeriesId
	 * @param parentNodeId
	 * @param areaLevel
	 * @return
	 * @throws BusinessException
	 */
	public List<ComArea> getAreaNodeList(String modelSeriesId,String parentNodeId,Integer areaLevel)
			throws BusinessException;
	
	/**根据机型   区域ID获取LH_ HSI详细信息
	 * @param modelSeriesId
	 * @param parentNodeOneId
	 * @param parentNodeTwoId
	 * @param parentNodeThreeId
	 * @param page
	 * @return
	 * @throws BusinessException
	 */
	public List getLhHsiListByAreaId(String modelSeriesId,
			String parentNodeOneId, String parentNodeTwoId,
			String parentNodeThreeId,Page page) throws BusinessException;
	
	/**根据机型   区域ID获取 task_id 详细信息
	 * @param modelSeriesId
	 * @param firstTextField
	 * @param page
	 * @return
	 * @throws BusinessException
	 */
	public List getLhrifTaskId(String modelSeriesId,String firstTextField,Page page)throws BusinessException ;
	/**
	 * 根据机型获取LhMain
	 * @param modelSeriesId
	 * @return
	 */
	public List<LhMain> getLhHsiByModelSeriesId(String modelSeriesId)throws BusinessException;
	
}
