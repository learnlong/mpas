package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.paramdefinemanage.dao.ILhirfParamDao;

public class LhirfParamDao extends BaseDAO implements  ILhirfParamDao {

	/**
	 * 查询Cus_Interval表中属于某一机型分析区域的评级数据;
	 * @param object[0] internalFlg A/int
     * @param object[1] internalFlg B/out
     * @param object[2] anaFlg LH5/S6
     * @param object[3] modelSeriesId
	 * @return 自定义评级表List
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List getCusInList(Object[] ob) throws BusinessException {
		String sql="SELECT A.INTERVAL_LEVEL, "+
				   " MIN(CASE WHEN A.INTERNAL_FLG = '" + ob[0] + "' THEN A.INTERVAL_VALUE END) AS A ,"+
				   " MIN(CASE WHEN A.INTERNAL_FLG = '" + ob[1] + "' THEN A.INTERVAL_VALUE END) AS B,  "+
				   " MIN(CASE WHEN A.INTERNAL_FLG = '" + ob[0] + "' THEN A.INTERVAL_ID END) AS AID, "+
				   " MIN(CASE WHEN A.INTERNAL_FLG = '" + ob[1] +"' THEN A.INTERVAL_ID END) AS BID "+
				   " FROM CUS_INTERVAL A WHERE A.ANA_FLG = '" + ob[2] + "' AND A.MODEL_SERIES_ID= '" + ob[3] + "' "+
				   " GROUP BY A.INTERVAL_LEVEL ORDER BY A.INTERVAL_LEVEL ASC ";
		return executeQueryBySql(sql);
	}
	
	
	
	
}
