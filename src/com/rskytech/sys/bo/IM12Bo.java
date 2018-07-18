package com.rskytech.sys.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M12;

public interface IM12Bo extends IBo {
	/**
	 *根据msiId查询M12
	 * @param msiId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-21
	 */
	public List<M12> getM12AListByMsiId(String msiId)throws BusinessException ;
	/**
	 * 第一次加载M12时从M0表中同步数据
	 * @param msiId
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-21
	 */
	public void cogradientM12(String msiId,String userId)throws BusinessException;
	/**
	 * 删除M12
	 * @param m12Id
	 * @param userId
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-19
	 */
	public void delete(String m12Id, String userId)throws BusinessException ;
	/**
	 * 保存m0的数据,其中save保存com_log_operate,saveorupdate保存com_log_db以及保存m0
	 * @param m0 m0数据
	 * @param dbOperate 数据操作类型
	 * @param user 当前用户
	 * @param pageId 操作页面
	 * @param source_system 操作方式(系统,area,lhirf,struct)
	 * @param jsonData 修改的数据集
	 * @param comModelSeries 
	 */
	public HashMap<String, String> SaveOrUpdateM12(ComUser user,
			String pageId, String source_system,String jsonData, ComModelSeries comModelSeries,String isUpdateStep);
}
