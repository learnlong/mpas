package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;

public interface IZa7Bo extends IBo {

	/**
	 * 查询ZA7页面的列表
	 * @param msId 机型ID
	 * @param areaId 当前区域的ID
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa7List(String msId, String areaId);
	
	/**
	 * 保存ZA7
	 * @param areaId 当前区域的ID
	 * @param hasAccept 是否接收
	 * @param taskId 转区域任务ID
	 * @param destTask 区域接收任务ID
	 * @param taskIntervalRepeat 合并后的任务间隔
	 * @param rejectResion 拒绝原因
	 * @author zhangjianmin
	 */
	public void saveZa7(String areaId, Integer hasAccept, String taskId, String destTask, String taskIntervalRepeat, String rejectResion);
	
	/**
	 * 改变转区域任务的整体接收和拒绝其情况
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param areaId 当前区域的ID
	 * @param zaId 区域主表ID
	 * @param taskId 转区域任务ID
	 * @author zhangjianmin
	 */
	public void changeTask(String userId, String msId, String areaId, String zaId, String taskId);
	
	/**
	 * 变更状态和导航栏
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @author zhangjianmin
	 */
	public JSONObject updateZa7StepAndStatus(String userId, String msId, String zaId);
	
	/**
	 * 清楚标准区域任务的重复任务间隔的垃圾数据
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public void cleanTaskInterval(String msId);
}
