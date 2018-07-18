package com.rskytech.basedata.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComAreaDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;

public class ComAreaDao extends BaseDAO implements IComAreaDao {

	@SuppressWarnings("unchecked")
	public List<ComArea> loadChildArea(String msId, String areaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (areaId != null && !"0".equals(areaId)) {
			dc.add(Restrictions.eq("comArea.areaId", areaId));// 查询子区域
		} else {
			dc.add(Restrictions.isNull("comArea.areaId"));// 查询根区域
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("areaCode"));
		return this.findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<ComArea> loadChildArea(String msId, String areaId, Page page) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		if (areaId != null && !"0".equals(areaId)) {
			dc.add(Restrictions.eq("comArea.areaId", areaId));// 查询子区域
		} else {
			dc.add(Restrictions.isNull("comArea.areaId"));// 查询根区域
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("areaCode"));
		return this.findByCriteria(dc, page).getResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<ComAreaDetail> loadAreaDetail(String areaId) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComAreaDetail.class);
		dc.add(Restrictions.eq("comArea.areaId", areaId));
		dc.addOrder(Order.asc("equipmentName"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 使用多个字符串查询多条区域记录(TaskMpdBo使用)
	 * @param ids
	 * @return
	 * @throws DAOException
	 */
	public List getAreasByIds(String ids) throws DAOException {
		String hql = "from ComArea a where a.areaId in ("+ids+") and a.validFlag="+ComacConstants.VALIDFLAG_YES;
		return this.findByHql(hql, null);
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean checkArea(String msId, String areaId, String areaCode) throws BusinessException{
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", msId));
		dc.add(Restrictions.eq("areaCode", areaCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		List<ComArea> list = this.findByCriteria(dc);
		
		if (list == null || list.size() == 0){
			return false;
		} else if (areaId == null || "".equals(areaId)){
			return true;
		} else {
			ComArea area = list.get(0);
			if (area.getAreaId().equals(areaId)){
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getSelfAndChildArea(String msId, String areaId){
		String s = "select a.area_id from com_area a where a.model_series_id = '" + msId + "' and a.valid_flag = 1" +
				" start with a.area_id = '" + areaId + "' connect by prior a.area_id = a.parent_area_id";
		return this.executeQueryBySql(s);
	}
}
