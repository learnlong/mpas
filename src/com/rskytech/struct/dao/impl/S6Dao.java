package com.rskytech.struct.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.S5;
import com.rskytech.pojo.S6;
import com.rskytech.pojo.S6Ea;
import com.rskytech.pojo.Sy;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.struct.dao.IS6Dao;
@SuppressWarnings("unchecked")
public class S6Dao extends BaseDAO implements IS6Dao {
	/**
	 * 得到S6表的所有数据
	 * 
	 * @param ssiId
	 *            ssiid
	 * @return 数据list
	 * @throws BusinessException
	 */
	@Override
	public List getS6EaRecords(String ssiId, String region) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.EA_ID,");
		sql.append(" B.SSI_FORM,");
		sql.append(" CASE  WHEN B.IS_METAL = '0' THEN  A.EDR  ELSE  NULL END AS EDRFALSE,");
		sql.append(" CASE  WHEN B.IS_METAL = '0' THEN  A.ADR  ELSE  NULL END AS ADRFALSE,");
		sql.append(" CASE  WHEN B.IS_METAL = '0' THEN  A.INTERVAL  ELSE  NULL  END AS INTERVALFALSE,");
		sql.append(" CASE  WHEN B.IS_METAL = '0' THEN  A.EA_TYPE  ELSE  NULL  END AS EA_TYPEFALSE,");
		sql.append(" CASE  WHEN B.IS_METAL = '1' THEN  A.EDR  ELSE  NULL END AS EDRTRUE,");
		sql.append(" CASE  WHEN B.IS_METAL = '1' THEN  A.ADR  ELSE  NULL END AS ADRTRUE,");
		sql.append(" CASE  WHEN B.IS_METAL = '1' THEN  A.INTERVAL  ELSE  NULL  END AS INTERVALTRUE,");
		sql.append(" CASE  WHEN B.IS_METAL = '1' THEN  A.EA_TYPE  ELSE  NULL  END AS EA_TYPETRUE, ");
		sql.append(" B.S1_ID,");
		sql.append(" B.IS_METAL,B.OWN_AREA,");
		sql.append(" S.EFFECTIVENESS,");
		sql.append(" C.IN_OR_OUT");
		sql.append(" FROM S_6_EA A");
		sql.append(" JOIN S_6 C ON A.S6_ID=C.S6_ID");
		sql.append(" RIGHT JOIN S_1 B");
		sql.append(" ON A.S1_ID = B.S1_ID");
		sql.append(" LEFT JOIN S_MAIN S");
		sql.append(" ON S.SSI_ID = B.SSI_ID");
		sql.append(" WHERE B.SSI_ID ='" + ssiId + "'");
		if ("in".equals(region)) {
			sql.append(" AND B.INTERNAL=1");
			sql.append(" AND C.IN_OR_OUT='IN'");
		}
		if ("out".equals(region)) {
			sql.append(" AND B.OUTERNAL=1");
			sql.append(" AND C.IN_OR_OUT='OUT'");
		}
		//sql.append(" order by B.SSI_FORM asc");
		sql.append(" order by b.s1_id asc");
		return this.executeQueryBySql(sql.toString());
	}
	
	@Override
	public List<S6Ea> getS6EaByS1Id(String inorOut, String i) throws BusinessException {
		String hql = "";
		if ((ComacConstants.INNER).equals(inorOut)) {
			hql = "SELECT A.* FROM  S_6_EA A JOIN S_6 B ON A.S6_ID=B.S6_ID AND B.IN_OR_OUT='IN' WHERE A.S1_ID='" + i + "'";
		}
		if ((ComacConstants.OUTTER).equals(inorOut)) {
			hql = "SELECT A.* FROM  S_6_EA A JOIN S_6 B ON A.S6_ID=B.S6_ID AND B.IN_OR_OUT='OUT' WHERE A.S1_ID='" + i + "'";
		}
		return this.executeQueryBySql(hql,S6Ea.class);
	}
	
	@Override
	public List searchSsi(String ssiId, String str) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TASK_CODE, COUNT(TASK_CODE)");
		sb.append("  FROM TASK_MSG");
		sb.append(" WHERE VALID_FLAG = " + ComacConstants.VALIDFLAG_YES);
		sb.append(" AND (ANY_CONTENT3 ='" + str + "' OR ANY_CONTENT3 ='2')");
		sb.append("   AND (TASK_VALID IS NULL OR TASK_VALID <>3)");
		sb.append("  AND TASK_MSG.SOURCE_ANA_ID='" + ssiId + "'");
		sb.append(" GROUP BY TASK_CODE");
		return this.executeQueryBySql(sb.toString());
	}
	
		/**
		 * 查询Cus_Interval表中属于S6机型分析区域的评级数据;
		 * @return 自定义评级表List
		 * @throws BusinessException
		 * @author wangyueli
		 * createdate 2012-08-03
		 */
		@SuppressWarnings("unchecked")
		@Override
		public List getCusInList(String inOrOut,int miniValue,String modelSeriesId) throws BusinessException {
			String sql="SELECT A.INTERVAL_LEVEL, A.INTERVAL_VALUE"+
					   " FROM CUS_INTERVAL A WHERE A.ANA_FLG ='S6' AND A.INTERNAL_FLG = '" + inOrOut +
					   "' AND A.MODEL_SERIES_ID= '" + modelSeriesId + "' "+
					   " AND A.INTERVAL_LEVEL= "+miniValue;
			return executeQueryBySql(sql);
		}

		@Override
		public List<Sy> getSyRecords(String id,String region) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(Sy.class);
			dc.add(Restrictions.eq("s1.s1Id",id));
			if ("in".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","IN"));
			}
			if ("out".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","OUT"));
			}
			return this.findByCriteria(dc);
		}
		
		@Override
		public List<S4> getS4Records(String id,String region) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(S4.class);
			dc.add(Restrictions.eq("s1.s1Id",id));
			if ("in".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","IN"));
			}
			if ("out".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","OUT"));
			}
			return this.findByCriteria(dc);
		}

		@Override
		public List<S5> getS5Records(String id,String region) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(S5.class);
			dc.add(Restrictions.eq("s1.s1Id",id));
			if ("in".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","IN"));
			}
			if ("out".equals(region)) {
				dc.add(Restrictions.eq("inOrOut","OUT"));
			}
			return this.findByCriteria(dc);
		}
		
		/**
		 * 得到临时任务数据符合要求的
		 * @param modelId 飞机ID
		 * @param sourceSystem 所在系统区域
		 * @param taskValid 数据的有效性
		 * @param region 数据所属内或者外
		 * @return
		 * @throws BusinessException
		 */
		@Override
		public List<TaskMsg> getOtherRecords(String ssiId,String modelId, String sourceSystem,Integer taskValid,String region)
				throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(TaskMsg.class);
			dc.add(Restrictions.eq("sourceAnaId",ssiId));
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelId));
			dc.add(Restrictions.eq("sourceSystem",sourceSystem));
			dc.add(Restrictions.eq("validFlag",ComacConstants.VALIDFLAG_YES));
			if(taskValid!=null){
				dc.add(Restrictions.eq("taskValid",taskValid));
			}else{
				dc.add(Restrictions.or(Restrictions.eq("taskValid",2),Restrictions.isNull("taskValid")));
			}
			if("in".equals(region)){
				dc.add(Restrictions.or(Restrictions.eq("anyContent3","0"),Restrictions.eq("anyContent3","2")));
			}
			if("out".equals(region)){
				dc.add(Restrictions.or(Restrictions.eq("anyContent3","1"),Restrictions.eq("anyContent3","2")));
			}
			dc.addOrder(Order.asc("taskCode"));
			return this.findByCriteria(dc);
		}
		@Override
		public List<S6> getS6Records(String ssiId,String inorOut) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(S6.class);
			dc.add(Restrictions.eq("SMain.ssiId",ssiId));
			if ("IN".equals(inorOut)) {
				dc.add(Restrictions.eq("inOrOut", ComacConstants.INNER));
			}
			if("OUT".equals(inorOut)){
				dc.add(Restrictions.eq("inOrOut", ComacConstants.OUTTER));
			}
			return this.findByCriteria(dc);
		}


		@Override
		public List<S6> getS6BySsiId(String ssiId,String inOrOut) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(S6.class);
			dc.add(Restrictions.eq("SMain.ssiId",ssiId));
			if(inOrOut!=null){
				dc.add(Restrictions.eq("inOrOut",inOrOut));
			}
			return this.findByCriteria(dc);
		}

		@Override
		public List<CusInterval> getIntervalRecords(String inOrOut, String modelId)
				throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(CusInterval.class);
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelId));
			dc.add(Restrictions.eq("internalFlg",inOrOut));
			return this.findByCriteria(dc);
		}

		@Override
		public List<S1> getS1IdBySssiId(String ssiId,String inOrOut) throws BusinessException {
			DetachedCriteria dc=DetachedCriteria.forClass(S1.class);
			dc.add(Restrictions.eq("SMain.id",ssiId));
			if(ComacConstants.INNER.equals(inOrOut)){
				dc.add(Restrictions.eq("internal",1));
			}
			if(ComacConstants.OUTTER.equals(inOrOut)){
				dc.add(Restrictions.eq("outernal",1));
			}
			return this.findByCriteria(dc);
		}

		@Override
		public List<TaskMsg> searchTempTaskCode(String taskCode,String modelId, String region) {
			DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
			dc.add(Restrictions.eq("anyContent2", taskCode));
			dc.add(Restrictions.eq("comModelSeries.id", modelId));
			dc.add(Restrictions.eq("sourceSystem",ComacConstants.STRUCTURE_CODE));
			if(region!=null){
				if("in".equals(region)){
					dc.add(Restrictions.eq("anyContent3","0"));
				}
				if("out".equals(region)){
					dc.add(Restrictions.eq("anyContent3","1"));
				}
				dc.add(Restrictions.eq("taskValid", 3));
			}
			return this.findByCriteria(dc);
		}
}
