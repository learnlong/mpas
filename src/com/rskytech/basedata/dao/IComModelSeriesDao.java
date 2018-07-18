package com.rskytech.basedata.dao;

import java.util.List;

import com.richong.arch.bo.BusinessException;
import com.richong.arch.dao.IDAO;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;

public interface IComModelSeriesDao extends IDAO {

	/**
	 * 查询机型数据
	 * @param msCode 机型编号关键字
	 * @param msName 机型名称关键字
	 * @param page 翻页参数
	 * @return 机型列表
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public List<ComModelSeries> getComModelSeriesList(String msCode, String msName, Page page) throws BusinessException;
	
	/**
	 * 查询非当前机型ID的其他机型编号记录
	 * @param msCode 机型编号
	 * @param msId 当前机型ID（如果机型存在的话）
	 * @return 机型列表
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public List<ComModelSeries> getModelSeriesByCodeNotNow(String msCode, String msId) throws BusinessException;
	
	/**
	 * 查询默认机型
	 * @return 机型列表
	 * @throws BusinessException
	 * @author zhangjianmin
	 */
	public List<ComModelSeries> getDefaultMs() throws BusinessException;
	
	/**
	 * 删除机型数据
	 * @param modelSeriesId 机型ID
	 * @return boolean
	 * @author zhangjianmin
	 */
	public boolean deleteModelSeries(String modelSeriesId) throws BusinessException;
	
	/**
	 * 复制自定义机型数据到当前机型
	 * @param msId
	 * @param userId
	 * @return
	 */
	public boolean copyDefaultCustomData(String msId, String userId);
}
