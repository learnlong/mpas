package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.CusEdrAdr;
import com.rskytech.pojo.S4;
import com.rskytech.pojo.Sy;

public interface ISyDao extends IDAO {
	public List getItemCount(String itemName,String modelId,String aOrb) throws BusinessException;
	public List getLevelCount(String  itemId,String modelId,String aOrb) throws BusinessException;
	/**
	 * 根据smainId和s1Id查询Sy
	 * @param ssiId
	 * @param id
	 * @param inOrOut
	 * @return
	 * @throws BusinessException
	 */
	public List<Sy> getSyListBySsiIdAndS1Id(String ssiId, String id, String inOrOut) throws BusinessException;
	/**
	 * 根据stepFlg和机型查询CusEdrAdr定义的参数
	 * @param step
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public List<CusEdrAdr> getCusStepList(String step,String modelSeriesId) throws BusinessException;
	/**
	 * 根据SSiId和内外查询Sy
	 * @param ssiId
	 * @param inOrOut
	 * @return
	 * @throws BusinessException
	 */
	public List getSyBySsiId(String ssiId, Integer inOrOut) throws BusinessException;
	/**
	 * 根据SsiId和是否为金属以及内/外部查询S1
	 * @param sssiId
	 * @param isMetal
	 * @param inOrOut
	 * @return
	 * @throws BusinessException
	 */
	public List getS1(String sssiId, int isMetal, int inOrOut) throws BusinessException;
}
