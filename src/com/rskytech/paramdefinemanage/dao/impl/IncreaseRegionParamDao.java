package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.dao.IIncreaseRegionParamDao;

public class IncreaseRegionParamDao extends BaseDAO implements IIncreaseRegionParamDao{
	
	
	
	@Override
	public List getLevelCount(String modelSeriesId, Integer vaildFlg) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT DISTINCT LEVELCOUNT FROM"); 
		sb.append(" (SELECT Z.ITEM_S45_ID, COUNT (C.LEVEL_ID) LEVELCOUNT");
		sb.append(" FROM CUS_ITEM_S45 Z LEFT JOIN CUS_LEVEL C ON Z.ITEM_S45_ID = C.ITEM_ID");    
		sb.append(" WHERE Z.STEP_FLG ='" + ComacConstants.ZA43 + "'");
		sb.append(" AND Z.VALID_FLAG =" + vaildFlg);
		sb.append(" AND C.VALID_FLAG =" + vaildFlg);
		sb.append(" AND Z.MODEL_SERIES_ID ='" + modelSeriesId+"'");
		sb.append(" GROUP BY Z.ITEM_S45_ID");
		// 把在LEVEL表里不存在的项目统计出来，LEVELCOUNT直接设为0
		sb.append(" UNION SELECT Z1.ITEM_S45_ID, 0 AS LEVELCOUNT");
		sb.append(" FROM CUS_ITEM_S45 Z1");
		sb.append(" WHERE NOT EXISTS (SELECT 1 FROM CUS_LEVEL C1 WHERE Z1.ITEM_S45_ID = C1.ITEM_ID AND C1.VALID_FLAG = 1)");
		sb.append(" AND Z1.VALID_FLAG =" + vaildFlg);
		sb.append(" AND Z1.MODEL_SERIES_ID ='" + modelSeriesId+"'");
		sb.append(" AND Z1.STEP_FLG ='" + ComacConstants.ZA43 + "')");
        List list = this.executeQueryBySql(sb.toString());
		return list;
	}

}
