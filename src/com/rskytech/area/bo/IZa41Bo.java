package com.rskytech.area.bo;

import java.util.HashMap;

import net.sf.json.JSONObject;

import com.richong.arch.bo.IBo;
import com.rskytech.pojo.Za41;

public interface IZa41Bo extends IBo {

	/**
	 * 通过区域主表ID，查询ZA41数据
	 * @param zaId 区域主表ID
	 * @return Za41
	 * @author zhangjianmin
	 */
	@SuppressWarnings("unchecked")
	public HashMap loadZa41(String zaId);
	
	/**
	 * 保存ZA41的数据
	 * @param userId 用户ID
	 * @param zaId 区域主表ID
	 * @param za41 za41表的Id
	 * @author zhangjianmin
	 */
	public JSONObject saveZa41(String userId, String zaId, Za41 za41);
}
