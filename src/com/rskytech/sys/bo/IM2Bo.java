package com.rskytech.sys.bo;


import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M2;

public interface IM2Bo extends IBo {
	/**
	 * 通过故障影响的ID，查询唯一的一条M2记录
	 * @param msiId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-23
	 */
	public M2 getM2ByM13fId(String m13fId)throws BusinessException ;
	/**
	 * 根据M2Id删除Afm
	 * @param m2Id
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-27
	 */
	public void deleteAfm(String m2Id,String userId) throws BusinessException;
	/**
	 * 根据M2Id删除参考mmel
	 * @param m2Id
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-27
	 */
	public void deleteMmel(String m2Id,String userId) throws BusinessException;
	/**
	 * 根据MsiId查询M2
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-27
	 */
	public List<M2> getM2ListByMsiId(String msiId)throws BusinessException;
	public ArrayList<String> saveM2(ComUser sysUser, String pageId, String sourceSystem,
			String jsonData,String msiId,String m13fId,Integer isSaveAfm,
			String afmJsonData,Integer isSaveMmel,String mmelJsonData,ComModelSeries comModelSeries);
	
	/**
	 * 更新步骤
	 * @param user
	 * @param msiId
	 * @param comModelSeries
	 */
	public void updataMStep(ComUser user, String msiId,ComModelSeries comModelSeries);

}
