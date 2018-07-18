package com.rskytech.area.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.TaskMsgDetail;

public interface IZa7Dao extends IDAO {

	/**
	 * 查询转移到区域的系统任务
	 * @param msId 机型ID
	 * @param areaId 转移到的区域的ID
	 * @return 任务数据列表
	 * @author zhangjianmin
	 */
	public List<Object[]> searchSysTask(String msId, String areaId) throws BusinessException;
	
	/**
	 * 查询转移到区域的结构任务
	 * @param msId 机型ID
	 * @param areaId 转移到的区域的ID
	 * @return 任务数据列表
	 * @author zhangjianmin
	 */
	public List<Object[]> searchStructureTask(String msId, String areaId) throws BusinessException;
	
	/**
	 * 查询转移到区域的LHIRF任务
	 * @param msId 机型ID
	 * @param areaId 转移到的区域的ID
	 * @return 任务数据列表
	 * @author zhangjianmin
	 */
	public List<Object[]> searchLhirfTask(String msId, String areaId) throws BusinessException;
	
	/**
	 * 查询转移到区域的任务的合并明细
	 * @param taskId 转移到区域任务的ID
	 * @param areaId 接收任务的区域ID
	 * @return 任务数据列表
	 * @author zhangjianmin
	 */
	public List<TaskMsgDetail> searchDestTaskDetail(String taskId, String areaId) throws BusinessException;
	
	/**
	 * 查询转移区域任务的接收、拒绝、待定情况
	 * @param taskId 转移到区域任务的ID
	 * @author zhangjianmin
	 */
	public Object[] getAcceptResult(String taskId) throws BusinessException;
	
	/**
	 * 查询拒绝接收的明细
	 * @param taskId 转移到区域任务的ID
	 * @author zhangjianmin
	 */
	public TaskMsgDetail getNoAcceptTaskMsgDetail(String taskId) throws BusinessException;
	
	/**
	 * 清楚标准区域任务的重复任务间隔的垃圾数据
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public boolean cleanTaskInterval(String msId);
	
	/**
	 * 查询区域报表ZA7中需要的数据
	 * @param msId 机型ID
	 * @param areaId 转移到的区域的ID
	 * @return 任务数据列表
	 * @author zhangjianmin
	 */
	public List<Object[]> getReportZa7List(String msId, String areaId) throws BusinessException;
}
