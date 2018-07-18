package com.rskytech.sys.bo;


import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M0;

public interface IM0Bo extends IBo {
	/**
	 * 查询最高可管理层是当前MSI的MSI及子ATA
	 * @param msiId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-19
	 */
	public List<M0>  getMsiATAListByMsiId(String msiId)throws BusinessException;
	/**
	 * 第一次加载M0时从Mselect表中同步数据
	 * @param msiId
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-19
	 */
	public  void cogradientM0(String msiId,String userId,String modelSerierId)throws BusinessException;
	/**
	 * 删除手动添加的ATA
	 * @param m0Id
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-19
	 */
	public void delete(String m0Id, String userId)throws BusinessException ;
	/**
	 * 保存m0的数据,其中save保存com_log_operate,saveorupdate保存com_log_db以及保存m0
	 * @param m0 m0数据
	 * @param dbOperate 数据操作类型
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 */
	public void saveOrUpdateM0(String jsonData,ComUser user,String defaultEff);
}
