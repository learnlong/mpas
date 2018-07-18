package com.rskytech.paramdefinemanage.dao.impl;

import java.util.List;

import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.dao.IStandardRegionParamDao;

public class StandardRegionParamDao extends BaseDAO implements IStandardRegionParamDao{

	@Override
	public List getLevelCount(String modelSeriesId,Integer vaildFlg) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT DISTINCT LEVELCOUNT FROM"); 
		sb.append(" (SELECT   Z.ITEM_ZA5_ID, COUNT (C.LEVEL_ID) LEVELCOUNT");
		sb.append(" FROM CUS_ITEM_ZA5 Z LEFT JOIN CUS_LEVEL C ON Z.ITEM_ZA5_ID = C.ITEM_ID");    
		sb.append(" WHERE Z.IS_LEAF_NODE =" + ComacConstants.YES);
		sb.append(" AND C.VALID_FLAG =" + vaildFlg);
		sb.append(" AND Z.VALID_FLAG =" + vaildFlg);
		sb.append(" AND Z.MODEL_SERIES_ID ='" + modelSeriesId+"'");
		sb.append(" GROUP BY Z.ITEM_ZA5_ID");
		// 把在LEVEL表里不存在的项目统计出来，LEVELCOUNT直接设为0
		sb.append(" UNION SELECT Z1.ITEM_ZA5_ID, 0 AS LEVELCOUNT");
		sb.append(" FROM CUS_ITEM_ZA5 Z1");
		sb.append(" WHERE NOT EXISTS (SELECT 1 FROM CUS_LEVEL C1 WHERE Z1.ITEM_ZA5_ID = C1.ITEM_ID AND C1.VALID_FLAG = 1)");
		sb.append(" AND Z1.VALID_FLAG =" + vaildFlg);
		sb.append(" AND Z1.MODEL_SERIES_ID ='" + modelSeriesId+"'");
		sb.append(" AND Z1.IS_LEAF_NODE =" + ComacConstants.YES + ")");
        List list = this.executeQueryBySql(sb.toString());
		return list;
	}
	
	/*
	 * 查询指定机型下每集项目下的的项目节点
	 * @param rowNum 节点的级数
	 * @param itemZa5Id 项目ID
	 * @param modelSeriesId 机型系列编号
	 * @return 项目节点list
	 */
	@SuppressWarnings("unchecked")
	public List getAllLeaf(Integer rowNum,String itemZa5Id,String modelSeriesId){
		String sql ="";
		if(rowNum == 1){		//查询第一行下所有的叶子节点
			sql = "select * from CUS_ITEM_ZA5 where IS_LEAF_NODE = '1' " +
					"and VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"'and MODEL_SERIES_ID ='"+modelSeriesId+"'" +
					"  and PARENT_ID ='"+itemZa5Id+"' " +
					"UNION select * from CUS_ITEM_ZA5 where  IS_LEAF_NODE = '1' and  VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"'" +
					" and  PARENT_ID in " +
					"(select ITEM_ZA5_ID from CUS_ITEM_ZA5 where  MODEL_SERIES_ID ='"+modelSeriesId+"'  and " +
					"VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"'   and PARENT_ID ='"+itemZa5Id+"') " +
					"UNION select * from CUS_ITEM_ZA5 where VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"' " +
					"and PARENT_ID in ( select ITEM_ZA5_ID from CUS_ITEM_ZA5 where  " +
					"IS_LEAF_NODE = '0' and  VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"' and  " +
					"PARENT_ID in" +
					"(select ITEM_ZA5_ID from CUS_ITEM_ZA5 where  MODEL_SERIES_ID ='"+modelSeriesId+"' " +
					"and VALID_FLAG = '"+ComacConstants.VALIDFLAG_YES+"'   and PARENT_ID ='"+itemZa5Id+"'))" ;
			}
		else if(rowNum == 2){	//查询第二行下所有的叶子节点
			sql =  "select * from CUS_ITEM_ZA5 c where c.is_leaf_node ='1'and c.valid_flag ='"+ComacConstants.VALIDFLAG_YES+"' " +
					"and c.parent_id = '"+itemZa5Id+"' and c.model_series_id ='"+modelSeriesId+"' " +
					"UNION  select * from CUS_ITEM_ZA5 c where c.valid_flag = '"+ComacConstants.VALIDFLAG_YES+"' " +
					"and c.model_series_id ='"+modelSeriesId+"' and c.parent_id in " +
					"(select c.item_za5_id from CUS_ITEM_ZA5 c where c.is_leaf_node ='0'" +
					"and c.valid_flag ='"+ComacConstants.VALIDFLAG_YES+"' and c.model_series_id ='"+modelSeriesId+"' and c.parent_id = '"+itemZa5Id+"')";	
		}else if(rowNum == 3){	//查询第三行下所有的叶子节点
			sql = " select * from Cus_Item_Za5 c where c.valid_flag ='"+ComacConstants.VALIDFLAG_YES+"'" +
					" and c.model_series_id='"+modelSeriesId+"' and c.parent_id = '"+itemZa5Id+"'";
			
		}
		List list = executeQueryBySql(sql);
		return list;
	}
	
	
	
}
