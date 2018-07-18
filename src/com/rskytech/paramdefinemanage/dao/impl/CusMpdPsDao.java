package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.ICusMpdPsDao;
import com.rskytech.pojo.CusMpdPs;

public class CusMpdPsDao extends BaseDAO implements ICusMpdPsDao{

	@SuppressWarnings("rawtypes")
	@Override
	public List findAllPsByModelSeriesId(String modelSeriesId) throws DAOException {
		String hqlString = "from CusMpdPs p where p.comModelSeries.modelSeriesId = '" + modelSeriesId +"' order by p.psFlg asc,p.psSort asc";
		return this.findByHql(hqlString, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public CusMpdPs findById(String id) throws DAOException {
		String hqlString = "from CusMpdPs p where p.psId = '" + id+"'";
		List list = this.findByHql(hqlString, null);
		if (list.size() == 1)
			return (CusMpdPs) list.get(0);
		return null;
	}
	
	/**
	 * 当前机型是否存在唯一一条MPD附录类型为"报表首页"的记录
	 * 
	 * @param modelSeriesId
	 * @param flg
	 * @param isUpdate
	 * @return
	 * @throws DAOException
	 */
	public boolean isPsFlgUnique(String modelSeriesId, String cusMpdPsId) throws DAOException {
		String hqlString = "SELECT P.PSID FROM CUSMPDPS P WHERE P.COMMODELSERIES.MODELSERIESID = '" + modelSeriesId + "' AND P.PSFLG = 0 ";
		if (cusMpdPsId != null && "0".equals(cusMpdPsId )) {
			hqlString += "AND P.PSID <> '" + cusMpdPsId+"'";
		}
		return this.findByHql(hqlString, null).size() == 1;
	}
	
}
