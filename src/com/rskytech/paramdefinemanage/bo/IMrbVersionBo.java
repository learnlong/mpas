package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMpdVersion;

public interface IMrbVersionBo extends IBo  {

	/**
	 * 根据机型  对表TaskMpdVersion进行查询
	 * return list

	 */
	public List<TaskMpdVersion> getMRBList(String modelSeriesId,Page page) throws BusinessException;
	/**
	 * 根据机型 对表TaskMpdVersion进行查询

	 */
	public TaskMpdVersion getTaskMrbVersion(String modelSeriesId)throws BusinessException;
	/**
	 * 保存数据（事物处理）

	 */
	public void saveMRBVersion(ComUser comUser,ComModelSeries comModelSeries, String verId,
			String versionNo, String versionDescCn, String versionDescEn)
			throws BusinessException;
    
}
