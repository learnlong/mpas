package com.rskytech.paramdefinemanage.bo;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.TaskMpdVersion;

public interface ITaskMpdVersionBo extends IBo{

	/**
	 * 保存或更新MPD版本
	 * 
	 * @param version
	 * @param operateFlg
	 * @param userid
	 * @return
	 * @throws BusinessException
	 */
	public boolean saveVersion(TaskMpdVersion version, String operateFlg, String userid) throws BusinessException;
	
	/**
	 * 根据机型号获取MPD版本号最大值
	 * 
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public int getMaxVersionCode(String modelSeriesId, String versionType, Integer validFlag) throws BusinessException;
}
