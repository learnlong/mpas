package com.rskytech.lhirf.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.impl.BaseDAO;
import com.richong.arch.web.Page;
import com.rskytech.ComacConstants;
import com.rskytech.lhirf.dao.ILhMainDao;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.LhMain;

@SuppressWarnings("unchecked")
public class LhMainDao extends BaseDAO implements ILhMainDao {

	@Override
	public List<LhMain> getLhMainListByAreaId(String areaId, String modelId,String hsiCode,String hsiName)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(LhMain.class);
		dc.add(Restrictions.eq("comArea.id", areaId ));
		if(!BasicTypeUtils.isNullorBlank(hsiCode)){
			dc.add(Restrictions.like("hsiCode", hsiCode, MatchMode.ANYWHERE));
		}
		if(!BasicTypeUtils.isNullorBlank(hsiName)){
			dc.add(Restrictions.like("hsiName", hsiName, MatchMode.ANYWHERE));
		}
		if(!BasicTypeUtils.isNullorBlank(modelId)){
		  dc.add(Restrictions.eq("comModelSeries.id", modelId ));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		return this.findByCriteria(dc);
	}
	
	@Override
	public List<LhMain> getLhMainByRefHsiCode(String refHsiCode,String comModelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(LhMain.class);
		dc.add(Restrictions.eq("comModelSeries.id", comModelSeriesId));
		dc.add(Restrictions.eq("refHsiCode", refHsiCode));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}
	
	@Override
	public List<LhMain> getLhMainByHsiCode(String hsiCode,String comModelSeriesId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(LhMain.class);
		dc.add(Restrictions.eq("hsiCode", hsiCode));
		dc.add(Restrictions.eq("comModelSeries.id", comModelSeriesId));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		return this.findByCriteria(dc);
	}
	@Override
	public List<ComArea> getAreaNodeList(String modelSeriesId,String parentNodeId,Integer areaLevel)
			throws BusinessException {
		int defaultAreaLevel =1;
		DetachedCriteria dc = DetachedCriteria.forClass(ComArea.class);
		if(null == modelSeriesId ){
			if(null != parentNodeId){
				dc.add(Restrictions.eq("comArea.id", parentNodeId));
			}else{
				dc.add(Restrictions.eq("areaLevel",  defaultAreaLevel));
			}
		}else{///机型不为空
			dc.add(Restrictions.eq("comModelSeries.id", modelSeriesId));
			if(parentNodeId == null || "".equals(parentNodeId)){
				dc.add(Restrictions.eq("areaLevel",  areaLevel));
			}else{
				dc.add(Restrictions.eq("comArea.id", parentNodeId));
				dc.add(Restrictions.eq("areaLevel", areaLevel));
			}
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.VALIDFLAG_YES));
		dc.addOrder(Order.asc("areaCode"));
		return this.findByCriteria(dc);
	}
	
	/**
	 * 根据 区域的ID   查询区域节点下的所有HSI 信息
	 * @param modelSeriesId  机型ID
	 * @param parentNodeOneId 区域第一 节点 
	 * @param  parentNodeTwoId 区域第二节点
	 * @return parentNodeThreeId  区域第三节点
	 */
	@Override
	public List getLhHsiListByAreaId(String modelSeriesId,
			String parentNodeOneId, String parentNodeTwoId,
			String parentNodeThreeId,Page page) {
		String str1 = "";
		if (modelSeriesId != null || "".equals(modelSeriesId)) {
			if (parentNodeOneId == null || "".equals(parentNodeOneId)) {
				// /只查找指定一个机型的所有HSI");
				str1 = " WHERE ZZ.MODEL_SERIES_ID = '" + modelSeriesId + "'";
			} else if (parentNodeTwoId == null || "".equals(parentNodeTwoId)) {
				// 只查找指定第一节点的个 400 节点其以下的二级和三级");
				str1 = " WHERE ZZ.MODEL_SERIES_ID = '"
						+ modelSeriesId
						+ "'"
						+ " AND ZZ.AREA_ID IN(  "
						+ " SELECT A22.AREA_ID  FROM  COM_AREA A22  WHERE A22.PARENT_AREA_ID IN "
						+ "(SELECT A.AREA_ID FROM COM_AREA A WHERE A.PARENT_AREA_ID = '"
						+ parentNodeOneId
						+ "'"
						+ ") OR A22.AREA_ID IN (SELECT A.AREA_ID FROM COM_AREA A WHERE "
						+ " A.PARENT_AREA_ID = '" + parentNodeOneId + "'"
						+ ") )";
			} else if (parentNodeThreeId == null
					|| "".equals(parentNodeThreeId)) {

				// /只查找指定第二节点及第二节点下挂的所有第三节点");
				str1 = " WHERE ZZ.MODEL_SERIES_ID = '"
						+ modelSeriesId
						+ "' AND ZZ.AREA_ID IN ( "
						+ " SELECT AA.AREA_ID FROM COM_AREA AA  WHERE  AA.AREA_ID IN ("
						+ "SELECT A.AREA_ID	FROM COM_AREA A WHERE"
						+ " A.PARENT_AREA_ID ='" + parentNodeTwoId + "'"
						+ "OR A.AREA_ID = '" + parentNodeTwoId + "'" + ") )";
			} else if (parentNodeThreeId != null
					|| !"".equals(parentNodeThreeId)) {
				// /只查找指定第三节节点下的所有HSI");
				str1 = " WHERE ZZ.MODEL_SERIES_ID = '" + modelSeriesId
						+ "' AND ZZ.AREA_ID = '" + parentNodeThreeId + "'";
			}
		}
		String sql = " SELECT  *   FROM  ("
				+ "SELECT HH.HSI_ID ,HH.HSI_CODE, HH.HSI_NAME, HH.LH_COMP_NAME, "
				+ " HH.ATA_CODE, HH.IPV_OPVP_OPVE,  HH.REF_HSI_CODE, HH.STATUS,  "
				+ " HH.ANA_USER,A.MODEL_SERIES_ID,A.AREA_ID,A.AREA_CODE"
				+ " FROM LH_MAIN HH "
				+ " JOIN COM_AREA A ON  HH.AREA_ID = A.AREA_ID "
				+ " AND HH.MODEL_SERIES_ID = A.MODEL_SERIES_ID " 
				+ " WHERE HH.VALID_FLAG = " + ComacConstants.VALIDFLAG_YES+")  ZZ" + str1
				+ " ORDER BY ZZ.HSI_CODE  ASC";
		 
		return this.findBySql(page, sql, null).getResult();
	}
	
	@Override
	public List getLhrifTaskId(String modelSeriesId, String firstTextField, Page page) {
		  String sql = " SELECT * FROM  "
				+ "(SELECT  M.HSI_CODE, M.HSI_NAME, A.AREA_CODE,T.TASK_ID,T.TASK_CODE,"
				+ " T.TASK_TYPE, T.TASK_DESC,T.REACH_WAY,"
				+ " T.TASK_INTERVAL ,T.HAS_ACCEPT,T.REJECT_REASON,T.WHY_TRANSFER, "
				+ " T.SOURCE_SYSTEM,T.NEED_TRANSFER, T.SYS_TRANSFER,"
				+ " A.AREA_ID,T.MODEL_SERIES_ID,T.VALID_FLAG,T.Own_Area  FROM TASK_MSG  T  "
				+ " JOIN LH_MAIN M ON T.SOURCE_ANA_ID = M.HSI_ID AND T.MODEL_SERIES_ID = M.MODEL_SERIES_ID "
				+ " LEFT JOIN COM_AREA  A ON A.AREA_ID = M.AREA_ID "
				+ "	WHERE M.VALID_FLAG = " + ComacConstants.VALIDFLAG_YES + ""
				+ " ) ZZ  WHERE  ZZ.SOURCE_SYSTEM ='"+ ComacConstants.LHIRF_CODE + "'"
				+ " AND ZZ.NEED_TRANSFER =" + ComacConstants.YES
				+ " AND ZZ.SYS_TRANSFER ='" + ComacConstants.ZONAL_CODE+"'" 
				+ " AND ZZ.VALID_FLAG = " + ComacConstants.VALIDFLAG_YES
				+ " AND  ZZ.MODEL_SERIES_ID = '" + modelSeriesId + "'";
				if (firstTextField != null && !"".equals(firstTextField)) {
					sql +=" AND ZZ.HSI_CODE LIKE '%"+firstTextField+"%'";
				} 
		  return this.findBySql(page, sql, null).getResult();
	}

	@Override
	public List<LhMain> getLhHsiByModelSeriesId(String modelSeriesId)
			throws BusinessException {
			DetachedCriteria dc = DetachedCriteria.forClass(LhMain.class);
			dc.add(Restrictions.eq("lhMain.comModelSeries.id",modelSeriesId));
			dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
			return  this.findByCriteria(dc);
	}
	
}
