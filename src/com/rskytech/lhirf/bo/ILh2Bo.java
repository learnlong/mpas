package com.rskytech.lhirf.bo;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh2;

public interface ILh2Bo extends IBo {
	/**
	 * 通过Lhsid 查询lh2 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-23
	 */
	public Lh2 getLh2ByHsiId(String hsiId)throws BusinessException ;
	/**
	 * 通过Lhsid 保存 lh2 表中的数据
	 * @param ComUser user,
	  * @param String hsiId
	 * @param String env,页面中英文 说明
	 * @param String picContent,
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-22
	 */
	public void saveLh2andStep(ComUser user, String hsiId, String env,String picContent)throws BusinessException ;

}
