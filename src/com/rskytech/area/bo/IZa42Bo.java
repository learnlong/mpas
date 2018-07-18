package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za42;

public interface IZa42Bo extends IBo {

	/**
	 * 查询是否已创建ZA42，如果没有，则创建
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @return za42Id
	 * @author zhangjianmin
	 */
	public String createZa42(String userId, String zaId);
	
	/**
	 * 查询ZA42数据
	 * @param za42Id ZA42表ID
	 * @return 
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public HashMap loadZa42(String za42Id);
	
	/**
	 * 查询问题7生成的任务信息
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 任务信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> loadTaskMsgList(String msId, String zaId);
	
	/**
	 * 保存ZA42的数据
	 * @param userId 用户ID
	 * @param cm 机型实例
	 * @param zaId 区域主表ID
	 * @param za42 za42实例
	 * @param jsonData 问题7的任务列表变更记录
	 * @param storeRemoveId 问题7中删除的任务
	 * @author zhangjianmin
	 */
	public JSONObject saveZa42(String userId, ComModelSeries ms, String zaId, Za42 za42, String jsonData, String storeRemoveId);
}
