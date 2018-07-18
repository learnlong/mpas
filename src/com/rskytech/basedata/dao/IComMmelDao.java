package com.rskytech.basedata.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.rskytech.pojo.ComMmel;

public interface IComMmelDao extends IDAO {

	/**
	 * 通过机型ID，查询MMEL信息
	 * @param msId 机型ID
	 * @return MMEL信息
	 * @author zhangjianmin
	 */
	public List<ComMmel> loadMmelList(String msId) throws BusinessException;
	
	/**
	 * 验证MMEL编号是否重复
	 * @param msId 当前机型ID
	 * @param mmelId 当前需要变更的MMEL_ID
	 * @param mmelCode 当前需要变更的编号
	 * @return true：重复；false：不重复
	 * @author zhangjianmin
	 */
	public boolean checkMmel(String msId, String mmelId, String mmelCode) throws BusinessException;
}
