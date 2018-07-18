package com.rskytech.lhirf.bo;

import java.util.ArrayList;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.LhMain;

public interface ILhSelectBo extends IBo {
	/**
	 * 删除HSI时 同时删除该hsi的相关数据
	 * @param  表lh_HSI 
	 * @return 
	 * @throws BusinessException
	 */
	public ArrayList<String> deleteHsi(LhMain lhMain, ComUser user,String modelSeriesId) throws BusinessException;
	/**
	 * 页面认领HSI, 需替换的MSG-3的任务表 创建人 MRB 中的创建人
	 * @param comModelSeries 
	 *  @param  表lh_HSI Id
	 *  @param  表ComUser user 
	 * @return 
	 */
	public void doReplaceHsi(String replaceHsiId,ComUser user, ComModelSeries comModelSeries)throws BusinessException;
	
	/**
	 * 保存hsi的数据
	 * @param sysUser
	 * @param areaId
	 * @param jsonData
	 * @param comModelSeries 
	 * @return
	 */
	public boolean saveOrUpdateLhHsi(ComUser sysUser, String areaId,String jsonData, ComModelSeries comModelSeries);
	/**
	 * 检查hsi编号是否存在
	 * @param hsiCode
	 * @param modelSeriesId 
	 */
	public boolean verifyHsiCodeExist(String hsiCode, String modelSeriesId);

}
