package com.rskytech.lhirf.bo;


import java.util.ArrayList;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Lh4;

public interface ILh4Bo extends IBo {
	
	
	/**
	 * 通过Lhsid 查询lh4 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh4数据
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-24
	 */
	public Lh4 getLh4BylhHsId(String hsiId)throws BusinessException ;
	/**
	 * 通过Lhsid 查询lh4 表中的数据
	 * @param Lh4 tlh4
	 * @param String hsiId
	 * @param String dbOperate DB操作标识符
	 * @param comuser  user
	 * @param Integer needLhTask页面传值 是否需要维修任务
	 * @param Integer oldneedLhTask 页面改动前的 是否维修任务值
	 * @param String[] eDArr,String[] aDArr 页面自定义ED  AD各个值
	 * @return 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-25
	 */
	public ArrayList<String> doSaveLh4andRef(Lh4 tlh4, String hsiId, String dbOperate,
			ComUser user, Integer needLhTask, Integer oldneedLhTask,
			boolean isSafeChange, String[] eDArr, String[] aDArr,ComModelSeries comModelSeries)
			throws BusinessException;
}
