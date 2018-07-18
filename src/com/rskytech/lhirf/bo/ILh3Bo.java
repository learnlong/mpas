package com.rskytech.lhirf.bo;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh3;

public interface ILh3Bo extends IBo {
	
	/**
	 * 通过Lhsid 查询lh3 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh3数据List
	 * @throws BusinessException
	 */
	public  List<Lh3> getLh3ListByHsiId(String hsiId,Page page)throws BusinessException ;
	
	/**
	 * 通过Lhsid 保存 lh2 表中的数据
	 * @param String hsiId
	 * @param ComUser user,
	 * @param String jsonData 页面表格传值
	 * @return 
	 * @throws BusinessException
	 * @author wangyueli
	 * @param comModelSeries 
	 */
	public void saveLh3andStep(ComUser user, String hsiId, String jsonData, ComModelSeries comModelSeries)throws BusinessException ;

	public List<Lh3> getLh3lListByHsiIdNoPage(String hsiId) throws BusinessException;
}
