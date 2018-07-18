package com.rskytech.area.bo;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.ComArea;
import com.rskytech.pojo.ComModelSeries;
import com.rskytech.pojo.ComUser;
import com.rskytech.pojo.Za1;
import com.rskytech.pojo.ZaMain;

public interface IZa1Bo extends IBo {

	/**
	 * 如果通过区域ID，没有查询到ZA_MAIN记录，则插入新记录，最后返回ZA_MAIN记录
	 * @param user 用户实例
	 * @param ms 机型实例
	 * @param area 区域实例
	 * @return zaMain实例
	 * @author zhangjianmin
	 */
	public ZaMain selectZaMain(ComUser user, ComModelSeries ms, ComArea area);
	
	/**
	 * 通过区域ID，查询ZA_MAIN记录
	 * @param ms 机型实例
	 * @param area 区域实例
	 * @return zaMain实例
	 * @author zhangjianmin
	 */
	public ZaMain selectZaMain(ComModelSeries ms, ComArea area);
	
	/**
	 * 通过区域主表ID，查询ZA1
	 * @param zaId 区域主表ID
	 * @param area 区域类
	 * @return Za1
	 * @author zhangjianmin
	 */
	public Za1 selectZa1(String zaId, ComArea area);
	
	/**
	 * 保存ZA1的数据
	 * @param userId 用户ID
	 * @param za1 za1实例
	 * @param oldType 修改前区域的分析类型
	 * @param effectiveness 适用性
	 * @author zhangjianmin
	 */
	public JSONObject saveZa1(String userId, Za1 za1, String oldType, String effectiveness);
	
	/**
	 * 获取当前区域的分析类型
	 * @param za1 za1实例
	 * @return 分析类型
	 * @author zhangjianmin
	 */
	public String getAreaAnalysisType(Za1 za1);
	
	/**
	 * 通过区域主表ID，查询ZA1
	 * @param zaId 区域主表ID
	 * @return Za1
	 * @author zhangjianmin
	 */
	public Za1 getZa1ByZaId(String zaId);
}
