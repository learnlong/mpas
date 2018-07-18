package com.rskytech.lhirf.bo;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh1;

public interface ILh1Bo extends IBo {
	
	/*
	 * LH1  表操作
	 */

	/**
	 * 通过Lhsid 查询lh1 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-22
	 */
	public Lh1 getLh1ByHsiId(String hsiId)throws BusinessException ;
	/**
	 * 通过Lhsid 保存 lh1 表中的数据
	 * @param ComUser user
	 * @param String hsiId
	 * @param String picContent  页面  图文 中英文 
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 */
	public void saveLh1andStep(ComUser user,String hsiId,String picContent, String lheff)throws BusinessException ;
	/**
	 * 通过Lhsid 查询lhstep表中的数据
	 * @param HsiId 表lh_HSI 
	 * @param comuser  user
	 * @return 
	 * @throws BusinessException
	 * @author wangyueli
	 * @param comModelSeries 
	 * @createdate 2012-8-25
	 */
	public void lh1Stepshow(String hsiId,ComUser user, ComModelSeries comModelSeries)throws BusinessException ;
	
}
