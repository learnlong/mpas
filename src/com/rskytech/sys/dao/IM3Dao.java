package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;
public interface IM3Dao  extends IDAO{
	/**
	 * 获取供应商
	 */
	public List getVendorCountByMsi(String msiId)throws BusinessException;
	/**
	 * 根据m3Id和任务ID查询唯一一条附加任务记录
	 * 
	 * @param m13cId
	 * @return
	 * @throws BusinessException
	 */
	public List<M3Additional> getM3AdditionalByM3Id(String id, String taskId)
			throws BusinessException;
	/**
	 * 根据msiId查询M3
	 * 
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 */
	public List<M3> getM3ListByMsiId(String msiId) throws BusinessException;
	/**
	 * 根据故障原因Id查询唯一一条M3
	 * @param m13cId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public List<M3> getM3ByM13cId(String m13cId)throws BusinessException ;
}
