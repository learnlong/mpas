package com.rskytech.basedata.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComVendor;

public interface IComVendorDao extends IDAO {

	/**
	 * 通过机型ID，查询供应商信息
	 * @param msId 机型ID
	 * @return 供应商信息
	 * @author zhangjianmin
	 */
	public List<ComVendor> loadVendorList(String msId) throws BusinessException;
	
	/**
	 * 验证供应商编号是否重复
	 * @param msId 当前机型ID
	 * @param vendorId 当前需要变更的供应商ID
	 * @param vendorCode 当前需要变更的编号
	 * @return true：重复；false：不重复
	 * @author zhangjianmin
	 */
	public boolean checkVendor(String msId, String vendorId, String vendorCode) throws BusinessException;
}
