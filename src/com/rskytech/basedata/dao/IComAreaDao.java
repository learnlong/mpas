package com.rskytech.basedata.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.DAOException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComAreaDetail;

public interface IComAreaDao extends IDAO {

	/**
	 * 通过当前区域ID，查询其下一级区域信息
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @return 下一级区域的显示信息
	 * @author zhangjianmin
	 */
	public List<ComArea> loadChildArea(String msId, String areaId) throws BusinessException;
	
	/**
	 * 通过当前区域ID，查询其下一级区域信息
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @param page 翻页参数
	 * @return 下一级区域的显示信息
	 * @author zhangjianmin
	 */
	public List<ComArea> loadChildArea(String msId, String areaId, Page page) throws BusinessException;
	
	/**
	 * 通过当前区域ID，查询其明细信息
	 * @param areaId 当前区域ID
	 * @return 区域明细信息LIST
	 * @author zhangjianmin
	 */
	public List<ComAreaDetail> loadAreaDetail(String areaId) throws BusinessException;
	
	public List getAreasByIds(String ids) throws DAOException ;
	
	/**
	 * 验证区域编号是否重复
	 * @param msId 当前机型ID
	 * @param areaId 当前需要变更的区域ID
	 * @param areaCode 当前需要变更的编号
	 * @return true：重复；false：不重复
	 * @author zhangjianmin
	 */
	public boolean checkArea(String msId, String areaId, String areaCode) throws BusinessException;
	
	/**
	 * 通过当前区域ID，查询自己本身及其以下的所有子区域
	 * @param msId 当前机型ID
	 * @param areaId 当前区域ID
	 * @return 自己本身及其以下的所有子区域
	 * @author zhangjianmin
	 */
	public List<Object> getSelfAndChildArea(String msId, String areaId);
}
