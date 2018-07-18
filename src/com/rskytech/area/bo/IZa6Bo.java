package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;

public interface IZa6Bo extends IBo {
	
	/**
	 * 查询ZA6页面需要显示的任务及其信息
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param areaId 区域ID
	 * @return 显示信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa6List(String msId, String zaId, String areaId);
	
	/**
	 * 查询标准区域任务
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 显示信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getStandardTaskList(String msId, String zaId);
	
	/**
	 * 保存ZA6
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param doSelect 处理方式
	 * @param taskId 当前增强任务的ID
	 * @param destTask 合并当前增强任务的标准任务的ID
	 * @param taskIntervalMerge 合并后的任务间隔
	 * @author zhangjianmin
	 */
	public JSONObject saveZa6(String userId, String msId, String zaId, String doSelect, String taskId, String destTask, String taskIntervalMerge);

	/**
	 * 清楚标准区域任务的重复任务间隔的垃圾数据
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public void cleanTaskInterval(String msId);
}
