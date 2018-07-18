package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.TaskMrb;

public interface ITaskMrbMaintainBo  extends IBo{
	
	public List<TaskMrb> getMrbTaskBySourceSystem(String modelSeriesId, String sourceSystem) throws BusinessException;

}
