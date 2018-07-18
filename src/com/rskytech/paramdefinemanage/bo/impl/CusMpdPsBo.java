package com.rskytech.paramdefinemanage.bo.impl;

import java.io.File;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusMpdPsBo;
import com.rskytech.paramdefinemanage.dao.ICusMpdPsDao;
import com.rskytech.pojo.CusMpdPs;

public class CusMpdPsBo extends BaseBO implements ICusMpdPsBo{

	private ICusMpdPsDao cusMpdPsDao;
	

	public ICusMpdPsDao getCusMpdPsDao() {
		return cusMpdPsDao;
	}


	public void setCusMpdPsDao(ICusMpdPsDao cusMpdPsDao) {
		this.cusMpdPsDao = cusMpdPsDao;
	}


	@Override
	public CusMpdPs findById(String id) throws BusinessException {
		return this.getCusMpdPsDao().findById(id);
	}
	
	
	@Override
	public void deletePsFile(String urlString) throws BusinessException {
		if (urlString != null && !urlString.equals("")) {
			String filePath = urlString.substring(urlString.lastIndexOf(ComacConstants.MPD_PS_SAVE_PATH));
			String realPath = ServletActionContext.getServletContext().getRealPath(filePath);
			File file = new File(realPath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusMpdPs> findByModelSeriesId(String id) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(CusMpdPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", id));
		dc.addOrder(Order.asc("psFlg"));
		dc.addOrder(Order.asc("psSort"));
		return this.findByCritera(dc);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isPsFlgUnique(String modelSeriesId, String cusMpdPsId) throws BusinessException {
		DetachedCriteria dc=DetachedCriteria.forClass(CusMpdPs.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		if (cusMpdPsId != null && !"".equals(cusMpdPsId )) {
			dc.add(Restrictions.ne("psId", cusMpdPsId));
		}
		dc.add(Restrictions.eq("psFlg", ComacConstants.PSFLG_0));
		List<CusMpdPs> list = this.findByCritera(dc);
		if (list!= null && list.size()>0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean saveMpdPs(CusMpdPs ps, String operateFlg, String userid) throws BusinessException {
		saveOrUpdate(ps, operateFlg, userid);
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean deleteMpdPsById(Class clazz, String id, String userid) throws BusinessException {
		CusMpdPs ps = findById(id);
		this.deletePsFile(ps.getPsUrl());
		this.delete(clazz, id, userid);
		return true;
	}

	
	
	
}
