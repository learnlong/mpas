package com.rskytech.area.bo;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ZaMain;
import com.rskytech.pojo.ZaStep;

public interface IZaStepBo extends IBo {

	/**
	 * 如果通过区域主表ID，没有查询到区域步骤记录，则插入新记录，最后返回区域步骤记录
	 * @param userId 用户ID
	 * @param zaMain 区域主表
	 * @param pageName 当前页面的名称
	 * @return 区域步骤
	 * @author zhangjianmin
	 */
	public ZaStep selectZaStep(String userId, ZaMain zaMain, String pageName);
	
	/**
	 * 因ZA1数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param type 当前区域分析的分析类型（不需要区域分析、标准区域分析、增强区域分析）
	 * @author zhangjianmin
	 */
	public void updateZa1StepAndStatus(String userId, String zaId, String type);
	
	/**
	 * 因ZA2数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param position 内部/外部
	 * @return 下一步跳转页面的代码
	 * @author zhangjianmin
	 */
	public Integer updateZa2StepAndStatus(String userId, String zaId, Integer position);
	
	/**
	 * 因ZA41数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param rstTask RST任务状态，1新增，2删除，0没变
	 * @return 下一步跳转页面的代码
	 * @author zhangjianmin
	 */
	public Integer updateZa41StepAndStatus(String userId, String zaId, Integer rstTask);
	
	/**
	 * 因ZA42数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param taskStatus 任务状态，1新增，2删除，0没变
	 * @return 下一步跳转页面的代码
	 * @author zhangjianmin
	 */
	public Integer updateZa42StepAndStatus(String userId, String zaId, Integer taskStatus);
	
	/**
	 * 因ZA43数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @return 下一步跳转页面的代码
	 * @author zhangjianmin
	 */
	public Integer updateZa43StepAndStatus(String userId, String msId, String zaId);
	
	/**
	 * 因ZA5数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaId 区域主表ID
	 * @param za5Step 当前ZA5的步骤 
	 * @return 下一步跳转页面的代码
	 * @author zhangjianmin
	 */
	public Integer updateZa5StepAndStatus(String userId, String msId, String zaId, String za5Step);
	
	/**
	 * 因ZA6数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaMain 区域主表
	 * @author zhangjianmin
	 */
	public Integer updateZa6StepAndStatus(String userId, String msId, ZaMain zaMain);
	
	/**
	 * 因ZA7数据变更，更新ZA_STEP和分析状态
	 * @param userId 用户ID
	 * @param msId 机型ID
	 * @param zaMain 区域主表
	 * @author zhangjianmin
	 */
	public Integer updateZa7StepAndStatus(String userId, String msId, ZaMain zaMain);
}
