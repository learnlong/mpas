package com.rskytech.basedata.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComModelSeriesDao;
import com.rskytech.pojo.ComModelSeries;

public class ComModelSeriesDao extends BaseDAO implements IComModelSeriesDao {

	@SuppressWarnings("unchecked")
	public List<ComModelSeries> getComModelSeriesList(String msCode, String msName, Page page) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComModelSeries.class);		
		if (msCode != null && !"".equals(msCode)) {
			dc.add(Restrictions.like("modelSeriesCode", msCode, MatchMode.ANYWHERE));
		}
		if (msName != null && !"".equals(msName)) {
			dc.add(Restrictions.like("modelSeriesName", msName, MatchMode.ANYWHERE));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("modelSeriesCode"));
		return this.findByCriteria(dc, page).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ComModelSeries> getModelSeriesByCodeNotNow(String msCode, String msId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComModelSeries.class);
		dc.add(Restrictions.eq("modelSeriesCode", msCode));
		if (msId != null){
			dc.add(Restrictions.ne("modelSeriesId", msId));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<ComModelSeries> getDefaultMs() throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComModelSeries.class);
		dc.add(Restrictions.eq("defaultModelSeries", 1));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
	
	public boolean deleteModelSeries(String modelSeriesId) throws BusinessException{
		boolean flag = true;
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
		    @SuppressWarnings("deprecation")
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL pro_delete_modelseries_data(?)}"); 
		    st.setString(1, modelSeriesId); 
		    st.execute(); 
	    }catch(Exception e){ 
	    	flag = false;
	    }finally{ 
		    session.disconnect(); 
		    session.close(); 
	    } 
		return flag;
	}
	
	public boolean copyDefaultCustomData(String msId, String userId) {
		boolean flag = true;
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
		    @SuppressWarnings("deprecation")
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL PRO_COPY_DEFAULT_DATA(?, ?)}"); 
		    st.setString(1, msId); 
		    st.setString(2, userId); 
		    st.execute(); 
	    }catch(Exception e){ 
	    	flag = false;
	    }finally{ 
		    session.disconnect(); 
		    session.close(); 
	    } 
		return flag;		
	}
}
