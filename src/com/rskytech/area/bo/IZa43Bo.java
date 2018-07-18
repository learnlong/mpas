package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;

public interface IZa43Bo extends IBo {

	/**
	 * 创建ZA43中的评级表的HTML代码
	 * @param msId 机型ID
	 * @return ZA43评级表的HTML代码
	 * @author zhangjianmin
	 */
	public String generateMatrixHtml(String msId);
	
	/**
	 * 查询ZA42生成的任务信息
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 任务信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> loadTaskMsgList(String msId, String zaId);
	
	/**
	 * 查询选择任务后的任务数据和对应的ZA43记录
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param taskId 任务ID
	 * @return 显示信息
	 * @author zhangjianmin
	 */
	public JSONObject loadZa43Analysis(String msId, String zaId, String taskId);
	
	/**
	 * 保存ZA43页面
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param areaId 区域ID
	 * @param zaId 区域主表ID
	 * @param taskId 非RST任务的ID
	 * @param taskDesc 非RST任务的任务描述
	 * @param reachWay 非RST任务的接近方式
	 * @param taskInterval 非RST任务的任务间隔
	 * @param rstTaskId RST任务的ID
	 * @param rstTaskDesc RST任务的任务描述
	 * @param rstReachWay RST任务的接近方式
	 * @param rstTaskInterval RST任务的任务间隔
	 * @param za43Select ZA43矩阵中的各个选择的结果
	 * @param finalResult ZA43矩阵中的最终结果
	 * @author zhangjianmin
	 */
	public JSONObject saveZa43(String userId, String msId, String areaId, String zaId, String taskId, String taskDesc, String reachWay, String taskInterval, 
			String rstTaskId, String rstTaskDesc, String rstReachWay, String rstTaskInterval, String za43Select, String finalResult);
}
