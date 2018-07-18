package com.rskytech.sys.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.TaskMsg;
import com.rskytech.sys.dao.IM5Dao;
public class M5Dao  extends BaseDAO implements IM5Dao{
	/**
	 * 根据任务Id查询所属的原因编号及故障影响类型
	 * @param taskId
	 * @return
	 * @author chendexu
	 * createdate 2012-08-15
	 */
	@Override
	public List getCauseCodeAndCauseTypeByTaskId(String taskId)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT C.CAUSE_CODE, A.FAILURE_CAUSE_TYPE");
		sql.append(" FROM M_2 A,M_1_3_F F,M_1_3_C C");
		sql.append(" WHERE A.M13F_ID = F.M13F_ID AND C.M13F_ID = F.M13F_ID");
		sql.append(" AND C.M13C_ID IN(SELECT M13C_ID FROM  M_3");
		sql.append(" WHERE JIANYAN_TASK_ID= '"+ taskId+"' OR JIANKONG_TASK_ID ='"+taskId+"'");
		sql.append(" OR JIANCHA_TASK_ID ='"+ taskId+ "' OR CHAIXIU_TASK_ID ='"+taskId+"' OR BAOYANG_TASK_ID ='"+taskId+"'");
		sql.append(" OR BAOFEI_TASK_ID ='"+taskId+"' OR ZONGHE_TASK_ID ='"+taskId+ "' OR");
		sql.append(" M3_ID IN(SELECT A.M3_ID FROM  m_3 A, M_3_ADDITIONAL B");
		sql.append(" WHERE  A.M3_ID= B.M3_ID AND B.Add_Task_Id ='"+taskId+"'))");
		sql.append(" ORDER BY C.CAUSE_CODE");
		return this.executeQueryBySql(sql.toString());
	}

	@Override
	public List<TaskMsg> searchM5HeBing(String sourceAnaId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(TaskMsg.class);
		dc.add(Restrictions.eq("sourceAnaId", sourceAnaId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.add(Restrictions.eq("sourceSystem", ComacConstants.SYSTEM_CODE));
		dc.add(Restrictions.eq("taskValid", ComacConstants.YES));
		dc.addOrder(Order.asc("taskCode"));
		return this.findByCriteria(dc);
		
	}

}
