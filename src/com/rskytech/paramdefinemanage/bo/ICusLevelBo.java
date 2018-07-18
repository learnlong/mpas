package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusLevel;

public interface ICusLevelBo extends IBo{

	/**
	 * 根据不同分析步骤中的项目查询出相应的等级数据
	 * @param modelSeriesId 机型系列ID
	 * @param anaFlg 自定义分析步骤：ZA5、S4A、S4B、S5A、S5B、ZA43、LH4
	 * @param itemId 项目ID
	 * @return 自定义S4、S5项目List
	 * @throws BusinessException
	 */
	public List<CusLevel> getLevelList(String  modelSeriesId,String anaFlg,String itemId) throws BusinessException;
	
	/**
	 * 保存S45级别的事务
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusLevel  CusLevel项目对象
	 * @param operateFlag  更新 还是 保存?
	 * @param userId  当前用户ID
	 * @throws BusinessException
	 */
	public void saveS45Level(CusLevel cusLevel,String operateFlag,String userId,String modelSeriesId,String stepFlg) throws BusinessException;
	
	/**
	 * 删除级别节点
	 * @param modelSeriesId 机型系列Id
	 * @param stepFlg 分析步骤 ：S4A、S4B、S5A、S5B、LH4
	 * @param cusLevel  CusLevel项目对象
	 * @param operateFlag  更新 还是 保存?
	 * @param userId  当前用户ID
	 * @throws BusinessException
	 */
	public void deleteNode(CusLevel cusLevel,String userId,String modelSeriesId,String stepFlg) throws BusinessException;
}
