package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.ITaskMpdVersionDao;
import com.rskytech.pojo.TaskMpdVersion;

public class TaskMpdVersionDao extends BaseDAO implements ITaskMpdVersionDao {

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TaskMpdVersion> findByModelSeriesId(String modelSeriesId, String versionType, Integer validFlag) throws DAOException {
		String hqlString = "from TaskMpdVersion v where v.versionType = ? and v.validFlag = ? and v.comModelSeries.modelSeriesId = ?";
		List list = this.findByHql(hqlString, new Object[] { versionType, validFlag, modelSeriesId });
		return list;
	}
	
	@Override
	public int getMaxVersionCode(String modelSeriesId, String versionType, Integer validFlag) throws DAOException {
		String hql = "select max(CAST(v.versionNo as integer)) from TaskMpdVersion v where v.versionType = '" + versionType + "' and v.validFlag = " + validFlag;
		Integer maxId = (Integer) this.getSession().createQuery(hql).uniqueResult();
		return maxId == null ? 0 : maxId;
	}
	
}
