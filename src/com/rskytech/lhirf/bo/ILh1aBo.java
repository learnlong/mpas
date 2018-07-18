package com.rskytech.lhirf.bo;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh1a;

public interface ILh1aBo extends IBo {
	
	
	/**
	 * 通过Lhsid 查询lh1a 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1a对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-25
	 */
	public Lh1a getLh1aByHsiId(String hsiId)throws BusinessException ;
	/**
	 * 通过Lhsid 查询lh1a 表中的数据
	 * @param HsiId 表lh_HSI 
	 * @param comuser  user
	 * @return Lhirf 中 lh1a对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-25
	 */
	public void doSaveLh1AandRef(String hsiId,Lh1a lh1a,String dbOperate,ComUser user,String modelSeriesId)throws BusinessException ;
	
	/**
	 * 通过Lhsid 查询lhstep表中的数据
	 * @param HsiId 表lh_HSI 
	 * @param comuser  user
	 * @return 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-25
	 */
	public void lh1aStepshow(String hsiId,ComUser user,String modelSeriesId)throws BusinessException ;
	
}
