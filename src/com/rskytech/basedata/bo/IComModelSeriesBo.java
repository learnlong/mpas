package com.rskytech.basedata.bo;

import java.util.HashMap;
import java.util.List;

import com.richong.arch.bo.IBo;
import com.richong.arch.web.Page;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;

public interface IComModelSeriesBo extends IBo {

	/**
	 * 获取机型数据的显示LIST
	 * @param msCode 机型编号关键字
	 * @param msName 机型名称关键字
	 * @param page 翻页参数
	 * @return 机型显示信息列表
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap> loadModelSeriesList(String msCode, String msName, Page page);
	
	/**
	 * 验证机型编号是否重复
	 * @param msCode 需要验证的编号
	 * @param msId 当前机型ID（如果机型存在的话）
	 * @return TURE：不重复；FALSE：重复
	 * @author zhangjianmin
	 */
	public boolean checkModelSeries(String msCode, String msId);
	
	/**
	 * 新增和修改机型操作
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param jsonData 需要操作的数据集合
	 * @return success：成功；exits：重复机型；nowMs：不能修改当前机型
	 * @author zhangjianmin
	 */
	public String newOrUpdateMs(ComUser user, ComModelSeries ms, String jsonData);
	
	/**
	 * 删除机型数据
	 * @param modelSeriesId 机型ID
	 * @return boolean
	 * @author zhangjianmin
	 */
	public boolean deleteModelSeries(String modelSeriesId);
	
	/**
	 * 默认机型
	 * @param user 用户实例
	 * @param msId 机型ID
	 * @return 成功/失败
	 * @author zhangjianmin
	 */
	public String defaultModelSeries(ComUser user, String msId);
	
	/**
	 * 根据机型编号查询机型
	 * @param msCode
	 * @return
	 */
	public ComModelSeries getMsByMsCode(String msCode);
	/**
	 * 复制自定义机型数据到当前机型
	 * @param msId
	 * @param userId
	 * @return
	 */
	public boolean copyDefaultCustomData(String msId, String userId);
}
