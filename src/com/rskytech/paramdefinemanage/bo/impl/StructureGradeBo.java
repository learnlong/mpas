package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IStructureGradeBo;
import com.rskytech.paramdefinemanage.dao.IStructureGradeDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusInterval;

public class StructureGradeBo extends BaseBO implements IStructureGradeBo{

	private IStructureGradeDao structureGradeDao;
	
		
	public IStructureGradeDao getStructureGradeDao() {
		return structureGradeDao;
	}



	public void setStructureGradeDao(IStructureGradeDao structureGradeDao) {
		this.structureGradeDao = structureGradeDao;
	}


	/**
	 * 查询Cus_Interval表中属于某一机型LH5,S6分析区评级表数据
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCusIntervalList(Object[] obs)
			throws BusinessException {
		return structureGradeDao.getCusInList(obs);
	}
	
	
	
	@Override
	public void saveS6All( ComUser user,ComModelSeries comModelSeries, String jsonData) throws BusinessException {
		if(jsonData != null){
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
			JSONObject jsonObject = null;
			String dbOperate = "";
			 	for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("intervalIntId");
				String idb = jsonObject.getString("intervalOutId");
				CusInterval cus = new CusInterval();
				CusInterval cusInterval = new CusInterval();
				// 修改操作时
				if (!BasicTypeUtils.isNullorBlank(id) && !"0".equals(id)&&
					(!BasicTypeUtils.isNullorBlank(id) && !"0".equals(id))) {
					dbOperate = ComacConstants.DB_UPDATE;
					cus = (CusInterval) this.loadById(CusInterval.class,id);
					cusInterval=(CusInterval) this.loadById(CusInterval.class,idb);
				} else {// 追加操作时
	                dbOperate = ComacConstants.DB_INSERT;  
	                cus.setAnaFlg("S6");
	                cus.setComModelSeries(comModelSeries);
	                cus.setInternalFlg("int");
	    			cusInterval.setAnaFlg("S6");
	                cusInterval.setInternalFlg("out");
	                cusInterval.setComModelSeries(comModelSeries);
				}
				cus.setIntervalValue(jsonObject.getString("valueInt"));
				cus.setIntervalLevel(jsonObject.getInt("levelS6"));
				cusInterval.setIntervalValue(jsonObject.getString("valueOut"));
				cusInterval.setIntervalLevel(jsonObject.getInt("levelS6"));
				this.saveOrUpdate(cus, dbOperate, user.getUserId());
				this.saveOrUpdate(cusInterval, dbOperate, user.getUserId());
			}///
		}

	}
	
	/**
	 * 删除Cus_Interval表中属于某一机型LH5分析区评级表,按 等级评级删除两条数据记录
	 * 
	 * @param idA
	 * @param idB
	 * @param userId
	 * @throws BusinessException
	 */
	public void updateLh4All(String deleteIdAint, String deleteIdBout,
			ComUser user) throws BusinessException {
		String[] idArr = { deleteIdAint, deleteIdBout };
		for (int i = 0; i < idArr.length; i++) {
			if (!BasicTypeUtils.isNullorBlank(idArr[i])) {
				CusInterval cusInt = (CusInterval) this.loadById(
						CusInterval.class, idArr[i]);
				if (cusInt != null) {
					String modelSeriesId = cusInt.getComModelSeries()
							.getModelSeriesId();
					Integer levelValue = cusInt.getIntervalLevel();
					String internalFlg = cusInt.getInternalFlg();
					List<CusInterval> listA = this.getLh5Data(modelSeriesId,
							levelValue, internalFlg);
					for (int j = 0; j < listA.size(); j++) {
						CusInterval cusInterval = listA.get(j);
						cusInterval.setIntervalLevel(cusInterval
								.getIntervalLevel() - 1);
						this.saveOrUpdate(cusInterval,
								ComacConstants.DB_UPDATE, user.getUserId());
					}
					this.delete(cusInt, user.getUserId());
				}
			}
		}
	}
	

	/*
	 * 根据传入的级别查询出LH5中所有比该级别值大的数据
	 */
	@SuppressWarnings("unchecked")
	public List<CusInterval> getLh5Data(String modelSeriesId,
			Integer levelValue, String internalFlg) {

		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("anaFlg", ComacConstants.LHIRF_LH5));
		dc.add(Restrictions.eq("internalFlg", internalFlg));
		dc.add(Restrictions.not(Restrictions.between("intervalLevel", 0,
				levelValue)));
		dc.addOrder(Order.asc("intervalLevel"));
		return this.findByCritera(dc);
	}

	
	
	/**
	 * 删除Cus_Interval表中属于某一机型LH5或S6分析区评级表,按 等级评级删除两条数据记录
	 * 
	 * @param idA
	 * @param idB
	 * @param userId
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteAll(String deleteIdAint, String deleteIdBout,
			String jsonData, ComUser user,ComModelSeries comModelSeries) throws BusinessException {

		if (jsonData == null) { // 为空,则是LH5的删除
			this.updateLh4All(deleteIdAint, deleteIdBout, user);
		} else {
			// 先删除 在 排序保存
			if (!BasicTypeUtils.isNullorBlank(deleteIdAint)) {
				CusInterval cusInt = (CusInterval) this.loadById(
						CusInterval.class, deleteIdAint);
				if (cusInt != null) {
					this.delete(cusInt, user.getUserId());
				}
			}
			if (!BasicTypeUtils.isNullorBlank(deleteIdBout)) {
				CusInterval cusOut = (CusInterval) this.loadById(
						CusInterval.class, deleteIdBout);
				if (cusOut != null) {
					this.delete(cusOut, user.getUserId());
				}
			}
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
			JSONObject jsonObject = null;
			String dbOperate = "";
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("intervalIntId");
				String idb = jsonObject.getString("intervalOutId");
				CusInterval cus = new CusInterval();
				CusInterval cusInterval = new CusInterval();
				// 修改操作时
				if (!BasicTypeUtils.isNullorBlank(id)
						&& !"0".equals(id)
						&& (!BasicTypeUtils.isNullorBlank(id) && !"0"
								.equals(id))) {
					dbOperate = ComacConstants.DB_UPDATE;
					cus = (CusInterval) this.loadById(CusInterval.class, id);
					cusInterval = (CusInterval) this.loadById(
							CusInterval.class, idb);
				} else {// 追加操作时
					dbOperate = ComacConstants.DB_INSERT;
					cus.setAnaFlg("S6");
					cus.setComModelSeries(comModelSeries);
					cus.setInternalFlg("int");
					cusInterval.setAnaFlg("S6");
					cusInterval.setInternalFlg("out");
					cusInterval.setComModelSeries(comModelSeries);
				}
				cus.setIntervalValue(jsonObject.getString("valueInt"));
				cus.setIntervalLevel(jsonObject.getInt("levelS6"));
				cusInterval.setIntervalValue(jsonObject.getString("valueOut"));
				cusInterval.setIntervalLevel(jsonObject.getInt("levelS6"));

				this.saveOrUpdate(cus, dbOperate, user.getUserId());
				this.saveOrUpdate(cusInterval, dbOperate, user.getUserId());
			}
		}
	}
	
	
	
	
	
	
	
	
	
}
