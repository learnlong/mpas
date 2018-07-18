package com.rskytech.report.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.pojo.ComReport;
import com.rskytech.report.dao.IComReportDao;

public class ComReportDao extends BaseDAO implements IComReportDao {

	@Override
	public Page loadComReportList(Page page, String modelSeriesId,
			String reportType, String generateId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.report_id,");
		sb.append("       a.report_name,");
		sb.append("       a.version_no,");
		sb.append("       a.version_user,");
		sb.append("       b.user_name,");
		sb.append("       to_char(a.version_date, 'yyyy-mm-dd') version_date,");
		sb.append("       a.report_status,");
		sb.append("       a.report_word_url,");
		sb.append("       a.report_pdf_url,");
		sb.append("       a.version_desc");
		sb.append("  from com_report a, com_user b");
		sb.append(" where a.version_user = b.user_id(+)");
		sb.append("   and a.valid_flag = " + ComacConstants.VALIDFLAG_YES);
		sb.append("   and a.model_series_id = '" + modelSeriesId + "'");
		sb.append("   and a.report_type = '" + reportType + "'");
		if(generateId != null && !"".equals(generateId)){
			sb.append("   and a.generate_id = '" + generateId + "'");
		}
		sb.append("  order by a.create_date asc");
		return this.findBySql(page, sb.toString(), null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComReport> loadAllReportListNoPage(String modelSeriesId,
			String reportType, String mainId) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComReport.class);
		dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
		dc.add(Restrictions.eq("reportType", reportType));
		if(mainId !=null && !"".equals(mainId)){
			dc.add(Restrictions.eq("generateId", mainId));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}

}
