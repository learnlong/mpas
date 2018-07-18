package com.rskytech.area.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.area.dao.IZa7Dao;
import com.rskytech.pojo.TaskMsgDetail;

public class Za7Dao extends BaseDAO implements IZa7Dao {

	@SuppressWarnings("unchecked")
	public List<Object[]> searchSysTask(String msId, String areaId) throws BusinessException{
		String sql = "SELECT A.TASK_ID, A.SOURCE_SYSTEM, C.ATA_CODE, C.ATA_NAME, A.TASK_CODE, A.TASK_TYPE, " +
                  	 "       A.TASK_INTERVAL, A.REACH_WAY, A.TASK_DESC, A.WHY_TRANSFER, D.HAS_ACCEPT, D.REJECT_REASON, D.DEST_TASK " +
					 "FROM TASK_MSG A, M_MAIN B, COM_ATA C, TASK_MSG_DETAIL D " +
					 "WHERE A.MODEL_SERIES_ID = B.MODEL_SERIES_ID " +
					 "  AND A.MODEL_SERIES_ID = C.MODEL_SERIES_ID " +
					 "  AND A.SOURCE_ANA_ID = B.MSI_ID " +
					 "  AND B.ATA_ID = C.ATA_ID " +
					 "  AND A.TASK_ID = D.TASK_ID " +
					 "  AND A.MODEL_SERIES_ID = '" + msId + "'"+
					 "  AND A.SOURCE_SYSTEM = '" + ComacConstants.SYSTEM_CODE + "' " +
					 "  AND ((A.TASK_VALID IS NULL AND A.NEED_TRANSFER = 1 AND A.SYS_TRANSFER = '" + ComacConstants.ZONAL_CODE + "') OR A.TASK_VALID = 2) " +
					 "  AND D.WHERE_TRANSFER = '" + areaId  +"'"+
					 "  AND A.VALID_FLAG = " + ComacConstants.YES +
					 "  AND B.VALID_FLAG = " + ComacConstants.YES +
					 "  AND C.VALID_FLAG = " + ComacConstants.YES + 
					 "  AND D.VALID_FLAG = " + ComacConstants.YES + " " +
					 "ORDER BY C.ATA_CODE";
		return this.executeQueryBySql(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> searchStructureTask(String msId, String areaId) throws BusinessException{
		String sql = "SELECT A.TASK_ID, A.SOURCE_SYSTEM, C.ATA_CODE, C.ATA_NAME, A.TASK_CODE, A.TASK_TYPE,  " +
					"        A.TASK_INTERVAL, A.REACH_WAY, A.TASK_DESC, A.WHY_TRANSFER, B.HAS_ACCEPT, B.REJECT_REASON, B.DEST_TASK " +
					"  FROM TASK_MSG A, TASK_MSG_DETAIL B,  " +
					"      (SELECT X.SSI_ID, Y.ATA_CODE, Y.ATA_NAME " +
					"         FROM S_MAIN X, COM_ATA Y " +
					"        WHERE X.ATA_ID = Y.ATA_ID " +
					"          AND X.MODEL_SERIES_ID = Y.MODEL_SERIES_ID " +
					"          AND X.MODEL_SERIES_ID = '" + msId + "' " +
					"          AND (X.IS_SSI = 1 OR (X.IS_SSI = 0 AND X.IS_ANA = 1)) " +
					"          AND X.IS_ADD IS NULL " +
					"          AND X.VALID_FLAG = " + ComacConstants.YES +
					"        UNION " +
					"        SELECT X.SSI_ID, X.ADD_CODE, X.ADD_NAME FROM S_MAIN X " +
					"        WHERE X.MODEL_SERIES_ID = '" + msId + "' " +
					"          AND (X.IS_SSI = 1 OR (X.IS_SSI = 0 AND X.IS_ANA = 1)) " +
					"          AND X.IS_ADD = 1 " +
					"          AND X.VALID_FLAG = " + ComacConstants.YES + ") C " +
					"  WHERE A.TASK_ID = B.TASK_ID " +
					"    AND A.MODEL_SERIES_ID = '" + msId + "' " +
					"    AND A.SOURCE_SYSTEM = '" + ComacConstants.STRUCTURE_CODE + "'  " +
					"    AND A.SOURCE_ANA_ID = C.SSI_ID " +
					"    AND ((A.TASK_VALID IS NULL AND A.NEED_TRANSFER = 1 AND A.SYS_TRANSFER = '" + ComacConstants.ZONAL_CODE + "') OR A.TASK_VALID = 2)  " +
					"    AND B.WHERE_TRANSFER = '" + areaId  +"' " +
					"    AND A.VALID_FLAG = " + ComacConstants.YES +
					"    AND B.VALID_FLAG = " + ComacConstants.YES +
					"  ORDER BY C.ATA_CODE";
		return this.executeQueryBySql(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> searchLhirfTask(String msId, String areaId) throws BusinessException{
		String sql = "SELECT A.TASK_ID, A.SOURCE_SYSTEM, D.HSI_CODE, D.HSI_NAME, A.TASK_CODE, A.TASK_TYPE, " +
					"        A.TASK_INTERVAL, A.REACH_WAY, A.TASK_DESC, A.WHY_TRANSFER, F.HAS_ACCEPT, F.REJECT_REASON, F.DEST_TASK  " +
					"  FROM TASK_MSG A, LH_MAIN D, TASK_MSG_DETAIL F  " +
					" WHERE A.MODEL_SERIES_ID = D.MODEL_SERIES_ID " +
					"   AND A.SOURCE_ANA_ID = D.HSI_ID " +
					"   AND A.TASK_ID = F.TASK_ID  " +
					"   AND A.TASK_CODE != '" + ComacConstants.EMPTY + "' " +
					"   AND A.MODEL_SERIES_ID = '" + msId + "' " +
					"   AND A.SOURCE_SYSTEM = '" + ComacConstants.LHIRF_CODE + "' " +
					"   AND ((A.TASK_VALID IS NULL AND A.NEED_TRANSFER = 1 AND A.SYS_TRANSFER = '" + ComacConstants.ZONAL_CODE + "') OR A.TASK_VALID = 2) " +
					"   AND F.WHERE_TRANSFER = '" + areaId  +"' " +
					"   AND A.VALID_FLAG = 1 " +
					"   AND D.VALID_FLAG = 1 " +
					"   AND F.VALID_FLAG = 1 " +
					" ORDER BY D.HSI_CODE";
		return this.executeQueryBySql(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsgDetail> searchDestTaskDetail(String taskId, String areaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		dc.add(Restrictions.eq("taskMsg.taskId", taskId));
		
		if (areaId != null){
			dc.add(Restrictions.eq("whereTransfer", areaId));
		}
		
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getAcceptResult(String taskId) throws BusinessException{
		String sql = "SELECT COUNT(CASE WHEN B.HAS_ACCEPT = 0 THEN 1 END), " +
					 "       COUNT(CASE WHEN B.HAS_ACCEPT IS NULL OR B.HAS_ACCEPT = 2 THEN 1 END), " +
					 "       COUNT(CASE WHEN B.HAS_ACCEPT = 1 THEN 1 END) " +
					 "FROM TASK_MSG_DETAIL B " +
					 "WHERE B.TASK_ID = '" + taskId  +"'"+
					 "  AND B.VALID_FLAG = " + ComacConstants.YES;
		List<Object[]> list = this.executeQueryBySql(sql);
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public TaskMsgDetail getNoAcceptTaskMsgDetail(String taskId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsgDetail.class);
		dc.add(Restrictions.eq("taskMsg.taskId", taskId));
		dc.add(Restrictions.eq("hasAccept", 0));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		List<TaskMsgDetail> tmd = this.findByCriteria(dc);
		if (tmd != null && tmd.size() > 0){
			return tmd.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public boolean cleanTaskInterval(String msId) {
		boolean flag = true;
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL PRO_UPDATE_TASK_INTERVAL(?)}"); 
		    st.setString(1, msId); 
		    st.execute(); 
	    }catch(Exception e){ 
	    	flag = false;
	    }finally{ 
		    session.disconnect(); 
		    session.close(); 
	    } 
		return flag;		
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getReportZa7List(String msId, String areaId) throws BusinessException{
		String sql = "SELECT A.TASK_ID, A.TASK_CODE, A.TASK_DESC, A.TASK_INTERVAL, A.WHY_TRANSFER, B.HAS_ACCEPT, B.REJECT_REASON, " +
					"       D.PROFESSION_NAME A, F.USER_NAME B, E.PROFESSION_NAME C, G.USER_NAME D " +
					"FROM TASK_MSG A, TASK_MSG_DETAIL B, COM_COORDINATION C, COM_PROFESSION D, COM_PROFESSION E, COM_USER F, COM_USER G " +
					"WHERE A.TASK_ID = B.TASK_ID " +
					"  AND A.TASK_ID = C.TASK_ID " +
					"  AND C.SEND_WORKGROUP = D.PROFESSION_ID(+) " +
					"  AND C.RECEIVED_WORKGROUP = E.PROFESSION_ID(+) " +
					"  AND C.SEND_USER = F.USER_ID(+) " +
					"  AND C.RECEIVE_USER = G.USER_ID(+) " +
					"  AND A.MODEL_SERIES_ID = '" + msId + "' " +
					"  AND A.SOURCE_SYSTEM IN ('STRUCTURE', 'LHIRF', 'SYS') " +
					"  AND ((A.TASK_VALID IS NULL AND A.NEED_TRANSFER = 1 AND A.SYS_TRANSFER = 'ZONE') OR A.TASK_VALID = 2) " +
					"  AND B.WHERE_TRANSFER = '" + areaId + "' " +
					"  AND A.VALID_FLAG = 1 AND B.VALID_FLAG = 1 AND C.VALID_FLAG = 1 AND D.VALID_FLAG = 1 " +
					"  AND E.VALID_FLAG = 1 AND F.VALID_FLAG = 1 AND G.VALID_FLAG = 1 " +
					"ORDER BY A.TASK_CODE";
		return this.executeQueryBySql(sql);
	}
}
