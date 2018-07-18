package com.rskytech.basedata.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.ComVendor;

public interface IComVendorBo extends IBo {

	/**
	 * 通过机型ID，查询供应商信息
	 * @param msId 机型ID
	 * @return 供应商列表
	 * @author zhangjianmin
	 */
	public List<ComVendor> getVendorList(String msId);
	
	/**
	 * 通过机型ID，查询供应商信息
	 * @param msId 机型ID
	 * @return 供应商信息
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> loadVendorList(String msId);
	
	/**
	 * 新增和修改供应商操作
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param jsonData 需要操作的数据集合
	 * @return 
	 * @author zhangjianmin
	 */
	public String newOrUpdateVendor(ComUser user, ComModelSeries ms, String jsonData);
	
	/**
	 * 根据供应商Id查询供应商名称
	 * @param vendorId
	 */
	public String getVendorNameById(String vendorId);
	
	/**
	 * 根据供应商Id查询供应商编号
	 * @param vendorId
	 */
	public String getVendorCodeById(String vendorId);
	
}
