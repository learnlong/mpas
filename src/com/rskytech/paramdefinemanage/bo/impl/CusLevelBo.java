package com.rskytech.paramdefinemanage.bo.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.impl.BaseBO;
import com.rskytech.ComacConstants;
import com.rskytech.paramdefinemanage.bo.ICusEdrAdrBo;
import com.rskytech.paramdefinemanage.bo.ICusLevelBo;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.CusLevel;

public class CusLevelBo extends BaseBO implements ICusLevelBo {

	private ICusEdrAdrBo cusEdrAdrBo;
	/**
	 * 查询自定义S4、S5项目表中的所有有效的数据
	 * @param null
	 * @param itemId : -1表示获取当前分析步骤中项目的级别数量
	 * @return 自定义S4、S5项目List
	 * @throws BusinessException
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
	 * 保存S45级别的事务
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusLevel  CusLevel项目对象
	 * @param operateFlag  更新 还是 保存?
	 * @param userId  当前用户ID
	 * @throws BusinessException
	 */
	@Override
	public void saveS45Level(CusLevel cusLevel, String operateFlag,
			String userId, String modelSeriesId, String stepFlg)
			throws BusinessException {
		this.saveOrUpdate(cusLevel, operateFlag, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == 1) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			cusEdrAdrBo.saveOrUpdate(temp, ComacConstants.DB_UPDATE,
					userId);
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
	public void deleteNode(CusLevel cusLevel, String userId,
			String modelSeriesId, String stepFlg) throws BusinessException {
		
		this.update(cusLevel, userId);
		// 设置edradr表中数据的完整性为否
		List<CusEdrAdr> cusEdrAdrList = cusEdrAdrBo.getCusEdrAdrList(
				modelSeriesId, stepFlg);
		if (cusEdrAdrList.size() != 0
				&& cusEdrAdrList.get(0).getOperateFlg() == ComacConstants.CUSEDRADR_Full_YES) { // EdrAdr表中存在数据并且数据完整性为1
			CusEdrAdr temp = cusEdrAdrList.get(0);
			temp.setOperateFlg(ComacConstants.CUSEDRADR_Full_NO);
			cusEdrAdrBo.saveOrUpdate(temp, ComacConstants.DB_UPDATE,
					userId);
		}
	}

	public ICusEdrAdrBo getCusEdrAdrBo() {
		return cusEdrAdrBo;
	}

	public void setCusEdrAdrBo(ICusEdrAdrBo cusEdrAdrBo) {
		this.cusEdrAdrBo = cusEdrAdrBo;
	}
}
