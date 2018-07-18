package com.rskytech.sys.dao.impl;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.MSelect;
import com.rskytech.sys.dao.IMsiSelectDao;

@SuppressWarnings("unchecked")
public class MsiSelectDao extends BaseDAO implements IMsiSelectDao{

	@Override
	public List<MSelect> getListMSelectByModelSeriesId(String modelSeriesId,String name,String isNoMsi) throws BusinessException {
		String sql = "select * from M_SELECT m join Com_Model_Series c "
				   + "on m.model_series_id=c.model_series_id " 
				   + "join COM_ATA ca on ca.ATA_ID=m.ATA_ID ";
			   sql +="where m.model_series_id='"+modelSeriesId+"' ";
		if (!BasicTypeUtils.isNullorBlank(name)&&!"null".equals(name)) {
			sql +="and ca.ATA_NAME like '%"+name+"%' ";
		}
		if (BasicTypeUtils.isNumberString(isNoMsi)) {
			sql += "and m.IS_MSI="+isNoMsi;
			}
		return this.executeQueryBySql(sql,MSelect.class);
	}

	@Override
	public List<MSelect> getMSelectByataId(String ataId,String comModelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(MSelect.class);
		dc.add(Restrictions.eq("comAta.id", ataId));
		dc.add(Restrictions.eq("comModelSeries.id", comModelSeriesId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}

	public List<Object[]> findAllAtaByAtaId(String ataId,String modelSeriesId,String userId){
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (select aa.* ,fun_is_owner_auth('"+modelSeriesId+"','"+ComacConstants.SYSTEM_CODE+"'," +
					"aa.ata_id,'"+userId+"','"+ComacConstants.POSITION_ID_PROFESSION_ANAYIST+"') flag from (");
		sb.append("SELECT c.ata_id, c.ata_code,c.ata_name, c.ata_level FROM com_ata c ");
		sb.append("start with c.ata_id='"+ataId+"' connect   by   PRIOR c.ata_id=c.parent_ata_id ");
		sb.append(") aa) ab where ab.flag='1' order by ab.ata_code");
		/*sb.append(" UNION ALL (select a.ata_id,a.ata_code,a.ata_name,a.ata_level" +
					" from com_ata a where a.parent_ata_id='"+ataId+"')) aa) ab where ab.flag='1'");*/
			return this.executeQueryBySql(sb.toString());
	}

	@Override
	public ComAta getSupAtaByAta(String ataId, String comModelSeriesId) {
		String sql = "select * from COM_ATA t where t.ata_id in" +
				"(select ata.parent_ata_id from COM_ATA ata where ata.ata_id='"+ataId+"' and ata.model_series_id='"+comModelSeriesId+"' and ata.valid_flag = 1) and t.valid_flag = 1";

	   List<Object> li=this.executeQueryBySql(sql,ComAta.class);
	   if (li.size()>0) {
		   return (ComAta) li.get(0);
	   }
		return null;
	}

	@Override
	public List<Object[]> getHighLevelByAta(String ataId,
			String comModelSeriesId) {
		String sql = "select ata.ata_code,t.highest_level from M_SELECT t,com_ata ata where " +
				"t.ata_id='"+ataId+"' and t.model_series_id='"+comModelSeriesId+"' and ata.ata_id=t.ata_id and t.highest_level=ata.ata_code and ata.valid_flag = 1 and t.valid_flag = 1 and t.is_msi = 1";

		return this.executeQueryBySql(sql);

	}



	

}
