package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IMrbVersionBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.TaskMpdVersion;

public class MrbVersionBo extends BaseBO implements IMrbVersionBo{

	/**
	 * 根据机型版本类型有效标志对表TaskMpdVersion进行查询
	 */
	@SuppressWarnings("unchecked")
	public List<TaskMpdVersion> getMRBList(String modelSeriesId, Page page)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpdVersion.class);
		dc.add(Restrictions.eq("versionType",
				ComacConstants.TASK_MPD_VERSION_TYPE_MRB));
		dc.add(Restrictions.or(Restrictions.eq("validFlag",
						ComacConstants.VALIDFLAG_YES), Restrictions.eq(
						"validFlag", ComacConstants.VALIDFLAG_TWO)));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.addOrder(Order.desc("createDate"));
		if(page == null){
			return this.findByCritera(dc);
		}
		return this.findByCritera(dc, page).getResult();
	}

	/**
	 * 根据机型 对表TaskMpdVersion进行查询
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TaskMpdVersion getTaskMrbVersion(String modelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMpdVersion.class);
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.add(Restrictions.eq("versionType", ComacConstants.MRB));
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		List<TaskMpdVersion> list = this.findByCritera(dc);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 保存数据
	 */
	@Override
	public void saveMRBVersion(ComUser comUser,ComModelSeries comModelSeries, String verId,
			String versionNo, String versionDescCn, String versionDescEn)
			throws BusinessException {
		TaskMpdVersion mrb = null;
		String dbOperate = "";// db操作区分
		if (!BasicTypeUtils.isNullorBlank(verId)) {// 修改操作时
			dbOperate = ComacConstants.DB_UPDATE;
			mrb = (TaskMpdVersion) this.loadById(TaskMpdVersion.class, verId);
		} else {// 追加操作时
			dbOperate = ComacConstants.DB_INSERT;
			String modelSeriesId = comModelSeries
					.getModelSeriesId();
			mrb = new TaskMpdVersion();
			mrb.setComModelSeries(comModelSeries);
			TaskMpdVersion taskMpdVersion = this
					.getTaskMrbVersion(modelSeriesId);
			if (taskMpdVersion != null) {
				taskMpdVersion.setValidFlag(ComacConstants.OLD_VERSION);
				this.update(taskMpdVersion, comUser.getUserId());
			}
		}
		mrb.setVersionNo(versionNo);
		mrb.setVersionDescCn(versionDescCn);
		mrb.setVersionDescEn(versionDescEn);
		mrb.setValidFlag(ComacConstants.NEW_VERSION);
		mrb.setVersionType(ComacConstants.MRB);
		this.saveOrUpdate(mrb, dbOperate, comUser.getUserId());
	}
	
	
}
