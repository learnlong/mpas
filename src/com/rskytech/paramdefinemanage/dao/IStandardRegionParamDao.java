package com.rskytech.paramdefinemanage.dao;

import java.util.List;

import com.richong.arch.dao.IDAO;

public interface IStandardRegionParamDao  extends IDAO {

	/**
	 * 查询za5主矩阵的各个叶节点下的级别节点是否一样多
	 * @param modelSeriesId 机型编号
	 * @param vaildFlg 有效区分
	 * @return 
	 */
	public List getLevelCount(String modelSeriesId,Integer vaildFlg); 
	public List getAllLeaf(Integer rowNum,String itemZa5Id,String modelSeriesId);
	
	
}
