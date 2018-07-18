package com.rskytech.struct.bo;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.SStep;

public interface ISsiStepBo extends IBo {
	/**
	 * 根据传入的步骤的值确定页面的导航栏显示情况
	 * @param ssiId
	 * @param ssiStep
	 * @param stepValue 初始化的页面
	 * @return
	 */
	public int[] initStep(String ssiId,SStep ssiStep,String stepPage);
	
	public SStep changeStep(SStep sStep,String ssiId);
	
	/**
	 * 保存或者更新数据后变更状态
	 * @param ssiId
	 */
	public void changeStatus(String ssiId);
}
