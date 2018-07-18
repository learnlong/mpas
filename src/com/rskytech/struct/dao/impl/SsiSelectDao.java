package com.rskytech.struct.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.SMain;
import com.rskytech.struct.dao.ISsiSelectDao;

@SuppressWarnings("unchecked")
public class SsiSelectDao extends BaseDAO implements ISsiSelectDao {

	@Override
	public List<SMain> getSMainByAtaId(String ataId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(SMain.class);
		dc.add(Restrictions.eq("comAta.id",ataId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
	
	public List<SMain> getSelfSMainByAtaId(String ataId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(SMain.class);
		dc.add(Restrictions.eq("parentAtaId",ataId));
		dc.add(Restrictions.isNull("comAta.id"));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}

	@Override
	public List<Object[]> getSsiList(String ataId, String modelSeriesId,String userId) {
		StringBuffer sb1 = new StringBuffer();
//		List<Object[]> listAll= new ArrayList<Object[]>();
		sb1.append(" SELECT s.ssi_id,s.add_code ata_code, s.add_name ata_name, s.is_ssi, s.is_ana,s.is_add,s.add_user,s.ata_id,'1'" +
					" from s_main s WHERE s.ata_id is null and" +
					" s.parent_ata_id='"+ataId+"' and s.valid_flag=1 and s.model_series_id='"+modelSeriesId+"'");
		/*	List<Object[]> list1 = this.executeQueryBySql(sb1.toString());
		if(list1!=null){
			listAll.addAll(list1);
		}*/
		String sqlFuc = " fun_is_owner_auth('"+modelSeriesId+"','"+ComacConstants.STRUCTURE_CODE+"'," +
				"aa.ata_id,'"+userId+"','"+ComacConstants.POSITION_ID_PROFESSION_ANAYIST+"') flag";
		String sqla ="SELECT a.ata_id,a.ata_code,a.ata_name FROM Com_Ata a"
			       +" where a.parent_ata_id = '"+ataId+"'"
			       +" UNION ALL SELECT c.ata_id,c.ata_code,c.ata_name "
			       +" FROM Com_Ata c WHERE c.ata_id = '"+ataId+"' and c.valid_flag=1";  //查询当前ata及其子ata
		String sql ="select * from (select * from (SELECT s.ssi_id, aa.ata_code, aa.ata_name, s.is_ssi, " 
				+"s.is_ana,s.IS_ADD,s.ADD_USER,"
				+"case when s.ata_id is null then aa.ata_id else s.ata_id end as ata_id,"+sqlFuc
				+" FROM ("+sqla+") aa LEFT JOIN s_main s ON s.ata_id = aa.ata_id ) x "
				+" where x.flag='1' union all "
				+sb1.toString() + ") zz order by zz.ata_code asc";	
	/*	List<Object[]> list3 = this.executeQueryBySql(sql);
		if(list3!=null){
			listAll.addAll(list3);
		}*/
		return this.executeQueryBySql(sql);
	}
	
	@Override
	public List searchAnalysisProByAtaId(String ataId,String modelSeriesId,String userId) {
		String sql = "select fun_is_owner_auth('"+modelSeriesId+"','"+ComacConstants.STRUCTURE_CODE+"'," +
				"'"+ataId+"','"+userId+"','"+ComacConstants.POSITION_ID_PROFESSION_ANAYIST+"') flag from dual";
		return this.executeQueryBySql(sql);
	}

	@Override
	public List SearchSsiListAndAtaListByAtaCode(String SsiCode,
			String modelSerierId) throws BusinessException {
		String sql = "select s.ssi_id from s_main s where s.add_code='"+SsiCode+"' " +
					"and s.model_series_id='"+modelSerierId+"'";
				sql+=" union all select c.ata_id from Com_Ata c where  c.ata_code='"+SsiCode+"' "+
					 "and c.model_series_id='"+modelSerierId+"'";
		return this.executeQueryBySql(sql);
	}

	@Override
	public List<Object[]> getSsiRecords(Integer isSsi, Integer isOwn, String ssiName,
			String model,int start,int limit) throws BusinessException {
		int end = start+limit;
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * FROM (SELECT a.*,ROWNUM rn FROM (select * from(SELECT  s.ssi_id,");
		sb.append("CASE WHEN s.add_code IS NULL THEN c.ata_code ELSE s.add_code end ssiCode,");
		sb.append("CASE WHEN s.add_name IS NULL THEN c.ata_name ELSE s.add_name end ssiName,");
		sb.append("s.model_series_id,s.is_ssi,s.is_ana,s.is_add");
		sb.append(" FROM s_main s LEFT JOIN Com_Ata c ON s.ata_id = c.ata_id where s.valid_flag="+ComacConstants.YES+") aa");
		sb.append(" where 1=1");
		if(!"all".equals(model)){
			sb.append(" and aa.model_series_id ='"+model+"'");
		}
		if (isSsi!=null&&isSsi != 2) {
			sb.append(" and aa.is_ssi ="+isSsi+"");
		}
		if (isOwn!=null&&isOwn == 1 ) {
			sb.append(" and aa.is_add ="+isOwn+"");
		}else if(isOwn!=null&&isOwn == 0){
			sb.append(" and aa.is_add is null");
		}
		if (ssiName != null &&! "".equals(ssiName)) {
			sb.append(" and aa.ssiName like '%"+ssiName+"%'");
		}
		sb.append(") a WHERE ROWNUM <="+end+") bb WHERE bb.rn>="+start+"");
		return this.executeQueryBySql(sb.toString());
	}

	@Override
	public List<Object> getSsiTotalRecords(Integer isSsi,Integer isOwn,String ssiName,String model) throws BusinessException {
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) total from (select * from(SELECT  s.ssi_id,");
		sb.append("CASE WHEN s.add_code IS NULL THEN c.ata_code ELSE s.add_code end ssiCode,");
		sb.append("CASE WHEN s.add_name IS NULL THEN c.ata_name ELSE s.add_name end ssiName,");
		sb.append("s.model_series_id,s.is_ssi,s.is_ana,s.is_add");
		sb.append(" FROM s_main s LEFT JOIN Com_Ata c ON s.ata_id = c.ata_id where s.valid_flag="+ComacConstants.YES+") aa");
		sb.append(" where 1=1");
		if(!"all".equals(model)){
			sb.append(" and aa.model_series_id ='"+model+"'");
		}
		if (isSsi!=null&&isSsi != 2) {
			sb.append(" and aa.is_ssi ="+isSsi+"");
		}
		if (isOwn!=null&&isOwn == 1 ) {
			sb.append(" and aa.is_add ="+isOwn+"");
		}else if(isOwn!=null&&isOwn == 0){
			sb.append(" and aa.is_add is null");
		}
		if (ssiName != null &&! "".equals(ssiName)) {
			sb.append(" and aa.ssiName like '%"+ssiName+"%'");
		}
		sb.append(")");
		return this.executeQueryBySql(sb.toString());
	}

	@Override
	public void deleteUnSsiAnalysis(String ssiId,String modelSeriesId) {
		String sql = "delete from task_msg t where t.source_system = '"
					+ComacConstants.STRUCTURE_CODE+"' and t.source_step='UNSSI'" +
					" and t.model_series_id='"+modelSeriesId+"' and t.source_ana_id='"+ssiId+"'";
		this.executeBySql(sql);
	}
	
}
