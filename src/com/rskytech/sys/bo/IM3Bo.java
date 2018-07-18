package com.rskytech.sys.bo;

import java.util.ArrayList;
import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.M3;
import com.rskytech.pojo.M3Additional;

public interface IM3Bo extends IBo {
	/**
	 * 根据故障原因Id查询唯一一条M3
	 * @param m13cId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public M3 getM3ByM13cId(String m13cId)throws BusinessException ;
	/**
	 *  根据m3Id和任务ID查询唯一一条附加任务记录
	 * @param m13cId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public M3Additional getM3AdditionalByM3Id(String m3Id,String taskId)throws BusinessException;
	/**
	 * 根据msiId查询M3
	 * @param msiId
	 * @return
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-25
	 */
	public List<M3> getM3ListByMsiId(String msiId)throws BusinessException;
	
	/**
	 * 获取供应商
	 */
	public List getVendorCountByMsi(String msiId)throws BusinessException;
	/**保存m3数据
	 * @param user 用户
	 * @param pageId 操作页面
	 * @param systemLhirfCode 所属系统
	 * @param jsonData    json字符串
	 * @param msiId      系统main表的Id
	 * @param m13cId     故障原因Id
	 */
	public ArrayList<String> saveM3(ComUser user, String pageId, String sourceSystem,
			String jsonData, String msiId, String m13cId,String jsonTask,ComModelSeries comModelSeries);
	/**
	 * 删除任务
	 * @param m13cId  故障原因Id
	 * @param select  任务所属问题的回答
	 * @param effectResult 删除的任务所属的问题号
	 * @param taskId       任务Id
	 * @param sysUser      当前登录用户
	 * @param msiId        当前MSIId
	 * @return 
	 */
	public ArrayList<String> deleteTask(String m13cId, String select, String effectResult,
			String taskId, ComUser sysUser,String msiId,ComModelSeries comModelSeries);
}
