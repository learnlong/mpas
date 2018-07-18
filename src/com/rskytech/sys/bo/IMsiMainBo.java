package com.rskytech.sys.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.MMain;
public interface IMsiMainBo extends IBo {
	/**
	 * 根据机型查询MSI
	 * @param ataId
	 * @return MSelect
	 * @throws BusinessException
	 * @author chendexu
	 * createdate 2012-08-23
	 */
	public List<MMain> getMMainByStatus(String modelSeriesId)throws BusinessException;

	/**
	 * 根据ataID和机型ID查询MMain
	 * @param ataId
	 * @param modelSeriesId
	 * @return
	 */
	public MMain getMMainByAtaIdAndModelSeries(String ataId, String modelSeriesId);
		/**
	 * 查询所有MSI
	 * @param modelSeriesId
	 * @return
	 * @throws BusinessException
	 */
	public List<Object[]> getMSIAll(String modelSeriesId)throws BusinessException;

}
