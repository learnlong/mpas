package com.rskytech.paramdefinemanage.bo.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.ILhirfParamBo;
import com.rskytech.paramdefinemanage.dao.ILhirfParamDao;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusInterval;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.SStep;

public class LhirfParamBo extends BaseBO implements ILhirfParamBo{
	
	private ICusEdrAdrBo cusEdrAdrBo;
	private ILhirfParamDao lhirfParamDao;
	
	
	public ICusEdrAdrBo getCusEdrAdrBo() {
		return cusEdrAdrBo;
	}


	public void setCusEdrAdrBo(ICusEdrAdrBo cusEdrAdrBo) {
		this.cusEdrAdrBo = cusEdrAdrBo;
	}
	

	public ILhirfParamDao getLhirfParamDao() {
		return lhirfParamDao;
	}


	public void setLhirfParamDao(ILhirfParamDao lhirfParamDao) {
		this.lhirfParamDao = lhirfParamDao;
	}


	/**
	 * 根据传入的参数获取S45项目的算法
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param itemFlg 项目类型区分：VR、SC、LK、AD、ED、PR、EV、RS
	 * @return List
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusItemS45> getAlgList(String stepFlg, String itemFlg,
			String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemS45.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("stepFlg", stepFlg));
		dc.add(Restrictions.eq("itemFlg", itemFlg));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("itemFlg"));
		dc.addOrder(Order.asc("itemSort"));
		return this.findByCritera(dc);

	}
	
	
	/**
	 * 查询自定义S4、S5项目表中的所有有效的数据
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg  分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param itemFlg  项目区分 ：VR、SC、LK、AD、ED、PR、EV、RS
	 * @return 自定义S4、S5、LH4项目List
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusItemS45> getS45List(String modelSeriesId, String stepFlg,
			String itemFlg) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemS45.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("stepFlg", stepFlg));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		if (BasicTypeUtils.isNullorBlank(itemFlg) == false) { // 判断node是否为空
			dc.add(Restrictions.eq("itemFlg", itemFlg));
		}
		dc.addOrder(Order.asc("itemFlg"));
		dc.addOrder(Order.asc("itemSort"));
		return this.findByCritera(dc);
	}
	
	/**
	 * 保存S45项目的事务
	 * 
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusItemS45 : cusItemS45项目对象
	 * @param operateFlag "update"还是"save"?
	 * @param userId : 当前用户ID
	 * @throws BusinessException
	 */
	@Override
	public void saveS45Item(CusItemS45 cusItemS45, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException {

		saveOrUpdate(cusItemS45, operateFlag, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.CUSEDRADR_Full_YES) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			cusEdrAdrBo.saveOrUpdate(temp, ComacConstants.DB_UPDATE, userId);
		}
	}
	
	
	/**
	 * 删除项目节点
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusItemS45 : CusItemS45项目对象
	 * @param userId : 当前用户ID
	 * @throws BusinessException
	 */
	@Override
	public void deleteNode(CusItemS45 cusItemS45, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException {

		this.update(cusItemS45, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.CUSEDRADR_Full_YES) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			cusEdrAdrBo.saveOrUpdate(temp, ComacConstants.DB_UPDATE, userId);
		}
	}
	
	
	/**
	 * 检测分析该分析步骤是否已经存在分析数据
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @return 是否存在已分析数据：存在：false，不存在：true
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkCusS45Mtrix(String stemFlg, String modelSeriesId)
			throws BusinessException {
		if (ComacConstants.LH4.equals(stemFlg)) {
			DetachedCriteria dc = DetachedCriteria.forClass(LhStep.class);
			dc.createAlias("lhMain", "lhMain");
			dc.createAlias("lhMain.comModelSeries", "comModelSeries");
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
			dc.add(Restrictions.eq("lhMain.validFlag", ComacConstants.YES));
			dc.add(Restrictions.in("lh4", new Integer[] { 1, 2 }));
			List<LhStep> stepList = (List<LhStep>) this.findByCritera(dc);
			if (stepList.size() > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			// 根据分析步骤的状态来检测是否已经存在分析数据
			DetachedCriteria dc = DetachedCriteria.forClass(SStep.class);
			dc.createAlias("SMain", "SMain");
			dc.createAlias("SMain.comModelSeries", "comModelSeries");
			dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
			dc.add(Restrictions.eq("SMain.validFlag", ComacConstants.YES));
			if (stemFlg.equals(ComacConstants.S4A)) {
				dc.add(Restrictions.or(Restrictions.in("s4aIn", new Integer[] {
						1, 2 }), Restrictions.in("s4aOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals(ComacConstants.S4B)) {
				dc.add(Restrictions.or(Restrictions.in("s4bIn", new Integer[] {
						1, 2 }), Restrictions.in("s4aOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals(ComacConstants.S5A)) {
				dc.add(Restrictions.or(Restrictions.in("s5aIn", new Integer[] {
						1, 2 }), Restrictions.in("s4aOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals(ComacConstants.S5B)) {
				dc.add(Restrictions.or(Restrictions.in("s5bIn", new Integer[] {
						1, 2 }), Restrictions.in("s4aOut",
						new Integer[] { 1, 2 })));
			}
			List<SStep> stepList = (List<SStep>) this.findByCritera(dc);
			if (stepList.size() > 0) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	
	/**
	 * 保存LH4矩阵树
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param getSysUser : 当前登录用户对象
	 * @param EDMMA : EA算法
	 * @param ADMMA : AD算法
	 * @param ROOTMMA : LH4根节点算法
	 * @throws BusinessException
	 */
	@Override
	public void saveLH4(ComUser comUser,ComModelSeries comModelSeries, String EDMMA, String ADMMA,
			String ROOTMMA) throws BusinessException {
		String[] algorithms = { "ED", "AD" };
		List<CusItemS45> list = new ArrayList<CusItemS45>();
		String temp = "";
		for (int i = 0; i < algorithms.length; i++) {
			list = this.getS45List(comModelSeries
					.getModelSeriesId(), ComacConstants.LH4, algorithms[i]);
			for (CusItemS45 cusItem : list) {
				if (i == 0) {
					temp = EDMMA;
				} else if (i == 1) {
					temp = ADMMA;
				}
				cusItem.setItemAlgorithm(temp);
				this.saveOrUpdate(cusItem, ComacConstants.DB_UPDATE, comUser
						.getUserId());
			}
		}

		// 操作EDRADR表
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
			comModelSeries.getModelSeriesId(), ComacConstants.LH4);
		CusEdrAdr cusEdrAdr = new CusEdrAdr();
		String operateFlag;
		if (cusEdrAdrList.size() == 0) { // 增加操作
			operateFlag = ComacConstants.DB_INSERT;
			cusEdrAdr.setComModelSeries(comModelSeries);
			cusEdrAdr.setStepFlg(ComacConstants.LH4);
			cusEdrAdr.setAlgorithmFlg(ROOTMMA);
		} else { // 更新操作
			cusEdrAdr = cusEdrAdrList.get(0);
			cusEdrAdr.setAlgorithmFlg(ROOTMMA);
			operateFlag = ComacConstants.DB_UPDATE;
		}
		cusEdrAdrBo.saveOrUpdate(cusEdrAdr, operateFlag, comUser.getUserId());
	}
	
	
	/**
	 * 查询Cus_Interval表中属于某一机型LH5,S6分析区评级表数据
	 * 
	 * @param internalFlg
	 *            A/int
	 * @param internalFlg
	 *            B/out
	 * @param anaFlg
	 *            LH5/S6
	 * @param modelSeriesId
	 * @return 自定义评级List
	 * @throws BusinessException
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getCusIntervalList(Object[] obs)
			throws BusinessException {
		return lhirfParamDao.getCusInList(obs);
	}
	
	
	/**
	 * 删除Cus_Interval表中属于LH5步骤某一机型的所有数据(用于LH5的回退)
	 * 
	 * @param modelSeriesId
	 *            机型ID
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void finalBackLh5(String modelSeriesId, String userId)
			throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusInterval.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("anaFlg", ComacConstants.LHIRF_LH5));
		List<CusInterval> list = this.findByCritera(dc);
		for (CusInterval cusInterval : list) {
			this.delete(cusInterval);
		}

		// 操作EDRADR表
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, ComacConstants.LH4);
		CusEdrAdr cusEdrAdr = new CusEdrAdr();
		if (cusEdrAdrList.size() != 0) {
			cusEdrAdr = cusEdrAdrList.get(0);
			cusEdrAdr.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			cusEdrAdrBo.saveOrUpdate(cusEdrAdr, ComacConstants.DB_UPDATE,
					userId);
		}
	}
	
	/**
	 * 保存LH5的数据,并将LH4的完成状态改为1(1:表示完成,0:表示未完成)
	 * 
	 * @param modelSeriesId
	 *            机型ID
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void saveLh5(ComUser comUser,ComModelSeries comModelSeries ,String jsonData)
			throws BusinessException {
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		JSONObject jsonObject = null;
		String dbOperate = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String id = jsonObject.getString("intervalIdA");
			String idb = jsonObject.getString("intervalIdB");
			CusInterval cus = new CusInterval();
			CusInterval cusInterval = new CusInterval();
			// 修改操作时
			if (!BasicTypeUtils.isNullorBlank(id) && !"0".equals(id)
					&& (!BasicTypeUtils.isNullorBlank(id) && !"0".equals(id))) {
				dbOperate = ComacConstants.DB_UPDATE;
				cus = (CusInterval) this.loadById(
						CusInterval.class, id);
				cusInterval = (CusInterval) this.loadById(
						CusInterval.class, idb);
			} else { // 追加操作时
				dbOperate = ComacConstants.DB_INSERT;
				cus.setAnaFlg(ComacConstants.LHIRF_LH5);
				cus.setComModelSeries(comModelSeries);
				cus.setInternalFlg("A");
				cusInterval.setAnaFlg(ComacConstants.LHIRF_LH5);
				cusInterval.setComModelSeries(comModelSeries);
				cusInterval.setInternalFlg("B");
			}
			cus.setIntervalValue(jsonObject.getString("valueA"));
			cus.setIntervalLevel(jsonObject.getInt("level"));
			cusInterval.setIntervalValue(jsonObject.getString("valueB"));
			cusInterval.setIntervalLevel(jsonObject.getInt("level"));
			this.saveOrUpdate(cus, dbOperate, comUser.getUserId());
			this.saveOrUpdate(cusInterval, dbOperate, comUser.getUserId());
			
			// 操作EDRADR表
			List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
					comModelSeries.getModelSeriesId(),
					ComacConstants.LH4);
			CusEdrAdr cusEdrAdr = new CusEdrAdr();
			if (cusEdrAdrList.size() != 0) {
				cusEdrAdr = cusEdrAdrList.get(0);
				cusEdrAdr.setOperateFlg(ComacConstants.CUSEDRADR_Full_YES);
				cusEdrAdrBo.saveOrUpdate(cusEdrAdr, ComacConstants.DB_UPDATE,
						comUser.getUserId());
			}
		}
	}

	
	
	
	


}
