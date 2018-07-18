package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.pojo.M2;
import com.rskytech.pojo.M4;
import com.rskytech.sys.dao.IM4Dao;

public class M4Dao extends BaseDAO implements IM4Dao {

	@Override
	public List<M2> getListM2ByTaskId(String taskId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M2.class);
		dc.createAlias("m13F", "m13F");
		dc.createAlias("m13F.m13Cs", "m13C");
		dc.createAlias("m13C.m3s", "m3");
		Criterion c1 = Restrictions.or(Restrictions.eq("m3.baoyangTaskId",
				taskId), Restrictions.eq("m3.jianyanTaskId", taskId));
		Criterion c2 = Restrictions.or(Restrictions
				.eq("m3.jiankongTaskId", taskId), Restrictions.eq(
				"m3.jianchaTaskId", taskId));
		Criterion c3 = Restrictions.or(Restrictions.eq("m3.zongheTaskId",taskId),Restrictions.or(
								Restrictions.eq("m3.chaixiuTaskId", taskId), 
								Restrictions.eq("m3.baofeiTaskId", taskId)));
		Criterion c12 = Restrictions.or(c1, c2);
		Criterion c123 = Restrictions.or(c3, c12);
		dc.add(c123);
		List<M2> list = this.findByCriteria(dc);
		
		DetachedCriteria dc1 = DetachedCriteria.forClass(M2.class);
		dc1.createAlias("m13F", "m13F");
		dc1.createAlias("m13F.m13Cs", "m13C");
		dc1.createAlias("m13C.m3s", "m3");
		dc1.createAlias("m3.m3Additionals", "m3Additional");
		dc1.add(Restrictions.eq("m3Additional.addTaskId", taskId));
		List<M2> list1 = this.findByCriteria(dc1);
		list.addAll(list1);
		return list;
	}

	@Override
	public List<M4> getListM4ByMsiId(String msiId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M4.class);
		dc.add(Restrictions.eq("MMain.id", msiId));
		return this.findByCriteria(dc);
	}

	@Override
	public List<M4> getM4ByTaskId(String taskId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(M4.class);
		dc.add(Restrictions.eq("taskId", taskId));
		return this.findByCriteria(dc);
	}

	@Override
	public void deleteM4ByTaskId(String taskId) {
		String sql = "delete from M_4 t WHERE t.task_id='"+taskId+"'";
		this.executeBySql(sql);
	}

}
