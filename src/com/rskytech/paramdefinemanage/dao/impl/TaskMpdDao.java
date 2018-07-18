package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.ITaskMpdDao;

public class TaskMpdDao extends BaseDAO implements ITaskMpdDao{

	@SuppressWarnings("rawtypes")
	@Override
	@Deprecated
	public List findBySql(String modelSeriesId, String SOURCE_SYSTEM) throws DAOException {
		String sqlString = "SELECT M.*,(SELECT WM_CONCAT(AREA_CODE) FROM COM_AREA A WHERE A.AREA_ID IN ("
				+ " SELECT REGEXP_SUBSTR (M.OWN_AREA, '[^,]+', 1,ROWNUM) FROM TASK_MPD CONNECT BY ROWNUM <="
				+ " LENGTH (M.OWN_AREA) - LENGTH (REPLACE (M.OWN_AREA, ',','')) + 1)) AREA_CODE FROM TASK_MPD M WHERE M.OWN_AREA IS NOT NULL"
				+ " and m.MODEL_SERIES_ID ='" + modelSeriesId + "' and M.SOURCE_SYSTEM = '" + SOURCE_SYSTEM + "' and m.VALID_FLAG = 1";
		return this.executeQueryBySql(sqlString);
	}
	
	@SuppressWarnings("rawtypes")
	public List findByHql(String modelSeriesId, String SOURCE_SYSTEM) throws DAOException {
		//这里的区域不能为空的条件 先注释掉 M.ownArea is not null and
		String hql = "from TaskMpd M where  M.comModelSeries.modelSeriesId = '" + modelSeriesId + "' and M.sourceSystem = '" + SOURCE_SYSTEM
				+ "' and M.validFlag = 1";
		return super.findByHql(hql, null);
	}
}
