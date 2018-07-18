package com.rskytech.lhirf.bo;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.Lh5;
import com.rskytech.pojo.LhMain;
import com.rskytech.pojo.TaskMsg;

public interface ILh5Bo extends IBo {
	
	
	/**
	 * 通过Lhsid 查询lh5 表中的数据
	 * @param HsiId 表lh_HSI ID
	 * @return Lhirf 中 lh1对象 
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-8-23
	 */
	public Lh5 getLh5ByHsiId(String hsiId)throws BusinessException ;

	/**
	 * 保存 LH5 以及Lh5 分析过程中产生的任务 msg-3
	 * @param Lhhsi 表lh_HSI 
	 * @param taskmsg 表taskMsg
	 * @param userId 
	 * @return  
	 * @throws BusinessException
	 * @author wangyueli
	 * @createdate 2012-12-06
	 */
	public void saveLh5andMsg( Lh5 lh5, String operateFlgLh5,TaskMsg taskMsg, String operateFlgMsg,String userId)throws BusinessException ;


	/**
	 * 通过Lhsid 查询lh5 表中的数据
	 * @param comModelSeries 
	 * @param HsiId 表lh_HSI 
	 * @param String  needRedesign 页面传值是否需要维修任务
	 * @param String  dbOperate DB操作标识符
	 * @param comuser  user
	 * @param LhMain	lhHsilh5
	 * @param Lh5  lh5
	 * @param String jsonData 页面任务列表传入值
	 * @param String[] arryDeltTaskId 页面任务列表传入值 待删除的MSG-3 id
	 * @return 
	 * @throws BusinessException
	 */
	public ArrayList<String> doSaveLh5andRef(String hsiId,String needRedesign,  String dbOperate,
			ComUser user,LhMain	lhHsilh5,Lh5  lh5,String jsonData,String[] arryDeltTaskId, ComModelSeries comModelSeries) throws BusinessException  ;
	/**
	 * 查询Cus_Interval表中属于某一机分析区域数据 A或者 B 套数据
	 * @param  anaFlg LH5/S6
	 * @param  internalFlg A/B
	 * @param modelSeriesId
	 * @return 自定义评级表List
	 * @throws BusinessException
	 */
	public List<CusInterval> getCusIntervalbyFlg(String anaFlg, String internalFlg,
			String modelSeriesId);

}
