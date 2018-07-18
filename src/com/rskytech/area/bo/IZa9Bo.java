package com.rskytech.area.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;

public interface IZa9Bo extends IBo {

	/**
	 * 查询ZA9页面需要显示的任务及其信息
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 显示信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> getZa9List(String msId, String zaId);
	
	/**
	 * 保存ZA9
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param jsonData jsonData对象
	 * @author zhangjianmin
	 */
	public void saveZa9(String userId, String msId, String jsonData);
}
