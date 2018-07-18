package com.rskytech.area.bo;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.Za5;

public interface IZa5Bo extends IBo {

	/**
	 * 生成za5的第一矩阵表格
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public String loadFirstMatrix(String msId);
	
	/**
	 * 生成za5的第二矩阵表格
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public String loadSecondMatrix(String msId);
	
	/**
	 * 生成za5的第三矩阵表格
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public String loadThirdMatrix(String msId);
	
	/**
	 * 生成za5的最终矩阵表格
	 * @param msId 机型ID
	 * @author zhangjianmin
	 */
	public String loadLastMatrix(String msId);
	
	/**
	 * 通过区域主表ID，查询ZA5
	 * @param zaId 区域主表ID
	 * @param step ZA5A或ZA5B
	 * @return Za5
	 * @author zhangjianmin
	 */
	public Za5 getZa5ByZaId(String zaId, String step);
	
	/**
	 * 查询当前标准区域分析的ED/AD选项
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param step 当前步骤：ZA5A、ZA5B
	 * @return 显示信息
	 * @author zhangjianmin
	 */
	public JSONObject loadLevel(String msId, String zaId, String step);
	
	/**
	 * 保存ZA43页面
	 * @param userId 用户ID
	 * @param ms 机型实例
	 * @param zaId 区域主表ID
	 * @param step ZA5的步骤：ZA5A和ZA5B
	 * @param checkLevelArr 选择项
	 * @param reachWay 接近方式
	 * @param taskDesc 任务描述
	 * @param taskInterval 任务间隔
	 * @param level_1 选择中间项
	 * @param level_2 选择中间项
	 * @param level_3 选择中间项
	 * @author zhangjianmin
	 */
	public JSONObject saveZa5(String userId, ComModelSeries ms, String zaId, String step, String checkLevelArr, 
			String reachWay, String taskDesc, String taskInterval, Integer level_1, Integer level_2, Integer level_3);
}
