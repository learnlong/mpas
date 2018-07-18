package com.rskytech.basedata.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComAtaDao;
import com.rskytech.pojo.ComAta;

public class ComAtaDao extends BaseDAO implements IComAtaDao{

	@SuppressWarnings("unchecked")
	public List<ComAta> loadChildAta(String msId, String ataId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (ataId != null && !"0".equals(ataId)) {
			dc.add(Restrictions.eq("comAta.ataId", ataId));// 查询子ATA
		} else {
			dc.add(Restrictions.isNull("comAta.ataId"));// 查询根ATA
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("ataCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<ComAta> loadChildAta(String msId, String ataId, Page page) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (ataId != null && !"0".equals(ataId)) {
			dc.add(Restrictions.eq("comAta.ataId", ataId));// 查询子ATA
		} else {
			dc.add(Restrictions.isNull("comAta.ataId"));// 查询根ATA
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("ataCode"));
		return this.findByCriteria(dc, page).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkAta(String msId, String ataId, String ataCode) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("ataCode", ataCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComAta> list = this.findByCriteria(dc);
		
		if (list == null || list.size() == 0){
			return false;
		} else if (ataId == null || "".equals(ataId)){
			return true;
		} else {
			ComAta ata = list.get(0);
			if (ata.getAtaId().equals(ataId)){
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getSelfAndChildAta(String msId, String ataId){
		String s = "select a.ata_id from com_ata a where a.model_series_id = '" + msId + "' and a.valid_flag = 1" +
				" start with a.ata_id = '" + ataId + "' connect by prior a.ata_id = a.parent_ata_id";
		return this.executeQueryBySql(s);
	}
	
	public String getAtaIsHaveMSI(String ataCode,String msId){
		StringBuilder s = new StringBuilder();
		s.append(" select m.msi_id from M_MAIN m,m_set t where m.msi_id=t.msi_id");
		s.append(" and m.valid_flag=1 and m.status<>'NEW'");
		s.append(" and m.model_series_id='"+msId+"'");
		s.append(" and t.function_code like '"+ataCode+"%'");
		List<Object> objList=this.executeQueryBySql(s.toString());
		if(objList==null||objList.size()<1){
			return "0";
		}else{
			return "1";
		}
	}
	
	public void deleteAta(String ataId){
		String s = "delete com_ata where ata_id = '" + ataId + "'";
		this.executeBySql(s);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComAta> getComAtaByAtaCode(String ataCode, String msId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("ataCode", ataCode));
		dc.add(Restrictions.eq("comModelSeries.id", msId));
		return this.findByCriteria(dc);
	}
	
	public HashMap<String, String> importAta(String msId, String biaoshi) {
		HashMap<String, String> map = new HashMap<String, String>();
	    Session session = getHibernateTemplate().getSessionFactory().openSession(); 
	    try{ 
		    @SuppressWarnings("deprecation")
			Connection conn = session.connection(); 
		    CallableStatement st = conn.prepareCall("{CALL PRO_INSERT_ATA(?, ?, ?, ?)}"); 
		    st.setString(1, msId); 
		    st.setString(2, biaoshi); 
		    st.registerOutParameter(3, Types.VARCHAR);
			st.registerOutParameter(4, Types.VARCHAR);
		    st.execute(); 
		    map.put("res", st.getString(3));
			map.put("msg", st.getString(4));
	    }catch(Exception e){ 
	    	map.put("res", "导入失败");
	    }finally{ 
		    session.disconnect(); 
		    session.close(); 
	    } 
		return map;		
	}

	@Override
	public List<ComAta> getAtaIdByAtaCode(String ataCode) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComAta.class);
		dc.add(Restrictions.eq("ataCode", ataCode));
		return this.findByCriteria(dc);
	}
}
