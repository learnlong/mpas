package com.rskytech.sys.bo.impl;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.basedata.dao.IComVendorDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ComVendor;
import com.rskytech.pojo.M11;
import com.rskytech.pojo.MStep;
import com.rskytech.sys.bo.IM11Bo;
import com.rskytech.sys.bo.IMStepBo;
import com.rskytech.sys.dao.IM11Dao;

public class M11Bo extends BaseBO implements IM11Bo {

	IMStepBo mstepBo;
	private IComVendorDao comVendorDao;
	private IM11Dao m11Dao;

	/**
	 * 根据MSI查询M11
	 * 
	 * @param msiId
	 * @return
	 * @author chendexu createdate 2012-08-21
	 */

	public M11 getM11ByMsiId(String msiId) throws BusinessException {
		List<M11> list = m11Dao.getM11ByMsiId(msiId);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 保存m11的数据,保存logdb的日志,保存logoperate
	 * 
	 * @parm m11对象
	 * @parm user当前用户
	 * @param operateFlg操作类型
	 * @param pageId
	 *            页面
	 * @param msiId
	 *         
	 */
	public void saveM11(M11 m11, ComUser user, String operateFlg,
			String pageId, String msiId ,ComModelSeries comModelSeries) {
		saveOrUpdate(m11, operateFlg, user.getUserId());
		this.saveComLogOperate(user, pageId, ComacConstants.SYSTEM_CODE);
		MStep mstep = mstepBo.getMStepByMsiId(msiId);
		if (mstep.getM12().equals(ComacConstants.STEP_NO)) {
			mstep.setM11(ComacConstants.STEP_FINISH);
			List<ComVendor> venList = comVendorDao.loadVendorList(comModelSeries.getModelSeriesId());
			if (venList.size() > 0) {
				mstep.setM12(ComacConstants.STEP_NOW);
			}
			this.mstepBo.saveOrUpdate(mstep, ComacConstants.DB_UPDATE, user
					.getUserId());
		}
	}

	public IMStepBo getMstepBo() {
		return mstepBo;
	}

	public void setMstepBo(IMStepBo mstepBo) {
		this.mstepBo = mstepBo;
	}

	public IM11Dao getM11Dao() {
		return m11Dao;
	}

	public void setM11Dao(IM11Dao m11Dao) {
		this.m11Dao = m11Dao;
	}

	public IComVendorDao getComVendorDao() {
		return comVendorDao;
	}

	public void setComVendorDao(IComVendorDao comVendorDao) {
		this.comVendorDao = comVendorDao;
	}

}
