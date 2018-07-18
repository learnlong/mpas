package com.rskytech.sys.bo;

import java.util.ArrayList;
import java.util.List;


import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComAta;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.MSelect;

public interface IMsiSelectBo extends IBo{
	/**
	 * 根据ATAID查询对应的MSI选择表中数据
	 * @param ataId
	 * @param modelSeriesId
	 * @return MSelect
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-15
	 */
	public List<MSelect>  getMSelectByataId(String ataId,String comModelSeriesId)throws BusinessException;
	/**
	 * 根据ATAID查询自身和所有子节点ata数据
	 * @param ataId
	 * @param modelSeriesId
	 * @return list<Object[]>
	 * @throws BusinessException
	 */
	public List<Object[]> getAtaAndfindAllChildByAtaId(String ataId,String modelSeriesId,String userId)throws BusinessException;
	/**
	 * 根据机型查询MSelect
	 * @param modelSeriesId
	 * @return
	 */
	public List<MSelect> getListMSelectByModelSeriesId(String modelSeriesId,String code,String isNoMsi)throws BusinessException;
	/**
	 * 保存MSelect数据
	 * @return 
	 */
	public ArrayList<String> savaMSelect(String jsonData,ComModelSeries comModelSeries,String userId); 
	
	/**
	 * 检验最高可管理层是否有效
	 * @return 
	 */
	public ArrayList<String> checkoutHighLevel(String jsonData,String comModelSeriesId);
	
}
