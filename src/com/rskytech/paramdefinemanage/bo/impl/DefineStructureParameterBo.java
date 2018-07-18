package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.base.BasicTypeUtils;
import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.IDefineStructureParameterBo;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusItemS45;
import com.rskytech.pojo.CusLevel;
import com.rskytech.pojo.LhStep;
import com.rskytech.pojo.SStep;

public class DefineStructureParameterBo extends BaseBO implements IDefineStructureParameterBo{

	
	/**
	 * 根据机型、分析步骤查询EDR、ADR选择表的数据
	 * @param modelSeriesId 机型系列ID
	 * @param stepFlg 分析步骤
	 * @return 当前机型分析步骤的选择表数据List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CusEdrAdr> getCusEdrAdrList(String modelSeriesId,
			String stepFlg) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusEdrAdr.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId",modelSeriesId));
		dc.add(Restrictions.eq("stepFlg",stepFlg));
		return this.findByCritera(dc);
	}
	
	
	/**
	 * 查询自定义S4、S5项目表中的所有有效的数据
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg  分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param itemFlg  项目区分 ：VR、SC、LK、AD、ED、PR、EV、RS
	 * @return 自定义S4、S5、LH4项目List
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
	 * 查询自定义S4、S5项目表中的所有有效的数据
	 * @param null
	 * @param itemId : -1表示获取当前分析步骤中项目的级别数量
	 * @return 自定义S4、S5项目List
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CusLevel> getLevelList(String modelSeriesId, String anaFlg,
			String itemId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusLevel.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("anaFlg", anaFlg));
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		if (itemId != null && !"".equals(itemId) && !"-1".equals(itemId)) {
			dc.add(Restrictions.eq("itemId", itemId));
		}
		dc.addOrder(Order.asc("itemId"));
		dc.addOrder(Order.asc("levelValue"));
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
	 */
	@Override
	public void saveS45Item(CusItemS45 cusItemS45, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException {

		saveOrUpdate(cusItemS45, operateFlag, userId);
		// 设置edradr表中数据的完整性为否
//		List<CusEdrAdr> cusEdrAdrList = getCusEdrAdrList(
//				modelSeriesId, stepFlg);
//		if (cusEdrAdrList.size() != 0
//				&& cusEdrAdrList.get(0).getOperateFlg() == 1) { // EdrAdr表中存在数据并且数据完整性为1
//			CusEdrAdr temp = cusEdrAdrList.get(0);
//			temp.setOperateFlg(0);
//			this.saveOrUpdate(temp, ComacConstants.DB_UPDATE, userId);
//		}
	}
	
	/**
	 * 检测分析该分析步骤是否已经存在分析数据
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @return 是否存在已分析数据：存在：false，不存在：true
	 */
	@SuppressWarnings("unchecked")
	public Boolean checkCusS45Mtrix(String stemFlg, String modelSeriesId)
			throws BusinessException {
		if ("LH4".equals(stemFlg)) {
			DetachedCriteria dc = DetachedCriteria.forClass(LhStep.class);
			dc.createAlias("lhHsi", "lhHsi");
			dc.createAlias("lhHsi.lhMain", "lhMain");
			dc.add(Restrictions.eq("lhMain.modelSeriesId", modelSeriesId));
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
			//dc.createAlias("SSsi", "SSsi");
			dc.createAlias("SMain", "SMain");
			dc.add(Restrictions.eq("SMain.comModelSeries.modelSeriesId", modelSeriesId));
			dc.add(Restrictions.eq("SMain.validFlag", ComacConstants.YES));//1
			if (stemFlg.equals("S4A")) {
				dc.add(Restrictions.or(Restrictions.in("s4aIn", new Integer[] {
						1, 2 }), Restrictions.in("s4aOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals("S4B")) {
				dc.add(Restrictions.or(Restrictions.in("s4bIn", new Integer[] {
						1, 2 }), Restrictions.in("s4bOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals("S5A")) {
				dc.add(Restrictions.or(Restrictions.in("s5aIn", new Integer[] {
						1, 2 }), Restrictions.in("s5aOut",
						new Integer[] { 1, 2 })));
			} else if (stemFlg.equals("S5B")) {
				dc.add(Restrictions.or(Restrictions.in("s5bIn", new Integer[] {
						1, 2 }), Restrictions.in("s5bOut",
						new Integer[] { 1, 2 }))); 
			}else if(stemFlg.equals("SYA")){
				dc.add(Restrictions.or(Restrictions.in("syaIn", new Integer[] {
						1, 2 }), Restrictions.in("syaOut",
						new Integer[] { 1, 2 }))); 
			}else if(stemFlg.equals("SYB")){
				dc.add(Restrictions.or(Restrictions.in("sybIn", new Integer[] {
						1, 2 }), Restrictions.in("sybOut",
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
	 * 检测二级项目的数量是否超出数据库字段的数量
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param itemFlg 项目标记 : 这个是一级项目的标记,查询的时候取项目标记不为一级项目标记的,就是二级项目了
	 * @return 二级项目的数量
	 */
	@Override
	public Integer checkItemCount(String stepFlg, String itemFlg,
			String modelSeriesId) throws BusinessException {
		DetachedCriteria dc = DetachedCriteria.forClass(CusItemS45.class);
		dc.add(Restrictions.eq("comModelSeries.modelSeriesId", modelSeriesId));
		dc.add(Restrictions.eq("stepFlg", stepFlg));
		if((ComacConstants.MATRIX_ZEROLEVELID.equals(itemFlg))){//增加的项目是一级项目  0
			dc.add(Restrictions.eq("itemFlg", itemFlg));
		}else{
			dc.add(Restrictions.ne("itemFlg", ComacConstants.MATRIX_ZEROLEVELID));
		}
		dc.add(Restrictions.eq("validFlag", ComacConstants.YES));
		dc.addOrder(Order.asc("itemFlg"));
		dc.addOrder(Order.asc("itemSort"));
		return this.findByCritera(dc).size();
	}
	
	/**
	 * 保存S45级别的事务
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusLevel  CusLevel项目对象
	 * @param operateFlag  更新 还是 保存?
	 * @param userId  当前用户ID
	 */
	@Override
	public void saveS45Level(CusLevel cusLevel, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException { 
		this.saveOrUpdate(cusLevel, operateFlag, userId);
//		// 设置edradr表中数据的完整性为否
//		List<CusEdrAdr> cusEdrAdrList = getCusEdrAdrList(
//				modelSeriesId, stepFlg);
//		if (cusEdrAdrList.size() != 0
//				&& cusEdrAdrList.get(0).getOperateFlg() == 1) { // EdrAdr表中存在数据并且数据完整性为1
//			CusEdrAdr temp = cusEdrAdrList.get(0);
//			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
//			this.saveOrUpdate(temp, ComacConstants.DB_UPDATE,
//					userId);
//		}
	}
	
	/**
	 * 删除项目节点
	 * 
	 * @param modelSeriesId : 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusItemS45 : CusItemS45项目对象
	 * @param userId : 当前用户ID
	 */
	@Override
	public void deleteNode(CusItemS45 cusItemS45, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException {

		this.update(cusItemS45, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.CUSEDRADR_Full_YES) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			this.saveOrUpdate(temp, ComacConstants.DB_UPDATE, userId);
		}
	}
	
	/**
	 * 删除级别节点
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusLevel  CusLevel项目对象
	 * @param operateFlag  更新 还是 保存?
	 * @param userId  当前用户ID
	 * @throws BusinessException
	 */
	@Override
	public void deleteNodeLevel(CusLevel cusLevel, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException {
		
		this.update(cusLevel, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.CUSEDRADR_Full_YES) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			this.saveOrUpdate(temp, ComacConstants.DB_UPDATE,
					userId);
		}
	}

	
	
	
	
	
	
	
}
