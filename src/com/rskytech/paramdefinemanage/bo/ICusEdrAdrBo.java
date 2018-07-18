package com.rskytech.paramdefinemanage.bo;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.bo.IBo;
import com.rskytech.pojo.CusEdrAdr;

public interface ICusEdrAdrBo  extends IBo{

	/**
	 * 根据机型、分析步骤查询EDR、ADR选择表的数据
	 * @param modelSeriesId 机型系列ID
	 * @param stepFlg 分析步骤
	 * @return 当前机型分析步骤的选择表数据List
	 * @throws BusinessException
	 */
	public List<CusEdrAdr> getCusEdrAdrList(String  modelSeriesId,String stepFlg)throws BusinessException;
	
	
	
	
	
}
