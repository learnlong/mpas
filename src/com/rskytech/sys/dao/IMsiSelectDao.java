package com.rskytech.sys.dao;



import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.MSelect;
public interface IMsiSelectDao extends IDAO {
	/**
	 * 根据机型查询MSelect
	 * @param modelSeriesId
	 * @return
	 */
	public List<MSelect> getListMSelectByModelSeriesId(String modelSeriesId,String code,String isNoMsi)throws BusinessException;
	/**
	 * 根据ATAID查询对应的MSI选择表中数据
	 * @param ataId
	 * @return MSelect
	 * @throws BusinessException
	 */
	public List<MSelect> getMSelectByataId(String ataId,String comModelSeriesId)throws BusinessException;
	/**
	 * 根据ataId查询具有权限的ata数据
	 * @param ataId
	 * @param modelSeriesId
	 * @param userId
	 * @return 
	 */
	public List<Object[]> findAllAtaByAtaId(String ataId,String modelSeriesId,String userId);
	
	/**
	 *根据ata得到上级ata
	 * @return 
	 */
	public ComAta getSupAtaByAta(String ataId,String comModelSeriesId);
	
	/**
	 *根据ata查询MSI选择是否符合最高管理层规则，如果是则返回对象
	 * @return 
	 */
	public List<Object[]> getHighLevelByAta(String ataId,String comModelSeriesId);
}
