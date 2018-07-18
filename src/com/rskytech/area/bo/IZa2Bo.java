package com.rskytech.area.bo;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.Za2;

public interface IZa2Bo extends IBo{

	/**
	 * 通过区域主表ID，查询ZA2
	 * @param zaId 区域主表ID
	 * @return Za2
	 * @author zhangjianmin
	 */
	public Za2 getZa2ByZaId(String zaId);
	
	/**
	 * 保存ZA2的数据
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param za2Id za2表的Id
	 * @param position 内部/外部
	 * @param picContent 图文混排
	 * @author zhangjianmin
	 */
	public JSONObject saveZa2(String userId, String zaId, String za2Id, Integer position, String picContent);
}
