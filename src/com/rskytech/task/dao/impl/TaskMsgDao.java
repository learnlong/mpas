package com.rskytech.task.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.task.dao.ITaskMsgDao;

public class TaskMsgDao extends BaseDAO implements ITaskMsgDao {
	/**
     * 根据机型和任务编号查询任务
     * @param msiId
     * @param taskCode
     * @return
     * @throws BusinessException
     */
		public List<TaskMsg> getTaskByTaskCode(String modelSeriesId, String taskCode)
				throws BusinessException {
			DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
			dc.add(Restrictions.eq("taskCode",taskCode));
			dc.add(Restrictions.eq("comModelSeries.id",modelSeriesId));
			dc.add(Restrictions.eq("validFlag",ComacConstants.YES));
			return this.findByCriteria(dc);
		}
		
		/**
		 * 根据lhirf 或系统的Mainid 机型查询任务表中记录
		   @param modelSeriesId 当前机型id
		 * @param mainId 主表id生成任务的分析 mainId ID（四大MAIN的ID）
		 *  @param sourceSystem 产生任务系统
		 *  @param sourceStep  产生任务步骤
		 * @return
		 * @throws BusinessException
		 */
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskMsgListByMainId(String modelSeriesId,
			String mainId,String sourceSystem, String sourceStep)
			throws BusinessException {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("sourceAnaId",mainId));
		dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		if(sourceStep!=null&&!"".equals(sourceStep)&&!"null".equals(sourceStep)){
			dc.add(Restrictions.eq("sourceStep",sourceStep));
		}
		dc.add(Restrictions.eq("validFlag",ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}

	/**
	 * 查询系统转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskMsgIsSysTransfer(String modelSeriesId, 
			String sourceSystem,String firstTextField,Page page) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * from (");
		sb.append(" SELECT t.*,");
		sb.append(" c.ata_code ");
		sb.append(" FROM TASK_MSG t");
		sb.append(" JOIN m_main m");
		sb.append(" ON m.msi_id = t.source_ana_id");
		sb.append(" AND t.model_series_id = m.model_series_id");
		sb.append(" AND m.valid_flag = 1");
		sb.append(" LEFT JOIN com_ata c");
		sb.append(" ON m.ata_id = c.ata_id");
		sb.append(" AND c.valid_flag = 1");
		sb.append(" WHERE t.need_transfer = "+ComacConstants.YES);
		sb.append(" AND t.sys_transfer ='"+ComacConstants.ZONAL_CODE+"'");
		sb.append(" AND t.model_series_id ='"+modelSeriesId+"'");
		sb.append(" AND t.source_system ='"+sourceSystem+"') zz");
		if (firstTextField != null && !"".equals(firstTextField)) {
			sb.append(" where zz.ata_code like '%"+firstTextField+"%'");
		}
		return this.executeQueryBySql(sb.toString(), TaskMsg.class);
	}
	
	/**
	 * 查询结构转区域的任务
	 * @param modelSeriesId 
	 * @param sourceSystem
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getTaskMsgIsStructTransfer(String modelSeriesId, 
			String sourceSystem,String firstTextField,Page page) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * from (");
		sb.append(" SELECT t.*,");
		sb.append(" CASE WHEN s.ata_id IS NULL THEN s.add_code ELSE c.ata_code END ata_code ");
		sb.append(" FROM TASK_MSG t");
		sb.append(" JOIN s_main s");
		sb.append(" ON s.ssi_id = t.source_ana_id");
		sb.append(" AND t.model_series_id = s.model_series_id");
		sb.append(" AND s.valid_flag = 1");
		sb.append(" LEFT JOIN com_ata c");
		sb.append(" ON s.ata_id = c.ata_id");
		sb.append(" AND c.valid_flag = 1");
		sb.append(" WHERE t.need_transfer = "+ComacConstants.YES);
		sb.append(" AND t.sys_transfer ='"+ComacConstants.ZONAL_CODE+"'");
		sb.append(" AND t.model_series_id ='"+modelSeriesId+"'");
		sb.append(" AND t.source_system ='"+sourceSystem+"') zz");
		if (firstTextField != null && !"".equals(firstTextField)) {
			sb.append(" where zz.ata_code like '%"+firstTextField+"%'");
		}
		return this.executeQueryBySql(sb.toString(), TaskMsg.class);
	}

	@Override
	public List<TaskMsg> getTaskByMrbId(String modelSeriesId, String mrbId) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("mrbId",mrbId));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("validFlag",ComacConstants.YES));
		return this.findByCriteria(dc);
	}

	@Override
	public boolean deleteTasksByTaskId(String taskId) {
		boolean flag = true;
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
		    @SuppressWarnings("deprecation")
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL PRO_DELETE_TASK_MSG_DATA(?)}"); 
		    st.setString(1,taskId); 
		    st.execute(); 
	    }catch(Exception e){ 
	    	flag = false;
	    }finally{ 
		    session.disconnect(); 
		    session.close(); 
	    } 
		return flag;		
	}
	
	public boolean deleteBiaoZhunTasksByTaskId(String msId, String taskId) {
		boolean flag = true;
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
		    @SuppressWarnings("deprecation")
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL PRO_DELETE_BIAOZHUN_TASK(?, ?)}"); 
		    st.setString(1, msId); 
		    st.setString(2, taskId); 
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
	public List<Object[]> getToAreaTaskNoAccept(String msId, String areaId) throws BusinessException{
		String s = "SELECT A.TASK_ID, A.SOURCE_SYSTEM, A.TASK_CODE " +
					"FROM TASK_MSG A, TASK_MSG_DETAIL B " +
					"WHERE A.TASK_ID = B.TASK_ID AND A.MODEL_SERIES_ID = '" + msId + "' " +
					"  AND A.SOURCE_SYSTEM IN ('" + ComacConstants.SYSTEM_CODE + "', '" + ComacConstants.STRUCTURE_CODE + "', '" + ComacConstants.LHIRF_CODE + "') " +
					"  AND A.TASK_CODE IS NOT NULL AND A.NEED_TRANSFER = 1 AND A.SYS_TRANSFER = '" + ComacConstants.ZONAL_CODE + "'" +
					"  AND B.WHERE_TRANSFER = '" + areaId + "' AND B.HAS_ACCEPT IS NULL AND A.VALID_FLAG = 1 AND B.VALID_FLAG = 1";
		return this.executeQueryBySql(s);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getZengQiangAreaTaskNoAccept(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA_4_1"), Restrictions.eq("sourceStep", "ZA_4_2")));
		dc.add(Restrictions.isNull("taskValid"));
		dc.add(Restrictions.eq("anyContent2", "noAnalysis"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getZengQiangAreaTask(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA_4_1"), Restrictions.eq("sourceStep", "ZA_4_2")));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("anyContent1"));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> getAreaStandardTask(String msId, String zaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.or(Restrictions.eq("sourceStep", "ZA5A"), Restrictions.eq("sourceStep", "ZA5B")));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> findAreaTaskMsg(String msId, String zaId, String areaStep) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.eq("sourceStep", areaStep));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> findAreaTaskMsg(String msId, String zaId, String areaStep, String anyContent) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.ZONAL_CODE));
		dc.add(Restrictions.eq("sourceAnaId", zaId));
		dc.add(Restrictions.eq("sourceStep", areaStep));
		dc.add(Restrictions.eq("anyContent1", anyContent));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}

	@Override
	public List<TaskMsg> getTempTaskMsgByS1Id(String s1Id,String sourceStep,String inOrOut) {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		if(sourceStep!=null){
			if(sourceStep.equals("S3")){
				dc.add(Restrictions.eq("sourceStep", sourceStep));
				dc.add(Restrictions.eq("anyContent1", s1Id+"FD"));
			}else if(sourceStep.equals("S6")){
				dc.add(Restrictions.eq("sourceStep", "AD/ED"));
				dc.add(Restrictions.eq("anyContent1", s1Id+"AD/ED"));
			}
		}else{
			dc.add(Restrictions.or(Restrictions.eq("anyContent1", s1Id+"AD/ED"),Restrictions.eq("anyContent1", s1Id+"FD")));
		}
		if(inOrOut!=null){
			if("in".equals(inOrOut)){
				dc.add(Restrictions.or(Restrictions.eq("anyContent3","0"),Restrictions.eq("anyContent3","2")));
			}
			if("out".equals(inOrOut)){
				dc.add(Restrictions.or(Restrictions.eq("anyContent3","1"),Restrictions.eq("anyContent3","2")));
			}
		}
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskMsg> findOneAnaAllTask(String msId, String sourceSystem, String sourceAnaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("sourceSystem", sourceSystem));
		dc.add(Restrictions.eq("sourceAnaId", sourceAnaId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
	}
}
