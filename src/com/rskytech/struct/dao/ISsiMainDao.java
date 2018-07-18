package com.rskytech.struct.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.S1;
import com.rskytech.pojo.SMain;
import com.rskytech.pojo.SStep;

public interface ISsiMainDao extends IDAO {
	/**
	 * 根据机型id查询所有Ssi
	 * @param modelSeriesId
	 * @return
	 */
	public  List<Object[]> getSSsiListByModelSeriesId(String modelSeriesId);
	/**
	 * 根据SsiId查询S1
	 */
	public List<S1> getS1ListBySsiId(String ssiId);
	/**
	 * 根据是SsiId查询Sstep
	 * 
	 */
	public List<SStep> getSStepListBySsiId(String ssiId);
	
	
	/**
	 * 根据parentAtaId ,机型Id，用户Id查询具有分析权限的且父节点为AtaId的SMain数据
	 * @param ataId
	 * @param modelSeriesId
	 * @param userId
	 * @return
	 */
	public List<SMain> getSMainListByParentAtaId(String parentAtaId,String modelSeriesId,String userId );
}
