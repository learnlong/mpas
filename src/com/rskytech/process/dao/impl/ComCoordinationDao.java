package com.rskytech.process.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComCoordination;
import com.rskytech.process.dao.IComCoordinationDao;

public class ComCoordinationDao extends BaseDAO implements IComCoordinationDao{
	public List<ComCoordination> findCoordinationList(String userId,String modelSeriesId,int flag,Page page)
			throws BusinessException {
		String sql = "select * " +
					"  from COM_COORDINATION cc " +
					" where cc.MODEL_SERIES_ID = '"+modelSeriesId+"' " +
					"   and (cc.SEND_USER = '"+userId+"' or cc.RECEIVE_USER = '"+userId+"') " +
					"   and cc.VALID_FLAG = "+flag+" " +
					"order by cc.is_received";
		return findBySql(sql, page);
	}
	
	public List<ComCoordination> findCoordinationSearchList(String userId,String modelSeriesId,int flag,Page page,String type,String isReceive)
			throws BusinessException {
		String type1="";
		String isReceive1="";
		int num =0;
		if(type!=null&&!"".equals(type)){
		if("STR_TO_Z".equals(type)){
			type = "STR_TO_Z','STR_TO_SYS";
		  }
		 type1 = " co.TYPE in("+"'"+type+"'"+")";
		 num=1;
		}
		if(isReceive !=null&&!"".equals(isReceive)){
			if(num==1||num==2){
				isReceive1 = " and co.IS_RECEIVED ='"+isReceive+"'";
			}else{
				isReceive1 = " co.IS_RECEIVED ='"+isReceive+"'";
			}
			
		}
	  String str = "co where";
	  String sql ="select * from COM_COORDINATION cc where cc.SEND_USER!='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
				" and cc.VALID_FLAG="+flag+" and cc.RECEIVE_USER='"+userId+"' and cc.IS_RECEIVED ='1'"+" union all ";//当前用户为接收人，但不是发送人
      sql+="select * from COM_COORDINATION cc where cc.SEND_USER='"+userId+"' and cc.RECEIVE_USER='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
				" and cc.VALID_FLAG="+flag+"and cc.IS_RECEIVED ='1' union all ";
      sql+="select * from COM_COORDINATION cc where cc.SEND_USER='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
    		  " and cc.VALID_FLAG="+flag+" and cc.IS_RECEIVED ='2' union all ";
      sql+="select * from COM_COORDINATION cc where cc.SEND_USER='"+userId+"' and cc.RECEIVE_USER!='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
				" and cc.VALID_FLAG="+flag+" and cc.IS_RECEIVED !='2' union all ";
      sql+="select * from COM_COORDINATION cc where cc.SEND_USER='"+userId+"' and cc.RECEIVE_USER='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
				" and cc.VALID_FLAG="+flag+"and cc.IS_RECEIVED in('3','4') union all ";
      sql+="select * from COM_COORDINATION cc where cc.SEND_USER!='"+userId+"' and cc.MODEL_SERIES_ID='"+modelSeriesId+"'" +
				" and cc.VALID_FLAG="+flag+" and cc.RECEIVE_USER='"+userId+"' and cc.IS_RECEIVED in('3','4')"; 
      
	String sql1= "select * from ("+sql+") "+str+type1+isReceive1;
		return findBySql(sql1, page);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ComCoordination> findBySql(String str, Page page) {
		Session s = null;
		List list = null;
		try {
			s = this.getHibernateTemplate().getSessionFactory().openSession();
			Query q = s.createSQLQuery(str);
			int start = 0;
			int limit = 20;
			if (page != null) {
				List totalCount = null;
				totalCount = s.createSQLQuery(str).list();
				page.setTotalCount(totalCount.size());
				start = page.getStartIndex();
				limit = page.getPageSize();
			}
			if (limit == 0) {
				limit = 20;
			}
			q = s.createSQLQuery(str).addEntity(ComCoordination.class);
			q.setFirstResult(start);
			q.setMaxResults(limit);
			list = q.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				s.close();
			}
		}
		return list;
	}

	@Override
	public List<ComCoordination> findCoordinationByTaskId(String comTaskId,
			String modelSeriesId, int flag) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(ComCoordination.class);
		dc.add(Restrictions.eq("taskId", comTaskId));
		dc.add(Restrictions.eq("validFlag", flag));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<ComCoordination> findCoordinationById(String comTaskId,
			String comS6OutOrIn, String modelSeriesId, int flag)
			throws BusinessException {

		DetachedCriteria dc = DetachedCriteria.forClass(ComCoordination.class);
		dc.add(Restrictions.eq("taskId", comTaskId));
		if(comS6OutOrIn!=null){
			dc.add(Restrictions.eq("s6OutOrIn", comS6OutOrIn));
		}
		dc.add(Restrictions.eq("validFlag", flag));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		return this.findByCriteria(dc);
	}

}
