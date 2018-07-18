package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.dao.ILhMrbDao;
import com.rskytech.pojo.TaskMrb;

@SuppressWarnings("unchecked")
public class LhMrbDao extends BaseDAO implements ILhMrbDao {

	/**
	 * 登陆用户ID号 查询其分析权限下的 msg任务
	 * 
	 * @param modelSeriesId
	 * @param userId 用戶id
	 * @param nowsysTem 任务生成所在子系统
	 * @return objectlist[]
	 */
	@Override
	public List<Object[]> getLhMsgListBythree(String modelSeriesId, String userId,
			String taskType, String ipvOpvpOpve, String taskInterval)
			throws BusinessException {
		    String str1  ="";
		    String str2 =  "" ;
		    String str3 =  "" ;
	       if (taskType == null || "".equals(taskType)){
	    	    str1 =  "" ;
	       }else{
	    	    str1 = " AND T.TASK_TYPE = '" + taskType+"'"; 
	       }
	       if( ipvOpvpOpve == null || "".equals(ipvOpvpOpve)){
	    	    str2 =  "" ;
	       }else{
	    	    str2 = " AND T.ANY_CONTENT1 ='" + ipvOpvpOpve+"'"; 
	       }
	       
	       if( taskInterval == null ||  "".equals(taskInterval)){
	    	    str3 =  "" ;
	       }else{
	    	    str3 = "  AND T.TASK_INTERVAL like '%" + taskInterval+"%'";  
	       }
	      StringBuffer sb = new StringBuffer();
	      sb.append("SELECT A.AREA_CODE,Z.* FROM (");
	      sb.append("SELECT h.hsi_code,m.mrb_id,m.mrb_code," +
	    		  "t.task_id,t.task_code,t.task_type,t.any_content1,t.task_interval,t.reach_way,t.task_desc," +
	      		  "t.effectiveness,t.mrb_id as mrbIds,t.need_transfer,t.has_accept,t.task_valid,t.model_series_id,t.valid_flag,h.area_id");
	      sb.append(" FROM task_msg t JOIN lh_main h ON h.hsi_id=t.source_ana_id  AND h.model_series_id=t.model_series_id ");
	      sb.append(" LEFT JOIN task_mrb m ON m.mrb_id=t.mrb_id AND m.model_series_id=t.model_series_id");
	      sb.append(" WHERE t.model_series_id = '"+modelSeriesId+"'");
	      sb.append(" AND t.create_user = '"+userId+"'");
	      sb.append(" AND t.source_system = '"+ComacConstants.LHIRF_CODE+"'");
	      sb.append(" "+str1+ " "+ str2+ " "+ str3);
	      sb.append(" AND t.task_code IS NOT NULL) Z");
	      sb.append(" JOIN COM_AREA A ON Z.AREA_ID = A.AREA_ID AND A.MODEL_SERIES_ID =Z.MODEL_SERIES_ID  WHERE Z.TASK_VALID IS NULL ");
	      sb.append(" AND Z.VALID_FLAG ="+ComacConstants.VALIDFLAG_YES);
	      sb.append(" AND (Z.NEED_TRANSFER IS NULL OR Z.NEED_TRANSFER  =" +ComacConstants.NO);
	      sb.append(" OR (Z.NEED_TRANSFER = "+ComacConstants.YES+" AND Z.HAS_ACCEPT = "+ComacConstants.NO+"))");
	      sb.append(" ORDER BY Z.HSI_CODE ,Z.TASK_CODE");
	     return this.executeQueryBySql(sb.toString());
	       
	}

	@Override
	public List<TaskMrb> getMrbByMrbCode(String modelSeriesId, String mrbCode)
			throws BusinessException {
		   DetachedCriteria dc =DetachedCriteria.forClass(TaskMrb.class);
		   dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId ));
		   dc.add(Restrictions.eq("mrbCode", mrbCode ));
		   dc.add(Restrictions.eq("validFlag", ComacConstants.YES ));
		   return this.findByCriteria(dc);
	}

}
