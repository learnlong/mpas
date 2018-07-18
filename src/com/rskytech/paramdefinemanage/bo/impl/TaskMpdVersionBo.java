package com.rskytech.paramdefinemanage.bo.impl;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.paramdefinemanage.bo.ITaskMpdVersionBo;
import com.rskytech.paramdefinemanage.dao.ITaskMpdVersionDao;
import com.rskytech.pojo.TaskMpdVersion;

public class TaskMpdVersionBo extends BaseBO implements ITaskMpdVersionBo{

	private ITaskMpdVersionDao taskMpdVersionDao;
	
	
	
	public ITaskMpdVersionDao getTaskMpdVersionDao() {
		return taskMpdVersionDao;
	}

	public void setTaskMpdVersionDao(ITaskMpdVersionDao taskMpdVersionDao) {
		this.taskMpdVersionDao = taskMpdVersionDao;
	}

	@Override
	public boolean saveVersion(TaskMpdVersion version, String operateFlg, String userid) throws BusinessException {
		this.saveOrUpdate(version, operateFlg, userid);
		return true;
	}
	
	@Override
	public synchronized int getMaxVersionCode(String modelSeriesId, String versionType, Integer validFlag) throws BusinessException {
		int maxId = this.taskMpdVersionDao.getMaxVersionCode(modelSeriesId, versionType, validFlag);
		return ++maxId;
	}
	
}
