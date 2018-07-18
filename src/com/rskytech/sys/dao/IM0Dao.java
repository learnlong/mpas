package com.rskytech.sys.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.M0;

public interface IM0Dao extends IDAO {
	/**
	 * 查询最高可管理层是当前MSI的MSI及子ATA
	 * @param msiId   系统Main的Id
	 * @return
	 */
	public List<M0> getMsiATAListByMsiId(String msiId)throws BusinessException;
}
